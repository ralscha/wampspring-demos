package ch.rasc.wampspring.demo.various.map;

public class LatLng {
	private final double lat;

	private final double lng;

	public LatLng(String latLngString) {
		int posOfComma = latLngString.indexOf(",");

		this.lat = Double.parseDouble(latLngString.substring(0, posOfComma).trim());
		this.lng = Double.parseDouble(latLngString.substring(posOfComma + 1).trim());
	}

	public double getLat() {
		return lat;
	}

	public double getLng() {
		return lng;
	}

}
