package com.astrodust.artemisproducer.service;

import com.astrodust.artemisproducer.entity.Event;
import com.astrodust.artemisproducer.repository.EventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventServiceImpl implements EventService{

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ArtemisProducerService artemisProducerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Event generateEvent(Event event) {
        Event savedEvent = saveOrUpdate(event);
        try{
            String jsonEvent = objectMapper.writeValueAsString(event);
            artemisProducerService.send(jsonEvent);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return savedEvent;
    }

    @Override
    public Event saveOrUpdate(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Optional<Event> findById(String id) {
        return eventRepository.findById(id);
    }
}
