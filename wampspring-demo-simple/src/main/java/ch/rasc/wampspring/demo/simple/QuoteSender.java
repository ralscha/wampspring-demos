package ch.rasc.wampspring.demo.simple;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ch.rasc.wampspring.EventMessenger;

@Service
public class QuoteSender {

	private final static Random random = new Random();
	private final static String[] quotes = { "GOOG", "AAPL", "MSFT", "IBM" };

	private final EventMessenger eventMessenger;
	
	@Autowired
	public QuoteSender(EventMessenger eventMessenger) {
		this.eventMessenger = eventMessenger;
	}
	
	@Scheduled(initialDelay = 5000, fixedDelay = 2000)
	public void sendStockQuotes() {
		eventMessenger.sendToAll("/topic/PRICE.STOCK." + quotes[random.nextInt(4)],
				random.nextDouble());
	}
	
}
