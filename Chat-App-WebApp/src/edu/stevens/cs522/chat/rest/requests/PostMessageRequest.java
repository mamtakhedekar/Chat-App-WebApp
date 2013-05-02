package edu.stevens.cs522.chat.rest.requests;

import java.util.HashSet;
import java.util.Set;

public class PostMessageRequest extends Request {

	private Set<String> subjects = new HashSet<String>();

	private String message;

	public String getMessage() {
		return message;
	}

	public Set<String> getSubjects() {
		return subjects;
	}

	public PostMessageRequest(Set<String> tags, String message) {
		super(RequestType.PUBLISH);
		this.subjects = tags;
		this.message = message;
	}
	
}
