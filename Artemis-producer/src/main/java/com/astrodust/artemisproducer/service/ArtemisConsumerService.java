package com.astrodust.artemisproducer.service;

import com.astrodust.artemisproducer.entity.Event;
import com.astrodust.artemisproducer.enums.EventEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.support.converter.SimpleMessageConverter;
import org.springframework.stereotype.Service;

import javax.jms.Message;
import javax.jms.TextMessage;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class ArtemisConsumerService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventService eventService;

    private static SimpleMessageConverter simpleMessageConverter = new SimpleMessageConverter();

    @JmsListener(destination = "${info.artemis.consumer.to.producer.queue}", concurrency = "5-10")
    public void receiveMessage(Message message){
        log.info("SoA:: Received----- " + message);
        try {
            Thread.sleep(5000); // to test concurrency
            if(message instanceof TextMessage){
                log.info("SoA:: Start------------");
                String json = (String) simpleMessageConverter.fromMessage(message);
                message.acknowledge();
                Map<String, Object> map = objectMapper.readValue(json, HashMap.class);
                if(map.containsKey("producerId") && map.containsKey("status")){
                    String producerId = (String) map.get("producerId");
                    EventEnum status = EventEnum.valueOf((String) map.get("status"));
                    Event event = eventService.findById(producerId).orElse(null);
                    if(event!=null){
                        log.info("SoA:: Event Found!");
                        event.setStatus(status);
                        event.setModifiedTime(new Date());
                        eventService.saveOrUpdate(event);
                    }
                }
            }
            else{
                log.info("SoA:: message must be type of TextMessage!");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
