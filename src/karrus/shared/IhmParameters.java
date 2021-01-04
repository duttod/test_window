package karrus.shared;

import java.io.Serializable;

public class IhmParameters implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private boolean devMode = false;
	private double minForTravelTimePlot;
	private double maxForTravelTimePlot;
	private double minForCountPlot;
	private double maxForCountPlot;
	private double minForOccupancyPlot;
	private double maxForOccupancyPlot;
	private double minForSpeedPlot;
	private double maxForSpeedPlot;
	private int dataPlotRefreshingRate;
	private String fullDayStart = "00:00:00";
	private String fullDayStop = "23:59:59";
	private String morningStart = "00:00:00";
	private String morningStop = "12:00:00";
	private String afternoonStart = "12:00:00";
	private String afternoonStop = "23:59:59";
	private int plotMaxTimeWindow = 48; 
	private boolean unknownMarkings = false;
	private boolean missingDataMarkings = false;
	private boolean nightMarkings = false;
	private String road = "";
	
	public IhmParameters(){}
	
	public IhmParameters(boolean devMode, double minForTravelTimePlot, double maxForTravelTimePlot, double minForCountPlot, double maxForCountPlot, double minForOccupancyPlot, 
			            double maxForOccupancyPlot, double minForSpeedPlot, double maxForSpeedPlot, int dataPlotRefreshingRate, String fullDayStart, String fullDayStop,
			            String morningStart, String morningStop, String afternoonStart, String afternoonStop, int plotMaxTimeWindow, boolean unknownMarkings, boolean missingDataMarkings,
			            boolean nightMarkings, String road) {
		
		this.setDevMode(devMode);
		this.minForTravelTimePlot = minForTravelTimePlot;
		this.maxForTravelTimePlot = maxForTravelTimePlot;
		this.minForCountPlot = minForCountPlot;
		this.maxForCountPlot = maxForCountPlot;
		this.minForOccupancyPlot = minForOccupancyPlot;
		this.maxForOccupancyPlot = maxForOccupancyPlot;
		this.minForSpeedPlot = minForSpeedPlot;
		this.maxForSpeedPlot = maxForSpeedPlot;
		this.dataPlotRefreshingRate = dataPlotRefreshingRate;
		this.fullDayStart = fullDayStart;
		this.fullDayStop = fullDayStop;
		this.morningStart = morningStart;
		this.morningStop = morningStop;
		this.afternoonStart = afternoonStart;
		this.afternoonStop = afternoonStop;
		this.plotMaxTimeWindow = plotMaxTimeWindow;
		this.unknownMarkings = unknownMarkings;
		this.missingDataMarkings = missingDataMarkings;
		this.nightMarkings = nightMarkings;
		this.road = road;
	}
	
	public boolean isDevMode() {
		return devMode;
	}
	
	public void setDevMode(boolean devMode) {
		this.devMode = devMode;
	}

	public double getMinForTravelTimePlot() {
		return minForTravelTimePlot;
	}
	
	public void setMinForTravelTimePlot(double minForTravelTimePlot) {
		this.minForTravelTimePlot = minForTravelTimePlot;
	}
	
	public double getMaxForTravelTimePlot() {
		return maxForTravelTimePlot;
	}
	
	public void setMaxForTravelTimePlot(double maxForTravelTimePlot) {
		this.maxForTravelTimePlot = maxForTravelTimePlot;
	}
	
	public double getMinForCountPlot() {
		return minForCountPlot;
	}
	
	public void setMinForCountPlot(double minForCountPlot) {
		this.minForCountPlot = minForCountPlot;
	}
	
	public double getMaxForCountPlot() {
		return maxForCountPlot;
	}
	
	public void setMaxForCountPlot(double maxForCountPlot) {
		this.maxForCountPlot = maxForCountPlot;
	}
	
	public double getMinForOccupancyPlot() {
		return minForOccupancyPlot;
	}
	
	public void setMinForOccupancyPlot(double minForOccupancyPlot) {
		this.minForOccupancyPlot = minForOccupancyPlot;
	}
	
	public double getMaxForOccupancyPlot() {
		return maxForOccupancyPlot;
	}
	
	public void setMaxForOccupancyPlot(double maxForOccupancyPlot) {
		this.maxForOccupancyPlot = maxForOccupancyPlot;
	}
	
	public double getMinForSpeedPlot() {
		return minForSpeedPlot;
	}
	
	public void setMinForSpeedPlot(double minForSpeedPlot) {
		this.minForSpeedPlot = minForSpeedPlot;
	}
	
	public double getMaxForSpeedPlot() {
		return maxForSpeedPlot;
	}
	
	public void setMaxForSpeedPlot(double maxForSpeedPlot) {
		this.maxForSpeedPlot = maxForSpeedPlot;
	}
	
	public int getDataPlotRefreshingRate() {
		return dataPlotRefreshingRate;
	}
	
	public void setDataPlotRefreshingRate(int dataPlotRefreshingRate) {
		this.dataPlotRefreshingRate = dataPlotRefreshingRate;
	}
	
	public String getFullDayStart() {
		return fullDayStart;
	}

	public void setFullDayStart(String fullDayStart) {
		this.fullDayStart = fullDayStart;
	}

	public String getFullDayStop() {
		return fullDayStop;
	}

	public void setFullDayStop(String fullDayStop) {
		this.fullDayStop = fullDayStop;
	}

	public String getMorningStart() {
		return morningStart;
	}

	public void setMorningStart(String morningStart) {
		this.morningStart = morningStart;
	}

	public String getMorningStop() {
		return morningStop;
	}

	public void setMorningStop(String morningStop) {
		this.morningStop = morningStop;
	}

	public String getAfternoonStart() {
		return afternoonStart;
	}

	public void setAfternoonStart(String afternoonStart) {
		this.afternoonStart = afternoonStart;
	}

	public String getAfternoonStop() {
		return afternoonStop;
	}

	public void setAfternoonStop(String afternoonStop) {
		this.afternoonStop = afternoonStop;
	}

	public int getPlotMaxTimeWindow() {
		return plotMaxTimeWindow;
	}

	public void setPlotMaxTimeWindow(int plotMaxTimeWindow) {
		this.plotMaxTimeWindow = plotMaxTimeWindow;
	}

	public boolean isUnknownMarkings() {
		return unknownMarkings;
	}

	public void setUnknownMarkings(boolean unknownMarkings) {
		this.unknownMarkings = unknownMarkings;
	}

	public boolean isMissingDataMarkings() {
		return missingDataMarkings;
	}

	public void setMissingDataMarkings(boolean missingDataMarkings) {
		this.missingDataMarkings = missingDataMarkings;
	}

	public boolean isNightMarkings() {
		return nightMarkings;
	}

	public void setNightMarkings(boolean nightMarkings) {
		this.nightMarkings = nightMarkings;
	}

	public String getRoad() {
		return road;
	}

	public void setRoad(String road) {
		this.road = road;
	}
}
