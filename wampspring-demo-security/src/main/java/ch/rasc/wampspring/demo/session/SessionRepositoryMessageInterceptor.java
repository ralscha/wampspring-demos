/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.rasc.wampspring.demo.session;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.session.ExpiringSession;
import org.springframework.session.SessionRepository;
import org.springframework.util.Assert;

import ch.rasc.wampspring.message.WampMessage;

/**
 * <p>
 * Acts as a {@link ChannelInterceptor} to ensure the
 * {@link ExpiringSession#getLastAccessedTime()} is up to date.
 * </p>
 * <ul>
 *
 * @author Rob Winch
 * @author Ralph Schaer
 */
public final class SessionRepositoryMessageInterceptor<S extends ExpiringSession> extends
		ChannelInterceptorAdapter {

	private final SessionRepository<S> sessionRepository;

	/**
	 * Creates a new instance
	 *
	 * @param sessionRepository the {@link SessionRepository} to use. Cannot be null.
	 */
	public SessionRepositoryMessageInterceptor(SessionRepository<S> sessionRepository) {
		Assert.notNull(sessionRepository, "sessionRepository cannot be null");
		this.sessionRepository = sessionRepository;
	}

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		if (message == null) {
			return message;
		}

		if (message instanceof WampMessage) {
			String sessionId = ((WampMessage) message).getWampSession().getAttribute(
					SessionSupport.SPRING_SESSION_ID_ATTR_NAME);
			if (sessionId != null) {
				S session = this.sessionRepository.getSession(sessionId);
				if (session != null) {
					// update the last accessed time
					this.sessionRepository.save(session);
				}
			}
		}
		return super.preSend(message, channel);
	}

}