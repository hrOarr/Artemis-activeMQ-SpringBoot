package com.astrodust.artemisproducer.service;

import com.astrodust.artemisproducer.entity.Event;

import java.util.Optional;

public interface EventService {
    Event generateEvent(Event event);
    Event saveOrUpdate(Event event);
    Optional<Event> findById(String id);
}
