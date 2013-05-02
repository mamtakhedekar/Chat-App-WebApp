package edu.stevens.cs522.chat.rest.requests.internal;

import java.net.URL;

import android.os.Parcel;
import android.os.Parcelable;


public class GetPeersRequest extends Request {
	
	private double radius;
	
	public double getRadius() {
		return radius;
	}
	
	public GetPeersRequest(URL url, double radius) {
		super(url, RequestType.RECEIVE);
		this.radius = radius;
	}
	
	public GetPeersRequest(Parcel in) {
		super(in);
		this.radius = in.readDouble();
	}
	
	@Override
	public void writeToParcel(Parcel out, int flags) {
		super.writeToParcel(out, flags);
		out.writeDouble(radius);
	}
	
	public static final Parcelable.Creator<GetPeersRequest> CREATOR = new Parcelable.Creator<GetPeersRequest>() {
		public GetPeersRequest createFromParcel(Parcel in) {
			return new GetPeersRequest(in);
		}

		public GetPeersRequest[] newArray(int size) {
			return new GetPeersRequest[size];
		}
	};

}
