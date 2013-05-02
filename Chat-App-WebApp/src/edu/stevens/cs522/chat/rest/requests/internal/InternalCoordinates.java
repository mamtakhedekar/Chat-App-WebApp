package edu.stevens.cs522.chat.rest.requests.internal;

import android.os.Parcel;
import android.os.Parcelable;

public class InternalCoordinates implements Parcelable {
	
	@SuppressWarnings("unused")
	private final static String TAG = InternalCoordinates.class.getCanonicalName();
	
	/*
	 * Every message that is exchanged has both physical and digital coordinates
	 * of the sender.
	 */
	
	private String peer;
	
	private String host;
	
	private int port;
	
	private double longitude;
	
	private double latitude;
	
	public String getPeer() {
		return peer;
	}

	public void setPeer(String peer) {
		this.peer = peer;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void writeToParcel(Parcel out, int flags) {
		out.writeString(peer);
		out.writeString(host);
		out.writeInt(port);
		out.writeDouble(longitude);
		out.writeDouble(latitude);
	}

	public InternalCoordinates() {
	}
	
	public InternalCoordinates(Parcel in) {
		peer = in.readString();
		host = in.readString();
		port = in.readInt();
		longitude = in.readDouble();
		latitude = in.readDouble();
	}

	public static final Parcelable.Creator<InternalCoordinates> CREATOR = new Parcelable.Creator<InternalCoordinates>() {
		public InternalCoordinates createFromParcel(Parcel in) {
			return new InternalCoordinates(in);
		}

		public InternalCoordinates[] newArray(int size) {
			return new InternalCoordinates[size];
		}
	};

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

}
