package karrus.client.service;

import java.util.Date;
import java.util.List;

import karrus.shared.hibernate.TtItinerary;
import karrus.shared.hibernate.RsuStation;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("ExportService")
public interface ExportService extends RemoteService {	

	String getCountDataCsvFilePath(String stationName, String way, Date initDate, Date finalDate) throws Exception;
	String exportStationsTable(List<RsuStation> stations) throws Exception;
	String exportLanesTable() throws Exception;
	String exportClosedAlarmsTable(Date initDate, Date finalDate, String Source, String Type, String State) throws Exception;
	String exportOpenedAlarmsTable() throws Exception;
	String getIndividualTravelTimeCsvFilePath(String itinerary, Date initDate, Date finalDate) throws Exception;
	String getStatisticalTravelTimeCsvFilePath(String selectedItinerary, Date initialDate, Date finalDate, int period, int horizon, String typeOfTimestamp) throws Exception;
	String getWeatherDataCsvFilePath(String stationName, Date initDate, Date finalDate) throws Exception;
	String exportItinerariesTable(List<TtItinerary> itineraries) throws Exception;
}
