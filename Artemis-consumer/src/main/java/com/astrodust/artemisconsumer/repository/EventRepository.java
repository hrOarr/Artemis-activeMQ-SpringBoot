package com.astrodust.artemisconsumer.repository;

import com.astrodust.artemisconsumer.entity.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {
}
