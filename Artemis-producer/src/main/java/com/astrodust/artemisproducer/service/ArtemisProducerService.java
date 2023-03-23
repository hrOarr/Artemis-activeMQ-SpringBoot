package com.astrodust.artemisproducer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ArtemisProducerService {

    @Value("${info.artemis.producer.to.consumer.queue}")
    private String queue;
    private final JmsTemplate jmsTemplate;

    public ArtemisProducerService(JmsTemplate jmsTemplate){
        this.jmsTemplate = jmsTemplate;
    }

    @Retryable(value = Exception.class, maxAttempts = 2, backoff = @Backoff(delay = 1000))
    public void send(String message) throws Exception{
        log.info("--------------Sending--------------");
        jmsTemplate.convertAndSend(queue, message);
        log.info("---------------Message Sent---------------");
    }

    @Recover
    void recover(String message){
        log.info("Recover Method: {}", message);
    }
}
