package edu.stevens.cs522.chat.rest.requests;

import java.util.ArrayList;
import java.util.List;


public class GetPeersResponse {
	
	public static class Peer {
		private String peer;
		private String host;
		private int port;
		private double latitude;
		private double longitude;
		
		public String getPeer() {
			return peer;
		}
		public void setPeer(String peer) {
			this.peer = peer;
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
		public double getLatitude() {
			return latitude;
		}
		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}
		public double getLongitude() {
			return longitude;
		}
		public void setLongitude(double longitude) {
			this.longitude = longitude;
		}
		public Peer() { }
	}
	
	private List<Peer> peers = new ArrayList<Peer>();
	
	public List<Peer> getPeers() {
		return peers;
	}
	
	public GetPeersResponse() {
	}

}
