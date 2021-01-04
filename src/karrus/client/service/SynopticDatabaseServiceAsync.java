package karrus.client.service;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

import karrus.shared.hibernate.AlAlarmOpened;
import karrus.shared.hibernate.CtCountData;
import karrus.shared.hibernate.WthMeteoData;

public interface SynopticDatabaseServiceAsync {
	
	void getLastDataForItineraries(List<String> itinerariesNames, AsyncCallback<Map<String, Double>> callback);
	void getLastDataForLanes(List<String> stationsNames, List<String> lanes, AsyncCallback<Map<String, CtCountData>> callback);
	void getOpenedAlarms(AsyncCallback<List<AlAlarmOpened>> callback);
	void getLastWeatherDataForStations(List<String> stationsNames, AsyncCallback<Map<String, WthMeteoData>> callback);
}
