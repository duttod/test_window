package karrus.server.service;


import java.util.Date;
import java.util.List;

import karrus.client.service.AlarmsDatabaseService;
import karrus.server.database.AlarmDatabaseDriver;
import karrus.shared.hibernate.AlAlarmClosed;
import karrus.shared.hibernate.AlAlarmOpened;
import net.sf.gilead.core.PersistentBeanManager;
import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.core.serialization.GwtProxySerialization;
import net.sf.gilead.core.store.stateless.StatelessProxyStore;
import net.sf.gilead.gwt.PersistentRemoteService;

public class AlarmsDatabaseServiceImpl extends PersistentRemoteService implements AlarmsDatabaseService {

	private static final long serialVersionUID = 1L;
//	private static Logger logger = Logger.getLogger(AlarmsDatabaseServiceImpl.class);

	/**
	 * Constructor.
	 * @throws Exception 
	 */
	public AlarmsDatabaseServiceImpl() throws Exception {
		HibernateUtil gileadHibernateUtil = new HibernateUtil();
		gileadHibernateUtil.setSessionFactory(karrus.server.database.HibernateUtil.getSessionFactory());
		PersistentBeanManager persistentBeanManager = new PersistentBeanManager();
		persistentBeanManager.setPersistenceUtil(gileadHibernateUtil);
		StatelessProxyStore proxyStore = new StatelessProxyStore(); 
		proxyStore.setProxySerializer(new GwtProxySerialization());
		persistentBeanManager.setProxyStore(proxyStore);
		setBeanManager(persistentBeanManager);		
	}
	
	public List<AlAlarmClosed> getAlarmsHistoryFiltered(Date initDate, Date finalDate, String Source, String Type, String State) throws Exception {
		return AlarmDatabaseDriver.getAlarmsHistoryFiltered(initDate, finalDate, Source, Type, State);
	}
	
	public List<AlAlarmOpened> getCurrentAlarmsFiltered(Date initDate, Date finalDate, String Source, String Type, String State) throws Exception {
		return AlarmDatabaseDriver.getCurrentAlarmsFiltered(initDate, finalDate, Source, Type, State);
	}

	public void closeAlarms(List<AlAlarmOpened> selectedAlarms) throws Exception {
		AlarmDatabaseDriver.closeAlarms(selectedAlarms);
	}
	
	public void acknowledgeAlarms(List<AlAlarmOpened> selectedAlarms) throws Exception {
		AlarmDatabaseDriver.acknowledgeAlarms(selectedAlarms);
	}
	
	public void openAlarms(List<AlAlarmClosed> selectedAlarms) throws Exception {
		AlarmDatabaseDriver.openAlarms(selectedAlarms);
	}
	
	public List<AlAlarmClosed> getAlarmSources() throws Exception {
		return AlarmDatabaseDriver.getAlarmSources();
	}
	
	public List<AlAlarmClosed> getClosedAlarms() throws Exception {
		return AlarmDatabaseDriver.getClosedAlarms();
	}
}
