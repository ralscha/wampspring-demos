package ch.rasc.wampspring.demo.various.snake;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SnakeMessage {
	private final String type;

	private final Object data;

	private final Integer id;

	private SnakeMessage(String type) {
		this(type, null, null);
	}

	private SnakeMessage(String type, Object data) {
		this(type, data, null);
	}

	private SnakeMessage(String type, Object data, Integer id) {
		this.type = type;
		this.data = data;
		this.id = id;
	}

	public static SnakeMessage createDeadMessage() {
		return new SnakeMessage("dead");
	}

	public static SnakeMessage createKillMessage() {
		return new SnakeMessage("kill");
	}

	public static SnakeMessage createUpdateMessage(List<Map<String, Object>> data) {
		return new SnakeMessage("update", data);
	}

	public static SnakeMessage createJoinMessage(List<Map<String, Object>> data) {
		return new SnakeMessage("join", data);
	}

	public static SnakeMessage createLeaveMessage(Integer id) {
		return new SnakeMessage("leave", null, id);
	}

	public String getType() {
		return this.type;
	}

	public Object getData() {
		return this.data;
	}

	public Integer getId() {
		return this.id;
	}

}
