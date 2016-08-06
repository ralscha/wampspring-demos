package ch.rasc.wampspring.demo.salmar;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	private static final int MAX_PROFANITY_LEVEL = 5;

	@Bean
	@Scope(value = "wampsession", proxyMode = ScopedProxyMode.TARGET_CLASS)
	@Description("Keeps track of the level of profanity of a websocket session")
	public SessionProfanity sessionProfanity() {
		return new SessionProfanity(MAX_PROFANITY_LEVEL);
	}

	@Bean
	@Description("Utility class to check the number of profanities and filter them")
	public ProfanityChecker profanityFilter() {
		Set<String> profanities = new HashSet<>(Arrays.asList("damn", "crap", "ass"));
		ProfanityChecker checker = new ProfanityChecker();
		checker.setProfanities(profanities);
		return checker;
	}

}
