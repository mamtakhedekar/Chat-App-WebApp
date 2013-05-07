package edu.stevens.cs522.chat.rest.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.Set;

import android.app.Service;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.util.Log;
import edu.stevens.cs522.chat.providers.ChatContent;
import edu.stevens.cs522.chat.rest.requests.GetMessagesResponse;
import edu.stevens.cs522.chat.rest.requests.GetPeersResponse;
import edu.stevens.cs522.chat.rest.requests.HttpCoordinates;
import edu.stevens.cs522.chat.rest.requests.internal.CheckInRequest;
import edu.stevens.cs522.chat.rest.requests.internal.CheckOutRequest;
import edu.stevens.cs522.chat.rest.requests.internal.GetMessagesRequest;
import edu.stevens.cs522.chat.rest.requests.internal.GetPeersRequest;
import edu.stevens.cs522.chat.rest.requests.internal.InternalCoordinates;
import edu.stevens.cs522.chat.rest.requests.internal.PostMessageRequest;
import edu.stevens.cs522.chat.rest.requests.internal.Request;

/*
 * The description of the logic that is performed on a background thread.
 */
public class WebRequestProcessor {
	
	private final static String TAG = WebRequestProcessor.class.getCanonicalName();
	/**
	 * 
	 */
	private final Service chatService;
	
	private final Context context;
	
	private final RestMethod restMethod;

	/**
	 * @param chatService
	 */
	WebRequestProcessor(Service chatService) {
		this.chatService = chatService;
		this.context = chatService.getApplicationContext();
		this.restMethod = new RestMethod(context);
	}
	
//	public WebRequestProcessor() {
//	}
	
	public void performRequest(Request request) {
		switch(request.getRequestType()) {
		case SUBSCRIBE:
			checkIn((CheckInRequest)request);
			break;
		case PUBLISH:
			postMessage((PostMessageRequest)request);
			break;
		case RECEIVE:
			getMessages((GetMessagesRequest)request);
			break;
		case PEERS:
			getPeers((GetPeersRequest)request);
			break;
		case UNSUBSCRIBE:
			checkOut((CheckOutRequest)request);
			break;
		}
	}
	
	private ContentResolver getContentResolver() {
		return chatService.getContentResolver();
	}
	
	private void sendBroadcast(Intent broadcastIntent) {
		chatService.sendBroadcast(broadcastIntent);
	}
	
	private void checkIn(CheckInRequest request) {
		/*
		 * TODO: Invoke the checkIn operation using the REST method.
		 */
		HttpCoordinates coordinates = getCoordinates(request);
		String id = coordinates.getPeer();
		URL baseUrl = request.getUrl();
		restMethod.checkIn(id, coordinates, baseUrl, request.getSubjects());

		/*
		 * End To do
		 */
	}
	
	private void postMessage(PostMessageRequest request) {
		/*
		 * TODO: Invoke the postMessage operation using the REST method.
		 */
		HttpCoordinates coordinates = getCoordinates(request);
		String id = coordinates.getPeer();
		URL baseUrl = request.getUrl();
		Set<String> tags = request.getSubjects();
		String message = request.getMessage();
		restMethod.postMessage(id, coordinates, baseUrl, tags, message);
		/*
		 * End To do
		 */
	}
	
	private void getMessages(GetMessagesRequest request) {
		HttpCoordinates coordinates = getCoordinates(request);
		String id = coordinates.getPeer();
		URL baseUrl = request.getUrl();
		int seqNumber = request.getSeqNumber();
		double radius = request.getRadius();
		GetMessagesResponse response = restMethod.getMessages(id, baseUrl, seqNumber, radius);
		
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		for (GetMessagesResponse.Message message : response.getMessages()) {
			addReceivedMessage(message, ops);
		}
		
		try {
			ContentResolver cr = getContentResolver();
//			cr.insert(ChatContent.Messages.CONTENT_URI, values);
			cr.applyBatch(ChatContent.Messages.AUTHORITY, ops);
		} catch (OperationApplicationException e) {
			Log.e(TAG, "Exception while performing batch message insertions: "+e);
		} catch (RemoteException e) {
			Log.e(TAG, "IPC failure while performing batch message insertion: "+e);
		}
		
		/*
		 * Logic for updating the Messages cursor is done on the UI thread.
		 */
		sendBroadcast(msgUpdateBroadcast);

	}
	
	private void getPeers(GetPeersRequest request) {
		HttpCoordinates coordinates = getCoordinates(request);
		String id = coordinates.getPeer();
		URL baseUrl = request.getUrl();
		double radius = request.getRadius();
		GetPeersResponse response = restMethod.getPeers(id, baseUrl, radius);
		
		/*
		 * TODO: Make this a batch insertion.
		 */
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		for (GetPeersResponse.Peer peer : response.getPeers()) {
			addSender(peer, ops);
		}
		
		try {
			ContentResolver cr = getContentResolver();
//			cr.insert(ChatContent.Messages.CONTENT_URI, values);
			cr.applyBatch(ChatContent.Peers.AUTHORITY, ops);
		} catch (OperationApplicationException e) {
			Log.e(TAG, "Exception while performing batch message insertions: "+e);
		} catch (RemoteException e) {
			Log.e(TAG, "IPC failure while performing batch message insertion: "+e);
		}
	}
	
	private void checkOut(CheckOutRequest request) {
		HttpCoordinates coordinates = getCoordinates(request);
		String id = coordinates.getPeer();
		URL baseUrl = request.getUrl();
		restMethod.checkOut(id, baseUrl);
	}
	
	private HttpCoordinates getCoordinates(Request request) {
		InternalCoordinates coord = request.getCoordinates();
		HttpCoordinates coordinates = new HttpCoordinates();
		coordinates.setLatitude(coord.getLatitude());
		coordinates.setLongitude(coord.getLongitude());
		coordinates.setPeer(coord.getPeer());
		return coordinates;
	}


	private Intent msgUpdateBroadcast = new Intent(ChatService.NEW_MESSAGE_BROADCAST);

	public void addReceivedMessage(GetMessagesResponse.Message msg, ArrayList<ContentProviderOperation> ops) {
		/*
		 * Add sender and message to the content provider for received messages.
		 */
		ContentValues values = new ContentValues();
		values.put(ChatContent.Messages.SENDER, msg.metadata.peer);
		values.put(ChatContent.Messages.MESSAGE, msg.text);
		
		ContentProviderOperation.Builder cpo = ContentProviderOperation.newInsert(ChatContent.Messages.CONTENT_URI);
		cpo.withValues(values);
		ops.add(cpo.build());
	}

	public void addSender(GetPeersResponse.Peer info,  ArrayList<ContentProviderOperation> ops) {

		/*
		 * Add sender information to content provider for peers
		 * information, if we have not already heard from them. If repeat
		 * message, update location information.
		 */
		ContentValues values = new ContentValues();
		values.put(ChatContent.Peers.NAME, info.getPeer());
		values.put(ChatContent.Peers.HOST, info.getHost());
		values.put(ChatContent.Peers.PORT, info.getPort());
		values.put(ChatContent.Peers.LATITUDE, info.getLatitude());
		values.put(ChatContent.Peers.LONGITUDE, info.getLongitude());

		//String[] projection = new String[] { ChatContent.Peers.NAME };
		//String where = ChatContent.Peers.NAME + "= ?";
		//String[] selectionArgs = new String[] { info.getPeer() };

		ContentProviderOperation.Builder cpo = ContentProviderOperation.newInsert(ChatContent.Messages.CONTENT_URI);
		cpo.withValues(values);
		ops.add(cpo.build());
		
		/*
		ContentResolver cr = getContentResolver();
		Cursor c = cr.query(ChatContent.Peers.CONTENT_URI, projection, where,
				selectionArgs, null);
		if (!c.moveToFirst()) {
			cr.insert(ChatContent.Peers.CONTENT_URI, values);
		} else {
			cr.update(ChatContent.Peers.CONTENT_URI, values, where,
					selectionArgs);
		}
		*/
	}
	

}