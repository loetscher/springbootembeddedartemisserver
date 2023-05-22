package com.example.messaging.embedded.adapter.messaging.configuration;

import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class ConnectionConfigurationMessaging2 {

  @Value("${custom.messaging.servicebus.messaging2.uri}")
  private String messaging2Uri;
  @Value("${custom.messaging.servicebus.messaging2.user}")
  private String messaging2User;
  @Value("${custom.messaging.servicebus.messaging2.password}")
  private String messaging2Password;

  @Bean
  public DefaultJmsListenerContainerFactory messaging2ContainerFactory() throws JMSException {
    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
    factory.setConnectionFactory(jmsMessaging2Connection());
    factory.setSessionTransacted(true);
    factory.setMaxMessagesPerTask(1);
    factory.setConcurrency("1-5");
    return factory;
  }

  @Bean
  public ConnectionFactory jmsMessaging2Connection() {
    final var connection = new JmsConnectionFactory();
    connection.setRemoteURI(messaging2Uri);
    connection.setUsername(messaging2User);
    connection.setPassword(messaging2Password);
    return connection;
  }

//  @Bean
//  public ConnectionFactory jmsMessaging2Connection() throws JMSException {
//    final var connection = new ActiveMQJMSConnectionFactory();
//    connection.setBrokerURL(messaging2Uri);
//    connection.setUser(messaging2User);
//    connection.setPassword(messaging2Password);
//    return connection;
//  }
  @Bean
  public JmsTemplate jmsTemplateMessaging2() {
    final var temp = new JmsTemplate();
    temp.setConnectionFactory(jmsMessaging2Connection());
    return temp;
  }

}
