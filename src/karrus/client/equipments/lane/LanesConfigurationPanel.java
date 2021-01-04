package karrus.client.equipments.lane;

import java.util.List;

import karrus.client.FrontalWebApp;
import karrus.client.appearance.Css;
import karrus.client.generic.CustomDialogBox;
import karrus.shared.hibernate.CtLane;
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
 * Creates a panel to manage lanes : parameters edition, add lane, remove lane
 * 
 * @author estelle.dumas@karrus-its.com
 */
public class LanesConfigurationPanel extends Composite {

	private List<CtLane> lanes;
	private LanesTable lanesTable;
	private VerticalPanel panel;
	private HorizontalPanel buttonsPanel;

	/**
	 * Constructor.
	 * @param lanes
	 */
	public LanesConfigurationPanel(List<CtLane> lanes){
		this.lanes = lanes;
		Button exportTableButton = new Button(Language.lanesConfigurationPanelExportButton);
		exportTableButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				exportTableAction();
			}
		});
		Label titleLabel = new Label(Language.lanesConfigurationPanelTitle);
		titleLabel.setStyleName(Css.titleLabelStyle);
		lanesTable = new LanesTable(lanes, this);
		Button editLaneButton = new Button(Language.lanesConfigurationPanelEditButton);
		editLaneButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				editLaneAction();
			}
		});
		Button addNewLaneButton = new Button(Language.lanesConfigurationPanelAddButton);
		addNewLaneButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				addNewLaneAction();
			}
		});
		Button removeLaneButton = new Button(Language.lanesConfigurationPanelRemoveButton);
		removeLaneButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				removeLaneAction();
			}
		});
		HorizontalPanel editButtonsPanel = new HorizontalPanel();
		editButtonsPanel.setSpacing(3);
		editButtonsPanel.add(editLaneButton);
		buttonsPanel = new HorizontalPanel();
		buttonsPanel.add(addNewLaneButton);
		buttonsPanel.add(new HTML("&nbsp"));
		buttonsPanel.add(removeLaneButton);
		panel = new VerticalPanel();
		panel.setSize("10px", "10px");
		panel.add(titleLabel);
		VerticalPanel exportButtonContainer = new VerticalPanel();
		exportButtonContainer.add(exportTableButton);
		exportButtonContainer.addStyleName(Css.bottomPadding);
		panel.add(exportButtonContainer);
		panel.add(lanesTable);
		panel.add(editButtonsPanel);
		editButtonsPanel.add(buttonsPanel);
		this.initWidget(panel);
		
	}

	/**
	 * Exports the table containing lanes in a csv file.
	 */
	private void exportTableAction(){
		FrontalWebApp.exportService.exportLanesTable(new AsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				Log.debug("exportService.exportLanesTable: ok.");
				String baseURL = GWT.getHostPageBaseURL();
				String url = baseURL + "ExportCountDataTableServlet//" + result;
				Window.open(url, Language.blankString, "");
			}
			@Override
			public void onFailure(Throwable caught) {
				Log.error("exportService.exportLanesTable: failure.\n"+caught.getMessage());
				new CustomDialogBox(Language.genericErrorString, Language.okString);
			}
		});
	}
	
	/**
	 * Opens a pop up panel to edit all parameters of the selected lane
	 */
	private void editLaneAction() {
		try {
			lanesTable.editLaneParameters();
		} catch (Exception e) {
			Log.error(e.getMessage());
			new CustomDialogBox(Language.genericErrorString, Language.okString);
		}
	}

	/**
	 * Opens a pop up panel to edit all parameters of the new lane. 
	 */
	private void addNewLaneAction() {
		new LaneEditableParametersPanel(null, lanesTable);
	}

	/**
	 * Updates the lane table.
	 */
	public void updateTable(){
		FrontalWebApp.genericDatabaseService.getAllLanes(new AsyncCallback<List<CtLane>>() {
			@Override
			public void onSuccess(List<CtLane> result) {
				Log.debug("genericDatabaseService.getAllLanes: ok");
				lanes = result;
				if (lanesTable!=null) {
					panel.remove(lanesTable);
				}	
				lanesTable = new LanesTable(lanes, LanesConfigurationPanel.this);
				panel.insert(lanesTable, 2);
				panel.setCellHeight(lanesTable, "25%");
			}
			@Override
			public void onFailure(Throwable caught) {
				Log.error("genericDatabaseService.getAllLanes: failure\n"+caught.getMessage());
				new CustomDialogBox(Language.genericErrorString, Language.okString);
			}
		});
	}

	/**
	 * Opens a pop up dialog box to delete a lane.
	 */
	private void removeLaneAction(){
		try {
			lanesTable.removeLane();
		} catch (Exception e) {
			Log.error(e.getMessage());
			new CustomDialogBox(Language.genericErrorString, Language.okString);
		}
	}
}
