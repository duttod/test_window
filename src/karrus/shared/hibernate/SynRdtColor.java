package karrus.shared.hibernate;

import net.sf.gilead.pojo.gwt.LightEntity;

/**
 * SynRdtColor generated by hbm2java.
 *
 * Custom pojo generation. Generated: 28 janv. 2020 16:50:06.
 *
 * @author Denis Jacquet (denis.jacquet@karrus-its.com)
 *
 */
public class SynRdtColor extends LightEntity implements java.io.Serializable {

	private SynRdtColorId id;
	private String station;
	private String lane;

	public SynRdtColor() {
	}

	public SynRdtColor(SynRdtColorId id, String station, String lane) {
		this.id = id;
		this.station = station;
		this.lane = lane;
	}

	public SynRdtColorId getId() {
		return this.id;
	}

	public void setId(SynRdtColorId id) {
		this.id = id;
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

	private static final long serialVersionUID = 1L;

}
