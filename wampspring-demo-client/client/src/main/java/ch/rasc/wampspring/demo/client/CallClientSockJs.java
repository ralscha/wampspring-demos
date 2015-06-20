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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import ch.rasc.wampspring.message.CallMessage;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CallClientSockJs {

	public static void main(String[] args) throws InterruptedException {

		List<Transport> transports = new ArrayList<>(2);
		transports.add(new WebSocketTransport(new StandardWebSocketClient()));
		transports.add(new RestTemplateXhrTransport());
		WebSocketClient webSocketClient = new SockJsClient(transports);

		JsonFactory jsonFactory = new MappingJsonFactory(new ObjectMapper());

		CountDownLatch latch = new CountDownLatch(10_000);
		TestTextWebSocketHandler handler = new TestTextWebSocketHandler(jsonFactory,
				latch);

		Long[] start = new Long[1];
		ListenableFuture<WebSocketSession> future = webSocketClient.doHandshake(handler,
				"ws://localhost:8080/wampOverSockJS");
		future.addCallback(wss -> {
			start[0] = System.currentTimeMillis();
			for (int i = 0; i < 10_000; i++) {

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

		if (!latch.await(3, TimeUnit.MINUTES)) {
			System.out.println("SOMETHING WENT WRONG");
		}

		System.out.println((System.currentTimeMillis() - start[0]) / 1000 + " seconds");
		System.out.println("SUCCESS: " + handler.getSuccess());
		System.out.println("ERROR  : " + handler.getError());
	}

}
