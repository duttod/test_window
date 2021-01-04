package karrus.client.equipments.station;

import karrus.client.FrontalWebApp;
import karrus.client.appearance.Css;
import karrus.client.generic.CustomDialogBox;
import karrus.client.generic.DoubleBoxGeneric;
import karrus.client.generic.IntegerBoxGeneric;
import karrus.client.generic.TextBoxGeneric;
import karrus.shared.ApplicationException;
import karrus.shared.hibernate.RsuStation;
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
 * Creates a pop up panel to edit all parameters of the station.
 * 
 * @author estelle.dumas@karrus-its.com
 */
public class StationEditableParametersPanel {

	private TextBoxGeneric idPanel;
	private TextBoxGeneric serialPanel;
	private TextBoxGeneric hostPanel;
	private DoubleBoxGeneric latitudePanel;
	private DoubleBoxGeneric longitudePanel;
	private TextBoxGeneric roadPanel;
	private TextBoxGeneric positionPanel;
	private IntegerBoxGeneric cycleCountingSecPanel;
	private IntegerBoxGeneric cycleTravelTimeSecPanel;
	private IntegerBoxGeneric cycleWeatherSecPanel;
	private IntegerBoxGeneric cycleV2XSecPanel;
	private RsuStation station;
	private DialogBox dialogBox;
	private StationsTable table;

	/**
	 * Constructor.
	 * @param station
	 * @param table
	 */
	public StationEditableParametersPanel(RsuStation station, StationsTable table) {
		this.table = table;
		this.station = station;
		this.idPanel = new TextBoxGeneric(Language.stationId, (this.station==null)?"":this.station.getId());
		this.serialPanel = new TextBoxGeneric(Language.stationSerial, (this.station==null)?"":this.station.getSerial());
		this.hostPanel = new TextBoxGeneric(Language.stationHost, (this.station==null)?"":this.station.getHost());
		this.latitudePanel = new DoubleBoxGeneric(Language.stationLatitude, (this.station==null)?null:this.station.getLatitude());
		this.longitudePanel = new DoubleBoxGeneric(Language.stationLongitude, (this.station==null)?null:this.station.getLongitude());
		this.roadPanel = new TextBoxGeneric(Language.stationRoad, (this.station==null)?"":this.station.getRoad());
		this.positionPanel = new TextBoxGeneric(Language.stationPosition, (this.station==null)?"":this.station.getPosition());
		this.cycleCountingSecPanel = new IntegerBoxGeneric(Language.stationCycleCountingSec, (this.station==null)?null:this.station.getCycleCountingSec());
		this.cycleTravelTimeSecPanel = new IntegerBoxGeneric(Language.stationCycleTravelTimeSec, (this.station==null)?null:this.station.getCycleTravelTimeSec());
		this.cycleWeatherSecPanel = new IntegerBoxGeneric(Language.stationCycleWeatherSec, (this.station==null)?null:this.station.getCycleWeatherSec());
		this.cycleV2XSecPanel = new IntegerBoxGeneric(Language.stationCycleV2XSec, (this.station==null)?null:this.station.getCycleV2xSec());
		int i = 0;
		FlexTable flexTable = new FlexTable();
		flexTable.setHTML(i, 0, idPanel.getLabel());
		flexTable.setWidget(i, 1, idPanel.getTextBox());
		i++;
		flexTable.setHTML(i, 0, serialPanel.getLabel());
		flexTable.setWidget(i, 1, serialPanel.getTextBox());
		i++;
		flexTable.setHTML(i, 0, hostPanel.getLabel());
		flexTable.setWidget(i, 1, hostPanel.getTextBox());
		i++;
		flexTable.setHTML(i, 0, latitudePanel.getLabel());
		flexTable.setWidget(i, 1, latitudePanel.getTextBox());
		i++;
		flexTable.setHTML(i, 0, longitudePanel.getLabel());
		flexTable.setWidget(i, 1, longitudePanel.getTextBox());
		i++;
		flexTable.setHTML(i, 0, roadPanel.getLabel());
		flexTable.setWidget(i, 1, roadPanel.getTextBox());
		i++;
		flexTable.setHTML(i, 0, positionPanel.getLabel());
		flexTable.setWidget(i, 1, positionPanel.getTextBox());
		i++;
		flexTable.setHTML(i, 0, cycleCountingSecPanel.getLabel());
		flexTable.setWidget(i, 1, cycleCountingSecPanel.getTextBox());
		i++;
		flexTable.setHTML(i, 0, cycleTravelTimeSecPanel.getLabel());
		flexTable.setWidget(i, 1, cycleTravelTimeSecPanel.getTextBox());
		i++;
		flexTable.setHTML(i, 0, cycleWeatherSecPanel.getLabel());
		flexTable.setWidget(i, 1, cycleWeatherSecPanel.getTextBox());
		i++;
		flexTable.setHTML(i, 0, cycleV2XSecPanel.getLabel());
		flexTable.setWidget(i, 1, cycleV2XSecPanel.getTextBox());
		i++;
		Button saveButton = new Button(Language.saveString);
		saveButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				StationEditableParametersPanel.this.saveAction();
			}
		});
		Button cancelButton = new Button(Language.cancelString);
		cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				StationEditableParametersPanel.this.cancelAction();
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
	 * Saves all parameters are in the database.
	 */
	public void saveAction() {
		// if station==null, create a new station in the database
		if (station==null) {
			try {
				final String id = getId();
				RsuStation newStation = new RsuStation(
					id,
					getSerial(),
					getHost(),
					getLatitude(),
					getLongitude(),
					getRoad(),
					getPosition(),
					getCycleCountingSec(),
					getCycleTravelTimeSec(),
					getCycleWeatherSec(),
					getCycleV2xSec()
				);
				FrontalWebApp.configurationsDatabaseService.addNewStation(newStation, new AsyncCallback<RsuStation>() {
					@Override
					public void onFailure(Throwable caught) {
						Log.error("configurationsDatabaseService.addNewStation: failure. " + caught.getMessage() + "\n"+Language.stationNotSaved(id));
						String message = Language.stationNotSaved(id);
						if (caught instanceof ApplicationException) {
							message += "\n" + caught.getMessage();
						}
						new CustomDialogBox(message, Language.okString);
						dialogBox.hide();
					}
					@Override
					public void onSuccess(RsuStation result) {
						Log.debug("configurationsDatabaseService.addNewStation: ok. "+Language.stationSaved(id));
						StationEditableParametersPanel.this.station = result;
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
		// if station is not null, update the station in the database
		else {
			try {
				final RsuStation oldStation = new RsuStation(
					station.getId(),
					station.getSerial(),
					station.getHost(),
					station.getLatitude(),
					station.getLongitude(),
					station.getRoad(),
					station.getPosition(),
					station.getCycleCountingSec(),
					station.getCycleTravelTimeSec(),
					station.getCycleWeatherSec(),
					station.getCycleV2xSec()
				);
				station.setId(getId());
				station.setSerial(getSerial());
				station.setHost(getHost());
				station.setLatitude(getLatitude());
				station.setLongitude(getLongitude());
				station.setRoad(getRoad());
				station.setPosition(getPosition());
				station.setCycleCountingSec(getCycleCountingSec());
				station.setCycleTravelTimeSec(getCycleTravelTimeSec());
				station.setCycleWeatherSec(getCycleWeatherSec());
				station.setCycleV2xSec(getCycleV2xSec());
				FrontalWebApp.configurationsDatabaseService.updateStation(oldStation, station, new AsyncCallback<RsuStation>() {
					@Override
					public void onFailure(Throwable caught) {
						Log.error("configurationsDatabaseService.updateStation: failure. " + caught.getMessage() + "\n" + Language.stationNotSaved(StationEditableParametersPanel.this.station.getId()));
						String message = Language.stationNotSaved(StationEditableParametersPanel.this.station.getId());
						if (caught instanceof ApplicationException) {
							message += "\n" + caught.getMessage();
						}
						new CustomDialogBox(message, Language.okString);
						dialogBox.hide();
						table.updateTable();
					}
					@Override
					public void onSuccess(RsuStation result) {
						Log.debug("configurationsDatabaseService.updateStation: ok. "+Language.stationSaved(StationEditableParametersPanel.this.station.getId()));
						StationEditableParametersPanel.this.station = result;
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
	 * Returns the id parameter entered by the user in the text box.
	 * @return 
	 * @throws Exception if the id is empty
	 */
	private String getId() throws Exception{
		String id = idPanel.getValue().trim();
		if (id.equals("")) {
			throw new Exception(Language.stationIdEmptyError);
		}	
		return id;
	}
	
	/**
	 * Returns the serial parameter entered by the user in the text box.
	 * @return 
	 * @throws Exception if the serial is empty
	 */
	private String getSerial() throws Exception{
		String serial = serialPanel.getValue().trim();
		if (serial.equals("")) {
			throw new Exception(Language.stationSerialEmptyError);
		}	
		return serial;
	}
	
	/**
	 * Returns the host parameter entered by the user in the text box.
	 * @return 
	 * @throws Exception if the host is empty
	 */
	private String getHost() throws Exception {
		String host = hostPanel.getValue().trim();
		if (host.equals("")) {
			throw new Exception(Language.stationHostEmptyError);
		}	
		return host;
	}
	
	/**
	 * Returns the latitude parameter entered by the user in the text box.
	 * @return 
	 * @throws Exception if the latitude parameter has not the good format.
	 */
	private Double getLatitude() throws Exception{
		try {
			return latitudePanel.getValue();
		} catch (Exception e) {
			throw new Exception(Language.parameterError(latitudePanel.getString())+e.getMessage());
		}
	}

	/**
	 * Returns the longitude parameter entered by the user in the text box.
	 * @return 
	 * @throws Exception if the longitude parameter has not the good format.
	 */
	private Double getLongitude() throws Exception{
		try {
			return longitudePanel.getValue();
		} catch (Exception e) {
			throw new Exception(Language.parameterError(longitudePanel.getString())+e.getMessage());
		}
	}
	
	/**
	 * Returns the road parameter entered by the user in the text box.
	 * @return 
	 */
	private String getRoad(){
		return roadPanel.getValue();
	}
	
	/**
	 * Returns the position parameter entered by the user in the text box.
	 * @return 
	 */
	private String getPosition(){
		return positionPanel.getValue();
	}
	
	/**
	 * Returns the cycle counting parameter entered by the user in the text box.
	 * @return 
	 * @throws Exception if the cycle counting parameter has not the good format.
	 */
	private Integer getCycleCountingSec() throws Exception {
		try {
			return cycleCountingSecPanel.getValue();
		} catch (Exception e) {
			throw new Exception(Language.parameterError(cycleCountingSecPanel.getString())+e.getMessage());	
		}
	}
	
	/**
	 * Returns the cycle travel time parameter entered by the user in the text box.
	 * @return 
	 * @throws Exception if the cycle travel time parameter has not the good format.
	 */
	private Integer getCycleTravelTimeSec() throws Exception {
		try {
			return cycleTravelTimeSecPanel.getValue();
		} catch (Exception e) {
			throw new Exception(Language.parameterError(cycleTravelTimeSecPanel.getString())+e.getMessage());	
		}
	}
	
	/**
	 * Returns the cycle weather parameter entered by the user in the text box.
	 * @return 
	 * @throws Exception if the cycle weather parameter has not the good format.
	 */
	private Integer getCycleWeatherSec() throws Exception {
		try {
			return cycleWeatherSecPanel.getValue();
		} catch (Exception e) {
			throw new Exception(Language.parameterError(cycleWeatherSecPanel.getString())+e.getMessage());	
		}
	}
	
	/**
	 * Returns the cycle V2X parameter entered by the user in the text box.
	 * @return 
	 * @throws Exception if the cycle V2X parameter has not the good format.
	 */
	private Integer getCycleV2xSec() throws Exception{
		try {
			return cycleV2XSecPanel.getValue();
		} catch (Exception e) {
			throw new Exception(Language.parameterError(cycleV2XSecPanel.getString())+e.getMessage());	
		}
	}
}
