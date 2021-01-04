package karrus.client.alarm.currentAlarms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karrus.client.generic.ResizableHeader;
import karrus.client.utils.DateTime;
import karrus.shared.hibernate.AlAlarmOpened;
import karrus.shared.language.Language;

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

public class OpenedAlarmsGrid {

	// The list of data to display.
	private List<OpenedAlarmContent> ALARMS = new ArrayList<OpenedAlarmContent>();
	public DataGrid<OpenedAlarmContent> dataGrid;
	private OpenedAlarmContent lastSelectedAlarm;
	private SingleSelectionModel<OpenedAlarmContent> selectionModel;
	private boolean test = true;
	private Map<OpenedAlarmContent, Integer> alarmsToIndex= new HashMap<OpenedAlarmContent, Integer>();
	CurrentAlarmsPanel currentAlarmsPanel;
	List<CheckBox> checkBoxsList = new ArrayList<CheckBox>();
	
	public OpenedAlarmsGrid(List<AlAlarmOpened> alarms, CurrentAlarmsPanel currentAlarmsPanel) {
		this.currentAlarmsPanel = currentAlarmsPanel;
		boolean isSelection = false;
		AlAlarmOpened alarm;
		String openingTimestamp;
		String acknowledgementTimestamp;
		String status;
		String type;
		String source;
		CheckBox checkBox  = new CheckBox();
		checkBoxsList.add(checkBox);
		for (int i=0;i<alarms.size();i++) {
			alarm = alarms.get(i);
			openingTimestamp = alarm.getId().getOpeningTimestamp() == null ? "" : DateTime.frenchDateTimeFormat.format(alarm.getId().getOpeningTimestamp());
			acknowledgementTimestamp = alarm.getAcknowledgementTimestamp() == null ? "" : DateTime.frenchDateTimeFormat.format(alarm.getAcknowledgementTimestamp());
			status = alarm.getStatus();
			type = alarm.getId().getType();
			source = alarm.getId().getSource();
			OpenedAlarmContent openedAlarmContent = new OpenedAlarmContent(openingTimestamp, acknowledgementTimestamp, status, type, source, false);
			ALARMS.add(openedAlarmContent);
			alarmsToIndex.put(openedAlarmContent, i);
		}
		// Create a CellTable.
		dataGrid = new DataGrid<OpenedAlarmContent>(ALARMS.size());
		
		//######################################################################
		Column<OpenedAlarmContent, Boolean> checkBoxesColumn = new Column<OpenedAlarmContent, Boolean>(new CheckboxCell()) {
			@Override
			public Boolean getValue(OpenedAlarmContent alarm) {
	            return alarm.getIsChecked();
	         }   
		};
		ResizableHeader<OpenedAlarmContent> resizeCheckBoxesColumn = new ResizableHeader<OpenedAlarmContent>(
				"", dataGrid, checkBoxesColumn );
		resizeCheckBoxesColumn.setHeaderStyleNames("headerStyle");
		dataGrid.addColumn(checkBoxesColumn, resizeCheckBoxesColumn);
		dataGrid.setColumnWidth(0, "40px");
		checkBoxesColumn.setFieldUpdater(new FieldUpdater<OpenedAlarmContent, Boolean>() {
		    @Override
		    public void update(int index, OpenedAlarmContent object, Boolean value) {
		    	object.setIsChecked(value);
		    	OpenedAlarmsGrid.this.currentAlarmsPanel.setCheckBoxValue(index, value);
		    }
		});
		//######################################################################
		TextColumn<OpenedAlarmContent> typeColumn = new TextColumn<OpenedAlarmContent>() {
			@Override
			public String getValue(OpenedAlarmContent alarm) {
				return alarm.getType();
			}
		};
		ResizableHeader<OpenedAlarmContent> resizeTypeColumn = new ResizableHeader<OpenedAlarmContent>(
				Language.openedAlarmTypeHeader, dataGrid, typeColumn );
		resizeTypeColumn.setHeaderStyleNames("headerStyle");
		dataGrid.addColumn(typeColumn, resizeTypeColumn);
		dataGrid.setColumnWidth(1, "200px");
		//######################################################################
		TextColumn<OpenedAlarmContent> sourceColumn = new TextColumn<OpenedAlarmContent>() {
			@Override
			public String getValue(OpenedAlarmContent alarm) {
				return alarm.getSource();
			}
		};
		ResizableHeader<OpenedAlarmContent> resizeSourceColumn = new ResizableHeader<OpenedAlarmContent>(
				Language.openedAlarmSourceHeader, dataGrid, sourceColumn );
		resizeSourceColumn.setHeaderStyleNames("headerStyle");
		dataGrid.addColumn(sourceColumn, resizeSourceColumn);
		//######################################################################
		TextColumn<OpenedAlarmContent> statusColumn = new TextColumn<OpenedAlarmContent>() {
			@Override
			public String getValue(OpenedAlarmContent alarm) {
				return alarm.getStatus();
			}
		};
		ResizableHeader<OpenedAlarmContent> resizeStatusColumn = new ResizableHeader<OpenedAlarmContent>(
				Language.openedAlarmStatusHeader, dataGrid, statusColumn );
		resizeStatusColumn.setHeaderStyleNames("headerStyle");
		dataGrid.addColumn(statusColumn, resizeStatusColumn);
		//######################################################################
		TextColumn<OpenedAlarmContent> openingTimestampColumn = new TextColumn<OpenedAlarmContent>() {
			@Override
			public String getValue(OpenedAlarmContent alarm) {
				return alarm.getOpeningTimestamp();
			}
		};
		ResizableHeader<OpenedAlarmContent> resizeOpeningTimestampColumn = new ResizableHeader<OpenedAlarmContent>(
				Language.openedAlarmOpeningTimestampHeader, dataGrid, openingTimestampColumn );
		resizeOpeningTimestampColumn.setHeaderStyleNames("headerStyle");
		dataGrid.addColumn(openingTimestampColumn, resizeOpeningTimestampColumn);
		//######################################################################
		TextColumn<OpenedAlarmContent> acknowledgementTimestampColumn = new TextColumn<OpenedAlarmContent>() {
			@Override
			public String getValue(OpenedAlarmContent alarm) {
				return alarm.getAcknowledgementTimestamp();
			}
		};
		ResizableHeader<OpenedAlarmContent> resizeAcknowledgementTimestampColumn = new ResizableHeader<OpenedAlarmContent>(
				Language.openedAlarmAcknowledgementTimestampHeader, dataGrid, acknowledgementTimestampColumn );
		resizeAcknowledgementTimestampColumn.setHeaderStyleNames("headerStyle");
		dataGrid.addColumn(acknowledgementTimestampColumn, resizeAcknowledgementTimestampColumn);
		//######################################################################		
		dataGrid.setRowCount(ALARMS.size(), true);
		dataGrid.setRowData(0, ALARMS);
		dataGrid.addStyleName("bordered");
		if (isSelection) {
			selectionModel = new SingleSelectionModel<OpenedAlarmContent>();
			dataGrid.setSelectionModel(selectionModel);
			selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
				public void onSelectionChange(SelectionChangeEvent event) {
					OpenedAlarmContent selected = selectionModel.getSelectedObject();
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
							OpenedAlarmContent currentSelectedSet = selectionModel
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
		dataGrid.setWidth("1000px");
//		dataGrid.setPageSize(5);
//		dataGrid.setAlwaysShowScrollBars(true);
	}
	
	public Integer getSelectedStationIndex() {
		OpenedAlarmContent openedAlarmContent = selectionModel.getSelectedObject();
		if (openedAlarmContent == null) {
			return -1;
		}
		return alarmsToIndex.get(openedAlarmContent);
	}
	
	public List<OpenedAlarmContent> getVisibleAlarms() {
		return dataGrid.getVisibleItems();
	}
}