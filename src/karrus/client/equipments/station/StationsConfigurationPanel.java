package karrus.client.equipments.station;

import java.util.List;

import karrus.client.FrontalWebApp;
import karrus.client.appearance.Css;
import karrus.client.generic.CustomDialogBox;
import karrus.shared.hibernate.RsuStation;
import karrus.shared.language.Language;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Creates a panel to manage stations : parameters edition, add station, remove station
 * 
 * @author estelle.dumas@karrus-its.com
 */
public class StationsConfigurationPanel extends Composite {

	private List<RsuStation> stations;
	private StationsTable stationsTable;
	private VerticalPanel panel;
	private HorizontalPanel buttonsPanel;

	/**
	 * Constructor.
	 * @param stations
	 */
	public StationsConfigurationPanel(List<RsuStation> stations){
		this.stations = stations;
		Button exportTableButton = new Button(Language.CSVexportString);
		exportTableButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				exportTableAction();
			}
		});
		Label titleLabel = new Label(Language.stationsTitle);
		titleLabel.setStyleName(Css.titleLabelStyle);
		stationsTable = new StationsTable(stations, this);
		Button editStationButton = new Button(Language.editStationString);
		editStationButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				editStationAction();
			}
		});
		Button addNewStationButton = new Button(Language.addNewStationString);
		addNewStationButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				addNewStationAction();
			}
		});
		Button removeStationButton = new Button(Language.removeStationString);
		removeStationButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				removeStationAction();
			}
		});
		HorizontalPanel editButtonsPanel = new HorizontalPanel();
		editButtonsPanel.setSpacing(3);
		editButtonsPanel.add(editStationButton);
		buttonsPanel = new HorizontalPanel();
		buttonsPanel.add(addNewStationButton);
		buttonsPanel.add(new HTML("&nbsp"));
		buttonsPanel.add(removeStationButton);
		panel = new VerticalPanel();
		panel.setSize("10px", "10px");
		panel.add(titleLabel);
		VerticalPanel exportButtonContainer = new VerticalPanel();
		exportButtonContainer.add(exportTableButton);
		exportButtonContainer.addStyleName(Css.bottomPadding);
		panel.add(exportButtonContainer);
		panel.add(stationsTable);
		panel.add(editButtonsPanel);
		editButtonsPanel.add(buttonsPanel);
		this.initWidget(panel);
		
	}

	/**
	 * Exports the table containing stations in a csv file.
	 */
	private void exportTableAction(){
		FrontalWebApp.exportService.exportStationsTable(stations, new AsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				Log.debug("service.exportStationsTable: ok");
				String baseURL = GWT.getHostPageBaseURL();
				String url = baseURL + "ExportCountDataTableServlet//" + result;
				Window.open(url, Language.blankString, "");
			}
			@Override
			public void onFailure(Throwable caught) {
				Log.error("service.exportStationsTable: failure\n"+caught.getMessage());
				new CustomDialogBox("service.exportStationsTable: failure", Language.okString);
			}
		});
	}
	
	/**
	 * Opens a pop up panel to edit all parameters of the station
	 */
	private void editStationAction(){
		try {
			stationsTable.editStationParameters();
		} catch (Exception e) {
			Log.error(e.getMessage());
			new CustomDialogBox(e.getMessage(), Language.okString);
		}
	}

	/**
	 * Opens a pop up panel to edit all parameters of the new station.
	 */
	private void addNewStationAction(){
		new StationEditableParametersPanel(null, stationsTable);
	}

	/**
	 * Updates the stations table.
	 */
	public void updateTable(){
		FrontalWebApp.configurationsDatabaseService.getStations(new AsyncCallback<List<RsuStation>>() {
			@Override
			public void onSuccess(List<RsuStation> result) {
				Log.debug("configurationsDatabaseService.getStations: ok");
				stations = result;
				if (stationsTable!=null) {
					panel.remove(stationsTable);
				}	
				stationsTable = new StationsTable(stations, StationsConfigurationPanel.this);
				panel.insert(stationsTable, 2);
				panel.setCellHeight(stationsTable, "25%");
			}
			@Override
			public void onFailure(Throwable caught) {
				Log.error("configurationsDatabaseService.getStations: failure\n"+caught.getMessage());
				new CustomDialogBox("configurationsDatabaseService.getStations: failure", Language.okString);
			}
		});
	}

	/**
	 * Opens a pop up dialog box to select a station to delete.
	 */
	private void removeStationAction(){
		try {
			stationsTable.removeStation();
		} catch (Exception e) {
			Log.error(e.getMessage());
			new CustomDialogBox(e.getMessage(), Language.okString);
		}
	}
}
