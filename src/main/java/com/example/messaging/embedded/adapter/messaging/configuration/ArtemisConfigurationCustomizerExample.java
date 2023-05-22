package com.example.messaging.embedded.adapter.messaging.configuration;

import org.springframework.boot.autoconfigure.jms.artemis.ArtemisConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ArtemisConfigurationCustomizerExample {

	@Bean
	public ArtemisConfigurationCustomizer customizer() {
		return configuration -> {
      try {
        configuration.addAcceptorConfiguration("netty", "tcp://localhost:61616");
        configuration.addAcceptorConfiguration("netty", "tcp://localhost:5672?protocols=AMQP,CORE");

        // AddressSettings
        // This doesn't work yet and needs some fixing
        /*
        AddressSettings settings = new AddressSettings();
        settings.setAutoCreateJmsTopics(true);
        settings.setAutoCreateJmsQueues(true);

        SimpleString deadLetterRequest = new SimpleString("jms.queue.deadLetterQueue");
        settings.setMaxDeliveryAttempts(3);
        settings.setDeadLetterAddress(deadLetterRequest);
        settings.setExpiryAddress(deadLetterRequest);

        configuration.addAddressesSetting("settings", settings);
        */

      } catch (Exception e) {
        throw new RuntimeException("Failed to add netty transport acceptor to artemis instance", e);
      }

    };
	}

}
