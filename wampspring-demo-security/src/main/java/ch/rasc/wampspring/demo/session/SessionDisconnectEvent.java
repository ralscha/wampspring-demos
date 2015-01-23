package ch.rasc.wampspring.demo.session;

import org.springframework.context.ApplicationEvent;
import org.springframework.web.socket.WebSocketSession;

@SuppressWarnings("serial")
public class SessionDisconnectEvent extends ApplicationEvent {

	private final WebSocketSession webSocketSession;

	public SessionDisconnectEvent(Object source, WebSocketSession webSocketSession) {
		super(source);
		this.webSocketSession = webSocketSession;
	}

	public WebSocketSession getWebSocketSession() {
		return this.webSocketSession;
	}
}