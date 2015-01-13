package ch.rasc.wampspring.demo.simple;

import org.springframework.stereotype.Service;

import ch.rasc.wampspring.annotation.WampCallListener;

@Service
public class TestService {

	@WampCallListener(value = "toUpperCase/**", authenticated = true)
	public String toUpperCase(String aString) {
		return aString.toUpperCase();
	}

}
