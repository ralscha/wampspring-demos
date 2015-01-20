package ch.rasc.wampspring.demo.various.snake;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
@Scope(value = "wampsession", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SnakeId {

	private static final AtomicInteger snakeIds = new AtomicInteger(0);

	private final String webSocketSessionId;

	private final Integer id;

	@Autowired
	public SnakeId(WebSocketSession webSocketSession) {
		this.webSocketSessionId = webSocketSession.getId();
		this.id = snakeIds.incrementAndGet();
	}

	public String getWebSocketSessionId() {
		return webSocketSessionId;
	}

	public Integer getId() {
		return id;
	}

}