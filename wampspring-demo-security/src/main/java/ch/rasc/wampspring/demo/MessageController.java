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
package ch.rasc.wampspring.demo;

import java.security.Principal;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import ch.rasc.wampspring.EventMessenger;
import ch.rasc.wampspring.annotation.WampCallListener;
import ch.rasc.wampspring.annotation.WampSubscribeListener;
import ch.rasc.wampspring.annotation.WampUnsubscribeListener;
import ch.rasc.wampspring.demo.data.ActiveWebSocketUser;
import ch.rasc.wampspring.demo.data.ActiveWebSocketUserRepository;
import ch.rasc.wampspring.demo.data.InstantMessage;
import ch.rasc.wampspring.demo.data.User;
import ch.rasc.wampspring.handler.WampSession;

/**
 * Controller for managing {@link Message} instances.
 *
 * @author Rob Winch
 *
 */
@Controller
@RequestMapping("/")
public class MessageController {
	private final EventMessenger eventMessenger;
	private final ActiveWebSocketUserRepository activeUserRepository;

	@Autowired
	public MessageController(ActiveWebSocketUserRepository activeUserRepository,
			EventMessenger eventMessenger) {
		this.activeUserRepository = activeUserRepository;
		this.eventMessenger = eventMessenger;
	}

	@RequestMapping("")
	public String im() {
		return "index";
	}

	@WampCallListener("/im")
	public void im(InstantMessage im, @AuthenticationPrincipal User currentUser) {
		System.out.println(im);
		System.out.println(currentUser);
		// im.setFrom(currentUser.getEmail());
		// messagingTemplate.convertAndSendToUser(im.getTo(), "/queue/messages", im);
		// messagingTemplate.convertAndSendToUser(im.getFrom(), "/queue/messages", im);
	}

	@WampCallListener("/users")
	public List<String> subscribeMessages(Principal principal) {
		System.out.println(principal.getName());
		return this.activeUserRepository.findAllActiveUsers();
	}

	@WampSubscribeListener("/messages")
	public void subscribeUser(@AuthenticationPrincipal User currentUser,
			Principal principal, WampSession session) {
		System.out.println(currentUser);
		System.out.println(principal);
		this.activeUserRepository.save(new ActiveWebSocketUser(session.getSessionId(),
				principal.getName(), Calendar.getInstance()));
		this.eventMessenger.sendToAllExcept("/signin", principal.getName(), session.getSessionId());
	}

	@WampUnsubscribeListener("/messages")
	public void unsubscribeUser(WampSession session) {
		ActiveWebSocketUser user = this.activeUserRepository.findOne(session
				.getSessionId());
		if (user == null) {
			return;
		}

		this.activeUserRepository.delete(session.getSessionId());
		this.eventMessenger.sendToAllExcept("/signout", user.getUsername(), session.getSessionId());
	}

}
