package karrus.client.synoptic.modal.travelTime;

import karrus.client.generic.plot.CountDataTimeSeriesPlot;
import karrus.client.generic.plot.PlotType;
import karrus.client.appearance.Css;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;



/**
 * Creates a panel containing a plot for speed and count from count data way.
 * The plots are displayed in real time.
 * 
 * @author estelle.dumas@karrus-its.com
 */
public class TravelTimePanel extends Composite{

	private HorizontalPanel panel;
	
	/**
	 * Constructor.
	 * @param travelTimeDashboard CountDataDashboard
	 * @param width int
	 * @param height int
	 */
	public TravelTimePanel(TravelTimeDashboard travelTimeDashboard, int width, int height){
		panel = new HorizontalPanel();
		this.initWidget(panel);
		CountDataTimeSeriesPlot timeSeriesPlot = new CountDataTimeSeriesPlot(this,"", width, height - 25, PlotType.STEP, false, true, true, false);
		timeSeriesPlot.setStyleName(Css.trafficsPlotStyle);
		panel.add(timeSeriesPlot);
		travelTimeDashboard.addTimeSeriesForDataDashboard(timeSeriesPlot);
	}
}
