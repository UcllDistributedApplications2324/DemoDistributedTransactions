package be.ucll.da.patientservice.domain.prescription;

import be.ucll.da.patientservice.adapters.messaging.RabbitMqMessageSender;
import be.ucll.da.patientservice.client.accounting.api.model.PatientAccountCreatedEvent;
import be.ucll.da.patientservice.client.pharmacy.api.model.MedicationReservedEvent;
import be.ucll.da.patientservice.domain.patient.Patient;
import be.ucll.da.patientservice.domain.patient.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PrescriptionRequestSaga {

    private RabbitMqMessageSender eventSender;
    private PrescriptionRepository prescriptionRepository;
    private PatientRepository patientRepository;

    @Autowired
    public PrescriptionRequestSaga(RabbitMqMessageSender eventSender, PrescriptionRepository prescriptionRepository, PatientRepository patientRepository) {
        this.eventSender = eventSender;
        this.prescriptionRepository = prescriptionRepository;
        this.patientRepository = patientRepository;
    }

    public void executeSaga(Prescription prescription) {
        eventSender.sendReserveMedicationCommand(prescription.getMedicationId(), prescription.getId());
    }

    public void executeSaga(MedicationReservedEvent event) {
        Prescription prescription = prescriptionRepository.findById(Long.valueOf(event.getPrescriptionId())).orElseThrow();
        eventSender.sendOpenAccountCommand(prescription.getPatientId(), prescription.getId());
    }

    public void executeSaga(PatientAccountCreatedEvent event) {
        Prescription prescription = prescriptionRepository.findById(Long.valueOf(event.getPrescriptionId())).orElseThrow();
        Patient patient = patientRepository.findById(prescription.getPatientId()).orElseThrow();

        if (event.getAccountCreated()) {
            prescription.ok();
            patient.setAccountId(event.getAccountId().toString()); // When consuming parallel events, this accountId could be wrong!
        } else {
            // Shame that we paid for the medication
            prescription.noInsurance();
            patient.setAccountId(null);
        }
    }
}
