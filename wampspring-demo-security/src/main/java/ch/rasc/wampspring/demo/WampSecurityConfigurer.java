package ch.rasc.wampspring.demo;

import org.springframework.context.annotation.Configuration;

import ch.rasc.wampspring.message.WampMessageType;
import ch.rasc.wampspring.security.AbstractSecurityWampConfigurer;
import ch.rasc.wampspring.security.WampMessageSecurityMetadataSourceRegistry;

@Configuration
public class WampSecurityConfigurer extends AbstractSecurityWampConfigurer {

	@Override
	protected void configureInbound(WampMessageSecurityMetadataSourceRegistry messages) {
		messages.antMatchers(WampMessageType.PUBLISH, "/queue/**", "/topic/**")
				.denyAll()
				.antMatchers(WampMessageType.SUBSCRIBE, "/queue/**/*-user*",
						"/topic/**/*-user*").denyAll().anyMessage().authenticated();
	}

}
