package com.example.messaging.embedded.adapter.messaging.configuration.internal;

import java.util.Iterator;
import java.util.stream.Collectors;
import org.apache.activemq.artemis.api.core.QueueConfiguration;
import org.apache.activemq.artemis.api.core.RoutingType;
import org.apache.activemq.artemis.core.config.CoreAddressConfiguration;
import org.apache.activemq.artemis.core.server.embedded.EmbeddedActiveMQ;
import org.apache.activemq.artemis.jms.server.config.JMSConfiguration;
import org.apache.activemq.artemis.jms.server.config.JMSQueueConfiguration;
import org.apache.activemq.artemis.jms.server.config.TopicConfiguration;
import org.apache.activemq.artemis.jms.server.config.impl.JMSConfigurationImpl;
import org.apache.activemq.artemis.jms.server.config.impl.JMSQueueConfigurationImpl;
import org.apache.activemq.artemis.jms.server.config.impl.TopicConfigurationImpl;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Copy from org.springframework.boot.autoconfigure.jms.artemis.ArtemisEmbeddedServerConfiguration
 * As mentioned in <a href="https://github.com/spring-projects/spring-boot/issues/7034">...</a>
 */
@Configuration(
    proxyBeanMethods = false
)
@ConditionalOnClass({EmbeddedActiveMQ.class})
@ConditionalOnProperty(
    prefix = "spring.artemis.embedded",
    name = {"enabled"},
    havingValue = "true",
    matchIfMissing = true
)
class ArtemisEmbeddedServerConfiguration {
  private final ArtemisProperties properties;

  ArtemisEmbeddedServerConfiguration(ArtemisProperties properties) {
    this.properties = properties;
  }

  @Bean
  @ConditionalOnMissingBean
  org.apache.activemq.artemis.core.config.Configuration artemisConfiguration() {
    return (new ArtemisEmbeddedConfigurationFactory(this.properties)).createConfiguration();
  }

  @Bean(
      initMethod = "start",
      destroyMethod = "stop"
  )
  @ConditionalOnMissingBean
  EmbeddedActiveMQ embeddedActiveMq(org.apache.activemq.artemis.core.config.Configuration configuration, JMSConfiguration jmsConfiguration, ObjectProvider<ArtemisConfigurationCustomizer> configurationCustomizers) {
    Iterator var4 = jmsConfiguration.getQueueConfigurations().iterator();

    while(var4.hasNext()) {
      JMSQueueConfiguration queueConfiguration = (JMSQueueConfiguration)var4.next();
      String queueName = queueConfiguration.getName();
      configuration.addAddressConfiguration((new CoreAddressConfiguration()).setName(queueName).addRoutingType(RoutingType.ANYCAST).addQueueConfiguration((new QueueConfiguration(queueName)).setAddress(queueName).setFilterString(queueConfiguration.getSelector()).setDurable(queueConfiguration.isDurable()).setRoutingType(RoutingType.ANYCAST)));
    }

    var4 = jmsConfiguration.getTopicConfigurations().iterator();

    while(var4.hasNext()) {
      TopicConfiguration topicConfiguration = (TopicConfiguration)var4.next();
      configuration.addAddressConfiguration((new CoreAddressConfiguration()).setName(topicConfiguration.getName()).addRoutingType(RoutingType.MULTICAST));
    }

    configurationCustomizers.orderedStream().forEach((customizer) -> {
      customizer.customize(configuration);
    });
    EmbeddedActiveMQ embeddedActiveMq = new EmbeddedActiveMQ();
    embeddedActiveMq.setConfiguration(configuration);
    return embeddedActiveMq;
  }

  @Bean
  @ConditionalOnMissingBean
  JMSConfiguration artemisJmsConfiguration(ObjectProvider<JMSQueueConfiguration> queuesConfiguration, ObjectProvider<TopicConfiguration> topicsConfiguration) {
    JMSConfiguration configuration = new JMSConfigurationImpl();
    configuration.getQueueConfigurations().addAll(queuesConfiguration.orderedStream().collect(Collectors.toList()));
    configuration.getTopicConfigurations().addAll(topicsConfiguration.orderedStream().collect(Collectors.toList()));
    this.addQueues(configuration, this.properties.getEmbedded().getQueues());
    this.addTopics(configuration, this.properties.getEmbedded().getTopics());
    return configuration;
  }

  private void addQueues(JMSConfiguration configuration, String[] queues) {
    boolean persistent = this.properties.getEmbedded().isPersistent();
    String[] var4 = queues;
    int var5 = queues.length;

    for(int var6 = 0; var6 < var5; ++var6) {
      String queue = var4[var6];
      JMSQueueConfigurationImpl jmsQueueConfiguration = new JMSQueueConfigurationImpl();
      jmsQueueConfiguration.setName(queue);
      jmsQueueConfiguration.setDurable(persistent);
      jmsQueueConfiguration.setBindings(new String[]{"/queue/" + queue});
      configuration.getQueueConfigurations().add(jmsQueueConfiguration);
    }

  }

  private void addTopics(JMSConfiguration configuration, String[] topics) {
    String[] var3 = topics;
    int var4 = topics.length;

    for(int var5 = 0; var5 < var4; ++var5) {
      String topic = var3[var5];
      TopicConfigurationImpl topicConfiguration = new TopicConfigurationImpl();
      topicConfiguration.setName(topic);
      topicConfiguration.setBindings(new String[]{"/topic/" + topic});
      configuration.getTopicConfigurations().add(topicConfiguration);
    }

  }
}
