package karrus.client.history.weather;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karrus.client.FrontalWebApp;
import karrus.client.generic.plot.PlotClickMessageFormatter;
import karrus.client.generic.plot.PlotType;
import karrus.client.generic.plot.CountDataTimeSeriesPlot;
import karrus.client.utils.Color;
import karrus.client.utils.DateTime;
import karrus.shared.hibernate.WthMeteoData;
import karrus.shared.language.Language;
import karrus.shared.plot.TimeSeries;
import ca.nanometrics.gflot.client.event.PlotItem;
import ca.nanometrics.gflot.client.event.PlotPosition;
import ca.nanometrics.gflot.client.jsni.Plot;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Panel containing temperatures plot and humidity plot
 * 
 * @author estelle.dumas@karrus-its.com
 */
public class TemperatureAndHumidityPlotPanel extends Composite {

	private VerticalPanel plotsPanel;
	private int height = (int)FrontalWebApp.getHeightForMainPanel()*40/100;
	private int width = FrontalWebApp.getWidthForMainPanel() - 25;
	private CountDataTimeSeriesPlot temperaturesTimeSeriesPlot = new CountDataTimeSeriesPlot(this, "", width, height, PlotType.POINT, true, false, true, true);
	private CountDataTimeSeriesPlot humidityTimeSeriesPlot = new CountDataTimeSeriesPlot(this, "", width, height, PlotType.POINT, true, false, true, true);
	
	/**
	 * Constructor
	 * @param station The station for which weather data are plotted
	 * @param startDate Beginning timestamp of the time interval
	 * @param endDate End timestamp of the time interval
	 */
	public TemperatureAndHumidityPlotPanel(String station, Date startDate, Date endDate) {	
		
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
		colorMap.put("0", Color.colorGreen);
		colorMap.put("1", Color.colorYellow);
		colorMap.put("2", Color.colorTurquoise);
		colorMap.put("3", Color.colorMagenta);
		colorMap.put("4", Color.colorGrey);
		colorMap.put("5", Color.colorDarkOrange);
		colorMap.put("6", Color.colorSampleSizeAll);
		colorMap.put("7", Color.colorBlue);
		colorMap.put("8", Color.colorLime);
		PlotClickMessageFormatter temperaturePlotClickMessageFormatter = new PlotClickMessageFormatter() {
			@Override
			public String getMessage(Plot plot, PlotPosition position, PlotItem item) {
				Date d = new Date((long)item.getDataPoint().getX());
				int offset = 0;
				TimeZone tz = TimeZone.createTimeZone(offset);
				String x = DateTime.frenchDateTimeFormat.format(d, tz);
				String y = NumberFormat.getFormat("##.#").format(item.getDataPoint().getY());
				String message = x + " - " + y + " " + Language.temperatureUnit;
				return message;
			}
		};
		temperaturesTimeSeriesPlot.addTimeSeries(new TimeSeries(
				getSerieTimestamps(weatherData),
				getAirTemperatureData(weatherData)),
				Language.airTemperatureLegend, colorMap.get("0"),
				Language.temperatureYLabel, (double) -10, 60., Language.temperatureUnit, temperaturePlotClickMessageFormatter, PlotType.LINE, false);
		temperaturesTimeSeriesPlot.addTimeSeries(new TimeSeries(
				getSerieTimestamps(weatherData),
				getDewTemperatureData(weatherData)),
				Language.dewTemperatureLegend, colorMap.get("1"),
				Language.temperatureYLabel, (double) -10, 60., Language.temperatureUnit, temperaturePlotClickMessageFormatter, PlotType.LINE, false);
		temperaturesTimeSeriesPlot.addTimeSeries(new TimeSeries(
				getSerieTimestamps(weatherData),
				getRoadTemperatureData(weatherData)),
				Language.roadTemperatureLegend, colorMap.get("2"),
				Language.temperatureYLabel, (double) -10, 60., Language.temperatureUnit, temperaturePlotClickMessageFormatter, PlotType.LINE, false);
		temperaturesTimeSeriesPlot.addTimeSeries(new TimeSeries(
				getSerieTimestamps(weatherData),
				getBelowSurfTemperatureData(weatherData)),
				Language.belowSurfTemperatureLegend, colorMap.get("3"),
				Language.temperatureYLabel, (double) -10, 60., Language.temperatureUnit, temperaturePlotClickMessageFormatter, PlotType.LINE, false);
		temperaturesTimeSeriesPlot.addTimeSeries(new TimeSeries(
				getSerieTimestamps(weatherData),
				getFreezingTemperatureData(weatherData)),
				Language.freezingTemperatureLegend, colorMap.get("4"),
				Language.temperatureYLabel, (double) -10, 60., Language.temperatureUnit, temperaturePlotClickMessageFormatter, PlotType.LINE, false);
		temperaturesTimeSeriesPlot.plot();
		plotsPanel.add(temperaturesTimeSeriesPlot);
		PlotClickMessageFormatter humidityPlotClickMessageFormatter = new PlotClickMessageFormatter() {
			@Override
			public String getMessage(Plot plot, PlotPosition position, PlotItem item) {
				Date d = new Date((long)item.getDataPoint().getX());
				int offset = 0;
				TimeZone tz = TimeZone.createTimeZone(offset);
				String x = DateTime.frenchDateTimeFormat.format(d, tz);
				String y = NumberFormat.getFormat("##.#").format(item.getDataPoint().getY());
				String message = x + " - " + y + " %";
				return message;
			}
		};
		humidityTimeSeriesPlot.addTimeSeries(new TimeSeries(
				getSerieTimestamps(weatherData),
				getHumidityData(weatherData)),
				Language.humidityLegend, colorMap.get("0"),
				Language.humidityYLabel, 0, 100, "%", humidityPlotClickMessageFormatter, PlotType.LINE, false);
		humidityTimeSeriesPlot.plot();
		plotsPanel.add(humidityTimeSeriesPlot);
	}
	
	private List<Date> getSerieTimestamps(List<WthMeteoData> weatherDataList) {
		List<Date> timestamps = new ArrayList<Date>();
		for (WthMeteoData weatherData : weatherDataList) {
			timestamps.add(weatherData.getId().getTimestamp());
		}
		return timestamps;
	}
	
	private List<Double> getAirTemperatureData(List<WthMeteoData> weatherDataList) {
		List<Double> airTemperatureValues = new ArrayList<Double>();
		for (WthMeteoData weatherData : weatherDataList) {
			airTemperatureValues.add(weatherData.getTemperatureAir() == null ? null : weatherData.getTemperatureAir());
		}
		return airTemperatureValues;
	}
	
	private List<Double> getDewTemperatureData(List<WthMeteoData> weatherDataList) {
		List<Double> dewTemperatureValues = new ArrayList<Double>();
		for (WthMeteoData weatherData : weatherDataList) {
			dewTemperatureValues.add(weatherData.getTemperatureDew()== null ? null : weatherData.getTemperatureDew());
		}
		return dewTemperatureValues;
	}
	
	private List<Double> getHumidityData(List<WthMeteoData> weatherDataList) {
		List<Double> humidityValues = new ArrayList<Double>();
		for (WthMeteoData weatherData : weatherDataList) {
			humidityValues.add(weatherData.getHumidity()== null ? null : weatherData.getHumidity());
		}
		return humidityValues;
	}
	
	private List<Double> getRoadTemperatureData(List<WthMeteoData> weatherDataList) {
		List<Double> roadTemperatureValues = new ArrayList<Double>();
		for (WthMeteoData weatherData : weatherDataList) {
			roadTemperatureValues.add(weatherData.getTemperatureRoad() == null ? null : weatherData.getTemperatureRoad());
		}
		return roadTemperatureValues;
	}
	
	private List<Double> getBelowSurfTemperatureData(List<WthMeteoData> weatherDataList) {
		List<Double> belowSurfTemperatureValues = new ArrayList<Double>();
		for (WthMeteoData weatherData : weatherDataList) {
			belowSurfTemperatureValues.add(weatherData.getTemperatureBelowSurf() == null ? null : weatherData.getTemperatureBelowSurf());
		}
		return belowSurfTemperatureValues;
	}
	
	private List<Double> getFreezingTemperatureData(List<WthMeteoData> weatherDataList) {
		List<Double> freezingTemperatureValues = new ArrayList<Double>();
		for (WthMeteoData weatherData : weatherDataList) {
			freezingTemperatureValues.add(weatherData.getTemperatureFreezing() == null ? null : weatherData.getTemperatureFreezing());
		}
		return freezingTemperatureValues;
	}
}
