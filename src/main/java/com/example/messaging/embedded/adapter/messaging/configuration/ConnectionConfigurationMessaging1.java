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
public class ConnectionConfigurationMessaging1 {
  @Value("${custom.messaging.servicebus.messaging1.uri}")
  private String messaging1Uri;
  @Value("${custom.messaging.servicebus.messaging1.user}")
  private String messaging1User;
  @Value("${custom.messaging.servicebus.messaging1.password}")
  private String messaging1Password;

  @Bean
  @Primary
  public DefaultJmsListenerContainerFactory messaging1ContainerFactory() {
    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
    factory.setConnectionFactory(jmsMessaging1Connection());
    factory.setSessionTransacted(true);
    factory.setMaxMessagesPerTask(1);
    factory.setConcurrency("1-5");
    return factory;
  }

  @Bean
  @Primary
  public ConnectionFactory jmsMessaging1Connection() {
    final var connection = new JmsConnectionFactory();
    connection.setRemoteURI(messaging1Uri);
    connection.setUsername(messaging1User);
    connection.setPassword(messaging1Password);
    return connection;
  }

//  @Bean
//  @Primary
//  public ConnectionFactory jmsMessaging1Connection() throws JMSException {
//    final var connection = new ActiveMQJMSConnectionFactory();
//    connection.setBrokerURL(messaging1Uri);
//    connection.setUser(messaging1User);
//    connection.setPassword(messaging1Password);
//    return connection;
//  }

  @Bean
  @Primary
  public JmsTemplate jmsTemplateMessaging1(){
    final var temp = new JmsTemplate();
    temp.setConnectionFactory(jmsMessaging1Connection());
    return temp;
  }

}
