package ch.rasc.wampspring.demo.salmar.config;

import org.springframework.context.annotation.Configuration;

import ch.rasc.wampspring.security.AbstractSecurityWampConfigurer;
import ch.rasc.wampspring.security.WampMessageSecurityMetadataSourceRegistry;

@Configuration
public class SecurityWampConfigurer extends AbstractSecurityWampConfigurer {
	@Override
	protected void configureInbound(WampMessageSecurityMetadataSourceRegistry messages) {
		messages.anyMessage().authenticated();
	}
}
