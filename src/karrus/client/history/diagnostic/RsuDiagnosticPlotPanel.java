package karrus.client.history.diagnostic;

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
import karrus.shared.hibernate.RsuDiag;
import karrus.shared.language.Language;
import karrus.shared.plot.TimeSeries;
import ca.nanometrics.gflot.client.event.PlotItem;
import ca.nanometrics.gflot.client.event.PlotPosition;
import ca.nanometrics.gflot.client.jsni.Plot;

import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Panel containing roadside unit diagnostic
 * 
 * @author estelle.dumas@karrus-its.com
 */
public class RsuDiagnosticPlotPanel extends Composite {

	private VerticalPanel plotsPanel;
	private int height = (int)FrontalWebApp.getHeightForMainPanel()*70/100;
	private int width = FrontalWebApp.getWidthForMainPanel() - 25;
	private CountDataTimeSeriesPlot rsuDiagnosticTimeSeriesPlot = new CountDataTimeSeriesPlot(this, "", width, height, PlotType.POINT, true, false, true, true);
	
	/**
	 * Constructor
	 * @param station The station for which diagnostic are plotted
	 * @param startDate Beginning timestamp of the time interval
	 * @param endDate End timestamp of the time interval
	 */
	public RsuDiagnosticPlotPanel(String station, Date startDate, Date endDate) {	
		plotsPanel = new VerticalPanel();
		plotsPanel.setSize(FrontalWebApp.getWidthForMainPanel() + "px", FrontalWebApp.getHeightForMainPanel() + "px");
		plotsPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
//		plotsPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		plotsPanel.setSpacing(0);
		this.initWidget(plotsPanel);
	}
    
	/**
	 * 
	 * @param rsuDiagsList
	 * @throws Exception
	 */
	public void addPlots(List<RsuDiag> rsuDiagsList) throws Exception {
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
		PlotClickMessageFormatter batteryLevelPlotClickMessageFormatter = new PlotClickMessageFormatter() {
			@Override
			public String getMessage(Plot plot, PlotPosition position, PlotItem item) {
				Date d = new Date((long)item.getDataPoint().getX());
				int offset = 0;
				TimeZone tz = TimeZone.createTimeZone(offset);
				String x = DateTime.frenchDateTimeFormat.format(d, tz);
				String y = (int)Math.floor(item.getDataPoint().getY()*1.0)/1+"";
				String message = x + " - " + y + " " + "V";
				return message;
			}
		};
		rsuDiagnosticTimeSeriesPlot.addTimeSeries(new TimeSeries(
				getSerieTimestamps(rsuDiagsList),
				getBatteryLevelData(rsuDiagsList)),
				Language.rsuBatteryLevelLegend, colorMap.get("0"),
				Language.emptyString, 0, 30, "V", batteryLevelPlotClickMessageFormatter, PlotType.LINE, false, 1, true, null);
		PlotClickMessageFormatter rsuLoadPlotClickMessageFormatter = new PlotClickMessageFormatter() {
			@Override
			public String getMessage(Plot plot, PlotPosition position, PlotItem item) {
				Date d = new Date((long)item.getDataPoint().getX());
				int offset = 0;
				TimeZone tz = TimeZone.createTimeZone(offset);
				String x = DateTime.frenchDateTimeFormat.format(d, tz);
				String y = (int)Math.floor(item.getDataPoint().getY()*1.0)/1+"";
				String message = x + " - " + y + " " + "%";
				return message;
			}
		};
		rsuDiagnosticTimeSeriesPlot.addTimeSeries(new TimeSeries(
				getSerieTimestamps(rsuDiagsList),
				getLoadData(rsuDiagsList)),
				Language.rsuLoadLegend, colorMap.get("2"),
				Language.emptyString, 0, 100, "%", rsuLoadPlotClickMessageFormatter, PlotType.LINE, false, 2, true, null);
		PlotClickMessageFormatter rsuMemoryPlotClickMessageFormatter = new PlotClickMessageFormatter() {
			@Override
			public String getMessage(Plot plot, PlotPosition position, PlotItem item) {
				Date d = new Date((long)item.getDataPoint().getX());
				int offset = 0;
				TimeZone tz = TimeZone.createTimeZone(offset);
				String x = DateTime.frenchDateTimeFormat.format(d, tz);
				String y = (int)Math.floor(item.getDataPoint().getY()*1.0)/1+"";
				String message = x + " - " + y + " " + "%";
				return message;
			}
		};
		rsuDiagnosticTimeSeriesPlot.addTimeSeries(new TimeSeries(
				getSerieTimestamps(rsuDiagsList),
				getMemoryData(rsuDiagsList)),
				Language.rsuMemoryLegend, colorMap.get("3"),
				Language.emptyString, 0, 100, "%", rsuMemoryPlotClickMessageFormatter, PlotType.LINE, false, 2, true, null);
		PlotClickMessageFormatter rsuTemperaturePlotClickMessageFormatter = new PlotClickMessageFormatter() {
			@Override
			public String getMessage(Plot plot, PlotPosition position, PlotItem item) {
				Date d = new Date((long)item.getDataPoint().getX());
				int offset = 0;
				TimeZone tz = TimeZone.createTimeZone(offset);
				String x = DateTime.frenchDateTimeFormat.format(d, tz);
				String y = (int)Math.floor(item.getDataPoint().getY()*1.0)/1+"";
				String message = x + " - " + y + " " + Language.temperatureUnit;
				return message;
			}
		};
		rsuDiagnosticTimeSeriesPlot.addTimeSeries(new TimeSeries(
				getSerieTimestamps(rsuDiagsList),
				getTemperatureData(rsuDiagsList)),
				Language.rsuTemperatureLegend, colorMap.get("7"),
				Language.emptyString, 0, 50, Language.temperatureUnit, rsuTemperaturePlotClickMessageFormatter, PlotType.LINE, false, 3, true, null);
		rsuDiagnosticTimeSeriesPlot.getPlot().getPlotOptions().getYAxisOptions(1).setColor(colorMap.get("0"));
		rsuDiagnosticTimeSeriesPlot.getPlot().getPlotOptions().getYAxisOptions(3).setColor(colorMap.get("7"));
		rsuDiagnosticTimeSeriesPlot.plot();
		plotsPanel.add(rsuDiagnosticTimeSeriesPlot);
	}
	
	private List<Date> getSerieTimestamps(List<RsuDiag> rsuDiagsList) {
		List<Date> timestamps = new ArrayList<Date>();
		for (RsuDiag rsuDiag : rsuDiagsList) {
			timestamps.add(rsuDiag.getId().getTimestamp());
		}
		return timestamps;
	}
	
	private List<Double> getBatteryLevelData(List<RsuDiag> rsuDiagsList) {
		List<Double> batteryLevelValues = new ArrayList<Double>();
		for (RsuDiag rsuDiag : rsuDiagsList) {
			batteryLevelValues.add(rsuDiag.getBatteryLevel() == null ? null : rsuDiag.getBatteryLevel()/1.);
		}
		return batteryLevelValues;
	}
	
	private List<Double> getTemperatureData(List<RsuDiag> rsuDiagsList) {
		List<Double> batteryLevelValues = new ArrayList<Double>();
		for (RsuDiag rsuDiag : rsuDiagsList) {
			batteryLevelValues.add(rsuDiag.getTemperatureInternal() == null ? null : rsuDiag.getTemperatureInternal()/1.);
		}
		return batteryLevelValues;
	}
	
	private List<Double> getLoadData(List<RsuDiag> rsuDiagsList) {
		List<Double> batteryLevelValues = new ArrayList<Double>();
		for (RsuDiag rsuDiag : rsuDiagsList) {
			batteryLevelValues.add(rsuDiag.getLoad() == null ? null : rsuDiag.getLoad()/1.);
		}
		return batteryLevelValues;
	}
	
	private List<Double> getMemoryData(List<RsuDiag> rsuDiagsList) {
		List<Double> batteryLevelValues = new ArrayList<Double>();
		for (RsuDiag rsuDiag : rsuDiagsList) {
			batteryLevelValues.add(rsuDiag.getMemory() == null ? null : rsuDiag.getMemory()/1.);
		}
		return batteryLevelValues;
	}
}
