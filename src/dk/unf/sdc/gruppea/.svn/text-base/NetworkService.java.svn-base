package dk.unf.sdc.gruppea;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

public class NetworkService {
	private static final String tag1 = "BluetoothFrame.NetworkService";
	static final UUID MY_UUID = UUID
			.fromString("99E67F40-9849-11E1-A8B0-0800200C9A66");
	public final static String UPDATE_TEXT = "dk.sdc.BT.update";
	private BluetoothAdapter mBluetoothAdapter;
	private ConnectedThread ct;
	private String message;

	private final Handler mHandler;

	public NetworkService(Context context, BluetoothAdapter btAdapter,
			Handler handler) {
		mBluetoothAdapter = btAdapter;

		mHandler = handler;
	}

	public synchronized void start() {
		AcceptThread aThread = new AcceptThread();
		Log.d(tag1, "Trying to create accept thread.");
		aThread.start();
	}

	public synchronized void connect(BluetoothDevice bd) {
		ConnectThread cThread = new ConnectThread(bd);
		Log.d(tag1, "Trying to create connectthread.");
		cThread.start();
	};

	private class AcceptThread extends Thread {
		private final BluetoothServerSocket mmServerSocket;

		public AcceptThread() {
			// Use a temporary object that is later assigned to mmServerSocket,
			// because mmServerSocket is final
			Log.d(tag1, "Acceptthread created.");

			BluetoothServerSocket tmp = null;
			try {
				// MY_UUID is the app's UUID string, also used by the client
				// code
				tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(
						"BTserviceTest", MY_UUID);
			} catch (IOException e) {
				Log.d(tag1, "Exception Initializing communication");
			}
			mmServerSocket = tmp;
			if(mmServerSocket == null){
				Log.d(tag1, "Serversocket is null.");
			}
		}

		public void run() {
			mBluetoothAdapter.cancelDiscovery();
			Log.d(tag1, "AcceptThread started.");
			BluetoothSocket socket = null;
			// Keep listening until exception occurs or a socket is returned
			while (true) {
				try {
					Log.d(tag1, "Trying to accept connection.");
					socket = mmServerSocket.accept();
				} catch (IOException e) {
					Log.d(tag1, "Caught exception - breaking.");
					break;
				}
				// If a connection was accepted
				if (socket != null) {
					Log.d(tag1, "Calling manageconnection.");
					// Do work to manage the connection (in a separate thread)
					manageConnectedSocket(socket);
				}
			}
		}

		private void manageConnectedSocket(BluetoothSocket socket) {
			Log.d(tag1, "Connection as server OK - transmitting data");
			ct = new ConnectedThread(socket);
			ct.start();
		}

		/** Will cancel the listening socket, and cause the thread to finish */
		public void cancel() {
			try {
				mmServerSocket.close();
			} catch (IOException e) {
			}
		}
	}

	private class ConnectThread extends Thread {
		private final BluetoothSocket mmSocket;

		public ConnectThread(BluetoothDevice device) {
			Log.d(tag1, "ConnectThread started.");
			// Use a temporary object that is later assigned to mmSocket,
			// because mmSocket is final
			BluetoothSocket tmp = null;
			// Get a BluetoothSocket to connect with the given BluetoothDevice
			try {
				// MY_UUID is the app's UUID string, also used by the server
				// code
				Log.d(tag1, "Trying to create socket.");
				tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
			} catch (IOException e) {
				Log.d(tag1, "Could not create socket.");
			}
			mmSocket = tmp;
			Log.d(tag1, "CONNECTtHREAD created mmsocket.");
		}

		public void run() {
			// Cancel discovery because it will slow down the connection
			mBluetoothAdapter.cancelDiscovery();
			try {
				// Connect the device through the socket. This will block
				// until it succeeds or throws an exception
				Log.d(tag1, "Connecting to BT peer.");
				mmSocket.connect();
				Log.d(tag1, "Connection OK");
			} catch (IOException connectException) {
				Log.d(tag1, "Failed to connect" + connectException.getMessage());
				// Unable to connect; close the socket and get out
				try {
					mmSocket.close();
				} catch (IOException closeException) {
				}
				return;
			}
			// Do work to manage the connection (in a separate thread)
			manageConnectedSocket(mmSocket);
		}

		private void manageConnectedSocket(BluetoothSocket socket) {
			Log.d(tag1, "Connected.");
			ct = new ConnectedThread(socket);
			ct.start();
			String connect = "GO";
			ct.write(connect.getBytes());
			Log.d(tag1, "Client wrote " + connect);
			mHandler.obtainMessage(SpilActivity.MESSAGE_CONNECTED).sendToTarget();
		}

		/** Will cancel an in-progress connection, and close the socket */
		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
			}
		}
	}

	private class ConnectedThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;

		public ConnectedThread(BluetoothSocket socket) {
			mmSocket = socket;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;
			// Get the input and output streams, using temp objects because
			// member streams are final
			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e) {
			}
			mmInStream = tmpIn;
			mmOutStream = tmpOut;
		}

		public void run() {
			byte[] buffer = new byte[1024]; // buffer store for the stream
			int bytes; // bytes returned from read()
			// Keep listening to the InputStream until an exception occurs
			while (true) {
				// Read from the InputStream
				try {
					bytes = mmInStream.read(buffer);
					message = (new String(buffer, 0, bytes));
					if(message.equals("GO")){
						mHandler.obtainMessage(SpilActivity.MESSAGE_START).sendToTarget();
					}else{
					mHandler.obtainMessage(SpilActivity.MESSAGE_READ,bytes, -1, buffer).sendToTarget();
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}

		/* Call this from the main activity to send data to the remote device */
		public void write(byte[] bytes) {
			try {
				mmOutStream.write(bytes);
			} catch (IOException e) {
			}
		}

		/* Call this from the main activity to shutdown the connection */
		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
			}
		}
	}

	public void writemessage(String input) {
		ct.write(input.getBytes());
	}

}
