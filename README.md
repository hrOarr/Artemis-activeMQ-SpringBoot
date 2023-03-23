# Artemis-activeMQ-SpringBoot
### Spring JMS
JMS is a standard API for using MOM (Message Oriented Middleware) in Java. JMS exchanges messages between producers and consumers in a decoupled way.

ActiveMQ is a JMS provider which facilates the use of JMS concepts. Apache ActiveMQ Artemis is an asynchronous messaging system which uses for loosely coupled communication in microservices. 

It supports two main ways - 
- Queue (also known as point-to-point messaging)
- Topic (publish/subscribers messaging)

In this project, I tried to focus on basic usage implementation of ActiveMQ Artemis in Spring Boot. Two different applications (one is producer, another is consumer) are made to demonstrate the flow of Artemis with JMS.
