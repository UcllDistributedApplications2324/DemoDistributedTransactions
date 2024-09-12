package be.ucll.da.patientservice.domain.prescription;

import be.ucll.da.patientservice.api.model.ApiPrescription;
import be.ucll.da.patientservice.domain.patient.Patient;
import be.ucll.da.patientservice.domain.patient.PatientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PrescriptionService {

    private PatientRepository patientRepository;
    private PrescriptionRepository prescriptionRepository;
    private PrescriptionRequestSaga saga;

    @Autowired
    public PrescriptionService(PatientRepository patientRepository, PrescriptionRepository repository, PrescriptionRequestSaga saga) {
        this.patientRepository = patientRepository;
        this.prescriptionRepository = repository;
        this.saga = saga;
    }

    public String registerRequest(Integer patientId, ApiPrescription request) {
        var prescription = new Prescription(patientId, request.getMedicationId());

        if (patientRepository.findById(patientId.longValue()).isEmpty()) {
            patientRepository.save(new Patient());
        }

        prescription = prescriptionRepository.save(prescription);
        saga.executeSaga(prescription);

        return prescription.getPrescriptionNumber();
    }
}
