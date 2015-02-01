package ch.rasc.wampspring.demo.various.earthquake;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import ch.rasc.wampspring.EventMessenger;
import ch.rasc.wampspring.annotation.WampCallListener;
import ch.rasc.wampspring.broker.SubscriptionRegistry;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class EarthquakeService {

	private final static String pastHour = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_hour.geojson";

	private final static Log logger = LogFactory.getLog(EarthquakeService.class);

	private final static ObjectMapper objectMapper = new ObjectMapper();

	private GeoJson lastResult = null;

	private final SubscriptionRegistry subscriptionRegistry;

	private final EventMessenger eventMessenger;

	@Autowired
	public EarthquakeService(EventMessenger eventMessenger,
			SubscriptionRegistry subscriptionRegistry) {
		this.subscriptionRegistry = subscriptionRegistry;
		this.eventMessenger = eventMessenger;
	}

	@WampCallListener("initialload")
	public GeoJson subscribe() {
		if (this.lastResult == null) {
			readLatestData();
		}
		return this.lastResult;
	}

	@Scheduled(initialDelay = 2000, fixedDelay = 60000)
	public void pollData() {
		if (this.subscriptionRegistry.hasSubscriptions()) {
			readLatestData();
		}
	}

	private void readLatestData() {
		try {
			this.lastResult = objectMapper.readValue(new URL(pastHour), GeoJson.class);
			this.eventMessenger.sendToAll("/earthquakes", this.lastResult);
		}
		catch (IOException e) {
			logger.error("poll data", e);
		}
	}

}
