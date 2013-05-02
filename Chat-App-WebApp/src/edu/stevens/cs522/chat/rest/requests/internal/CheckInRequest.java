package edu.stevens.cs522.chat.rest.requests.internal;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import android.os.Parcel;
import android.os.Parcelable;


public class CheckInRequest extends Request {
	
	private Set<String> subjects = new HashSet<String>();
	
	public Set<String> getSubjects() {
		return subjects;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		super.writeToParcel(out, flags);
		Request.writeStrings(out, subjects);
	}
	
	public CheckInRequest(URL url, Set<String> subjects) {
		super(url, RequestType.SUBSCRIBE);
		this.subjects = subjects;
	}
	
	public CheckInRequest(Parcel in) {
		super(in);
		Request.readStrings(in, subjects);
	}
	
	public static final Parcelable.Creator<CheckInRequest> CREATOR = new Parcelable.Creator<CheckInRequest>() {
		public CheckInRequest createFromParcel(Parcel in) {
			return new CheckInRequest(in);
		}

		public CheckInRequest[] newArray(int size) {
			return new CheckInRequest[size];
		}
	};
	
}
