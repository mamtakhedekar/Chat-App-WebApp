package edu.stevens.cs522.chat.rest.requests.internal;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/*
 * Web service requests.  Since we pass them via intents to the service,
 * they must implement Parcelable, which is much more efficient than Serializable.
 * See http://shri.blog.kraya.co.uk/2010/04/26/android-parcel-data-to-pass-between-activities-using-parcelable-classes/
 * for more information about implementing Parcelable.
 */

public abstract class Request implements Parcelable {
	
	private final static String TAG = Request.class.getCanonicalName();
		
	public static enum RequestType {
		SUBSCRIBE("subscribe"),
		PUBLISH("publish"),
		RECEIVE("receive"),
		PEERS("peers"),
		UNSUBSCRIBE("unsubscribe");
		private String value;
		public String value() {
			return value;
		}
		public static RequestType fromString(String v) {
			for (RequestType requestType : values()) {
				if (requestType.value.equals(v)) {
					return requestType;
				}
			}
			return null;
		}
		private RequestType(String v) {
			value = v;
		}
	}
	
	private RequestType requestType;
	
	public RequestType getRequestType() {
		return requestType;
	}
	
	private URL url;
	
	public URL getUrl() {
		return url;
	}
	
	private InternalCoordinates coordinates;
	
	public InternalCoordinates getCoordinates() {
		return coordinates;
	}
	
	public void setCoordinates(InternalCoordinates coordinates) {
		this.coordinates = coordinates;
	}

	public Request(URL url, RequestType requestType) {
		this.requestType = requestType;
		this.url = url;
	}
	
	public Request(Parcel in) {
		this.requestType = RequestType.fromString(in.readString());
		try {
			url = new URL(in.readString());
		} catch (MalformedURLException e) {
			Log.e(TAG, "Malformed URL: "+e);
		}
		this.coordinates = in.readParcelable(InternalCoordinates.class.getClassLoader());
	}
	
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(requestType.value());
		out.writeString(url.toString());
		out.writeParcelable(coordinates, flags);
	}
	
	public static void readStrings(Parcel in, Set<String> strings) {
		int numSubjects = in.readInt();
		for (int ix = 0; ix < numSubjects; ix++) {
			strings.add(in.readString());
		}
	}
	
	public static void writeStrings(Parcel out, Set<String> strings) {
		out.writeInt(strings.size());
		for (String s : strings) {
			out.writeString(s);
		}
	}

	public int describeContents() {
		return requestType.ordinal();
	}

}
