package lab9;

class ReceiverRunner implements Runnable {
	public void run() {
		new PolylineReceiver().go();
	}
}

class RequestRunner implements Runnable {
	public void run() {
		new PolylineRequest().go();
	}
}

public class PolylineTest {
	public static void main(String[] args) {
		new Thread(new ReceiverRunner()).start();
		try {
			Thread.sleep(1000);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		new Thread(new RequestRunner()).start();
	}
}
