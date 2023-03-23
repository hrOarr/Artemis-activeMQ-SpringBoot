package com.astrodust.artemisconsumer.service;

import com.astrodust.artemisconsumer.entity.Event;
import com.astrodust.artemisconsumer.enums.EventStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.support.converter.SimpleMessageConverter;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import javax.jms.Message;
import javax.jms.TextMessage;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class ArtemisConsumerService {
    private final EventService eventService;
    private final ObjectMapper objectMapper;
    private static SimpleMessageConverter simpleMessageConverter = new SimpleMessageConverter();
    private final ReentrantLock reentrantLock = new ReentrantLock();

    public ArtemisConsumerService(EventService eventService, ObjectMapper objectMapper){
        this.eventService = eventService;
        this.objectMapper = objectMapper;
    }

    @JmsListener(destination = "${info.artemis.producer.to.consumer.queue}", concurrency = "5-10")
    @SendTo("${info.artemis.consumer.to.producer.queue}")
    public String receiveMessage(Message message){
        log.info("Message Received: {}", message);
        String ret = "";
        try {
            if(message instanceof TextMessage){
                log.info("-------------Processing Started------------");
                String json = (String) simpleMessageConverter.fromMessage(message);
                message.acknowledge();
                Event currentEvent = parseToEvent(json);
                Event existingEvent = findByKeyAndStatus(currentEvent);

                if(currentEvent.getCreatedTime().equals(existingEvent.getCreatedTime())){
                    currentEvent.setStatus(EventStatus.SUCCESS);
                    currentEvent.setModifiedTime(LocalDateTime.now());
                    eventService.saveOrUpdate(currentEvent);
                    log.info("Event Success: {}", currentEvent.getKey());
                }
                else if(currentEvent.getCreatedTime().compareTo(existingEvent.getCreatedTime())>0){
                    // re-send on queue
                    log.info("FAILED Event will be produced again");
                    currentEvent.setStatus(EventStatus.FAILED);
                    currentEvent.setModifiedTime(LocalDateTime.now());
                }
                else{
                    currentEvent.setStatus(EventStatus.REJECTED);
                    currentEvent.setModifiedTime(LocalDateTime.now());
                    eventService.saveOrUpdate(currentEvent);
                    log.info("Event REJECTED: {}", currentEvent.getKey());
                }
                ret = objectMapper.writeValueAsString(currentEvent);
                log.info("----------------Processing End--------------");
            }
            else{
                log.info("Message must be type of TextMessage");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            log.error("Error on Message Parsing: {}", e.getMessage());
        }
        return ret;
    }

    private Event findByKeyAndStatus(Event currentEvent){
        reentrantLock.lock();
        Event existingEvent = null;
        try {
            existingEvent = eventService.findByKeyAndStatus(currentEvent.getKey(), EventStatus.PROCESSING);
            if(existingEvent==null){
                currentEvent.setStatus(EventStatus.PROCESSING);
                existingEvent = eventService.saveOrUpdate(currentEvent);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            reentrantLock.unlock();
        }
        return existingEvent;
    }

    private Event parseToEvent(String json) throws JsonProcessingException {
        Map<String, Object> map = objectMapper.readValue(json, HashMap.class);
        String producerId = (String) map.get("id");
        String key = map.get("key")!=null?(String) map.get("key"):null;
        LocalDateTime currentDate = LocalDateTime.now();
        Event event = new Event();
        event.setKey(key);
        event.setStatus(EventStatus.valueOf((String) map.get("status")));
        event.setProducerId(producerId);
        event.setCreatedTime(currentDate);
        event.setModifiedTime(currentDate);
        return event;
    }
}
