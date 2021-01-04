package karrus.client.history.diagnostic;

import java.util.Date;
import java.util.List;

import com.google.gwt.i18n.client.DateTimeFormat;

import karrus.client.FrontalWebApp;
import karrus.client.appearance.Css;
import karrus.client.generic.CustomDialogBox;
import karrus.client.generic.datetime.SelectDatePanel;
import karrus.client.generic.progressbar.WaitingDialogBox;
import karrus.client.utils.DateTime;
import karrus.shared.hibernate.RsuDiag;
import karrus.shared.language.Language;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
public class RsuDiagnosticParametersPanel extends Composite {

	private FrontalWebApp frontalWebApp;
	private SelectDataSourcesPanel selectDataSourcesPanel;
    private SelectDatePanel selectDatePanel;
    private DateTimeFormat dateFormatter = DateTimeFormat.getFormat("dd/MM");  
    public Button plotGraphButton;
   
	/**
	 * Constructor.
	 * @param frontalWebApp
	 */
	public RsuDiagnosticParametersPanel(FrontalWebApp frontalWebApp) {

		this.frontalWebApp = frontalWebApp;
		selectDataSourcesPanel = new SelectDataSourcesPanel();
		selectDataSourcesPanel.setWidth(FrontalWebApp.getWidthForBorderedParametersWidgets() +"px");
		selectDataSourcesPanel.setStyleName(Css.borderStyle);
		selectDatePanel = new SelectDatePanel();
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
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getSelectedStation() throws Exception{
		return selectDataSourcesPanel.getSelectedStation();
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
			final WaitingDialogBox waitingDialogBox = new WaitingDialogBox();
			FrontalWebApp.genericDatabaseService.getRsuDiagnosticForStation(selectedStation, getStartDate(), getEndDate(), new AsyncCallback<List<RsuDiag>>() {
				@Override
				public void onSuccess(List<RsuDiag> result) {
					if (waitingDialogBox.isShowing()) {
						waitingDialogBox.terminate();
					}
					Log.debug("genericDatabaseService.getRsuDiagnosticForStation: ok");
					if (result.isEmpty()) {
						new CustomDialogBox(Language.noDataForThisTimePeriod, Language.okString);
						return;
					}
					try {
						RsuDiagnosticPlotPanel rsuDiagnosticPlotPanel = new RsuDiagnosticPlotPanel(selectedStation, getStartDate(), getEndDate());
						String tabBarLabel = "";
						tabBarLabel = selectedStation + " - " + dateFormatter.format(getStartDate()) + " " + dateFormatter.format(getEndDate());
						frontalWebApp.addNewTabbedPanel(Language.rsuDiagnosticMenu + "_diag", rsuDiagnosticPlotPanel, tabBarLabel, true);
					    rsuDiagnosticPlotPanel.addPlots(result);
					} catch (Exception e) {
						Log.error(e.getMessage());
						new CustomDialogBox(e.getMessage(), Language.okString);
					}	
				}
				@Override
				public void onFailure(Throwable caught) {
					waitingDialogBox.terminate();
					Log.error("genericDatabaseService.getRsuDiagnosticForStation: failure\n"+caught.getMessage());
					new CustomDialogBox(Language.genericErrorString, Language.okString);
				}
			});
		} catch (Exception exception) {
			Log.error(exception.getMessage());
			new CustomDialogBox(exception.getMessage(), Language.okString);
		}
	}
}
