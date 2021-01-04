package karrus.client.equipments.station;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karrus.client.FrontalWebApp;
import karrus.client.generic.CustomDialogBox;
import karrus.shared.hibernate.RsuStation;
import karrus.shared.language.Language;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;

/**
 * Shows stations in a table.
 * 
 * @author estelle.dumas@karrus-its.com
 */
public class StationsTable extends Composite {

	private List<RsuStation> stations;
	private Map<Integer, RsuStation> indexToStation;
	private StationsConfigurationPanel stationsPanel;
	private StationsGrid stationsGrid;

	/**
	 * Constructor.
	 * @param stations
	 * @param stationsPanel
	 */
	public StationsTable(List<RsuStation> stations, StationsConfigurationPanel stationsPanel) {
		this.stations = stations;
		this.stationsGrid = new StationsGrid(this.stations);
		this.stationsPanel = stationsPanel;
		this.indexToStation = new HashMap<Integer, RsuStation>();
		for (int i = 0; i < stations.size(); i++) {
			indexToStation.put(i, stations.get(i));
		}
		this.initWidget(stationsGrid.dataGrid);
	}

	/**
	 * Opens a pop up panel to edit all parameters of the station.
	 * @throws Exception if no station is selected
	 */
	public void editStationParameters() throws Exception{
		int index = stationsGrid.getSelectedStationIndex();
		if (index==-1) {
			throw new Exception(Language.noSelectedStationError);
		}
		new StationEditableParametersPanel(indexToStation.get(index), this);
	}

	/**
	 * Opens a pop up panel to confirm the removing of the station.
	 * @throws Exception if no station is selected
	 */
	public void removeStation() throws Exception{
		int index = stationsGrid.getSelectedStationIndex();
		if (index==-1) {
			throw new Exception(Language.noSelectedStationError);
		}
		RsuStation seletedStation = indexToStation.get(index);
		final String selectedStationId = seletedStation.getId();
		final String selectedStationSerial = seletedStation.getSerial();
		String message = Language.confirmRemovingStation(selectedStationId);
		String button1Message = Language.yesString;
		ClickHandler action1 = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				removeStation(selectedStationId, selectedStationSerial);
			}
		};
		String button2Message = Language.noString;
		ClickHandler action2 = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				new CustomDialogBox(Language.stationNotRemoved(selectedStationId), Language.okString);
			}
		};
		new CustomDialogBox(message, button1Message, action1, button2Message, action2);
	}

	/**
	 * Removes the station, i.e. deletes it from the database
	 * @param stationId
	 * @param stationSerial
	 */
	private void removeStation(final String stationId, String stationSerial){
		FrontalWebApp.configurationsDatabaseService.removeStation(stationId, stationSerial, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				Log.debug("service.removeStation: ok. "+Language.stationRemoved(stationId));
				updateTable();
				FrontalWebApp.updateDatabaseObject();
			}
			@Override
			public void onFailure(Throwable caught) {
				Log.error("service.removeStation: failure.\n"+Language.stationNotRemoved(stationId));
				new CustomDialogBox(Language.stationNotRemoved(stationId), Language.okString);
			}
		}); 
	}

	/**
	 * Updates the stations table.
	 */
	public void updateTable(){
		this.stationsPanel.updateTable();
	}
}
