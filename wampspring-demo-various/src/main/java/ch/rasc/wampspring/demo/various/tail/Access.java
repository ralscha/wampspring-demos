package ch.rasc.wampspring.demo.various.tail;

public class Access {

	private String ip;

	private long date;

	private Double[] ll;

	private String message;

	private String city;

	private String country;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public Double[] getLl() {
		return ll;
	}

	public void setLl(Double[] ll) {
		this.ll = ll;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
