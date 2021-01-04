package karrus.server.database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karrus.shared.hibernate.CtCountData;
import karrus.shared.hibernate.SynAlarm;
import karrus.shared.hibernate.SynRdtDisplay;
import karrus.shared.hibernate.SynTtColor;
import karrus.shared.hibernate.SynRdtColor;
import karrus.shared.hibernate.SynTtDisplay;
import karrus.shared.hibernate.SynWeatherDisplay;
import karrus.shared.hibernate.SynWeatherStation;
import karrus.shared.hibernate.TtBtItt;
import karrus.shared.hibernate.WthMeteoData;

import org.apache.log4j.Logger;


public class SynopticDatabaseDriver {

	private static Logger logger = Logger.getLogger(SynopticDatabaseDriver.class);
	
	@SuppressWarnings("unchecked")
	public static Map<String, Double> getLastDataForItineraries(List<String> itinerariesNames) throws Exception {
		Map<String, Double> itineraryNameToTtLastHourMeanMap = new HashMap<String, Double>();
		if (itinerariesNames.size() == 0) {
			return itineraryNameToTtLastHourMeanMap;
		}
		Calendar calendar = Calendar.getInstance();
		String toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:00").format(calendar.getTime());
		calendar.add(Calendar.MINUTE, -60);
		String fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:00").format(calendar.getTime());
		List<TtBtItt> ttBtItts = new ArrayList<TtBtItt>();
		String query = "";
		int[] ittsArray;
		int sum = 0;
		double mean = 0.;
		for (String itineraryName : itinerariesNames) {
		    sum = 0;
			query = "SELECT \"tt_bt_itt\".* FROM \"tt_bt_itt\", \"tt_itinerary\" WHERE ";
			query += "\"tt_bt_itt\".origin = '" + itineraryName.split("-")[0] + "' AND \"tt_bt_itt\".destination = '" + itineraryName.split("-")[1] + "' AND ";
			query += "\"tt_bt_itt\".\"timestamp\">'"+fromDate+"' AND \"tt_bt_itt\".\"timestamp\"<='"+toDate+"' AND ";
			query += "\"tt_bt_itt\".origin = \"tt_itinerary\".origin AND \"tt_bt_itt\".destination = \"tt_itinerary\".destination AND ";
			query += "\"tt_bt_itt\".travel_time < \"tt_itinerary\".scale ";
			query += "ORDER BY \"tt_bt_itt\".\"timestamp\" DESC LIMIT 10;";
			logger.info(query);
			ttBtItts = (List<TtBtItt>)HibernateDatabaseDriver.get(query, TtBtItt.class);
			if (ttBtItts.size() < 5) {
				continue;
			}
			else {
				ittsArray = new int[ttBtItts.size()];
				for (int i = 0; i < ttBtItts.size(); i++) {
					ittsArray[i] = ttBtItts.get(i).getId().getTravelTime();
				}
				Arrays.sort(ittsArray);
				for (int i = 0; i < ittsArray.length; i++) {
					sum += ittsArray[i];
				}
				mean = sum/ittsArray.length;
				itineraryNameToTtLastHourMeanMap.put(itineraryName, mean);
			}
		}
		return itineraryNameToTtLastHourMeanMap;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, CtCountData> getLastDataForLanes(List<String> stationsNames, List<String> lanes) throws Exception {
		Map<String, CtCountData> lanesToCtCountData = new HashMap<String, CtCountData>();
		if (stationsNames.size() == 0) {
			return lanesToCtCountData;
		}
		try {
			Calendar calendar = Calendar.getInstance();
			String toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:00").format(calendar.getTime());
			calendar.add(Calendar.MINUTE, -15);
			String fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:00").format(calendar.getTime());
			List<CtCountData> ctCountData = new ArrayList<CtCountData>();
			String query = "SELECT * FROM \"ct_count_data\" WHERE ( ";
			for (int i = 0; i < stationsNames.size(); i++) {
				query += "(station = '" + stationsNames.get(i) + "' AND lane = '" + lanes.get(i) + "')  OR ";
			}
			query = query.substring(0, query.length() - 3) + ") ";
			query += " AND \"timestamp\">'"+fromDate+"' AND \"timestamp\"<='"+toDate+"' ORDER BY \"timestamp\" DESC";
			logger.info(query);
			ctCountData = (List<CtCountData>)HibernateDatabaseDriver.get(query, CtCountData.class);
			for (int i = 0; i < stationsNames.size(); i++) {
				for (int j = 0; j < ctCountData.size(); j++) {
					if (ctCountData.get(j).getId().getStation().equals(stationsNames.get(i)) && ctCountData.get(j).getId().getLane().equals(lanes.get(i))) {
						lanesToCtCountData.put(stationsNames.get(i) + "/" + lanes.get(i), ctCountData.get(j));
						break;
					}
				}
			}
			return lanesToCtCountData;
		} catch (Exception exception){
			logger.error(exception.getMessage());
			throw exception;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, WthMeteoData> getLastWeatherDataForStations(List<String> stationsNames) throws Exception {
		Map<String, WthMeteoData> stationToWeatherData = new HashMap<String, WthMeteoData>();
		if (stationsNames.size() == 0) {
			return stationToWeatherData;
		}
		try {
			Calendar calendar = Calendar.getInstance();
			String toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:00").format(calendar.getTime());
			calendar.add(Calendar.MINUTE, -15);
			String fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:00").format(calendar.getTime());
			List<WthMeteoData> weatherData = new ArrayList<WthMeteoData>();
			String query = "SELECT * FROM \"wth_meteo_data\" WHERE ( ";
			for (int i = 0; i < stationsNames.size(); i++) {
				query += "station = '" + stationsNames.get(i) + "' OR ";
			}
			query = query.substring(0, query.length() - 3) + ") ";
			query += " AND \"timestamp\">'"+fromDate+"' AND \"timestamp\"<='"+toDate+"' ORDER BY \"timestamp\" DESC";
			logger.info(query);
			weatherData = (List<WthMeteoData>)HibernateDatabaseDriver.get(query, WthMeteoData.class);
			for (int i = 0; i < stationsNames.size(); i++) {
				for (int j = 0; j < weatherData.size(); j++) {
					if (weatherData.get(j).getId().getStation().equals(stationsNames.get(i))) {
						stationToWeatherData.put(stationsNames.get(i), weatherData.get(j));
						break;
					}
				}
			}
			return stationToWeatherData;
		} catch (Exception exception){
			logger.error(exception.getMessage());
			throw exception;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<SynAlarm> getSynAlarms() throws Exception {
		try {
			String query = "SELECT * FROM \"syn_alarm\" ORDER BY source";
			logger.info(query);
			List<SynAlarm> synAlarms = (List<SynAlarm>)HibernateDatabaseDriver.get(query, SynAlarm.class);
			return synAlarms;
		} catch (Exception exception){
			logger.error(exception.getMessage());
			throw exception;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<SynRdtColor> getSynRdtColors() throws Exception {
		try {
			String query = "SELECT * FROM \"syn_rdt_color\" ORDER BY station";
			logger.info(query);
			List<SynRdtColor> synRdtColors = (List<SynRdtColor>)HibernateDatabaseDriver.get(query, SynRdtColor.class);
			return synRdtColors;
		} catch (Exception exception){
			logger.error(exception.getMessage());
			throw exception;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<SynRdtDisplay> getSynRdtDisplays() throws Exception {
		try {
			String query = "SELECT * FROM \"syn_rdt_display\" ORDER BY item_id";
			logger.info(query);
			List<SynRdtDisplay> synRdtDisplays = (List<SynRdtDisplay>)HibernateDatabaseDriver.get(query, SynRdtDisplay.class);
			return synRdtDisplays;
		} catch (Exception exception){
			logger.error(exception.getMessage());
			throw exception;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<SynTtColor> getSynTtColors() throws Exception {
		try {
			String query = "SELECT * FROM \"syn_tt_color\" ORDER BY item_id";
			logger.info(query);
			List<SynTtColor> synTtColors = (List<SynTtColor>)HibernateDatabaseDriver.get(query, SynTtColor.class);
			return synTtColors;
		} catch (Exception exception){
			logger.error(exception.getMessage());
			throw exception;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<SynTtDisplay> getSynTtDisplays() throws Exception {
		try {
			String query = "SELECT * FROM \"syn_tt_display\" ORDER BY item_id";
			logger.info(query);
			List<SynTtDisplay> synTtDisplays = (List<SynTtDisplay>)HibernateDatabaseDriver.get(query, SynTtDisplay.class);
			return synTtDisplays;
		} catch (Exception exception){
			logger.error(exception.getMessage());
			throw exception;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<SynWeatherStation> getSynWeatherStations() throws Exception {
		try {
			String query = "SELECT * FROM \"syn_weather_station\" ORDER BY item_id";
			logger.info(query);
			List<SynWeatherStation> synWeatherStations = (List<SynWeatherStation>)HibernateDatabaseDriver.get(query, SynWeatherStation.class);
			return synWeatherStations;
		} catch (Exception exception){
			logger.error(exception.getMessage());
			throw exception;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<SynWeatherDisplay> getSynWeatherDisplays() throws Exception {
		try {
			String query = "SELECT * FROM \"syn_weather_display\" ORDER BY item_id";
			logger.info(query);
			List<SynWeatherDisplay> synWeatherDisplays = (List<SynWeatherDisplay>)HibernateDatabaseDriver.get(query, SynWeatherDisplay.class);
			return synWeatherDisplays;
		} catch (Exception exception){
			logger.error(exception.getMessage());
			throw exception;
		}
	}
}
