package karrus.shared.hibernate;

import net.sf.gilead.pojo.gwt.LightEntity;

/**
 * SynTtColor generated by hbm2java.
 *
 * Custom pojo generation. Generated: 28 janv. 2020 16:50:06.
 *
 * @author Denis Jacquet (denis.jacquet@karrus-its.com)
 *
 */
public class SynTtColor extends LightEntity implements java.io.Serializable {

	private SynTtColorId id;
	private String origin;
	private String destination;

	public SynTtColor() {
	}

	public SynTtColor(SynTtColorId id, String origin, String destination) {
		this.id = id;
		this.origin = origin;
		this.destination = destination;
	}

	public SynTtColorId getId() {
		return this.id;
	}

	public void setId(SynTtColorId id) {
		this.id = id;
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

	private static final long serialVersionUID = 1L;

}
