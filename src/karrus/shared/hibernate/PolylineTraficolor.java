package karrus.shared.hibernate;

import net.sf.gilead.pojo.gwt.LightEntity;

/**
 * PolylineTraficolor generated by hbm2java.
 *
 * Custom pojo generation. Generated: 16 d�c. 2019 10:13:37.
 *
 * @author Denis Jacquet (denis.jacquet@karrus-its.com)
 *
 */
public class PolylineTraficolor extends LightEntity implements java.io.Serializable {

	private PolylineTraficolorId id;
	private String listOfLatlngs;

	public PolylineTraficolor() {
	}

	public PolylineTraficolor(PolylineTraficolorId id, String listOfLatlngs) {
		this.id = id;
		this.listOfLatlngs = listOfLatlngs;
	}

	public PolylineTraficolorId getId() {
		return this.id;
	}

	public void setId(PolylineTraficolorId id) {
		this.id = id;
	}

	public String getListOfLatlngs() {
		return this.listOfLatlngs;
	}

	public void setListOfLatlngs(String listOfLatlngs) {
		this.listOfLatlngs = listOfLatlngs;
	}

	private static final long serialVersionUID = 1L;

}