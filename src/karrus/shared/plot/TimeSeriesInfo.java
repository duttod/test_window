package karrus.shared.plot;

import java.io.Serializable;

import ca.nanometrics.gflot.client.Tick;
import karrus.client.generic.plot.PlotClickMessageFormatter;
import karrus.client.generic.plot.PlotType;

public class TimeSeriesInfo implements Serializable {
	
	private static final long serialVersionUID = -1299972965541248735L;
	private String title;
	private String color;
	private String yLabel;
	private PlotType plotType;
	private double minValue;
	private double maxValue;
	private boolean marking;
	private int yAxis; // default is 1 = left; other possibility = 2 : right
	private boolean isDisplayed;
	private Tick[] ticks;
	private String dataUnit;
	private PlotClickMessageFormatter plotClickMessageFormatter;

	public TimeSeriesInfo() {};

	public TimeSeriesInfo(String title, String color, String yLabel, PlotType plotType, double minValue, double maxValue, boolean marking, int yAxis,  boolean isDisplayed, Tick[] ticks, String dataUnit, 
						  PlotClickMessageFormatter plotClickMessageFormatter) {
		this.title = title;
		this.color = color;
		this.yLabel = yLabel;
		this.plotType = plotType;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.setMarking(marking);
		this.yAxis = yAxis;
		this.isDisplayed = isDisplayed;
		this.ticks = ticks;
		this.dataUnit = dataUnit;
		this.plotClickMessageFormatter = plotClickMessageFormatter;
	}

	public String getYLabel() {
		return yLabel;
	}

	public void setYLabel(String yLabel) {
		this.yLabel = yLabel;
	}

	public double getMinValue() {
		return minValue;
	}

	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public String getTitle(){
		return this.title;
	}
	
	public String getColor(){
		return this.color;
	}
	
	public PlotType getPlotType() {
		return plotType;
	}

	public void setPlotType(PlotType plotType) {
		this.plotType = plotType;
	}

	public boolean isMarking() {
		return marking;
	}

	public void setMarking(boolean marking) {
		this.marking = marking;
	}
	
	public int getyAxis() {
		return yAxis;
	}

	public void setyAxis(int yAxis) {
		this.yAxis = yAxis;
	}
	
	public Tick[] getTicks() {
		return ticks;
	}

	public void setTicks(Tick[] ticks) {
		this.ticks = ticks;
	}
	
	public String getDataUnit() {
		return dataUnit;
	}

	public void setDataUnit(String dataUnit) {
		this.dataUnit = dataUnit;
	}
	
	public PlotClickMessageFormatter getPlotClickMessageFormatter() {
		return plotClickMessageFormatter;
	}

	public void setPlotClickMessageFormatter(PlotClickMessageFormatter plotClickMessageFormatter) {
		this.plotClickMessageFormatter = plotClickMessageFormatter;
	}
	
	public boolean isDisplayed() {
		return isDisplayed;
	}

	public void setIsDisplayed(boolean isDisplayed) {
		this.isDisplayed = isDisplayed;
	}

	public boolean equals(TimeSeriesInfo timeSeriesInfo){
		return this.getTitle().equals(timeSeriesInfo.getTitle())
		&& this.getColor().equals(timeSeriesInfo.getColor()) 
		&& this.getYLabel().equals(timeSeriesInfo.getYLabel());
	}
}
