package karrus.client.equipments.itinerary;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karrus.client.FrontalWebApp;
import karrus.client.generic.CustomDialogBox;
import karrus.shared.hibernate.TtItinerary;
import karrus.shared.hibernate.TtItineraryId;
import karrus.shared.language.Language;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;

/**
 * Shows itineraries in a table.
 * 
 * @author estelle.dumas@karrus-its.com
 */
public class ItinerariesTable extends Composite{

	private List<TtItinerary> itineraries;
	private Map<Integer, TtItinerary> indexToItinerary;
	private ItinerariesConfigurationPanel itinerariesPanel;
	private ItineraryGrid itinerariesGrid;

	/**
	 * Constructor.
	 * @param itineraries
	 * @param itinerariesPanel
	 */
	public ItinerariesTable(List<TtItinerary> itineraries, ItinerariesConfigurationPanel itinerariesPanel){
		this.itineraries = itineraries;
		this.itinerariesGrid = new ItineraryGrid(this.itineraries);
		this.itinerariesPanel = itinerariesPanel;
		this.indexToItinerary = new HashMap<Integer, TtItinerary>();
		for (int i = 0; i < itineraries.size(); i++) {
			indexToItinerary.put(i, itineraries.get(i));
		}
		this.initWidget(itinerariesGrid.dataGrid);
	}

	/**
	 * Opens a pop up panel to edit all parameters of the itineray.
	 * @throws Exception if no itineray is selected
	 */
	public void editItineraryParameters() throws Exception{
		int index = itinerariesGrid.getSelectedItineraryIndex();
		if (index==-1) {
			throw new Exception(Language.noSelectedItineraryError);
		}
		new ItineraryEditableParametersPanel(indexToItinerary.get(index), this);
	}

	/**
	 * Opens a pop up panel to confirm the removing of the itinerary.
	 * @throws Exception if no itinerary is selected
	 */
	public void removeItinerary() throws Exception{
		int index = itinerariesGrid.getSelectedItineraryIndex();
		if (index==-1) {
			throw new Exception(Language.noSelectedItineraryError);
		}
		TtItineraryId id = indexToItinerary.get(index).getId();
		final String name = indexToItinerary.get(index).getName();
		final String origin = id.getOrigin();
		final String dest = id.getDestination();
		String message = Language.confirmRemovingItinerary(name);
		String button1Message = Language.yesString;
		ClickHandler action1 = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				removeItinerary(name, origin, dest);
			}
		};
		String button2Message = Language.noString;
		ClickHandler action2 = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				new CustomDialogBox(Language.itineraryNotRemoved(name), Language.okString);
			}
		};
		new CustomDialogBox(message, button1Message, action1, button2Message, action2);
	}


	/**
	 * Removes the itinerary, i.e. deletes it from the database
	 * @param name
	 * @param origin
	 * @param destination
	 */
	private void removeItinerary(final String name, String origin, String destination){
		FrontalWebApp.configurationsDatabaseService.removeItinerary(name, origin, destination, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				Log.debug("configurationsDatabaseService.removeItinerary: ok. "+Language.itineraryRemoved(name));
				new CustomDialogBox(Language.itineraryRemoved(name), Language.okString);
				updateTable();
				FrontalWebApp.updateDatabaseObject();
			}
			@Override
			public void onFailure(Throwable caught) {
				Log.error("configurationsDatabaseService.removeItinerary: failure.\n"+Language.itineraryNotRemoved(name));
				new CustomDialogBox(Language.itineraryNotRemoved(name), Language.okString);
			}
		}); 
	}

	/**
	 * Updates the itineraries table.
	 */
	public void updateTable(){
		this.itinerariesPanel.updateTable();
	}
}
