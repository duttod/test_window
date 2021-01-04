package karrus.shared.hibernate;

import net.sf.gilead.pojo.gwt.LightEntity;

/**
 * SynAlarm generated by hbm2java.
 *
 * Custom pojo generation. Generated: 28 janv. 2020 16:50:06.
 *
 * @author Denis Jacquet (denis.jacquet@karrus-its.com)
 *
 */
public class SynAlarm extends LightEntity implements java.io.Serializable {

	private SynAlarmId id;
	private String type;
	private String source;

	public SynAlarm() {
	}

	public SynAlarm(SynAlarmId id, String type, String source) {
		this.id = id;
		this.type = type;
		this.source = source;
	}

	public SynAlarmId getId() {
		return this.id;
	}

	public void setId(SynAlarmId id) {
		this.id = id;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	private static final long serialVersionUID = 1L;

}