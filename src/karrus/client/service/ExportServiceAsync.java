package karrus.client.service;

import java.util.Date;
import java.util.List;

import karrus.shared.hibernate.TtItinerary;
import karrus.shared.hibernate.RsuStation;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ExportServiceAsync {
	
	void getCountDataCsvFilePath(String stationName, String way, Date initDate, Date finalDate, AsyncCallback<String> callback);
	void exportStationsTable(List<RsuStation> stations, AsyncCallback<String> callback);
	void exportLanesTable(AsyncCallback<String> callback);
	void exportClosedAlarmsTable(Date initDate, Date finalDate, String Source, String Type, String State, AsyncCallback<String> callback);
	void exportOpenedAlarmsTable(AsyncCallback<String> callback);
	void getIndividualTravelTimeCsvFilePath(String itinerary, Date initDate, Date finalDate, AsyncCallback<String> callback);
	void getStatisticalTravelTimeCsvFilePath(String selectedItinerary,Date initialDate, Date finalDate, int period, int horizon, String typeOfTimestamp, AsyncCallback<String> asyncCallback);
	void getWeatherDataCsvFilePath(String stationName, Date initDate, Date finalDate, AsyncCallback<String> callback);
	void exportItinerariesTable(List<TtItinerary> itineraries, AsyncCallback<String> callback);
}
