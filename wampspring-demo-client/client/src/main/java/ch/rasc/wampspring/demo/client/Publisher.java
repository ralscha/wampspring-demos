/**
 * Copyright 2015-2015 Ralph Schaer <ralphschaer@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.rasc.wampspring.demo.client;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.rasc.wampspring.message.PublishMessage;
import ch.rasc.wampspring.message.WampMessage;
import ch.rasc.wampspring.message.WelcomeMessage;

public class Publisher {

	public static void main(String[] args) throws InterruptedException {

		WebSocketClient webSocketClient = new StandardWebSocketClient();
		final JsonFactory jsonFactory = new MappingJsonFactory(new ObjectMapper());

		final CountDownLatch welcomeLatch = new CountDownLatch(1);
		final CountDownLatch latch = new CountDownLatch(1_000_000);
		TextWebSocketHandler handler = new TextWebSocketHandler() {

			@Override
			protected void handleTextMessage(WebSocketSession session,
					TextMessage message) throws Exception {
				WampMessage wampMessage = WampMessage.fromJson(jsonFactory,
						message.getPayload());

				if (wampMessage instanceof WelcomeMessage) {
					latch.countDown();
				}

			}

		};

		Long[] start = new Long[1];
		ListenableFuture<WebSocketSession> future = webSocketClient.doHandshake(handler,
				"ws://localhost:8080/wamp");
		future.addCallback(wss -> {

			// Waiting for WELCOME message
			try {
				welcomeLatch.await(5, TimeUnit.SECONDS);

				start[0] = System.currentTimeMillis();
				for (int i = 0; i < 1_000_000; i++) {
					PublishMessage publishMessage = new PublishMessage("/test/myqueue",
							i);
					try {
						wss.sendMessage(
								new TextMessage(publishMessage.toJson(jsonFactory)));
					}
					catch (Exception e) {
						System.out.println("ERROR SENDING PUBLISH_MESSAGE" + e);
					}
					latch.countDown();
				}

			}
			catch (Exception e1) {
				System.out.println("SENDING PUBLISH MESSAGES: " + e1);
			}

		}, t -> {
			System.out.println("DO HANDSHAKE ERROR: " + t);
			System.exit(1);
		});

		if (!latch.await(3, TimeUnit.MINUTES)) {
			System.out.println("SOMETHING WENT WRONG");
		}
		System.out.println((System.currentTimeMillis() - start[0]) / 1000 + " seconds");
	}

}