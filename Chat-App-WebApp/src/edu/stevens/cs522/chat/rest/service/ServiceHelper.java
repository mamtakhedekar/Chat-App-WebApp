package edu.stevens.cs522.chat.rest.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import edu.stevens.cs522.chat.R;
import edu.stevens.cs522.chat.network.INetworkInfo;
import edu.stevens.cs522.chat.rest.requests.internal.CheckInRequest;
import edu.stevens.cs522.chat.rest.requests.internal.CheckOutRequest;
import edu.stevens.cs522.chat.rest.requests.internal.GetMessagesRequest;
import edu.stevens.cs522.chat.rest.requests.internal.InternalCoordinates;
import edu.stevens.cs522.chat.rest.requests.internal.PostMessageRequest;
import edu.stevens.cs522.chat.rest.requests.internal.Request;


public class ServiceHelper {
	
	private final static String TAG = ServiceHelper.class.getCanonicalName();
	
	private Activity ui;
	private INetworkInfo networkInfo;
	
	public ServiceHelper (Activity ui, INetworkInfo networkInfo) {
		this.ui = ui;
		this.networkInfo = networkInfo;
	}
	
	private URL webServiceUrl = null;
	
	private URL getWebServiceUrl() {
		if (webServiceUrl == null)
			setWebServiceUrl(ui.getString(R.string.target_url_default));
		return webServiceUrl;
	}
	
	public void setWebServiceUrl(String url) {
		try {
			webServiceUrl = url==null ? null : new URL(url);
		} catch (MalformedURLException e) {
			Log.e(TAG, "Malformed URL for Web service: "+e.getMessage());
		}
	}
	
	public void checkInRequest(String url, Set<String> subjects) {
		setWebServiceUrl(url);
		CheckInRequest request = new CheckInRequest(getWebServiceUrl(), subjects);
		for (String subject : subjects) {
			request.getSubjects().add(subject);
		}
		request.setCoordinates(getCoordinates());
		addRequest(request);
	}
	
	public void checkOutRequest(String url) {
		setWebServiceUrl(url);
		CheckOutRequest request = new CheckOutRequest(getWebServiceUrl());
		request.setCoordinates(getCoordinates());
		addRequest(request);
	}

	public void getMessagesRequest(String url, int seqNumber, double radius) {
		/*
		 * TODO: send a Get Messages request.
		 */
		setWebServiceUrl(url);
		GetMessagesRequest request = new GetMessagesRequest(getWebServiceUrl(),seqNumber,radius);
		request.setCoordinates(getCoordinates());
		addRequest(request);
		/*
		 * End To do
		 */
	}

	public void postMessageRequest(String url, Set<String> subjects, String message) {
		/*
		 * TODO: send a Post Message request.
		 */
		setWebServiceUrl(url);
		PostMessageRequest request = new PostMessageRequest(getWebServiceUrl(), subjects, message);
		request.setCoordinates(getCoordinates());
		addRequest(request);
		/*
		 * End To do
		 */
	}
	
	private InternalCoordinates getCoordinates() {
		InternalCoordinates coordinates = new InternalCoordinates();
		coordinates.setPeer(ui.getString(R.string.user_name));
		coordinates.setLatitude(Double.parseDouble(ui.getString(R.string.latitude)));
		coordinates.setLongitude(Double.parseDouble(ui.getString(R.string.longitude)));
		coordinates.setHost(networkInfo.getLocalAddress());
		coordinates.setPort(Integer.parseInt(ui.getString(R.string.app_port)));
		return coordinates;
	}
	
	private synchronized void addRequest(Request request) {
		/*
		 * TODO: Use an intent to send the request to a background service.
		 * The request is included as a Parcelable extra in the intent.
		 * The key for the intent extra is in the ChatService class.
		 */
		Intent serviceIntent = new Intent(ui.getApplicationContext(), ChatService.class);
		serviceIntent.putExtra(ChatService.WEB_SERVICE_REQUEST_KEY, request);
		ui.startService(serviceIntent);
		
		/*`
		 * End Todo
		 */
	}
	
}
