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
import android.content.ContentResolver;
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
	
	private static final int LOADER_ID = 0;	
	private static final int URL_LOADER = 0;
	
	private int lastSeqNumber = 0;
	// The callbacks through which we will interact with the LoaderManager.
	private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;	

	/*
	 * UI.
	 */
	private EditText message;
	private EditText serviceUrl;
	private Button checkIn;
	private Button postMessage;
	private Button getMessages;
	private Button checkOut;
	private ListView msgList;
	
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
		String[] to = new String[] { ChatContent.Messages.MESSAGE };
        int[] from = new int[] { R.id.messages_message };
        
		mCallbacks = this;     
        LoaderManager lm = getLoaderManager();
        lm.initLoader(LOADER_ID, null, mCallbacks);
        
		messageAdapter = new SimpleCursorAdapter(
        		this,       // Context.
                R.layout.messages_row,  // Specify the row template to use 
                null,          // Cursor encapsulates the DB query result.
                to, 		// Array of cursor columns to bind to.
                from, 0);
		
		// Bind to our new adapter.
        msgList = (ListView)findViewById(R.id.msgList);
        msgList.setAdapter(this.messageAdapter);
		
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
	public class Receiver extends BroadcastReceiver {
		
		public Receiver()
		{
			Log.i("Receiver", "Created BroadcastReceiver" );
		}
		
		@Override 
		public void onReceive(Context context, Intent intent) {
		      // react to the event
			String action = intent.getAction();
			if(action.equalsIgnoreCase(ChatService.NEW_MESSAGE_BROADCAST)){  
			messageAdapter.changeCursor(makeMessageCursor());
		   }
		}
	}	
	

	protected Cursor makeMessageCursor () {
	
		String[] projection = 
				new String[] { ChatContent.Messages._ID,
							   ChatContent.Messages.SENDER, 
							   ChatContent.Messages.MESSAGE };
		ContentResolver cr = getContentResolver();
		Cursor c = cr.query(ChatContent.Messages.CONTENT_URI,
		        projection, null, null, null);		
		return c;
	}
		
	/*
	 * End Todo
	 */

	Receiver updater; 
	/*
	 * TODO: Loader manager for messages content provider.
	 */

	public Loader<Cursor> onCreateLoader(int paramInt, Bundle paramBundle) {
		String[] projection = 
				new String[] { ChatContent.Messages._ID,
							   ChatContent.Messages.SENDER, 
							   ChatContent.Messages.MESSAGE };
		switch
		(LOADER_ID)
		{
		case URL_LOADER:
			return new CursorLoader(this, ChatContent.Messages.CONTENT_URI, projection, null, null, null);
		default:
			return null;		//An invalid id was passed in
		}
	}

	public void onLoadFinished(Loader<Cursor> paramLoader, Cursor paramD) {
		messageAdapter.changeCursor(paramD);
		
	}

	public void onLoaderReset(Loader<Cursor> paramLoader) {
		messageAdapter.changeCursor(null);
	}
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
	@Override
	protected void onPause() {
	   unregisterReceiver(updater);
	   super.onPause();
	}

	@Override
	protected void onResume() {
	   this.updater = new Receiver();
	   registerReceiver(
	         this.updater, 
	         new IntentFilter(
	        		 ChatService.NEW_MESSAGE_BROADCAST));
	   super.onResume();
	}
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