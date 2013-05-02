package edu.stevens.cs522.chat.rest.requests.internal;

import java.net.URL;

import android.os.Parcel;
import android.os.Parcelable;

public class CheckOutRequest extends Request {
	
	@Override
	public void writeToParcel(Parcel out, int flags) {
		super.writeToParcel(out, flags);
	}
	
	public CheckOutRequest(URL url) {
		super(url, RequestType.UNSUBSCRIBE);
	}

	public CheckOutRequest(Parcel in) {
		super(in);
	}
	
	public static final Parcelable.Creator<CheckOutRequest> CREATOR = new Parcelable.Creator<CheckOutRequest>() {
		public CheckOutRequest createFromParcel(Parcel in) {
			return new CheckOutRequest(in);
		}

		public CheckOutRequest[] newArray(int size) {
			return new CheckOutRequest[size];
		}
	};
	
}
