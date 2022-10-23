package com.astrodust.artemisconsumer.service;

import com.astrodust.artemisconsumer.entity.Event;
import com.astrodust.artemisconsumer.enums.EventEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.support.converter.SimpleMessageConverter;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class ArtemisConsumerService {

    @Autowired
    private EventService eventService;

    @Autowired
    private ObjectMapper objectMapper;

    private static SimpleMessageConverter simpleMessageConverter = new SimpleMessageConverter();

    @JmsListener(destination = "${info.artemis.producer.to.consumer.queue}", concurrency = "5-10")
    @SendTo("${info.artemis.consumer.to.producer.queue}")
    public String receiveMessage(Message message){
        log.info("SoA:: Received----- " + message);
        try {
            if(message instanceof TextMessage){
                log.info("SoA:: Started------------");
                String json = (String) simpleMessageConverter.fromMessage(message);
                message.acknowledge();
                Map<String, Object> map = objectMapper.readValue(json, HashMap.class);
                if(map.containsKey("id") && map.containsKey("key")) {

                    String producerId = (String) map.get("id");
                    Event event = new Event();
                    event.setKey((String) map.get("key"));
                    event.setProducerId(producerId);
                    event.setStatus(EventEnum.PROCESSING);
                    event.setCreatedTime(new Date());
                    event.setModifiedTime(new Date());
                    eventService.saveOrUpdate(event);

                    // sleep for 5 seconds
                    Thread.sleep(5000);

                    event.setStatus(EventEnum.SUCCESS);
                    event.setModifiedTime(new Date());
                    eventService.saveOrUpdate(event);
                    log.info("SoA:: End--------------");
                    return objectMapper.writeValueAsString(event);
                }
            }
            else{
                log.info("SoA:: message must be type of TextMessage!");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return ""; // empty
    }
}
