package karrus.shared;

import java.util.ArrayList;
import java.util.List;

import net.sf.gilead.pojo.gwt.LightEntity;
import karrus.shared.hibernate.SynAlarm;
import karrus.shared.hibernate.SynRdtDisplay;
import karrus.shared.hibernate.SynTtColor;
import karrus.shared.hibernate.SynRdtColor;
import karrus.shared.hibernate.SynTtDisplay;
import karrus.shared.hibernate.SynWeatherDisplay;
import karrus.shared.hibernate.SynWeatherStation;
import karrus.shared.hibernate.TtItinerary;
import karrus.shared.hibernate.CtLane;
import karrus.shared.hibernate.UsrCredential;
import karrus.shared.hibernate.UsrUser;
import karrus.shared.hibernate.RsuStation;

public class DatabaseObjects extends LightEntity implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List<UsrUser> users = new ArrayList<UsrUser>();
	private List<UsrCredential> credentials = new ArrayList<UsrCredential>();
	private List<RsuStation> stations = new ArrayList<RsuStation>();
	private List<CtLane> lanes = new ArrayList<CtLane>();
	private List<TtItinerary> btItineraries = new ArrayList<TtItinerary>();
	private List<SynAlarm> synAlarms = new ArrayList<SynAlarm>();
	private List<SynRdtColor> synRdtColors = new ArrayList<SynRdtColor>();
	private List<SynRdtDisplay> synRdtDisplays = new ArrayList<SynRdtDisplay>();
	private List<SynTtColor> synTtColors = new ArrayList<SynTtColor>();
	private List<SynTtDisplay> synTtDisplays = new ArrayList<SynTtDisplay>();
	private List<SynWeatherStation> synWeatherStations = new ArrayList<SynWeatherStation>();
	private List<SynWeatherDisplay> synWeatherDisplays = new ArrayList<SynWeatherDisplay>();
	
	public DatabaseObjects(){}
	
	public DatabaseObjects(List<UsrUser> users, List<UsrCredential> credentials, List<RsuStation> stations, List<CtLane> lanes, 
			               List<TtItinerary> btItineraries, List<SynAlarm> synAlarms, List<SynRdtColor> synRdtColors, List<SynRdtDisplay> synRdtDisplays, List<SynTtColor> synTtColors, 
			               List<SynTtDisplay> synTtDisplays, List<SynWeatherStation> synWeatherStations, List<SynWeatherDisplay> synWeatherDisplays) {
		this.users = users;
		this.credentials = credentials;
		this.stations = stations;
		this.lanes = lanes;
		this.btItineraries = btItineraries;
		this.synAlarms = synAlarms;
		this.synRdtColors = synRdtColors;
		this.synRdtDisplays = synRdtDisplays;
		this.synTtColors = synTtColors;
		this.synTtDisplays = synTtDisplays;
		this.synWeatherStations = synWeatherStations;
		this.synWeatherDisplays = synWeatherDisplays;
	}

	public List<UsrUser> getUsers() {
		return users;
	}

	public void setUsers(List<UsrUser> users) {
		this.users = users;
	}
	
	public List<UsrCredential> getCredentials() {
		return credentials;
	}

	public void setCredentials(List<UsrCredential> credentials) {
		this.credentials = credentials;
	}
	
	public List<RsuStation> getStations() {
		return stations;
	}
	public void setStations(List<RsuStation> stations) {
		this.stations = stations;
	}
	
	public List<CtLane> getLanes() {
		return lanes;
	}
	public void setLanes(List<CtLane> lanes) {
		this.lanes = lanes;
	}
	
	public List<TtItinerary> getBtItineraries() {
		return btItineraries;
	}
	public void setBtItineraries(List<TtItinerary> btItineraries) {
		this.btItineraries = btItineraries;
	}
	
	public List<SynAlarm> getSynAlarms() {
		return synAlarms;
	}
	public void setSynAlarms(List<SynAlarm> synAlarms) {
		this.synAlarms = synAlarms;
	}
	
	public List<SynRdtColor> getSynRdtColors() {
		return synRdtColors;
	}
	public void setSynRdtColors(List<SynRdtColor> synRdtColors) {
		this.synRdtColors = synRdtColors;
	}
	
	public List<SynRdtDisplay> getSynRdtDisplays() {
		return synRdtDisplays;
	}
	public void setSynRdtDisplays(List<SynRdtDisplay> synRdtDisplays) {
		this.synRdtDisplays = synRdtDisplays;
	}
	
	public List<SynTtColor> getSynTtColors() {
		return synTtColors;
	}
	public void setSynTtColors(List<SynTtColor> synTtColors) {
		this.synTtColors = synTtColors;
	}
	
	public List<SynTtDisplay> getSynTtDisplays() {
		return synTtDisplays;
	}
	public void setSynTtDisplays(List<SynTtDisplay> synTtDisplays) {
		this.synTtDisplays = synTtDisplays;
	}
	
	public List<SynWeatherDisplay> getSynWeatherDisplays() {
		return synWeatherDisplays;
	}
	public void setSynWeatherDisplays(List<SynWeatherDisplay> synWeatherDisplays) {
		this.synWeatherDisplays = synWeatherDisplays;
	}
	
	public List<SynWeatherStation> getSynWeatherStations() {
		return synWeatherStations;
	}
	public void setSynWeatherStations(List<SynWeatherStation> synWeatherStations) {
		this.synWeatherStations = synWeatherStations;
	}
}


