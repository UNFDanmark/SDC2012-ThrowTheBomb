package dk.unf.sdc.gruppea;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class SpilActivity extends Activity {

	private Spil spil;
	private TextView status;
	private Context mContext;
	private NetworkService netService;
	private BluetoothAdapter mBluetoothAdapter;
	private static final int REQUEST_ENABLE_BT = 1;
	private static final String tag = "BluetoothFrame";
	private ArrayList<BluetoothDevice> discoveredPeers;
	private AlertDialog serverDialog;
	private AlertDialog clientDialog;
	private EditText input_serverName;
	private EditText input_clientServerName;
	private String clientServerName;
	private Button server;
	private Button client;
	private Button kastBombe;
	private ImageView im;
	private View layout;
	private LinearLayout.LayoutParams kastBombeLP;

	// Message types sent from the BluetoothChatService Handler
	public static final int MESSAGE_READ = 1;
	public static final int MESSAGE_START = 2;
	public static final int MESSAGE_CONNECTED = 3;
	public static final int MESSAGE_UPDATEVIEW = 4;
	public static final int MESSAGE_LOST = 5;
	public static final int MESSAGE_SENDBOMB = 6;

	Vibrator vibrator;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		spil = new Spil(mHandler);
		setContentView(R.layout.activity_spil);
		status = (TextView) findViewById(R.id.status);
		// Don't
		// touch-----------------------------------------------------------------------
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			// Device does not support Bluetooth
		}
		netService = new NetworkService(mContext, mBluetoothAdapter, mHandler);
		IntentFilter deviceFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		Log.d(tag, "Registering deviceReceiver");
		registerReceiver(deviceReceiver, deviceFilter);
		IntentFilter adapterFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		Log.d(tag, "Registering adapterReceiver");
		registerReceiver(adapterReceiver, adapterFilter);
		server = (Button) findViewById(R.id.start_server_button);
		client = (Button) findViewById(R.id.start_client_button);
		serverDialog = new AlertDialog.Builder(this).create();
		clientDialog = new AlertDialog.Builder(this).create();
		input_serverName = new EditText(this);
		input_clientServerName = new EditText(this);
		layout = findViewById(R.id.layoutId);
		discoveredPeers = new ArrayList<BluetoothDevice>();
		// Touch
		// again-----------------------------------------------------------------------
		kastBombe = (Button) findViewById(R.id.kastBombe);
		kastBombe.setOnClickListener(sendListener);
		kastBombeLP = (LayoutParams) kastBombe.getLayoutParams();
		setIm((ImageView) findViewById(R.id.bombe_billede));
		server.setOnClickListener(startServerListener);
		client.setOnClickListener(startClientListener);
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		updateView();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(tag, "Unregistering deviceReceiver");
		unregisterReceiver(deviceReceiver);
		Log.d(tag, "Unregistering adapterReceiver");
		unregisterReceiver(adapterReceiver);
	}

	// Create a BroadcastReceiver for ACTION_FOUND
	private final BroadcastReceiver deviceReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// When discovery finds a device
			Log.d(tag, action);
			Log.d(tag, String.valueOf(BluetoothDevice.ACTION_FOUND.equals(action)));
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// Get the BluetoothDevice object from the Intent
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				try {
					discoveredPeers.add(device);
					Log.d(tag, device.getName() + " added to discovered list.");
				} catch (Exception e) {
					Log.d(tag, "Failed adding device to device list.");
				}
			}
		}
	};

	// Create a BroadcastReceiver for ACTION_DISCOVERY
	private final BroadcastReceiver adapterReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// When discovery finds a device
			Log.d(tag, action);
			if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				Log.d(tag, "Trying to connect as client.");
				boolean bo = false;
				for (BluetoothDevice device : discoveredPeers) {
					Log.d(tag, "DEVICE NAME: " + device.getName());
					if (device.getName().equals(clientServerName)) {
						Log.d(tag, "Discovered, OK.");
						netService.connect(device);
						bo = true;
					}
				}
				if (!bo) {
					Toast.makeText(getApplicationContext(), "Could not find server with name: " + clientServerName, Toast.LENGTH_SHORT).show();
				}
			}
		}
	};

	@Override
	protected void onActivityResult(int request, int result, Intent intent) {
		switch (request) {
		case REQUEST_ENABLE_BT:
			// When the request to enable Bluetooth returns
			if (result == Activity.RESULT_OK) {
				// Bluetooth is now enabled, so set up a chat session
				Toast.makeText(this, "BT Enabled", Toast.LENGTH_SHORT).show();
			} else {
				// User did not enable Bluetooth or an error occurred
				Log.d(tag, "BT not enabled");
				Toast.makeText(this, "BT Not Enabled", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private OnClickListener startServerListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			serverDialog.setTitle("Start server");
			serverDialog.setView(input_serverName);
			serverDialog.setMessage("Name your server:");
			serverDialog.setCancelable(false);
			serverDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Start server", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					if (!mBluetoothAdapter.isEnabled()) {
						Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
						startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
					}
					Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
					discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
					startActivity(discoverableIntent);
					mBluetoothAdapter.setName(input_serverName.getText().toString());
					Log.d(tag, "Bluetooth name set to: " + mBluetoothAdapter.getName());
					Log.d(tag, "Starting server.");
					netService.start();
				}
			});
			serverDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					serverDialog.cancel();
				}
			});
			serverDialog.show();
		}
	};

	private OnClickListener startClientListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			clientDialog.setTitle("Connect to server");
			clientDialog.setView(input_clientServerName);
			clientDialog.setMessage("Type which server you want to connect to:");
			clientDialog.setCancelable(false);
			clientDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Connect to server", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					if (!mBluetoothAdapter.isEnabled()) {
						Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
						startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
					}
					clientServerName = input_clientServerName.getText().toString();
					Log.d(tag, "clientServerName is: " + clientServerName);
					mBluetoothAdapter.startDiscovery();
				}
			});
			clientDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					clientDialog.cancel();
				}
			});
			clientDialog.show();
		}
	};
	/*
	 * HOLD JER FRA ALT OVER DENNE LINJE. HOLD JER FRA ALT OVER DENNE LINJE.
	 * HOLD JER FRA ALT OVER DENNE LINJE. HOLD JER FRA ALT OVER DENNE LINJE.
	 * HOLD JER FRA ALT OVER DENNE LINJE. HOLD JER FRA ALT OVER DENNE LINJE.
	 * HOLD JER FRA ALT OVER DENNE LINJE. HOLD JER FRA ALT OVER DENNE LINJE.
	 * HOLD JER FRA ALT OVER DENNE LINJE. HOLD JER FRA ALT OVER DENNE LINJE.
	 * HOLD JER FRA ALT OVER DENNE LINJE. HOLD JER FRA ALT OVER DENNE LINJE.
	 * (bortset fra måske onCreate())
	 */

	private OnClickListener restartListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			server.setVisibility(View.INVISIBLE);
			server.setEnabled(false);
			spil.restart();
			layout.setBackgroundColor(Color.WHITE);
		}
	};

	public void updateView() {
		// Skal være ansvarlig for at hente al information fra spilobjektet, så
		// den kan tegne/skrive osv.!
		// spil.getBlahBlah();
		if (spil.isServer() && !spil.isGameRunning()) {
			kastBombe.setOnClickListener(startBombeListener);
			kastBombe.setText("Start Bomben");
			getIm().setVisibility(View.VISIBLE);
			kastBombe.setVisibility(View.VISIBLE);
			kastBombe.setEnabled(true);
		}
		if (spil.isGameRunning() && spil.hasBomb()) {
			getIm().setVisibility(View.VISIBLE);
			kastBombe.setVisibility(View.VISIBLE);
			kastBombe.setEnabled(true);
		}
		if (!spil.isServer() && !spil.isGameRunning()) {
			getIm().setVisibility(View.INVISIBLE);
			kastBombe.setVisibility(View.INVISIBLE);
			kastBombe.setEnabled(false);
		}
		if (spil.isGameRunning() && !spil.hasBomb()) {
			getIm().setVisibility(View.INVISIBLE);
			kastBombe.setVisibility(View.INVISIBLE);
			kastBombe.setEnabled(false);
		}

	}

	private OnClickListener sendListener = new OnClickListener() { // sender
																	// bomben

		@Override
		public void onClick(View v) {
			spil.sendBomb();
			kastBombe.setVisibility(View.INVISIBLE);
			LinearLayout.LayoutParams lp = kastBombeLP;
			Random r = new Random();
			lp.leftMargin = r.nextInt(400)-200;
			lp.topMargin = r.nextInt(450)*-1;
			v.setLayoutParams(lp);
		}
	};
	private OnClickListener startBombeListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			spil.startGameAsServer();
			kastBombe.setOnClickListener(sendListener);
			kastBombe.setText("Throw Bombe");
		}
	};

	private void writeDevice(String input) {
		netService.writemessage(input);
	}

	public ImageView getIm() {
		return im;
	}

	public void setIm(ImageView im) {
		this.im = im;
	}

	// The Handler that gets information back from the BluetoothChatService
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case MESSAGE_READ:
				layout.setBackgroundColor(Color.WHITE);
				byte[] readBuf = (byte[]) msg.obj;
				// construct a string from the valid bytes in the buffer
				// vinder string
				String readMessage = new String(readBuf, 0, msg.arg1);
				//
				im.setImageResource(R.drawable.bomb);
				if (readMessage.equals("Lost")) {
					// status.setText("VUNDET");
					layout.setBackgroundResource(R.drawable.vundet);
					// getIm().setVisibility(View.VISIBLE);
					// im.setImageResource(R.drawable.vundet);
					server.setText("Restart game");
					server.setEnabled(true);
					server.setVisibility(View.VISIBLE);
					server.setOnClickListener(restartListener);
					kastBombe.setLayoutParams(kastBombeLP);
					break;
				}

				spil.receiveBomb(readMessage); // Fortæl spilobjektet, at vi
												// lige har modtaget bomben, og
												// sig hvor lang tid der går,
												// før den springer.
				break;

			case MESSAGE_START:
				spil.setupGameAsServer();
				Toast.makeText(getApplicationContext(), "Player connected", Toast.LENGTH_LONG).show();
				status.setText("GO!");
				server.setVisibility(View.INVISIBLE);
				server.setEnabled(false);
				client.setVisibility(View.INVISIBLE);
				client.setEnabled(false);
				break;

			case MESSAGE_CONNECTED:
				Toast.makeText(getApplicationContext(), "Connected to server", Toast.LENGTH_LONG).show();
				status.setText("GO!");
				server.setVisibility(View.INVISIBLE);
				server.setEnabled(false);
				client.setVisibility(View.INVISIBLE);
				client.setEnabled(false);
				break;

			case MESSAGE_UPDATEVIEW:
				updateView();
				break;
			case MESSAGE_LOST:
				status.setText("");
				kastBombe.setVisibility(View.INVISIBLE);
				kastBombe.setEnabled(false);
				getIm().setVisibility(View.INVISIBLE);
				layout.setBackgroundResource(R.drawable.boom);
				vibrator.vibrate(4000);
				writeDevice("Lost");

				// skal informere andre spillere om at en spiller har tabt
				break;
			case MESSAGE_SENDBOMB:
				writeDevice(Long.toString(spil.getTimeLeft()));
				break;

			}
		}
	};

}
