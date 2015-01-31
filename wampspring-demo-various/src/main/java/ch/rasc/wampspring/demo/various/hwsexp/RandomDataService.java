package ch.rasc.wampspring.demo.various.hwsexp;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ch.rasc.wampspring.EventMessenger;
import ch.rasc.wampspring.broker.SubscriptionRegistry;

@Service
public class RandomDataService {

	private final static Random random = new Random();

	private final EventMessenger eventMessenger;

	private final SubscriptionRegistry subscriptionRegistry;

	@Autowired
	public RandomDataService(EventMessenger eventMessenger,
			SubscriptionRegistry subscriptionRegistry) {
		this.subscriptionRegistry = subscriptionRegistry;
		this.eventMessenger = eventMessenger;
	}

	@Scheduled(initialDelay = 2000, fixedRate = 1000)
	public void sendRandomData() {
		if (this.subscriptionRegistry.hasSubscriptions()) {
			int[] randomNumbers = new int[100];
			for (int i = 0; i < randomNumbers.length; i++) {
				randomNumbers[i] = random.nextInt(101);
			}
			this.eventMessenger.sendToAll("hwsexp", randomNumbers);
		}
	}

}