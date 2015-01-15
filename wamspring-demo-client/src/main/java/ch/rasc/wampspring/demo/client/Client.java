package ch.rasc.wampspring.demo.client;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import ch.rasc.wampspring.message.CallMessage;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Client {

	public static void main(String[] args) throws InterruptedException {
		WebSocketClient webSocketClient = new StandardWebSocketClient();
		JsonFactory jsonFactory = new MappingJsonFactory(new ObjectMapper());

		CountDownLatch latch = new CountDownLatch(1_000_000);
		TestTextWebSocketHandler handler = new TestTextWebSocketHandler(jsonFactory,
				latch);

		Long[] start = new Long[1];
		ListenableFuture<WebSocketSession> future = webSocketClient.doHandshake(handler,
				"ws://localhost:8080/wamp");
		future.addCallback(wss -> {
			start[0] = System.currentTimeMillis();
			for (int i = 0; i < 1_000_000; i++) {

				CallMessage callMessage = new CallMessage(UUID.randomUUID().toString(),
						"testService.sum", i, i + 1);
				try {
					wss.sendMessage(new TextMessage(callMessage.toJson(jsonFactory)));
				}
				catch (Exception e) {
					System.out.println("ERROR SENDING CALLMESSAGE" + e);
					latch.countDown();
				}
			}

		}, t -> {
			System.out.println("DO HANDSHAKE ERROR: " + t);
			System.exit(1);
		});

		latch.await();
		System.out.println((System.currentTimeMillis() - start[0]) / 1000 + " seconds");
		System.out.println("SUCCESS: " + handler.getSuccess());
		System.out.println("ERROR  : " + handler.getError());
	}

}
