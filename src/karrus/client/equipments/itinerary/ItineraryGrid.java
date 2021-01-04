package karrus.client.equipments.itinerary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karrus.client.generic.ResizableHeader;
import karrus.shared.hibernate.TtItinerary;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class ItineraryGrid {

	// The list of data to display.
	private List<ItineraryContent> ITINERARIES = new ArrayList<ItineraryContent>();
	public DataGrid<ItineraryContent> dataGrid;
	private ItineraryContent lastSelectedItinerary;
	private SingleSelectionModel<ItineraryContent> selectionModel;
	private boolean test = true;
	private Map<ItineraryContent, Integer> itinerariesToIndex= new HashMap<ItineraryContent, Integer>();
	
	public ItineraryGrid(List<TtItinerary> itineraries) {
		boolean isSelection = true;
		TtItinerary itinerary;
		String origin;
		String destination;
		String name;
		String nominalTravelTime;
		String maxTravelTime;
		String levelOfServiceOne;
		String levelOfServiceTwo;
		String scale;
		for (int i=0;i<itineraries.size();i++) {
			itinerary = itineraries.get(i);
			origin = itinerary.getId().getOrigin();
			destination = itinerary.getId().getDestination();
			name = itinerary.getName();
			nominalTravelTime = itinerary.getNominalTravelTime() == null ? "" : Integer.toString(itinerary.getNominalTravelTime());
			maxTravelTime = itinerary.getMaxTravelTime() == null ? "" : Integer.toString(itinerary.getMaxTravelTime());
			levelOfServiceOne = itinerary.getLevelOfServiceThreshold1() == null ? "" : Integer.toString(itinerary.getLevelOfServiceThreshold1());
			levelOfServiceTwo = itinerary.getLevelOfServiceThreshold2() == null ? "" : Integer.toString(itinerary.getLevelOfServiceThreshold2());
			scale = Integer.toString(itinerary.getScale());
			ItineraryContent itineraryContent = new ItineraryContent(origin, destination, name, nominalTravelTime, maxTravelTime, levelOfServiceOne, levelOfServiceTwo, scale);
			ITINERARIES.add(itineraryContent);
			itinerariesToIndex.put(itineraryContent, i);
		}
		// Create a CellTable.
		dataGrid = new DataGrid<ItineraryContent>();
		TextColumn<ItineraryContent> originColumn = new TextColumn<ItineraryContent>() {
			@Override
			public String getValue(ItineraryContent itinerary) {
				return itinerary.getOrigin();
			}
		};
		ResizableHeader<ItineraryContent> resizeOriginColumn = new ResizableHeader<ItineraryContent>(
				"Origine", dataGrid, originColumn );
		resizeOriginColumn.setHeaderStyleNames("headerStyle");
		dataGrid.addColumn(originColumn, resizeOriginColumn);
		//######################################################################
		TextColumn<ItineraryContent> destinationColumn = new TextColumn<ItineraryContent>() {
			@Override
			public String getValue(ItineraryContent itinerary) {
				return itinerary.getDestination();
			}
		};
		ResizableHeader<ItineraryContent> resizeDestinationColumn = new ResizableHeader<ItineraryContent>(
				"Destination", dataGrid, destinationColumn );
		resizeDestinationColumn.setHeaderStyleNames("headerStyle");
		dataGrid.addColumn(destinationColumn, resizeDestinationColumn);
		//######################################################################		
//		TextColumn<ItineraryContent> nameColumn = new TextColumn<ItineraryContent>() {
//			@Override
//			public String getValue(ItineraryContent itinerary) {
//				return itinerary.getName();
//			}
//		};
//		ResizableHeader<ItineraryContent> resizeNameColumn = new ResizableHeader<ItineraryContent>(
//				"Nom", dataGrid, nameColumn );
//		resizeNameColumn.setHeaderStyleNames("headerStyle");
//		dataGrid.addColumn(nameColumn, resizeNameColumn);
		//######################################################################
		TextColumn<ItineraryContent> nominalTravelTimeColumn = new TextColumn<ItineraryContent>() {
			@Override
			public String getValue(ItineraryContent itinerary) {
				return itinerary.getNominalTravelTime();
			}
		};
		ResizableHeader<ItineraryContent> resizeNominalTravelTimeColumn = new ResizableHeader<ItineraryContent>(
				"TP (s)", dataGrid, nominalTravelTimeColumn );
		resizeNominalTravelTimeColumn.setHeaderStyleNames("headerStyle");
		dataGrid.addColumn(nominalTravelTimeColumn, resizeNominalTravelTimeColumn);
		dataGrid.setColumnWidth(2, "100px");
		//######################################################################
		TextColumn<ItineraryContent> maxTravelTimeColumn = new TextColumn<ItineraryContent>() {
			@Override
			public String getValue(ItineraryContent itinerary) {
				return itinerary.getMaxTravelTime();
			}
		};
		ResizableHeader<ItineraryContent> resizeMaxTravelTimeColumn = new ResizableHeader<ItineraryContent>(
				"TP Max (s)", dataGrid, maxTravelTimeColumn );
		resizeMaxTravelTimeColumn.setHeaderStyleNames("headerStyle");
		dataGrid.addColumn(maxTravelTimeColumn, resizeMaxTravelTimeColumn);
		dataGrid.setColumnWidth(3, "100px");
		//######################################################################
		TextColumn<ItineraryContent> levelOfServiceOneColumn = new TextColumn<ItineraryContent>() {
			@Override
			public String getValue(ItineraryContent itinerary) {
				return itinerary.getLevelOfServiceThreshold1();
			}
		};
		ResizableHeader<ItineraryContent> resizeLevelOfServiceOneColumn = new ResizableHeader<ItineraryContent>(
				"NDS1 (s)", dataGrid, levelOfServiceOneColumn );
		resizeLevelOfServiceOneColumn.setHeaderStyleNames("headerStyle");
		dataGrid.addColumn(levelOfServiceOneColumn, resizeLevelOfServiceOneColumn);
		dataGrid.setColumnWidth(4, "100px");
		//######################################################################
		TextColumn<ItineraryContent> levelOfServiceTwoColumn = new TextColumn<ItineraryContent>() {
			@Override
			public String getValue(ItineraryContent itinerary) {
				return itinerary.getLevelOfServiceThreshold2();
			}
		};
		ResizableHeader<ItineraryContent> resizelevelOfServiceTwoColumn = new ResizableHeader<ItineraryContent>(
				"NDS2 (s)", dataGrid, levelOfServiceTwoColumn );
		resizelevelOfServiceTwoColumn.setHeaderStyleNames("headerStyle");
		dataGrid.addColumn(levelOfServiceTwoColumn, resizelevelOfServiceTwoColumn);
		dataGrid.setColumnWidth(5, "100px");
		//######################################################################
		TextColumn<ItineraryContent> scaleColumn = new TextColumn<ItineraryContent>() {
			@Override
			public String getValue(ItineraryContent itinerary) {
				return itinerary.getScale();
			}
		};
		ResizableHeader<ItineraryContent> resizeScaleColumn = new ResizableHeader<ItineraryContent>(
				"Echelle (s)", dataGrid, scaleColumn );
		resizeScaleColumn.setHeaderStyleNames("headerStyle");
		dataGrid.addColumn(scaleColumn, resizeScaleColumn);
		dataGrid.setColumnWidth(6, "100px");
		//######################################################################
		dataGrid.setRowCount(ITINERARIES.size(), true);
		dataGrid.setRowData(0, ITINERARIES);
		dataGrid.addStyleName("bordered");
		if (isSelection) {
			selectionModel = new SingleSelectionModel<ItineraryContent>();
			dataGrid.setSelectionModel(selectionModel);
			selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
				public void onSelectionChange(SelectionChangeEvent event) {
					ItineraryContent selected = selectionModel.getSelectedObject();
			        if (selected != null) {
			        	lastSelectedItinerary = selected;
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
							ItineraryContent currentSelectedSet = selectionModel
									.getSelectedObject();
							if (currentSelectedSet == lastSelectedItinerary && test == true) {
								selectionModel.clear();
								lastSelectedItinerary = null;
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
	
	public Integer getSelectedItineraryIndex() {
		ItineraryContent itineraryContent = selectionModel.getSelectedObject();
		if (itineraryContent == null) {
			return -1;
		}
		return itinerariesToIndex.get(itineraryContent);
	}
}