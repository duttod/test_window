package karrus.shared.hibernate;

import net.sf.gilead.pojo.gwt.LightEntity;

/**
 * V2xAlarm generated by hbm2java.
 *
 * Custom pojo generation. Generated: 28 janv. 2020 16:50:06.
 *
 * @author Denis Jacquet (denis.jacquet@karrus-its.com)
 *
 */
public class V2xAlarm extends LightEntity implements java.io.Serializable {

	private V2xAlarmId id;
	private String type;
	private Integer code;
	private String idEvent;
	private String idVehicule;
	private Double gpsLatitude;
	private Double gpsLongitude;
	private String status;
	private String text;

	public V2xAlarm() {
	}

	public V2xAlarm(V2xAlarmId id) {
		this.id = id;
	}

	public V2xAlarm(V2xAlarmId id, String type, Integer code, String idEvent, String idVehicule, Double gpsLatitude,
			Double gpsLongitude, String status, String text) {
		this.id = id;
		this.type = type;
		this.code = code;
		this.idEvent = idEvent;
		this.idVehicule = idVehicule;
		this.gpsLatitude = gpsLatitude;
		this.gpsLongitude = gpsLongitude;
		this.status = status;
		this.text = text;
	}

	public V2xAlarmId getId() {
		return this.id;
	}

	public void setId(V2xAlarmId id) {
		this.id = id;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getCode() {
		return this.code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getIdEvent() {
		return this.idEvent;
	}

	public void setIdEvent(String idEvent) {
		this.idEvent = idEvent;
	}

	public String getIdVehicule() {
		return this.idVehicule;
	}

	public void setIdVehicule(String idVehicule) {
		this.idVehicule = idVehicule;
	}

	public Double getGpsLatitude() {
		return this.gpsLatitude;
	}

	public void setGpsLatitude(Double gpsLatitude) {
		this.gpsLatitude = gpsLatitude;
	}

	public Double getGpsLongitude() {
		return this.gpsLongitude;
	}

	public void setGpsLongitude(Double gpsLongitude) {
		this.gpsLongitude = gpsLongitude;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	private static final long serialVersionUID = 1L;

}
