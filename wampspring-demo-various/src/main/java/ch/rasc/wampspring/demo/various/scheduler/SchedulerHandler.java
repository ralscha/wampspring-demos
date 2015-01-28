package ch.rasc.wampspring.demo.various.scheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.rasc.wampspring.EventMessenger;
import ch.rasc.wampspring.annotation.WampCallListener;
import ch.rasc.wampspring.annotation.WampPublishListener;
import ch.rasc.wampspring.message.PublishMessage;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SchedulerHandler {

	@Autowired
	private EventMessenger eventMessenger;

	private final static ObjectMapper mapper = new ObjectMapper();

	@WampCallListener(value = "schdemo#doInitialLoad")
	public Map<String, Collection<CustomEvent>> doInitialLoad() {
		return Collections.singletonMap("data", CustomEventDb.list());
	}

	@WampPublishListener(value = "schdemo#clientDoUpdate",
			replyTo = "schdemo#serverDoUpdate", excludeSender = true)
	public CustomEvent update(CustomEvent record) {
		CustomEventDb.update(record);
		return record;
	}

	@WampPublishListener(value = "schdemo#clientDoAdd", replyTo = "schdemo#serverDoAdd",
			excludeSender = true)
	public Map<String, List<Object>> add(PublishMessage message,
			List<Map<String, Object>> records) {
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

		this.eventMessenger
				.sendTo("schdemo#serverSyncId", Collections.singletonMap("records", ids),
						message.getWebSocketSessionId());

		return Collections.singletonMap("records", updatedRecords);
	}

	@WampPublishListener(value = "schdemo#clientDoRemove",
			replyTo = "schdemo#serverDoRemove", excludeSender = true)
	public Map<String, List<Integer>> remove(List<Integer> ids) {
		CustomEventDb.delete(ids);
		return Collections.singletonMap("ids", ids);
	}

}
