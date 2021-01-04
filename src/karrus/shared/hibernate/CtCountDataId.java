package karrus.shared.hibernate;

import java.util.Date;

import net.sf.gilead.pojo.gwt.LightEntity;

/**
 * CtCountDataId generated by hbm2java.
 *
 * Custom pojo generation. Generated: 28 janv. 2020 16:50:06.
 *
 * @author Denis Jacquet (denis.jacquet@karrus-its.com)
 *
 */
public class CtCountDataId extends LightEntity implements java.io.Serializable {

	private Date timestamp;
	private String station;
	private String lane;

	public CtCountDataId() {
	}

	public CtCountDataId(Date timestamp, String station, String lane) {
		this.timestamp = timestamp;
		this.station = station;
		this.lane = lane;
	}

	public Date getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getStation() {
		return this.station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public String getLane() {
		return this.lane;
	}

	public void setLane(String lane) {
		this.lane = lane;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof CtCountDataId))
			return false;
		CtCountDataId castOther = (CtCountDataId) other;

		return ((this.getTimestamp() == castOther.getTimestamp()) || (this.getTimestamp() != null
				&& castOther.getTimestamp() != null && this.getTimestamp().equals(castOther.getTimestamp())))
				&& ((this.getStation() == castOther.getStation()) || (this.getStation() != null
						&& castOther.getStation() != null && this.getStation().equals(castOther.getStation())))
				&& ((this.getLane() == castOther.getLane()) || (this.getLane() != null && castOther.getLane() != null
						&& this.getLane().equals(castOther.getLane())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getTimestamp() == null ? 0 : this.getTimestamp().hashCode());
		result = 37 * result + (getStation() == null ? 0 : this.getStation().hashCode());
		result = 37 * result + (getLane() == null ? 0 : this.getLane().hashCode());
		return result;
	}

	private static final long serialVersionUID = 1L;

}
