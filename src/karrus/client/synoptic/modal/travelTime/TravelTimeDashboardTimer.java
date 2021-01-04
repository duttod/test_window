package karrus.client.synoptic.modal.travelTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import karrus.client.FrontalWebApp;
import karrus.client.generic.CustomDialogBox;
import karrus.client.generic.plot.PlotClickMessageFormatter;
import karrus.client.generic.plot.PlotMinMax;
import karrus.client.generic.plot.PlotType;
import karrus.client.generic.plot.CountDataTimeSeriesPlot;
import karrus.client.utils.Color;
import karrus.client.utils.DateTime;
import karrus.client.utils.Utils;
import karrus.shared.hibernate.TtBtItt;
import karrus.shared.language.Language;
import karrus.shared.plot.TimeSeries;
import ca.nanometrics.gflot.client.event.PlotItem;
import ca.nanometrics.gflot.client.event.PlotPosition;
import ca.nanometrics.gflot.client.jsni.Plot;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Creates a timer to get speed and count from count data way.
 * 
 * @author estelle.dumas@karrus-its.com
 */
public class TravelTimeDashboardTimer extends Timer {

	private int refreshingRate;
	private int timeHorizon; 
	private Map<String, TimeSeries> dataToPlot2TimeSeries = new LinkedHashMap<String, TimeSeries>();
	private CountDataTimeSeriesPlot currentCountDataTimeSeriesPlot;
	private String itinerary;
	
	/**
	 * Constructor.
	 * @param refreshingRate int (in s)
	 * @param timeHorizon int (in s)
	 */
	public TravelTimeDashboardTimer(String itinerary, int refreshingRate, int timeHorizon) {
		dataToPlot2TimeSeries.put(Language.individualValidTravelTimeColumn, new TimeSeries());
		dataToPlot2TimeSeries.put(Language.individualNotValidTravelTimeColumn, new TimeSeries());
		this.refreshingRate = refreshingRate*1000;
		this.timeHorizon = timeHorizon*1000;
		this.itinerary = itinerary;
	}

	/**
	 * Starts the timer.
	 */
	public void start() {
		Log.debug("count data dashboard timer started...");
		this.run();
		this.scheduleRepeating(this.refreshingRate);
	}

	/**
	 * Stops the timer.
	 */
	public void stop() {
		Log.debug("count data dashboard timer stopped...");
		this.cancel();
	}

	/**
	 * The 'run' method is bufferized.
	 * Here is a scheme to understand the buffered action : 
	 * 
	 * We already have TimeSeries between the date a and the date b.
	 * We want to have TimeSeries between the date c and the date d.
	 * It depends on where are c and d respecting to a and b :
	 *
	 *                     a                 b
	 *                     |                 |
	 *  case 1:            |        c--------|------d            ----> remove values before c AND get values [b,d] and add them
	 *  case 2:     c------|-----------------|------d            ----> get values [c,a] and [b,d] and add them
	 *  case 3:     c------|--------d        |                   ----> remove values after d AND get values [c,a] and add them
	 *  case 4:  c-----d   |                 |                   ----> new time series [c,d]
	 *  case 5:            |                 |     c-----d       ----> new time series [c,d]
	 *  case 6:            |    c-------d    |                   ----> remove values before c AND remove values after d
	 * 
	 */


	@Override
	public void run() {
		Log.debug("count data dashboard timer running...");
		final Date timeHorizonStop = new Date();
		final Date timeHorizonStart = new Date(timeHorizonStop.getTime()-timeHorizon);

		if (currentCountDataTimeSeriesPlot!=null) {
			Date f = null; 
			Date l = null; 
			if (currentCountDataTimeSeriesPlot!=null) {
				currentCountDataTimeSeriesPlot.setMinMax(timeHorizonStart, timeHorizonStop); 
				f = currentCountDataTimeSeriesPlot.getFirstDateInTimeSeries();
				l = currentCountDataTimeSeriesPlot.getLastDateInTimeSeries();
			}
			final Date firstDateInCurrentCountDataTimeSeriesPlot = f;
			final Date lastDateInCurrentCountDataTimeSeriesPlot = l;
			if (firstDateInCurrentCountDataTimeSeriesPlot==null && lastDateInCurrentCountDataTimeSeriesPlot==null){
				final String itinerary = this.itinerary;
				Log.debug("CAS 11111");
				FrontalWebApp.genericDatabaseService.getTtBtItt(itinerary, timeHorizonStart, timeHorizonStop, new AsyncCallback<List<TtBtItt>>() {
					@Override
					public void onFailure(Throwable caught) {
						Log.error("service.getTtBtItt: failure\n"+caught.getMessage());
						caught.printStackTrace();
						new CustomDialogBox(Language.genericErrorString, Language.okString);
					}

					@Override
					public void onSuccess(List<TtBtItt> result) {
						try {
							currentCountDataTimeSeriesPlot.clear();
							if (result.size()==0) {
								for (String data : dataToPlot2TimeSeries.keySet()){
									String frenchColumnName = getFrenchColumnName(data);
									String yLabel = getUnits(data);
									String dataUnit = getDataUnit(data);
									String color = getColor(data);
									PlotClickMessageFormatter plotClickMessageFormatter = getPlotClickMessageFormatter(data);
									int axisNumber = getAxisNumber(data);
									dataToPlot2TimeSeries.put(data, null);
									currentCountDataTimeSeriesPlot.addTimeSeries(null, frenchColumnName, color, yLabel, PlotMinMax.getMinFromColumnName(data), PlotMinMax.getMaxFromColumnName(data), dataUnit, plotClickMessageFormatter, PlotType.POINT, false, axisNumber, true, null);
								}
							}
							else {
								for (String data : dataToPlot2TimeSeries.keySet()){
									TimeSeries timeSeries = getTimeSeries(result, data);
									String frenchColumnName = getFrenchColumnName(data);
									String yLabel = getUnits(data);
									String dataUnit = getDataUnit(data);
									String color = getColor(data);
									PlotClickMessageFormatter plotClickMessageFormatter = getPlotClickMessageFormatter(data);
									int axisNumber = getAxisNumber(data);
									dataToPlot2TimeSeries.put(data, timeSeries);
									currentCountDataTimeSeriesPlot.addTimeSeries(timeSeries, frenchColumnName, color, yLabel, PlotMinMax.getMinFromColumnName(data), PlotMinMax.getMaxFromColumnName(data), dataUnit, plotClickMessageFormatter, PlotType.POINT, false, axisNumber, true, null);
								}
							}
							currentCountDataTimeSeriesPlot.plot();
						} catch(Exception e){
							e.printStackTrace();
							Log.error(e.getMessage());
							new CustomDialogBox(Language.genericErrorString, Language.okString);
						}

					}
				});
			}
			else {
				final String itinerary = this.itinerary;
				if (timeHorizonStart.after(firstDateInCurrentCountDataTimeSeriesPlot)){
					if (timeHorizonStop.after(lastDateInCurrentCountDataTimeSeriesPlot)){
						// CAS 1
						if (lastDateInCurrentCountDataTimeSeriesPlot.after(timeHorizonStart)){
							Log.debug("CAS 1");
							currentCountDataTimeSeriesPlot.removeValuesBefore(timeHorizonStart);
							for (String data : dataToPlot2TimeSeries.keySet())
								dataToPlot2TimeSeries.get(data).removeValuesBefore(timeHorizonStart);
							FrontalWebApp.genericDatabaseService.getTtBtItt(itinerary, lastDateInCurrentCountDataTimeSeriesPlot, timeHorizonStop, new AsyncCallback<List<TtBtItt>>() {
								@Override
								public void onFailure(Throwable caught) {
									Log.error("service.getTtBtItt: failure\n"+caught.getMessage());
									new CustomDialogBox(Language.genericErrorString, Language.okString);
								}
								@Override
								public void onSuccess(List<TtBtItt> result) {
									try{
										currentCountDataTimeSeriesPlot.clear();
										if (result.size()==0){
											for (String data : dataToPlot2TimeSeries.keySet()){
												String frenchColumnName = getFrenchColumnName(data);
												String yLabel = getUnits(data);
												String dataUnit = getDataUnit(data);
												String color = getColor(data);
												PlotClickMessageFormatter plotClickMessageFormatter = getPlotClickMessageFormatter(data);
												int axisNumber = getAxisNumber(data);
												currentCountDataTimeSeriesPlot.addTimeSeries(dataToPlot2TimeSeries.get(data), frenchColumnName, color, yLabel, PlotMinMax.getMinFromColumnName(data), PlotMinMax.getMaxFromColumnName(data), dataUnit, plotClickMessageFormatter, PlotType.POINT, false, axisNumber, true, null);
											}
										}
										else {
											for (String data : dataToPlot2TimeSeries.keySet()){
												TimeSeries timeSeries = getTimeSeries(result, data);
												String frenchColumnName = getFrenchColumnName(data);
												String yLabel = getUnits(data);
												String dataUnit = getDataUnit(data);
												String color = getColor(data);
												PlotClickMessageFormatter plotClickMessageFormatter = getPlotClickMessageFormatter(data);
												int axisNumber = getAxisNumber(data);
												dataToPlot2TimeSeries.get(data).addValues(timeSeries);
												currentCountDataTimeSeriesPlot.addTimeSeries(dataToPlot2TimeSeries.get(data), frenchColumnName, color, yLabel, PlotMinMax.getMinFromColumnName(data), PlotMinMax.getMaxFromColumnName(data), dataUnit, plotClickMessageFormatter, PlotType.POINT, false, axisNumber, true, null);
											}


										}
										currentCountDataTimeSeriesPlot.plot();
									} catch(Exception e){
										Log.error(e.getMessage());
										new CustomDialogBox(Language.genericErrorString, Language.okString);
									}
								}
							});
						}
						// CAS 5
						else {
							Log.debug("CAS 5");
							FrontalWebApp.genericDatabaseService.getTtBtItt(itinerary, timeHorizonStart, timeHorizonStop, new AsyncCallback<List<TtBtItt>>() {
								@Override
								public void onFailure(Throwable caught) {
									Log.error("service.getTtBtItt: failure\n"+caught.getMessage());
									new CustomDialogBox(Language.genericErrorString, Language.okString);
								}
								@Override
								public void onSuccess(List<TtBtItt> result) {
									try{
										currentCountDataTimeSeriesPlot.clear();
										if (result.size()==0){
											for (String data : dataToPlot2TimeSeries.keySet()){
												String frenchColumnName = getFrenchColumnName(data);
												String yLabel = getUnits(data);
												String dataUnit = getDataUnit(data);
												String color = getColor(data);
												PlotClickMessageFormatter plotClickMessageFormatter = getPlotClickMessageFormatter(data);
												int axisNumber = getAxisNumber(data);
												currentCountDataTimeSeriesPlot.addTimeSeries(null, frenchColumnName, color, yLabel, PlotMinMax.getMinFromColumnName(data), PlotMinMax.getMaxFromColumnName(data), dataUnit, plotClickMessageFormatter, PlotType.POINT, false, axisNumber, true, null);
											}
										}
										else {

											for (String data : dataToPlot2TimeSeries.keySet()){
												TimeSeries timeSeries = getTimeSeries(result, data);
												String frenchColumnName = getFrenchColumnName(data);
												String yLabel = getUnits(data);
												String dataUnit = getDataUnit(data);
												String color = getColor(data);
												PlotClickMessageFormatter plotClickMessageFormatter = getPlotClickMessageFormatter(data);
												int axisNumber = getAxisNumber(data);
												dataToPlot2TimeSeries.get(data).setTimesSeries(timeSeries);
												currentCountDataTimeSeriesPlot.addTimeSeries(dataToPlot2TimeSeries.get(data), frenchColumnName, color, yLabel, PlotMinMax.getMinFromColumnName(data), PlotMinMax.getMaxFromColumnName(data), dataUnit, plotClickMessageFormatter, PlotType.POINT, false, axisNumber, true, null);
											}
										}
										currentCountDataTimeSeriesPlot.plot();
									} catch(Exception e){
										Log.error(e.getMessage());
										new CustomDialogBox(Language.genericErrorString, Language.okString);
									}

								}
							});
						}
					}
					// CAS 6
					else 
						Log.debug("CAS 6 : never occurs. If you see this message, there is a grave error...");
				}
				else {
					if (timeHorizonStop.after(firstDateInCurrentCountDataTimeSeriesPlot)){
						// CAS 2
						if (timeHorizonStop.after(lastDateInCurrentCountDataTimeSeriesPlot)){
							Log.debug("CAS 2");
							// get values between c and a, and add them
							FrontalWebApp.genericDatabaseService.getTtBtItt(itinerary, timeHorizonStart, firstDateInCurrentCountDataTimeSeriesPlot, new AsyncCallback<List<TtBtItt>>() {
								@Override
								public void onFailure(Throwable caught) {
									Log.error("service.getTtBtItt: failure\n"+caught.getMessage());
									new CustomDialogBox(Language.genericErrorString, Language.okString);
								}
								@Override
								public void onSuccess(final List<TtBtItt> result1) {
									// get values between b and d, and add them
									FrontalWebApp.genericDatabaseService.getTtBtItt(itinerary,  lastDateInCurrentCountDataTimeSeriesPlot, timeHorizonStop, new AsyncCallback<List<TtBtItt>>() {
										@Override
										public void onFailure(Throwable caught) {
											Log.error("service.getTtBtItt: failure\n"+caught.getMessage());
											new CustomDialogBox(Language.genericErrorString, Language.okString);
										}
										@Override
										public void onSuccess(List<TtBtItt> result2) {
											for (String data : dataToPlot2TimeSeries.keySet()){
												try {
													TimeSeries timeSeries1 = getTimeSeries(result1, data);
													TimeSeries timeSeries2 = getTimeSeries(result2, data);
													if (timeSeries1.size()!=0)
														timeSeries2.addValues(timeSeries1);
													if (dataToPlot2TimeSeries.get(data)==null)
														dataToPlot2TimeSeries.get(data).setTimesSeries(timeSeries2);
													else
														dataToPlot2TimeSeries.get(data).addValues(timeSeries2);
													String frenchColumnName = getFrenchColumnName(data);
													String yLabel = getUnits(data);
													String dataUnit = getDataUnit(data);
													String color = getColor(data);
													PlotClickMessageFormatter plotClickMessageFormatter = getPlotClickMessageFormatter(data);
													int axisNumber = getAxisNumber(data);
													currentCountDataTimeSeriesPlot.addTimeSeries(dataToPlot2TimeSeries.get(data), frenchColumnName, color, yLabel, PlotMinMax.getMinFromColumnName(data), PlotMinMax.getMaxFromColumnName(data), dataUnit, plotClickMessageFormatter, PlotType.POINT, false, axisNumber, true, null);
													currentCountDataTimeSeriesPlot.plot();
												} catch(Exception e){
													e.printStackTrace();
													Log.error(e.getMessage());
													new CustomDialogBox(Language.genericErrorString, Language.okString);
												}
											}


										}
									});
								}
							});
						}
						// CAS 3
						else {
							Log.debug("CAS 3 : never occurs. If you see this message, there is a grave error...");
						}
					}
					// CAS 4
					else {
						Log.debug("CAS 4 : never occurs. If you see this message, there is a grave error...");
					}
				}
			}
		}
	}

	public TimeSeries getTimeSeries(List<TtBtItt> btItts, String columnName) throws Exception{
		List<Date> dates = new ArrayList<Date>();
		List<Double> values = new ArrayList<Double>();
		if (columnName.equals(Language.individualValidTravelTimeColumn)) {
			for (TtBtItt btItt : btItts) {
				if (btItt.isAttValid()) {
					dates.add(btItt.getId().getTimestamp());
					values.add((double) btItt.getId().getTravelTime()/60);
				}
			}
		}
		else if (columnName.equals(Language.individualNotValidTravelTimeColumn)) {
			for (TtBtItt btItt : btItts) {
				if (!btItt.isAttValid()) {
					dates.add(btItt.getId().getTimestamp());
					values.add((double)btItt.getId().getTravelTime()/60);
				}
			}
		}
		return new TimeSeries(dates, values);

	}

	private String getUnits(String columnName){
		return Language.getUnitsForColumn(columnName);
	}
	
	private String getDataUnit(String columnName){
		return Language.getDataUnitForColumn(columnName);
	}

	private String getFrenchColumnName(String columnName){
		return Language.getFrenchForColumn(columnName);
	}

	private String getColor(String columnName){
		return Color.getColorFromColumn(columnName);
	}

	private int getAxisNumber(String columnName){
		return 1;
	}
	
	private PlotClickMessageFormatter getPlotClickMessageFormatter(String columName) {
		if (columName.equals(Language.individualValidTravelTimeColumn)) {
			PlotClickMessageFormatter plotClickMessageFormatter = new PlotClickMessageFormatter() {
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
			return plotClickMessageFormatter;
		}
		else {
			PlotClickMessageFormatter plotClickMessageFormatter = new PlotClickMessageFormatter() {
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
			return plotClickMessageFormatter;
		}
	}

	public void addPlot(CountDataTimeSeriesPlot timeSeriesPlot) {
		currentCountDataTimeSeriesPlot = timeSeriesPlot;
		
	}

	public void setTimeHorizon(int timeHorizon) {
		stop();
		this.timeHorizon = timeHorizon;
		start();
	}

	public void setRefreshingRate(int refreshingRate) {
		this.refreshingRate = refreshingRate;
		this.cancel();
		this.scheduleRepeating(this.refreshingRate);
	}
}

