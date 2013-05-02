package edu.stevens.cs522.chat.rest.requests;

public class GetPeersRequest extends Request {
	
	private double radius;
	
	public double getRadius() {
		return radius;
	}
	
	public GetPeersRequest(double radius) {
		super(RequestType.RECEIVE);
		this.radius = radius;
	}
	
}
