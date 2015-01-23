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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.rasc.wampspring.EventMessenger;

public class Snake {

	private static final int DEFAULT_LENGTH = 5;

	private final Integer id;

	private final String webSocketSessionId;

	private Direction direction;

	private int length = DEFAULT_LENGTH;

	private Location head;

	private Location lastHead;

	private final Deque<Location> tail = new ArrayDeque<>();

	private final String hexColor;

	public Snake(SnakeId snakeId) {
		this.id = snakeId.getId();
		this.webSocketSessionId = snakeId.getWebSocketSessionId();
		this.hexColor = SnakeUtils.getRandomHexColor();
		resetState();
	}

	private void resetState() {
		this.direction = Direction.NONE;
		this.head = SnakeUtils.getRandomLocation();
		this.tail.clear();
		this.length = DEFAULT_LENGTH;
	}

	private synchronized void kill(EventMessenger eventMessenger) {
		resetState();
		eventMessenger.sendTo("snake", SnakeMessage.createDeadMessage(),
				getWebSocketSessionId());
	}

	private synchronized void reward(EventMessenger eventMessenger) {
		this.length++;
		eventMessenger.sendTo("snake", SnakeMessage.createKillMessage(),
				getWebSocketSessionId());
	}

	public synchronized void update(Collection<Snake> snakes,
			EventMessenger eventMessenger) {
		Location nextLocation = this.head.getAdjacentLocation(this.direction);
		if (nextLocation.x >= SnakeUtils.PLAYFIELD_WIDTH) {
			nextLocation.x = 0;
		}
		if (nextLocation.y >= SnakeUtils.PLAYFIELD_HEIGHT) {
			nextLocation.y = 0;
		}
		if (nextLocation.x < 0) {
			nextLocation.x = SnakeUtils.PLAYFIELD_WIDTH;
		}
		if (nextLocation.y < 0) {
			nextLocation.y = SnakeUtils.PLAYFIELD_HEIGHT;
		}
		if (this.direction != Direction.NONE) {
			this.tail.addFirst(this.head);
			if (this.tail.size() > this.length) {
				this.tail.removeLast();
			}
			this.head = nextLocation;
		}

		handleCollisions(snakes, eventMessenger);
	}

	private void handleCollisions(Collection<Snake> snakes, EventMessenger eventMessenger) {
		for (Snake snake : snakes) {
			boolean headCollision = this.id != snake.id
					&& snake.getHead().equals(this.head);
			boolean tailCollision = snake.getTail().contains(this.head);
			if (headCollision || tailCollision) {
				kill(eventMessenger);
				if (this.id != snake.id) {
					snake.reward(eventMessenger);
				}
			}
		}
	}

	public synchronized Location getHead() {
		return this.head;
	}

	public synchronized Collection<Location> getTail() {
		return this.tail;
	}

	public synchronized void setDirection(Direction direction) {
		this.direction = direction;
	}

	public synchronized Map<String, Object> getLocationsData() {
		// Only create location data if it changed
		if (this.lastHead == null || !this.lastHead.equals(this.head)) {
			this.lastHead = this.head;

			List<Location> locations = new ArrayList<>();
			locations.add(this.head);
			locations.addAll(this.tail);

			Map<String, Object> es = new HashMap<>();
			es.put("id", getId());
			es.put("body", locations);
			return es;
		}

		return null;
	}

	public String getWebSocketSessionId() {
		return this.webSocketSessionId;
	}

	public Integer getId() {
		return this.id;
	}

	public String getHexColor() {
		return this.hexColor;
	}
}
