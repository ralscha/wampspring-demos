package ch.rasc.wampspring.demo.various.echarts;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ch.rasc.wampspring.EventMessenger;
import ch.rasc.wampspring.broker.SubscriptionRegistry;

@Service
public class EchartsService {

	private final static Random random = new Random();

	private final EventMessenger eventMessenger;

	private final SubscriptionRegistry subscriptionRegistry;

	@Autowired
	public EchartsService(EventMessenger eventMessenger,
			SubscriptionRegistry subscriptionRegistry) {
		this.subscriptionRegistry = subscriptionRegistry;
		this.eventMessenger = eventMessenger;
	}

	@Scheduled(initialDelay = 2000, fixedRate = 800)
	public void sendRandomData() {
		if (this.subscriptionRegistry.hasSubscriptions()) {
			int[] randomNumbers = new int[5];
			for (int i = 0; i < randomNumbers.length; i++) {
				randomNumbers[i] = random.nextInt(31);
			}
			this.eventMessenger.sendToAll("echarts", randomNumbers);
		}
	}

}