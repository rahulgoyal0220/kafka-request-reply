package io.project.services;

import io.project.repo.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import io.project.model.Employee;


@Component
public class ReplyingKafkaConsumer {

    private Logger logger = LoggerFactory.getLogger(ReplyingKafkaConsumer.class.getName());

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @KafkaListener(topics = "${kafka.topic.request-topic}")
    @SendTo
    public Employee listen(@Payload Employee request, @Header(KafkaHeaders.CORRELATION_ID) String id) throws InterruptedException {
        logger.info("Request data: "+ request.getUserId());
        logger.info("Correlation id: "+ id);
        Employee response = employeeService.getEmployeeById(request.getUserId());
        if(response == null){
//            response = employeeRepository.findAll();
        }else {
            logger.info("Employee response " + response.toString());
        }
        return response;
    }

}
