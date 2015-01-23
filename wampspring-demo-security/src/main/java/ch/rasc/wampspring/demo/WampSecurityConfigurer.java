package ch.rasc.wampspring.demo;

import org.springframework.context.annotation.Configuration;

import ch.rasc.wampspring.demo.security.AbstractSecurityWampConfigurer;
import ch.rasc.wampspring.demo.security.WampMessageSecurityMetadataSourceRegistry;

@Configuration
public class WampSecurityConfigurer extends AbstractSecurityWampConfigurer {

	@Override
	protected void configureInbound(WampMessageSecurityMetadataSourceRegistry messages) {
//		messages.antMatchers(WampMessageType.CALL, "toUpperCase").hasRole("ADMIN")		        
//				.anyMessage().authenticated();
		
		messages.anyMessage().authenticated();
	}

}
