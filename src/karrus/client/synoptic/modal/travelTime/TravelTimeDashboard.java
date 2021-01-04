package karrus.client.synoptic.modal.travelTime;

import java.util.ArrayList;
import java.util.List;

import karrus.client.FrontalWebApp;
import karrus.client.generic.BlankPanel;
import karrus.client.generic.CustomDialogBox;
import karrus.client.generic.plot.CountDataTimeSeriesPlot;
import karrus.client.generic.plot.PlotMinMax;
import karrus.client.synoptic.modal.DataDashboardInterface;
import karrus.client.synoptic.modal.TimeHorizonSelector;
import karrus.client.synoptic.modal.TimeHorizonSelectorListener;
import karrus.shared.hibernate.TtItinerary;
import karrus.shared.language.Language;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Creates a panel containing a plot with speed and count for count-data-way.
 * The plots are displayed in real time.
 * The user can choose a time horizon.
 * 
 * @author estelle.dumas@karrus-its.com
 */
public class TravelTimeDashboard extends Composite implements DataDashboardInterface, TimeHorizonSelectorListener {

	private ScrollPanel plotScrollPanel;
	private VerticalPanel panel;
	private TravelTimeDashboardTimer travelTimeDashboardTimer;
	private List<CountDataTimeSeriesPlot> timeSeriesPlots = new ArrayList<CountDataTimeSeriesPlot>(); // for filtering
	private TimeHorizonSelector timeHorizonSelector;

	/**
	 * Constructor.
	 * @throws Exception 
	 */	
	public TravelTimeDashboard(String itinerary, int widthForDashboardInTabPanel, int heightForDashboardInTabPanel) throws Exception{

		int timeHorizonInitial = 60*60*1000; // 1h in milliseconds
		travelTimeDashboardTimer = new TravelTimeDashboardTimer(itinerary, FrontalWebApp.ihmParameters.getDataPlotRefreshingRate(), timeHorizonInitial);//IhmParameters.getDataPlotTimeWindow());
		timeHorizonSelector = new TimeHorizonSelector();
		timeHorizonSelector.addTimeHorizonSelectorListener(this);
		timeHorizonSelector.setTimeHorizon(timeHorizonInitial);
		// plot panel
		plotScrollPanel = new ScrollPanel();
		VerticalPanel v = new VerticalPanel();
		v.add(new BlankPanel(widthForDashboardInTabPanel, 20));
		TravelTimePanel travelTimePanel = new TravelTimePanel(this, widthForDashboardInTabPanel*90/100, heightForDashboardInTabPanel*95/100);
		v.add(travelTimePanel);
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
		FrontalWebApp.configurationsDatabaseService.getItinerary(itinerary, new AsyncCallback<TtItinerary>() {
			@Override
			public void onSuccess(TtItinerary result) {
				Log.debug("configurationsDatabaseService.getItinerary: ok");
				PlotMinMax.setIndividualValidTravelTimeMinValue(0);
				PlotMinMax.setIndividualValidTravelTimeMaxValue(result.getScale()/60.);
				PlotMinMax.setIndividualNotValidTravelTimeMinValue(0);
				PlotMinMax.setIndividualNotValidTravelTimeMaxValue(result.getScale()/60.);
				startAction();
			}
			@Override
			public void onFailure(Throwable caught) {
				Log.error("configurationsDatabaseService.getItinerary: failure\n" + caught.getMessage());
				new CustomDialogBox(Language.genericErrorString, Language.okString);
			}
		});
	}

	@Override
	public void onTimeHorizonSelection(int timeHorizon) {
		travelTimeDashboardTimer.setTimeHorizon(timeHorizon);
	}

	/**
	 * Starts the timer to display on real time plots.
	 */
	public void startAction(){
		travelTimeDashboardTimer.start();

	}

	/**
	 * Stops the timer.
	 */
	public void stopAction(){
		travelTimeDashboardTimer.stop();

	}

	@Override
	public void addTimeSeriesForDataDashboard(CountDataTimeSeriesPlot timeSeriesPlot) {
		// register plot to timer
		travelTimeDashboardTimer.addPlot(timeSeriesPlot);
		timeSeriesPlot.addFilteredValue(-1d);
		timeSeriesPlot.addFilteredValue(0d);
		// make all the plots reactive to a change in the filter settings
		timeSeriesPlots.add(timeSeriesPlot);
	}
}
