package karrus.shared.hibernate;

import net.sf.gilead.pojo.gwt.LightEntity;

/**
 * SynTtDisplayId generated by hbm2java.
 *
 * Custom pojo generation. Generated: 28 janv. 2020 16:50:06.
 *
 * @author Denis Jacquet (denis.jacquet@karrus-its.com)
 *
 */
public class SynTtDisplayId extends LightEntity implements java.io.Serializable {

	private String synopticId;
	private String itemId;

	public SynTtDisplayId() {
	}

	public SynTtDisplayId(String synopticId, String itemId) {
		this.synopticId = synopticId;
		this.itemId = itemId;
	}

	public String getSynopticId() {
		return this.synopticId;
	}

	public void setSynopticId(String synopticId) {
		this.synopticId = synopticId;
	}

	public String getItemId() {
		return this.itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof SynTtDisplayId))
			return false;
		SynTtDisplayId castOther = (SynTtDisplayId) other;

		return ((this.getSynopticId() == castOther.getSynopticId()) || (this.getSynopticId() != null
				&& castOther.getSynopticId() != null && this.getSynopticId().equals(castOther.getSynopticId())))
				&& ((this.getItemId() == castOther.getItemId()) || (this.getItemId() != null
						&& castOther.getItemId() != null && this.getItemId().equals(castOther.getItemId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getSynopticId() == null ? 0 : this.getSynopticId().hashCode());
		result = 37 * result + (getItemId() == null ? 0 : this.getItemId().hashCode());
		return result;
	}

	private static final long serialVersionUID = 1L;

}
