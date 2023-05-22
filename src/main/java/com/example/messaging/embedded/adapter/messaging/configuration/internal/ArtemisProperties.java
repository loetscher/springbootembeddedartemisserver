package com.example.messaging.embedded.adapter.messaging.configuration.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.boot.autoconfigure.jms.JmsPoolConnectionFactoryProperties;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisMode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

/**
 * Copy org.springframework.boot.autoconfigure.jms.artemis.ArtemisProperties
 * As mentioned in <a href="https://github.com/spring-projects/spring-boot/issues/7034">...</a>
 */
@Component
@ConfigurationProperties(
    prefix = "spring.artemis"
)
class ArtemisProperties {
  private ArtemisMode mode;
  private String brokerUrl;
  private String user;
  private String password;
  private final ArtemisProperties.Embedded embedded = new ArtemisProperties.Embedded();
  @NestedConfigurationProperty
  private final JmsPoolConnectionFactoryProperties pool = new JmsPoolConnectionFactoryProperties();

  public ArtemisProperties() {
  }

  public ArtemisMode getMode() {
    return this.mode;
  }

  public void setMode(ArtemisMode mode) {
    this.mode = mode;
  }

  public String getBrokerUrl() {
    return this.brokerUrl;
  }

  public void setBrokerUrl(String brokerUrl) {
    this.brokerUrl = brokerUrl;
  }

  public String getUser() {
    return this.user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public ArtemisProperties.Embedded getEmbedded() {
    return this.embedded;
  }

  public JmsPoolConnectionFactoryProperties getPool() {
    return this.pool;
  }

  public static class Embedded {
    private static final AtomicInteger serverIdCounter = new AtomicInteger();
    private int serverId;
    private boolean enabled;
    private boolean persistent;
    private String dataDirectory;
    private String[] queues;
    private String[] topics;
    private String clusterPassword;
    private boolean defaultClusterPassword;

    public Embedded() {
      this.serverId = serverIdCounter.getAndIncrement();
      this.enabled = true;
      this.queues = new String[0];
      this.topics = new String[0];
      this.clusterPassword = UUID.randomUUID().toString();
      this.defaultClusterPassword = true;
    }

    public int getServerId() {
      return this.serverId;
    }

    public void setServerId(int serverId) {
      this.serverId = serverId;
    }

    public boolean isEnabled() {
      return this.enabled;
    }

    public void setEnabled(boolean enabled) {
      this.enabled = enabled;
    }

    public boolean isPersistent() {
      return this.persistent;
    }

    public void setPersistent(boolean persistent) {
      this.persistent = persistent;
    }

    public String getDataDirectory() {
      return this.dataDirectory;
    }

    public void setDataDirectory(String dataDirectory) {
      this.dataDirectory = dataDirectory;
    }

    public String[] getQueues() {
      return this.queues;
    }

    public void setQueues(String[] queues) {
      this.queues = queues;
    }

    public String[] getTopics() {
      return this.topics;
    }

    public void setTopics(String[] topics) {
      this.topics = topics;
    }

    public String getClusterPassword() {
      return this.clusterPassword;
    }

    public void setClusterPassword(String clusterPassword) {
      this.clusterPassword = clusterPassword;
      this.defaultClusterPassword = false;
    }

    public boolean isDefaultClusterPassword() {
      return this.defaultClusterPassword;
    }

    public Map<String, Object> generateTransportParameters() {
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("serverId", this.getServerId());
      return parameters;
    }
  }
}
