package karrus.client.equipments.station;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karrus.client.generic.ResizableHeader;
import karrus.shared.hibernate.RsuStation;
import karrus.shared.language.Language;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class StationsGrid {

	// The list of data to display.
	private List<StationContent> STATIONS = new ArrayList<StationContent>();
	public DataGrid<StationContent> dataGrid;
	private StationContent lastSelectedStation;
	private SingleSelectionModel<StationContent> selectionModel;
	private boolean test = true;
	private Map<StationContent, Integer> stationsToIndex= new HashMap<StationContent, Integer>();
	public StationsGrid(List<RsuStation> stations) {
		boolean isSelection = true;
		RsuStation station;
		String id;
		String serial;
		String host;
		String latitude;
		String longitude;
		String road;
		String position;
		String cycleCountingSec;
		String cycleTravelTimeSec;
		String cycleWeatherSec;
		String cycleV2XSec;
		for (int i=0;i<stations.size();i++) {
			station = stations.get(i);
			id = station.getId();
			serial = station.getSerial();
			host = station.getHost();
			latitude = station.getLatitude() == null ? "" : Double.toString(station.getLatitude());
			longitude = station.getLongitude() == null ? "" : Double.toString(station.getLongitude());
			road = station.getRoad();
			position = station.getPosition();
			cycleCountingSec = station.getCycleCountingSec() == null ? "" : Integer.toString(station.getCycleCountingSec());
			cycleTravelTimeSec = station.getCycleTravelTimeSec() == null ? "" : Integer.toString(station.getCycleTravelTimeSec());
			cycleWeatherSec = station.getCycleWeatherSec() == null ? "" : Integer.toString(station.getCycleWeatherSec());
			cycleV2XSec = station.getCycleV2xSec() == null ? "" : Integer.toString(station.getCycleV2xSec());
			StationContent stationContent = new StationContent(id, serial, host, latitude, longitude, road, position, cycleCountingSec, cycleTravelTimeSec,
					cycleWeatherSec, cycleV2XSec);
			STATIONS.add(stationContent);
			stationsToIndex.put(stationContent, i);
		}
		dataGrid = new DataGrid<StationContent>();
		//######################################################################
		TextColumn<StationContent> idColumn = new TextColumn<StationContent>() {
			@Override
			public String getValue(StationContent stations) {
				return stations.getId();
			}
		};
		ResizableHeader<StationContent> resizeIdColumn = new ResizableHeader<StationContent>(
				Language.stationId, dataGrid, idColumn );
		resizeIdColumn.setHeaderStyleNames("headerStyle");
		dataGrid.addColumn(idColumn, resizeIdColumn);
		//######################################################################
		TextColumn<StationContent> serialColumn = new TextColumn<StationContent>() {
			@Override
			public String getValue(StationContent stations) {
				return stations.getSerial();
			}
		};
		ResizableHeader<StationContent> resizeSerialColumn = new ResizableHeader<StationContent>(
				Language.stationSerial, dataGrid, serialColumn );
		resizeSerialColumn.setHeaderStyleNames("headerStyle");
		dataGrid.addColumn(serialColumn, resizeSerialColumn);
		//######################################################################
		TextColumn<StationContent> hostColumn = new TextColumn<StationContent>() {
			@Override
			public String getValue(StationContent stations) {
				return stations.getHost();
			}
		};
		ResizableHeader<StationContent> resizeHostColumn = new ResizableHeader<StationContent>(
			 Language.stationHost, dataGrid, hostColumn );
		resizeHostColumn.setHeaderStyleNames("headerStyle");
		dataGrid.addColumn(hostColumn, resizeHostColumn);
		//######################################################################
		TextColumn<StationContent> latitudeColumn = new TextColumn<StationContent>() {
			@Override
			public String getValue(StationContent stations) {
				return stations.getLatitude();
			}
		};
		ResizableHeader<StationContent> resizeLatitudeColumn = new ResizableHeader<StationContent>(
				Language.stationLatitude, dataGrid, latitudeColumn );
		resizeLatitudeColumn.setHeaderStyleNames("headerStyle");
		dataGrid.addColumn(latitudeColumn, resizeLatitudeColumn);
		//######################################################################
		TextColumn<StationContent> longitudeColumn = new TextColumn<StationContent>() {
			@Override
			public String getValue(StationContent stations) {
				return stations.getLongitude();
			}
		};
		ResizableHeader<StationContent> resizeLongitudeColumn = new ResizableHeader<StationContent>(
				Language.stationLongitude, dataGrid, longitudeColumn );
		resizeLongitudeColumn.setHeaderStyleNames("headerStyle");
		dataGrid.addColumn(longitudeColumn, resizeLongitudeColumn);
		//######################################################################
		TextColumn<StationContent> roadColumn = new TextColumn<StationContent>() {
			@Override
			public String getValue(StationContent stations) {
				return stations.getRoad();
			}
		};
		ResizableHeader<StationContent> resizeRoadColumn = new ResizableHeader<StationContent>(
				Language.stationRoad, dataGrid, roadColumn );
		resizeRoadColumn.setHeaderStyleNames("headerStyle");
		dataGrid.addColumn(roadColumn, resizeRoadColumn);
		dataGrid.setColumnWidth(5, "100px");
		//######################################################################
		TextColumn<StationContent> positionColumn = new TextColumn<StationContent>() {
			@Override
			public String getValue(StationContent stations) {
				return stations.getPosition();
			}
		};
		ResizableHeader<StationContent> resizePositionColumn = new ResizableHeader<StationContent>(
				Language.stationPosition, dataGrid, positionColumn );
		resizePositionColumn.setHeaderStyleNames("headerStyle");
		dataGrid.addColumn(positionColumn, resizePositionColumn);
		dataGrid.setColumnWidth(6, "100px");
		//######################################################################
		TextColumn<StationContent> cycleCountColumn = new TextColumn<StationContent>() {
			@Override
			public String getValue(StationContent stations) {
				return stations.getCycleCountingSec();
			}
		};
		ResizableHeader<StationContent> resizeCycleCountColumn = new ResizableHeader<StationContent>(
				Language.stationCycleCountingSec, dataGrid, cycleCountColumn);
		resizeCycleCountColumn.setHeaderStyleNames("headerStyle");
		dataGrid.addColumn(cycleCountColumn, resizeCycleCountColumn);
		dataGrid.setColumnWidth(7, "100px");
		//######################################################################
		TextColumn<StationContent> cycleTravelTimeColumn = new TextColumn<StationContent>() {
			@Override
			public String getValue(StationContent stations) {
				return stations.getCycleTravelTimeSec();
			}
		};
		ResizableHeader<StationContent> resizeCycleTravelTimeColumn = new ResizableHeader<StationContent>(
				Language.stationCycleTravelTimeSec, dataGrid, cycleTravelTimeColumn );
		resizeCycleTravelTimeColumn.setHeaderStyleNames("headerStyle");
		dataGrid.addColumn(cycleTravelTimeColumn, resizeCycleTravelTimeColumn);
		dataGrid.setColumnWidth(8, "100px");
		//######################################################################
		TextColumn<StationContent> cycleWeatherColumn = new TextColumn<StationContent>() {
			@Override
			public String getValue(StationContent stations) {
				return stations.getCycleWeatherSec();
			}
		};
		ResizableHeader<StationContent> resizeCycleWeatherColumn = new ResizableHeader<StationContent>(
				Language.stationCycleWeatherSec, dataGrid, cycleWeatherColumn );
		resizeCycleWeatherColumn.setHeaderStyleNames("headerStyle");
		dataGrid.addColumn(cycleWeatherColumn, resizeCycleWeatherColumn);
		dataGrid.setColumnWidth(9, "100px");
		//######################################################################
		TextColumn<StationContent> cycleV2XColumn = new TextColumn<StationContent>() {
			@Override
			public String getValue(StationContent stations) {
				return stations.getCycleV2XSec();
			}
		};
		ResizableHeader<StationContent> resizeCycleV2XColumn = new ResizableHeader<StationContent>(
				Language.stationCycleV2XSec, dataGrid, cycleV2XColumn);
		resizeCycleV2XColumn.setHeaderStyleNames("headerStyle");
		dataGrid.addColumn(cycleV2XColumn, resizeCycleV2XColumn);
		dataGrid.setColumnWidth(10, "100px");
		//######################################################################
		dataGrid.setRowCount(STATIONS.size(), true);
		dataGrid.setRowData(0, STATIONS);
		dataGrid.addStyleName("bordered");
		if (isSelection) {
			selectionModel = new SingleSelectionModel<StationContent>();
			dataGrid.setSelectionModel(selectionModel);
			selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
				public void onSelectionChange(SelectionChangeEvent event) {
					StationContent selected = selectionModel.getSelectedObject();
			        if (selected != null) {
			        	lastSelectedStation = selected;
			        	test = false;
			        }
			    }
			});
			dataGrid.addHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {
						@Override
						public void execute() {
							StationContent currentSelectedSet = selectionModel
									.getSelectedObject();
							if (currentSelectedSet == lastSelectedStation && test == true) {
								selectionModel.clear();
								lastSelectedStation = null;
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
	}
	
	public Integer getSelectedStationIndex() {
		StationContent stationContent = selectionModel.getSelectedObject();
		if (stationContent == null) {
			return -1;
		}
		return stationsToIndex.get(stationContent);
	}
}