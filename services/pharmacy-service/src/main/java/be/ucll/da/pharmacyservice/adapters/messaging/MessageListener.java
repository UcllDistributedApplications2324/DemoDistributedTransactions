package be.ucll.da.pharmacyservice.adapters.messaging;

import be.ucll.da.pharmacyservice.api.messaging.model.MedicationReservedEvent;
import be.ucll.da.pharmacyservice.api.messaging.model.ReserveMedicationCommand;
import be.ucll.da.pharmacyservice.domain.PharmacyService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Transactional
public class MessageListener {

    private final static Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);

    private PharmacyService pharmacyService;
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public MessageListener(PharmacyService pharmacyService, RabbitTemplate rabbitTemplate) {
        this.pharmacyService = pharmacyService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = {"q.pharmacy-service.reserve-medication"})
    public void onBookRoom(ReserveMedicationCommand command) {
        LOGGER.info("Received command: " + command);

        MedicationReservedEvent event = new MedicationReservedEvent();
        event.medicationId(command.getMedicationId());
        event.setPrescriptionId(command.getPrescriptionId());
        event.availableFrom(pharmacyService.reserveMedication(command.getMedicationId()));

        LOGGER.info("Sending event: " + event);
        this.rabbitTemplate.convertAndSend("x.medication-reserved", "", event);
    }
}
