package ch.rasc.wampspring.demo.security.config;

import org.springframework.context.annotation.Configuration;

import ch.rasc.wampspring.config.EnableWamp;
import ch.rasc.wampspring.config.WampEndpointRegistry;
import ch.rasc.wampspring.user.AbstractUserWampConfigurer;

@Configuration
@EnableWamp
public class WampConfig extends AbstractUserWampConfigurer {

	@Override
	public void registerWampEndpoints(WampEndpointRegistry registry) {
		registry.addEndpoint("/wamp").withSockJS();
		registry.addEndpoint("/ws");
	}

}