package org.superbiz.moviefun.albums;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class RabbitMessageController {

    final private RabbitTemplate rabbitTemplate;
    final private String rabbitMq;


    public RabbitMessageController(RabbitTemplate rabbitTemplate,
                                   @Value("${rabbitmq.queue}") String rabbitMq) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitMq = rabbitMq;
    }

    @PostMapping("/rabbit")
    public Map<String, String> publishMessage() {
        String message = "This text message will trigger the consumer";
        rabbitTemplate.convertAndSend(rabbitMq, message);

        Map<String, String> response = new HashMap<>();
        response.put("response", "Hello from Ramona and Pravin");
        return response;
    }


}
