package com.astrodust.artemisproducer.service;

import com.astrodust.artemisproducer.entity.Event;
import com.astrodust.artemisproducer.enums.EventStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.support.converter.SimpleMessageConverter;
import org.springframework.stereotype.Service;

import javax.jms.Message;
import javax.jms.TextMessage;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class ArtemisConsumerService {
    private final ObjectMapper objectMapper;
    private final EventService eventService;
    private final ArtemisProducerService producerService;
    private static SimpleMessageConverter simpleMessageConverter = new SimpleMessageConverter();

    public ArtemisConsumerService(ObjectMapper objectMapper, EventService eventService, ArtemisProducerService artemisProducerService){
        this.objectMapper = objectMapper;
        this.eventService = eventService;
        this.producerService = artemisProducerService;
    }

    @JmsListener(destination = "${info.artemis.consumer.to.producer.queue}", concurrency = "5-10")
    public void receiveMessage(Message message){
        log.info("--------------------Received Message--------------------");
        try {
            Thread.sleep(5000); // to test concurrency
            if(message instanceof TextMessage){
                log.info("-------------Start Event Processing------------");
                String json = (String) simpleMessageConverter.fromMessage(message);
                message.acknowledge();
                Map<String, Object> map = objectMapper.readValue(json, HashMap.class);
                String producerId = (String) map.get("producerId");
                EventStatus status = EventStatus.valueOf((String) map.get("status"));
                Event event = eventService.findById(producerId).orElse(null);
                if(event!=null){
                    log.info("Event Found with key and status: {}, {}", event.getKey(), status.name());
                    if(status.name().equalsIgnoreCase(EventStatus.FAILED.name())){
                        event.setStatus(EventStatus.QUEUED);
                        event.setModifiedTime(LocalDateTime.now());
                        eventService.saveOrUpdate(event);
                        String jsonEvent = objectMapper.writeValueAsString(event);
                        producerService.send(jsonEvent);
                        log.info("Re-send Event for key: {}", event.getKey());
                    }
                    else {
                        event.setStatus(status);
                        event.setModifiedTime(LocalDateTime.now());
                        eventService.saveOrUpdate(event);
                    }
                }
                else{
                    log.info("Event is not found with Id: {}", producerId);
                }
            }
            else{
                log.info("Message must be type of TextMessage");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            log.error("Error on Parsing Message: {}", e.getMessage());
        }
        log.info("------------- End of Event Processing ----------------");
    }
}
