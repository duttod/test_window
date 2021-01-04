package karrus.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

import karrus.shared.hibernate.AlAlarmOpened;
import karrus.shared.hibernate.AlAlarmOpenedId;

public class AlarmsManagerClient {

	static final int port = 6190;
	static final String cmd = "";
	
	public static void main(String[] args) {
		try {
			Socket socket = new Socket("localhost", port);
			System.out.println("Connected to " + socket);
			AlAlarmOpened alarm = new AlAlarmOpened(new AlAlarmOpenedId(new Date(), "type", "source"), "status");
//			AlAlarmClosed alarm = new AlAlarmClosed(new AlAlarmClosedId(new Date(), "type", "source"), "status");
			ObjectOutputStream outToServer = new ObjectOutputStream(socket.getOutputStream());
			BufferedReader bufferReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outToServer.writeObject(alarm);
			System.out.println("Object sent");
			String answer = bufferReader.readLine();
			System.out.println("Received line: " + answer);
			System.out.println("Finished");
			bufferReader.close();
			socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
