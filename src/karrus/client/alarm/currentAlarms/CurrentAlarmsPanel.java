package karrus.client.alarm.currentAlarms;

import java.util.ArrayList;
import java.util.List;

import karrus.client.FrontalWebApp;
import karrus.client.generic.CheckBoxGeneric;
import karrus.client.generic.CustomDialogBox;
import karrus.client.generic.EnterNamePanel;
import karrus.client.generic.ListBoxGeneric;
import karrus.client.generic.ScrollTableGeneric;
import karrus.client.generic.tab.TabbedPanelGeneric;
import karrus.client.login.UserManagement;
import karrus.client.appearance.Css;
import karrus.shared.hibernate.AlAlarmClosed;
import karrus.shared.hibernate.AlAlarmOpened;
import karrus.shared.language.Language;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.gen2.table.client.FixedWidthFlexTable;
import com.google.gwt.gen2.table.client.FixedWidthGrid;
import com.google.gwt.gen2.table.override.client.FlexTable.FlexCellFormatter;
import com.google.gwt.gen2.table.override.client.HTMLTable.CellFormatter;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Creates a panel enabling to set parameters to show alarms in a table
 * 
 * @author estelle.dumas@karrus-its.com
 */
public class CurrentAlarmsPanel extends Composite {

	private int numberOfBoxesChecked = 0;
	private DateTimeFormat dateFormatter = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss");
	public ListBoxGeneric sourceListBox;
	public ListBoxGeneric typeListBox;
	public ListBoxGeneric stateListBox;
	public List<CheckBox> checkBoxesList = new ArrayList<CheckBox>();
	public List<AlAlarmOpened> alarms = new ArrayList<AlAlarmOpened>();
	private List<String> sources = new ArrayList<String>();
	private List<String> types = new ArrayList<String>();
	private Button filterButton;
	private FlexTable filterParametersTable;
	int width = 800;
	int height = 500;
	private TabbedPanelGeneric parentPanel;
	public static List<String> usedTitles;
	private EnterNamePanel tablePanel;
	private FlexTable buttonsFlextable;
	private FlexTable closeAndExportButtonsTable;
	private FlexTable panel;
	private VerticalPanel alarmsTablePanel;
	private CheckBoxGeneric selectAllCheckBox;
	private Button exportButton;
	private Button reloadButton;
	private Button closeButton;
	private Button acknowledgementButton;
	private OpenedAlarmsGrid alarmsGrid;

	/**
	 * Constructor. 
	 * Create the panel enclosing the different widgets
	 * Widgets in the panel : date selection panel, filter panel
	 * @param karrusDireRdv KarrusDireRdv
	 */
	public CurrentAlarmsPanel(FrontalWebApp karrusDireRdv) {
		usedTitles = new ArrayList<String>();
		ClickHandler click = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				plotTableAction();
			}
		};
		tablePanel = new EnterNamePanel(Language.tableNameString, getTableName(), Language.displayTableString, click);
		filterButton = new Button(Language.alarmsDisplayButton);
		filterButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				plotTableAction();
			}
		});
		// Flextable containing widgets to set the parameters of the filter
		filterParametersTable = new FlexTable();
		FrontalWebApp.alarmsDatabaseService.getAlarmSources(new AsyncCallback<List<AlAlarmClosed>>() {
			public void onFailure(Throwable caught) {
				Log.error("alarmDatabaseService.getAlarmSources : failed "+caught.getMessage());
				new CustomDialogBox("alarmDatabaseService.getAlarmSources : failed "+caught.getMessage(), "ok");
			}
			public void onSuccess(List<AlAlarmClosed> result) {
				sources.add(Language.allAlarmString);
				for (AlAlarmClosed alarmSource : result) {
					sources.add(alarmSource.getId().getSource());
				}	
				types.add(Language.allAlarmString);
				types.add(Language.networkAlarmType);
				types.add(Language.rdtSyncAlarmType);
				types.add(Language.rdtSensorAlarmType);
				types.add(Language.rdtLatencyAlarmType);
				types.add(Language.rdtDataAlarmType);
				types.add(Language.serverAlimAlarmType);
				types.add(Language.serverRaid1AlarmType);
				types.add(Language.serverRaid5AlarmType);				
				types.add(Language.serverCpuAlarmType);
				types.add(Language.serverDiskAlarmType);
				types.add(Language.serverRamAlarmType);
				types.add(Language.serverProcessAlarmType);
				types.add(Language.serverTomcatAlarmType);
				types.add(Language.serverPostgresqlAlarmType);
				types.add(Language.serverNtpAlarmType);
				types.add(Language.serverSshAlarmType);
				types.add(Language.serverApacheAlarmType);
				types.add(Language.serverMySqlAlarmType);
				sourceListBox = new ListBoxGeneric(Language.equipmentString, sources, "", false);
				typeListBox = new ListBoxGeneric(Language.typeString, types, "", false);
				List<String> states = new ArrayList<String>();
				states.add(Language.allAlarmString);
				states.add(Language.newAlarmStatus);
				states.add(Language.acknowledgedAlarmStatus);
				states.add(Language.disappearedAlarmStatus);
				states.add(Language.solvedAlarmStatus);
				stateListBox = new ListBoxGeneric(Language.statusString, states, "", false);
				filterParametersTable.setWidget(0, 0, typeListBox);
				filterParametersTable.setWidget(0, 1, sourceListBox);
				filterParametersTable.setWidget(0, 2, stateListBox);
			}
		});
		buttonsFlextable = new FlexTable();
		buttonsFlextable.setWidget(0, 0, exportButton);
		exportButton = new Button(Language.exportTableString);
		exportButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				exportAction();
			}
		});
		reloadButton= new Button(Language.reloadAlarmsString);
		reloadButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				plotTableAction();
			}
		});
		buttonsFlextable.setWidget(0, 0, exportButton);
		buttonsFlextable.setWidget(0, 1, reloadButton);
		closeButton = new Button(Language.clotureAlarmAction);
		if (UserManagement.getCredential().getAlarms().equals(Language.visibleButNotModifiableRight)) {
			closeButton.setEnabled(false);
		}
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				closeSelectedAlarms();
			}
		});
		acknowledgementButton = new Button(Language.acknowledgeAlarmAction);
		if (UserManagement.getCredential().getAlarms().equals(Language.visibleButNotModifiableRight)) {
			acknowledgementButton.setEnabled(false);
		}
		acknowledgementButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				acknowledgeSelectedAlarms();
			}
		});
		closeAndExportButtonsTable = new FlexTable();
		closeAndExportButtonsTable.setWidget(0, 0, acknowledgementButton);
		acknowledgementButton.setVisible(false);
		closeAndExportButtonsTable.setWidget(0, 1, closeButton);
		closeButton.setVisible(false);
		VerticalPanel vTablesString = new VerticalPanel();
		Label filterLabel = new Label(Language.filterLabel);
		filterLabel.setStyleName(Css.boldAndBottomPadding);
		Label dateLabel = new Label(Language.plageHoraireString);
		vTablesString.add(filterLabel);
		vTablesString.add(dateLabel);
		VerticalPanel vTablePanel = new VerticalPanel();
//		vTablePanel.add(vTablesString);
//		vTablePanel.add(dateSelectionTable);
//		vTablePanel.add(filterParametersTable);
		vTablePanel.add(buttonsFlextable);
		vTablePanel.setWidth(FrontalWebApp.getWidthForBorderedParametersWidgets()+"px");
//		vTablePanel.setStyleName(Css.borderStyle);
		alarmsTablePanel = new VerticalPanel();
		alarmsTablePanel.setWidth(FrontalWebApp.getWidthForBorderedParametersWidgets() +"px");
		alarmsTablePanel.setStyleName(Css.borderStyle);
		selectAllCheckBox = new CheckBoxGeneric(Language.selectAll, false);
		selectAllCheckBox.getCheckBox().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				List<OpenedAlarmContent> openedAlarmContents = alarmsGrid.getVisibleAlarms();
				if (selectAllCheckBox.getCheckBox().getValue()) {
					for (int k =0; k < checkBoxesList.size(); k ++) {
						CheckBox checkBox = checkBoxesList.get(k);
						if (checkBox.isEnabled()) {
							checkBox.setValue(true);
							openedAlarmContents.get(k).setIsChecked(true);
						}
					}
					alarmsGrid.dataGrid.setRowData(0, openedAlarmContents);
					numberOfBoxesChecked = alarms.size();
					if (numberOfBoxesChecked > 0) {
						closeButton.setVisible(true);
						acknowledgementButton.setVisible(true);
					}
				}
				else {
					for (int k =0; k < checkBoxesList.size(); k ++) {
						CheckBox checkBox = checkBoxesList.get(k);
						if (checkBox.isEnabled()) {
							checkBox.setValue(false);
							openedAlarmContents.get(k).setIsChecked(false);
						}
					}
					alarmsGrid.dataGrid.setRowData(0, openedAlarmContents);
					numberOfBoxesChecked = 0;
					if (numberOfBoxesChecked == 0) {
						closeButton.setVisible(false);
						acknowledgementButton.setVisible(false);
					}
				}
			}
		});
		panel = new FlexTable();
		panel.setWidget(0, 0, vTablePanel);
//		panel.setHeight("10px"); // to force the size of the panel to be minimum
		this.initWidget(panel);
		plotTableAction();
   	}

	/**
	 * Action when clicking on the filter button.
	 */
	private void plotTableAction(){
		try {
//			alarmsTableStartDate = selectDatePanel.getStartDateTime();
//			alarmsTableStopDate = selectDatePanel.getEndDateTime();
//			if (selectDatePanel.allRadioButton.getValue()) {
//				alarmsTableStartDate = DateTime.dateTimeFormat.parse("1970-01-01 00:00:00");
//				alarmsTableStopDate = new Date();
//			}
//			alarmsTableSource = null;
//			if (!sourceListBox.getSelectedValue().equals(Language.allAlarmString)) {
//				alarmsTableSource = sourceListBox.getSelectedValue();
//			}
//			alarmsTableType = null;
//			if (!typeListBox.getSelectedValue().equals(Language.allAlarmString)) {
//				alarmsTableType = typeListBox.getSelectedValue();
//			}
//			alarmsTableState = null;
//			if (!stateListBox.getSelectedValue().equals(Language.allAlarmString)) {
//				alarmsTableState = stateListBox.getSelectedValue();
//			}
			FrontalWebApp.synopticDatabaseService.getOpenedAlarms(new AsyncCallback<List<AlAlarmOpened>>() {
				@Override
				public void onSuccess(List<AlAlarmOpened> result) {
					Log.debug("alarmDatabaseService.getOpenedAlarms: ok");
					try {
						alarmsGrid = new OpenedAlarmsGrid(result, CurrentAlarmsPanel.this);
						// Create the header of the schedule scrollpane
						FixedWidthFlexTable headerTable = new FixedWidthFlexTable();
						FlexCellFormatter headerFormatter = headerTable.getFlexCellFormatter();
						int i = 0;
						headerTable.setWidget(0, i, new Label(""));
						headerFormatter.setHorizontalAlignment(0, i, HasHorizontalAlignment.ALIGN_CENTER);
						i++;
						headerTable.setWidget(0, i, new Label(Language.typeString));
						headerFormatter.setHorizontalAlignment(0, i, HasHorizontalAlignment.ALIGN_CENTER);
						i++;
						headerTable.setWidget(0, i, new Label(Language.equipmentString));
						headerFormatter.setHorizontalAlignment(0, i, HasHorizontalAlignment.ALIGN_CENTER);
						i++;
						headerTable.setWidget(0, i, new Label(Language.alarmsStatusHeader));
						headerFormatter.setHorizontalAlignment(0, i, HasHorizontalAlignment.ALIGN_CENTER);
						i++;
						headerTable.setWidget(0, i, new Label(Language.openingTimestampString));
						headerFormatter.setHorizontalAlignment(0, i, HasHorizontalAlignment.ALIGN_CENTER);
						i++;
						headerTable.setWidget(0, i, new Label(Language.acknowledgedTimestampString));
						headerFormatter.setHorizontalAlignment(0, i, HasHorizontalAlignment.ALIGN_CENTER);
						i++;
						FixedWidthGrid dataTable = new FixedWidthGrid(result.size(), 6);
						CellFormatter formatter = dataTable.getCellFormatter();
						i = 0;
						checkBoxesList.clear();
						alarms.clear();
						for (int j =0; i < result.size(); j++) {
							AlAlarmOpened alarm = result.get(j);
							CurrentAlarmsPanel.this.alarms.add(alarm);
//							alarmContents.get(i)
							CheckBox checkBox = new CheckBox();
							checkBoxesList.add(checkBox);
							dataTable.setWidget(i, 0, checkBoxesList.get(i));
							dataTable.setWidget(i, 1, new Label(alarm.getId().getType()));
							dataTable.setWidget(i, 2, new Label(alarm.getId().getSource()));
							dataTable.setWidget(i, 3, new Label(alarm.getStatus()));
							dataTable.setWidget(i, 4, new Label(alarm.getId().getOpeningTimestamp() == null ? "" : dateFormatter.format(alarm.getId().getOpeningTimestamp())));
							dataTable.setWidget(i, 5, new Label(alarm.getAcknowledgementTimestamp() == null ? "" : dateFormatter.format(alarm.getAcknowledgementTimestamp())));
							formatter.setAlignment(i, 0, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
							formatter.setAlignment(i, 1, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
							formatter.setAlignment(i, 2, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
							formatter.setAlignment(i, 3, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
							formatter.setAlignment(i, 4, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
							formatter.setAlignment(i, 5, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
							i++;
						}
						ScrollTableGeneric alarmScrollTable = new ScrollTableGeneric(dataTable, headerTable);
						alarmScrollTable.setHeight(FrontalWebApp.getHeightForAlarmsTableInNotificationMenu()+"px");
						alarmScrollTable.setWidth(FrontalWebApp.getWidthForBorderedParametersWidgets() - 100 +"px");
						if (panel.getRowCount() == 1) {
							panel.setWidget(1, 0, alarmsTablePanel);
							alarmsTablePanel.add(new HTML("&nbsp"));
							alarmsTablePanel.add(alarmsGrid.dataGrid);
							alarmsTablePanel.add(selectAllCheckBox);
							alarmsTablePanel.add(closeAndExportButtonsTable);
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
					Log.error("alarmDatabaseService.getOpenedAlarms: failure\n"+caught.getMessage());
					new CustomDialogBox("alarmDatabaseService.getOpenedAlarms: failure", Language.okString);
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
			// Call to the the exportService.exportOpenedAlarmsTable service
			FrontalWebApp.exportService.exportOpenedAlarmsTable(new AsyncCallback<String>() {
				// If the call succeeded, open the csv file created		
				public void onSuccess(String outpuFileName) {
					Log.debug("service.exportOpenedAlarmsTable: ok");
					String baseURL = GWT.getHostPageBaseURL();
					String url = baseURL + "ExportCountDataTableServlet/" + outpuFileName;
					Window.open(url, Language.blankString, "");
				}
				public void onFailure(Throwable caught) {
					Log.error("service.exportOpenedAlarmsTable: failure\n" + caught.getMessage());
					new CustomDialogBox("service.exportOpenedAlarmsTable: failure", Language.okString);
				}
			});
		} catch (Exception e) {
			Log.error(e.getMessage());
			new CustomDialogBox(e.getMessage(), Language.okString);
		}
	}
	
	/**
	 * Method called when the close button is clicked
	 */
	public void closeSelectedAlarms() {
		numberOfBoxesChecked = 0;
		closeButton.setVisible(false);
		acknowledgementButton.setVisible(false);
		List<AlAlarmOpened> selectedAlarms = new ArrayList<AlAlarmOpened>();
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
		FrontalWebApp.alarmsDatabaseService.closeAlarms(selectedAlarms, new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				Log.error("service.closeAlarms: failure\n"+caught.getMessage());
				new CustomDialogBox("service.closeAlarms: failure", Language.okString);
			}
			public void onSuccess(Void result) {
				plotTableAction();
			}
		});
	}
	
	/**
	 * Method called when the acknowledgement button is clicked
	 */
	public void acknowledgeSelectedAlarms() {
		numberOfBoxesChecked = 0;
		closeButton.setVisible(false);
		acknowledgementButton.setVisible(false);
		List<AlAlarmOpened> selectedAlarms = new ArrayList<AlAlarmOpened>();
		// Get the selected unclosed alarms
		for (int i = 0; i < checkBoxesList.size(); i++){
			if (checkBoxesList.get(i).getValue() == true && alarms.get(i).getAcknowledgementTimestamp() == null) {
				selectedAlarms.add(alarms.get(i));
			}
		}
		// If no alarms is selected, open a dialog box and return
		if (selectedAlarms.size() == 0) {
			new CustomDialogBox(Language.noAlarmSelected, Language.okString);
			return;
		}
		// Call to the alarmDatabaseService.updateAlarms service to close all selected alarms
		FrontalWebApp.alarmsDatabaseService.acknowledgeAlarms(selectedAlarms, new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				Log.error("service.acknowledgeAlarms: failure\n"+caught.getMessage());
				new CustomDialogBox("service.acknowledgeAlarms: failure", Language.okString);
			}
			public void onSuccess(Void result) {
				plotTableAction();
			}
		});
	}
	
	/**
	 * Set the tabbed parent panel.
	 * @param tabbedPanelGeneric TabbedPanelGeneric
	 */
	public void setTabbedPanel(TabbedPanelGeneric tabbedPanelGeneric){
		this.parentPanel = tabbedPanelGeneric;
		tablePanel.setTextInTextBox(getTableName());
	}

	private String getTableName(){
		if (this.parentPanel!=null)
			return this.parentPanel.getTableName();
		return Language.emptyString;
	}
	
	public void setCheckBoxValue(int i, Boolean value) {
		checkBoxesList.get(i).setValue(value);
		if (checkBoxesList.get(i).getValue()) {
			numberOfBoxesChecked += 1;
			if (numberOfBoxesChecked > 0) {
				closeButton.setVisible(true);
				acknowledgementButton.setVisible(true);
			}
		}
		else {
			numberOfBoxesChecked -= 1;
			if (numberOfBoxesChecked == 0) {
				closeButton.setVisible(false);
				acknowledgementButton.setVisible(false);			
			}
		}
	}
}
