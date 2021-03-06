package karrus.shared.hibernate;

import net.sf.gilead.pojo.gwt.LightEntity;

/**
 * PopupInfoId generated by hbm2java.
 *
 * Custom pojo generation. Generated: 28 janv. 2020 16:50:06.
 *
 * @author Denis Jacquet (denis.jacquet@karrus-its.com)
 *
 */
public class PopupInfoId extends LightEntity implements java.io.Serializable {

	private String id;
	private String service;

	public PopupInfoId() {
	}

	public PopupInfoId(String id, String service) {
		this.id = id;
		this.service = service;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getService() {
		return this.service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof PopupInfoId))
			return false;
		PopupInfoId castOther = (PopupInfoId) other;

		return ((this.getId() == castOther.getId())
				|| (this.getId() != null && castOther.getId() != null && this.getId().equals(castOther.getId())))
				&& ((this.getService() == castOther.getService()) || (this.getService() != null
						&& castOther.getService() != null && this.getService().equals(castOther.getService())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getId() == null ? 0 : this.getId().hashCode());
		result = 37 * result + (getService() == null ? 0 : this.getService().hashCode());
		return result;
	}

	private static final long serialVersionUID = 1L;

}
