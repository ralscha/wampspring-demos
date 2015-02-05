package ch.rasc.wampspring.demo.security.config;

import org.springframework.context.annotation.Configuration;

import ch.rasc.wampspring.security.AbstractSecurityWampConfigurer;
import ch.rasc.wampspring.security.WampMessageSecurityMetadataSourceRegistry;

@Configuration
public class SecurityWampConfigurer extends AbstractSecurityWampConfigurer {

	@Override
	protected void configureInbound(WampMessageSecurityMetadataSourceRegistry messages) {
		messages.wampPublishDestMatchers("/queue/**", "/topic/**").denyAll()
				.wampSubscribeDestMatchers("/queue/**/*-user*", "/topic/**/*-user*")
				.denyAll().anyMessage().authenticated();
	}

}
