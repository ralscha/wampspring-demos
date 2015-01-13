package ch.rasc.wampspring.demo.various.map;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import ch.rasc.wampspring.EventMessenger;
import ch.rasc.wampspring.broker.SubscriptionRegistry;

@Service
public class CarDriver {

	private final SubscriptionRegistry subscriptionRegistry;

	private final EventMessenger eventMessenger;

	private int blueRoutePos = 0;

	private int redRoutePos = 0;

	private List<LatLng> blueRoute;

	private List<LatLng> redRoute;

	@Autowired
	public CarDriver(EventMessenger eventMessenger,
			SubscriptionRegistry subscriptionRegistry) throws IOException {
		this.subscriptionRegistry = subscriptionRegistry;
		this.eventMessenger = eventMessenger;

		blueRoute = readLatLng("/map/route_blue.txt");
		redRoute = readLatLng("/map/route_red.txt");
	}

	private static List<LatLng> readLatLng(String resource) throws IOException {
		List<LatLng> route;
		ClassPathResource cp = new ClassPathResource(resource);
		try (InputStream is = cp.getInputStream()) {
			String content = StreamUtils.copyToString(is, StandardCharsets.UTF_8);
			route = Arrays.stream(content.split("\n")).map(LatLng::new)
					.collect(Collectors.toList());
		}
		return route;
	}

	@Scheduled(initialDelay = 1000, fixedDelay = 1000)
	public void driveBlueCar() {
		if (subscriptionRegistry.hasSubscriptions()) {
			LatLng latLng = blueRoute.get(blueRoutePos);
			blueRoutePos++;
			if (blueRoutePos >= blueRoute.size()) {
				blueRoutePos = 0;
			}

			eventMessenger.sendToAll("/map/blue", latLng);
		}
	}

	@Scheduled(initialDelay = 2000, fixedDelay = 1200)
	public void driveRedCar() {
		if (subscriptionRegistry.hasSubscriptions()) {
			LatLng latLng = redRoute.get(redRoutePos);
			redRoutePos++;
			if (redRoutePos >= redRoute.size()) {
				redRoutePos = 0;
			}

			eventMessenger.sendToAll("/map/red", latLng);
		}
	}

}
