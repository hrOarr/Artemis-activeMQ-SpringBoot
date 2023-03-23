package com.astrodust.artemisproducer.controller;

import com.astrodust.artemisproducer.entity.Event;
import com.astrodust.artemisproducer.enums.EventStatus;
import com.astrodust.artemisproducer.service.EventServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Random;

@RestController
@RequestMapping(value = "/api/v1/events")
public class EventController {
    private final EventServiceImpl eventServiceImpl;

    public EventController(EventServiceImpl eventServiceImpl){
        this.eventServiceImpl = eventServiceImpl;
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> generateEvent(){
        Event event = new Event();
        LocalDateTime currentDate = LocalDateTime.now();
        event.setKey(String.valueOf(new Random().nextInt(5)));
        event.setStatus(EventStatus.QUEUED);
        event.setCreatedTime(currentDate);
        event.setModifiedTime(currentDate);
        event = eventServiceImpl.generateEvent(event);
        return ResponseEntity.status(HttpStatus.OK).body(event);
    }
}
