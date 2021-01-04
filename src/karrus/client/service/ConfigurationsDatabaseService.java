package karrus.client.service;

import java.util.List;

import karrus.shared.hibernate.CtLane;
import karrus.shared.hibernate.TtItinerary;
import karrus.shared.hibernate.RsuStation;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("ConfigurationsDatabaseService")
public interface ConfigurationsDatabaseService extends RemoteService{

	// Stations
	List<RsuStation> getStations() throws Exception;
	void removeStation(String stationId, String stationSerial) throws Exception;
	RsuStation updateStation(RsuStation oldStation, RsuStation station)  throws Exception;
	RsuStation addNewStation(RsuStation station)  throws Exception;
	// Lanes
	CtLane addNewLane(CtLane lane) throws Exception;
	CtLane updateLane(CtLane oldLane, CtLane lane) throws Exception;
	void removeLane(CtLane lane) throws Exception;
	// Itineraries
	void removeItinerary(String name, String origin, String destination) throws Exception;
	TtItinerary updateItinerary(String formerName, String formerOrigin, String formerDestination, String newName, String newOrigin, String newDestination, Integer nominalTravelTime, Integer maxTravelTime, Integer levelOfServiceThreshold1, Integer levelOfServiceThreshold2, Integer scale)  throws Exception;
	TtItinerary addNewItinerary(String name, String origin, String destination, Integer nominalTravelTime, Integer maxTravelTime, Integer levelOfServiceThreshold1, Integer levelOfServiceThreshold2, Integer scale)  throws Exception;
	List<TtItinerary> getAllItineraries() throws Exception;
	TtItinerary getItinerary(String itineraryName) throws Exception;
}
