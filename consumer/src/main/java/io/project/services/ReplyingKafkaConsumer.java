package io.project.services;

import io.project.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import io.project.model.User;


@Component
public class ReplyingKafkaConsumer {

    private Logger logger = LoggerFactory.getLogger(ReplyingKafkaConsumer.class.getName());

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @KafkaListener(topics = "${kafka.topic.request-topic}")
    @SendTo
    public User listen(@Payload User request, @Header(KafkaHeaders.CORRELATION_ID) String id) throws InterruptedException {
        logger.info("Request data: "+ request.getUserId());
        logger.info("Correlation id: "+ id);

        User response = userService.getUserById(request.getUserId());
        if(response == null){
//            response = employeeRepository.findAll();
        }else {
            logger.info("User response " + response.toString());
        }
        return response;
    }

}
