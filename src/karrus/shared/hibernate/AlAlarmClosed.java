package karrus.shared.hibernate;

import java.util.Date;

import net.sf.gilead.pojo.gwt.LightEntity;

/**
 * AlAlarmClosed generated by hbm2java.
 *
 * Custom pojo generation. Generated: 28 janv. 2020 16:50:06.
 *
 * @author Denis Jacquet (denis.jacquet@karrus-its.com)
 *
 */
public class AlAlarmClosed extends LightEntity implements java.io.Serializable {

	private AlAlarmClosedId id;
	private Date acknowledgementTimestamp;
	private Date closingTimestamp;
	private String status;

	public AlAlarmClosed() {
	}

	public AlAlarmClosed(AlAlarmClosedId id, String status) {
		this.id = id;
		this.status = status;
	}

	public AlAlarmClosed(AlAlarmClosedId id, Date acknowledgementTimestamp, Date closingTimestamp, String status) {
		this.id = id;
		this.acknowledgementTimestamp = acknowledgementTimestamp;
		this.closingTimestamp = closingTimestamp;
		this.status = status;
	}

	public AlAlarmClosedId getId() {
		return this.id;
	}

	public void setId(AlAlarmClosedId id) {
		this.id = id;
	}

	public Date getAcknowledgementTimestamp() {
		return this.acknowledgementTimestamp;
	}

	public void setAcknowledgementTimestamp(Date acknowledgementTimestamp) {
		this.acknowledgementTimestamp = acknowledgementTimestamp;
	}

	public Date getClosingTimestamp() {
		return this.closingTimestamp;
	}

	public void setClosingTimestamp(Date closingTimestamp) {
		this.closingTimestamp = closingTimestamp;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	private static final long serialVersionUID = 1L;

}
