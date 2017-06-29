package ch.rasc.wampspring.demo.salmar.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String SECURE_ADMIN_PASSWORD = "rockandroll";

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(new AuthenticationProvider() {

			@Override
			public boolean supports(Class<?> authentication) {
				return UsernamePasswordAuthenticationToken.class
						.isAssignableFrom(authentication);
			}

			@Override
			public Authentication authenticate(Authentication authentication)
					throws AuthenticationException {
				UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

				return new UsernamePasswordAuthenticationToken(token.getName(),
						token.getCredentials(),
						SECURE_ADMIN_PASSWORD.equals(token.getCredentials())
								? AuthorityUtils.createAuthorityList("ADMIN")
								: null);
			}
		});
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//@formatter:off
		http
		   .authorizeRequests()
		      .antMatchers("/js/**", "/fonts/**", "/lib/**", "/images/**", "/css/**").permitAll()
		      .antMatchers("/stats").hasAuthority("ADMIN")
		      .anyRequest().authenticated()
		   .and()
		      .formLogin().loginPage("/login.html")
		      .defaultSuccessUrl("/index.html").permitAll()
		   .and()
			  .logout().logoutSuccessUrl("/login.html").permitAll()
			.and()
			  .csrf().disable();
		//@formatter:on
	}

}
