package ch.rasc.wampspring.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.ExpiringSession;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.SessionRepository;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.SessionRepositoryFilter;

@Configuration
public class SessionConfig {

	@Bean
	public SessionRepository<ExpiringSession> sessionRepository() {
		MapSessionRepository mapSessionRepository = new MapSessionRepository();
		mapSessionRepository.setDefaultMaxInactiveInterval(1800);
		return mapSessionRepository;
	}

	@Bean
	public <S extends ExpiringSession> SessionRepositoryFilter<? extends ExpiringSession> sessionRepositoryFilter(
			SessionRepository<S> sessionRepository) {
		SessionRepositoryFilter<S> sessionRepositoryFilter = new SessionRepositoryFilter<>(
				sessionRepository);
		sessionRepositoryFilter.setHttpSessionStrategy(new CookieHttpSessionStrategy());
		return sessionRepositoryFilter;
	}

}
