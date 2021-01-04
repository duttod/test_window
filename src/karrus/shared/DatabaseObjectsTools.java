package karrus.shared;

import java.util.ArrayList;
import java.util.List;

import karrus.shared.hibernate.TtItinerary;
import karrus.shared.hibernate.RsuStation;
import karrus.shared.hibernate.UsrCredential;
import karrus.shared.hibernate.UsrUser;

public class DatabaseObjectsTools {
	
	
	private DatabaseObjects databaseObjects;
	
	public DatabaseObjectsTools(DatabaseObjects databaseObjects, IhmParameters ihmParameters){
		this.databaseObjects = databaseObjects;
	}

	public List<String> getStationsSerials(){
		List<String> names = new ArrayList<String>();
		for (RsuStation station : databaseObjects.getStations()) {
			names.add(station.getSerial());
		}	
		return names;
	}
	
	public List<String> getStationsIds(){
		List<String> ids = new ArrayList<String>();
		for (RsuStation e : databaseObjects.getStations()) { 
			ids.add(e.getId());
		}	
		return ids;
	}
	
	public List<String> getTravelTimeEnabledStationsIds(){
		List<String> ids = new ArrayList<String>();
		for (RsuStation station : databaseObjects.getStations()) {
			if (station.getCycleTravelTimeSec() != null && station.getCycleTravelTimeSec() > 0) {
				ids.add(station.getId());
			}
		}	
		return ids;
	}
	
	public List<String> getUserLogins(){
		List<String> userLogins = new ArrayList<String>();
		for (UsrUser user : databaseObjects.getUsers()) {
			userLogins.add(user.getLogin());
		}	
		return userLogins;
	}
	
	public List<String> getCredentialGroups(){
		List<String> credentialGroups = new ArrayList<String>();
		for (UsrCredential credential : databaseObjects.getCredentials()) {
			credentialGroups.add(credential.getCredential());
		}	
		return credentialGroups;
	}
	
	public List<String> getItinerariesName() {
		List<String> names = new ArrayList<String>();
		for (TtItinerary e : databaseObjects.getBtItineraries())
			names.add(e.getName());
		return names;
	}
	
}
