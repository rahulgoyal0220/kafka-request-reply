package io.project.resources;

import io.project.model.Employee;

import java.util.concurrent.ExecutionException;
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
public class InfoController {

    @Autowired
    private ReplyingKafkaTemplate<String, Employee, Employee> kafkaTemplate;

    @Value("${kafka.topic.request-topic}")
    private String requestTopic;

    @Value("${kafka.topic.reply-topic}")
    private String replyTopic;

    @ResponseBody
    @PostMapping(value = "/employee", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Employee post(@RequestBody Employee request) throws InterruptedException, ExecutionException {
        // create producer record
        ProducerRecord<String, Employee> record = new ProducerRecord<>(requestTopic, request);
        // set reply topic in header
        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, replyTopic.getBytes()));
        // post in kafka topic
        RequestReplyFuture<String, Employee, Employee> sendAndReceive = kafkaTemplate.sendAndReceive(record);

        // confirm if producer produced successfully
        SendResult<String, Employee> sendResult = sendAndReceive.getSendFuture().get();

        //print all headers
        sendResult.getProducerRecord().headers().forEach(header -> System.out.println(header.key() + ":" + header.value().toString()));

        // get consumer record
        ConsumerRecord<String, Employee> consumerRecord = sendAndReceive.get();
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
