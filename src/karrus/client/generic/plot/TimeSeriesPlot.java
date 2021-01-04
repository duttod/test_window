package karrus.client.generic.plot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import karrus.client.utils.Color;
import karrus.shared.language.Language;
import karrus.shared.plot.DoubleTimeSeries;
import karrus.shared.plot.StripeData;
import karrus.shared.plot.TimeSeries;
import karrus.shared.plot.TimeSeriesInfo;
import ca.nanometrics.gflot.client.DataPoint;
import ca.nanometrics.gflot.client.PlotModel;
import ca.nanometrics.gflot.client.PlotSelectionArea;
import ca.nanometrics.gflot.client.PlotWithInteractiveLegend;
import ca.nanometrics.gflot.client.Series;
import ca.nanometrics.gflot.client.SeriesHandler;
import ca.nanometrics.gflot.client.SimplePlot;
import ca.nanometrics.gflot.client.Tick;
import ca.nanometrics.gflot.client.event.PlotClickListener;
import ca.nanometrics.gflot.client.event.PlotHoverListener;
import ca.nanometrics.gflot.client.event.PlotItem;
import ca.nanometrics.gflot.client.event.PlotPosition;
import ca.nanometrics.gflot.client.event.PlotSelectedListener;
import ca.nanometrics.gflot.client.jsni.Plot;
import ca.nanometrics.gflot.client.options.AbstractAxisOptions.AxisPosition;
import ca.nanometrics.gflot.client.options.AxisOptions;
import ca.nanometrics.gflot.client.options.BarSeriesOptions;
import ca.nanometrics.gflot.client.options.GridOptions;
import ca.nanometrics.gflot.client.options.LegendOptions;
import ca.nanometrics.gflot.client.options.LineSeriesOptions;
import ca.nanometrics.gflot.client.options.Marking;
import ca.nanometrics.gflot.client.options.Markings;
import ca.nanometrics.gflot.client.options.PlotOptions;
import ca.nanometrics.gflot.client.options.PointsSeriesOptions;
import ca.nanometrics.gflot.client.options.Range;
import ca.nanometrics.gflot.client.options.SelectionOptions;
import ca.nanometrics.gflot.client.options.SelectionOptions.SelectionMode;
import ca.nanometrics.gflot.client.options.TimeSeriesAxisOptions;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A TimeSeriesPlot is a plot with one or more time series displayed on it.
 * 
 * @author Estelle DUMAS : estelle.dumas@karrus-its.com
 */
public class TimeSeriesPlot extends Composite  {

	private SimplePlot plot;
	private PlotWithInteractiveLegend plotWithInteractiveLegend;
	private VerticalPanel mainContainerVerticalPanel;
	private VerticalPanel plotPanel;
	private Date minDate;
	private Date maxDate;
	private int width;
	private int height;
	private PlotType plotType;
	private Map<TimeSeries, TimeSeriesInfo> timeSeriesInfos = new LinkedHashMap<TimeSeries, TimeSeriesInfo>();
	private Map<DoubleTimeSeries, TimeSeriesInfo> doubleTimeSeriesInfos = new LinkedHashMap<DoubleTimeSeries, TimeSeriesInfo>();
	private Map<String, PlotClickMessageFormatter> seriesToPlotClickMessageFormatter = new HashMap<String, PlotClickMessageFormatter>();
	private List<TimeSeriesInfo> emptyTimeSeries = new ArrayList<TimeSeriesInfo>();
	private List<Double> filteredValues = new ArrayList<Double>();
	private Map<TimeSeries, SeriesHandler> timeSeries2seriesHandler = new LinkedHashMap<TimeSeries, SeriesHandler>();
	private Map<DoubleTimeSeries, SeriesHandler> doubleTimeSeries2seriesHandler = new LinkedHashMap<DoubleTimeSeries, SeriesHandler>();
	private Button unzoomButton;
	// empty plot
	final double[] xMinMaxOriginal = {Double.NaN, Double.NaN};
	private boolean isZoomEnabled;
	private double yMin;
	private double yMax;
	private Map<SeriesHandler, Boolean> handler2visible = new LinkedHashMap<SeriesHandler, Boolean>();
	private boolean isMultipleAxis;
	private boolean isPlotWithInteractivLegend = false;
	private boolean isLegendShownOnGraph = false;
	private Button autoScaleButton;
	//	private Date previousClickTime = null; // for simulating the double click

	public TimeSeriesPlot(int width, int height, PlotType plotType, boolean isZoomEnabled) {
		this(width, height, null, null, plotType, isZoomEnabled, false, false);
	}
	
	public TimeSeriesPlot(int width, int height, PlotType plotType, boolean isZoomEnabled, boolean isMultipleAxis, boolean isPlotWithInteractivLegend) {
		this(width, height, null, null, plotType, isZoomEnabled, isMultipleAxis, isPlotWithInteractivLegend);
	}
	
	public TimeSeriesPlot(String title, int width, int height, PlotType plotType, boolean isZoomEnabled, boolean isMultipleAxis, boolean isPlotWithInteractivLegend) {
		this(title, width, height, null, null, plotType, isZoomEnabled, false, isMultipleAxis, isPlotWithInteractivLegend);
	}
	
	public TimeSeriesPlot(String title, int width, int height, PlotType plotType, boolean isZoomEnabled, boolean isLegendShownOnGraph) {
		this(title, width, height, null, null, plotType, isZoomEnabled, isLegendShownOnGraph, false, false);
	}
	
	public TimeSeriesPlot(String title, int width, int height, PlotType plotType, boolean isZoomEnabled, boolean isLegendShownOnGraph, boolean isMultipleAxis, boolean isPlotWithInteractivLegend) {
		this(title, width, height, null, null, plotType, isZoomEnabled, isLegendShownOnGraph, isMultipleAxis, isPlotWithInteractivLegend);
	}
	
	public TimeSeriesPlot(int width, int height, Date minDate, Date maxDate, PlotType plotType, boolean isZoomEnabled, boolean isMultipleAxis, boolean isPlotWithInteractivLegend) {
		this("", width, height, minDate, maxDate, plotType, isZoomEnabled, false, isMultipleAxis, isPlotWithInteractivLegend);
	}

	public TimeSeriesPlot(String title, int width, int height, Date minDate, Date maxDate, PlotType plotType, boolean isZoomEnabled, boolean isLegendShownOnGraph, boolean isMultipleAxis, boolean isPlotWithInteractivLegend) {
		
		this.minDate = minDate;
		this.maxDate = maxDate;
		this.width = width;
		this.height = height;
		this.plotType = plotType;
		this.isLegendShownOnGraph = isLegendShownOnGraph;
		this.isZoomEnabled = isZoomEnabled;
		this.isMultipleAxis = isMultipleAxis;
		this.isPlotWithInteractivLegend = isPlotWithInteractivLegend;
		plotPanel = new VerticalPanel();
		if (!title.equals(""))
			setTitle(title);
		plotPanel.add(new HTML("&nbsp"));
		plotPanel.addStyleName("moreMargin");
		plotPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		plotPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		mainContainerVerticalPanel = new VerticalPanel();
		mainContainerVerticalPanel.add(plotPanel);
		this.initWidget(mainContainerVerticalPanel);
		autoScaleButton = new Button(Language.autoScaleButton);
		autoScaleButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				autoScale();
			}
		});
		mainContainerVerticalPanel.add(autoScaleButton);
		unzoomButton = new Button("zoom out");
		unzoomButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				zoomOutAction();
				//plot.zoomOut();
			}
		});
		unzoomButton.setEnabled(false);
		Button saveImageButton = new Button("save image");
		saveImageButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if ( plot.isExportAsImageEnabled() )
				{
					plot.saveAsImage();
				}
				else
				{
					Window.alert( "The export A option isn't supported by your browser." );
				}
			}
		});
		//		panel.add(saveImageButton);
		instantiatePlot();
	}

	private void autoScale() {
		plot.getPlotOptions().getYAxisOptions(1).setMaximum(0);
		plot.getPlotOptions().getYAxisOptions(1).setMaximum(100);
		plot.redraw();
	}
	
	private void zoomOutAction(){
		plot.getPlotOptions().getXAxisOptions().setMinimum(xMinMaxOriginal[0]);
		plot.getPlotOptions().getXAxisOptions().setMaximum(xMinMaxOriginal[1]);
		plot.clearSelection();
		plot.redraw();
		unzoomButton.setEnabled(false);
	}

	/**
	 * Creates the plot : sets the axis, width, height...
	 */
	private void instantiatePlot() {
		
		// x-axis option
		TimeSeriesAxisOptions xAxis = new TimeSeriesAxisOptions();
		if (minDate!=null)
			xAxis.setMinimum(minDate.getTime());
		if (maxDate!=null)
			xAxis.setMaximum(maxDate.getTime());
		if (!new Double(xMinMaxOriginal[0]).equals(Double.NaN))
			xAxis.setMinimum(xMinMaxOriginal[0]);
		if (!new Double(xMinMaxOriginal[1]).equals(Double.NaN))
			xAxis.setMaximum(xMinMaxOriginal[1]);
		// options 
		PlotOptions plotOptions = new PlotOptions();
		plotOptions.addXAxisOptions(xAxis);
		// y-axis option
		//??--		int cpt = 0;
		//		for (TimeSeries timeSeries : timeSeriesInfos.keySet()){
		//			TimeSeriesInfo timeSeriesInfo = timeSeriesInfos.get(timeSeries);
		//			AxisOptions axisOptionY = new AxisOptions();
		//			axisOptionY.setLabel(timeSeriesInfo.getYLabel());
		//			//			axisOptionY.setLabelWidth(20);
		//			double minValue = timeSeriesInfo.getMinValue();
		//			double maxValue = timeSeriesInfo.getMaxValue();
		//			yMin = Math.min(yMin, minValue);
		//			yMax = Math.max(yMax, maxValue);
		//			if (!Double.isNaN(minValue))
		//				axisOptionY.setMinimum(minValue);
		//			else {
		//				minValue = -1;
		//				axisOptionY.setMinimum(minValue);	
		//			}
		//			if (!Double.isNaN(maxValue))
		//				axisOptionY.setMaximum(maxValue);
		//			axisOptionY.setPosition((cpt%2==0)?AxisPosition.LEFT:AxisPosition.RIGHT);
		//			plotOptions.addYAxisOptions(axisOptionY);
		//			cpt ++;
		//--??		}
		// y-axis option
		List<Integer> axis = new ArrayList<Integer>();
		for (TimeSeries timeSeries : timeSeriesInfos.keySet()){
			TimeSeriesInfo timeSeriesInfo = timeSeriesInfos.get(timeSeries);
			if (!axis.contains(timeSeriesInfo.getyAxis())) {
				AxisOptions axisOptionY = new AxisOptions();
				axisOptionY.setLabel(timeSeriesInfo.getYLabel());
				//			axisOptionY.setLabelWidth(20);
				double minValue = timeSeriesInfo.getMinValue();
				double maxValue = timeSeriesInfo.getMaxValue();
				yMin = Math.min(yMin, minValue);
				yMax = Math.max(yMax, maxValue);
				if (Double.isNaN(minValue))
					minValue = -1;
				axisOptionY.setMinimum(minValue);	
				if (!Double.isNaN(maxValue))
					axisOptionY.setMaximum(maxValue);
				if (timeSeriesInfos.size()==1) {
					if (timeSeriesInfo.getyAxis()==2)
						axisOptionY.setPosition(AxisPosition.LEFT);
				}
				else
					axisOptionY.setPosition((timeSeriesInfo.getyAxis()==1?AxisPosition.LEFT:AxisPosition.RIGHT));
				axisOptionY.setReserveSpace(true);
				axisOptionY.setShow(true);
				plotOptions.addYAxisOptions(axisOptionY);
				axis.add(timeSeriesInfo.getyAxis());
			}
		}
		for (DoubleTimeSeries timeSeries : doubleTimeSeriesInfos.keySet()){
			TimeSeriesInfo timeSeriesInfo = doubleTimeSeriesInfos.get(timeSeries);
			if (!axis.contains(timeSeriesInfo.getyAxis())) {
				AxisOptions axisOptionY = new AxisOptions();
				axisOptionY.setLabel(timeSeriesInfo.getYLabel());
				//			axisOptionY.setLabelWidth(20);
				double minValue = timeSeriesInfo.getMinValue();
				double maxValue = timeSeriesInfo.getMaxValue();
				yMin = Math.min(yMin, minValue);
				yMax = Math.max(yMax, maxValue);
				if (Double.isNaN(minValue))
					minValue = -1;
				axisOptionY.setMinimum(minValue);
				if (!Double.isNaN(maxValue))
					axisOptionY.setMaximum(maxValue);
				if (doubleTimeSeriesInfos.size()==1) {
					if (timeSeriesInfo.getyAxis()==2)
						axisOptionY.setPosition(AxisPosition.LEFT);
				}
				else
					axisOptionY.setPosition((timeSeriesInfo.getyAxis()==1?AxisPosition.LEFT:AxisPosition.RIGHT));
				axisOptionY.setReserveSpace(true);
				axisOptionY.setShow(true);
				plotOptions.addYAxisOptions(axisOptionY);
				axis.add(timeSeriesInfo.getyAxis());
			}
		}
		plotOptions.setLegendOptions(new LegendOptions().setShow(isLegendShownOnGraph));
		if (isZoomEnabled)
			plotOptions.setSelectionOptions(new SelectionOptions().setMode(SelectionMode.X).setColor("gray"));
		// markings
		Markings markings = new Markings();
		for (Iterator<TimeSeries> iterator = timeSeriesInfos.keySet().iterator(); iterator.hasNext();) {
			TimeSeries currentTimeSerie = iterator.next();
			if (timeSeriesInfos.get(currentTimeSerie).isMarking())
				markings.addMarkings(checkMarkings(currentTimeSerie, -1.0/60, Color.colorRedLight));
		}
		plotOptions.setGridOptions(new GridOptions().setMarkings(markings).setShow(true).setHoverable(true).setClickable(true));
		// plot
		plot = new SimplePlot(plotOptions);
		if (isZoomEnabled)
			plot.addSelectedListener(new PlotSelectedListener() {
				@Override
				public void onPlotSelected(PlotSelectionArea selectedArea) {
					double xFrom = selectedArea.getX().getFrom();
					double xTo = selectedArea.getX().getTo();
					plot.getPlotOptions().getXAxisOptions().setMinimum(xFrom);
					plot.getPlotOptions().getXAxisOptions().setMaximum(xTo);
					plot.clearSelection();
					plot.redraw();
					unzoomButton.setEnabled(true);
				}
			});
		plot.addDomHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				zoomOutAction();
			}
		}, DoubleClickEvent.getType());
		final PopupPanel popupPanel = new PopupPanel();
		final Label popupLabel = new Label();
		popupPanel.add(popupLabel);
		plot.addClickListener(new PlotClickListener() {
			@Override
			public void onPlotClick(Plot plot, PlotPosition position, PlotItem item) {
				if ( item != null && !item.getSeries().getLineSeriesOptions().getSteps() && !item.getSeries().getBarSeriesOptions().getShow() && !item.getSeries().getLineSeriesOptions().getFillBoolean()){
					if (seriesToPlotClickMessageFormatter.get(item.getSeries().getLabel()) != null) {
						popupLabel.setText(seriesToPlotClickMessageFormatter.get(item.getSeries().getLabel()).getMessage(plot, position, item));
						popupPanel.setPopupPosition( item.getPageX() + 10, item.getPageY() - 25 );
						popupPanel.show();
					}
				}
				else {
					popupPanel.hide();
				}	    
			}
		}, true);
		plot.addHoverListener(new PlotHoverListener() {
			@Override
			public void onPlotHover(Plot plot, PlotPosition position, PlotItem item) {
				if (item==null)
					popupPanel.hide();
			}
		}, false);
		plot.setHeight(height-25);
		plot.setWidth(width);
		if (plot.isExportAsImageEnabled())
			plot.saveAsImage();
	}
	
	private Marking[] checkMarkings(TimeSeries currentTimeSerie, double valueToCheck, String color){
		
		Date initDate = null;
		Date finalDate = null;
		Date previousDate = null;
		List<Marking> markings = new ArrayList<Marking>();
		for (Date currentDate : currentTimeSerie.keySet()) {
			if  (currentTimeSerie.get(currentDate)==valueToCheck) {
				if (initDate==null)
					initDate = currentDate;
				previousDate = currentDate;
			}
			else {
				if (initDate!=null) {
					finalDate = previousDate;
					Marking marking = new Marking();
					marking.setColor(color);
					marking.setX(new Range(getGWTDateTimeOrNot(initDate).getTime(), getGWTDateTimeOrNot(finalDate).getTime()));
					marking.setY(new Range(yMin, yMax));
					markings.add(marking);
					initDate = null;
					finalDate = null;
				}
			}
		}
		if (initDate!=null){
			finalDate = previousDate;
			Marking marking = new Marking();
			marking.setColor(color);
			marking.setX(new Range(getGWTDateTimeOrNot(initDate).getTime(), getGWTDateTimeOrNot(finalDate).getTime()));
			marking.setY(new Range(yMin, yMax));
			markings.add(marking);
			initDate = null;
			finalDate = null;
		}
		
		Marking[] res = new Marking[markings.size()];
		for (int i=0; i<markings.size(); i++)
			res[i] = markings.get(i);
		return res;
	}

	public void setPlotType(PlotType plotType){
		this.plotType = plotType;
	}

	public void disableDecimalFromY(){
		plot.getPlotOptions().addYAxisOptions(new AxisOptions().setTickDecimals(0));
	}

	public void addTimeSeries(TimeSeries timeSerie, String legendTitle, String color, String yLabel, double minValue, double maxValue, String dataUnit, PlotClickMessageFormatter plotClickMessageFormatter){
		addTimeSeries(timeSerie, legendTitle, color, yLabel, minValue, maxValue, dataUnit, plotClickMessageFormatter, null, false, false, false);
	}

	public void addTimeSeries(TimeSeries timeSerie, String legendTitle, String color, String yLabel, double minValue, double maxValue, String dataUnit, PlotClickMessageFormatter plotClickMessageFormatter, 
			                  PlotType plotType, boolean isUnknownMarking, boolean isNightMarking, boolean isMissingDataMarking) {
		addTimeSeries(timeSerie, legendTitle, color, yLabel, minValue, maxValue, dataUnit, plotClickMessageFormatter, plotType, isUnknownMarking, isNightMarking, isMissingDataMarking, 1);
	}

	public void addTimeSeries(TimeSeries timeSerie, String legendTitle, String color, String yLabel, double minValue, double maxValue, String dataUnit, PlotClickMessageFormatter plotClickMessageFormatter, 
			                  PlotType plotType, boolean isUnknownMarking, boolean isNightMarking, boolean isMissingDataMarking, int yAxis) {
		addTimeSeries(timeSerie, legendTitle, color, yLabel, minValue, maxValue, dataUnit, plotClickMessageFormatter, plotType, isUnknownMarking, isNightMarking, isMissingDataMarking, yAxis, true, null);
	}
	
	/**
	 * Adds a TimeSeries to the plot with the selected color.
	 * @param timeSerie TimeSeries
	 * @param legendTitle String
	 * @param color String
	 */
	public void addTimeSeries(TimeSeries timeSerie, String legendTitle, String color, String yLabel, double minValue, double maxValue, String dataUnit, PlotClickMessageFormatter plotClickMessageFormatter,
			                  PlotType plotType, boolean isUnknownMarking, boolean isNightMarking, boolean isMissingDataMarking, int yAxis, boolean isDisplayed, Tick[] ticks) {
		
		// check data validity
		if (timeSerie==null){
			TimeSeriesInfo newTimeSeriesInfo = new TimeSeriesInfo(legendTitle, color, yLabel, plotType, minValue, maxValue, isUnknownMarking, yAxis, isDisplayed, ticks, dataUnit, plotClickMessageFormatter);
			if (emptyTimeSeries.size()==0) 
				emptyTimeSeries.add(newTimeSeriesInfo);
			else {
				boolean isAlreadyPresent = false;
				for (TimeSeriesInfo t : emptyTimeSeries)
					if (t.equals(newTimeSeriesInfo)){
						isAlreadyPresent = true;
						break;
					}
				if (!isAlreadyPresent)
					emptyTimeSeries.add(newTimeSeriesInfo);
			}
			//			if (!emptyTimeSeries.contains(newTimeSeriesInfo))
			//				emptyTimeSeries.add(newTimeSeriesInfo);
			if (plot!=null)
				plotPanel.remove(plot);
			if (plotWithInteractiveLegend!=null)
				plotPanel.remove(plotWithInteractiveLegend);
			instantiatePlot();
			for (TimeSeriesInfo timeSeriesInfo : emptyTimeSeries)
				plot.getModel().addSeries(timeSeriesInfo.getTitle(), timeSeriesInfo.getColor());
			if (isPlotWithInteractivLegend){
				plotWithInteractiveLegend = new PlotWithInteractiveLegend(plot);
				plotPanel.add(plotWithInteractiveLegend);
			}
			else
				plotPanel.add(plot);
			return;
		}
		if (timeSerie.size()==0)
			return;
		// save data
		this.timeSeriesInfos.put(timeSerie, new TimeSeriesInfo(legendTitle, color, yLabel, plotType, minValue, maxValue, isUnknownMarking, yAxis, isDisplayed,ticks, dataUnit, plotClickMessageFormatter));
		// remove previous plot
		if (plotWithInteractiveLegend!=null)
			plotPanel.remove(plotWithInteractiveLegend);
		if (plot!=null)
			plotPanel.remove(plot);
		// updates
		Date minDate = timeSerie.getFirstDate();
		Date maxDate = timeSerie.getLastDate();
		if (new Double(xMinMaxOriginal[0]).equals(Double.NaN))
			xMinMaxOriginal[0] = getGWTDateTimeOrNot(minDate).getTime();
		else 
			xMinMaxOriginal[0] = Math.min(xMinMaxOriginal[0],getGWTDateTimeOrNot(minDate).getTime());
		if (new Double(xMinMaxOriginal[1]).equals(Double.NaN))
			xMinMaxOriginal[1] = getGWTDateTimeOrNot(maxDate).getTime();
		else
			xMinMaxOriginal[1] = Math.max(xMinMaxOriginal[1],getGWTDateTimeOrNot(maxDate).getTime());
		// instantiate plot
		instantiatePlot();
		// create all series
//		PlotModel model = plot.getModel();
//
//		plotTimeSeries(model);
//		plotDoubleTimeSeries(model);
//
//
//		// add plot
//		if (isPlotWithInteractivLegend){
//			plotWithInteractiveLegend = new PlotWithInteractiveLegend(plot);
//			for (Series seriesOnPlot : plotWithInteractiveLegend.getModel().getSeries()){
//				for (SeriesHandler s : handler2visible.keySet()){
//					if (s.getSeries().equals(seriesOnPlot))
//						plotWithInteractiveLegend.setSerieVisible(s, handler2visible.get(s), false);
//				}
//			}
//			panel.add(plotWithInteractiveLegend);
//		}
//		else
//			panel.add(plot);
//		//		if (isZoomEnabled)
//		//			panel.add(unzoomButton);
	}

	public void addDoubleTimeSeries(DoubleTimeSeries doubleTimeSerie, String legendTitle, String color, String yLabel, double minValue, double maxValue, PlotType plotType, boolean isUnknwonMarking,
			                        boolean isNightMarking, boolean isMissingDataMarking, int yAxis) {
		addDoubleTimeSeries(doubleTimeSerie, legendTitle, color, yLabel, minValue, maxValue, plotType, isUnknwonMarking, isNightMarking, isMissingDataMarking, yAxis, true, null);
	}
	
	public void addDoubleTimeSeries(DoubleTimeSeries doubleTimeSerie, String legendTitle, String color, String yLabel, double minValue, double maxValue, PlotType plotType, boolean isUnknwonMarking,
			                        boolean isNightMarking, boolean isMissingDataMarking, int yAxis, boolean isDisplayed, Tick[] ticks) {

		if (doubleTimeSerie.size()==0)
			return;
		// save data
		this.doubleTimeSeriesInfos.put(doubleTimeSerie, new TimeSeriesInfo(legendTitle, color, yLabel, plotType, minValue, maxValue, isUnknwonMarking, yAxis, isDisplayed, ticks, "", null));
		// remove previous plot
		if (plotWithInteractiveLegend!=null)
			plotPanel.remove(plotWithInteractiveLegend);
		if (plot!=null)
			plotPanel.remove(plot);
		// updates
		Date minDate = doubleTimeSerie.getFirstDate();
		Date maxDate = doubleTimeSerie.getLastDate();
		if (new Double(xMinMaxOriginal[0]).equals(Double.NaN))
			xMinMaxOriginal[0] = getGWTDateTimeOrNot(minDate).getTime();
		else 
			xMinMaxOriginal[0] = Math.min(xMinMaxOriginal[0],getGWTDateTimeOrNot(minDate).getTime());
		if (new Double(xMinMaxOriginal[1]).equals(Double.NaN))
			xMinMaxOriginal[1] = getGWTDateTimeOrNot(maxDate).getTime();
		else
			xMinMaxOriginal[1] = Math.max(xMinMaxOriginal[1],getGWTDateTimeOrNot(maxDate).getTime());
		// instantiate plot
		instantiatePlot();
	}
	
	public void plot(){

		// create all series
		PlotModel model = plot.getModel();
		plotTimeSeries(model);
		plotDoubleTimeSeries(model);
		// add plot
		if (isPlotWithInteractivLegend){
			plotWithInteractiveLegend = new PlotWithInteractiveLegend(plot);
			for (Series seriesOnPlot : plotWithInteractiveLegend.getModel().getSeries()){
				for (SeriesHandler s : handler2visible.keySet()){
					if (s.getSeries().equals(seriesOnPlot))
						plotWithInteractiveLegend.setSerieVisible(s, handler2visible.get(s), false);
				}
			}
			plotPanel.add(plotWithInteractiveLegend);
		}
		else
			plotPanel.add(plot);
		//		if (isZoomEnabled)
		//			panel.add(unzoomButton);

	}

	private void plotTimeSeries(PlotModel model) {
		for (Iterator<TimeSeries> iterator = timeSeriesInfos.keySet().iterator(); iterator.hasNext();) {
			TimeSeries currentTimeSerie = iterator.next();
			TimeSeriesInfo currentTimeSerieInfo = timeSeriesInfos.get(currentTimeSerie);
			PlotType plotTyp = currentTimeSerieInfo.getPlotType();
			boolean isSeriesHandlerVisible = currentTimeSerieInfo.isDisplayed();
			Series series = new Series(currentTimeSerieInfo.getTitle()).setColor(currentTimeSerieInfo.getColor());
			seriesToPlotClickMessageFormatter.put(currentTimeSerieInfo.getTitle(), currentTimeSerieInfo.getPlotClickMessageFormatter());
			series = setSeriesOptions(series, plotTyp, currentTimeSerieInfo.getColor());	
			if (isMultipleAxis) {
				if (timeSeriesInfos.size()==1) {
					if (currentTimeSerieInfo.getyAxis()==2) 
						series.setYAxis(1);
				}
				else 
					series.setYAxis(currentTimeSerieInfo.getyAxis());
			}
			SeriesHandler handler = model.addSeries(series);
			for (Date currentDate : currentTimeSerie.keySet()){
				if (filteredValues.contains(currentTimeSerie.get(currentDate)))
					continue;
				//				DataPoint d = new DataPoint(Date.UTC(currentDate.getYear(), currentDate.getMonth(), currentDate.getDate(), currentDate.getHours(), currentDate.getMinutes(), currentDate.getSeconds()), currentTimeSerie.get(currentDate));
				if (currentTimeSerie.get(currentDate)!=-1.0/60 && currentTimeSerie.get(currentDate)!=-2.0/60 && currentTimeSerie.get(currentDate)!=-3.0/60) {
					DataPoint d = new DataPoint(getGWTDateTimeOrNot(currentDate).getTime(), currentTimeSerie.get(currentDate)); 
					handler.add(d);
				}
			}
			timeSeries2seriesHandler.put(currentTimeSerie, handler);
			handler.setVisible(isSeriesHandlerVisible);
			handler2visible.put(handler, isSeriesHandlerVisible);
		}
	}

	private void plotDoubleTimeSeries(PlotModel model){
		
		for (Iterator<DoubleTimeSeries> iterator = doubleTimeSeriesInfos.keySet().iterator(); iterator.hasNext();) {
			DoubleTimeSeries currentDoubleTimeSerie = iterator.next();
			TimeSeriesInfo currentTimeSerieInfo = doubleTimeSeriesInfos.get(currentDoubleTimeSerie);
			PlotType plotTyp = currentTimeSerieInfo.getPlotType();
			boolean isSeriesHandlerVisible = currentTimeSerieInfo.isDisplayed();
			Series series = new Series(currentTimeSerieInfo.getTitle()).setColor(currentTimeSerieInfo.getColor());
			seriesToPlotClickMessageFormatter.put(currentTimeSerieInfo.getTitle(), currentTimeSerieInfo.getPlotClickMessageFormatter());
			series = setSeriesOptions(series, plotTyp, currentTimeSerieInfo.getColor());	
			if (isMultipleAxis) {
				if (timeSeriesInfos.size()==1) {
					if (currentTimeSerieInfo.getyAxis()==2) 
						series.setYAxis(1);
				}
				else 
					series.setYAxis(currentTimeSerieInfo.getyAxis());
			}
			SeriesHandler handler = model.addSeries(series);
			for (StripeData stripeData : currentDoubleTimeSerie.getData()){
				if (stripeData.getUpValue()!=-1.0/60 && stripeData.getDownValue()!=-1.0/60
						&& stripeData.getUpValue()!=-2.0/60 && stripeData.getDownValue()!=-2.0/60
						&& stripeData.getUpValue()!=-3.0/60 && stripeData.getDownValue()!=-3.0/60) {
					DataPoint d = new DataPoint(getGWTDateTimeOrNot(stripeData.getDate()).getTime(), stripeData.getUpValue(), stripeData.getDownValue()); 
					handler.add(d);
				}
			}
			doubleTimeSeries2seriesHandler.put(currentDoubleTimeSerie, handler);
			handler.setVisible(isSeriesHandlerVisible);
			handler2visible.put(handler, isSeriesHandlerVisible);
		}
	}

	private Series setSeriesOptions(Series series, PlotType plotTyp, String color){
		
		if (plotTyp!=null) {
			if (plotTyp.equals(PlotType.POINT))
				series.setPointsOptions(new PointsSeriesOptions().setLineWidth(1).setShow(true).setFill(true).setFillColor(color).setRadius(2));
			if (plotTyp.equals(PlotType.BAR)) {
				series.setHoverable(false).setClickable(false);
				series.setBarsSeriesOptions(new BarSeriesOptions().setLineWidth(1).setShow(true).setBarWidth(5*60*1000));
			}	
			if (plotTyp.equals(PlotType.LINE))
				series.setLineSeriesOptions(new LineSeriesOptions().setShow(true).setLineWidth(1));
			if (plotTyp.equals(PlotType.STEP)) {
				series.setHoverable(false).setClickable(false);
				series.setLineSeriesOptions(new LineSeriesOptions().setShow(true).setLineWidth(3).setSteps(true));
			}	
			if (plotTyp.equals(PlotType.STEP_FILL)) {
				series.setHoverable(false).setClickable(false);
				series.setLineSeriesOptions(new LineSeriesOptions().setShow(true).setLineWidth(1).setSteps(true).setFill(true).setFill(1));
			}	
			if (plotTyp.equals(PlotType.FILL)) { 
				series.setHoverable(false).setClickable(false);
				series.setLineSeriesOptions(new LineSeriesOptions().setShow(true).setLineWidth(0).setFill(true));
			}	
		}
		else {
			if (this.plotType.equals(PlotType.POINT))
				series.setPointsOptions(new PointsSeriesOptions().setLineWidth(1).setShow(true).setFill(true).setFillColor(color).setRadius(2));
			if (this.plotType.equals(PlotType.BAR))
				series.setBarsSeriesOptions(new BarSeriesOptions().setLineWidth(1).setShow(true).setBarWidth(5*60*1000));
			if (this.plotType.equals(PlotType.LINE))
				series.setLineSeriesOptions(new LineSeriesOptions().setShow(true).setLineWidth(1));
			if (this.plotType.equals(PlotType.STEP))
				series.setLineSeriesOptions(new LineSeriesOptions().setShow(true).setLineWidth(3).setSteps(true));
			if (this.plotType.equals(PlotType.STEP_FILL))
				series.setLineSeriesOptions(new LineSeriesOptions().setShow(true).setLineWidth(1).setSteps(true).setFill(true));
			if (this.plotType.equals(PlotType.FILL)) 
				series.setLineSeriesOptions(new LineSeriesOptions().setShow(true).setLineWidth(0).setFill(true));
		}
		return series;
	}
	@SuppressWarnings("deprecation")
	public static Date getGWTDateTimeOrNot(Date d){
		return (new Date(d.getTime()-d.getTimezoneOffset()*60*1000));
		//		return d;
//		return new Date(Date.UTC(d.getYear(), d.getMonth(), d.getDate(), d.getHours(), d.getMinutes(), d.getSeconds()));
		//		return new Date(d.getTime()+d.getTimezoneOffset()*60*1000);
	}

	/**
	 * Adds a TimeSeries (made of dates and values) to the plot with the selected color.
	 * @param dates List<Date>
	 * @param values List<Double>
	 * @param color String
	 * @throws Exception 
	 */
	public void addTimeSeries(List<Date> dates, List<Double> values, String legendTitle, String color, String yLabel, double minValue, double maxValue) throws Exception {
		addTimeSeries(new TimeSeries(dates, values), legendTitle, color, yLabel, minValue, maxValue, "", null);
	} 

	public void addTimeSeries(List<Date> dates, List<Double> values, String title, String color) throws Exception {
		addTimeSeries(new TimeSeries(dates, values), title, color);
	}


	public void addTimeSeries(TimeSeries timeSeries, String title, String color){
		addTimeSeries(timeSeries, title, color, "", 0, 100, "", null);
	}

	/**
	 * Adds a value to filter.
	 * @param filteredValue double
	 */
	public void addFilteredValue(Double filteredValue) {
		
		if (!filteredValues.contains(filteredValue))
			filteredValues.add(filteredValue);
	}

	/**
	 * Removes a value to filter.
	 * @param filteredValue double
	 */
	public void removeFilteredValue(Double filteredValue) {
		filteredValues.remove(filteredValue);
	}

	private List<TimeSeries> getTimeSeriesClone() {
		
		List<TimeSeries> toReturn = new ArrayList<TimeSeries>();
		Set<TimeSeries> timeSeries = timeSeriesInfos.keySet();
		for (TimeSeries tSeries : timeSeries)
			toReturn.add(tSeries);
		return toReturn;
	}

	private List<String> getTitlesClone() {
		
		List<String> toReturn = new ArrayList<String>();
		Set<TimeSeries> timeSeries = timeSeriesInfos.keySet();
		for (TimeSeries tSeries : timeSeries)
			toReturn.add(timeSeriesInfos.get(tSeries).getTitle());
		return toReturn;
	}


	private List<String> getColorsClone() {
		
		List<String> toReturn = new ArrayList<String>();
		Set<TimeSeries> timeSeries = timeSeriesInfos.keySet();
		for (TimeSeries tSeries : timeSeries)
			toReturn.add(timeSeriesInfos.get(tSeries).getColor());
		return toReturn;
	}

	private List<String> getYLabelsClone() {
		
		List<String> toReturn = new ArrayList<String>();
		Set<TimeSeries> timeSeries = timeSeriesInfos.keySet();
		for (TimeSeries tSeries : timeSeries)
			toReturn.add(timeSeriesInfos.get(tSeries).getYLabel());
		return toReturn;
	}
	private List<Double> getOldMinValuesClone() {
		
		List<Double> toReturn = new ArrayList<Double>();
		Set<TimeSeries> timeSeries = timeSeriesInfos.keySet();
		for (TimeSeries tSeries : timeSeries)
			toReturn.add(timeSeriesInfos.get(tSeries).getMinValue());
		return toReturn;
	}
	private List<Double> getOldMaxValuesClone() {
		
		List<Double> toReturn = new ArrayList<Double>();
		Set<TimeSeries> timeSeries = timeSeriesInfos.keySet();
		for (TimeSeries tSeries : timeSeries)
			toReturn.add(timeSeriesInfos.get(tSeries).getMaxValue());
		return toReturn;
	}
	private List<String> getOldDataUnitsClone() {
		List<String> toReturn = new ArrayList<String>();
		Set<TimeSeries> timeSeries = timeSeriesInfos.keySet();
		for (TimeSeries tSeries : timeSeries)
			toReturn.add(timeSeriesInfos.get(tSeries).getDataUnit());
		return toReturn;
	}
	private List<PlotClickMessageFormatter> getOldPlotClickMessageFormattersClone() {
		List<PlotClickMessageFormatter> toReturn = new ArrayList<PlotClickMessageFormatter>();
		Set<TimeSeries> timeSeries = timeSeriesInfos.keySet();
		for (TimeSeries tSeries : timeSeries)
			toReturn.add(timeSeriesInfos.get(tSeries).getPlotClickMessageFormatter());
		return toReturn;
	}
	//	public void zoomIn(){
	//		// this.width = this.width*2;
	//		this.height = this.height*2;
	//		refresh();
	//	}
	//
	//	public void zoomOut(){
	//		// this.width = this.width/2;
	//		this.height = this.height/2;
	//		refresh();
	//	}

	public void refresh() {
		List<TimeSeries> oldTimeSeries = getTimeSeriesClone();
		List<String> oldColors = getColorsClone();
		List<String> oldTitles = getTitlesClone();
		List<String> oldYLabels = getYLabelsClone();
		List<Double> oldMinValues = getOldMinValuesClone();
		List<Double> oldMaxValues = getOldMaxValuesClone();
		List<String> oldDataUnits = getOldDataUnitsClone();
		List<PlotClickMessageFormatter> oldPlotClickMessageFormatters = getOldPlotClickMessageFormattersClone();
		timeSeriesInfos.clear();
		timeSeries2seriesHandler.clear();
		for (int i = 0; i < oldTimeSeries.size(); i++)
			addTimeSeries(oldTimeSeries.get(i), oldTitles.get(i), oldColors.get(i), oldYLabels.get(i), oldMinValues.get(i), oldMaxValues.get(i), oldDataUnits.get(i), oldPlotClickMessageFormatters.get(i));
		unzoomButton.setEnabled(false);
	}

	public void clear() {
		timeSeriesInfos.clear();
		refresh();
	}


	/**
	 * Removes a timeSeries from the plot.
	 * @param timeSeries TimeSeries
	 */
	public void removeTimeSeries(TimeSeries timeSeries){
		
		SeriesHandler handler = timeSeries2seriesHandler.get(timeSeries);
		plot.getModel().removeSeries(handler);
		timeSeries2seriesHandler.remove(timeSeries);
		timeSeriesInfos.remove(timeSeries);
		if (timeSeries2seriesHandler.size()==0){
			plotPanel.remove(plotWithInteractiveLegend);
			instantiatePlot();
			plotPanel.add(plotWithInteractiveLegend);
		}
	}

	public Date getFirstDateInTimeSeries(){
		
		if (this.timeSeries2seriesHandler.size()==0)
			return null;
		Date date = new Date(); 
		for (TimeSeries timeSeries : this.timeSeries2seriesHandler.keySet()) {
			Date currentDate = timeSeries.getFirstDate();
			if (currentDate.before(date))
				date = currentDate;
		}
		return date;
	}

	public Date getLastDateInTimeSeries(){
		
		if (this.timeSeries2seriesHandler.size()==0)
			return null;
		Date date = getFirstDateInTimeSeries(); // initialisation bidon 
		for (TimeSeries timeSeries : this.timeSeries2seriesHandler.keySet()) {
			Date currentDate = timeSeries.getLastDate();
			if (currentDate.after(date))
				date = currentDate;
		}
		return date;
	}

	public void removeValuesBefore(Date firstDate) {
		for (TimeSeries timeSeries : this.timeSeries2seriesHandler.keySet())
			timeSeries.removeValuesBefore(firstDate);
	}

	public void removeValuesAfter(Date lastDate) {
		for (TimeSeries timeSeries : this.timeSeries2seriesHandler.keySet())
			timeSeries.removeValuesAfter(lastDate);
	}

	public void setTitle(Widget widget) {
		plotPanel.insert(widget, 0);	
	}

	public void setTitle(String title){
		
		Label titleLabel = new Label(title);
		titleLabel.setStyleName("titleLabel");
		setTitle(titleLabel);
	}

	public void setMultipleAxis(boolean isMultipleAxis){
		this.isMultipleAxis = isMultipleAxis;
	}
}
