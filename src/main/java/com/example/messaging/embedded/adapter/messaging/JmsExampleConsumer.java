package com.example.messaging.embedded.adapter.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class JmsExampleConsumer {

  Logger log = LoggerFactory.getLogger(JmsExampleConsumer.class);

  @JmsListener(destination = "${spring.jms.template.default-destination}-1", containerFactory = "messaging1ContainerFactory")
  public void receiveMessaging1(String message) {
    log.info("Received messaging1 message='{}'", message);
  }

  @JmsListener(destination = "${spring.jms.template.default-destination}-2", containerFactory = "messaging2ContainerFactory")
  public void receiveMessaging2(String message) {
    log.info("Received messaging2 message='{}'", message);
  }
}
