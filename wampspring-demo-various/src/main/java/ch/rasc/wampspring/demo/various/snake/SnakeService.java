/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.rasc.wampspring.demo.various.snake;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.rasc.wampspring.EventMessenger;
import ch.rasc.wampspring.annotation.WampCallListener;
import ch.rasc.wampspring.annotation.WampSubscribeListener;
import ch.rasc.wampspring.annotation.WampUnsubscribeListener;

/**
 * Sets up the timer for the multi-player snake game WebSocket example.
 */
@Service
public class SnakeService {

	private final ConcurrentHashMap<Integer, Snake> snakes = new ConcurrentHashMap<>();

	private final EventMessenger eventMessenger;

	private Timer gameTimer;

	private final SnakeId currentSnakeId;

	@Autowired
	public SnakeService(SnakeId currentSnakeId, EventMessenger eventMessenger) {
		this.eventMessenger = eventMessenger;
		this.currentSnakeId = currentSnakeId;
	}

	@WampSubscribeListener(value = "snake", replyTo = "snake")
	public synchronized SnakeMessage addSnake() {
		Snake newSnake = new Snake(currentSnakeId);
		if (snakes.isEmpty()) {
			startTimer();
		}
		snakes.put(newSnake.getId(), newSnake);

		return SnakeMessage.createJoinMessage(createJoinData());
	}

	@WampUnsubscribeListener(value = "snake", replyTo = "snake")
	public synchronized SnakeMessage removeSnake() {
		try {
			Integer snakeId = currentSnakeId.getId();
			if (snakeId != null) {
				snakes.remove(snakeId);
				if (snakes.isEmpty()) {
					if (gameTimer != null) {
						gameTimer.cancel();
						gameTimer = null;
					}
				}

				return SnakeMessage.createLeaveMessage(snakeId);
			}
		}
		catch (BeanCreationException e) {	
			//A websocket session was destroyed from another example
			//The other example does not have the scope snakeId in it's session
			//so a call to currentSnakeId.getId() throws an error
		}

		return null;
	}

	public void startTimer() {
		gameTimer = new Timer(SnakeService.class.getSimpleName() + " Timer");
		gameTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				tick();

			}
		}, 100, 100);
	}

	public void tick() {
		Collection<Snake> allSnakes = getSnakes();
		List<Map<String, Object>> updateData = new ArrayList<>();
		for (Snake snake : allSnakes) {
			snake.update(allSnakes, eventMessenger);

			Map<String, Object> locationsData = snake.getLocationsData();
			if (locationsData != null) {
				updateData.add(locationsData);
			}
		}

		if (!updateData.isEmpty()) {
			eventMessenger.sendToAll("snake",
					SnakeMessage.createUpdateMessage(updateData));
		}
	}

	private Collection<Snake> getSnakes() {
		return Collections.unmodifiableCollection(snakes.values());
	}

	public List<Map<String, Object>> createJoinData() {
		List<Map<String, Object>> result = new ArrayList<>();
		for (Snake snake : getSnakes()) {
			Map<String, Object> es = new HashMap<>();
			es.put("id", snake.getId());
			es.put("color", snake.getHexColor());
			result.add(es);
		}
		return result;
	}

	@WampCallListener
	public void changeDirection(String message) {
		Snake snake = snakes.get(currentSnakeId.getId());
		if (snake != null) {
			if ("west".equals(message)) {
				snake.setDirection(Direction.WEST);
			}
			else if ("north".equals(message)) {
				snake.setDirection(Direction.NORTH);
			}
			else if ("east".equals(message)) {
				snake.setDirection(Direction.EAST);
			}
			else if ("south".equals(message)) {
				snake.setDirection(Direction.SOUTH);
			}
		}
	}

}
