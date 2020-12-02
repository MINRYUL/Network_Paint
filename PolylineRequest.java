package lab9;

import java.io.IOException;
import java.net.Socket;

public class PolylineRequest {
	public void go() {
		Socket	sock;
		try {
			// Create a socket object with the IP address "192.168.0.1". You choose a proper port number
			// which should match the number of the ServerSocket object in SimpleTalkReceiver
			sock = new Socket("127.0.0.1", 5000);
			new PolylineEditor(sock, "Polyline A");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
