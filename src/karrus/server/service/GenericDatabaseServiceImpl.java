package karrus.server.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import karrus.client.service.GenericDatabaseService;
import karrus.server.core.Core;
import karrus.server.core.ErrorStreamReader;
import karrus.server.database.GenericDatabaseDriver;
import karrus.server.database.SynopticDatabaseDriver;
import karrus.server.os.Environment;
import karrus.server.os.FileTools;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import karrus.shared.CountDataAndLanesMap;
import karrus.shared.DatabaseObjects;
import karrus.shared.hibernate.CtCountData;
import karrus.shared.hibernate.RsuDiag;
import karrus.shared.hibernate.SynAlarm;
import karrus.shared.hibernate.SynRdtDisplay;
import karrus.shared.hibernate.SynTtColor;
import karrus.shared.hibernate.SynRdtColor;
import karrus.shared.hibernate.CtLane;
import karrus.shared.hibernate.SynTtDisplay;
import karrus.shared.hibernate.SynWeatherDisplay;
import karrus.shared.hibernate.SynWeatherStation;
import karrus.shared.hibernate.SysEnv;
import karrus.shared.hibernate.TtBtItt;
import karrus.shared.hibernate.TtBtStat;
import karrus.shared.hibernate.TtItinerary;
import karrus.shared.hibernate.UsrCredential;
import karrus.shared.hibernate.UsrUser;
import karrus.shared.hibernate.RsuStation;
import karrus.shared.hibernate.V2xAlarm;
import karrus.shared.hibernate.WthMeteoData;
import karrus.shared.plot.SampleCount;
import karrus.shared.plot.TravelTimePlot;
import net.sf.gilead.core.PersistentBeanManager;
import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.core.serialization.GwtProxySerialization;
import net.sf.gilead.core.store.stateless.StatelessProxyStore;
import net.sf.gilead.gwt.PersistentRemoteService;

import org.apache.log4j.Logger;


public class GenericDatabaseServiceImpl extends PersistentRemoteService implements GenericDatabaseService {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(GenericDatabaseServiceImpl.class);

	/**
	 * Constructor.
	 * @throws Exception 
	 */
	public GenericDatabaseServiceImpl() throws Exception {
		HibernateUtil gileadHibernateUtil = new HibernateUtil();
		gileadHibernateUtil.setSessionFactory(karrus.server.database.HibernateUtil.getSessionFactory());
		PersistentBeanManager persistentBeanManager = new PersistentBeanManager();
		persistentBeanManager.setPersistenceUtil(gileadHibernateUtil);
		StatelessProxyStore proxyStore = new StatelessProxyStore(); 
		proxyStore.setProxySerializer(new GwtProxySerialization());
		persistentBeanManager.setProxyStore(proxyStore);
		setBeanManager(persistentBeanManager);		
	}

	public void setProjectName(String projectName) throws Exception {
		Core.setProjetNameAndInit(projectName);
	}

	public DatabaseObjects getDatabaseObjects() throws Exception {
		try {
			List<UsrUser> users = GenericDatabaseDriver.getUsers();
			List<UsrCredential> credentials = GenericDatabaseDriver.getCredentials();
			List<TtItinerary> itineraries = GenericDatabaseDriver.getAllItineraries();
			List<RsuStation> stations = GenericDatabaseDriver.getAllStations();
			List<CtLane> lanes = GenericDatabaseDriver.getAllLanes();
			List<SynAlarm> synAlarms = SynopticDatabaseDriver.getSynAlarms();
			List<SynRdtColor> synRdtColors = SynopticDatabaseDriver.getSynRdtColors();
			List<SynRdtDisplay> synRdtDisplays = SynopticDatabaseDriver.getSynRdtDisplays();
			List<SynTtColor> synTtColors = SynopticDatabaseDriver.getSynTtColors();
			List<SynTtDisplay> synTtDisplays = SynopticDatabaseDriver.getSynTtDisplays();
			List<SynWeatherStation> synWeatherStations = SynopticDatabaseDriver.getSynWeatherStations();
			List<SynWeatherDisplay> synWeatherDisplays = SynopticDatabaseDriver.getSynWeatherDisplays();
			DatabaseObjects dataBaseObjects = new DatabaseObjects(users, credentials, stations, lanes, itineraries, synAlarms, synRdtColors, synRdtDisplays, synTtColors, 
					                                              synTtDisplays, synWeatherStations, synWeatherDisplays);
			return dataBaseObjects;
		} catch (Exception exception){
			logger.error(exception.getMessage());
			throw exception;
		}
	}
	
	public List<RsuStation> getAllStations() throws Exception {
		return GenericDatabaseDriver.getAllStations();
	}
	
	@Override
	public List<String> getStationsIdsForAlarmsFilter() throws Exception {
		List<String> stationsIds = new ArrayList<String>();
		for (RsuStation rsuStation : GenericDatabaseDriver.getAllStations()) {
			String stationId = rsuStation.getId().toUpperCase().replace(" ", "-");
			stationId	 = Normalizer.normalize(stationId, Normalizer.Form.NFD);
			stationId = stationId.replaceAll("[^\\p{ASCII}]", "");
			stationsIds.add(stationId);
		}
		return stationsIds;
	}
	
	public List<CtLane> getAllLanes() throws Exception {
		return GenericDatabaseDriver.getAllLanes();
	}
	
	@Override
	public CountDataAndLanesMap getCountDataForStationAndWay(String stationName, String way, Date startDate, Date finalDate) throws Exception {
		return GenericDatabaseDriver.getCountDataForStationAndWay(stationName, way, startDate, finalDate);
	}
	
	@Override
	public List<String> getWaysForStation(String stationName) throws Exception {
		return GenericDatabaseDriver.getWaysForStation(stationName);
	}
	
	@Override
	public List<CtCountData> getCountDataForStationAndLane(String stationName, String lane, Date startDate, Date finalDate) throws Exception {
		return GenericDatabaseDriver.getCountDataForStationAndLane(stationName, lane, startDate, finalDate);
	}
	
	@Override
	public List<RsuDiag> getRsuDiagnosticForStation(String stationName, Date startDate, Date finalDate) throws Exception {
		return GenericDatabaseDriver.getRsuDiagnosticForStation(stationName, startDate, finalDate);
	}
	
	@Override
	public List<V2xAlarm> getV2xAlarms(String stationName, Date startDate, Date finalDate) throws Exception {
		return GenericDatabaseDriver.getV2xAlarms(stationName, startDate, finalDate);
	}
	
	public UsrUser checkUserAndPassword(String login, String password) throws Exception{
		return GenericDatabaseDriver.checkUserAndPassword(login, password);
	}
	
	public List<UsrUser> getUsers() throws Exception{
		return GenericDatabaseDriver.getUsers();
	}
	
	public UsrUser updatePassword(UsrUser user, String newPwd) throws Exception{
		return GenericDatabaseDriver.updatePassword(user, newPwd);
	}

	public UsrUser updateUser(UsrUser formerUser, String newLogin, String newEmail, String newCredential) throws Exception {
		return GenericDatabaseDriver.updateUser(formerUser, newLogin, newEmail, newCredential);
	}

	public UsrUser addNewUser(String login, String password, String email, String credential) throws Exception {
		return GenericDatabaseDriver.addNewUser(login, password, email, credential);
	}

	public void removeUser(String login) throws Exception{
		GenericDatabaseDriver.removeUser(login);
	}
	

	public List<UsrCredential> getCredentials() throws Exception {
		return GenericDatabaseDriver.getCredentials();
	}
	
	public void removeCredential(UsrCredential credential) throws Exception{
		GenericDatabaseDriver.removeCredential(credential);
	}
	
	public UsrCredential updateCredential(UsrCredential oldCredential, UsrCredential credential) throws Exception {
		return GenericDatabaseDriver.updateCredential(oldCredential, credential);
	}
	
	public UsrCredential addNewCredential(UsrCredential credential) throws Exception {
		return GenericDatabaseDriver.addNewCredential(credential);
	}
	
	public TravelTimePlot getTravelTimePlot(String itinerary, Date initDate, Date finalDate) throws Exception {
		List<TtBtStat> btStats = GenericDatabaseDriver.getBtStat(itinerary, initDate, finalDate);
		List<SampleCount> sampleCounts = new ArrayList<SampleCount>();
		for (TtBtStat btStat : btStats) {
			sampleCounts.add(new SampleCount(btStat.getId().getTimestamp(), btStat.getSampleSize5Min()));
		}	
		List<TtBtItt> itt = GenericDatabaseDriver.getBtItt(itinerary, initDate, finalDate);
		List<TtBtItt> validItts = new ArrayList<TtBtItt>();
		List<TtBtItt> nonValidItts = new ArrayList<TtBtItt>();
		for (TtBtItt p : itt) {
			if (p.isAttValid())
				validItts.add(p);
			else
				nonValidItts.add(p);
		}
		return new TravelTimePlot(btStats, validItts, nonValidItts, sampleCounts);
	}
	
	public List<TtBtItt> getTtBtItt(String itineraryString, Date initDate, Date finalDate) throws Exception {
		return GenericDatabaseDriver.getBtItt(itineraryString, initDate, finalDate);
	}
	
	public List<TtBtStat> getTtBtStat(String itineraryString, Date initDate, Date finalDate) throws Exception {
		return GenericDatabaseDriver.getBtStat(itineraryString, initDate, finalDate);
	}
	
	
	public List<WthMeteoData> getWeatherDataForStation(String stationName, Date startDate, Date finalDate) throws Exception {
		return GenericDatabaseDriver.getWeatherDataForStation(stationName, startDate, finalDate);
	}
	
	////////////////////////////////////
	//             Environment        //
	////////////////////////////////////
	
	public List<String> getReferentialFiles() throws Exception{
		String path= Environment.getPropertiesDirectory();
		String ref = Environment.getReferentialFile();
		String prefix = ref.substring(ref.lastIndexOf("/")+1);
		prefix = prefix.substring(0, prefix.indexOf("."));
		prefix = prefix+"(\\w|_|-)*.ref";
		Pattern p = Pattern.compile(prefix);
		String [] s = new File(path).list();
		List<String> res = new ArrayList<String>();
		for (int i=0; i<s.length;i++){
			Matcher m = p.matcher(s[i]);
			if (m.matches()) {
				res.add(s[i]);
			}	
		} 
		Collections.sort(res);
		return res;
	}
	
	public String getCurrentReferentialFile() throws Exception{
		String ref = Environment.getReferentialFile();
		return ref.substring(ref.lastIndexOf("/")+1);
	}
	
	public void deleteReferential(String referentialFile) throws Exception {
		try {
			FileTools.delete(Environment.getPropertiesDirectory()+referentialFile);
		} catch(Exception e){
			e.printStackTrace();
			logger.info(e.getMessage());
			throw e;
		}
	}
	
	public void executeReferential(String referentialFile) throws Exception {
		try {
			FileTools.copy(Environment.getReferentialFile(), Environment.getReferentialFile()+".old");
			FileTools.delete(Environment.getReferentialFile());
			FileTools.copy(Environment.getPropertiesDirectory()+referentialFile, Environment.getReferentialFile());
			Process process;	
			String command = Environment.getScriptsDirectory() + "executeReferential.sh";
			String line;
			process = Runtime.getRuntime().exec(command);
			ErrorStreamReader errorStreamReader = new ErrorStreamReader(process);
			errorStreamReader.start();
			BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while ((line = in.readLine()) != null) {
				logger.debug(line);
			}
			in.close();
			errorStreamReader.interrupt();
		} catch (Exception exception) {
			logger.error(exception.getMessage());
			throw exception;
		}
	}
	
	public void saveCurrentReferential() throws Exception {
		try {
			Process process;	
			String command = Environment.getScriptsDirectory() + "saveCurrentReferential.sh";
			String line;
			process = Runtime.getRuntime().exec(command);
			ErrorStreamReader errorStreamReader = new ErrorStreamReader(process);
			errorStreamReader.start();
			BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while ((line = in.readLine()) != null) {
				logger.debug(line);
			}
			in.close();
			errorStreamReader.interrupt();
		} catch (Exception exception) {
			logger.error(exception.getMessage());
			throw exception;
		}
		
	}
	
	public SysEnv getEnvironmentVariable(String var) throws Exception{
		return GenericDatabaseDriver.getEnvironmentVariable(var);
	}
	
	public void setEnvironmentVariableContent(SysEnv sysEnv, String content) throws Exception{
		GenericDatabaseDriver.setEnvironmentVariableContent(sysEnv, content);
	}
}
