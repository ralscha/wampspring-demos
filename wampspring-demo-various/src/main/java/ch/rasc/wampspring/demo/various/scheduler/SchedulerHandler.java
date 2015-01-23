package ch.rasc.wampspring.demo.various.scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.rasc.wampspring.EventMessenger;
import ch.rasc.wampspring.annotation.WampPublishListener;
import ch.rasc.wampspring.message.PublishMessage;
import ch.rasc.wampspring.message.WampMessageHeader;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SchedulerHandler {

	@Autowired
	private EventMessenger eventMessenger;

	private final static ObjectMapper mapper = new ObjectMapper();

	@WampPublishListener("schdemo#clientDoInitialLoad")
	public void clientDoInitialLoad(PublishMessage message) {
		String sessionId = message.getHeader(WampMessageHeader.WEBSOCKET_SESSION_ID);
		this.eventMessenger.sendTo("schdemo#serverDoInitialLoad",
				Collections.singletonMap("data", CustomEventDb.list()),
				Collections.singleton(sessionId));
	}

	@WampPublishListener("schdemo#clientDoUpdate")
	public void clientDoUpdate(PublishMessage message, CustomEvent record) {
		CustomEventDb.update(record);
		this.eventMessenger.sendToAllExcept("schdemo#serverDoUpdate", record,
				message.getSessionId());
	}

	@WampPublishListener("schdemo#clientDoAdd")
	public void clientDoAdd(PublishMessage message, List<Map<String, Object>> records) {
		List<Object> updatedRecords = new ArrayList<>();
		List<Map<String, Object>> ids = new ArrayList<>();

		for (Map<String, Object> r : records) {
			@SuppressWarnings("unchecked")
			Map<String, Object> record = (Map<String, Object>) r.get("data");
			String internalId = (String) r.get("internalId");

			CustomEvent event = mapper.convertValue(record, CustomEvent.class);
			CustomEventDb.create(event);
			updatedRecords.add(event);

			Map<String, Object> result = new HashMap<>();
			result.put("internalId", internalId);
			result.put("record", event);
			ids.add(result);
		}

		this.eventMessenger.sendToAllExcept("schdemo#serverDoAdd",
				Collections.singletonMap("records", updatedRecords),
				message.getSessionId());
		this.eventMessenger.sendToAll("schdemo#serverSyncId",
				Collections.singletonMap("records", ids));
	}

	@WampPublishListener("schdemo#clientDoRemove")
	public void clientDoRemove(PublishMessage message, List<Integer> ids) {
		CustomEventDb.delete(ids);

		this.eventMessenger.sendToAllExcept("schdemo#serverDoRemove",
				Collections.singletonMap("ids", ids), message.getSessionId());
	}

}
