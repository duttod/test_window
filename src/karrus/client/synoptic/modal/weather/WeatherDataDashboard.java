package karrus.client.synoptic.modal.weather;

import java.util.ArrayList;
import java.util.List;

import karrus.client.FrontalWebApp;
import karrus.client.generic.BlankPanel;
import karrus.client.generic.plot.CountDataTimeSeriesPlot;
import karrus.client.synoptic.modal.DashBoardTimerInterface;
import karrus.client.synoptic.modal.DataDashboardInterface;
import karrus.client.synoptic.modal.TimeHorizonSelector;
import karrus.client.synoptic.modal.TimeHorizonSelectorListener;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Creates a panel containing a plot of weather data.
 * The plots are displayed in real time.
 * The user can choose a time horizon.
 * 
 * @author estelle.dumas@karrus-its.com
 */
public class WeatherDataDashboard extends Composite implements DataDashboardInterface, TimeHorizonSelectorListener {

	private ScrollPanel plotScrollPanel;
	private VerticalPanel panel;
	private DashBoardTimerInterface weatherDataDashboardTimer;
	private List<CountDataTimeSeriesPlot> timeSeriesPlots = new ArrayList<CountDataTimeSeriesPlot>();
	private TimeHorizonSelector timeHorizonSelector;

	/**
	 * Constructor.
	 * @throws Exception 
	 */	
	public WeatherDataDashboard(String station, int widthForDashboardInTabPanel, int heightForDashboardInTabPanel) throws Exception {

		int timeHorizonInitial = 60*60*1000; // 1h in milliseconds
		weatherDataDashboardTimer = new WeatherDataDashboardTimer(station, FrontalWebApp.ihmParameters.getDataPlotRefreshingRate(), timeHorizonInitial);
		timeHorizonSelector = new TimeHorizonSelector();
		timeHorizonSelector.addTimeHorizonSelectorListener(this);
		timeHorizonSelector.setTimeHorizon(timeHorizonInitial);
		// plot panel
		plotScrollPanel = new ScrollPanel();
		VerticalPanel v = new VerticalPanel();
		v.add(new BlankPanel(widthForDashboardInTabPanel, 20));
		WeatherDataPanel weatherDataPanel = new WeatherDataPanel(this, widthForDashboardInTabPanel*90/100, heightForDashboardInTabPanel*95/100);
		v.add(weatherDataPanel);
		plotScrollPanel.add(v);
		plotScrollPanel.setSize(widthForDashboardInTabPanel+"px", heightForDashboardInTabPanel+"px");
		// composite layout		
		panel = new VerticalPanel();
		panel.add(timeHorizonSelector);
		panel.add(plotScrollPanel);
		panel.setCellHorizontalAlignment(timeHorizonSelector, HasHorizontalAlignment.ALIGN_CENTER);
		panel.setCellHorizontalAlignment(plotScrollPanel, HasHorizontalAlignment.ALIGN_CENTER);
		this.initWidget(panel);
		// start timer with default refreshing rate	
		startAction();
	}

	@Override
	public void onTimeHorizonSelection(int timeHorizon) {
		weatherDataDashboardTimer.setTimeHorizon(timeHorizon);
	}

	/**
	 * Starts the timer to display on real time plots.
	 */
	public void startAction(){
		weatherDataDashboardTimer.start();

	}

	/**
	 * Stops the timer.
	 */
	public void stopAction(){
		weatherDataDashboardTimer.stop();

	}

	@Override
	public void addTimeSeriesForDataDashboard(CountDataTimeSeriesPlot timeSeriesPlot) {
		// register plot to timer
		weatherDataDashboardTimer.addPlot(timeSeriesPlot);
		timeSeriesPlot.addFilteredValue(-1d);
		// make all the plots reactive to a change in the filter settings
		timeSeriesPlots.add(timeSeriesPlot);
	}
}
