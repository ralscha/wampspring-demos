package ch.rasc.wampspring.demo.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.support.AbstractMessageChannel;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.messaging.access.expression.MessageExpressionVoter;
import org.springframework.security.messaging.access.intercept.ChannelSecurityInterceptor;
import org.springframework.security.messaging.access.intercept.MessageSecurityMetadataSource;
import org.springframework.security.messaging.context.AuthenticationPrincipalArgumentResolver;
import org.springframework.security.messaging.context.SecurityContextChannelInterceptor;

import ch.rasc.wampspring.config.WampConfigurerAdapter;
import ch.rasc.wampspring.message.WampMessageHeader;

/**
 * Allows configuring WAMP messages authorization.
 *
 * <p>
 * For example:
 * </p>
 *
 * <pre>
 * &#064;Configuration
 * public class WampSecurityConfigurer extends AbstractSecurityWampConfigurer {
 * 
 * 	&#064;Override
 * 	protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
 * 		messages.antMatchers(&quot;/user/queue/errors&quot;).permitAll().antMatchers(&quot;/admin/**&quot;)
 * 				.hasRole(&quot;ADMIN&quot;).anyMessage().authenticated();
 * 	}
 * }
 * </pre>
 */
public abstract class AbstractSecurityWampConfigurer extends WampConfigurerAdapter {

	private final WampMessageSecurityMetadataSourceRegistry inboundRegistry = new WampMessageSecurityMetadataSourceRegistry();

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
	}

	@Override
	public void configureClientInboundChannel(AbstractMessageChannel channel) {
		ChannelSecurityInterceptor inboundChannelSecurity = inboundChannelSecurity();
		if (this.inboundRegistry.containsMapping()) {
			channel.addInterceptor(securityContextChannelInterceptor());
			channel.addInterceptor(inboundChannelSecurity);
		}
	}

	@Bean
	public ChannelSecurityInterceptor inboundChannelSecurity() {
		ChannelSecurityInterceptor channelSecurityInterceptor = new ChannelSecurityInterceptor(
				inboundMessageSecurityMetadataSource());
		List<AccessDecisionVoter<? extends Object>> voters = new ArrayList<>();
		voters.add(new MessageExpressionVoter<>());
		AffirmativeBased manager = new AffirmativeBased(voters);
		channelSecurityInterceptor.setAccessDecisionManager(manager);
		return channelSecurityInterceptor;
	}

	@Bean
	public SecurityContextChannelInterceptor securityContextChannelInterceptor() {
		return new SecurityContextChannelInterceptor(WampMessageHeader.PRINCIPAL.name());
	}

	@Bean
	public MessageSecurityMetadataSource inboundMessageSecurityMetadataSource() {
		configureInbound(this.inboundRegistry);
		return this.inboundRegistry.createMetadataSource();
	}

	protected void configureInbound(
			@SuppressWarnings("unused") WampMessageSecurityMetadataSourceRegistry messages) {
		// by default nothing here
	}

}
