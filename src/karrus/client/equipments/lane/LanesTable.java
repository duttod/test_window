package karrus.client.equipments.lane;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karrus.client.FrontalWebApp;
import karrus.client.generic.CustomDialogBox;
import karrus.shared.hibernate.CtLane;
import karrus.shared.language.Language;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;

/**
 * Shows the lanes in a table.
 * 
 * @author estelle.dumas@karrus-its.com
 */
public class LanesTable extends Composite {

	private List<CtLane> lanes;
	private Map<Integer, CtLane> indexToLane;
	private LanesConfigurationPanel lanesConfigurationPanel;
	private LanesGrid lanesGrid;

	/**
	 * Constructor.
	 * @param lanes
	 * @param lanesConfigurationPanel
	 */
	public LanesTable(List<CtLane> lanes, LanesConfigurationPanel lanesConfigurationPanel) {
		this.lanes = lanes;
		this.lanesGrid = new LanesGrid(this.lanes);
		this.lanesConfigurationPanel = lanesConfigurationPanel;
		this.indexToLane = new HashMap<Integer, CtLane>();
		for (int i = 0; i < lanes.size(); i++) {
			indexToLane.put(i, lanes.get(i));
		}
		this.initWidget(lanesGrid.dataGrid);
	}

	/**
	 * Opens a pop up panel to edit all parameters of the lane.
	 * @throws Exception if no lane is selected
	 */
	public void editLaneParameters() throws Exception {
		int index = lanesGrid.getSelectedLaneIndex();
		if (index==-1) {
			throw new Exception(Language.noSelectedLaneError);
		}
		new LaneEditableParametersPanel(indexToLane.get(index), this);
	}

	/**
	 * Opens a pop up panel to confirm the removal of the lane.
	 * @throws Exception if no lane is selected
	 */
	public void removeLane() throws Exception {
		int index = lanesGrid.getSelectedLaneIndex();
		if (index==-1) {
			throw new Exception(Language.noSelectedLaneError);
		}
		final CtLane selectedLane = indexToLane.get(index);
		String message = Language.confirmRemovingLane(selectedLane);
		String button1Message = Language.yesString;
		ClickHandler action1 = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				removeLane(selectedLane);
			}
		};
		String button2Message = Language.noString;
		ClickHandler action2 = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				new CustomDialogBox(Language.laneNotRemoved(selectedLane), Language.okString);
			}
		};
		new CustomDialogBox(message, button1Message, action1, button2Message, action2);
	}

	/**
	 * Removes the lane, i.e. deletes it from the database
	 * @param lane
	 */
	private void removeLane(final CtLane lane){
		FrontalWebApp.configurationsDatabaseService.removeLane(lane, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				Log.debug("configurationsDatabaseService.removeLane: ok.\n" + Language.laneRemoved(lane));
				updateTable();
				FrontalWebApp.updateDatabaseObject();
			}
			@Override
			public void onFailure(Throwable caught) {
				Log.error("configurationsDatabaseService.removeLane: failure.\n" + Language.laneNotRemoved(lane));
				new CustomDialogBox(Language.laneNotRemoved(lane), Language.okString);
			}
		}); 
	}

	/**
	 * Updates the lanes table.
	 */
	public void updateTable(){
		this.lanesConfigurationPanel.updateTable();
	}
}
