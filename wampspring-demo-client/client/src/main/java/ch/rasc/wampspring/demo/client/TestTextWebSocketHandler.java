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

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.JsonFactory;

import ch.rasc.wampspring.message.CallErrorMessage;
import ch.rasc.wampspring.message.CallResultMessage;
import ch.rasc.wampspring.message.WampMessage;
import ch.rasc.wampspring.message.WelcomeMessage;

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
