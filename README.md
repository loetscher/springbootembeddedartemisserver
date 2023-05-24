# Sample configuration to use embedded Artemis broker with multiple connection factories

This sample demonstrates how to use an embedded artemis broker in a spring boot application with a connection factory bean configured.
The use case is mentioned in https://github.com/spring-projects/spring-boot/issues/7034.

The answer from that ticket:
> In such a scenrio, I recommend that you configure the embedded broker yourself since you have your own JMS configuration anyway.v
It shows how to use the embedded broker if multiple connection factories are used.

A possible use case to configure an embedded broker with connection factories could be the need for integration tests.
In production the application integrates different broker (Artemis, Azure Service Bus).
The embedded broker could then be used in test code to do write "integration" tests (like embedded h2 database).

## Layout

### _com/example/messaging/embedded/adapter/messaging/configuration/internal_

Contains copied sample configuration classes from ```org.springframework.boot.autoconfigure.jms.artemis.*``` classes

### _com/example/messaging/embedded/adapter/messaging/configuration/ConnectionConfigurationMessaging3.java_

Different connections to different broker

### _com/example/messaging/embedded/adapter/messaging/JmsExample*.java_

Example message producers and consumers.

Integration for "easy testing" is done using a rest endpoint:
* http://localhost:8080/send?message=broadcastToAllBrokers
* http://localhost:8080/send/messaging1?message=sendToBroker1Only
* http://localhost:8080/send/messaging2?message=sendToBroker2Only
