package lab9;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class PolylineReceiver{
	public void go() {
		Socket sock;
		try {
			// Create a server socket and wait until a network connection is requested
			// The socket object is passed to the SimpleTalkGUI instance as in the code below.
			ServerSocket serverSock = new ServerSocket(5000);
			sock = serverSock.accept();
			new PolylineEditor(sock, "Polyline B");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
