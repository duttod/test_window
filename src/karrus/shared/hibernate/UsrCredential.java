package karrus.shared.hibernate;

import net.sf.gilead.pojo.gwt.LightEntity;

/**
 * UsrCredential generated by hbm2java.
 *
 * Custom pojo generation. Generated: 28 janv. 2020 16:50:06.
 *
 * @author Denis Jacquet (denis.jacquet@karrus-its.com)
 *
 */
public class UsrCredential extends LightEntity implements java.io.Serializable {

	private String credential;
	private String station;
	private String synoptic;
	private String alarms;
	private String rdtCtData;
	private String rdtCtStations;
	private String rdtTtData;
	private String rdtTtBoxes;
	private String rdtTtItineraries;
	private String system;

	public UsrCredential() {
	}

	public UsrCredential(String credential) {
		this.credential = credential;
	}

	public UsrCredential(String credential, String station, String synoptic, String alarms, String rdtCtData,
			String rdtCtStations, String rdtTtData, String rdtTtBoxes, String rdtTtItineraries, String system) {
		this.credential = credential;
		this.station = station;
		this.synoptic = synoptic;
		this.alarms = alarms;
		this.rdtCtData = rdtCtData;
		this.rdtCtStations = rdtCtStations;
		this.rdtTtData = rdtTtData;
		this.rdtTtBoxes = rdtTtBoxes;
		this.rdtTtItineraries = rdtTtItineraries;
		this.system = system;
	}

	public String getCredential() {
		return this.credential;
	}

	public void setCredential(String credential) {
		this.credential = credential;
	}

	public String getStation() {
		return this.station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public String getSynoptic() {
		return this.synoptic;
	}

	public void setSynoptic(String synoptic) {
		this.synoptic = synoptic;
	}

	public String getAlarms() {
		return this.alarms;
	}

	public void setAlarms(String alarms) {
		this.alarms = alarms;
	}

	public String getRdtCtData() {
		return this.rdtCtData;
	}

	public void setRdtCtData(String rdtCtData) {
		this.rdtCtData = rdtCtData;
	}

	public String getRdtCtStations() {
		return this.rdtCtStations;
	}

	public void setRdtCtStations(String rdtCtStations) {
		this.rdtCtStations = rdtCtStations;
	}

	public String getRdtTtData() {
		return this.rdtTtData;
	}

	public void setRdtTtData(String rdtTtData) {
		this.rdtTtData = rdtTtData;
	}

	public String getRdtTtBoxes() {
		return this.rdtTtBoxes;
	}

	public void setRdtTtBoxes(String rdtTtBoxes) {
		this.rdtTtBoxes = rdtTtBoxes;
	}

	public String getRdtTtItineraries() {
		return this.rdtTtItineraries;
	}

	public void setRdtTtItineraries(String rdtTtItineraries) {
		this.rdtTtItineraries = rdtTtItineraries;
	}

	public String getSystem() {
		return this.system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	private static final long serialVersionUID = 1L;

}
