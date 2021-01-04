package karrus.server.service;

import java.util.List;

import karrus.client.service.ConfigurationsDatabaseService;
import karrus.server.database.ConfigurationDatabaseDriver;
import karrus.server.database.GenericDatabaseDriver;
import karrus.shared.hibernate.CtLane;
import karrus.shared.hibernate.RsuStation;
import karrus.shared.hibernate.TtItinerary;
import net.sf.gilead.core.PersistentBeanManager;
import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.core.serialization.GwtProxySerialization;
import net.sf.gilead.core.store.stateless.StatelessProxyStore;
import net.sf.gilead.gwt.PersistentRemoteService;


public class ConfigurationsDatabaseServiceImpl extends PersistentRemoteService implements ConfigurationsDatabaseService {

	private static final long serialVersionUID = 1L;

	//	private static Logger logger = Logger.getLogger(ConfigurationsDatabaseServiceImpl.class);

	/**
	 * Constructor.
	 * @throws Exception 
	 */
	public ConfigurationsDatabaseServiceImpl() throws Exception {
		HibernateUtil gileadHibernateUtil = new HibernateUtil();
		gileadHibernateUtil.setSessionFactory(karrus.server.database.HibernateUtil.getSessionFactory());
		PersistentBeanManager persistentBeanManager = new PersistentBeanManager();
		persistentBeanManager.setPersistenceUtil(gileadHibernateUtil);
		StatelessProxyStore proxyStore = new StatelessProxyStore(); 
		proxyStore.setProxySerializer(new GwtProxySerialization());
		persistentBeanManager.setProxyStore(proxyStore);
		setBeanManager(persistentBeanManager);
	}

	////////////////////////////////////
	//               stations         //
	////////////////////////////////////	

	@Override
	public List<RsuStation> getStations() throws Exception{
		return GenericDatabaseDriver.getStations();
	}

	@Override
	public void removeStation(String stationId, String stationSerial) throws Exception {
		GenericDatabaseDriver.removeStation(stationId, stationSerial);

	}

	@Override
	public RsuStation updateStation(RsuStation oldStation, RsuStation station) throws Exception {
		return GenericDatabaseDriver.updateStation(oldStation, station);
	}

	@Override
	public RsuStation addNewStation(RsuStation station) throws Exception {
		return GenericDatabaseDriver.addNewStation(station);
	}
	
	////////////////////////////////////
	//              lanes             //
	////////////////////////////////////
	
	@Override
	public void removeLane(CtLane lane) throws Exception {
		ConfigurationDatabaseDriver.removeLane(lane);

	}

	@Override
	public CtLane updateLane(CtLane oldLane, CtLane lane) throws Exception {
		return ConfigurationDatabaseDriver.updateLane(oldLane, lane);
	}

	@Override
	public CtLane addNewLane(CtLane lane) throws Exception {
		return ConfigurationDatabaseDriver.addNewLane(lane);
	}
	
	////////////////////////////////////
	//          bt itineraries        //
	////////////////////////////////////	
	
	@Override
	public List<TtItinerary> getAllItineraries() throws Exception{
		return GenericDatabaseDriver.getAllItineraries();
	}
	
	@Override
	public void removeItinerary(String name, String origin, String destination) throws Exception {
		ConfigurationDatabaseDriver.removeItinerary(name, origin, destination);

	}


	@Override
	public TtItinerary updateItinerary(String formerName, String formerOrigin, String formerDestination, String newName, String newOrigin, String newDestination, Integer nominalTravelTime, Integer maxTravelTime, Integer levelOfServiceThreshold1, Integer levelOfServiceThreshold2, Integer scale) throws Exception {
		return ConfigurationDatabaseDriver.updateItinerary(formerName, formerOrigin, formerDestination, newName, newOrigin, newDestination, nominalTravelTime, maxTravelTime, levelOfServiceThreshold1, levelOfServiceThreshold2, scale);
	}


	@Override
	public TtItinerary addNewItinerary(String name, String origin, String destination, Integer nominalTravelTime, Integer maxTravelTime, Integer levelOfServiceThreshold1, Integer levelOfServiceThreshold2, Integer scale) throws Exception {
		return ConfigurationDatabaseDriver.addNewItinerary(name, origin, destination, nominalTravelTime, maxTravelTime, levelOfServiceThreshold1, levelOfServiceThreshold2, scale);
	}
	
	@Override
	public TtItinerary getItinerary(String itineraryName) throws Exception {
		return GenericDatabaseDriver.getItineraryFromName(itineraryName);
	}
}
