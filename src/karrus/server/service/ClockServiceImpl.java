package karrus.server.service;

import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.client.event.domain.DomainFactory;
import de.novanic.eventservice.service.RemoteEventServiceServlet;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import karrus.client.service.ClockService;
import karrus.client.event.ServerClockEvent;

import org.apache.log4j.Logger;

public class ClockServiceImpl extends RemoteEventServiceServlet implements ClockService {
    
	private static final long serialVersionUID = 1L;
	SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss");
	private Timer timer;
	private Logger logger = Logger.getLogger(ClockServiceImpl.class);
   
    public synchronized void start() {
    	timer = new Timer();
    	timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Date now = new Date();
				String nowAsString = dateFormatter.format(now);
				sendMessage(nowAsString);
			}
		}, 0, 1000);
    	logger.debug("Event sender thread started");
    }

	@Override
    public void destroy() {
    	logger.debug("Service destroyed");
    	timer.cancel();
        super.destroy();
    }
    
    public void sendMessage(String message) {
    	 Event theEvent = new ServerClockEvent(message);
         addEvent(ServerClockEvent.SERVER_MESSAGE_DOMAIN, theEvent);
    }
    
    public void sendMessage2(String message) {
   	 Event theEvent = new ServerClockEvent(message);
        addEvent(DomainFactory.getDomain("server_message_domain2"), theEvent);
   }
    
}
