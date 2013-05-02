package edu.stevens.cs522.chat.rest.requests;

public class GetMessagesRequest extends Request {
	
	private int seqNumber;
	
	public int getSeqNumber() {
		return seqNumber;
	}
	
	private double radius;
	
	public double getRadius() {
		return radius;
	}
	
	public GetMessagesRequest(int seqNumber, double radius) {
		super(RequestType.RECEIVE);
		this.seqNumber = seqNumber;
		this.radius = radius;
	}
	
}
