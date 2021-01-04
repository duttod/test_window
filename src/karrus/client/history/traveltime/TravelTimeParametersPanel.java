package karrus.client.history.traveltime;

import java.util.Date;

import karrus.client.FrontalWebApp;
import karrus.client.appearance.Css;
import karrus.client.generic.CustomDialogBox;
import karrus.client.generic.datetime.SelectDatePanel;
import karrus.client.generic.progressbar.WaitingDialogBox;
import karrus.client.utils.DateTime;
import karrus.shared.language.Language;
import karrus.shared.plot.TravelTimePlot;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Creates a panel to enter parameters to show count data in a table or in a graph.
 * 
 * @author estelle.dumas@karrus-its.com
 */
public class TravelTimeParametersPanel extends Composite {
	private FrontalWebApp frontalWebApp;
	private SelectDataSourcesPanel itineraryPanel;
    private SelectDatePanel selectDatePanel;
    private ExtractionPanel extractionPanel;
    private DateTimeFormat dateFormatter = DateTimeFormat.getFormat("dd/MM");
	
	/**
	 * Constructor. 
	 * @param frontalWebApp 
	 */
	public TravelTimeParametersPanel(FrontalWebApp frontalWebApp) {
		this.frontalWebApp = frontalWebApp;
		itineraryPanel = new SelectDataSourcesPanel(FrontalWebApp.databaseObjects.getBtItineraries());
		itineraryPanel.setWidth(FrontalWebApp.getWidthForBorderedParametersWidgets()+"px");
		itineraryPanel.setStyleName(Css.borderStyle);
		selectDatePanel = new SelectDatePanel();
		VerticalPanel selectDatePanelContainer = new VerticalPanel();
		Label dateRangeLabel = new Label(Language.plageHoraireString);
		dateRangeLabel.setStyleName(Css.boldStyle);
		selectDatePanelContainer.add(dateRangeLabel);
		selectDatePanelContainer.add(selectDatePanel);
		selectDatePanelContainer.setWidth(FrontalWebApp.getWidthForBorderedParametersWidgets() +"px");
		selectDatePanelContainer.setStyleName(Css.borderStyle);
		ClickHandler plotTravelTimesClickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				plotTravelTimes();
			}
		};
		ClickHandler individualTravelTimesExportClickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				individualTravelTimesExport();
			}
		};
		ClickHandler statisticalTravelTimesExportClickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				statisticalTravelTimesExport();
			}
		};
		extractionPanel = new ExtractionPanel(plotTravelTimesClickHandler, individualTravelTimesExportClickHandler, statisticalTravelTimesExportClickHandler);
		extractionPanel.setWidth(FrontalWebApp.getWidthForBorderedParametersWidgets()+"px");
		extractionPanel.setStyleName(Css.borderStyle);
		int cpt = 0;
		FlexTable panel = new FlexTable();
		cpt ++;
		panel.setWidget(cpt, 0, new HTML("&nbsp"));
		cpt ++;
		panel.setWidget(cpt, 0, itineraryPanel);
		cpt ++;
		panel.setWidget(cpt, 0, selectDatePanelContainer);
		cpt ++;
		panel.setWidget(cpt, 0, extractionPanel);
		cpt ++;
		panel.setHeight("10px"); // to force the size of the panel to be minimum
		this.initWidget(panel);

	}

	/**
	 * @return String : returns the selected itinerary in the 'itinerary' list box.
	 * @throws Exception if empty
	 */
	public String getSelectedItinerary() throws Exception{
		return itineraryPanel.getSelectedItinerary();
	}
	
	/**
	 * @return Date : the initial Date entered in the initial date/time panel.
	 * @throws Exception : if the date or time is not correct
	 */
	public Date getInitialDate() throws Exception{
		return selectDatePanel.getInitialDateTime();
	}

	/**
	 * @return Date : the final Date entered in the final date/time panel.
	 * @throws Exception : if the date or time is not correct
	 */
	public Date getFinalDate() throws Exception{
		return selectDatePanel.getFinalDateTime();
	}

	/**
	 * Action when clicking on the plot graph button.
	 */
	private void plotTravelTimes(){
		try {
			DateTime.checkMaxSizeRequest(this.getInitialDate(), this.getFinalDate());
			final String selectedItinerary = this.getSelectedItinerary();
			final WaitingDialogBox waitingDialogBox = new WaitingDialogBox();
			FrontalWebApp.genericDatabaseService.getTravelTimePlot(selectedItinerary, getInitialDate(), getFinalDate(), new AsyncCallback<TravelTimePlot>() {
				@Override
				public void onSuccess(TravelTimePlot result) {
					if (waitingDialogBox.isShowing())
						waitingDialogBox.terminate();
					Log.debug("genericDatabaseService.getTravelTimePlot: ok");
					if (result.isEmpty()) {
						new CustomDialogBox(Language.noDataForThisTimePeriod, Language.okString);
						return;
					}
					try {
						String tabBarLabel = "";
						tabBarLabel = selectedItinerary  + " - " + dateFormatter.format(getInitialDate()) + " " + dateFormatter.format(getFinalDate());
						TravelTimePlotPanel panel = new TravelTimePlotPanel(selectedItinerary, getInitialDate(), getFinalDate(), true);
						frontalWebApp.addNewTabbedPanel(Language.travelTimeBluetoothMenu, panel, tabBarLabel, true);
						panel.addTravelTimePlot(result);
					} catch (Exception e) {
						e.printStackTrace();
						Log.error(e.getMessage());
						new CustomDialogBox(e.getMessage(), Language.okString);
					}						
				}
				@Override
				public void onFailure(Throwable caught) {
					waitingDialogBox.terminate();
					caught.printStackTrace();
					Log.error("genericDatabaseService.getTravelTimePlot: failure\n"+caught.getMessage());
					new CustomDialogBox("genericDatabaseService.getTravelTimePlot: failure", Language.okString);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
			new CustomDialogBox(e.getMessage(), Language.okString);
		}
	}
	
	/**
	 * Action when clicking on the individual tt export button.
	 */
	private void individualTravelTimesExport() {
		try {
			String selectedItinerary = this.getSelectedItinerary();
			FrontalWebApp.exportService.getIndividualTravelTimeCsvFilePath(selectedItinerary, getInitialDate(), getFinalDate(), new AsyncCallback<String>() {
				@Override
				public void onSuccess(String result) {
					Log.debug("exportService.getTravelTimesAndExportTable: ok");
					String baseURL = GWT.getHostPageBaseURL();
					String url = baseURL + "ExportCountDataTableServlet/"+result;
					Window.open(url, Language.blankString, "");
					Log.debug(url);
				}

				@Override
				public void onFailure(Throwable caught) {
					Log.error("exportService.getTravelTimesAndExportTable: failure\n"+caught.getMessage());
					new CustomDialogBox("exportService.getTravelTimesAndExportTable: failure", Language.okString);
				}
			});
		} catch (Exception e) {
			Log.error(e.getMessage());
			new CustomDialogBox(e.getMessage(), Language.okString);
		}
	}
	
	/**
	 * Action when clicking on the statistical tt export button.
	 */
	private void statisticalTravelTimesExport() {
		final WaitingDialogBox waitingDialogBox = new WaitingDialogBox();
		try {
			String selectedItinerary = this.getSelectedItinerary();
			int period = extractionPanel.getPeriod();
			int horizon = extractionPanel.getHorizon();
			String typeOfTimestamp = extractionPanel.getTypeOfTimestamp();
			FrontalWebApp.exportService.getStatisticalTravelTimeCsvFilePath(selectedItinerary, getInitialDate(), getFinalDate(), period, horizon, typeOfTimestamp, new AsyncCallback<String>() {
				@Override
				public void onSuccess(String result) {
					waitingDialogBox.terminate();
					Log.debug("exportService.getStatisticalTravelTimeCsvFilePath: ok");
					String baseURL = GWT.getHostPageBaseURL();
					String url = baseURL + "ExportCountDataTableServlet/"+result;
					Window.open(url, Language.blankString, "");
					Log.debug(url);
				}

				@Override
				public void onFailure(Throwable caught) {
					waitingDialogBox.terminate();
					Log.error("exportService.getStatisticalTravelTimeCsvFilePath: failure\n"+caught.getMessage());
					new CustomDialogBox(Language.genericErrorString, Language.okString);
				}
			});
		} catch (Exception e) {
			waitingDialogBox.terminate();
			Log.error(e.getMessage());
			new CustomDialogBox(e.getMessage(), Language.okString);
		}
	}
}
