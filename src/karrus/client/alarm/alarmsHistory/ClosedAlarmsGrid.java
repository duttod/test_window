package karrus.client.alarm.alarmsHistory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karrus.client.generic.ResizableHeader;
import karrus.client.utils.DateTime;
import karrus.shared.hibernate.AlAlarmClosed;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class ClosedAlarmsGrid {

	// The list of data to display.
	private List<ClosedAlarmContent> ALARMS = new ArrayList<ClosedAlarmContent>();
	public DataGrid<ClosedAlarmContent> dataGrid;
	private ClosedAlarmContent lastSelectedAlarm;
	private SingleSelectionModel<ClosedAlarmContent> selectionModel;
	private boolean test = true;
	private Map<ClosedAlarmContent, Integer> alarmsToIndex= new HashMap<ClosedAlarmContent, Integer>();
	AlarmsHistoryPanel alarmsHistoryPanel;
	List<CheckBox> checkBoxsList = new ArrayList<CheckBox>();
	
	public ClosedAlarmsGrid(List<AlAlarmClosed> alarms, AlarmsHistoryPanel alarmsHistoryPanel) {
		this.alarmsHistoryPanel = alarmsHistoryPanel;
		boolean isSelection = false;
		AlAlarmClosed alarm;
		String openingTimestamp;
		String closingTimestamp;
		String acknowledgementTimestamp;
		String status;
		String type;
		String source;
		CheckBox checkBox  = new CheckBox();
		checkBoxsList.add(checkBox);
		for (int i=0;i<alarms.size();i++) {
			alarm = alarms.get(i);
			openingTimestamp = alarm.getId().getOpeningTimestamp() == null ? "" : DateTime.frenchDateTimeFormat.format(alarm.getId().getOpeningTimestamp());
			closingTimestamp = alarm.getClosingTimestamp() == null ? "" : DateTime.frenchDateTimeFormat.format(alarm.getClosingTimestamp());
			acknowledgementTimestamp = alarm.getAcknowledgementTimestamp() == null ? "" : DateTime.frenchDateTimeFormat.format(alarm.getAcknowledgementTimestamp());
			status = alarm.getStatus();
			type = alarm.getId().getType();
			source = alarm.getId().getSource();
			ClosedAlarmContent closedAlarmContent = new ClosedAlarmContent(openingTimestamp, closingTimestamp, acknowledgementTimestamp, status, type, source, false);
			ALARMS.add(closedAlarmContent);
			alarmsToIndex.put(closedAlarmContent, i);
		}
		// Create a CellTable.
		dataGrid = new DataGrid<ClosedAlarmContent>(ALARMS.size());
		//######################################################################
		Column<ClosedAlarmContent, Boolean> checkBoxesColumn = new Column<ClosedAlarmContent, Boolean>(new CheckboxCell()) {
			@Override
			public Boolean getValue(ClosedAlarmContent alarm) {
	            return alarm.getIsChecked();
	         }   
		};
		ResizableHeader<ClosedAlarmContent> resizeCheckBoxesColumn = new ResizableHeader<ClosedAlarmContent>(
				"", dataGrid, checkBoxesColumn );
		resizeCheckBoxesColumn.setHeaderStyleNames("headerStyle");
		dataGrid.addColumn(checkBoxesColumn, resizeCheckBoxesColumn);
		dataGrid.setColumnWidth(0, "40px");
		checkBoxesColumn.setFieldUpdater(new FieldUpdater<ClosedAlarmContent, Boolean>() {
		    @Override
		    public void update(int index, ClosedAlarmContent object, Boolean value) {
		    	object.setIsChecked(value);
		    	ClosedAlarmsGrid.this.alarmsHistoryPanel.setCheckBoxValue(index, value);
		    }
		});
		//######################################################################
		TextColumn<ClosedAlarmContent> typeColumn = new TextColumn<ClosedAlarmContent>() {
			@Override
			public String getValue(ClosedAlarmContent alarm) {
				return alarm.getType();
			}
		};
		ResizableHeader<ClosedAlarmContent> resizeTypeColumn = new ResizableHeader<ClosedAlarmContent>(
				"Type", dataGrid, typeColumn );
		resizeTypeColumn.setHeaderStyleNames("headerStyle");
		dataGrid.addColumn(typeColumn, resizeTypeColumn);
		dataGrid.setColumnWidth(1, "200px");
		//######################################################################
		TextColumn<ClosedAlarmContent> sourceColumn = new TextColumn<ClosedAlarmContent>() {
			@Override
			public String getValue(ClosedAlarmContent alarm) {
				return alarm.getSource();
			}
		};
		ResizableHeader<ClosedAlarmContent> resizeSourceColumn = new ResizableHeader<ClosedAlarmContent>(
				"Equipement", dataGrid, sourceColumn );
		resizeSourceColumn.setHeaderStyleNames("headerStyle");
		dataGrid.addColumn(sourceColumn, resizeSourceColumn);
		//######################################################################
		TextColumn<ClosedAlarmContent> statusColumn = new TextColumn<ClosedAlarmContent>() {
			@Override
			public String getValue(ClosedAlarmContent alarm) {
				return alarm.getStatus();
			}
		};
		ResizableHeader<ClosedAlarmContent> resizeStatusColumn = new ResizableHeader<ClosedAlarmContent>(
				"Statut", dataGrid, statusColumn );
		resizeStatusColumn.setHeaderStyleNames("headerStyle");
		dataGrid.addColumn(statusColumn, resizeStatusColumn);
		//######################################################################
		TextColumn<ClosedAlarmContent> openingTimestampColumn = new TextColumn<ClosedAlarmContent>() {
			@Override
			public String getValue(ClosedAlarmContent alarm) {
				return alarm.getOpeningTimestamp();
			}
		};
		ResizableHeader<ClosedAlarmContent> resizeOpeningTimestampColumn = new ResizableHeader<ClosedAlarmContent>(
				"Horodate de creation", dataGrid, openingTimestampColumn );
		resizeOpeningTimestampColumn.setHeaderStyleNames("headerStyle");
		dataGrid.addColumn(openingTimestampColumn, resizeOpeningTimestampColumn);
		//######################################################################
		TextColumn<ClosedAlarmContent> acknowledgementTimestampColumn = new TextColumn<ClosedAlarmContent>() {
			@Override
			public String getValue(ClosedAlarmContent alarm) {
				return alarm.getAcknowledgementTimestamp();
			}
		};
		ResizableHeader<ClosedAlarmContent> resizeAcknowledgementTimestampColumn = new ResizableHeader<ClosedAlarmContent>(
				"Horodate d'acquittement", dataGrid, acknowledgementTimestampColumn );
		resizeAcknowledgementTimestampColumn.setHeaderStyleNames("headerStyle");
		dataGrid.addColumn(acknowledgementTimestampColumn, resizeAcknowledgementTimestampColumn);
		//######################################################################
		TextColumn<ClosedAlarmContent> closingTimestampColumn = new TextColumn<ClosedAlarmContent>() {
			@Override
			public String getValue(ClosedAlarmContent alarm) {
				return alarm.getClosingTimestamp();
			}
		};
		ResizableHeader<ClosedAlarmContent> resizeClosingTimestampColumn = new ResizableHeader<ClosedAlarmContent>(
				"Horodate de cloture", dataGrid, closingTimestampColumn );
		resizeClosingTimestampColumn.setHeaderStyleNames("headerStyle");
		dataGrid.addColumn(closingTimestampColumn, resizeClosingTimestampColumn);
		//######################################################################
		dataGrid.setRowCount(ALARMS.size(), true);
		dataGrid.setRowData(0, ALARMS);
		dataGrid.addStyleName("bordered");
		if (isSelection) {
			selectionModel = new SingleSelectionModel<ClosedAlarmContent>();
			dataGrid.setSelectionModel(selectionModel);
			selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
				public void onSelectionChange(SelectionChangeEvent event) {
					ClosedAlarmContent selected = selectionModel.getSelectedObject();
			        if (selected != null) {
			        	lastSelectedAlarm = selected;
			        	test = false;
			        	Log.debug("You selected: " + selected.getIsChecked());
			        }
			    }
			});
			dataGrid.addHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {
						@Override
						public void execute() {
							ClosedAlarmContent currentSelectedSet = selectionModel
									.getSelectedObject();
							if (currentSelectedSet == lastSelectedAlarm && test == true) {
								selectionModel.clear();
								lastSelectedAlarm = null;
							}
							test = true;

						}
					});
				}
			}, ClickEvent.getType());
		}
		dataGrid.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		dataGrid.setHeight("300px");
		dataGrid.setWidth("1200px");
//		dataGrid.setPageSize(5);
//		dataGrid.setAlwaysShowScrollBars(true);
	}
	
	public Integer getSelectedStationIndex() {
		ClosedAlarmContent closedAlarmContent = selectionModel.getSelectedObject();
		if (closedAlarmContent == null) {
			return -1;
		}
		return alarmsToIndex.get(closedAlarmContent);
	}
	
	public List<ClosedAlarmContent> getVisibleAlarms() {
		return dataGrid.getVisibleItems();
	}
}