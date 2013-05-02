/*********************************************************************

    Chat service: accept chat messages from other peers.
    
    Sender name and GPS coordinates are encoded
    in the messages, and stripped off upon receipt.

    Copyright (c) 2012 Stevens Institute of Technology

 **********************************************************************/

package edu.stevens.cs522.chat.rest.service;

import android.app.IntentService;
import android.content.Intent;
import edu.stevens.cs522.chat.rest.requests.internal.Request;

public class ChatService extends IntentService  {
	

	/*
	 * The global chat service uses a Web service to exchange chat messages via the cloud.
	 * We assume that the Web requests are performed serially in the app, which allows us
	 * to use the IntentService class to delegate background thread handling.  Requests
	 * are sent to the service via intents, and we just have to provide the background
	 * thread operation handleIntent, which processes each request message in turn.
	 * This operation runs on a background thread, all of the service implementation
	 * is provided for us in the IntentService class.  We could bind to the service,
	 * but in this case it is pointless.  It would be useful e.g. if we gave the client
	 * the ability to interrupt an in-progress Web service request.
	 */

	public static final String NEW_MESSAGE_BROADCAST = "edu.stevens.cs522.chat.NewMessageBroadcast";
	
	public static final String WEB_SERVICE_REQUEST_KEY = "edu.stevens.cs522.chat.WebRequest";

	
	public ChatService() {
		super(ChatService.class.getCanonicalName());
	}
	
	/*
	 * This executes on a background thread managed by the service.
	 */
	public void onHandleIntent(Intent intent) {
		/*
		 * TODO: Extract the request message as a parcelable extra from the intent.
		 * Instantiate the Web request processor class and dispatch this request.
		 */

		/*
		 * End To do
		 */
	}


}
