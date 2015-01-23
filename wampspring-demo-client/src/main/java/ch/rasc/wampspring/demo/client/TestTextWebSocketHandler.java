package ch.rasc.wampspring.demo.client;

import java.util.concurrent.CountDownLatch;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import ch.rasc.wampspring.message.CallErrorMessage;
import ch.rasc.wampspring.message.CallResultMessage;
import ch.rasc.wampspring.message.WampMessage;
import ch.rasc.wampspring.message.WelcomeMessage;

import com.fasterxml.jackson.core.JsonFactory;

public class TestTextWebSocketHandler extends TextWebSocketHandler {

	private final JsonFactory jsonFactory;
	private final CountDownLatch latch;
	private int success = 0;
	private int error = 0;

	public TestTextWebSocketHandler(JsonFactory jsonFactory, CountDownLatch latch) {
		this.jsonFactory = jsonFactory;
		this.latch = latch;
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message)
			throws Exception {

		WampMessage wampMessage = WampMessage.fromJson(this.jsonFactory,
				message.getPayload());

		if (wampMessage instanceof WelcomeMessage) {
			System.out.println("WELCOME received: " + wampMessage);
		}
		else if (wampMessage instanceof CallResultMessage) {
			this.success++;
			this.latch.countDown();
		}
		else if (wampMessage instanceof CallErrorMessage) {
			this.error++;
			this.latch.countDown();
		}

	}

	public int getSuccess() {
		return this.success;
	}

	public int getError() {
		return this.error;
	}

}
