package edu.stevens.cs522.chat.rest.requests;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GetMessagesResponse {
	
	public static class Metadata {
		public String peer;
		public double latitude;
		public double longitude;
		public int seqNumber;
		public Set<String> tags;		
	}
	
	public static class Message {
		public Metadata metadata;
		public String text;
		public Message(String text, Metadata metadata) {
			this.metadata = metadata;
			this.text = text;
		}
	}
	
	private List<Message> message = new ArrayList<Message>();
	
	public List<Message> getMessages() {
		return message;
	}
	
	public GetMessagesResponse() {
	}

}
