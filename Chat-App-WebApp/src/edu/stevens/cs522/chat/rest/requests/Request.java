package edu.stevens.cs522.chat.rest.requests;

/*
 * Web service requests.  Since we pass them via intents to the service,
 * they must implement Parcelable, which is much more efficient than Serializable.
 * See http://shri.blog.kraya.co.uk/2010/04/26/android-parcel-data-to-pass-between-activities-using-parcelable-classes/
 * for more information about implementing Parcelable.
 */

public abstract class Request  {
	
	@SuppressWarnings("unused")
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
	
	private HttpCoordinates coordinates;
	
	public HttpCoordinates getCoordinates() {
		return coordinates;
	}
	
	public void setCoordinates(HttpCoordinates coordinates) {
		this.coordinates = coordinates;
	}

	public Request(RequestType requestType) {
		this.requestType = requestType;
	}
	
}
