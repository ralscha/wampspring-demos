package ch.rasc.wampspring.demo.security.data;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ActiveWebSocketUser {

	@Id
	private String id;

	private String username;

	private Date connectionTime;

	public ActiveWebSocketUser() {
		// default constructor
	}

	public ActiveWebSocketUser(String id, String username) {
		this.id = id;
		this.username = username;
		this.connectionTime = new Date();
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getConnectionTime() {
		return this.connectionTime;
	}

	public void setConnectionTime(Date connectionTime) {
		this.connectionTime = connectionTime;
	}

}
