package com.astrodust.artemisconsumer.repository;

import com.astrodust.artemisconsumer.entity.Event;
import com.astrodust.artemisconsumer.enums.EventStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {
    Optional<Event> findByKeyAndStatus(@Param("key") String key, @Param("status")EventStatus status);
}
