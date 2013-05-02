package edu.stevens.cs522.chat.rest.requests;


public class HttpCoordinates {
	
	@SuppressWarnings("unused")
	private final static String TAG = HttpCoordinates.class.getCanonicalName();
	
	/*
	 * Every message that is exchanged has both physical and digital coordinates
	 * of the sender.
	 */
	
	private String peer;
	
	private String host;
	
	private int port;
	
	private double longitude;
	
	private double latitude;
	
	public String getPeer() {
		return peer;
	}

	public void setPeer(String peer) {
		this.peer = peer;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public HttpCoordinates() {
	}
	
}
