package be.ucll.da.patientservice.adapters.messaging;

import be.ucll.da.patientservice.client.accounting.api.model.ClosePatientAccountCommand;
import be.ucll.da.patientservice.client.accounting.api.model.OpenPatientAccountCommand;
import be.ucll.da.patientservice.client.pharmacy.api.model.ReserveMedicationCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class RabbitMqMessageSender {

    private final static Logger LOGGER = LoggerFactory.getLogger(RabbitMqMessageSender.class);

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMqMessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendOpenAccountCommand(Long patientId, Long prescriptionId) {
        var command = new OpenPatientAccountCommand();
        command.patientId(patientId.intValue());
        command.prescriptionId(prescriptionId.toString());
        sendToQueue("q.account-service.open-account", command);
    }

    public void sendCloseAccountCommand(Long patientId, Long prescriptionId) {
        var command = new ClosePatientAccountCommand();
        command.patientId(patientId.intValue());
        command.prescriptionId(prescriptionId.toString());
        sendToQueue("q.account-service.close-account", command);
    }

    public void sendReserveMedicationCommand(String medicationId, Long prescriptionId) {
        var command = new ReserveMedicationCommand();
        command.medicationId(medicationId);
        command.prescriptionId(prescriptionId.toString());
        sendToQueue("q.pharmacy-service.reserve-medication", command);
    }

    private void sendToQueue(String queue, Object message) {
        LOGGER.info("Sending message: " + message);

        this.rabbitTemplate.convertAndSend(queue, message);
    }
}
