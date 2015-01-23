package ch.rasc.wampspring.demo.security;

import org.springframework.messaging.Message;
import org.springframework.security.messaging.util.matcher.MessageMatcher;
import org.springframework.util.Assert;

import ch.rasc.wampspring.message.WampMessage;
import ch.rasc.wampspring.message.WampMessageType;

/**
 * A {@link MessageMatcher} that matches if the provided {@link Message} has a type that
 * is the same as the {@link WampMessageType} that was specified in the constructor.
 *
 * @author Rob Winch
 * @author Ralph Schaer
 *
 */
public class WampMessageTypeMatcher implements MessageMatcher<Object> {
	private final WampMessageType typeToMatch;

	/**
	 * Creates a new instance
	 *
	 * @param typeToMatch the {@link WampMessageType} that will result in a match. Cannot
	 * be null.
	 */
	public WampMessageTypeMatcher(WampMessageType typeToMatch) {
		Assert.notNull(typeToMatch, "typeToMatch cannot be null");
		this.typeToMatch = typeToMatch;
	}

	@Override
	public boolean matches(Message<? extends Object> message) {
		if (message instanceof WampMessage) {
			return ((WampMessage) message).getType() == this.typeToMatch;
		}
		return false;
	}
}