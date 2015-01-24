package ch.rasc.wampspring.demo.various;

import org.springframework.context.annotation.Configuration;

import ch.rasc.wampspring.config.EnableWamp;
import ch.rasc.wampspring.config.WampConfigurerAdapter;
import ch.rasc.wampspring.config.WampEndpointRegistry;

@Configuration
@EnableWamp
public class WampConfig extends WampConfigurerAdapter {

	@Override
	public void registerWampEndpoints(WampEndpointRegistry registry) {
		registry.addEndpoint("/wamp").withSockJS();
		registry.addEndpoint("/ws");
	}

}