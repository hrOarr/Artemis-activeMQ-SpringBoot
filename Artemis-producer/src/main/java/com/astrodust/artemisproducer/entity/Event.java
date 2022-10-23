package com.astrodust.artemisproducer.entity;

import com.astrodust.artemisproducer.enums.EventEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@Document(collection = "events")
public class Event {
    @Id
    private String id;
    private String key;
    private EventEnum status;
    private Date createdTime;
    private Date modifiedTime;
}
