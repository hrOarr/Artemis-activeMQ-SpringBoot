package com.astrodust.artemisconsumer.service;

import com.astrodust.artemisconsumer.entity.Event;
import com.astrodust.artemisconsumer.repository.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public Event saveOrUpdate(Event event){
        return eventRepository.save(event);
    }
}
