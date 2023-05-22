package com.example.messaging.embedded.adapter.rest;


import com.example.messaging.embedded.adapter.messaging.JmsExampleProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.web.bind.annotation.RestController
public class RestController {

  @Autowired
  JmsExampleProducer jmsProducer;

  /**
   * <a href="http://localhost:8080/send?message=broadcast">...</a> message
   * @param message
   */
  @PostMapping("/send")
  public void sendDataToJms(@RequestParam String message) {
    jmsProducer.send(message);
  }

  /**
   * <a href="http://localhost:8080/send/messaging1?message=only">...</a> to messaging system 1
   * @param message
   */
  @PostMapping("/send/messaging1")
  public void sendDataToMessaging1(@RequestParam String message) {
    jmsProducer.sendMessaging1(message);
  }

  /**
   * <a href="http://localhost:8080/send/messaging2?message=only">...</a> to messaging system 2
   * @param message
   */
  @PostMapping("/send/messaging2")
  public void sendDataToMessaging2(@RequestParam String message) {
    jmsProducer.sendMessaging2(message);
  }
}
