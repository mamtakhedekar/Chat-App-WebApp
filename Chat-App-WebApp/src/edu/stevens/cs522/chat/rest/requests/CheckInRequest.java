package edu.stevens.cs522.chat.rest.requests;

import java.util.HashSet;
import java.util.Set;


public class CheckInRequest extends Request {
	
	private Set<String> subjects = new HashSet<String>();
	
	public Set<String> getSubjects() {
		return subjects;
	}

	public CheckInRequest(Set<String> subjects) {
		super(RequestType.SUBSCRIBE);
		this.subjects = subjects;
	}
	
}
