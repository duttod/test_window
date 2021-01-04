package karrus.server.core;

import javax.servlet.http.HttpServlet;
import karrus.server.database.GenericDatabaseDriver;
import karrus.server.jdbc.MySqlDriver;
import karrus.server.os.Environment;
import karrus.server.service.ClockServiceImpl;
import karrus.shared.language.Language;
import org.apache.log4j.Logger;

public final class Core extends HttpServlet{

	private static final long serialVersionUID = 1L;
	public static String projectName = "";
	private static Logger logger = Logger.getLogger(Core.class);
	private static boolean isFirstTime = true;
	private static ClockServiceImpl clockServiceImpl;

	/**
	 * Constructor.
	 * @throws Exception 
	 */
	public Core() {
		clockServiceImpl = new ClockServiceImpl();
		clockServiceImpl.start();
		logger.info("demarrage core");
		try {
			String[] dbCredentials = GenericDatabaseDriver.getEnvironmentVariable(Language.databaseCredentialsEnvironmentVariable).getContent().split(",");
			if (dbCredentials.length != 3) {
				throw new Exception("The database credentials variable in sys env has not the good format. Alarms engine not started");
			}
			MySqlDriver.setDatabaseCredentials(dbCredentials[0], dbCredentials[1], dbCredentials[2]);
//			Timer timer = new Timer();
//	    	timer.schedule(new TimerTask() {
//				@Override
//				public void run() {
//					try {
//						MySqlDriver.checkAlarms();
//					} catch (Exception e) {
//						logger.error("Alarms check failed\n" + e.getMessage());
//					}
//				}
//			}, 0, 5*1000);
//	    	logger.debug("Alarms engine started");
		} catch (Exception e) {
			logger.error("Alarms engine initializaiton failed\n" + e.getMessage());
		}
	}

	public static void setProjetNameAndInit(String name) {
		if (isFirstTime) {
			projectName = name;
			logger.info("Project name : " + projectName);
			try {
				Environment.log4jInit();
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
			isFirstTime = false;
		}
	}

	public static void main(String[] args) {
		try {
			new Core();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
}
