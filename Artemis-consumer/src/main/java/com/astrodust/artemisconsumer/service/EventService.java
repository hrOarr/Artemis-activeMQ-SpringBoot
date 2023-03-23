package com.astrodust.artemisconsumer.service;

import com.astrodust.artemisconsumer.entity.Event;
import com.astrodust.artemisconsumer.enums.EventStatus;
import com.astrodust.artemisconsumer.repository.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository){
        this.eventRepository = eventRepository;
    }

    public Event saveOrUpdate(Event event){
        return eventRepository.save(event);
    }
    public Event findByKeyAndStatus(String key, EventStatus status){
        return eventRepository.findByKeyAndStatus(key, status).orElse(null);
    }
}
