package com.astrodust.artemisproducer.service;

import com.astrodust.artemisproducer.entity.Event;
import com.astrodust.artemisproducer.repository.EventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventServiceImpl implements EventService{
    private final EventRepository eventRepository;
    private final ArtemisProducerService artemisProducerService;
    private final ObjectMapper objectMapper;

    public EventServiceImpl(EventRepository eventRepository, ArtemisProducerService producerService, ObjectMapper objectMapper){
        this.eventRepository = eventRepository;
        this.artemisProducerService = producerService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Event generateEvent(Event event) {
        Event savedEvent = saveOrUpdate(event);
        try{
            String jsonEvent = objectMapper.writeValueAsString(event);
            artemisProducerService.send(jsonEvent);
        } catch (Exception e) {
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
