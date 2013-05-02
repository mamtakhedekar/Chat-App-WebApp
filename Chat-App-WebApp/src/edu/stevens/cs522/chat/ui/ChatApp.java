/*********************************************************************

    Chat app: exchange messages with other instances of the app.
    
    Copyright (c) 2012 Stevens Institute of Technology

 **********************************************************************/

package edu.stevens.cs522.chat.ui;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import edu.stevens.cs522.chat.R;
import edu.stevens.cs522.chat.network.INetworkInfo;
import edu.stevens.cs522.chat.network.NetworkInfoService;
import edu.stevens.cs522.chat.providers.ChatContent;
import edu.stevens.cs522.chat.rest.service.ChatService;
import edu.stevens.cs522.chat.rest.service.ServiceHelper;

public class ChatApp extends Activity implements LoaderManager.LoaderCallbacks<Cursor>, INetworkInfo {
	
	final static public String TAG = ChatApp.class.getCanonicalName();

	/*
	 * Adapter for displaying received messages.
	 */
	CursorAdapter messageAdapter;
	
	private int lastSeqNumber = 0;

	/*
	 * UI.
	 */
	EditText message;
	EditText serviceUrl;
	Button checkIn;
	Button postMessage;
	Button getMessages;
	Button checkOut;
	
	Set<String> subjects;
	
	/*
	 * Service helper (part of the REST pattern for Android).
	 */
	private ServiceHelper serviceHelper;
	
	public ChatApp() {
		serviceHelper = new ServiceHelper(this, this);
	}

	/*
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		String[] subjectArray = getResources().getStringArray(R.array.subjects);
		subjects = new HashSet<String>();
		for (String s : subjectArray) {
			subjects.add(s);
		}
		
		serviceUrl = (EditText) findViewById(R.id.web_service_url);

		message = (EditText) findViewById(R.id.message_text);

		/*
		 * TODO: Messages content provider should be linked to the listview
		 * named "msgList" in the UI:
		 * 1. Use a SimpleCursorAdapter to create an adapter with a null cursor.
		 * 2. Use messages_row layout for the list of messages
		 * 3. Start a loader to load the cursor.
		 */
		/*
		 * The list adapter displays the visible parts of the cursor.
		 */

		/*
		 * End Todo
		 */

		checkIn = (Button) findViewById(R.id.checkin_button);
		checkIn.setOnClickListener(checkInListener);

		postMessage = (Button) findViewById(R.id.post_message_button);
		postMessage.setOnClickListener(postMessageListener);

		getMessages = (Button) findViewById(R.id.get_messages_button);
		getMessages.setOnClickListener(getMessagesListener);

		checkOut = (Button) findViewById(R.id.checkout_button);
		checkOut.setOnClickListener(checkOutListener);
		
//		/*
//		 * Get our network address
//		 */
//		Intent networkInfoLookup = new Intent(this, NetworkInfoService.class);
//		startService(networkInfoLookup);
	}
	
	/*
	 * TODO: Since the content provider for messages received is now updated on a background
	 * thread, it sends a broadcast to the UI to tell it to update the cursor.  The UI
	 * should register a broadcast receiver that will change the cursor for the messages adapter.
	 */

	/*
	 * End Todo
	 */

	
	/*
	 * TODO: Loader manager for messages content provider.
	 */


	/*
	 * End To Do
	 */
	
	private String thisMachine;
	
	public void setLocalAddress(String address) {
		thisMachine = address;
	}
	
	public String getLocalAddress() {
		return thisMachine;
	}

//	/*
//	 * TODO: Broadcast receiver for a broadcast that provides the network
//	 * address of this device (see NetworkInfoService).  It should be
//	 * registered in onResume and unregistered in onDestroy.
//	 */
//
//	/*
//	 * End Todo
//	 */

	/*
	 * On click listener for the send button
	 */
	private OnClickListener checkInListener = new OnClickListener() {
		public void onClick(View v) {
			checkIn();
		}
	};

	private OnClickListener postMessageListener = new OnClickListener() {
		public void onClick(View v) {
			postMessage();
		}
	};

	private OnClickListener getMessagesListener = new OnClickListener() {
		public void onClick(View v) {
			getMessages();
		}
	};

	private OnClickListener checkOutListener = new OnClickListener() {
		public void onClick(View v) {
			checkOut();
		}
	};

	/*
	 * Send the message in the message EditText
	 */
	private void checkIn() {
		Editable url = serviceUrl.getText();
		serviceHelper.checkInRequest(url==null ? null : url.toString(), subjects);
	}

	private void postMessage() {
		Editable url = serviceUrl.getText();
		String theNewMessage = message.getText().toString();
		serviceHelper.postMessageRequest(url==null ? null : url.toString(), subjects, theNewMessage);
		message.setText("");
	}

	private void getMessages() {
		Editable url = serviceUrl.getText();
		serviceHelper.getMessagesRequest(url==null ? null : url.toString(), lastSeqNumber, 1000);
	}

	private void checkOut() {
		Editable url = serviceUrl.getText();
		serviceHelper.checkOutRequest(url==null ? null : url.toString());
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/*
	 * Options menu includes an option to list all peers from whom we have
	 * received communication.
	 */

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		Intent i;

		switch (item.getItemId()) {
		case (R.id.show_peers):
			i = new Intent(this, ShowPeers.class);
			startActivity(i);
			return true;
		}
		return false;
	}

}