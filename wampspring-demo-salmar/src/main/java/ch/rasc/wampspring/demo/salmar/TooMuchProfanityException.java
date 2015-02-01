package ch.rasc.wampspring.demo.salmar;

/**
 *
 * @author Sergi Almar
 */
public class TooMuchProfanityException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TooMuchProfanityException(String message) {
		super(message);
	}
}
