package karrus.shared.hibernate;

import net.sf.gilead.pojo.gwt.LightEntity;

/**
 * LfPolylineId generated by hbm2java.
 *
 * Custom pojo generation. Generated: 28 janv. 2020 16:50:06.
 *
 * @author Denis Jacquet (denis.jacquet@karrus-its.com)
 *
 */
public class LfPolylineId extends LightEntity implements java.io.Serializable {

	private String origin;
	private String destination;
	private String zoomLevel;

	public LfPolylineId() {
	}

	public LfPolylineId(String origin, String destination, String zoomLevel) {
		this.origin = origin;
		this.destination = destination;
		this.zoomLevel = zoomLevel;
	}

	public String getOrigin() {
		return this.origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return this.destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getZoomLevel() {
		return this.zoomLevel;
	}

	public void setZoomLevel(String zoomLevel) {
		this.zoomLevel = zoomLevel;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof LfPolylineId))
			return false;
		LfPolylineId castOther = (LfPolylineId) other;

		return ((this.getOrigin() == castOther.getOrigin()) || (this.getOrigin() != null
				&& castOther.getOrigin() != null && this.getOrigin().equals(castOther.getOrigin())))
				&& ((this.getDestination() == castOther.getDestination())
						|| (this.getDestination() != null && castOther.getDestination() != null
								&& this.getDestination().equals(castOther.getDestination())))
				&& ((this.getZoomLevel() == castOther.getZoomLevel()) || (this.getZoomLevel() != null
						&& castOther.getZoomLevel() != null && this.getZoomLevel().equals(castOther.getZoomLevel())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getOrigin() == null ? 0 : this.getOrigin().hashCode());
		result = 37 * result + (getDestination() == null ? 0 : this.getDestination().hashCode());
		result = 37 * result + (getZoomLevel() == null ? 0 : this.getZoomLevel().hashCode());
		return result;
	}

	private static final long serialVersionUID = 1L;

}