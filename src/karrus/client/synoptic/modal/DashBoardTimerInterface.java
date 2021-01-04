package karrus.client.synoptic.modal;

import karrus.client.generic.plot.CountDataTimeSeriesPlot;

public interface DashBoardTimerInterface {

	public void stop();
	public void start();
	public void addPlot(CountDataTimeSeriesPlot timeSeriesPlot);
	public void setTimeHorizon(int timeHorizon);
}
