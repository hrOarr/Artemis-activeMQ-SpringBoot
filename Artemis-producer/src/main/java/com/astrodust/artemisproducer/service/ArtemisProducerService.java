package com.astrodust.artemisproducer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ArtemisProducerService {

    @Value("${info.artemis.producer.to.consumer.queue}")
    private String queue;

    @Autowired
    private JmsTemplate jmsTemplate;

    public void send(String message){
        log.info("SoA:: Sending---------");
        jmsTemplate.convertAndSend(queue, message);
        log.info("SoA:: Sent------------");
    }
}
