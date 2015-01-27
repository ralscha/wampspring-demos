package ch.rasc.wampspring.demo.simple;

import org.springframework.context.annotation.Configuration;

import ch.rasc.wampspring.config.DefaultWampConfiguration;
import ch.rasc.wampspring.config.WampEndpointRegistry;
import ch.rasc.wampspring.cra.AuthenticationSecretProvider;

@Configuration
public class WampConfig extends DefaultWampConfiguration {

	@Override
	public void registerWampEndpoints(WampEndpointRegistry registry) {
		registry.addEndpoint("/wamp").withSockJS();
	}

	@Override
	public AuthenticationSecretProvider authenticationSecretProvider() {
		return new MySecretProvider();
	}

}