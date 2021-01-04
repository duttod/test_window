package karrus.client.equipments.lane;

import karrus.client.FrontalWebApp;
import karrus.client.appearance.Css;
import karrus.client.generic.CustomDialogBox;
import karrus.client.generic.TextBoxGeneric;
import karrus.shared.ApplicationException;
import karrus.shared.hibernate.CtLane;
import karrus.shared.hibernate.CtLaneId;
import karrus.shared.language.Language;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Creates a pop up panel to edit all parameters of the lane.
 * 
 * @author estelle.dumas@karrus-its.com
 */
public class LaneEditableParametersPanel {

	private TextBoxGeneric stationPanel;
	private TextBoxGeneric lanePanel;
	private TextBoxGeneric roadPanel;
	private TextBoxGeneric wayPanel;
	private TextBoxGeneric positionPanel;
	private CtLane lane;
	private DialogBox dialogBox;
	private LanesTable table;

	/**
	 * Constructor.
	 * @param lane
	 * @param table
	 */
	public LaneEditableParametersPanel(CtLane lane, LanesTable table) {
		this.table = table;
		this.lane = lane;
		this.stationPanel = new TextBoxGeneric(Language.laneFieldStation, this.lane == null ? "" : (this.lane.getId().getStation() == null ? "" : this.lane.getId().getStation()));
		this.lanePanel = new TextBoxGeneric(Language.laneFieldLane, this.lane == null ? "" : (this.lane.getId().getLane() == null ? "" : this.lane.getId().getLane()));
		this.roadPanel = new TextBoxGeneric(Language.laneFieldRoad, this.lane == null ? "" : (this.lane.getRoad() == null ? "" : this.lane.getRoad()));
		this.wayPanel = new TextBoxGeneric(Language.laneFieldWay, this.lane == null ? "" : (this.lane.getWay() == null ? "" : this.lane.getWay()));
		this.positionPanel = new TextBoxGeneric(Language.laneFieldPosition, this.lane == null ? "" : (this.lane.getPosition() == null ? "" : this.lane.getPosition()));
		int i = 0;
		FlexTable flexTable = new FlexTable();
		flexTable.setHTML(i, 0, stationPanel.getLabel());
		flexTable.setWidget(i, 1, stationPanel.getTextBox());
		i++;
		flexTable.setHTML(i, 0, lanePanel.getLabel());
		flexTable.setWidget(i, 1, lanePanel.getTextBox());
		i++;
		flexTable.setHTML(i, 0, roadPanel.getLabel());
		flexTable.setWidget(i, 1, roadPanel.getTextBox());
		i++;
		flexTable.setHTML(i, 0, wayPanel.getLabel());
		flexTable.setWidget(i, 1, wayPanel.getTextBox());
		i++;
		flexTable.setHTML(i, 0, positionPanel.getLabel());
		flexTable.setWidget(i, 1, positionPanel.getTextBox());
		Button saveButton = new Button(Language.laneEditionSaveButton);
		saveButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				LaneEditableParametersPanel.this.saveAction();
			}
		});
		Button cancelButton = new Button(Language.laneEditionCancelButton);
		cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				LaneEditableParametersPanel.this.cancelAction();
			}
		});
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.add(saveButton);
		buttonPanel.add(new HTML("&nbsp&nbsp"));
		buttonPanel.add(cancelButton);
		VerticalPanel panel = new VerticalPanel();
		panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		panel.setSpacing(3);
		panel.add(flexTable);
		panel.add(buttonPanel);
		dialogBox = new DialogBox(false, true);
		dialogBox.setStyleName(Css.dialogBoxStyle);
		dialogBox.add(panel);
		dialogBox.center();
		dialogBox.show();
	}

	/**
	 * Action to do when the 'cancelButton' is clicked: no change is saved.
	 */
	public void cancelAction(){
		dialogBox.hide();
	}

	/**
	 * Saves or updates lane
	 */
	public void saveAction() {
		// if lane==null, save a new lane in the database
		if (lane==null) {
			try {
				final CtLane newLane = new CtLane(
					new CtLaneId(getStation(), getLane()),
					getRoad(),
					getWay(),
					getPosition()
				);	
				FrontalWebApp.configurationsDatabaseService.addNewLane(newLane, new AsyncCallback<CtLane>() {
					@Override
					public void onFailure(Throwable caught) {
						Log.error("configurationsDatabaseService.addNewLane: failure.\n" + caught.getMessage() + "\n" + Language.laneNotSaved(newLane));
						String message = Language.laneNotSaved(newLane);
						if (caught instanceof ApplicationException) {
							message += "\n" + caught.getMessage();
						}
						new CustomDialogBox(message, Language.okString);
						dialogBox.hide();
					}
					@Override
					public void onSuccess(CtLane result) {
						Log.debug("configurationsDatabaseService.addNewLane: ok.\n"+Language.laneSaved(newLane));
						LaneEditableParametersPanel.this.lane = result;
						if (table!=null) {
							table.updateTable();
						}	
						FrontalWebApp.updateDatabaseObject();
						dialogBox.hide();
					}
				});
			} catch (Exception e) {
				Log.error(e.getMessage());
				new CustomDialogBox(e.getMessage(), Language.okString);
			}
		}
		// if lane is not null, updates the lane in the database
		else {
			try {
				final CtLane newLane = new CtLane(
					new CtLaneId(getStation(), getLane()),
					getRoad(),
					getWay(),
					getPosition()
				);	
				FrontalWebApp.configurationsDatabaseService.updateLane(lane, newLane, new AsyncCallback<CtLane>() {
					@Override
					public void onFailure(Throwable caught) {
						Log.error("configurationsDatabaseService.updateLane: failure.\n" + caught.getMessage() + "\n" + Language.laneNotSaved(newLane));
						String message = Language.laneNotSaved(newLane);
						if (caught instanceof ApplicationException) {
							message += "\n" + caught.getMessage();
						}
						new CustomDialogBox(message, Language.okString);
						dialogBox.hide();
						table.updateTable();
					}
					@Override
					public void onSuccess(CtLane result) {
						Log.debug("configurationsDatabaseService.updateLane: ok.\n" + Language.laneSaved(newLane));
						LaneEditableParametersPanel.this.lane = result;
						if (table!=null)
							table.updateTable();
						FrontalWebApp.updateDatabaseObject();
						dialogBox.hide();
					}
				});
			} catch (Exception e) {
				Log.error(e.getMessage());
				new CustomDialogBox(e.getMessage(), Language.okString);
			}
		}
	}

	/**
	 * Returns the station parameter entered by the user in the text box.
	 * @return 
	 * @throws Exception if the station text box is empty
	 */
	private String getStation() throws Exception {
		String station = stationPanel.getValue().trim();
		if (station.equals(Language.emptyString)) {
			throw new Exception(Language.laneFieldStationEmptyError);
		}	
		return station;
	}
	
	/**
	 * Returns the lane parameter entered by the user in the text box.
	 * @return 
	 * @throws Exception if the lane is empty
	 */
	private String getLane() throws Exception {
		String lane = lanePanel.getValue().trim();
		if (lane.equals(Language.emptyString)) {
			throw new Exception(Language.laneFieldLaneEmptyError);
		}	
		return lane;
	}
	
	/**
	 * Returns the road parameter entered by the user in the text box.
	 * @return 
	 */
	private String getRoad(){
		return roadPanel.getValue();
	}
	
	/**
	 * Returns the way parameter entered by the user in the text box.
	 * @return 
	 */
	private String getWay(){
		return wayPanel.getValue();
	}
	
	/**
	 * Returns the position parameter entered by the user in the text box.
	 * @return 
	 */
	private String getPosition(){
		return positionPanel.getValue();
	}
}
