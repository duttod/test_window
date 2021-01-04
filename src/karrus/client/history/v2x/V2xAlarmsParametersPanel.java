package karrus.client.history.v2x;

import java.util.Date;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.i18n.client.DateTimeFormat;

import karrus.client.FrontalWebApp;
import karrus.client.appearance.Css;
import karrus.client.generic.CustomDialogBox;
import karrus.client.generic.datetime.SelectDatePanel;
import karrus.client.generic.progressbar.WaitingDialogBox;
import karrus.shared.hibernate.V2xAlarm;
import karrus.shared.language.Language;

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
 * Panel to display v2x alarms in a grid
 * 
 * @author estelle.dumas@karrus-its.com
 */
public class V2xAlarmsParametersPanel extends Composite {

	private FrontalWebApp frontalWebApp;
	private SelectDataSourcesPanel selectDataSourcePanel;
    private SelectDatePanel selectDatePanel;
    private DateTimeFormat dateFormatter = DateTimeFormat.getFormat("dd/MM");  
    public Button displayV2xAlarmsButton;
   
	/**
	 * Constructor.
	 * @param frontalWebApp
	 */
	public V2xAlarmsParametersPanel(FrontalWebApp frontalWebApp) {

		this.frontalWebApp = frontalWebApp;
		selectDataSourcePanel = new SelectDataSourcesPanel();
		selectDataSourcePanel.setWidth(FrontalWebApp.getWidthForBorderedParametersWidgets() +"px");
		selectDataSourcePanel.setStyleName(Css.borderStyle);
		selectDatePanel = new SelectDatePanel();
		displayV2xAlarmsButton = new Button(Language.displayV2xAlarmsButtonLabel);
		displayV2xAlarmsButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				displayV2xAlarms();
			}
		});
		FlexTable buttonsFlexTable = new FlexTable();
		buttonsFlexTable.setWidget(0, 0, displayV2xAlarmsButton);
		VerticalPanel extractionPanel = new VerticalPanel();
		Label extractionLabel = new Label(Language.v2xAlarmsExtractionLabel);
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
		panel.setWidget(cpt, 0, selectDataSourcePanel);
		cpt ++;
		VerticalPanel selectDatePanelContainer = new VerticalPanel();
		Label dateRangeLabel = new Label(Language.v2xAlarmsDateRangeLabel);
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
		return selectDataSourcePanel.getSelectedStation();
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
	 * Action when clicking on the display button.
	 */
	private void displayV2xAlarms(){
		try {
//			DateTime.checkMaxSizeRequest(this.getStartDate(), this.getEndDate());
			final String selectedStation = this.getSelectedStation();
			final WaitingDialogBox waitingDialogBox = new WaitingDialogBox();
			FrontalWebApp.genericDatabaseService.getV2xAlarms(selectedStation, getStartDate(), getEndDate(), new AsyncCallback<List<V2xAlarm>>() {
				@Override
				public void onSuccess(List<V2xAlarm> result) {
					if (waitingDialogBox.isShowing()) {
						waitingDialogBox.terminate();
					}
					Log.debug("genericDatabaseService.getV2xAlarms: ok");
					if (result.isEmpty()) {
						new CustomDialogBox(Language.noDataForThisTimePeriod, Language.okString);
						return;
					}
					try {
						V2xAlarmsGridPanel v2xAlarmsGridPanel = new V2xAlarmsGridPanel(result);
						String tabBarLabel = "";
						tabBarLabel = selectedStation + " - " + dateFormatter.format(getStartDate()) + " " + dateFormatter.format(getEndDate());
						frontalWebApp.addNewTabbedPanel(Language.v2xAlarmsMenu, v2xAlarmsGridPanel, tabBarLabel, true);
					} catch (Exception e) {
						Log.error(e.getMessage());
						new CustomDialogBox(e.getMessage(), Language.okString);
					}	
				}
				@Override
				public void onFailure(Throwable caught) {
					waitingDialogBox.terminate();
					Log.error("genericDatabaseService.getV2xAlarms: failure\n"+caught.getMessage());
					new CustomDialogBox(Language.genericErrorString, Language.okString);
				}
			});
		} catch (Exception exception) {
			Log.error(exception.getMessage());
			new CustomDialogBox(exception.getMessage(), Language.okString);
		}
	}
}
