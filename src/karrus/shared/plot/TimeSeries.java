package karrus.shared.plot;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class TimeSeries implements Serializable {
	
	private static final long serialVersionUID = -1L;
	private Map<Date, Double> dates2values;
	
	public TimeSeries() {};
	
	public TimeSeries(List<Date> dates, List<Double> values) throws Exception{
		if (dates.size()!=values.size()) {
			throw new Exception("ERREUR : dates et values n'ont pas la mm taille..."); // TODO : traiter l'erreur correctement
		}	
		Map<Date, Double> map = new TreeMap<Date, Double>();
		for (int i=0; i<dates.size(); i++) {
			if (dates.get(i) != null && values.get(i) != null) {
				map.put(dates.get(i), values.get(i));	
			}
		}	
		this.dates2values = map;
	}
	
	public TimeSeries(Map<Date, Double> mapDates2values) {
		super();
		dates2values = new TreeMap<Date, Double>();
		// on ordonne mapDates2values si ce n'est pas le cas
		Map<Date, Double> sortedMap = new TreeMap<Date, Double>();
		for (Date d : mapDates2values.keySet()) {
			sortedMap.put(d, mapDates2values.get(d));
		}	
		this.dates2values.putAll(sortedMap);
	}
	
	public void setTimesSeries(TimeSeries timeSeries){
		dates2values.clear();
		addValues(timeSeries);
	}
	
	public void removeValuesBefore(Date firstDate) {
		for (Iterator<Date> iterator = dates2values.keySet().iterator(); iterator.hasNext();) {
			Date date = iterator.next();
			if (date.before(firstDate)) {
				iterator.remove();
			}	
		}
	}
	
	public void removeValuesAfter(Date lastDate) {
		for (Iterator<Date> iterator = dates2values.keySet().iterator(); iterator.hasNext();) {
			Date date = iterator.next();
			if (date.after(lastDate)) { 
				iterator.remove();
			}	
		}
	}

	public void addValues(List<Date> dates, List<Double> values) throws Exception {
		if (dates.size()!=values.size()) {
			throw new Exception("ERREUR : dates et values n'ont pas la mm taille..."); // TODO : traiter l'erreur correctement
		}	
		for (int i = 0; i < dates.size(); i++)  {
			dates2values.put(dates.get(i), values.get(i));
		}	
	}
	
	public void addValues(TimeSeries timeSerie) {
		dates2values.putAll(timeSerie.dates2values);
	}

	public Date getFirstDate() {
		if (dates2values==null) {
			return null;
		}	
		if (dates2values.keySet().size()==0) {
			return null;
		}	
		Date date = dates2values.keySet().iterator().next();
		for (Date currentDate : dates2values.keySet()) {
			if (currentDate.before(date)) {
				date = currentDate;
			}	
		}
		return date;
	}
	
	public Date getLastDate() {
		if (dates2values==null) {
			return null;
		}	
		if (dates2values.keySet().size()==0) {
			return null;
		}	
		Date date = dates2values.keySet().iterator().next();
		for (Date currentDate : dates2values.keySet()) {
			if (currentDate.after(date)) {
				date = currentDate;
			}	
		}
		return date;
	}
	
	public String toString(){
		return dates2values.toString();
	}
	
	public int size(){
		return dates2values.size();
	}
	
	public Set<Date> keySet(){
		return dates2values.keySet();
	}
	
	public double get(Date date){
		return dates2values.get(date);
	}
}
