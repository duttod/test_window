package karrus.client.equipments.itinerary;

import java.util.List;

import karrus.client.FrontalWebApp;
import karrus.client.appearance.Css;
import karrus.client.generic.CustomDialogBox;
import karrus.shared.hibernate.TtItinerary;
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
 * Creates a panel to manage itineraries : parameters edition, add itinerary, remove itinerary
 * 
 * @author estelle.dumas@karrus-its.com
 */
public class ItinerariesConfigurationPanel extends Composite {

	private List<TtItinerary> itineraries;
	private ItinerariesTable itinerariesTable;
	private VerticalPanel panel;
	private HorizontalPanel buttonsPanel;

	/**
	 * Constructor.
	 * @param itineraries
	 */
	public ItinerariesConfigurationPanel(List<TtItinerary> itineraries){
		this.itineraries = itineraries;
		Button exportTableButton = new Button(Language.CSVexportString);
		exportTableButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				exportTableAction();
			}
		});
		Label titleLabel = new Label(Language.itinerariessTitle);
		titleLabel.setStyleName(Css.titleLabelStyle);
		itinerariesTable = new ItinerariesTable(itineraries, this);
		Button editItineraryButton = new Button(Language.editItineraryString);
		editItineraryButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				editItineraryAction();
			}
		});
		Button addNewItineraryButton = new Button(Language.addNewItineraryString);
		addNewItineraryButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				addNewItineraryAction();
			}
		});
		Button removeItineraryButton = new Button(Language.removeItineraryString);
		removeItineraryButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				removeItineraryAction();
			}
		});
		HorizontalPanel editButtonsPanel = new HorizontalPanel();
		editButtonsPanel.setSpacing(3);
		editButtonsPanel.add(editItineraryButton);
		buttonsPanel = new HorizontalPanel();
		buttonsPanel.add(addNewItineraryButton);
		buttonsPanel.add(new HTML("&nbsp"));
		buttonsPanel.add(removeItineraryButton);
		panel = new VerticalPanel();
		panel.setSize("10px", "10px");
		panel.add(titleLabel);
		VerticalPanel exportButtonContainer = new VerticalPanel();
		exportButtonContainer.add(exportTableButton);
		exportButtonContainer.addStyleName(Css.bottomPadding);
		panel.add(exportButtonContainer);
		panel.add(itinerariesTable);
		panel.add(editButtonsPanel);
		editButtonsPanel.add(buttonsPanel);
		this.initWidget(panel);
	}

	/**
	 * Exports the table containing itineraries in a csv file.
	 */
	private void exportTableAction(){
		FrontalWebApp.exportService.exportItinerariesTable(itineraries, new AsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				Log.debug("service.exportItinerariesTable: ok");
				String baseURL = GWT.getHostPageBaseURL();
				String url = baseURL + "ExportCountDataTableServlet//" + result;
				Window.open(url, Language.blankString, "");
			}
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				Log.error("service.exportItinerariesTable: failure\n"+caught.getMessage());
				new CustomDialogBox("service.exportItinerariesTable: failure", Language.okString);
			}
		});
	}

	/**
	 * Opens a pop up panel to edit all parameters of the itinerary
	 */
	private void editItineraryAction(){
		try {
			itinerariesTable.editItineraryParameters();
		} catch (Exception e) {
			Log.error(e.getMessage());
			new CustomDialogBox(e.getMessage(), Language.okString);
		}
	}

	/**
	 * Opens a pop up panel to edit all parameters of the new itinerary.
	 */
	private void addNewItineraryAction(){
		new ItineraryEditableParametersPanel(null, itinerariesTable);
	}

	/**
	 * Updates the itineraries table.
	 */
	public void updateTable(){
		FrontalWebApp.configurationsDatabaseService.getAllItineraries(new AsyncCallback<List<TtItinerary>>() {
			@Override
			public void onSuccess(List<TtItinerary> result) {
				Log.debug("configurationsDatabaseService.getAllItineraries: ok");
				itineraries = result;
				if (itinerariesTable!=null)
					panel.remove(itinerariesTable);
				itinerariesTable = new ItinerariesTable(itineraries, ItinerariesConfigurationPanel.this);
				panel.insert(itinerariesTable, 2);
				panel.setCellHeight(itinerariesTable, "25%");
			}
			@Override
			public void onFailure(Throwable caught) {
				Log.error("configurationsDatabaseService.getAllItineraries: failure\n"+caught.getMessage());
				new CustomDialogBox("configurationsDatabaseService.getAllItineraries: failure", Language.okString);
			}
		});
	}

	/**
	 * Opens a pop up dialog box to select an itinerary to delete.
	 */
	private void removeItineraryAction(){
		try {
			itinerariesTable.removeItinerary();
		} catch (Exception e) {
			Log.error(e.getMessage());
			new CustomDialogBox(e.getMessage(), Language.okString);
		}
	}
}
