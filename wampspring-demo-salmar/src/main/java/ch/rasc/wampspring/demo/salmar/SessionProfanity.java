package ch.rasc.wampspring.demo.salmar;

import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author Sergi Almar
 */
public class SessionProfanity {

	private long maxProfanityLevel = Long.MAX_VALUE;

	private final AtomicLong profanityLevel = new AtomicLong();

	public SessionProfanity() {
	}

	public SessionProfanity(int maxProfanityLevel) {
		this.maxProfanityLevel = maxProfanityLevel;
	}

	public void increment(long partialProfanity) {
		if (this.profanityLevel.intValue() + partialProfanity >= this.maxProfanityLevel) {
			this.profanityLevel.set(this.maxProfanityLevel);
			throw new TooMuchProfanityException(
					"You reached the max profanity level. You are banned");
		}

		this.profanityLevel.addAndGet(partialProfanity);
	}
}
