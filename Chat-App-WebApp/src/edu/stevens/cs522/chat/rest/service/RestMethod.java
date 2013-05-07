package edu.stevens.cs522.chat.rest.service;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.util.Log;

import com.google.gson.Gson;

import edu.stevens.cs522.chat.rest.requests.CheckInRequest;
import edu.stevens.cs522.chat.rest.requests.GetMessagesResponse;
import edu.stevens.cs522.chat.rest.requests.GetPeersResponse;
import edu.stevens.cs522.chat.rest.requests.HttpCoordinates;
import edu.stevens.cs522.chat.rest.requests.internal.PostMessageRequest;

public class RestMethod {

	private final static String TAG = RestMethod.class.getCanonicalName();

	private final static String GET_METHOD = "GET";
	private final static String PUT_METHOD = "PUT";
	private final static String POST_METHOD = "POST";
	private final static String DELETE_METHOD = "DELETE";

	private Context context;
	private String userAgent;

	public RestMethod(Context context) {
		this.context = context;
		this.userAgent = buildUserAgent(context);
	}

	/*
	 * Keep these in sync with the URLs provided by the Web service.
	 */
	// PUT
	private URL checkInUrl(URL baseUrl, String id) {
		try {
			return new URL(baseUrl + "/chat/" + id);
		} catch (MalformedURLException e) {
			Log.e(TAG, "Malformed URL for CheckIn: " + e.getMessage());
			return null;
		}
	}

	// DELETE
	private URL checkOutUrl(URL baseUrl, String id) {
		try {
			return new URL(baseUrl + "/chat/" + id);
		} catch (MalformedURLException e) {
			Log.e(TAG, "Malformed URL for CheckOut: " + e.getMessage());
			return null;
		}
	}

	// POST
	private URL pushMessageUrl(URL baseUrl, String id, String message) {
		try {
			return new URL(baseUrl + "/chat/" + id + "/messages/" + message);
		} catch (MalformedURLException e) {
			Log.e(TAG, "Malformed URL for Publish: " + e.getMessage());
			return null;
		}
	}

	// GET
	private URL getMessagesUrl(URL baseUrl, String id, int seq, double radius) {
		try {
			return new URL(baseUrl + "/chat/" + id + "/messages?seq=" + seq
					+ "&radius=" + radius);
		} catch (MalformedURLException e) {
			Log.e(TAG, "Malformed URL for Messages: " + e.getMessage());
			return null;
		}
	}

	// GET
	private URL getPeersUrl(URL baseUrl, String id, double radius) {
		try {
			return new URL(baseUrl + "/chat/" + id + "/peers?radius=" + radius);
		} catch (MalformedURLException e) {
			Log.e(TAG, "Malformed URL for Peers: " + e.getMessage());
			return null;
		}
	}

	// PUT
	public void checkIn(String id, HttpCoordinates coordinates, URL baseUrl,
			Set<String> subjects) {
		try {
			URL url = checkInUrl(baseUrl, id);
			CheckInRequest request = new CheckInRequest(subjects);
			request.setCoordinates(coordinates);
			executePushRequest(CheckInRequest.class, url, PUT_METHOD, request);
		} catch (IOException e) {
			Log.e(TAG, "Web service error while checking in: " + e);
		}
	}

	// DELETE
	public void checkOut(String id, URL baseUrl) {
		try {
			URL url = checkOutUrl(baseUrl, id);
			executeRequest(url, DELETE_METHOD);
		} catch (IOException e) {
			Log.e(TAG, "Web service error while checking out: " + e);
		}
	}

	// POST
	public void postMessage(String id, HttpCoordinates coordinates,
			URL baseUrl, Set<String> tags, String message) {
		/*
		 * TODO: Execute a POST request to send a message to the server.
		 */
		try {
			URL url = pushMessageUrl(baseUrl, id, message);
		//executeRequest(url, POST_METHOD);
			edu.stevens.cs522.chat.rest.requests.PostMessageRequest postMsg = 
				new edu.stevens.cs522.chat.rest.requests.PostMessageRequest( new HashSet(), message);
		
			executePushRequest(edu.stevens.cs522.chat.rest.requests.PostMessageRequest.class, url, POST_METHOD, postMsg);
		} catch (IOException e) {
			Log.e(TAG, "Web service error while posting a new message: " + e);
		}
		/*
		 * End To do
		 */
	}

	// GET
	public GetMessagesResponse getMessages(String id, URL baseUrl, int seq,
			double radius) {
		/*
		 * TODO: Execute a GET request to download recently posted messages.
		 */
		try {
			URL url = getMessagesUrl(baseUrl, id, seq, radius);
			GetMessagesResponse response = executePullRequest(GetMessagesResponse.class,
					url, GET_METHOD);
			return response;
		} catch (IOException e) {
			Log.e(TAG, "Web service error while getting local peer locations: "
					+ e);
			return null;
		}
		/*
		 * End To do
		 */
	}

	// GET
	public GetPeersResponse getPeers(String id, URL baseUrl, double radius) {
		try {
			URL url = getPeersUrl(baseUrl, id, radius);
			GetPeersResponse response = executePullRequest(GetPeersResponse.class,
					url, GET_METHOD);
			return response;
		} catch (IOException e) {
			Log.e(TAG, "Web service error while getting local peer locations: "
					+ e);
			return null;
		}
	}

	public void executeRequest(URL requestUrl, String method)
			throws IOException {

		/*
		 * It would of course be better if we queued the request until we had
		 * internet available.
		 */
		if (!isOnline()) {
			Log.v(TAG, "No-op with no connection: " + method + " " + requestUrl);
		}

		HttpURLConnection urlConnection = (HttpURLConnection) requestUrl
				.openConnection();
		urlConnection.setRequestProperty("User-Agent", userAgent);
		// urlConnection.setRequestProperty("Authorization", "Bearer " +
		// authToken);
		urlConnection.setDoOutput(false);
		urlConnection.setDoInput(false);
		urlConnection.setRequestMethod(method);

		Log.d(TAG, "Posting to URL: " + requestUrl);

		urlConnection.connect();
		throwErrors(urlConnection);
	}

	public <T> T executePullRequest(Class<T> clazz, URL requestUrl,
			String method) throws IOException {

		/*
		 * It would of course be better if we queued the request until we had
		 * internet available.
		 */
		if (!isOnline()) {
			Log.v(TAG, "No-op with no connection: " + method + " " + requestUrl);
			return null;
		}

		HttpURLConnection urlConnection = (HttpURLConnection) requestUrl
				.openConnection();
		urlConnection.setRequestProperty("User-Agent", userAgent);
		// urlConnection.setRequestProperty("Authorization", "Bearer " +
		// authToken);
		urlConnection.setDoOutput(false);
		urlConnection.setDoInput(true);
		urlConnection.setRequestMethod(method);

		Log.d(TAG, "Posting to URL: " + requestUrl);

		urlConnection.connect();
		throwErrors(urlConnection);
		String json = readInputStream(urlConnection.getInputStream());
		T response = new Gson().fromJson(json, clazz);
		return response;
	}

	public <T> void executePushRequest(Class<T> clazz, URL requestUrl,
			String method, T entity) throws IOException {

		/*
		 * It would of course be better if we queued the request until we had
		 * internet available.
		 */
		if (!isOnline()) {
			Log.v(TAG, "No-op with no connection: " + method + " " + requestUrl);
			return;
		}

		byte[] postJsonBytes = new Gson().toJson(entity).getBytes();

		HttpURLConnection urlConnection = (HttpURLConnection) requestUrl
				.openConnection();
		urlConnection.setRequestProperty("User-Agent", userAgent);
		urlConnection.setRequestProperty("Content-Type", "application/json");
		// urlConnection.setRequestProperty("Authorization", "Bearer " +
		// authToken);
		urlConnection.setDoOutput(true);
		urlConnection.setRequestMethod(method);
		urlConnection.setFixedLengthStreamingMode(postJsonBytes.length);

		Log.d(TAG, "Posting to URL: " + requestUrl);
		OutputStream out = new BufferedOutputStream(
				urlConnection.getOutputStream());
		out.write(postJsonBytes);
		out.flush();

		urlConnection.connect();
		throwErrors(urlConnection);
	}

	private void throwErrors(HttpURLConnection urlConnection)
			throws IOException {
		final int status = urlConnection.getResponseCode();
		if (status < 200 || status >= 300) {
			String exceptionMessage = "Error response " + status + " "
					+ urlConnection.getResponseMessage()
					+ " for " + urlConnection.getURL();
			throw new IOException(exceptionMessage);
		}
	}

	private static String readInputStream(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String responseLine;
		StringBuilder responseBuilder = new StringBuilder();
		while ((responseLine = bufferedReader.readLine()) != null) {
			responseBuilder.append(responseLine);
		}
		return responseBuilder.toString();
	}

	private boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		return cm.getActiveNetworkInfo() != null
				&& cm.getActiveNetworkInfo().isConnectedOrConnecting();
	}

	/**
	 * Build and return a user-agent string that can identify this application
	 * to remote servers. Contains the package name and version code.
	 */
	private static String buildUserAgent(Context context) {
		String versionName = "unknown";
		int versionCode = 0;

		try {
			final PackageInfo info = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			versionName = info.versionName;
			versionCode = info.versionCode;
		} catch (PackageManager.NameNotFoundException ignored) {
		}

		return context.getPackageName() + "/" + versionName + " ("
				+ versionCode + ") (gzip)";
	}

}
