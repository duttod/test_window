package karrus.client.history.counting;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;

import karrus.client.FrontalWebApp;
import karrus.client.appearance.Css;
import karrus.client.generic.CustomDialogBox;
import karrus.client.generic.datetime.SelectDatePanel;
import karrus.client.generic.progressbar.WaitingDialogBox;
import karrus.client.utils.DateTime;
import karrus.shared.CountDataAndLanesMap;
import karrus.shared.language.Language;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Count data parameters panel to select sources and time period
 * 
 * @author estelle.dumas@karrus-its.com
 */
public class CountingParametersPanel extends Composite {

	private FrontalWebApp frontalWebApp;
	private SelectDataSourcesPanel selectDataSourcesPanel;
    private SelectDatePanel selectDatePanel;
    private DateTimeFormat dateFormatter = DateTimeFormat.getFormat("dd/MM");  
    public Button plotGraphButton;
    public Button csvExportButton;
   
	/**
	 * Constructor.
	 * @param frontalWebApp
	 */
	public CountingParametersPanel(FrontalWebApp frontalWebApp) {

		this.frontalWebApp = frontalWebApp;
		selectDataSourcesPanel = new SelectDataSourcesPanel();
		selectDataSourcesPanel.setWidth(FrontalWebApp.getWidthForBorderedParametersWidgets() +"px");
		selectDataSourcesPanel.setStyleName(Css.borderStyle);
		selectDatePanel = new SelectDatePanel();
		csvExportButton = new Button(Language.CSVexportString);
		ClickHandler csvExportClickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				countDataExport();
			}
		};
		csvExportButton.addClickHandler(csvExportClickHandler);
		plotGraphButton = new Button(Language.displayString);
		ClickHandler plotGraphClickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				plotGraphAction();
			}
		};
		plotGraphButton.addClickHandler(plotGraphClickHandler);
		FlexTable buttonsFlexTable = new FlexTable();
		buttonsFlexTable.setWidget(0, 0, plotGraphButton);
		buttonsFlexTable.setWidget(0, 1, csvExportButton);
		VerticalPanel extractionPanel = new VerticalPanel();
		Label extractionLabel = new Label(Language.extractionString);
		extractionLabel.setStyleName(Css.boldStyle);
		VerticalPanel extractionLabelPanel = new VerticalPanel();
		extractionLabelPanel.add(extractionLabel);
		extractionPanel.add(extractionLabelPanel);
		extractionPanel.add(buttonsFlexTable);
		extractionPanel.setWidth(FrontalWebApp.getWidthForBorderedParametersWidgets()+"px");
		extractionPanel.setStyleName(Css.borderStyle);
		int cpt = 0;
		FlexTable panel = new FlexTable();
		cpt ++;
		panel.setWidget(cpt, 0, new HTML("&nbsp"));
		cpt ++;
		panel.setWidget(cpt, 0, selectDataSourcesPanel);
		cpt ++;
		VerticalPanel selectDatePanelContainer = new VerticalPanel();
		Label dateRangeLabel = new Label(Language.plageHoraireString);
		dateRangeLabel.setStyleName(Css.boldStyle);
		selectDatePanelContainer.add(dateRangeLabel);
		selectDatePanelContainer.add(selectDatePanel);
		selectDatePanelContainer.setWidth(FrontalWebApp.getWidthForBorderedParametersWidgets() +"px");
		selectDatePanelContainer.setStyleName(Css.borderStyle);
		panel.setWidget(cpt, 0, selectDatePanelContainer);
		cpt ++;
		panel.setWidget(cpt, 0, extractionPanel);
		panel.setHeight("10px"); // to force the size of the panel to be minimum
		this.initWidget(panel);
	}
	
	/**
	 * @return String : returns the access point selected in the access points list box name.
	 * @throws Exception if empty
	 */
	public String getSelectedStation() throws Exception{
		return selectDataSourcesPanel.getSelectedStation();
	}
	
	/**
	 * @return String : returns the selected way in the way list box.
	 * @throws Exception if empty
	 */
	public String getSelectedWay() throws Exception{
		return selectDataSourcesPanel.getSelectedWay();
	}
	
	/**
	 * @return Date : the start Date entered in the start date/time panel.
	 * @throws Exception : if the date or time is not correct
	 */
	public Date getStartDate() throws Exception{
		return selectDatePanel.getInitialDateTime();
	}

	/**
	 * @return Date : the final Date entered in the final date/time panel.
	 * @throws Exception : if the date or time is not correct
	 */
	public Date getEndDate() throws Exception{
		return selectDatePanel.getFinalDateTime();
	}

	/**
	 * Action when clicking on the plot graph button.
	 */
	private void plotGraphAction(){
		try {
			DateTime.checkMaxSizeRequest(this.getStartDate(), this.getEndDate());
			final String selectedStation = this.getSelectedStation();
			final String selectedWay = getSelectedWay();
			final WaitingDialogBox waitingDialogBox = new WaitingDialogBox();
			FrontalWebApp.genericDatabaseService.getCountDataForStationAndWay(selectedStation, selectedWay, getStartDate(), getEndDate(), new AsyncCallback<CountDataAndLanesMap>() {
				@Override
				public void onSuccess(CountDataAndLanesMap result) {
					if (waitingDialogBox.isShowing()) {
						waitingDialogBox.terminate();
					}
					Log.debug("genericDatabaseService.getCountData: ok");
					if (result.getLanesMap().isEmpty()) {
						new CustomDialogBox(Language.noLanesDefined(selectedStation, selectedWay), Language.okString);
						return;
					}
					if (result.getCountData().isEmpty()) {
						new CustomDialogBox(Language.noDataForThisTimePeriod, Language.okString);
						return;
					}
					try {
						CountingPlotPanel countDataPlotPanel = new CountingPlotPanel(selectedStation, "", getStartDate(), getEndDate());
						String tabBarLabel = "";
						tabBarLabel = selectedStation + "/" + getSelectedWay() + " - " + dateFormatter.format(getStartDate()) + " " + dateFormatter.format(getEndDate());
						frontalWebApp.addNewTabbedPanel(Language.countingDataHistoryTabLabel, countDataPlotPanel, tabBarLabel, true);
					    countDataPlotPanel.addCountPlots(result);
					} catch (Exception e) {
						Log.error(e.getMessage());
						new CustomDialogBox(e.getMessage(), Language.okString);
					}	
				}
				@Override
				public void onFailure(Throwable caught) {
					waitingDialogBox.terminate();
					Log.error("genericDatabaseService.getCountData: failure\n"+caught.getMessage());
					new CustomDialogBox("genericDatabaseService.getCountData: failure", Language.okString);
				}
			});
		} catch (Exception exception) {
			Log.error(exception.getMessage());
			new CustomDialogBox(exception.getMessage(), Language.okString);
		}
	}

	/**
	 * Action when clicking on the export data button.
	 */
	private void countDataExport() {
		try {
			final String selectedStation = this.getSelectedStation();
			final WaitingDialogBox waitingDialogBox = new WaitingDialogBox();
			FrontalWebApp.exportService.getCountDataCsvFilePath(selectedStation, getSelectedWay(), getStartDate(), getEndDate(), new AsyncCallback<String>() {
				@Override
				public void onSuccess(String result) {
					if (waitingDialogBox.isShowing()) {
						waitingDialogBox.terminate();
					}	
					Log.debug("exportService.getCountDataCsvFilePath: ok");
					String baseURL = GWT.getHostPageBaseURL();
					String url = baseURL + "ExportCountDataTableServlet/"+result.substring(0, result.length());
					Log.debug(url);
					Window.open(url, Language.blankString, "");
				}

				@Override
				public void onFailure(Throwable caught) {
					waitingDialogBox.terminate();
					Log.error("exportService.getCountDataCsvFilePath: failure\n"+caught.getMessage());
					new CustomDialogBox("exportService.getCountDataCsvFilePath: failure", Language.okString);
				}
			});
		} catch (Exception e) {
			Log.error(e.getMessage());
			new CustomDialogBox(e.getMessage(), Language.okString);
		}
	}
}
