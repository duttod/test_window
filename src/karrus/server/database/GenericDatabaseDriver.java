package karrus.server.database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karrus.shared.ApplicationException;
import karrus.shared.CountDataAndLanesMap;
import karrus.shared.hibernate.AlAlarmClosed;
import karrus.shared.hibernate.AlAlarmClosedId;
import karrus.shared.hibernate.AlAlarmOpened;
import karrus.shared.hibernate.AlAlarmOpenedId;
import karrus.shared.hibernate.CtCountData;
import karrus.shared.hibernate.CtLane;
import karrus.shared.hibernate.RsuDiag;
import karrus.shared.hibernate.RsuStation;
import karrus.shared.hibernate.SysEnv;
import karrus.shared.hibernate.TtBtItt;
import karrus.shared.hibernate.TtBtStat;
import karrus.shared.hibernate.TtItinerary;
import karrus.shared.hibernate.UsrCredential;
import karrus.shared.hibernate.UsrUser;
import karrus.shared.hibernate.V2xAlarm;
import karrus.shared.hibernate.WthMeteoData;
import karrus.shared.language.Language;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

public class GenericDatabaseDriver {

	private static Logger logger = Logger.getLogger(GenericDatabaseDriver.class);
	
	@SuppressWarnings("unchecked")
	public static List<TtBtItt> getBtItt(String itinerary, Date initDate, Date finalDate) throws Exception {
		try {
			TtItinerary i = getItineraryFromName(itinerary);
			String originId = i.getId().getOrigin();
			String destinationId = i.getId().getDestination();
			String query = "SELECT tt_bt_itt.timestamp, tt_bt_itt.mac_secret, tt_bt_itt.class, tt_bt_itt.att_valid, tt_bt_itt.travel_time, tt_bt_itt.origin, tt_bt_itt.destination "; 
			query += "FROM \"tt_bt_itt\",\"tt_itinerary\" ";
		    query += "WHERE \"tt_bt_itt\".origin='"+originId+"' AND \"tt_bt_itt\".destination='"+destinationId+"' AND \"tt_bt_itt\".timestamp>'"+initDate+"' ";
		    query += "AND \"tt_bt_itt\".timestamp<'"+finalDate+"' AND \"tt_bt_itt\".origin = tt_itinerary.origin AND \"tt_bt_itt\".destination = tt_itinerary.destination ";
		    query += "AND \"tt_bt_itt\".travel_time >=0.4*tt_itinerary.nominal_travel_time ORDER BY \"timestamp\" ASC;";
			logger.info(query);
			return (List<TtBtItt>)HibernateDatabaseDriver.get(query, TtBtItt.class);
		} catch (Exception e){
			logger.error(e.getMessage());
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<TtBtStat> getBtStat(String itineraryString, Date initDate, Date finalDate) throws Exception {
		try {
			TtItinerary itinerary = getItineraryFromName(itineraryString);
			String originId = itinerary.getId().getOrigin();
			String destinationId = itinerary.getId().getDestination();
			String query = "SELECT * FROM \"tt_bt_stat\" WHERE \"origin\"='"+originId+"' AND \"destination\"='"+destinationId+"' AND \"timestamp\">'"+initDate+"' AND \"timestamp\"<'"+finalDate+"' ORDER BY \"timestamp\" ASC;";
			logger.info(query);
			return (List<TtBtStat>)HibernateDatabaseDriver.get(query,TtBtStat.class);
		} catch (Exception e){
			logger.error(e.getMessage());
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<TtItinerary> getAllItineraries() throws Exception {
		try {
			String query = "SELECT * FROM \"tt_itinerary\" ORDER BY name";
			logger.info(query);
			return (List<TtItinerary>)HibernateDatabaseDriver.get(query, TtItinerary.class);
		} catch (Exception e){
			logger.error(e.getMessage());
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static TtItinerary getItineraryFromName(String itinerary) throws Exception {
		try {
			String query = "SELECT * FROM \"tt_itinerary\" WHERE name='"+itinerary+"'";
			logger.info(query);
			List<TtItinerary> out = (List<TtItinerary>)HibernateDatabaseDriver.get(query, TtItinerary.class);
			if (out.size()!=1) {
				throw new Exception("Error in getOrgin or Dest FromItinerary : not only one entry "+out.size());
			}	
			return out.get(0);
		} catch (Exception e){
			logger.error(e.getMessage());
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<WthMeteoData> getWeatherDataForStation(String stationName, Date startDate, Date finalDate) throws Exception {
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String query = "SELECT * FROM \"wth_meteo_data\" WHERE timestamp >= '"+dateFormatter.format(startDate)+"' AND ";
			query += "timestamp < '" + dateFormatter.format(finalDate) + "' ";
			query += "AND \"station\" = '" + stationName + "' ";
			query += "ORDER BY \"timestamp\" ASC";
			logger.info(query);
			return (List<WthMeteoData>)HibernateDatabaseDriver.get(query, WthMeteoData.class);
		} catch (Exception e){
			logger.error(e.getMessage());
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static CountDataAndLanesMap getCountDataForStationAndWay(String stationName, String way, Date startDate, Date finalDate) throws Exception {
		try {
			String query = "SELECT * FROM \"ct_lane\" WHERE \"station\" = '" + stationName + "' AND \"way\" = '" + way + "' ORDER by station, lane";
			logger.info(query);
			List<CtLane> lanesList = (List<CtLane>)HibernateDatabaseDriver.get(query, CtLane.class);
			Map<String, String> lanesMap = new HashMap<String, String>();
			for (CtLane lane : lanesList) {
				lanesMap.put(lane.getId().getLane(), lane.getWay());
			}
			List<CtCountData> countDataList = new ArrayList<CtCountData>();
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (lanesMap.keySet().size() > 0) {
				query = "SELECT * FROM \"ct_count_data\" WHERE timestamp >= '"+dateFormatter.format(startDate)+"' AND ";
				query += "timestamp < '" + dateFormatter.format(finalDate) + "' ";
				query += "AND \"station\" = '" + stationName + "' AND (";
				for (String laneId : lanesMap.keySet()) {
					query += "\"lane\" = '" + laneId + "' OR ";
				}
				query = query.substring(0, query.length() - 4) + ") ";
				query += "ORDER BY \"lane\", \"timestamp\" ASC";
				logger.info(query);
				countDataList = (List<CtCountData>)HibernateDatabaseDriver.get(query, CtCountData.class);
			} 
			else {
				logger.info(Language.noLanesDefined(stationName, way));
			}
			return new CountDataAndLanesMap(lanesMap, countDataList);
		} catch (Exception e){
			logger.error(e.getMessage());
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<String> getWaysForStation(String stationName) throws Exception {
		String query = "Select DISTINCT ON(way) * FROM ct_lane where station = '" + stationName + "';";
		logger.info(query);
		List<CtLane> lanesList = (List<CtLane>)HibernateDatabaseDriver.get(query, CtLane.class);
		List<String> waysList = new ArrayList<String>();
		for (CtLane ctLane : lanesList) {
			waysList.add(ctLane.getWay());
		}
		return waysList;
	}
	
	@SuppressWarnings("unchecked")
	public static List<CtCountData> getCountDataForStationAndLane(String stationName, String lane, Date startDate, Date finalDate) throws Exception {
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String query = "SELECT * FROM \"ct_count_data\" WHERE timestamp >= '"+dateFormatter.format(startDate)+"' AND ";
			query += "timestamp < '" + dateFormatter.format(finalDate) + "' ";
			query += "AND \"station\" = '" + stationName + "' ";
			query += "AND \"lane\" = '" + lane + "' ";
			query += "ORDER BY \"timestamp\" ASC";
			logger.info(query);
			return (List<CtCountData>)HibernateDatabaseDriver.get(query, CtCountData.class);
		} catch (Exception e){
			logger.error(e.getMessage());
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static CountDataAndLanesMap getCountDataForExport(String stationName, String way, Date startDate, Date finalDate) throws Exception {
		try {
			String query = "SELECT * FROM \"ct_lane\" WHERE \"station\" = '" + stationName + "' AND \"way\" = '" + way + "' ORDER by station, lane";
			logger.info(query);
			List<CtLane> lanesList = (List<CtLane>)HibernateDatabaseDriver.get(query, CtLane.class);
			Map<String, String> lanesMap = new HashMap<String, String>();
			for (CtLane lane : lanesList) {
				lanesMap.put(lane.getId().getLane(), lane.getId().getLane());
			}
			List<CtCountData> countDataList = new ArrayList<CtCountData>();
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (lanesMap.keySet().size() > 0) {
				query = "SELECT * FROM \"ct_count_data\" WHERE timestamp >= '"+dateFormatter.format(startDate)+"' AND ";
				query += "timestamp < '" + dateFormatter.format(finalDate) + "' ";
				query += "AND \"station\" = '" + stationName + "' AND (";
				for (String laneId : lanesMap.keySet()) {
					query += "\"lane\" = '" + laneId + "' OR ";
				}
				
				query = query.substring(0, query.length() - 4) + ") ";
				query += "ORDER BY \"timestamp\", \"lane\" ASC";
				logger.info(query);
				countDataList = (List<CtCountData>)HibernateDatabaseDriver.get(query, CtCountData.class);
			} 
			else {
				logger.info(Language.noLanesDefined(stationName, way));
			}
			return new CountDataAndLanesMap(lanesMap, countDataList);
		} catch (Exception e){
			logger.error(e.getMessage());
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<RsuDiag> getRsuDiagnosticForStation(String stationName, Date startDate, Date finalDate) throws Exception {
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String query = "SELECT * FROM \"rsu_diag\" WHERE timestamp >= '"+dateFormatter.format(startDate)+"' AND ";
			query += "timestamp < '" + dateFormatter.format(finalDate) + "' ";
			query += "AND \"station\" = '" + stationName + "' ";
			query += "ORDER BY \"timestamp\" ASC";
			logger.info(query);
			return (List<RsuDiag>)HibernateDatabaseDriver.get(query, RsuDiag.class);
		} catch (Exception e){
			logger.error(e.getMessage());
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<V2xAlarm> getV2xAlarms(String stationName, Date startDate, Date finalDate) throws Exception {
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String query = "SELECT * FROM \"v2x_alarm\" WHERE timestamp >= '"+dateFormatter.format(startDate)+"' AND ";
			query += "timestamp < '" + dateFormatter.format(finalDate) + "' ";
			query += "AND \"station\" = '" + stationName + "' ";
			query += "ORDER BY \"timestamp\" ASC";
			logger.info(query);
			return (List<V2xAlarm>)HibernateDatabaseDriver.get(query, V2xAlarm.class);
		} catch (Exception e){
			logger.error(e.getMessage());
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<RsuStation> getAllStations() throws Exception {
		try {
			String query = "SELECT * FROM \"rsu_station\" ORDER BY id";
			logger.info(query);
			List<RsuStation> lcrStations = (List<RsuStation>)HibernateDatabaseDriver.get(query, RsuStation.class);
			return lcrStations;
		} catch (Exception exception){
			logger.error(exception.getMessage());
			throw exception;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<CtLane> getAllLanes() throws Exception {
		try {
			String query = "SELECT * FROM \"ct_lane\" ORDER BY station, lane";
			logger.info(query);
			return (List<CtLane>)HibernateDatabaseDriver.get(query, CtLane.class);
		} catch (Exception exception){
			logger.error(exception.getMessage());
			throw exception;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static UsrUser checkUserAndPassword(String login, String password) throws Exception {
		List<UsrUser> out = new ArrayList<UsrUser>();
		Session session = karrus.server.database.HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try{
			transaction = session.beginTransaction();
			String query = "SELECT * FROM \"usr_user\" WHERE \"login\"='"+login+"'";
			out = session.createSQLQuery(query).addEntity(UsrUser.class).list();
			transaction.commit();
			if (out.size()==0)
				throw new Exception(Language.loginError1);
			if (out.size()>1)
				throw new Exception(Language.loginError2);
			query = "SELECT * FROM \"usr_user\" WHERE \"login\"='"+login+"' AND password = md5('" + password + "')";
			out = session.createSQLQuery(query).addEntity(UsrUser.class).list();
			if (out.size() != 1){
				throw new Exception(Language.loginError3);
			}
		} catch (HibernateException e) {
			transaction.rollback();
			logger.error(e.getMessage()+"\n"+e.getCause());
			throw new Exception(Language.loginError4);
		}
		finally {
			session.close();
		}
		return out.get(0);
	}
	
	
/*
 *#######################################################################################################
 *                                            STATIONS                                                 ##
 *#######################################################################################################	
 */
	
	@SuppressWarnings("unchecked")
	static public List<RsuStation> getStations() throws Exception {
		List<RsuStation> out = new ArrayList<RsuStation>();
		Session session = karrus.server.database.HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try{
			transaction = session.beginTransaction();
			String query = "SELECT * FROM \"rsu_station\" ORDER BY position asc";
			logger.trace("query="+query);
			out = session.createSQLQuery(query).addEntity(RsuStation.class).list();
			transaction.commit();
		} catch (HibernateException e) {
			transaction.rollback();
			logger.error(e.getMessage()+"\n"+e.getCause());
			throw new Exception(e.getMessage()+"\n"+e.getCause());
		}
		finally {
			session.close();
		}
		return out;
	}
	
	public static void removeStation(String stationId, String stationSerial) throws Exception {
		Session session = karrus.server.database.HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try{
			// delete station
			transaction = session.beginTransaction();
			String query="DELETE FROM \"rsu_station\" WHERE id='"+stationId+"' AND serial='"+stationSerial+"'; ";
			logger.debug(query);
			SQLQuery sqlQuery = session.createSQLQuery(query);
			sqlQuery.executeUpdate();
			transaction.commit();
		} catch (HibernateException e) {
			transaction.rollback();
			logger.error(e.getMessage()+"\n"+e.getCause());
			throw new Exception(e.getMessage()+"\n"+e.getCause());
		}
		finally {
			session.close();
		}	
	}

	public static RsuStation updateStation(RsuStation oldStation, RsuStation station) throws Exception {
		// If primary key has not changed, update the station in the database
		if (oldStation.getId().equals(station.getId())) {
			HibernateDatabaseDriver.update(station);
		}
		// Else save new station and delete previous one
		else {
			try {
				HibernateDatabaseDriver.save(station);
				HibernateDatabaseDriver.delete(oldStation);
			}
			catch (ConstraintViolationException e) {
				throw new ApplicationException(Language.stationConstraintViolationError(station.getId()));
			}
		}
		return getStation(station.getId(), station.getSerial());
	}

	public static RsuStation addNewStation(RsuStation station) throws Exception {
		try {
			HibernateDatabaseDriver.save(station);
			return getStation(station.getId(), station.getSerial());
		}
		catch (ConstraintViolationException e) {
			throw new ApplicationException(Language.stationConstraintViolationError(station.getId()));
		}
	}
	
	@SuppressWarnings("unchecked")
	private static RsuStation getStation(String stationId, String stationSerial) throws Exception {
		List<RsuStation> out = new ArrayList<RsuStation>();
		Session session = karrus.server.database.HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try{
			transaction = session.beginTransaction();
			String query = "SELECT * FROM \"rsu_station\" WHERE \"id\"='"+stationId+"' AND serial='"+stationSerial+"';";
			// logger.debug(query);
			out = session.createSQLQuery(query).addEntity(RsuStation.class).list();
			if (out.size()!=1)
				throw new Exception("Error in getStation: there is no exactly one element...");
			transaction.commit();
		} catch (HibernateException e) {
			transaction.rollback();
			logger.error(e.getMessage()+"\n"+e.getCause());
			throw new Exception(e.getMessage()+"\n"+e.getCause());
		}
		finally {
			session.close();
		}
		return out.get(0);
	}

	
/*
 *#######################################################################################################
                                              USERS	                                                   ##
 *#######################################################################################################	
 */
	
	@SuppressWarnings("unchecked")
	static public List<UsrUser> getUsers() throws Exception {
		List<UsrUser> out = new ArrayList<UsrUser>();
		Session session = karrus.server.database.HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try{
			transaction = session.beginTransaction();
			String query = "SELECT * FROM \"usr_user\" ORDER BY login asc";
			logger.info("query="+query);
			out = session.createSQLQuery(query).addEntity(UsrUser.class).list();
			transaction.commit();
		} catch (HibernateException e) {
			transaction.rollback();
			logger.error(e.getMessage()+"\n"+e.getCause());
			throw new Exception(e.getMessage()+"\n"+e.getCause());
		}
		finally {
			session.close();
		}
		return out;
	}
	
	@SuppressWarnings("unchecked")
	static private UsrUser getUser(String login) throws Exception {
		List<UsrUser> out = new ArrayList<UsrUser>();
		Session session = karrus.server.database.HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			String query = "SELECT * FROM \"usr_user\" WHERE \"login\"='"+login+"'";
			out = session.createSQLQuery(query).addEntity(UsrUser.class).list();
			transaction.commit();
			if (out.size()!=1)
				throw new Exception("Error in getUser: there is no exactly one element...");
		} catch (HibernateException e) {
			transaction.rollback();
			logger.error(e.getMessage()+"\n"+e.getCause());
			throw new Exception(e.getMessage()+"\n"+e.getCause());
		}
		finally {
			session.close();
		}
		return out.get(0);
	}
	
	public static UsrUser updatePassword(UsrUser formerUser, String newPassword) throws Exception {
		Session session = karrus.server.database.HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			String query="UPDATE \"usr_user\" SET \"password\"=md5('"+newPassword+"')";
			query += " WHERE \"login\"='"+formerUser.getLogin()+"' AND \"password\"='"+formerUser.getPassword()+"' AND \"credential\"='"+formerUser.getCredential()+"'";
			logger.debug(query);
			SQLQuery sqlQuery = session.createSQLQuery(query);
			sqlQuery.executeUpdate();
			transaction.commit();
		} catch (HibernateException e) {
			transaction.rollback();
			logger.error(e.getMessage()+"\n"+e.getCause());
			throw new Exception(e.getMessage()+"\n"+e.getCause());
		}
		finally {
			session.close();
		}
		return getUser(formerUser.getLogin());	
	}
	
	public static UsrUser updateUser(UsrUser formerUser, String newLogin, String newEmail, String newCredential) throws Exception {
		
		String query="UPDATE \"usr_user\" SET  \"login\"='"+newLogin+"', \"credential\"='"+newCredential+"', \"email\" = '" + newEmail +"'";
		query += " WHERE \"login\"='"+formerUser.getLogin()+"';";
		logger.debug(query);
		HibernateDatabaseDriver.executeQuery(query);
		return getUser(newLogin);	
	}

	public static UsrUser addNewUser(String login, String password, String email, String credential) throws Exception{
		String query = "";
		query += "INSERT INTO \"usr_user\" (login, password, email, \"credential\") VALUES ('" + login + "', md5('" + password + "'), '" + email + "', '" + credential + "')";
		logger.info(query);
		HibernateDatabaseDriver.executeQuery(query);
		return getUser(login);
	}
	
	public static void removeUser(String login) throws Exception {
		Session session = karrus.server.database.HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			String query="DELETE FROM \"usr_user\" WHERE \"login\"='"+login+"'";
			logger.debug(query);
			SQLQuery sqlQuery = session.createSQLQuery(query);
			sqlQuery.executeUpdate();
			transaction.commit();
		} catch (HibernateException e) {
			transaction.rollback();
			logger.error(e.getMessage()+"\n"+e.getCause());
			throw new Exception(e.getMessage()+"\n"+e.getCause());
		}
		finally {
			session.close();
		}
	}
	
/*
 *#######################################################################################################
                                      CREDENTIALS	                                                   ##
 *#######################################################################################################	
 */
		
	@SuppressWarnings("unchecked")
	public static List<UsrCredential> getCredentials() throws Exception {
		String query = "SELECT * FROM \"usr_credential\" ORDER BY \"credential\"";
		logger.info(query);
		return (List<UsrCredential>)HibernateDatabaseDriver.get(query, UsrCredential.class);
	}
	
	public static void removeCredential(UsrCredential credential) throws Exception {
		HibernateDatabaseDriver.delete(credential);
			
	}

	public static UsrCredential updateCredential(UsrCredential oldCredential, UsrCredential credential) throws Exception {		
		
		// If primary key has not changed, update the credential in the database
		if (oldCredential.getCredential().equals(credential.getCredential())) {
			HibernateDatabaseDriver.update(credential);
		}
		// Else delete previous credential and add new one
		else {
			try {
				HibernateDatabaseDriver.save(credential);
				HibernateDatabaseDriver.delete(oldCredential);
			}
			catch (ConstraintViolationException e) {
				throw new ApplicationException(Language.credentialConstraintViolationError(credential.getCredential()));
			}
		}
		return getCredential(credential.getCredential());
	}
	
	public static UsrCredential addNewCredential(UsrCredential credential) throws Exception {
		try {
			HibernateDatabaseDriver.save(credential);
			return getCredential(credential.getCredential());
		}
		catch (ConstraintViolationException e) {
			throw new ApplicationException(Language.credentialConstraintViolationError(credential.getCredential()));
		}
	}
	
	@SuppressWarnings("unchecked")
	private static UsrCredential getCredential(String credential) throws Exception {
		List<UsrCredential> out = new ArrayList<UsrCredential>();
		String query = "SELECT * FROM \"usr_credential\" WHERE \"credential\"='"+credential+"';";
		logger.info(query);
		out = (List<UsrCredential>)HibernateDatabaseDriver.get(query, UsrCredential.class);
		if (out.size()!=1) {
			throw new Exception("Error in getCredential: there is no exactly one element...");
		}
		return out.get(0);
	}
	
/*
 *#######################################################################################################
                                              ALARMS                                                   ##
 *#######################################################################################################	
 */
	
	@SuppressWarnings("unchecked")
	private static AlAlarmOpened getAlarmOpened(String type, String source) throws Exception {
		List<AlAlarmOpened> alAlarmOpenedList = new ArrayList<AlAlarmOpened>();
		String query = "SELECT * FROM \"al_alarm_opened\" WHERE \"type\"='" + type + "' AND \"source\"='" + source + "';";
		logger.info(query);
		alAlarmOpenedList = (List<AlAlarmOpened>)HibernateDatabaseDriver.get(query, AlAlarmOpened.class);
		if (alAlarmOpenedList.size()!=1) {
			throw new Exception("Error in getAlarmOpened: there is no unique alarm for these parameters");
		}
		return alAlarmOpenedList.get(0);
	}
	
	public static void openAlarm(Date openingTimestamp, String type, String source) {
		String status = Language.newAlarmStatus;
        try {
        	if (!GenericDatabaseDriver.isAlarmOpened(type, source)) {
        		// save new alarm to database
        		AlAlarmOpened alarmOpened = new AlAlarmOpened(new AlAlarmOpenedId(openingTimestamp, type, source), status);
        		HibernateDatabaseDriver.save(alarmOpened);
        		logger.info("New alarm saved: " + openingTimestamp+"|"+type+"|"+source+"|"+status+".");
        	}
        	else {
//        		logger.info("Alarm already present in database: " + openingTimestamp+"|"+type+"|"+source+"|"+status+".");
        	}
        } catch (Exception e) {
        	logger.error("Could not save the new openned alarm.");
        }
	} 
	 
	@SuppressWarnings("unchecked")
	static public boolean isAlarmOpened(String type, String source) throws Exception {
	    String query = "SELECT * FROM al_alarm_opened WHERE type='"+type+"' AND source='"+source+"';";
	    List<AlAlarmOpened> alAlarmOpenedList = (List<AlAlarmOpened>)HibernateDatabaseDriver.get(query, AlAlarmOpened.class);
	    if (alAlarmOpenedList.size()>0) {
	        return true;
	    }
	    else {
	        return false;
	    }
	}
	
	public static void closeAlarm(Date closingTimestamp, String type, String source) {
        try {
            if (GenericDatabaseDriver.isAlarmOpened(type, source)) {
                // get opened alarm
                AlAlarmOpened alarmOpened = GenericDatabaseDriver.getAlarmOpened(type, source);
                // get closing status from opening status
                String closingStatus = "";
                if (alarmOpened.getStatus().contains(Language.newAlarmStatus)) {
                    closingStatus = Language.disappearedAlarmStatus;
                }
                else if (alarmOpened.getStatus().contains(Language.acknowledgedAlarmStatus)) {
                    closingStatus = Language.solvedAlarmStatus;
                }
                // instantiate and store the closed alarm
                AlAlarmClosed alarmClosed = new AlAlarmClosed(new AlAlarmClosedId(alarmOpened.getId().getOpeningTimestamp(), type, source),
                        alarmOpened.getAcknowledgementTimestamp(), closingTimestamp, closingStatus);
                HibernateDatabaseDriver.save(alarmClosed);
                // delete the corresponding opened alarm
                String postgresQuery = "DELETE FROM al_alarm_opened WHERE type='"+type+"' and source='"+source+"'";
                HibernateDatabaseDriver.executeQuery(postgresQuery);
                logger.info("Opened alarm changed to closed alarm: " + type+"|"+source+"|"+closingStatus+".");
            }
            else {
//                logger.info("No open alarm: " + type+"|"+source+".");
            }
        } catch (Exception e) {
            logger.error("Could not create closed alarm and clear openned alarm.");
        }
    } 
	
/*
 *#######################################################################################################
                                            ENVIRONMENT                                                ##
 *#######################################################################################################	
 */
	
	@SuppressWarnings("unchecked")
	static public SysEnv getEnvironmentVariable(String var) throws Exception {
		String query = "SELECT * FROM \"sys_env\" where var = '" + var + "';";
		logger.debug(query);
		List<SysEnv> queryResult = (List<SysEnv>)HibernateDatabaseDriver.get(query, SysEnv.class);
		if (queryResult.size() == 1) {
			return queryResult.get(0);
		}
		else {
			throw new Exception(Language.systemEnvironmentDatabaseError(var));
		}
	}
	
	static public void setEnvironmentVariableContent(SysEnv sysEnv, String content) throws Exception {
		sysEnv.setContent(content);
		HibernateDatabaseDriver.update(sysEnv);
	}
}
