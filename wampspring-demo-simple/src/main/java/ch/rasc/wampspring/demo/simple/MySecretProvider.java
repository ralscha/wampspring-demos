package ch.rasc.wampspring.demo.simple;

import java.util.HashMap;
import java.util.Map;

import ch.rasc.wampspring.cra.AuthenticationSecretProvider;

public class MySecretProvider implements AuthenticationSecretProvider {

	private final Map<String, String> secretDb = new HashMap<>();

	public MySecretProvider() {
		this.secretDb.put("a", "secretofa");
		this.secretDb.put("b", "secretofb");
	}

	@Override
	public String getSecret(String authKey) {
		return this.secretDb.get(authKey);
	}

}
