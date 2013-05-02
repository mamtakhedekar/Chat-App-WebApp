package edu.stevens.cs522.chat.rest.requests.internal;

import java.net.URL;

import android.os.Parcel;
import android.os.Parcelable;


public class GetMessagesRequest extends Request {
	
	private int seqNumber;
	
	public int getSeqNumber() {
		return seqNumber;
	}
	
	private double radius;
	
	public double getRadius() {
		return radius;
	}
	
	public GetMessagesRequest(URL url, int seqNumber, double radius) {
		super(url, RequestType.RECEIVE);
		this.seqNumber = seqNumber;
		this.radius = radius;
	}
	
	public GetMessagesRequest(Parcel in) {
		super(in);
		seqNumber = in.readInt();
		radius = in.readDouble();
	}
	
	@Override
	public void writeToParcel(Parcel out, int flags) {
		super.writeToParcel(out, flags);
		out.writeInt(seqNumber);
		out.writeDouble(radius);
	}
	
	public static final Parcelable.Creator<GetMessagesRequest> CREATOR = new Parcelable.Creator<GetMessagesRequest>() {
		public GetMessagesRequest createFromParcel(Parcel in) {
			return new GetMessagesRequest(in);
		}

		public GetMessagesRequest[] newArray(int size) {
			return new GetMessagesRequest[size];
		}
	};

}
