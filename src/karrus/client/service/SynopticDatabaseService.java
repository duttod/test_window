package karrus.client.service;

import java.util.List;
import java.util.Map;

import karrus.shared.hibernate.AlAlarmOpened;
import karrus.shared.hibernate.CtCountData;
import karrus.shared.hibernate.WthMeteoData;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("SynopticDatabaseService")
public interface SynopticDatabaseService extends RemoteService{
	
	Map<String, Double> getLastDataForItineraries(List<String> itinerariesNames) throws Exception;
	Map<String, CtCountData> getLastDataForLanes(List<String> stationsNames, List<String> lanes) throws Exception;
	List<AlAlarmOpened> getOpenedAlarms() throws Exception;
	Map<String, WthMeteoData> getLastWeatherDataForStations(List<String> stationsNames) throws Exception;
}
