package ch.rasc.wampspring.demo.various.bandwidth;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import ch.rasc.wampspring.EventMessenger;
import ch.rasc.wampspring.broker.SubscriptionRegistry;

@Service
public class NetworkInfoProducer {

	private final Random rand = new Random();

	private long tx = 0;

	private long rx = 0;

	private final boolean isLinux;

	private final SubscriptionRegistry subscriptionRegistry;

	private final EventMessenger eventMessenger;

	@Value("${bandwidth.network.interface}")
	private String networkInterface;

	@Autowired
	public NetworkInfoProducer(EventMessenger eventMessenger,
			SubscriptionRegistry subscriptionRegistry) {
		this.subscriptionRegistry = subscriptionRegistry;
		this.eventMessenger = eventMessenger;
		OperatingSystemMXBean operatingSystemMXBean = ManagementFactory
				.getOperatingSystemMXBean();
		String os = operatingSystemMXBean.getName().toLowerCase();
		isLinux = os.indexOf("linux") != -1;
	}

	@Scheduled(initialDelay = 2000, fixedRate = 1000)
	public void sendNetworkInfo() {
		if (subscriptionRegistry.hasSubscriptions()) {
			if (isLinux) {
				try {
					ProcessBuilder pb = new ProcessBuilder("cat", "/sys/class/net/"
							+ networkInterface + "/statistics/rx_bytes");
					Process p = pb.start();
					p.waitFor();
					rx = Long.parseLong(StringUtils.trimAllWhitespace(StreamUtils
							.copyToString(p.getInputStream(), StandardCharsets.UTF_8)));

					pb = new ProcessBuilder("cat", "/sys/class/net/" + networkInterface
							+ "/statistics/tx_bytes");
					p = pb.start();
					p.waitFor();
					tx = Long.parseLong(StringUtils.trimAllWhitespace(StreamUtils
							.copyToString(p.getInputStream(), StandardCharsets.UTF_8)));
				}
				catch (NumberFormatException | IOException | InterruptedException e) {
					rx = 0;
					tx = 0;
				}
			}
			else {
				rx += rand.nextInt(512 * 1024);
				tx += rand.nextInt(512 * 1024);
			}

			Map<String, Long> info = new HashMap<>();
			info.put("rec", rx);
			info.put("snd", tx);

			eventMessenger.sendToAll("networkinfo", info);
		}
	}

}
