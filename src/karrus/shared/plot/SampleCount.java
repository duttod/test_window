package karrus.shared.plot;

import java.util.Date;

import net.sf.gilead.pojo.gwt.LightEntity;

/**
 * Ping generated by hbm2java.
 *
 * Custom pojo generation. Generated: 20 janv. 2014 10:48:49.
 *
 * @author Denis Jacquet (denis.jacquet@karrus-its.com)
 *
 */
public class SampleCount extends LightEntity implements java.io.Serializable {

	private Date timestamp;
	private int validCount;

	public SampleCount() {
	}

	public SampleCount(Date timestamp, int validCount) {
		this.timestamp = timestamp;
		this.validCount = validCount;
	}

	
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public int getValidCount() {
		return validCount;
	}

	public void setValidCount(int validCount) {
		this.validCount = validCount;
	}


	private static final long serialVersionUID = 1L;

}
