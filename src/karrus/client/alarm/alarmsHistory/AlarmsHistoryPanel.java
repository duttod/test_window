package karrus.client.alarm.alarmsHistory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import karrus.client.FrontalWebApp;
import karrus.client.generic.CheckBoxGeneric;
import karrus.client.generic.CustomDialogBox;
import karrus.client.generic.ListBoxGeneric;
import karrus.client.generic.datetime.SelectDatePanel;
import karrus.client.login.UserManagement;
import karrus.client.appearance.Css;
import karrus.shared.hibernate.AlAlarmClosed;
import karrus.shared.hibernate.SysEnv;
import karrus.shared.language.Language;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Creates a panel enabling to set parameters to show alarms in a table
 * 
 * @author estelle.dumas@karrus-its.com
 */
public class AlarmsHistoryPanel extends Composite {

	private int numberOfBoxesChecked = 0;
	private SelectDatePanel selectDatePanel;
	public ListBoxGeneric sourceListBox;
	public ListBoxGeneric typeListBox;
	public ListBoxGeneric stateListBox;
	public List<CheckBox> checkBoxesList = new ArrayList<CheckBox>();
	public List<AlAlarmClosed> alarms = new ArrayList<AlAlarmClosed>();
	private List<String> sources = new ArrayList<String>();
	private List<String> types = new ArrayList<String>();
	private Button filterButton;
	private FlexTable filterParametersTable;
	int width = 800;
	int height = 500;
	private FlexTable filterButtonTable;
	private FlexTable openButtonTable;
	private FlexTable mainContainerFlextable;
	private VerticalPanel alarmsTablePanel;
	private CheckBoxGeneric selectAllCheckBox;
	private Button exportButton;
	private Button openAlarmButton;
	private Date alarmsTableStartDate;
	private Date alarmsTableStopDate;
	private String alarmsTableSource;
	private String alarmsTableType;
	private String alarmsTableState;
	private ClosedAlarmsGrid alarmsGrid;

	/**
	 * Constructor. 
	 * Create the panel enclosing the different widgets
	 * Widgets in the panel : date selection panel, filter panel
	 * @param karrusDireRdv KarrusDireRdv
	 */
	public AlarmsHistoryPanel(FrontalWebApp karrusDireRdv) {
		selectDatePanel = new SelectDatePanel();
		ClickHandler filterButtonClickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				plotTableAction();
			}
		};
		filterButton = new Button(Language.alarmsDisplayButton);
		filterButton.addClickHandler(filterButtonClickHandler);
		filterParametersTable = new FlexTable();
		FrontalWebApp.genericDatabaseService.getEnvironmentVariable(Language.alarmTypes, new AsyncCallback<SysEnv>() {
			public void onFailure(Throwable caught) {
				Log.error("genericDatabaseService.getEnvironmentVariable : failed "+caught.getMessage());
				new CustomDialogBox(Language.genericErrorString, "ok");
			}
			public void onSuccess(SysEnv result) {
				Log.debug("genericDatabaseService.getEnvironmentVariable: ok");
				final String[] alarmTypesArray = result.getContent().split(",");
				FrontalWebApp.genericDatabaseService.getStationsIdsForAlarmsFilter(new AsyncCallback<List<String>>() {
					@Override
					public void onFailure(Throwable caught) {
						Log.error("genericDatabaseService.getStationsIdsForAlarmsFilter : failed "+caught.getMessage());
						new CustomDialogBox(Language.genericErrorString, "ok");
					}
					@Override
					public void onSuccess(List<String> result) {
						Log.debug("genericDatabaseService.getStationsIdsForAlarmsFilter: ok");
						for (int i = 0; i < alarmTypesArray.length; i++) {
							types.add(alarmTypesArray[i]);
						}
						Collections.sort(types);
						types.add(0, Language.allAlarmString);
						for (String stationId : result) {
							sources.add(stationId);
						}	
						sources.add(Language.middleware.toUpperCase());
						Collections.sort(sources);
						sources.add(0, Language.allAlarmString);
						sourceListBox = new ListBoxGeneric(Language.equipmentString, sources, "", false);
						typeListBox = new ListBoxGeneric(Language.typeString, types, "", false);
						List<String> states = new ArrayList<String>();
						states.add(Language.allAlarmString);
						states.add(Language.disappearedAlarmStatus);
						states.add(Language.solvedAlarmStatus);
						stateListBox = new ListBoxGeneric(Language.statusString, states, "", false);
						filterParametersTable.setWidget(0, 0, typeListBox);
						filterParametersTable.setWidget(0, 1, sourceListBox);
						filterParametersTable.setWidget(0, 2, stateListBox);
					}
				});
			}
		});
		filterButtonTable = new FlexTable();
		filterButtonTable.setWidget(0, 0, filterButton);
		exportButton = new Button(Language.exportTableString);
		exportButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				exportAction();
			}
		});
		filterButtonTable.setWidget(0, 1, exportButton);
		openAlarmButton = new Button(Language.openAlarmAction);
		if (UserManagement.getCredential().getAlarms().equals(Language.visibleButNotModifiableRight)) {
			openAlarmButton.setEnabled(false);
		}
		openAlarmButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				openSelectedAlarms();
			}
		});
		openButtonTable = new FlexTable();
		openButtonTable.setWidget(0, 0, openAlarmButton);
		openAlarmButton.setVisible(false);
		Label filterLabel = new Label(Language.filterLabel);
		filterLabel.setStyleName(Css.boldAndBottomPadding);
		VerticalPanel filterVerticalPanel = new VerticalPanel();
		filterVerticalPanel.add(filterLabel);
		filterVerticalPanel.add(selectDatePanel);
		filterVerticalPanel.getWidget(1).getElement().getStyle().setProperty("paddingLeft", "4px");
		filterVerticalPanel.add(filterParametersTable);
		filterVerticalPanel.add(filterButtonTable);
		filterVerticalPanel.setWidth(FrontalWebApp.getWidthForBorderedParametersWidgets() +"px");
		filterVerticalPanel.setStyleName(Css.borderStyle);
		alarmsTablePanel = new VerticalPanel();
		alarmsTablePanel.setWidth(FrontalWebApp.getWidthForBorderedParametersWidgets()+"px");
		alarmsTablePanel.setStyleName(Css.borderStyle);
		selectAllCheckBox = new CheckBoxGeneric(Language.selectAll, false);
		selectAllCheckBox.getCheckBox().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				List<ClosedAlarmContent> closedAlarmContents = alarmsGrid.getVisibleAlarms();
				if (selectAllCheckBox.getCheckBox().getValue()) {
					for (int k =0; k < checkBoxesList.size(); k ++) {
						CheckBox checkBox = checkBoxesList.get(k);
						if (checkBox.isEnabled()) {
							checkBox.setValue(true);
							closedAlarmContents.get(k).setIsChecked(true);
						}
					}
					alarmsGrid.dataGrid.setRowData(0, closedAlarmContents);
					numberOfBoxesChecked = alarms.size();
					if (numberOfBoxesChecked > 0) {
						openAlarmButton.setVisible(true);
					}
				}
				else {
					for (int k =0; k < checkBoxesList.size(); k ++) {
						CheckBox checkBox = checkBoxesList.get(k);
						if (checkBox.isEnabled()) {
							checkBox.setValue(false);
							closedAlarmContents.get(k).setIsChecked(false);
						}
					}
					alarmsGrid.dataGrid.setRowData(0, closedAlarmContents);
					numberOfBoxesChecked = 0;
					if (numberOfBoxesChecked == 0) {
						openAlarmButton.setVisible(false);
					}
				}
			}
		});
		mainContainerFlextable = new FlexTable();
		mainContainerFlextable.setWidget(0, 0, filterVerticalPanel);
		mainContainerFlextable.setHeight("10px"); // to force the size of the panel to be minimum
		this.initWidget(mainContainerFlextable);
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
	 * Action when clicking on the filter button.
	 */
	private void plotTableAction(){
		try {
			alarmsTableStartDate = selectDatePanel.getInitialDateTime();
			alarmsTableStopDate = selectDatePanel.getFinalDateTime();
			alarmsTableSource = null;
			if (!sourceListBox.getSelectedValue().equals(Language.allAlarmString)) {
				alarmsTableSource = sourceListBox.getSelectedValue();
			}
			alarmsTableType = null;
			if (!typeListBox.getSelectedValue().equals(Language.allAlarmString)) {
				alarmsTableType = typeListBox.getSelectedValue();
			}
			alarmsTableState = null;
			if (!stateListBox.getSelectedValue().equals(Language.allAlarmString)) {
				alarmsTableState = stateListBox.getSelectedValue();
			}
			FrontalWebApp.alarmsDatabaseService.getAlarmsHistoryFiltered(alarmsTableStartDate, alarmsTableStopDate, alarmsTableSource, alarmsTableType, alarmsTableState, new AsyncCallback<List<AlAlarmClosed>>() {
				@Override
				public void onSuccess(List<AlAlarmClosed> result) {
					Log.debug("alarmDatabaseService.getAlarmsHistoryFiltered: ok");	
					try {
						// Create the grid for closed alarms
						alarmsGrid = new ClosedAlarmsGrid(result, AlarmsHistoryPanel.this);
						checkBoxesList.clear();
						alarms.clear();
						for (int j =0; j < result.size(); j++) {
							AlAlarmClosed alarm = result.get(j);
							AlarmsHistoryPanel.this.alarms.add(alarm);
							CheckBox checkBox = new CheckBox();
							checkBoxesList.add(checkBox);
						}
						if (mainContainerFlextable.getRowCount() == 1) {
							mainContainerFlextable.setWidget(1, 0, alarmsTablePanel);
							alarmsTablePanel.add(new HTML("&nbsp"));
							alarmsTablePanel.add(alarmsGrid.dataGrid);
							alarmsTablePanel.add(selectAllCheckBox);
							alarmsTablePanel.add(openButtonTable);
						}
						else {
							alarmsTablePanel.remove(1);
							alarmsTablePanel.insert(alarmsGrid.dataGrid, 1);
						}
						selectAllCheckBox.setValue(false);
					} catch (Exception e) {
						Log.error(e.getMessage());
						new CustomDialogBox(e.getMessage(), Language.okString);
					}
				}
				@Override
				public void onFailure(Throwable caught) {
					Log.error("alarmDatabaseService.getAlarmsHistoryFiltered: failure\n"+caught.getMessage());
					new CustomDialogBox("alarmDatabaseService.getAlarmsHistoryFiltered: failure", Language.okString);
				}
			});
		} catch (Exception e) {
			Log.error(e.getMessage());
			new CustomDialogBox(e.getMessage(), Language.okString);
		}
	}

	/**
	 * Method called when the export button is clicked
	 */
	private void exportAction(){
		try {
			alarmsTableStartDate = selectDatePanel.getInitialDateTime();
			alarmsTableStopDate = selectDatePanel.getFinalDateTime();
			alarmsTableSource = null;
			if (!sourceListBox.getSelectedValue().equals(Language.allAlarmString)) {
				alarmsTableSource = sourceListBox.getSelectedValue();
			}
			alarmsTableType = null;
			if (!typeListBox.getSelectedValue().equals(Language.allAlarmString)) {
				alarmsTableType = typeListBox.getSelectedValue();
			}
			alarmsTableState = null;
			if (!stateListBox.getSelectedValue().equals(Language.allAlarmString)) {
				alarmsTableState = stateListBox.getSelectedValue();
			}
			// Call to the the exportService.exportAlarmsTable service
			FrontalWebApp.exportService.exportClosedAlarmsTable(
					alarmsTableStartDate, alarmsTableStopDate, alarmsTableSource, alarmsTableType, alarmsTableState,
					new AsyncCallback<String>() {
				// If the call succeeded, open the csv file created		
				public void onSuccess(String outpuFileName) {
					Log.debug("service.exportAlarmsTable: ok");
					String baseURL = GWT.getHostPageBaseURL();
					String url = baseURL + "ExportCountDataTableServlet/" + outpuFileName;
					Window.open(url, Language.blankString, "");
				}
				public void onFailure(Throwable caught) {
					Log.error("service.exportAlarmsTable: failure\n" + caught.getMessage());
					new CustomDialogBox("service.exportAlarmsTable: failure", Language.okString);
				}
			});
		} catch (Exception e) {
			Log.error(e.getMessage());
			new CustomDialogBox(e.getMessage(), Language.okString);
		}
	}
	
	/**
	 * Method called when the reopen button is clicked
	 */
	public void openSelectedAlarms() {
		numberOfBoxesChecked = 0;
		openAlarmButton.setVisible(false);
		List<AlAlarmClosed> selectedAlarms = new ArrayList<AlAlarmClosed>();
		// Get the selected unclosed alarms
		for (int i = 0; i < checkBoxesList.size(); i++){
			if (checkBoxesList.get(i).getValue() == true ) {
				selectedAlarms.add(alarms.get(i));
			}
		}
		// If no alarms is selected, open a dialog box and return
		if (selectedAlarms.size() == 0) {
			new CustomDialogBox(Language.noAlarmSelected, Language.okString);
			return;
		}
		// Call to the alarmDatabaseService.updateAlarms service to close all selected alarms
		FrontalWebApp.alarmsDatabaseService.openAlarms(selectedAlarms, new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				Log.error("service.openAlarms: failure\n"+caught.getMessage());
				new CustomDialogBox("service.openAlarms: failure", Language.okString);
			}
			public void onSuccess(Void result) {
				plotTableAction();
			}
		});
	}
	
	public void setCheckBoxValue(int i, Boolean value) {
		checkBoxesList.get(i).setValue(value);
		if (checkBoxesList.get(i).getValue()) {
			numberOfBoxesChecked += 1;
			if (numberOfBoxesChecked > 0) {
				openAlarmButton.setVisible(true);
		
			}
		}
		else {
			numberOfBoxesChecked -= 1;
			if (numberOfBoxesChecked == 0) {
				openAlarmButton.setVisible(false);
			}
		}
	}
}
