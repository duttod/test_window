package karrus.shared.hibernate;

import net.sf.gilead.pojo.gwt.LightEntity;

/**
 * TtBtMacRt generated by hbm2java.
 *
 * Custom pojo generation. Generated: 28 janv. 2020 16:50:06.
 *
 * @author Denis Jacquet (denis.jacquet@karrus-its.com)
 *
 */
public class TtBtMacRt extends LightEntity implements java.io.Serializable {

	private TtBtMacRtId id;
	private String class_;
	private String station;

	public TtBtMacRt() {
	}

	public TtBtMacRt(TtBtMacRtId id, String station) {
		this.id = id;
		this.station = station;
	}

	public TtBtMacRt(TtBtMacRtId id, String class_, String station) {
		this.id = id;
		this.class_ = class_;
		this.station = station;
	}

	public TtBtMacRtId getId() {
		return this.id;
	}

	public void setId(TtBtMacRtId id) {
		this.id = id;
	}

	public String getClass_() {
		return this.class_;
	}

	public void setClass_(String class_) {
		this.class_ = class_;
	}

	public String getStation() {
		return this.station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	private static final long serialVersionUID = 1L;

}
