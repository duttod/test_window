package karrus.client.equipments.itinerary;

 class ItineraryContent {
	
	String origin;
	String destination;
	String name;
	String nominalTravelTime;
	String maxTravelTime;
	String levelOfServiceOne;
	String levelOfServiceTwo;
	String scale;

	public ItineraryContent(String origin, String destination, String name, String nominalTravelTime, String maxTravelTime, String levelOfServiceOne, String levelOfServiceTwo, String scale) {
		this.origin = origin;
		this.destination = destination;
		this.name = name;
		this.nominalTravelTime = nominalTravelTime;
		this.maxTravelTime = maxTravelTime;
		this.levelOfServiceOne = levelOfServiceOne;
		this.levelOfServiceTwo = levelOfServiceTwo;
		this.scale = scale;
	}
	
	public String getOrigin() {
		return origin;
	}
	
	public String getDestination() {
		return destination;
	}
	
	public String getName() {
		return name;
	}
	
	public String getNominalTravelTime() {
		return nominalTravelTime;
	}
	
	public String getMaxTravelTime() {
		return maxTravelTime;
	}
	
	public String getLevelOfServiceThreshold1() {
		return levelOfServiceOne;
	}
	
	public String getLevelOfServiceThreshold2() {
		return levelOfServiceTwo;
	}	
	
	public String getScale() {
		return scale;
	}	
}
