package karrus.shared.hibernate;

import net.sf.gilead.pojo.gwt.LightEntity;

/**
 * LfConf generated by hbm2java.
 *
 * Custom pojo generation. Generated: 28 janv. 2020 16:50:06.
 *
 * @author Denis Jacquet (denis.jacquet@karrus-its.com)
 *
 */
public class LfConf extends LightEntity implements java.io.Serializable {

	private String var;
	private String value;

	public LfConf() {
	}

	public LfConf(String var) {
		this.var = var;
	}

	public LfConf(String var, String value) {
		this.var = var;
		this.value = value;
	}

	public String getVar() {
		return this.var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	private static final long serialVersionUID = 1L;

}