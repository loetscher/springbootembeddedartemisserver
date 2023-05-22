package com.example.messaging.embedded.adapter.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class JmsExampleProducer {

  public static final String QUEUE = "example-test-queue";
  Logger log = LoggerFactory.getLogger(JmsExampleProducer.class);

  @Autowired
  @Qualifier("jmsTemplateMessaging1")
  private JmsTemplate jmsMessaging1Template;

  @Autowired
  @Qualifier("jmsTemplateMessaging2")
  private JmsTemplate jmsMessaging2Template;

  public void sendMessaging1(String message) {
    jmsMessaging1Template.convertAndSend(QUEUE + "-1", message);
    log.info("Sent message to messaging1='{}'", message);
  }

  public void sendMessaging2(String message) {
    jmsMessaging2Template.convertAndSend(QUEUE + "-2", message);
    log.info("Sent message to messaging2='{}'", message);
  }

  public void send(String message) {
    this.sendMessaging1(message);
    this.sendMessaging2(message);
    log.info("Sent message to messaging1 and messaging2='{}'", message);
  }
}
