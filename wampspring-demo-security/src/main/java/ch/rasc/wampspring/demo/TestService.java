package ch.rasc.wampspring.demo;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import ch.rasc.wampspring.annotation.WampCallListener;

@Service
public class TestService {

	@WampCallListener(value = "toUpperCase")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String toUpperCase(String aString, @AuthenticationPrincipal Principal principal) {
		System.out.println(principal);
		return aString.toUpperCase();
	}

}
