package ch.rasc.wampspring.demo.various;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import ch.rasc.wampspring.config.DefaultWampConfiguration;
import ch.rasc.wampspring.config.WampEndpointRegistry;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class WampConfig extends DefaultWampConfiguration {

	// Reusing the objectMapper that spring boot provides

	@Autowired
	ObjectMapper objectMapper;

	@Override
	public ObjectMapper objectMapper() {
		return objectMapper;
	}

	@Override
	public void registerWampEndpoints(WampEndpointRegistry registry) {
		registry.addEndpoint("/wamp").withSockJS();
		registry.addEndpoint("/ws");
	}

}