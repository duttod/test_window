package karrus.client.alarm.alarmsHistory;

 class ClosedAlarmContent {
	
	String openingTimestamp;
	String closingTimestamp;
	String acknowledgementTimestamp;
	String status;
	String type;
	String source;
	Boolean isChecked;

	public ClosedAlarmContent(String openingTimestamp, String closingTimestamp, String acknowledgementTimestamp, String status, String type, String source, Boolean isChecked) {
		this.openingTimestamp = openingTimestamp;
		this.closingTimestamp = closingTimestamp;
		this.acknowledgementTimestamp = acknowledgementTimestamp;
		this.status = status;
		this.type = type;
		this.source = source;
		this.isChecked = isChecked;
	}
	
	public String getOpeningTimestamp() {
		return openingTimestamp;
	}
	
	public String getClosingTimestamp() {
		return closingTimestamp;
	}
	
	public String getAcknowledgementTimestamp() {
		return acknowledgementTimestamp;
	}
	
	public String getStatus() {
		return status;
	}
	
	public String getType() {
		return type;
	}
	
	public String getSource() {
		return source;
	}
	
	public Boolean getIsChecked() {
		return isChecked;
	}
	
	public void setIsChecked(Boolean isChecked) {
		this.isChecked = isChecked;;
	}
}
