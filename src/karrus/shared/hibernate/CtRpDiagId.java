package karrus.shared.hibernate;

import java.util.Date;

import net.sf.gilead.pojo.gwt.LightEntity;

/**
 * CtRpDiagId generated by hbm2java.
 *
 * Custom pojo generation. Generated: 28 janv. 2020 16:50:06.
 *
 * @author Denis Jacquet (denis.jacquet@karrus-its.com)
 *
 */
public class CtRpDiagId extends LightEntity implements java.io.Serializable {

	private Date timestamp;
	private String id;

	public CtRpDiagId() {
	}

	public CtRpDiagId(Date timestamp, String id) {
		this.timestamp = timestamp;
		this.id = id;
	}

	public Date getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof CtRpDiagId))
			return false;
		CtRpDiagId castOther = (CtRpDiagId) other;

		return ((this.getTimestamp() == castOther.getTimestamp()) || (this.getTimestamp() != null
				&& castOther.getTimestamp() != null && this.getTimestamp().equals(castOther.getTimestamp())))
				&& ((this.getId() == castOther.getId()) || (this.getId() != null && castOther.getId() != null
						&& this.getId().equals(castOther.getId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getTimestamp() == null ? 0 : this.getTimestamp().hashCode());
		result = 37 * result + (getId() == null ? 0 : this.getId().hashCode());
		return result;
	}

	private static final long serialVersionUID = 1L;

}
