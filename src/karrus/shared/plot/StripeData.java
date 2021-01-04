package karrus.shared.plot;

import java.io.Serializable;
import java.util.Date;

public class StripeData implements Serializable {
	private static final long serialVersionUID = 1L;

	
	private Date date;
	private double upValue;
	private double downValue;
	
	public StripeData(){}
	
	public StripeData(Date date, double upValue, double downValue){
		this.date = date;
		this.upValue = upValue;
		this.downValue = downValue;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getUpValue() {
		return upValue;
	}

	public void setUpValue(double upValue) {
		this.upValue = upValue;
	}

	public double getDownValue() {
		return downValue;
	}

	public void setDownValue(double downValue) {
		this.downValue = downValue;
	}
	
	
}
