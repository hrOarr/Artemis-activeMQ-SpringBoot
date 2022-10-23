package com.astrodust.artemisproducer.config;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

@Configuration
public class JmsConfig {
//    @Value("${spring.artemis.broker-url}")
//    private String URL;
//    @Value("${spring.artemis.user}")
//    private String USERNAME;
//    @Value("${spring.artemis.password}")
//    private String PASSWORD;
//
//    @Bean(name = "artemisActiveMQConnectionFactory")
//    public ConnectionFactory connectionFactory() throws JMSException {
//        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
//        connectionFactory.setBrokerURL(URL);
//        connectionFactory.setUser(USERNAME);
//        connectionFactory.setPassword(PASSWORD);
//        return connectionFactory;
//    }
//
//    @Bean
//    public CachingConnectionFactory cachingConnectionFactory() throws JMSException {
//        CachingConnectionFactory cachingConnectionFactory =
//                new CachingConnectionFactory(connectionFactory());
//        cachingConnectionFactory.setSessionCacheSize(10);
//        return cachingConnectionFactory;
//    }
//
//    @Bean
//    public JmsTemplate jmsTemplate() throws JMSException {
//        JmsTemplate template = new JmsTemplate();
//        template.setConnectionFactory(connectionFactory());
//        template.setPubSubDomain(false); // false for queue
//        return template;
//    }
//
//    @Bean(name = "customJmsListenerContainerFactory")
//    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() throws JMSException {
//        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory());
//        factory.setPubSubDomain(false); // false for queue
//        factory.setConcurrency("5-10");
//        return factory;
//    }
}
