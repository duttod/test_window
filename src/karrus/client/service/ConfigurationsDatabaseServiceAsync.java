package karrus.client.service;

import java.util.List;

import karrus.shared.hibernate.CtLane;
import karrus.shared.hibernate.TtItinerary;
import karrus.shared.hibernate.RsuStation;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ConfigurationsDatabaseServiceAsync {

	// stations
	void getStations(AsyncCallback<List<RsuStation>> callback);
	void removeStation(String stationId, String stationSerial, AsyncCallback<Void> callback);
	void updateStation(RsuStation oldStation, RsuStation station, AsyncCallback<RsuStation> callback);
	void addNewStation(RsuStation station, AsyncCallback<RsuStation> callback);
	// lanes
	void addNewLane(CtLane lane, AsyncCallback<CtLane> callback);
	void updateLane(CtLane oldLane, CtLane lane, AsyncCallback<CtLane> callback);
	void removeLane(CtLane lane, AsyncCallback<Void> callback);
	// itineraries
	void getAllItineraries(AsyncCallback<List<TtItinerary>> callback);
	void removeItinerary(String name, String origin, String destination, AsyncCallback<Void> callback);
	void updateItinerary(String formerName, String formerOrigin, String formerDestination, String newName, String newOrigin, String newDestination, Integer nominalTravelTime, Integer maxTravelTime, Integer levelOfServiceThreshold1, Integer levelOfServiceThreshold2, Integer scale, AsyncCallback<TtItinerary> callback);
	void addNewItinerary(String name, String origin, String destination, Integer nominalTravelTime, Integer maxTravelTime, Integer levelOfServiceThreshold1, Integer levelOfServiceThreshold2, Integer scale, AsyncCallback<TtItinerary> callback);
	void getItinerary(String itineraryName, AsyncCallback<TtItinerary> callback);
}
