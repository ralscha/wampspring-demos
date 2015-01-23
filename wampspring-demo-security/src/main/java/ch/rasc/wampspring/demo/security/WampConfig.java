package ch.rasc.wampspring.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.ExpiringSession;

import ch.rasc.wampspring.config.WampEndpointRegistry;
import ch.rasc.wampspring.demo.session.DefaultSessionWampConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class WampConfig extends DefaultSessionWampConfiguration<ExpiringSession> {

	@Autowired
	ObjectMapper objectMapper;

	@Override
	public ObjectMapper objectMapper() {
		return this.objectMapper;
	}

	@Override
	public void registerWampEndpoints(WampEndpointRegistry registry) {
		registry.addEndpoint("/wamp").withSockJS();
	}

}