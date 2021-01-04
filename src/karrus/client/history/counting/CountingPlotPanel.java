package karrus.client.history.counting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karrus.client.FrontalWebApp;
import karrus.client.generic.CustomDialogBox;
import karrus.client.generic.plot.PlotClickMessageFormatter;
import karrus.client.generic.plot.PlotType;
import karrus.client.generic.plot.CountDataTimeSeriesPlot;
import karrus.client.utils.Color;
import karrus.client.utils.DateTime;
import karrus.shared.CountDataAndLanesMap;
import karrus.shared.hibernate.CtCountData;
import karrus.shared.language.Language;
import karrus.shared.plot.TimeSeries;
import ca.nanometrics.gflot.client.event.PlotItem;
import ca.nanometrics.gflot.client.event.PlotPosition;
import ca.nanometrics.gflot.client.jsni.Plot;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Panel containing speed, count and occupancy plots
 * 
 * @author estelle.dumas@karrus-its.com
 */
public class CountingPlotPanel extends Composite {

	private VerticalPanel plotsPanel;
	private int height = (int)FrontalWebApp.getHeightForMainPanel()*27/100;
	private int width = FrontalWebApp.getWidthForMainPanel() - 5;
	private CountDataTimeSeriesPlot countTimeSeriesPlot = new CountDataTimeSeriesPlot(this, "", width, height, PlotType.POINT, true, false, true, true);
	private CountDataTimeSeriesPlot occupancyTimeSeriesPlot = new CountDataTimeSeriesPlot(this, "", width, height, PlotType.POINT, true, false, true, true);
	private CountDataTimeSeriesPlot averageSpeedTimeSeriesPlot = new CountDataTimeSeriesPlot(this, "", width, height, PlotType.POINT, true, false, true, true);
	
	/**
	 * Constructor
	 * @param station The station for which count data are plotted
	 * @param way The way for which count data are plotted
	 * @param startDate Beginning timestamp of the time interval
	 * @param endDate End timestamp of the time interval
	 */
	public CountingPlotPanel(String station,String way, Date startDate, Date endDate) {	
		
		plotsPanel = new VerticalPanel();
		plotsPanel.setSpacing(0);
		this.initWidget(plotsPanel);
	}
    
	/**
	 * Add speed, count, occupancy plots to the plots panel
	 * @param countDataAndLanesMap The list of count data to be plotted and a map of the corresponding lanes ids and lanes human readable ids.
	 */
	public void addCountPlots(CountDataAndLanesMap countDataAndLanesMap){
		
		List<CtCountData> countDataList = countDataAndLanesMap.getCountData();
		Map<String, String> lanesMap = new HashMap<String, String>();
		lanesMap = countDataAndLanesMap.getLanesMap();
		Map<String, String> colorMap = new HashMap<String, String>();
		colorMap.put("0", Color.colorGreen);
		colorMap.put("1", Color.colorSalmon);
		colorMap.put("2", Color.colorTurquoise);
		colorMap.put("3", Color.colorMagenta);
		colorMap.put("4", Color.colorGrey);
		colorMap.put("5", Color.colorDarkOrange);
		colorMap.put("6", Color.colorSampleSizeAll);
		colorMap.put("7", Color.colorBlue);
		colorMap.put("8", Color.colorLime);
		colorMap.put("descendant", Color.colorGreen);
		colorMap.put("montant", Color.colorRed);
		List<String> lanesId= new ArrayList<String>();
		// Map associating a time series label and the corresponding time series
		Map<String, TimeSeries> countTimeSeriesLabelToCountTimeSeries = new HashMap<String, TimeSeries>();
		Map<String, TimeSeries> occupancyTimeSeriesLabelToOccupancyTimeSeries = new HashMap<String, TimeSeries>();
		Map<String, TimeSeries> averageSpeedTimeSeriesLabelToAverageSpeedTimeSeries = new HashMap<String, TimeSeries>();
		String currentLane = countDataList.get(0).getId().getLane();
		List<Date> currentTimestamps = new ArrayList<Date>();
		List<Date> currentAverageSpeedsTimestamps = new ArrayList<Date>();
		List<Double> currentCounts = new ArrayList<Double>();
		List<Double> currentOccupancies = new ArrayList<Double>();
		List<Double> currentAverageSpeeds = new ArrayList<Double>();
		int i = 0;
		for (CtCountData countData : countDataList) {
			if (countData.getId().getLane() == currentLane) {
				currentTimestamps.add(countData.getId().getTimestamp());
				currentCounts.add((double)countData.getCount()*60);
				currentOccupancies.add(countData.getOccupancy());
				if (countData.getSpeed() != -1) {
					currentAverageSpeedsTimestamps.add(countData.getId().getTimestamp());
					currentAverageSpeeds.add(countData.getSpeed());
				}
			}
			else if (countData.getId().getLane() != currentLane) {
				try {
					lanesId.add(currentLane);
					countTimeSeriesLabelToCountTimeSeries.put(currentLane, new TimeSeries(currentTimestamps, currentCounts));
					occupancyTimeSeriesLabelToOccupancyTimeSeries.put(currentLane, new TimeSeries(currentTimestamps, currentOccupancies));
					averageSpeedTimeSeriesLabelToAverageSpeedTimeSeries.put(currentLane, new TimeSeries(currentAverageSpeedsTimestamps, currentAverageSpeeds));
				} catch (Exception e) {
					Log.error(e.getMessage());
					new CustomDialogBox(e.getMessage(), Language.okString);
				}	
				currentLane = countData.getId().getLane();
				currentTimestamps.clear();
				currentAverageSpeedsTimestamps.clear();
				currentCounts.clear();
				currentOccupancies.clear();
				currentAverageSpeeds.clear();
				currentTimestamps.add(countData.getId().getTimestamp());
				currentCounts.add((double)countData.getCount()*60);
				currentOccupancies.add(countData.getOccupancy());
				if (countData.getSpeed() != -1) {
					currentAverageSpeedsTimestamps.add(countData.getId().getTimestamp());
					currentAverageSpeeds.add(countData.getSpeed());
				}
			}
			if (i == countDataList.size() -1) {
				try {
					lanesId.add(currentLane);
					countTimeSeriesLabelToCountTimeSeries.put(currentLane, new TimeSeries(currentTimestamps, currentCounts));
					occupancyTimeSeriesLabelToOccupancyTimeSeries.put(currentLane, new TimeSeries(currentTimestamps, currentOccupancies));
					averageSpeedTimeSeriesLabelToAverageSpeedTimeSeries.put(currentLane, new TimeSeries(currentAverageSpeedsTimestamps, currentAverageSpeeds));
				} catch (Exception e) {
					Log.error(e.getMessage());
					new CustomDialogBox(e.getMessage(), Language.okString);
				}	
			}
			i++;
		}
		Collections.sort(lanesId);
		i = 0;
		PlotClickMessageFormatter averageSpeedPlotClickMessageFormatter = new PlotClickMessageFormatter() {
			@Override
			public String getMessage(Plot plot, PlotPosition position, PlotItem item) {
				Date d = new Date((long)item.getDataPoint().getX());
				int offset = 0;
				TimeZone tz = TimeZone.createTimeZone(offset);
				String x = DateTime.frenchDateTimeFormat.format(d, tz);
				String y = NumberFormat.getFormat("###").format(item.getDataPoint().getY());
				String message = x + " - " + y + " km/h" ;
				return message;
			}
		};
		PlotClickMessageFormatter countPlotClickMessageFormatter = new PlotClickMessageFormatter() {
			@Override
			public String getMessage(Plot plot, PlotPosition position, PlotItem item) {
				Date d = new Date((long)item.getDataPoint().getX());
				int offset = 0;
				TimeZone tz = TimeZone.createTimeZone(offset);
				String x = DateTime.frenchDateTimeFormat.format(d, tz);
				String y = NumberFormat.getFormat("###").format(item.getDataPoint().getY());
				String message = x + " - " + y + " veh/h" ;
				return message;
			}
		};
		PlotClickMessageFormatter occupancyPlotClickMessageFormatter = new PlotClickMessageFormatter() {
			@Override
			public String getMessage(Plot plot, PlotPosition position, PlotItem item) {
				Date d = new Date((long)item.getDataPoint().getX());
				int offset = 0;
				TimeZone tz = TimeZone.createTimeZone(offset);
				String x = DateTime.frenchDateTimeFormat.format(d, tz);
				String y = NumberFormat.getFormat("##.#").format(item.getDataPoint().getY());
				String message = x + " - " + y + " %" ;
				return message;
			}
		};
		for (String label : lanesId) {
			averageSpeedTimeSeriesPlot.addTimeSeries(averageSpeedTimeSeriesLabelToAverageSpeedTimeSeries.get(label), lanesMap.get(label), colorMap.get(label), Language.averageSpeedYLabelString, (double)0, 160., "km/h", averageSpeedPlotClickMessageFormatter);
			countTimeSeriesPlot.addTimeSeries(countTimeSeriesLabelToCountTimeSeries.get(label), lanesMap.get(label), colorMap.get(label), Language.countYLabelString, (double)0, 2000., "veh/h", countPlotClickMessageFormatter);
			occupancyTimeSeriesPlot.addTimeSeries(occupancyTimeSeriesLabelToOccupancyTimeSeries.get(label), lanesMap.get(label), colorMap.get(label), Language.occupancyYLabelString, (double)0, 105., "%", occupancyPlotClickMessageFormatter);
			i++;
		}
		averageSpeedTimeSeriesPlot.plot();
		plotsPanel.add(averageSpeedTimeSeriesPlot);
		countTimeSeriesPlot.plot();
		plotsPanel.add(countTimeSeriesPlot);
		occupancyTimeSeriesPlot.plot();
		plotsPanel.add(occupancyTimeSeriesPlot);
	}
	
//	public void addAggregatedCountPlots(
//		List<CtSsyAggregatedCountLane> aggregatedCountDataList) {
//		List<Date> timestamps = new ArrayList<Date>();
//		List<Double> counts = new ArrayList<Double>();
//		List<Double> speeds = new ArrayList<Double>();
//		for (CtSsyAggregatedCountLane aggregatedData : aggregatedCountDataList) {
//			timestamps.add(aggregatedData.getTimestamp());
//			counts.add((double) aggregatedData.getCount());
//			speeds.add((double) aggregatedData.getSpeedMedian());
//		}
//		try {
//			averageSpeedTimeSeriesPlot.addTimeSeries(new TimeSeries(timestamps,
//					speeds), "", Color.colorBlue,
//					Language.averageSpeedYLabelString, (double) 0, 160.);
//			countTimeSeriesPlot.addTimeSeries(
//					new TimeSeries(timestamps, counts), "", Color.colorBlue,
//					Language.countYLabelString, (double) 0, 2000.);
//			averageSpeedTimeSeriesPlot.addTimeSeries(new TimeSeries(timestamps,
//					speeds), "", Color.colorSampleSizeValid,
//					Language.averageSpeedYLabelString, (double) 0, 160.,
//					PlotType.BAR, false);
//			countTimeSeriesPlot.addTimeSeries(
//					new TimeSeries(timestamps, counts), "",
//					Color.colorSampleSizeValid,
//					Language.averageSpeedYLabelString, (double) 0, 160.,
//					PlotType.BAR, false);
//		} catch (Exception e) {
//			Log.debug("Unable to create new times series in plot");
//		}
//		countTimeSeriesPlot.getPlot().setSize(width + "px",
//				3 * height / 2 + "px");
//		countTimeSeriesPlot.plot();
//		plotsPanel.add(countTimeSeriesPlot);
//		averageSpeedTimeSeriesPlot.getPlot().setSize(width + "px",
//				3 * height / 2 + "px");
//		averageSpeedTimeSeriesPlot.plot();
//		plotsPanel.add(averageSpeedTimeSeriesPlot);
//	}
	
	public void redrawPlots (double xFrom, double xTo) {
		countTimeSeriesPlot.getPlot().getPlotOptions().getXAxisOptions().setMinimum(xFrom);
		countTimeSeriesPlot.getPlot().getPlotOptions().getXAxisOptions().setMaximum(xTo);
		countTimeSeriesPlot.getPlot().redraw();
		averageSpeedTimeSeriesPlot.getPlot().getPlotOptions().getXAxisOptions().setMinimum(xFrom);
		averageSpeedTimeSeriesPlot.getPlot().getPlotOptions().getXAxisOptions().setMaximum(xTo);
		averageSpeedTimeSeriesPlot.getPlot().redraw();
		occupancyTimeSeriesPlot.getPlot().getPlotOptions().getXAxisOptions().setMinimum(xFrom);
		occupancyTimeSeriesPlot.getPlot().getPlotOptions().getXAxisOptions().setMaximum(xTo);
		occupancyTimeSeriesPlot.getPlot().redraw();
	}
}
