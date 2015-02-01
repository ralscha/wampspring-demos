package ch.rasc.wampspring.demo.salmar;

/**
 *
 * @author Sergi Almar
 */
public class ChatMessage {

	private String username;
	private String message;
	private boolean priv = false;

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isPriv() {
		return this.priv;
	}

	public void setPriv(boolean priv) {
		this.priv = priv;
	}

	@Override
	public String toString() {
		return "ChatMessage [username=" + this.username + ", message=" + this.message
				+ ", priv=" + this.priv + "]";
	}

}
