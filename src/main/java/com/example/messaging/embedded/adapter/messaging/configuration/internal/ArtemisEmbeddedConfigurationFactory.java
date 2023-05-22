package com.example.messaging.embedded.adapter.messaging.configuration.internal;

import java.io.File;
import org.apache.activemq.artemis.api.core.QueueConfiguration;
import org.apache.activemq.artemis.api.core.RoutingType;
import org.apache.activemq.artemis.api.core.SimpleString;
import org.apache.activemq.artemis.api.core.TransportConfiguration;
import org.apache.activemq.artemis.core.config.Configuration;
import org.apache.activemq.artemis.core.config.CoreAddressConfiguration;
import org.apache.activemq.artemis.core.config.impl.ConfigurationImpl;
import org.apache.activemq.artemis.core.remoting.impl.invm.InVMAcceptorFactory;
import org.apache.activemq.artemis.core.server.JournalType;
import org.apache.activemq.artemis.core.settings.impl.AddressSettings;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Copy from org.springframework.boot.autoconfigure.jms.artemis.ArtemisEmbeddedConfigurationFactory
 * As mentioned in <a href="https://github.com/spring-projects/spring-boot/issues/7034">...</a>
 */
class ArtemisEmbeddedConfigurationFactory {
  private static final Log logger = LogFactory.getLog(
      ArtemisEmbeddedConfigurationFactory.class);
  private final ArtemisProperties.Embedded properties;

  ArtemisEmbeddedConfigurationFactory(ArtemisProperties properties) {
    this.properties = properties.getEmbedded();
  }

  Configuration createConfiguration() {
    ConfigurationImpl configuration = new ConfigurationImpl();
    configuration.setSecurityEnabled(false);
    configuration.setPersistenceEnabled(this.properties.isPersistent());
    String dataDir = this.getDataDir();
    configuration.setJournalDirectory(dataDir + "/journal");
    if (this.properties.isPersistent()) {
      configuration.setJournalType(JournalType.NIO);
      configuration.setLargeMessagesDirectory(dataDir + "/largemessages");
      configuration.setBindingsDirectory(dataDir + "/bindings");
      configuration.setPagingDirectory(dataDir + "/paging");
    }

    TransportConfiguration transportConfiguration = new TransportConfiguration(InVMAcceptorFactory.class.getName(), this.properties.generateTransportParameters());
    configuration.getAcceptorConfigurations().add(transportConfiguration);
    if (this.properties.isDefaultClusterPassword() && logger.isDebugEnabled()) {
      logger.debug("Using default Artemis cluster password: " + this.properties.getClusterPassword());
    }

    configuration.setClusterPassword(this.properties.getClusterPassword());
    configuration.addAddressConfiguration(this.createAddressConfiguration("DLQ"));
    configuration.addAddressConfiguration(this.createAddressConfiguration("ExpiryQueue"));
    configuration.addAddressSetting("#", (new AddressSettings()).setDeadLetterAddress(SimpleString.toSimpleString("DLQ")).setExpiryAddress(SimpleString.toSimpleString("ExpiryQueue")));
    return configuration;
  }

  private CoreAddressConfiguration createAddressConfiguration(String name) {
    return (new CoreAddressConfiguration()).setName(name).addRoutingType(RoutingType.ANYCAST).addQueueConfiguration((new QueueConfiguration(name)).setRoutingType(RoutingType.ANYCAST).setAddress(name));
  }

  private String getDataDir() {
    if (this.properties.getDataDirectory() != null) {
      return this.properties.getDataDirectory();
    } else {
      String tempDirectory = System.getProperty("java.io.tmpdir");
      return (new File(tempDirectory, "artemis-data")).getAbsolutePath();
    }
  }
}
