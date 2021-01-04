package karrus.server.database;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import karrus.shared.hibernate.AlAlarmClosed;
import karrus.shared.hibernate.AlAlarmClosedId;
import karrus.shared.hibernate.AlAlarmOpened;
import karrus.shared.hibernate.AlAlarmOpenedId;
import karrus.shared.language.Language;

import org.apache.log4j.Logger;


public class AlarmDatabaseDriver {

	private static Logger logger = Logger.getLogger(AlarmDatabaseDriver.class);
	private static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * Get filtered alarms according to several criteria.
	 * @param startDate Start date.
	 * @param stopDate Stop date.
	 * @param source Source. All sources if null.
	 * @param type Type. All types if null.
	 * @param state State. All states if null.
	 * @return List of alarms respecting the criteria.
	 * @throws Exception Raised if something goes wrong.
	 */
	@SuppressWarnings("unchecked")
	static public List<AlAlarmClosed> getAlarmsHistoryFiltered(Date startDate, Date stopDate, String source, String type, String state) throws Exception {
		String query = "SELECT * FROM \"al_alarm_closed\" WHERE \"opening_timestamp\">='"+startDate+"' AND \"opening_timestamp\"<='"+stopDate+"'";
		if (source!=null) {
			query +="AND \"source\" = '" + source + "' ";
		}
		if (type!=null) {
			query +="AND \"type\" = '" + type + "' ";
		}
		if (state!=null) {
			query +="AND \"status\" = '" + state + "' ";
		}
		query +="ORDER BY \"opening_timestamp\" DESC, \"source\", \"type\";";
		logger.info(query);
		return (List<AlAlarmClosed>)HibernateDatabaseDriver.get(query, AlAlarmClosed.class);
	}
	
	/**
	 * Get filtered opened alarms according to several criteria.
	 * @param startDate Start date.
	 * @param stopDate Stop date.
	 * @param source Source. All sources if null.
	 * @param type Type. All types if null.
	 * @param state State. All states if null.
	 * @return List of alarms respecting the criteria.
	 * @throws Exception Raised if something goes wrong.
	 */
	@SuppressWarnings("unchecked")
	static public List<AlAlarmOpened> getCurrentAlarmsFiltered(Date startDate, Date stopDate, String source, String type, String state) throws Exception {
		String query = "SELECT * FROM \"al_alarm_opened\" WHERE \"opening_timestamp\">='"+startDate+"' AND \"opening_timestamp\"<='"+stopDate+"'";
		if (source!=null) {
			query +="AND \"source\" = '" + source + "' ";
		}
		if (type!=null) {
			query +="AND \"type\" = '" + type + "' ";
		}
		if (state!=null) {
			query +="AND \"current-status\" = '" + state + "' ";
		}
		query +="ORDER BY \"opening_timestamp\" DESC;";
		logger.info(query);
		return (List<AlAlarmOpened>)HibernateDatabaseDriver.get(query, AlAlarmOpened.class);
	}
	
	@SuppressWarnings("unchecked")
	static public List<AlAlarmClosed> getClosedAlarms() throws Exception {
		String query = "SELECT * FROM \"al_alarm_closed\" ORDER BY \"opening_timestamp\" DESC;";
		logger.info(query);
		return (List<AlAlarmClosed>)HibernateDatabaseDriver.get(query, AlAlarmClosed.class);
	}
	
	@SuppressWarnings("unchecked")
	static public List<AlAlarmOpened> getOpenedAlarms() throws Exception {
		String query = "SELECT * FROM \"al_alarm_opened\" ORDER BY \"opening_timestamp\" DESC, \"source\", \"type\";";
//		logger.info(query);
		return (List<AlAlarmOpened>)HibernateDatabaseDriver.get(query, AlAlarmOpened.class);
	}

	@SuppressWarnings("unchecked")
	static public List<AlAlarmClosed> getAlarmSources() throws Exception {
		String query = "SELECT DISTINCT ON (\"source\") * FROM \"al_alarm_closed\";";
		logger.info(query);
		return (List<AlAlarmClosed>)HibernateDatabaseDriver.get(query, AlAlarmClosed.class);
	}
	
	static public void closeAlarms(List<AlAlarmOpened> selectedAlarms) throws Exception {
		Date now = new Date();
		now = dateFormatter.parse(dateFormatter.format(now));
		for (AlAlarmOpened alarmOpened : selectedAlarms) {
		    AlAlarmClosed alarm = new AlAlarmClosed(new AlAlarmClosedId(alarmOpened.getId().getOpeningTimestamp(), alarmOpened.getId().getType(), alarmOpened.getId().getSource()),
		    										alarmOpened.getAcknowledgementTimestamp(), now, Language.solvedAlarmStatus);
			HibernateDatabaseDriver.save(alarm);
		    HibernateDatabaseDriver.delete(alarmOpened);
		}
	}

	static public void acknowledgeAlarms(List<AlAlarmOpened> selectedAlarms) throws Exception {
		Date now = new Date();
		now = dateFormatter.parse(dateFormatter.format(now));
		for (AlAlarmOpened alarmOpened : selectedAlarms) {
			alarmOpened.setAcknowledgementTimestamp(now);
			alarmOpened.setStatus(Language.acknowledgedAlarmStatus);
			HibernateDatabaseDriver.update(alarmOpened);
		}
	}
	
	static public void openAlarms(List<AlAlarmClosed> selectedAlarms) throws Exception {
		Date now = new Date();
		now = dateFormatter.parse(dateFormatter.format(now));
		for (AlAlarmClosed alarm : selectedAlarms) {
			AlAlarmOpened alarmOpened = new AlAlarmOpened(new AlAlarmOpenedId(alarm.getId().getOpeningTimestamp(), alarm.getId().getType(), alarm.getId().getSource()),
					                                     now, Language.acknowledgedAlarmStatus);
			HibernateDatabaseDriver.save(alarmOpened);
			HibernateDatabaseDriver.delete(alarm);
		}
	}
}
