package karrus.client.history.weather;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karrus.client.FrontalWebApp;
import karrus.client.generic.plot.PlotClickMessageFormatter;
import karrus.client.generic.plot.PlotType;
import karrus.client.generic.plot.WeatherStatesTimeSeriesPlot;
import karrus.client.utils.Color;
import karrus.client.utils.DateTime;
import karrus.shared.hibernate.WthMeteoData;
import karrus.shared.language.Language;
import karrus.shared.plot.TimeSeries;
import ca.nanometrics.gflot.client.Tick;
import ca.nanometrics.gflot.client.event.PlotItem;
import ca.nanometrics.gflot.client.event.PlotPosition;
import ca.nanometrics.gflot.client.jsni.Plot;

import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Panel containing temperatures plot and humidity plot
 * 
 * @author estelle.dumas@karrus-its.com
 */
public class WeatherStatesPlotPanel extends Composite {

	private VerticalPanel plotsPanel;
	private int height = (int)FrontalWebApp.getHeightForMainPanel()*40/100;
	private int width = FrontalWebApp.getWidthForMainPanel() - 5;
	private WeatherStatesTimeSeriesPlot freezingStatesTimeSeriesPlot = new WeatherStatesTimeSeriesPlot(this, "", width, height, PlotType.POINT, true, false, true, true);
	private WeatherStatesTimeSeriesPlot precipitationStatesTimeSeriesPlot = new WeatherStatesTimeSeriesPlot(this, "", width, height, PlotType.POINT, true, false, true, true);
	
	/**
	 * Constructor
	 * @param station The station for which weather data are plotted
	 * @param startDate Beginning timestamp of the time interval
	 * @param endDate End timestamp of the time interval
	 */
	public WeatherStatesPlotPanel(String station, Date startDate, Date endDate) {	
		plotsPanel = new VerticalPanel();
		plotsPanel.setSpacing(0);
		this.initWidget(plotsPanel);
	}
    
	/**
	 * Add temperatures plot and humidity plot to the plots panel
	 * @param weatherData The list of weather data to be plotted
	 * @throws Exception 
	 */
	public void addPlots(List<WthMeteoData> weatherData) throws Exception {
		Map<String, String> colorMap = new HashMap<String, String>();
		Tick[] ticks;
		colorMap.put("0", Color.colorGreen);
		colorMap.put("1", Color.colorYellow);
		colorMap.put("2", Color.colorTurquoise);
		colorMap.put("3", Color.colorMagenta);
		colorMap.put("4", Color.colorGrey);
		colorMap.put("5", Color.colorDarkOrange);
		colorMap.put("6", Color.colorSampleSizeAll);
		colorMap.put("7", Color.colorBlue);
		colorMap.put("8", Color.colorLime);
		ticks = new Tick[10];
		ticks[0] = new Tick(0, Language.surfaceState_0);
		ticks[1] = new Tick(1, Language.surfaceState_1);
		ticks[2] = new Tick(2, Language.surfaceState_2);
		ticks[3] = new Tick(3, Language.surfaceState_3);
		ticks[4] = new Tick(4, Language.surfaceState_4);
		ticks[5] = new Tick(5, Language.surfaceState_5);
		ticks[6] = new Tick(6, Language.surfaceState_6);
		ticks[7] = new Tick(7, Language.surfaceState_7);
		ticks[8] = new Tick(8, Language.surfaceState_8);
		ticks[9] = new Tick(9, Language.surfaceState_9);
		PlotClickMessageFormatter statesClickMessageFormatter = new PlotClickMessageFormatter() {
			@Override
			public String getMessage(Plot plot, PlotPosition position, PlotItem item) {
				Date d = new Date((long)item.getDataPoint().getX());
				int offset = 0;
				TimeZone tz = TimeZone.createTimeZone(offset);
				String x = DateTime.frenchDateTimeFormat.format(d, tz);
				String y = (int)Math.floor(item.getDataPoint().getY()*1.0)/1+"";
				String message = x + " - " + y + "";
				return message;
			}
		};
		freezingStatesTimeSeriesPlot.addTimeSeries(new TimeSeries(
				getSerieTimestamps(weatherData),
				getSurfaceStateData(weatherData)),
				Language.surfaceStateLegend, colorMap.get("0"),
				Language.emptyString, 0, 10, "", statesClickMessageFormatter, null, false, 1, true, ticks);
		ticks = new Tick[6];
		ticks[0] = new Tick(0, Language.freezingRisk_0);
		ticks[1] = new Tick(1, Language.freezingRisk_1);
		ticks[2] = new Tick(2, Language.freezingRisk_2);
		ticks[3] = new Tick(3, Language.freezingRisk_3);
		ticks[4] = new Tick(4, Language.freezingRisk_4);
		ticks[5] = new Tick(9, Language.freezingRisk_9);
		freezingStatesTimeSeriesPlot.addTimeSeries(new TimeSeries(
				getSerieTimestamps(weatherData),
				getFreezingRiskData(weatherData)),
				Language.freezingRiskLegend, colorMap.get("7"),
				Language.emptyString, 0, 10, "", statesClickMessageFormatter, null, false, 2, true, ticks);
		ticks = new Tick[6];
		ticks[0] = new Tick(0, Language.salinity0);
		ticks[1] = new Tick(20, Language.salinity20);
		ticks[2] = new Tick(40, Language.salinity40);
		ticks[3] = new Tick(60, Language.salinity60);
		ticks[4] = new Tick(80, Language.salinity80);
		ticks[5] = new Tick(100, Language.salinity100);
		freezingStatesTimeSeriesPlot.addTimeSeries(new TimeSeries(
				getSerieTimestamps(weatherData),
				getSalinityData(weatherData)),
				Language.salinityLegend, colorMap.get("2"),
				Language.emptyString, 0, 100, "", statesClickMessageFormatter, PlotType.LINE, false, 3, true, ticks);
		ticks = new Tick[10];
		ticks[0] = new Tick(0, Language.precipitationType_0);
		ticks[1] = new Tick(1, Language.precipitationType_1);
		ticks[2] = new Tick(2, Language.precipitationType_2);
		ticks[3] = new Tick(3, Language.precipitationType_3);
		ticks[4] = new Tick(4, Language.emptyString);
		ticks[5] = new Tick(5, Language.emptyString);
		ticks[6] = new Tick(6, Language.emptyString);
		ticks[7] = new Tick(7, Language.emptyString);
		ticks[8] = new Tick(8, Language.emptyString);
		ticks[9] = new Tick(9, Language.precipitationType_9);
		precipitationStatesTimeSeriesPlot.addTimeSeries(new TimeSeries(
				getSerieTimestamps(weatherData),
				getPrecipitationTypeData(weatherData)),
				Language.precipitationTypeLegend, colorMap.get("3"),
				Language.emptyString, 0, 10, "", statesClickMessageFormatter, null, false, 1, true, ticks);
		ticks = new Tick[7];
		ticks[0] = new Tick(0, Language.precipitationIntensity_0);
		ticks[1] = new Tick(1, Language.precipitationIntensity_1);
		ticks[2] = new Tick(2, Language.precipitationIntensity_2);
		ticks[3] = new Tick(3, Language.precipitationIntensity_3);
		ticks[4] = new Tick(4, Language.precipitationIntensity_4);
		ticks[5] = new Tick(5, Language.precipitationIntensity_5);
		ticks[6] = new Tick(9, Language.precipitationIntensity_9);
		precipitationStatesTimeSeriesPlot.addTimeSeries(new TimeSeries(
				getSerieTimestamps(weatherData),
				getPrecipitationIntensityData(weatherData)),
				Language.precipitationIntensityLegend, colorMap.get("4"),
				Language.emptyString, 0, 10, "", statesClickMessageFormatter, null, false, 2, true, ticks);
		freezingStatesTimeSeriesPlot.plot();
		plotsPanel.add(freezingStatesTimeSeriesPlot);
		precipitationStatesTimeSeriesPlot.plot();
		plotsPanel.add(precipitationStatesTimeSeriesPlot);
	}
	
	private List<Date> getSerieTimestamps(List<WthMeteoData> weatherDataList) {
		List<Date> timestamps = new ArrayList<Date>();
		for (WthMeteoData weatherData : weatherDataList) {
			timestamps.add(weatherData.getId().getTimestamp());
		}
		return timestamps;
	}
	
	private List<Double> getSurfaceStateData(List<WthMeteoData> weatherDataList) {
		List<Double> surfaceStateValues = new ArrayList<Double>();
		for (WthMeteoData weatherData : weatherDataList) {
			surfaceStateValues.add(weatherData.getSurfaceState() == null ? null : weatherData.getSurfaceState()/1.);
		}
		return surfaceStateValues;
	}
	
	private List<Double> getSalinityData(List<WthMeteoData> weatherDataList) {
		List<Double> salinityValues = new ArrayList<Double>();
		for (WthMeteoData weatherData : weatherDataList) {
			salinityValues.add(weatherData.getSalinity() == null ? null : weatherData.getSalinity());
		}
		return salinityValues;
	}
	
	private List<Double> getPrecipitationTypeData(List<WthMeteoData> weatherDataList) {
		List<Double> precipitationTypeValues = new ArrayList<Double>();
		for (WthMeteoData weatherData : weatherDataList) {
			precipitationTypeValues.add(weatherData.getPrecipitationType() == null ? null : weatherData.getPrecipitationType()/1.);
		}
		return precipitationTypeValues;
	}
	
	private List<Double> getPrecipitationIntensityData(List<WthMeteoData> weatherDataList) {
		List<Double> precipitationIntensityValues = new ArrayList<Double>();
		for (WthMeteoData weatherData : weatherDataList) {
			precipitationIntensityValues.add(weatherData.getPrecipitationIntensity() == null ? null : weatherData.getPrecipitationIntensity()/1.);
		}
		return precipitationIntensityValues;
	}
	
	private List<Double> getFreezingRiskData(List<WthMeteoData> weatherDataList) {
		List<Double> freezingRiskValues = new ArrayList<Double>();
		for (WthMeteoData weatherData : weatherDataList) {
			freezingRiskValues.add(weatherData.getFreezingRisk() == null ? null : weatherData.getFreezingRisk()/1.);
		}
		return freezingRiskValues;
	}
	
	public void redrawPlots (double xFrom, double xTo) {
		freezingStatesTimeSeriesPlot.getPlot().getPlotOptions().getXAxisOptions().setMinimum(xFrom);
		freezingStatesTimeSeriesPlot.getPlot().getPlotOptions().getXAxisOptions().setMaximum(xTo);
		freezingStatesTimeSeriesPlot.getPlot().redraw();
	}
}
