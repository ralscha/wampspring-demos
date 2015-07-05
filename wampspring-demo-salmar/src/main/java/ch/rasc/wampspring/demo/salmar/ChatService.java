package ch.rasc.wampspring.demo.salmar;

import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.rasc.wampspring.annotation.WampCallListener;
import ch.rasc.wampspring.annotation.WampSubscribeListener;
import ch.rasc.wampspring.annotation.WampUnsubscribeListener;
import ch.rasc.wampspring.user.UserEventMessenger;

/**
 *
 * @author Sergi Almar
 * @author Ralph Schaer
 */
@Service
public class ChatService {

	private final ProfanityChecker profanityFilter;

	private final SessionProfanity profanity;

	private final UserEventMessenger userEventMessenger;

	private final Set<String> participants = new HashSet<>();

	@Autowired
	public ChatService(ProfanityChecker profanityFilter, SessionProfanity profanity,
			UserEventMessenger userEventMessenger) {
		this.profanityFilter = profanityFilter;
		this.profanity = profanity;
		this.userEventMessenger = userEventMessenger;
	}

	@WampCallListener("getUsername")
	public String getUsername(Principal principal) {
		return principal.getName();
	}

	@WampCallListener("retrieveParticipants")
	public List<Map<String, String>> retrieveParticipants() {
		return this.participants.stream()
				.map(p -> Collections.singletonMap("username", p))
				.collect(Collectors.toList());
	}

	@WampSubscribeListener("/chat")
	public void subscribe(Principal principal) {
		this.participants.add(principal.getName());
		this.userEventMessenger.sendToAll("/chat.login", principal.getName());
	}

	@WampUnsubscribeListener("/chat")
	public void unsubscribe(Principal principal) {
		this.participants.remove(principal.getName());
		this.userEventMessenger.sendToAll("/chat.logout", principal.getName());
	}

	@WampCallListener("publishMessage")
	public void publishMessage(String message, Principal principal) {
		String sanitizedMessage = checkProfanityAndSanitize(message);

		ChatMessage chatMessage = new ChatMessage();
		chatMessage.setMessage(sanitizedMessage);
		chatMessage.setUsername(principal.getName());
		this.userEventMessenger.sendToAll("/chat", chatMessage);
	}

	@WampCallListener("publishPrivateMessage")
	public void publishPrivateMessage(String message, String username,
			Principal principal) {
		String sanitizedMessage = checkProfanityAndSanitize(message);

		ChatMessage chatMessage = new ChatMessage();
		chatMessage.setMessage(sanitizedMessage);
		chatMessage.setUsername(principal.getName());
		chatMessage.setPriv(true);
		this.userEventMessenger.sendToUser("/chat", chatMessage, username);
	}

	private String checkProfanityAndSanitize(String message) {
		long profanityLevel = this.profanityFilter.getMessageProfanity(message);
		this.profanity.increment(profanityLevel);
		return this.profanityFilter.filter(message);
	}

}