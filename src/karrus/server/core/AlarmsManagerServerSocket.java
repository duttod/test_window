package karrus.server.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;
import java.util.Timer;

import karrus.server.service.ClockServiceImpl;
import karrus.shared.hibernate.AlAlarmClosed;
import karrus.shared.hibernate.AlAlarmOpened;

import org.apache.log4j.Logger;

/**
 * Server socket that treats messages from the alarms manager module.
 */
public class AlarmsManagerServerSocket extends Thread {

	private static Logger logger = Logger.getLogger(AlarmsManagerServerSocket.class);
	private int port;
	private Date alarmsManagerTimestamp;
	private PrintWriter printWriter;
	private ServerSocket serverSocket;
	public Timer waitForResponseTimer;
	private String response = "";
	private String comment = "";
	private ClockServiceImpl clockServiceImpl;

	public AlarmsManagerServerSocket(int port, ClockServiceImpl clockServiceImpl) {
		// save port
		this.port = port;
		this.clockServiceImpl = clockServiceImpl;
		// clear messages
		clearResponse();
		// start server socket
		try {
			serverSocket = new ServerSocket(port);
			logger.info(prefixLog("alarms manager server socket started."));
		} catch (IOException e) {
			logger.info(prefixLog("Could not start alarms manager server socket"));
		}
	}

	public String prefixLog(String string) {
		return "socket/"+port + " " + string;
	}

	private void clearResponse(){
		response = "";
		comment = "";
	}

	/**
	 * wait for a client and process it 
	 */
	public void run() {
		try {
			while (true) {
				BufferedReader bufferedReader = null;
				try {
					// initialize transaction
					Socket clientSocket = serverSocket.accept();
					logger.info(prefixLog("New client from " + clientSocket.getInetAddress()) + ".");
					ObjectInputStream inFromClient = new ObjectInputStream(clientSocket.getInputStream());
					bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())), true);
					// read lines
					while (true) {
						Object object = inFromClient.readObject();
						alarmsManagerTimestamp = new Date();
						logger.info(prefixLog("Received object at timestamp " + alarmsManagerTimestamp + "."));
						if (object instanceof AlAlarmOpened) {
							AlAlarmOpened alarm = (AlAlarmOpened)object; 
							clockServiceImpl.sendMessage(alarm.getStatus());
							try {
								// put the alarms manager message in the core
								sendResponseToAlarmsManager("ACK", "");
							} catch (Exception e) {
								response = "NACK";
								comment = "error";
								sendResponseToAlarmsManager(response, comment);
								logger.error(prefixLog("NACK/error sent. Exception message: " + e.getMessage()));								
							}
							break;
						} 
						else if (object instanceof AlAlarmClosed) {
							AlAlarmClosed alarm = (AlAlarmClosed)object; 
							clockServiceImpl.sendMessage(alarm.getId().getType());
							try {
								// put the alarms manager message in the core
								sendResponseToAlarmsManager("ACK", "");
							} catch (Exception e) {
								response = "NACK";
								comment = "error";
								sendResponseToAlarmsManager(response, comment);
								logger.error(prefixLog("NACK/error sent. Exception message: " + e.getMessage()));								
							}
							break;
						}
					}
					bufferedReader.close();
					printWriter.close();
					clientSocket.close();
				} catch (SocketException e) {
					logger.info(prefixLog("socket exception : " + e.getMessage()));
					e.printStackTrace();
					if (bufferedReader!=null) {
						bufferedReader.close();
					}	
					printWriter.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.info(prefixLog(e.getMessage()));
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(prefixLog(e.getMessage()));
		}
	}

	private void sendResponseToAlarmsManager(String response, String comment){
		String res = response + "," + comment;
		printWriter.println(res); 
		logger.info(prefixLog("SEND : "+res));
		clearResponse();
	}

	public void stopServerSocket() throws IOException {
		printWriter.close();
		serverSocket.close();
		logger.info(prefixLog("alarmsManagerServerSocket closed on port "+port));
	}
}
