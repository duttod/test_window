package karrus.shared.plot;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DoubleTimeSeries implements Serializable {
	private static final long serialVersionUID = -1L;


	private List<StripeData> data;

	public DoubleTimeSeries() {};

	public DoubleTimeSeries(List<StripeData> stripeData) throws Exception{
		this.data = stripeData;
	}
	
	public DoubleTimeSeries(List<Date> dates, List<Double> upValues, List<Double> downValues) throws Exception{
		if (dates.size()!=upValues.size() && dates.size()!=downValues.size())
			throw new Exception("Les listes n'ont pas la même taille...");
		this.data = new ArrayList<StripeData>();
		for (int i=0; i<dates.size(); i++)
			this.data.add(new StripeData(dates.get(i), upValues.get(i), downValues.get(i)));
	}
	

	public List<Date> getDates(){
		List<Date> dates = new ArrayList<Date>();
		for (StripeData d : data)
			dates.add(d.getDate());
		return dates;
	}

	
	public List<StripeData> getData(){
		return data;
	}
	
	public int size(){
		return data.size();
	}

	public Date getFirstDate() {
		if (data==null)
			return null;
		if (data.size()==0)
			return null;
		Date d = data.get(0).getDate();
		for (StripeData s : data) {
			Date currentDate = s.getDate();
			if (currentDate.before(d))
				d = currentDate;
		}
		return d;
	}

	public Date getLastDate() {
		if (data==null)
			return null;
		if (data.size()==0)
			return null;
		Date d = data.get(0).getDate();
		for (StripeData s : data) {
			Date currentDate = s.getDate();
			if (currentDate.after(d))
				d = currentDate;
		}
		return d;
	}


	public String toString(){
		return data.toString();
	}
}
