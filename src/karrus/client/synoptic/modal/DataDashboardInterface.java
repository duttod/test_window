package karrus.client.synoptic.modal;

import karrus.client.generic.plot.CountDataTimeSeriesPlot;

public interface DataDashboardInterface {
	
	public void stopAction();
	public void startAction();
	public void addTimeSeriesForDataDashboard(CountDataTimeSeriesPlot timeSeriesPlot);
}

