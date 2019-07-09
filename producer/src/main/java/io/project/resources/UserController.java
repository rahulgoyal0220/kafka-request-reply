package io.project.resources;

import java.util.concurrent.ExecutionException;

import io.project.model.User;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private ReplyingKafkaTemplate<String, User, User> kafkaTemplate;

    @Value("${kafka.topic.request-topic}")
    private String requestTopic;

    @Value("${kafka.topic.reply-topic}")
    private String replyTopic;

    @ResponseBody
    @PostMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public User getUser(@RequestBody User request) throws InterruptedException, ExecutionException {
        // create producer record
        ProducerRecord<String, User> record = new ProducerRecord<>(requestTopic, request);
        // set reply topic in header
        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, replyTopic.getBytes()));
        // post in kafka topic
        RequestReplyFuture<String, User, User> sendAndReceive = kafkaTemplate.sendAndReceive(record);

        // confirm if producer produced successfully
        SendResult<String, User> sendResult = sendAndReceive.getSendFuture().get();

        //print all headers
        sendResult.getProducerRecord().headers().forEach(header -> System.out.println(header.key() + ":" + header.value().toString()));

        // get consumer record
        ConsumerRecord<String, User> consumerRecord = sendAndReceive.get();
        // return consumer value
        return consumerRecord.value();
    }

  /*  @GetMapping(value = "/employee/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Employee getEmployee(@PathVariable("id") Integer userId) throws InterruptedException, ExecutionException {
        // get Employee record
        ProducerRecord<String, Integer> record = new ProducerRecord<>(requestTopic, userId);
        // set reply topic in header
        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, replyTopic.getBytes()));
        // post in kafka topic
       RequestReplyFuture<String, Integer, Employee> sendAndReceive = kafkaTemplate.sendAndReceive(record);

        // confirm if employee produced successfully
        SendResult<String, Integer> sendResult = sendAndReceive.getSendFuture().get();

        //print all headers
        sendResult.getProducerRecord().headers().forEach(header -> System.out.println(header.key() + ":" + header.value().toString()));

        // get employee record
        ConsumerRecord<String, Employee> consumerRecord = sendAndReceive.get();
        // return employee value
        return consumerRecord.value();
    }*/

}
