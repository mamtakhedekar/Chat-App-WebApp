package edu.stevens.cs522.chat.network;

import java.net.InetAddress;
import java.net.UnknownHostException;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class NetworkInfoService extends IntentService {
	
	/*
	 * At start-up, get the network name of this device.
	 */
	
	public static final String LOCAL_HOST_NAME_LOOKUP = "edu.stevens.cs522.chat.HostNameLookup";

	public static final String LOCAL_HOST_NAME_BROADCAST = "edu.stevens.cs522.chat.HostNameLookupResult";
	
	public static final String LOCAL_HOST_NAME_KEY = "hostname";
	
	private static final String TAG = NetworkInfoService.class.getCanonicalName();
	
	public NetworkInfoService() {
		super(NetworkInfoService.class.getCanonicalName());
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		try {
			InetAddress host = InetAddress.getLocalHost();
			Log.d(TAG, "Network info service host name lookup: "+host.toString());
			Intent broadcast = new Intent(LOCAL_HOST_NAME_BROADCAST);
			broadcast.putExtra(LOCAL_HOST_NAME_KEY, host.toString());
			sendBroadcast(broadcast);
		} catch (UnknownHostException e) {
			Log.e(TAG, "Unknown host name: "+ e);
		}

	}

}
