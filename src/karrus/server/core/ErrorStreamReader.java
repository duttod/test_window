package karrus.server.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;




import org.apache.log4j.Logger;

import com.allen_sauer.gwt.log.client.Log;

public class ErrorStreamReader extends Thread {

	private static Logger logger = Logger.getLogger(ErrorStreamReader.class);
	private Process processus;
	
	public ErrorStreamReader(Process p) {
		this.processus = p;
	}
	
	public void run(){
		String line;
		BufferedReader inError = new BufferedReader(new InputStreamReader(processus.getErrorStream()));
		try {
			while ((line = inError.readLine()) != null) {
				logger.error(line);
			}
			inError.close();
		} catch (IOException e) {
			Log.error(e.getMessage());
		}
	}
}
