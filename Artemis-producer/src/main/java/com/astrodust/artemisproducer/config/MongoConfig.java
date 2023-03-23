package com.astrodust.artemisproducer.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.host}")
    private String HOST;

    @Value("${spring.data.mongodb.port}")
    private String PORT;

    @Value("${spring.data.mongodb.database}")
    private String DBNAME;

//    @Bean
//    public MongoClient mongo() {
//        ConnectionString connectionString = new ConnectionString("mongodb://"+HOST+":"+PORT+"/"+DBNAME);
//        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
//                .applyConnectionString(connectionString)
//                .build();
//
//        return MongoClients.create(mongoClientSettings);
//    }
//
//    @Bean(name = "mongoTemplate")
//    public MongoTemplate mongoTemplate() throws Exception {
//        return new MongoTemplate(mongo(), DBNAME);
//    }
}
