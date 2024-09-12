package be.ucll.da.patientservice.adapters.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    public Jackson2JsonMessageConverter converter() {
        ObjectMapper mapper =
                new ObjectMapper()
                        .registerModule(new ParameterNamesModule())
                        .registerModule(new Jdk8Module())
                        .registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(new StdDateFormat());

        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(Jackson2JsonMessageConverter converter, CachingConnectionFactory cachingConnectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        rabbitTemplate.setMessageConverter(converter);
        return rabbitTemplate;
    }

    @Bean
    public Declarables createOpenAccountQueue(){
        return new Declarables(new Queue("q.account-service.open-account"));
    }

    @Bean
    public Declarables createAccountOpenedExchange(){
        return new Declarables(
                new FanoutExchange("x.account-openings"),
                new Queue("q.account-openings.patient-service" ),
                new Binding("q.account-openings.patient-service", Binding.DestinationType.QUEUE, "x.account-openings", "account-openings.patient-service", null));
    }

    @Bean
    public Declarables createCloseAccountQueue(){
        return new Declarables(new Queue("q.account-service.close-account"));
    }

    @Bean
    public Declarables createAccountTerminationsExchange(){
        return new Declarables(
                new FanoutExchange("x.account-terminations"),
                new Queue("q.account-terminations.patient-service" ),
                new Binding("q.account-terminations.patient-service", Binding.DestinationType.QUEUE, "x.account-terminations", "account-terminations.patient-service", null));
    }

    @Bean
    public Declarables createReserveMedicationQueue(){
        return new Declarables(new Queue("q.pharmacy-service.reserve-medication"));
    }

    @Bean
    public Declarables createMedicationReservedExchange(){
        return new Declarables(
                new FanoutExchange("x.medication-reserved"),
                new Queue("q.medication-reserved.patient-service" ),
                new Binding("q.medication-reserved.patient-service", Binding.DestinationType.QUEUE, "x.medication-reserved", "medication-reserved.patient-service", null));
    }
}
