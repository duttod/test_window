package karrus.server.service;

import java.io.IOException;

import karrus.client.service.IhmPropertiesService;
import karrus.server.core.PropertyFileParser;
import karrus.server.os.Environment;
import karrus.shared.IhmParameters;
import net.sf.gilead.core.PersistentBeanManager;
import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.core.serialization.GwtProxySerialization;
import net.sf.gilead.core.store.stateless.StatelessProxyStore;
import net.sf.gilead.gwt.PersistentRemoteService;

import org.apache.log4j.Logger;

public class IhmPropertiesServiceImpl extends PersistentRemoteService implements IhmPropertiesService {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(IhmPropertiesServiceImpl.class);
	private static String devModeString = "dev";
	private static String minForTravelTimePlot = "travel.time.min";
	private static String maxForTravelTimePlot = "travel.time.max";
	private static String minOccupancyString = "plot.occupancy.min";
	private static String maxOccupancyString = "plot.occupancy.max";
	private static String minSpeedString = "plot.speed.min";
	private static String maxSpeedString = "plot.speed.max";
	private static String minCountString = "plot.count.min";
	private static String maxCountString = "plot.count.max";
	private static String dataPlotRefreshingRateString = "refreshing.rate.data.plot";
	private static String fullDayStartString = "fullDay.start";
	private static String fullDayStopString = "fullDay.stop";
	private static String morningStartString = "morning.start";
	private static String morningStopString = "morning.stop";
	private static String afternoonStartString = "afternoon.start";
	private static String afternoonStopString = "afternoon.stop";
	private static String plotMaxTimeWindowString = "plot.max.time.window";
	private static String unknownMarkingsString = "markings.unknown";
	private static String missingDataMarkingsString = "markings.missingData";
	private static String nightMarkingsString = "markings.night";
	private static String roadString = "road";
	private static String alarmsManagerCommunicationString = "alarms.manager.communication.port";
	
	/**
	 * Constructor.
	 * @throws Exception 
	 */
	public IhmPropertiesServiceImpl() throws Exception {
		HibernateUtil gileadHibernateUtil = new HibernateUtil();
		gileadHibernateUtil.setSessionFactory(karrus.server.database.HibernateUtil.getSessionFactory());
		PersistentBeanManager persistentBeanManager = new PersistentBeanManager();
		persistentBeanManager.setPersistenceUtil(gileadHibernateUtil);
		StatelessProxyStore proxyStore = new StatelessProxyStore(); 
		proxyStore.setProxySerializer(new GwtProxySerialization());
		persistentBeanManager.setProxyStore(proxyStore);
		setBeanManager(persistentBeanManager);
	}

	public IhmParameters getIhmParameters() throws Exception {
		boolean devMode = false;
		try {
			devMode = getIhmPropertyAsBoolean(devModeString);
		} catch (Exception e){
			logger.error(e.getMessage());
		}
		int maxForTravelTime = getIhmPropertyAsInteger(maxForTravelTimePlot);
		int minForTravelTime = getIhmPropertyAsInteger(minForTravelTimePlot);
		int minOccupancy = getIhmPropertyAsInteger(minOccupancyString);
		int maxOccupancy = getIhmPropertyAsInteger(maxOccupancyString);
		int minCount = getIhmPropertyAsInteger(minCountString);
		int maxCount = getIhmPropertyAsInteger(maxCountString);
		int minSpeed = getIhmPropertyAsInteger(minSpeedString);
		int maxSpeed = getIhmPropertyAsInteger(maxSpeedString);
		int dataPlotRefreshingRate = getIhmPropertyAsInteger(dataPlotRefreshingRateString);
		String fullDayStart = getIhmProperty(fullDayStartString);
		String fullDayStop = getIhmProperty(fullDayStopString);
		String morningStart = getIhmProperty(morningStartString);
		String morningStop = getIhmProperty(morningStopString);
		String afternoonStart = getIhmProperty(afternoonStartString);
		String afternoonStop = getIhmProperty(afternoonStopString);
		Integer plotMaxTimeWindow = getIhmPropertyAsInteger(plotMaxTimeWindowString);
		boolean unknownMarkings = getIhmPropertyAsBoolean(unknownMarkingsString);
		boolean missingDataMarkings = getIhmPropertyAsBoolean(missingDataMarkingsString);
		boolean nightMarkings = getIhmPropertyAsBoolean(nightMarkingsString);
		String road = "";
		try {
			road = getIhmProperty(roadString);
		} catch (Exception e){
			logger.error(e.getMessage());
		}
		return new IhmParameters(devMode, minForTravelTime, maxForTravelTime, minCount, maxCount, minOccupancy, maxOccupancy, minSpeed, maxSpeed, dataPlotRefreshingRate,
				fullDayStart, fullDayStop, morningStart, morningStop, afternoonStart, afternoonStop, plotMaxTimeWindow, unknownMarkings, missingDataMarkings, nightMarkings, road);
	}

	public static String getIhmProperty(String property) throws Exception{
		String file = Environment.getIhmPropertiesFile();
		PropertyFileParser ihmProperties = null;
		try {
			ihmProperties = new PropertyFileParser(file);
			return ihmProperties.getProperty(property);
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new Exception(e);
		}          
	}

	public static Integer getIhmPropertyAsInteger(String property) throws Exception{
		String file = Environment.getIhmPropertiesFile();
		PropertyFileParser ihmProperties = null;
		try {
			ihmProperties = new PropertyFileParser(file);
			return ihmProperties.getPropertyAsInteger(property);
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new Exception(e);
		}          
	}
	
	public static Double getIhmPropertyAsDouble(String property) throws Exception{
		String file = Environment.getIhmPropertiesFile();
		PropertyFileParser ihmProperties = null;
		try {
			ihmProperties = new PropertyFileParser(file);
			return ihmProperties.getPropertyAsDouble(property);
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new Exception(e);
		}          
	}

	private static boolean getIhmPropertyAsBoolean(String property) throws Exception{
		String file = Environment.getIhmPropertiesFile();
		PropertyFileParser ihmProperties = null;
		try {
			ihmProperties = new PropertyFileParser(file);
			return ihmProperties.getPropertyAsBoolean(property);
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new Exception(e);
		}          
	}
	
	public static int getAlarmsManagerCommunicationPort() throws Exception {
		int port = getIhmPropertyAsInteger(alarmsManagerCommunicationString);
		return port;
	}
}
