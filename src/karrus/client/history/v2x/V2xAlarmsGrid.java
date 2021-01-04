package karrus.client.history.v2x;

import java.util.ArrayList;
import java.util.List;

import karrus.client.generic.ResizableHeader;
import karrus.shared.hibernate.V2xAlarm;
import karrus.shared.language.Language;
import karrus.client.appearance.Css;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class V2xAlarmsGrid {

	// The list of data to display.
	private List<V2xAlarm> V2X_ALARMS = new ArrayList<V2xAlarm>();
	private DataGrid<V2xAlarm> dataGrid;
	private SingleSelectionModel<V2xAlarm> selectionModel;
	private boolean unselectFlag = true;
	private DateTimeFormat dateTimeFormatter = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss");
	
	public V2xAlarmsGrid(List<V2xAlarm> v2xAlarms) {
		boolean isSelection = true;
		V2X_ALARMS = v2xAlarms;
		// Create a CellTable.
		dataGrid = new DataGrid<V2xAlarm>();
		//######################################################################
		TextColumn<V2xAlarm> timestampColumn = new TextColumn<V2xAlarm>() {
			@Override
			public String getValue(V2xAlarm v2xAlarm) {
				return v2xAlarm.getId().getTimestamp() == null ? "" : dateTimeFormatter.format(v2xAlarm.getId().getTimestamp());
			}
		};
		ResizableHeader<V2xAlarm> timestampColumnResizableHeader = new ResizableHeader<V2xAlarm>(
				Language.v2xAlarmsGridTimestampHeaderLabel, dataGrid, timestampColumn);
		timestampColumnResizableHeader.setHeaderStyleNames(Css.gridHeaderStyle);
		dataGrid.addColumn(timestampColumn, timestampColumnResizableHeader);
		//######################################################################
		TextColumn<V2xAlarm> stationColumn = new TextColumn<V2xAlarm>() {
			@Override
			public String getValue(V2xAlarm v2xAlarm) {
				return v2xAlarm.getId().getStation() == null ? "" : v2xAlarm.getId().getStation();
			}
		};
		ResizableHeader<V2xAlarm> stationColumnResizableHeader = new ResizableHeader<V2xAlarm>(
				Language.v2xAlarmsGridStationHeaderLabel, dataGrid, stationColumn);
		stationColumnResizableHeader.setHeaderStyleNames(Css.gridHeaderStyle);
		dataGrid.addColumn(stationColumn, stationColumnResizableHeader);
		//######################################################################
		TextColumn<V2xAlarm> typeColumn = new TextColumn<V2xAlarm>() {
			@Override
			public String getValue(V2xAlarm v2xAlarm) {
				return v2xAlarm.getType() == null ? "" : v2xAlarm.getType();
			}
		};
		ResizableHeader<V2xAlarm> typeColumnResizableHeader = new ResizableHeader<V2xAlarm>(
				Language.v2xAlarmsGridTypeHeaderLabel, dataGrid, typeColumn);
		typeColumnResizableHeader.setHeaderStyleNames(Css.gridHeaderStyle);
		dataGrid.addColumn(typeColumn, typeColumnResizableHeader);
		dataGrid.setColumnWidth(typeColumn, "100px");
		//######################################################################
		TextColumn<V2xAlarm> codeColumn = new TextColumn<V2xAlarm>() {
			@Override
			public String getValue(V2xAlarm v2xAlarm) {
				return v2xAlarm.getCode() == null ? "" : Integer.toString(v2xAlarm.getCode());
			}
		};
		ResizableHeader<V2xAlarm> codeColumnResizableHeader = new ResizableHeader<V2xAlarm>(
				Language.v2xAlarmsGridCodeHeaderLabel, dataGrid, codeColumn);
		codeColumnResizableHeader.setHeaderStyleNames(Css.gridHeaderStyle);
		dataGrid.addColumn(codeColumn, codeColumnResizableHeader);
		dataGrid.setColumnWidth(codeColumn, "100px");
		//######################################################################
		TextColumn<V2xAlarm> idEventColumn = new TextColumn<V2xAlarm>() {
			@Override
			public String getValue(V2xAlarm v2xAlarm) {
				return v2xAlarm.getIdEvent() == null ? "" : v2xAlarm.getIdEvent();
			}
		};
		ResizableHeader<V2xAlarm> idEventColumnResizableHeader = new ResizableHeader<V2xAlarm>(
				Language.v2xAlarmsGridIdEventHeaderLabel, dataGrid, idEventColumn);
		idEventColumnResizableHeader.setHeaderStyleNames(Css.gridHeaderStyle);
		dataGrid.addColumn(idEventColumn, idEventColumnResizableHeader);
		dataGrid.setColumnWidth(idEventColumn, "100px");
		//######################################################################
		TextColumn<V2xAlarm> idVehicleColumn = new TextColumn<V2xAlarm>() {
			@Override
			public String getValue(V2xAlarm v2xAlarm) {
				return v2xAlarm.getIdVehicule() == null ? "" : v2xAlarm.getIdVehicule();
			}
		};
		ResizableHeader<V2xAlarm> idVehicleColumnResizableHeader = new ResizableHeader<V2xAlarm>(
				Language.v2xAlarmsGridIdVehicleHeaderLabel, dataGrid, idVehicleColumn);
		idVehicleColumnResizableHeader.setHeaderStyleNames(Css.gridHeaderStyle);
		dataGrid.addColumn(idVehicleColumn, idVehicleColumnResizableHeader);
		dataGrid.setColumnWidth(idVehicleColumn, "100px");
		//######################################################################
		TextColumn<V2xAlarm> gpsLatitudeColumn = new TextColumn<V2xAlarm>() {
			@Override
			public String getValue(V2xAlarm v2xAlarm) {
				return v2xAlarm.getGpsLatitude() == null ? "" : Double.toString(v2xAlarm.getGpsLatitude());
			}
		};
		ResizableHeader<V2xAlarm> gpsLatitudeColumnResizableHeader = new ResizableHeader<V2xAlarm>(
				Language.v2xAlarmsGridGpsLatitudeHeaderLabel, dataGrid, gpsLatitudeColumn);
		gpsLatitudeColumnResizableHeader.setHeaderStyleNames(Css.gridHeaderStyle);
		dataGrid.addColumn(gpsLatitudeColumn, gpsLatitudeColumnResizableHeader);
		dataGrid.setColumnWidth(gpsLatitudeColumn, "100px");
		//######################################################################
		TextColumn<V2xAlarm> gpsLongitudeColumn = new TextColumn<V2xAlarm>() {
			@Override
			public String getValue(V2xAlarm v2xAlarm) {
				return v2xAlarm.getGpsLongitude() == null ? "" : Double.toString(v2xAlarm.getGpsLongitude());
			}
		};
		ResizableHeader<V2xAlarm> gpsLongitudeColumnResizableHeader = new ResizableHeader<V2xAlarm>(
				Language.v2xAlarmsGridGpsLongitudeHeaderLabel, dataGrid, gpsLongitudeColumn);
		gpsLongitudeColumnResizableHeader.setHeaderStyleNames(Css.gridHeaderStyle);
		dataGrid.addColumn(gpsLongitudeColumn, gpsLongitudeColumnResizableHeader);
		dataGrid.setColumnWidth(gpsLongitudeColumn, "100px");
		//######################################################################
		TextColumn<V2xAlarm> statusColumn = new TextColumn<V2xAlarm>() {
			@Override
			public String getValue(V2xAlarm v2xAlarm) {
				return v2xAlarm.getStatus() == null ? "" : v2xAlarm.getStatus();
			}
		};
		ResizableHeader<V2xAlarm> statusColumnResizableHeader = new ResizableHeader<V2xAlarm>(
				Language.v2xAlarmsGridStatusHeaderLabel, dataGrid, statusColumn);
		statusColumnResizableHeader.setHeaderStyleNames(Css.gridHeaderStyle);
		dataGrid.addColumn(statusColumn, statusColumnResizableHeader);
		dataGrid.setColumnWidth(statusColumn, "100px");
		//######################################################################
		TextColumn<V2xAlarm> textColumn = new TextColumn<V2xAlarm>() {
			@Override
			public String getValue(V2xAlarm v2xAlarm) {
				return v2xAlarm.getText() == null ? "" : v2xAlarm.getText();
			}
		};
		ResizableHeader<V2xAlarm> textColumnResizableHeader = new ResizableHeader<V2xAlarm>(
				Language.v2xAlarmsGridTextHeaderLabel, dataGrid, textColumn);
		textColumnResizableHeader.setHeaderStyleNames(Css.gridHeaderStyle);
		dataGrid.addColumn(textColumn, textColumnResizableHeader);
		//######################################################################
		dataGrid.setRowCount(V2X_ALARMS.size(), true);
		dataGrid.setRowData(0, V2X_ALARMS);
		dataGrid.addStyleName(Css.borderStyle);
		if (isSelection) {
			selectionModel = new SingleSelectionModel<V2xAlarm>();
			dataGrid.setSelectionModel(selectionModel);
			selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
				public void onSelectionChange(SelectionChangeEvent event) {
					unselectFlag = false;
			    }
			});
			dataGrid.addHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {
						@Override
						public void execute() {
							if (unselectFlag == true) {
								selectionModel.clear();
							}
							unselectFlag = true;
						}
					});
				}
			}, ClickEvent.getType());
		}
		dataGrid.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		dataGrid.setHeight("700px");
		dataGrid.setWidth("1500px");
	}
	
	public DataGrid<V2xAlarm> getDatagrid() {
		return this.dataGrid;
	}
}