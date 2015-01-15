package ch.rasc.wampspring.demo.client.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.rasc.wampspring.EventMessenger;
import ch.rasc.wampspring.annotation.WampCallListener;

@Service
public class TestService {

	private final EventMessenger eventMessenger;

	@Autowired
	public TestService(EventMessenger eventMessenger) {
		this.eventMessenger = eventMessenger;
	}

	@WampCallListener
	public int sum(int a, int b) {
		return a + b;
	}

	@WampCallListener
	public String addSubscriber(String topic) {
		return null;
	}

	public void sendStockQuotes() {
		eventMessenger.sendToAll("", "");
	}

}
