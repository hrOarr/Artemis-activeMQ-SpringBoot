package com.astrodust.artemisproducer.controller;

import com.astrodust.artemisproducer.entity.Event;
import com.astrodust.artemisproducer.enums.EventEnum;
import com.astrodust.artemisproducer.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/event-driven")
public class EventController {

    @Autowired
    @Qualifier("eventServiceImpl")
    private EventService eventService;

    @PostMapping(value = "/")
    public ResponseEntity<?> createEvent(){
        Event event = new Event();
        event.setKey(UUID.randomUUID().toString());
        event.setCreatedTime(new Date());
        event.setStatus(EventEnum.QUEUED);
        event.setModifiedTime(new Date());
        Event saved = eventService.generateEvent(event);
        return ResponseEntity.status(HttpStatus.OK).body(saved);
    }
}
