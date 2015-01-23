package ch.rasc.wampspring.demo.various.smoothie;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ch.rasc.wampspring.EventMessenger;
import ch.rasc.wampspring.broker.SubscriptionRegistry;

@Service
public class CpuDataService {

	private static final String SMOOTHIE_TOPIC_URI = "smoothie";

	private final Random random = new Random();

	private final SubscriptionRegistry subscriptionRegistry;

	private final EventMessenger eventMessenger;

	@Autowired
	public CpuDataService(EventMessenger eventMessenger,
			SubscriptionRegistry subscriptionRegistry) {
		this.subscriptionRegistry = subscriptionRegistry;
		this.eventMessenger = eventMessenger;
	}

	@Scheduled(initialDelay = 5000, fixedDelay = 1000)
	public void sendData() {
		if (this.subscriptionRegistry.hasSubscriptions()) {
			// System.out.println("SENDING DATA:"+System.nanoTime());
			final CpuData cpuData = new CpuData();
			cpuData.setHost1(new double[] { this.random.nextDouble(),
					this.random.nextDouble(), this.random.nextDouble(),
					this.random.nextDouble() });
			cpuData.setHost2(new double[] { this.random.nextDouble(),
					this.random.nextDouble(), this.random.nextDouble(),
					this.random.nextDouble() });
			cpuData.setHost3(new double[] { this.random.nextDouble(),
					this.random.nextDouble(), this.random.nextDouble(),
					this.random.nextDouble() });
			cpuData.setHost4(new double[] { this.random.nextDouble(),
					this.random.nextDouble(), this.random.nextDouble(),
					this.random.nextDouble() });

			this.eventMessenger.sendToAll(SMOOTHIE_TOPIC_URI, cpuData);
		}
	}

}
