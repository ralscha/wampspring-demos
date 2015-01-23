package ch.rasc.wampspring.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;

@Configuration
// @EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

	@Configuration
	public static class DefaultWebSecurityConfigurerAdapter extends
			WebSecurityConfigurerAdapter {

		@Autowired
		public void configureGlobal(AuthenticationManagerBuilder auth,
				UserDetailsService userDetailsService) throws Exception {
			auth.userDetailsService(userDetailsService).passwordEncoder(
					new BCryptPasswordEncoder());
		}
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			//@formatter:off
			http
			   .authorizeRequests()
			      .anyRequest().authenticated()
			   .and()
			      .formLogin().and()
				  .logout().permitAll()
				.and()
				  .csrf().disable();
			//@formatter:on
		}

	}

	@Configuration
	@Order(1)
	public static class H2ConsoleSecurityConfigurationAdapter extends
			WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			//@formatter:off
			http
			  .antMatcher("/h2/**")
			    .authorizeRequests()
			    .anyRequest()
				.fullyAuthenticated()
			  .and()
			    .csrf().disable()
			    .headers().disable();
			//@formatter:on
		}
	}

	@Bean
	public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
		return new SecurityEvaluationContextExtension();
	}
}
