package karrus.client.equipments.lane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karrus.client.generic.ResizableHeader;
import karrus.shared.hibernate.CtLane;
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

public class LanesGrid {

	// The list of data to display.
	private List<CtLane> LANES = new ArrayList<CtLane>();
	public DataGrid<CtLane> dataGrid;
	private SingleSelectionModel<CtLane> selectionModel;
	private boolean unselectFlag = true;
	private Map<CtLane, Integer> lanesToIndex= new HashMap<CtLane, Integer>();
	
	public LanesGrid(List<CtLane> lanes) {
		boolean isSelection = true;
		for (int i=0;i<lanes.size();i++) {
			LANES.add(lanes.get(i));
			lanesToIndex.put(lanes.get(i), i);
		}
		dataGrid = new DataGrid<CtLane>();
		//######################################################################
		TextColumn<CtLane> stationColumn = new TextColumn<CtLane>() {
			@Override
			public String getValue(CtLane lane) {
				return lane.getId().getStation() == null ? "" : lane.getId().getStation();
			}
		};
		ResizableHeader<CtLane> stationColumnHeader = new ResizableHeader<CtLane>(
				Language.lanesGridStationHeader, dataGrid, stationColumn);
		stationColumnHeader.setHeaderStyleNames("headerStyle");
		dataGrid.addColumn(stationColumn, stationColumnHeader);
		//######################################################################
		TextColumn<CtLane> laneColumn = new TextColumn<CtLane>() {
			@Override
			public String getValue(CtLane lane) {
				return lane.getId().getLane() == null ? "" : lane.getId().getLane();
			}
		};
		ResizableHeader<CtLane> laneColumnHeader = new ResizableHeader<CtLane>(
				Language.lanesGridLaneHeader, dataGrid, laneColumn);
		laneColumnHeader.setHeaderStyleNames("headerStyle");
		dataGrid.addColumn(laneColumn, laneColumnHeader);
		//######################################################################
		TextColumn<CtLane> roadColumn = new TextColumn<CtLane>() {
			@Override
			public String getValue(CtLane lane) {
				return lane.getRoad() == null ? "" : lane.getRoad();
			}
		};
		ResizableHeader<CtLane> roadColumnHeader = new ResizableHeader<CtLane>(
				Language.lanesGridRoadHeader, dataGrid, roadColumn);
		roadColumnHeader.setHeaderStyleNames("headerStyle");
		dataGrid.addColumn(roadColumn, roadColumnHeader);
		//######################################################################
		TextColumn<CtLane> wayColumn = new TextColumn<CtLane>() {
			@Override
			public String getValue(CtLane lane) {
				return lane.getWay() == null ? "" : lane.getWay();
			}
		};
		ResizableHeader<CtLane> wayColumnHeader = new ResizableHeader<CtLane>(
				Language.lanesGridWayHeader, dataGrid, wayColumn);
		wayColumnHeader.setHeaderStyleNames("headerStyle");
		dataGrid.addColumn(wayColumn, wayColumnHeader);
		//######################################################################
		TextColumn<CtLane> positionColumn = new TextColumn<CtLane>() {
			@Override
			public String getValue(CtLane lane) {
				return lane.getPosition() == null ? "" : lane.getPosition();
			}
		};
		ResizableHeader<CtLane> positionColumnHeader = new ResizableHeader<CtLane>(
				Language.lanesGridPositionHeader, dataGrid, positionColumn);
		positionColumnHeader.setHeaderStyleNames("headerStyle");
		dataGrid.addColumn(positionColumn, positionColumnHeader);
		//######################################################################
		dataGrid.setRowCount(LANES.size(), true);
		dataGrid.setRowData(0, LANES);
		dataGrid.addStyleName("bordered");
		if (isSelection) {
			selectionModel = new SingleSelectionModel<CtLane>();
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
		dataGrid.setHeight("300px");
		dataGrid.setWidth("1200px");	
	}
	
	public Integer getSelectedLaneIndex() {
		CtLane lane = selectionModel.getSelectedObject();
		if (lane == null) {
			return -1;
		}
		return lanesToIndex.get(lane);
	}
}