package karrus.client.equipments.station;

 class StationContent {
	
	String id;// = station.getId();
	String serial;// = station.getSerial();
	String host;// = station.getHost();
	String latitude;// = station.getLatitude() == null ? "" : Double.toString(station.getLatitude());
	String longitude;// = station.getLongitude() == null ? "" : Double.toString(station.getLongitude());
	String road;// = station.getRoad();
	String position;// = station.getPosition();
	String cycleCountingSec;// = station.getCycleCountingSec() == null ? "" : Integer.toString(station.getCycleCountingSec());
	String cycleTravelTimeSec;// = station.getCycleTravelTimeSec() == null ? "" : Integer.toString(station.getCycleTravelTimeSec());
	String cycleWeatherSec;// = station.getCycleWeatherSec() == null ? "" : Integer.toString(station.getCycleWeatherSec());
	String cycleV2XSec;// = station.getCycleV2xSec() == null ? "" : Integer.toString(station.getCycleV2xSec());

	public StationContent(String id, String serial, String host, String latitude, String longitude, String road, String position,
			String cycleCountingSec, String cycleTravelTimeSec, String cycleWeatherSec, String cycleV2XSec) {
		this.id = id;
		this.serial = serial;
		this.host = host;
		this.latitude = latitude;
		this.longitude = longitude;
		this.road = road;
		this.position = position;
		this.cycleCountingSec = cycleCountingSec;
		this.cycleTravelTimeSec = cycleTravelTimeSec;
		this.cycleWeatherSec = cycleWeatherSec;
		this.cycleV2XSec = cycleV2XSec;
	}
	
	public String getId() {
		return id;
	}
	
	public String getSerial() {
		return serial;
	}
	
	public String getHost() {
		return host;
	}
	
	public String getLatitude() {
		return latitude;
	}
	
	public String getLongitude() {
		return longitude;
	}
	
	public String getRoad() {
		return road;
	}
	
	public String getPosition() {
		return position;
	}
	
	public String getCycleCountingSec() {
		return cycleCountingSec;
	}
	
	public String getCycleTravelTimeSec() {
		return cycleTravelTimeSec;
	}
	
	public String getCycleV2XSec() {
		return cycleV2XSec;
	}
	
	public String getCycleWeatherSec() {
		return cycleWeatherSec;
	}
	
}
