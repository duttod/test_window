package karrus.client.equipments.itinerary;

import java.util.List;

import karrus.client.FrontalWebApp;
import karrus.client.appearance.Css;
import karrus.client.generic.CustomDialogBox;
import karrus.client.generic.IntegerBoxGeneric;
import karrus.client.generic.ListBoxGeneric;
import karrus.shared.ApplicationException;
import karrus.shared.hibernate.TtItinerary;
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
 * Creates a pop up panel to edit all parameters of the itinerary.
 * 
 * @author estelle.dumas@karrus-its.com
 */
public class ItineraryEditableParametersPanel {

//	private TextBoxGeneric namePanel;
	private ListBoxGeneric originPanel;
	private ListBoxGeneric destinationPanel;
	private IntegerBoxGeneric nominalTravelTimePanel;
	private IntegerBoxGeneric maxTravelTimePanel;
	private IntegerBoxGeneric levelOfServiceThreshold1Panel;
	private IntegerBoxGeneric levelOfServiceThreshold2Panel;
	private IntegerBoxGeneric scalePanel;
	private TtItinerary itinerary;
	private DialogBox dialogBox;
	private ItinerariesTable table;

	/**
	 * Constructor.
	 * @param itinerary
	 * @param table
	 */
	public ItineraryEditableParametersPanel(TtItinerary itinerary, ItinerariesTable table){
		this.table = table;
		this.itinerary = itinerary;
		List<String> stationNames = FrontalWebApp.databaseObjectsTools.getTravelTimeEnabledStationsIds();
//		this.namePanel = new TextBoxGeneric(Language.nameString, (this.itinerary==null)?"":this.itinerary.getName());
		this.originPanel = new ListBoxGeneric(null, stationNames, (this.itinerary==null)?"":this.itinerary.getId().getOrigin(), false);
		this.destinationPanel = new ListBoxGeneric(null, stationNames, (this.itinerary==null)?"":this.itinerary.getId().getDestination(), false);
		this.nominalTravelTimePanel = new IntegerBoxGeneric(Language.nominalTravelTimeString, (this.itinerary==null)?null:this.itinerary.getNominalTravelTime());
		this.maxTravelTimePanel = new IntegerBoxGeneric(Language.maxTravelTimeString, (this.itinerary==null)?null:this.itinerary.getMaxTravelTime());
		this.levelOfServiceThreshold1Panel = new IntegerBoxGeneric(Language.levelOfServiceThreshold1String, (this.itinerary==null)?null:this.itinerary.getLevelOfServiceThreshold1());
		this.levelOfServiceThreshold2Panel = new IntegerBoxGeneric(Language.levelOfServiceThreshold2String, (this.itinerary==null)?null:this.itinerary.getLevelOfServiceThreshold2());
		this.scalePanel = new IntegerBoxGeneric(Language.scaleString, (this.itinerary==null)?null:this.itinerary.getScale());
		int i = 0;
		FlexTable flexTable = new FlexTable();
		flexTable.setHTML(i, 0, Language.originString);
		flexTable.setWidget(i, 1, originPanel);
		i++;
		flexTable.setHTML(i, 0, Language.destinationString);
		flexTable.setWidget(i, 1, destinationPanel);
		i++;
//		flexTable.setHTML(i, 0, namePanel.getLabel());
//		flexTable.setWidget(i, 1, namePanel.getTextBox());
//		i++;
		flexTable.setHTML(i, 0, nominalTravelTimePanel.getLabel());
		flexTable.setWidget(i, 1, nominalTravelTimePanel.getTextBox());
		i++;
		flexTable.setHTML(i, 0, maxTravelTimePanel.getLabel());
		flexTable.setWidget(i, 1, maxTravelTimePanel.getTextBox());
		i++;
		flexTable.setHTML(i, 0, levelOfServiceThreshold1Panel.getLabel());
		flexTable.setWidget(i, 1, levelOfServiceThreshold1Panel.getTextBox());
		i++;
		flexTable.setHTML(i, 0, levelOfServiceThreshold2Panel.getLabel());
		flexTable.setWidget(i, 1, levelOfServiceThreshold2Panel.getTextBox());
		i++;
		flexTable.setHTML(i, 0, scalePanel.getLabel());
		flexTable.setWidget(i, 1, scalePanel.getTextBox());
		i++;
		Button saveButton = new Button(Language.saveString);
		saveButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ItineraryEditableParametersPanel.this.saveAction();
			}
		});

		Button cancelButton = new Button(Language.cancelString);
		cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ItineraryEditableParametersPanel.this.cancelAction();
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
		// if itinerary==null, create a new itinerary in the database
		if (itinerary==null)
			try {
				final String name = getOrigin() + "-" + getDestination();
				FrontalWebApp.configurationsDatabaseService.addNewItinerary(name, getOrigin(), getDestination(), getNominalTravelTime(), getMaxTravelTime(), getLevelOfServiceThreshold1(), getLevelOfServiceThreshold2(), getScale(), new AsyncCallback<TtItinerary>() {
					@Override
					public void onFailure(Throwable caught) {
						Log.error("configurationsDatabaseService.addNewItinerary: failure.\n"+Language.itineraryNotSaved(name)+"\n"+caught.getMessage());
						String message = Language.itineraryNotSaved(name);
						if (caught instanceof ApplicationException) {
							message += "\n" + caught.getMessage();
						}
						new CustomDialogBox(message, Language.okString);
						dialogBox.hide();
					}

					@Override
					public void onSuccess(TtItinerary result) {
						Log.debug("configurationsDatabaseService.addNewItinerary: ok. "+Language.itinerarySaved(name));
						new CustomDialogBox(Language.itinerarySaved(name), Language.okString);
						ItineraryEditableParametersPanel.this.itinerary = result;
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
		// if itinerary is not null, update the itinerary in the database
		else
			try {
				String formerName = itinerary.getName();
				String formerOrigin = itinerary.getId().getOrigin();
				String formerDestination = itinerary.getId().getDestination();
				FrontalWebApp.configurationsDatabaseService.updateItinerary(formerName, formerOrigin, formerDestination, getOrigin() + "-" + getDestination(), getOrigin(), getDestination(), getNominalTravelTime(),
						                                                    getMaxTravelTime(), getLevelOfServiceThreshold1(), getLevelOfServiceThreshold2(), getScale(), new AsyncCallback<TtItinerary>() {
					@Override
					public void onFailure(Throwable caught) {
						Log.error("configurationsDatabaseService.updateItinerary: failure.\n"+Language.itineraryNotSaved(ItineraryEditableParametersPanel.this.itinerary.getName())+caught.getMessage());
						String message = Language.itineraryNotSaved(ItineraryEditableParametersPanel.this.itinerary.getName());
						if (caught instanceof ApplicationException) {
							message += "\n" + caught.getMessage();
						}
						new CustomDialogBox(message, Language.okString);
						dialogBox.hide();
					}

					@Override
					public void onSuccess(TtItinerary result) {
						Log.debug("configurationsDatabaseService.updateItinerary: ok. "+Language.itinerarySaved(ItineraryEditableParametersPanel.this.itinerary.getName()));
						new CustomDialogBox(Language.itinerarySaved(ItineraryEditableParametersPanel.this.itinerary.getName()), Language.okString);
						ItineraryEditableParametersPanel.this.itinerary = result;
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

//	/**
//	 * Returns the name parameter entered by the user in the text box.
//	 * @return string
//	 * @throws Exception if the name is empty or already used
//	 */
//	private String getName() throws Exception{
//		String name = namePanel.getValue().trim();
//		return name;
//	}

	/**
	 * Returns the origin parameter enter by the user in the text box.
	 * @return String 
	 */
	private String getOrigin(){
		return originPanel.getSelectedValue();
	}
	
	

	/**
	 * Returns the destination parameter enter by the user in the text box.
	 * @return String 
	 */
	private String getDestination(){
		return destinationPanel.getSelectedValue();
	}

	/**
	 * Returns the nominal travel time parameter enter by the user in the text box.
	 * @return Integer
	 * @throws Exception if the nominal travel time is not correct
	 */
	private Integer getNominalTravelTime() throws Exception{
		try {
			Integer n = nominalTravelTimePanel.getValue();
			if (n==null)
				throw new Exception(Language.emptyError);
			else
				return n;
		} catch (Exception e) {
			throw new Exception(Language.parameterError(nominalTravelTimePanel.getString())+e.getMessage());
		}
	}
	
	/**
	 * Returns the max travel time parameter enter by the user in the text box.
	 * @return Integer
	 * @throws Exception if the max travel time is not correct
	 */
	private Integer getMaxTravelTime() throws Exception{
		try {
			Integer n = maxTravelTimePanel.getValue();
			if (n==null)
				throw new Exception(Language.emptyError);
			else
				return n;
		} catch (Exception e) {
			throw new Exception(Language.parameterError(maxTravelTimePanel.getString())+e.getMessage());
		}
	}

	/**
	 * Returns the level of service threshold 1 parameter enter by the user in the text box.
	 * @return Integer
	 * @throws Exception if the level of service threshold 1 is not correct
	 */
	private Integer getLevelOfServiceThreshold1() throws Exception{
		try {
			Integer l = levelOfServiceThreshold1Panel.getValue();
			if (l==null)
				throw new Exception(Language.emptyError);
			else
				return l;
		} catch (Exception e) {
			throw new Exception(Language.parameterError(levelOfServiceThreshold1Panel.getString())+e.getMessage());
		}
	}

	/**
	 * Returns the level of service threshold 2 parameter enter by the user in the text box.
	 * @return Integer
	 * @throws Exception if the level of service threshold 2 is not correct
	 */
	private Integer getLevelOfServiceThreshold2() throws Exception{
		try {
			Integer l = levelOfServiceThreshold2Panel.getValue();
			if (l==null)
				throw new Exception(Language.emptyError);
			else
				return l;
		} catch (Exception e) {
			throw new Exception(Language.parameterError(levelOfServiceThreshold2Panel.getString())+e.getMessage());
		}
	}
	
	/**
	 * Returns the scale parameter entered by the user in the text box.
	 * @return Integer
	 * @throws Exception if the scale parameter is not correct
	 */
	private Integer getScale() throws Exception{
		try {
			Integer scale = scalePanel.getValue();
			if (scale==null) {
				throw new Exception(Language.emptyError);
			}
			else {
				return scale;
			}
		} catch (Exception e) {
			throw new Exception(Language.parameterError(scalePanel.getString())+e.getMessage());
		}
	}
}
