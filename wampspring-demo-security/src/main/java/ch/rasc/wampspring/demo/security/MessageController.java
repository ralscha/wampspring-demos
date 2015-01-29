/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package ch.rasc.wampspring.demo.security;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import ch.rasc.wampspring.annotation.WampCallListener;
import ch.rasc.wampspring.annotation.WampSubscribeListener;
import ch.rasc.wampspring.annotation.WampUnsubscribeListener;
import ch.rasc.wampspring.config.WampSession;
import ch.rasc.wampspring.demo.security.data.ActiveWebSocketUser;
import ch.rasc.wampspring.demo.security.data.ActiveWebSocketUserRepository;
import ch.rasc.wampspring.demo.security.data.InstantMessage;
import ch.rasc.wampspring.demo.security.data.User;
import ch.rasc.wampspring.user.UserEventMessenger;

/**
 * Controller for managing {@link Message} instances.
 *
 * @author Rob Winch
 *
 */
@Controller
@RequestMapping("/")
public class MessageController {

	private final UserEventMessenger userEventMessenger;

	private final ActiveWebSocketUserRepository activeUserRepository;

	@Autowired
	public MessageController(ActiveWebSocketUserRepository activeUserRepository,
			UserEventMessenger userEventMessenger) {
		this.activeUserRepository = activeUserRepository;
		this.userEventMessenger = userEventMessenger;
	}

	@RequestMapping("")
	public String im() {
		return "index";
	}

	@WampCallListener("/im")
	public void im(InstantMessage im, @AuthenticationPrincipal User currentUser) {
		im.setFrom(currentUser.getEmail());

		Set<String> sendToUsers = new HashSet<>();
		sendToUsers.add(im.getTo());
		sendToUsers.add(im.getFrom());
		this.userEventMessenger.sendToUsers("/messages", im, sendToUsers);

		// messagingTemplate.convertAndSendToUser(im.getTo(), "/queue/messages", im);
		// messagingTemplate.convertAndSendToUser(im.getFrom(), "/queue/messages", im);
	}

	@WampCallListener("/users")
	public Map<String, Object> subscribeMessages(@AuthenticationPrincipal User currentUser) {
		Map<String, Object> result = new HashMap<>();
		result.put("me", currentUser.getEmail());
		result.put("friends", this.activeUserRepository.findAllActiveUsers());
		return result;
	}

	@WampSubscribeListener(value = "/messages", replyTo = "/signin", excludeSender = true)
	public String subscribeUser(@AuthenticationPrincipal User currentUser,
			WampSession session) {

		this.activeUserRepository.save(new ActiveWebSocketUser(session
				.getWebSocketSessionId(), currentUser.getEmail()));

		return currentUser.getEmail();
	}

	@WampUnsubscribeListener(value = "/messages", replyTo = "/signout",
			excludeSender = true)
	public String unsubscribeUser(WampSession session) {

		ActiveWebSocketUser user = this.activeUserRepository.findOne(session
				.getWebSocketSessionId());

		if (user == null) {
			return null;
		}

		this.activeUserRepository.delete(session.getWebSocketSessionId());
		return user.getUsername();
	}

}
