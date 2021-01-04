package karrus.client.generic.plot;

import java.util.Date;
import java.util.List;

import karrus.shared.plot.TimeSeries;

import com.google.gwt.user.client.ui.Widget;

public interface TimeSeriesPlottable {
	
	/**
	 * Sets the widget as title.
	 * @param widget
	 */
	public void setTitle(Widget widget);
	// add time series
	/**
	 * Adds a TimeSeries (made of dates and values) to the plot with the selected color.
	 * @param timeSerie
	 * @param title
	 * @param color
	 */
	public void addTimeSeries(TimeSeries timeSerie, String title, String color);
	public void addTimeSeries(TimeSeries timeSerie, String title, String color, String yLabel, double minValue, double maxValue, String dataUnit, PlotClickMessageFormatter plotClickMessageFormatter);
	public void addTimeSeries(List<Date> dates, List<Double> values, String title, String color, String yLabel, double minValue, double maxValues) throws Exception;
	// remove time series
	public void removeTimeSeries(TimeSeries timeSeries);
	// filtered values
	public void addFilteredValue(Double filteredValue);
	public void removeFilteredValue(Double filteredValue);
	// remove values
	public void removeValuesBefore(Date firstDate);
	public void removeValuesAfter(Date lastDate);
	// get dates
	public Date getFirstDateInTimeSeries();
	public Date getLastDateInTimeSeries();
	public void refresh();
	public void clear();
}
