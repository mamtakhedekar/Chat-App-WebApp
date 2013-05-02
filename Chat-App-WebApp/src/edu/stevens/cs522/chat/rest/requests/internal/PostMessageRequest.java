package edu.stevens.cs522.chat.rest.requests.internal;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import android.os.Parcel;
import android.os.Parcelable;

public class PostMessageRequest extends Request {

	private Set<String> subjects = new HashSet<String>();

	private String message;

	public String getMessage() {
		return message;
	}

	public Set<String> getSubjects() {
		return subjects;
	}

	public PostMessageRequest(URL url, Set<String> tags, String message) {
		super(url, RequestType.PUBLISH);
		this.subjects = tags;
		this.message = message;
	}
	
	public void writeToParcel(Parcel out, int flags) {
		super.writeToParcel(out, flags);
		Request.writeStrings(out, subjects);
		out.writeString(message);
	}

	public PostMessageRequest(Parcel in) {
		super(in);
		Request.readStrings(in, subjects);
		message = in.readString();
	}

	public static final Parcelable.Creator<PostMessageRequest> CREATOR = new Parcelable.Creator<PostMessageRequest>() {
		public PostMessageRequest createFromParcel(Parcel in) {
			return new PostMessageRequest(in);
		}

		public PostMessageRequest[] newArray(int size) {
			return new PostMessageRequest[size];
		}
	};
	
}
