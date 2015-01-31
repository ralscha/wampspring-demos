package ch.rasc.wampspring.demo.webrtc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import ch.rasc.wampspring.config.AbstractWampConfigurer;
import ch.rasc.wampspring.config.EnableWamp;
import ch.rasc.wampspring.config.WampEndpointRegistry;

@Configuration
@EnableWamp
public class WampConfig extends AbstractWampConfigurer {

	@Override
	public void registerWampEndpoints(WampEndpointRegistry registry) {
		registry.addEndpoint("/wamp").withSockJS();
	}

	@Bean
	public ServletServerContainerFactoryBean createWebSocketContainer() {
		ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
		container.setMaxTextMessageBufferSize(1_000_000);
		container.setMaxBinaryMessageBufferSize(1_000_000);
		return container;
	}

}