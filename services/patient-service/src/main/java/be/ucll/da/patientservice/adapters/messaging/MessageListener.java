package be.ucll.da.patientservice.adapters.messaging;

import be.ucll.da.patientservice.client.accounting.api.model.PatientAccountCreatedEvent;
import be.ucll.da.patientservice.client.pharmacy.api.model.MedicationReservedEvent;
import be.ucll.da.patientservice.domain.prescription.PrescriptionRequestSaga;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Transactional
public class MessageListener {

    private final static Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);

    private final PrescriptionRequestSaga saga;

    @Autowired
    public MessageListener(PrescriptionRequestSaga saga) {
        this.saga = saga;
    }

    @RabbitListener(queues = {"q.medication-reserved.patient-service"})
    public void onMedicationReserved(MedicationReservedEvent event) {
        LOGGER.info("Receiving event: " + event);
        this.saga.executeSaga(event);
    }

    @RabbitListener(queues = {"q.account-openings.patient-service"}, concurrency = "5-5") // 5 simultane listeners
    public void onAccountOpened(PatientAccountCreatedEvent event) {
        LOGGER.info("Receiving event: " + event);
        this.saga.executeSaga(event);
    }
}