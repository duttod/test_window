package karrus.server.service;

import java.util.List;
import java.util.Map;

import karrus.client.service.SynopticDatabaseService;
import karrus.server.database.AlarmDatabaseDriver;
import karrus.server.database.SynopticDatabaseDriver;
import karrus.shared.hibernate.AlAlarmOpened;
import karrus.shared.hibernate.CtCountData;
import karrus.shared.hibernate.WthMeteoData;
import net.sf.gilead.core.PersistentBeanManager;
import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.core.serialization.GwtProxySerialization;
import net.sf.gilead.core.store.stateless.StatelessProxyStore;
import net.sf.gilead.gwt.PersistentRemoteService;

public class SynopticDatabaseServiceImpl extends PersistentRemoteService implements SynopticDatabaseService {

	private static final long serialVersionUID = 1L;
	//private static Logger logger = Logger.getLogger(SynopticDatabaseServiceImpl.class);

	/**
	 * Constructor.
	 * @throws Exception 
	 */
	public SynopticDatabaseServiceImpl() throws Exception {
		HibernateUtil gileadHibernateUtil = new HibernateUtil();
		gileadHibernateUtil.setSessionFactory(karrus.server.database.HibernateUtil.getSessionFactory());
		PersistentBeanManager persistentBeanManager = new PersistentBeanManager();
		persistentBeanManager.setPersistenceUtil(gileadHibernateUtil);
		StatelessProxyStore proxyStore = new StatelessProxyStore(); 
		proxyStore.setProxySerializer(new GwtProxySerialization());
		persistentBeanManager.setProxyStore(proxyStore);
		setBeanManager(persistentBeanManager);		
	}
	
	public Map<String, Double> getLastDataForItineraries(List<String> itinerariesNames) throws Exception {
		return SynopticDatabaseDriver.getLastDataForItineraries(itinerariesNames);
	}
	
	public Map<String, CtCountData> getLastDataForLanes(List<String> stationsNames, List<String> lanes) throws Exception {
		return SynopticDatabaseDriver.getLastDataForLanes(stationsNames, lanes);
	}
	
	@Override
	public Map<String, WthMeteoData> getLastWeatherDataForStations(List<String> stationsNames) throws Exception {
		return SynopticDatabaseDriver.getLastWeatherDataForStations(stationsNames);
	}
	
	public List<AlAlarmOpened> getOpenedAlarms() throws Exception  {
		return AlarmDatabaseDriver.getOpenedAlarms();
	}
}
