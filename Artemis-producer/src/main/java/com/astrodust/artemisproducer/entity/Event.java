package com.astrodust.artemisproducer.entity;

import com.astrodust.artemisproducer.enums.EventStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Document(collection = "events")
public class Event {
    @Id
    private String id;
    private String key;
    private EventStatus status;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
}
