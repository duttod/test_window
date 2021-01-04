package karrus.client.history.traveltime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import karrus.client.FrontalWebApp;
import karrus.client.appearance.LayoutInfo;
import karrus.client.generic.CustomDialogBox;
import karrus.client.generic.plot.PlotClickMessageFormatter;
import karrus.client.generic.plot.PlotType;
import karrus.client.generic.plot.TimeSeriesPlot;
import karrus.client.utils.Color;
import karrus.client.utils.DateTime;
import karrus.client.utils.Utils;
import karrus.shared.hibernate.SysEnv;
import karrus.shared.hibernate.TtBtItt;
import karrus.shared.hibernate.TtBtStat;
import karrus.shared.hibernate.TtItinerary;
import karrus.shared.language.Language;
import karrus.shared.plot.DoubleTimeSeries;
import karrus.shared.plot.SampleCount;
import karrus.shared.plot.TimeSeries;
import karrus.shared.plot.TravelTimePlot;
import ca.nanometrics.gflot.client.event.PlotItem;
import ca.nanometrics.gflot.client.event.PlotPosition;
import ca.nanometrics.gflot.client.jsni.Plot;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Creates a panel to show count data in graphs.
 * 
 * @author estelle.dumas@karrus-its.com
 */
public class TravelTimePlotPanel extends Composite {

	private TimeSeriesPlot timeSeriesPlot;
	private VerticalPanel plotsPanel;
	private String itinerary;
	private TravelTimePlot travelTimePlot;
	
	/**
	 * 
	 * @param itinerary
	 * @param initDate
	 * @param finalDate
	 * @throws Exception
	 */
	public TravelTimePlotPanel(String itinerary, Date initDate, Date finalDate, boolean isMultipleAxis) throws Exception{	
		this.itinerary = itinerary;
		plotsPanel = new VerticalPanel();
		plotsPanel.setSpacing(5);
		this.initWidget(plotsPanel);
		String title = Language.tpTitle(itinerary) + " - " + "du "+DateTime.frenchBarDateTimeFormat.format(initDate) +" au "+DateTime.frenchBarDateTimeFormat.format(finalDate);
		int height = LayoutInfo.onePlotHeight;
		int width = LayoutInfo.plotWidth;
		timeSeriesPlot = new TimeSeriesPlot(title, width, height, PlotType.POINT, true, false, isMultipleAxis, true);
		plotsPanel.add(timeSeriesPlot);
	}


	public void addTravelTimePlot(TravelTimePlot travelTimePlot){
		this.travelTimePlot = travelTimePlot;
		FrontalWebApp.genericDatabaseService.getEnvironmentVariable(Language.envBtPlotDefaultVar, new AsyncCallback<SysEnv>() {
			@Override
			public void onSuccess(SysEnv result) {
				Log.debug("genericDatabaseService.getEnvPropertiy ok");
				final SysEnv displayedPLotsSysEnv = result;
				FrontalWebApp.configurationsDatabaseService.getItinerary(itinerary, new AsyncCallback<TtItinerary>() {
					@Override
					public void onSuccess(TtItinerary result) {
						Log.debug("configurationsDatabaseService.getItinerary ok");
						Double plotScale = result.getScale()/60.;
						try {
							String[] displayedCurves = displayedPLotsSysEnv.getContent().split(",");
							List<String> displayedCurvesList = new ArrayList<String>();
							for (String curve : displayedCurves) {
								displayedCurvesList.add(curve);
							}
							boolean isDisplayed = false;
							boolean unknownMarkings = FrontalWebApp.ihmParameters.isUnknownMarkings();
							boolean nightMarkings = FrontalWebApp.ihmParameters.isNightMarkings();
							boolean missingDataMarkings = FrontalWebApp.ihmParameters.isMissingDataMarkings();
							// sample counts
							List<Date> sampleCountTimes = getTimeValueFromSampleCounts(TravelTimePlotPanel.this.travelTimePlot.getSampleCounts());
							//			List<Double> totalCounts = getTotalCountFromSampleCounts(travelTimePlot.getSampleCounts());
							List<Double> validCounts = getValidCountFromSampleCounts(TravelTimePlotPanel.this.travelTimePlot.getSampleCounts());
							//			TimeSeries timeSeriesTotalCount = new TimeSeries(sampleCountTimes, totalCounts);
							//			timeSeriesPlot.addTimeSeries(timeSeriesTotalCount, "Taille échantillon total", Color.colorSampleSizeAll, Language.sampleNumberTitle, 0, max, PlotType.STEP_FILL, false);
							TimeSeries timeSeriesValidCount = new TimeSeries(sampleCountTimes, validCounts);
							isDisplayed = displayedCurvesList.contains(Language.ttSamples)?true:false;
							PlotClickMessageFormatter individualTravelTimePlotClickMessageFormatter = new PlotClickMessageFormatter() {		
								@Override
								public String getMessage(Plot plot, PlotPosition position, PlotItem item) {
									Date d = new Date((long)item.getDataPoint().getX());
									int offset = 0;
									TimeZone tz = TimeZone.createTimeZone(offset);
									String x = DateTime.frenchDateTimeFormat.format(d, tz);
									double travelTimeInMinutes = item.getDataPoint().getY();
									int minutes = (int) Math.floor(travelTimeInMinutes);
									int seconds = (int) Math.round(travelTimeInMinutes*60 % 60);
									String y = minutes + " min " + Utils.addLeadingZeros(NumberFormat.getFormat("##").format(seconds), 2) + " s";
									String message = x + " - " + y;
									return message;
								}
							};
							timeSeriesPlot.addTimeSeries(timeSeriesValidCount, Language.validSampleSizeString, Color.colorSampleSizeValid, Language.sampleNumberTitle, 0, 120, "min", null, PlotType.BAR, false, false, false, 1, isDisplayed, null);
							// valid bt itt data
							List<Date> timesValidTtBtIttData = getTimeValueFromItt(TravelTimePlotPanel.this.travelTimePlot.getValidTtBtItts());
							List<Double> validTravelTimes = getTravelTimesFromItt(TravelTimePlotPanel.this.travelTimePlot.getValidTtBtItts());
							TimeSeries validTimeSeries = new TimeSeries(timesValidTtBtIttData, validTravelTimes);
							isDisplayed = displayedCurvesList.contains(Language.ttValids)?true:false;
							timeSeriesPlot.addTimeSeries(validTimeSeries, Language.validTravelTimesString, Color.colorGreen, Language.travelTimesMinTitle, 0, plotScale, "min", individualTravelTimePlotClickMessageFormatter,
									                     PlotType.POINT, false, false, false, 2, isDisplayed, null);
							// non valid bt itt data
							List<Date> timesNonValidTtBtIttData = getTimeValueFromItt(TravelTimePlotPanel.this.travelTimePlot.getNonValidTtBtItts());
							List<Double> nonValidTravelTimes = getTravelTimesFromItt(TravelTimePlotPanel.this.travelTimePlot.getNonValidTtBtItts());
							TimeSeries nonValidTimeSeries = new TimeSeries(timesNonValidTtBtIttData, nonValidTravelTimes);
							isDisplayed = displayedCurvesList.contains(Language.ttFiltered)?true:false;
							timeSeriesPlot.addTimeSeries(nonValidTimeSeries, Language.filteredTravelTimesString, Color.colorRed, Language.travelTimesMinTitle, 0, plotScale, "min", individualTravelTimePlotClickMessageFormatter,
									                     PlotType.POINT, false, false, false, 2, isDisplayed, null);
							// tpm
							List<Date> timesTtBtStatData = getTimeValueFromTtBtStat(TravelTimePlotPanel.this.travelTimePlot.getTtBtStats());
							List<Double> avgTravelTimes = getAvgTravelTimesFromTtBtStat(TravelTimePlotPanel.this.travelTimePlot.getTtBtStats());
							TimeSeries timeSeriesAvg = new TimeSeries(timesTtBtStatData, avgTravelTimes);
							isDisplayed = displayedCurvesList.contains(Language.ttMean)?true:false;
							timeSeriesPlot.addTimeSeries(timeSeriesAvg, Language.meanTravelTimesString, Color.colorBlack, Language.travelTimesMinTitle, 0, plotScale, "min", null, 
									                     PlotType.STEP, unknownMarkings, nightMarkings, missingDataMarkings, 2, isDisplayed, null);
							// median
							List<Double> medians = getMedianFromTtBtStat(TravelTimePlotPanel.this.travelTimePlot.getTtBtStats());
							TimeSeries timeSeriesMedian = new TimeSeries(timesTtBtStatData, medians);
							isDisplayed = displayedCurvesList.contains(Language.ttMedian)?true:false;
							timeSeriesPlot.addTimeSeries(timeSeriesMedian, Language.medianTravelTimesString, Color.colorGrey, Language.travelTimesMinTitle, 0, plotScale, "min", null,
									                     PlotType.STEP, false, false, false, 2, isDisplayed, null);
							// standard deviation stripe
							List<Double> upStandardDeviations = getUpStandardDeviationsFromTtBtStat(TravelTimePlotPanel.this.travelTimePlot.getTtBtStats());
							List<Double> downStandardDeviations = getDownStandardDeviationsFromTtBtStat(TravelTimePlotPanel.this.travelTimePlot.getTtBtStats());
							DoubleTimeSeries timeSeriesStandardDev = new DoubleTimeSeries(timesTtBtStatData, upStandardDeviations, downStandardDeviations);
							isDisplayed = displayedCurvesList.contains(Language.ttStd)?true:false;
							timeSeriesPlot.addDoubleTimeSeries(timeSeriesStandardDev, Language.standardDeviationsString, Color.colorTurquoise, Language.travelTimesMinTitle, 0, plotScale, PlotType.FILL, false, false, false, 2, isDisplayed, null);
							// decile 10-90 stripe
							List<Double> deciles10 = getDecile10FromTtBtStat(TravelTimePlotPanel.this.travelTimePlot.getTtBtStats());
							List<Double> deciles90 = getDecile90FromTtBtStat(TravelTimePlotPanel.this.travelTimePlot.getTtBtStats());
							DoubleTimeSeries timeSeries1090 = new DoubleTimeSeries(timesTtBtStatData, deciles90, deciles10);
							isDisplayed = displayedCurvesList.contains(Language.ttDecile10)?true:false;
							timeSeriesPlot.addDoubleTimeSeries(timeSeries1090, Language.decile1090String, Color.colorOrangeRed, Language.travelTimesMinTitle, 0, plotScale, PlotType.FILL, false, false, false, 2, isDisplayed, null);
							// decile 20-80 stripe
							List<Double> deciles20 = getDecile20FromTtBtStat(TravelTimePlotPanel.this.travelTimePlot.getTtBtStats());
							List<Double> deciles80 = getDecile80FromTtBtStat(TravelTimePlotPanel.this.travelTimePlot.getTtBtStats());
							DoubleTimeSeries timeSeries2080 = new DoubleTimeSeries(timesTtBtStatData, deciles80, deciles20);
							isDisplayed = displayedCurvesList.contains(Language.ttDecile20)?true:false;
							timeSeriesPlot.addDoubleTimeSeries(timeSeries2080, Language.decile2080String, Color.colorSalmon, Language.travelTimesMinTitle, 0, plotScale, PlotType.FILL, false, false, false, 2, isDisplayed, null);
							// decile 30-70 stripe
							List<Double> deciles30 = getDecile30FromTtBtStat(TravelTimePlotPanel.this.travelTimePlot.getTtBtStats());
							List<Double> deciles70 = getDecile70FromTtBtStat(TravelTimePlotPanel.this.travelTimePlot.getTtBtStats());
							DoubleTimeSeries timeSeries3070 = new DoubleTimeSeries(timesTtBtStatData, deciles70, deciles30);
							isDisplayed = displayedCurvesList.contains(Language.ttDecile30)?true:false;
							timeSeriesPlot.addDoubleTimeSeries(timeSeries3070, Language.decile3070String, Color.colorDarkOrange, Language.travelTimesMinTitle, 0, plotScale, PlotType.FILL, false, false, false, 2, isDisplayed, null);
							// decile 40-60 stripe
							List<Double> deciles40 = getDecile40FromTtBtStat(TravelTimePlotPanel.this.travelTimePlot.getTtBtStats());
							List<Double> deciles60 = getDecile60FromTtBtStat(TravelTimePlotPanel.this.travelTimePlot.getTtBtStats());
							DoubleTimeSeries timeSeries4060 = new DoubleTimeSeries(timesTtBtStatData, deciles60, deciles40);
							isDisplayed = displayedCurvesList.contains(Language.ttDecile40)?true:false;
							timeSeriesPlot.addDoubleTimeSeries(timeSeries4060, Language.decile4060String, Color.colorOrange, Language.travelTimesMinTitle, 0, plotScale, PlotType.FILL, false, false, false, 2, isDisplayed, null);
							// min
							List<Double> min = getMinFromTtBtStat(TravelTimePlotPanel.this.travelTimePlot.getTtBtStats());
							TimeSeries timeSeriesMin = new TimeSeries(timesTtBtStatData, min);
							isDisplayed = displayedCurvesList.contains(Language.ttMin)?true:false;
							timeSeriesPlot.addTimeSeries(timeSeriesMin, Language.minString, Color.colorLime, Language.travelTimesMinTitle, 0, plotScale, "min", null, PlotType.STEP, false, false, false, 2, isDisplayed, null);
							// max
							List<Double> max = getMaxFromTtBtStat(TravelTimePlotPanel.this.travelTimePlot.getTtBtStats());
							TimeSeries timeSeriesMax = new TimeSeries(timesTtBtStatData, max);
							isDisplayed = displayedCurvesList.contains(Language.ttMax)?true:false;
							timeSeriesPlot.addTimeSeries(timeSeriesMax, Language.maxString, Color.colorTomato, Language.travelTimesMinTitle, 0, plotScale, "min", null, PlotType.STEP, false, false, false, 2, isDisplayed, null);
							timeSeriesPlot.plot();
						} catch(Exception e){
							Log.error(e.getMessage());
							new CustomDialogBox(e.getMessage(), Language.okString);
						}
					}
					@Override
					public void onFailure(Throwable caught) {
						Log.error("configurationsDatabaseService.getItinerary failed. " + caught.getMessage());
						new CustomDialogBox(Language.genericErrorString, Language.okString);
					}
				});	
			}
			@Override
			public void onFailure(Throwable caught) {
				Log.error("genericDatabaseService.getEnvPropertiy failed. " + caught.getMessage());
				new CustomDialogBox("genericDatabaseService.getEnvPropertiy failed. " + caught.getMessage(), Language.okString);
			}
		});
	}

	private List<Date> getTimeValueFromItt(List<TtBtItt> data) {
		List<Date> dates = new ArrayList<Date>();
		for (TtBtItt d : data)
			dates.add(d.getId().getTimestamp());
		return dates;
	}

	private List<Date> getTimeValueFromTtBtStat(List<TtBtStat> data) {
		List<Date> dates = new ArrayList<Date>();
		for (TtBtStat d : data) 
			dates.add(d.getId().getTimestamp());
		return dates;
	}

	private List<Double> getTravelTimesFromItt(List<TtBtItt> data){
		List<Double> travelTimes = new ArrayList<Double>();
		for (TtBtItt d : data){
			travelTimes.add(d.getId().getTravelTime()/60.0);
		}
		return travelTimes;
	}

	private List<Double> getAvgTravelTimesFromTtBtStat(List<TtBtStat> data){
		List<Double> travelTimes = new ArrayList<Double>();
		for (TtBtStat d : data)
			travelTimes.add(d.getAverage()/60.0);
		return travelTimes;
	}

	private List<Double> getUpStandardDeviationsFromTtBtStat(List<TtBtStat> data){
		List<Double> standardDeviation = new ArrayList<Double>();
		for (TtBtStat d : data)
			if (d.getAverage()==-1)
				standardDeviation.add(-1/60.0);
			else
//				standardDeviation.add(d.getAverage()/60.0+d.getStandardDeviation().doubleValue()/60.0);
				standardDeviation.add(d.getAverage()/60.0+d.getStandardDeviation()/60.0);
		return standardDeviation;
	}

	private List<Double> getDownStandardDeviationsFromTtBtStat(List<TtBtStat> data){
		List<Double> standardDeviation = new ArrayList<Double>();
		for (TtBtStat d : data)
			if (d.getAverage()==-1)
				standardDeviation.add(-1/60.0);
			else
//				standardDeviation.add(d.getAverage()/60.0-d.getStandardDeviation().doubleValue()/60.0);
				standardDeviation.add(d.getAverage()/60.0-d.getStandardDeviation()/60.0);
		return standardDeviation;
	}

	private List<Double> getMedianFromTtBtStat(List<TtBtStat> data){
		List<Double> medianes = new ArrayList<Double>();
		for (TtBtStat d : data)
			if (d.getAverage()==-1)
				medianes.add(-1/60.0);
			else
//				medianes.add(d.getMedian().doubleValue()/60.0);
				medianes.add(d.getMedian()/60.0);
		return medianes;
	}

	private List<Double> getDecile10FromTtBtStat(List<TtBtStat> data){
		List<Double> deciles = new ArrayList<Double>();
		for (TtBtStat d : data)
			if (d.getAverage()==-1)
				deciles.add(-1/60.0);
			else
//				deciles.add(d.getDecile10().doubleValue()/60.0);
				deciles.add(d.getDecile10()/60.0);
		return deciles;
	}

	private List<Double> getDecile20FromTtBtStat(List<TtBtStat> data){
		List<Double> deciles = new ArrayList<Double>();
		for (TtBtStat d : data)
			if (d.getAverage()==-1)
				deciles.add(-1/60.0);
			else
//				deciles.add(d.getDecile20().doubleValue()/60.0);
				deciles.add(d.getDecile20()/60.0);
		return deciles;
	}

	private List<Double> getDecile30FromTtBtStat(List<TtBtStat> data){
		List<Double> deciles = new ArrayList<Double>();
		for (TtBtStat d : data)
			if (d.getAverage()==-1)
				deciles.add(-1/60.0);
			else
//				deciles.add(d.getDecile30().doubleValue()/60.0);
				deciles.add(d.getDecile30()/60.0);
		return deciles;
	}
	
	private List<Double> getDecile40FromTtBtStat(List<TtBtStat> data){
		List<Double> deciles = new ArrayList<Double>();
		for (TtBtStat d : data)
			if (d.getAverage()==-1)
				deciles.add(-1/60.0);
			else
//				deciles.add(d.getDecile40().doubleValue()/60.0);
				deciles.add(d.getDecile40()/60.0);
		return deciles;
	}
	
	private List<Double> getDecile60FromTtBtStat(List<TtBtStat> data){
		List<Double> deciles = new ArrayList<Double>();
		for (TtBtStat d : data)
			if (d.getAverage()==-1)
				deciles.add(-1/60.0);
			else
//				deciles.add(d.getDecile60().doubleValue()/60.0);
				deciles.add(d.getDecile60()/60.0);
		return deciles;
	}
	
	private List<Double> getDecile70FromTtBtStat(List<TtBtStat> data){
		List<Double> deciles = new ArrayList<Double>();
		for (TtBtStat d : data)
			if (d.getAverage()==-1)
				deciles.add(-1/60.0);
			else
//				deciles.add(d.getDecile70().doubleValue()/60.0);
				deciles.add(d.getDecile70()/60.0);
		return deciles;
	}
	
	private List<Double> getDecile80FromTtBtStat(List<TtBtStat> data){
		List<Double> deciles = new ArrayList<Double>();
		for (TtBtStat d : data)
			if (d.getAverage()==-1)
				deciles.add(-1/60.0);
			else
//				deciles.add(d.getDecile80().doubleValue()/60.0);
				deciles.add(d.getDecile80()/60.0);
		return deciles;
	}
	
	private List<Double> getDecile90FromTtBtStat(List<TtBtStat> data){
		List<Double> deciles = new ArrayList<Double>();
		for (TtBtStat d : data)
			if (d.getAverage()==-1)
				deciles.add(-1/60.0);
			else
//				deciles.add(d.getDecile90().doubleValue()/60.0);
				deciles.add(d.getDecile90()/60.0);
		return deciles;
	}

	private List<Double> getMinFromTtBtStat(List<TtBtStat> data){
		List<Double> mins = new ArrayList<Double>();
		for (TtBtStat d : data)
			if (d.getAverage()==-1)
				mins.add(-1/60.0);
			else
//				mins.add(d.getMinTravelTime().doubleValue()/60.0);
				mins.add(d.getMinTravelTime()/60.0);
		return mins;
	}

	private List<Double> getMaxFromTtBtStat(List<TtBtStat> data){
		List<Double> max = new ArrayList<Double>();
		for (TtBtStat d : data)
			if (d.getAverage()==-1)
				max.add(-1/60.0);
			else
//				max.add(d.getMaxTravelTime().doubleValue()/60.0);
				max.add(d.getMaxTravelTime()/60.0);
		return max;
	}


	private List<Date> getTimeValueFromSampleCounts(List<SampleCount> data) {
		List<Date> dates = new ArrayList<Date>();
		for (SampleCount d : data){
			if (d.getValidCount()!=-1)
				dates.add(d.getTimestamp());
		}
		return dates;
	}

	//	private List<Double> getTotalCountFromSampleCounts(List<SampleCount> data){
	//		List<Double> totalCounts = new ArrayList<Double>();
	//		for (SampleCount d : data)
	//			totalCounts.add(new Double(d.getTotalCount()));
	//		return totalCounts;
	//	}

	private List<Double> getValidCountFromSampleCounts(List<SampleCount> data){
		List<Double> totalCounts = new ArrayList<Double>();
		for (SampleCount d : data)
			if (d.getValidCount()!=-1)
				totalCounts.add(new Double(d.getValidCount()));
		return totalCounts;
	}
}
