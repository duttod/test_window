package karrus.client.users.credential;

import java.util.ArrayList;
import java.util.List;

import karrus.client.FrontalWebApp;
import karrus.client.appearance.Css;
import karrus.client.generic.CustomDialogBox;
import karrus.client.generic.ListBoxGeneric;
import karrus.client.generic.TextBoxGeneric;
import karrus.shared.ApplicationException;
import karrus.shared.hibernate.UsrCredential;
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
 * Creates a pop up panel to edit all parameters of the credential.
 * 
 * @author estelle.dumas@karrus-its.com
 */
public class CredentialEditableParametersPanel {

	private TextBoxGeneric credentialTextBox;
	private ListBoxGeneric stationListBox;
	private ListBoxGeneric synopticListBox;
	private ListBoxGeneric alarmsListBox;
	private ListBoxGeneric rdtCtDataListBox;
	private ListBoxGeneric rdtCtStationsListBox;
	private ListBoxGeneric rdtTtDataListBox;
	private ListBoxGeneric rdtTtBoxesListBox;
	private ListBoxGeneric rdtTtItinerariesListBox;
	private ListBoxGeneric systemListBox;
	private UsrCredential groupCredential;
	private DialogBox dialogBox;
	private CredentialsTable table;
	private String fieldsWidth = "150px";

	/**
	 * Constructor.
	 * @param groupCredential
	 * @param table
	 */
	public CredentialEditableParametersPanel(UsrCredential groupCredential, CredentialsTable table){
		
		this.table = table;
		this.groupCredential = groupCredential;
		List<String> userRightsReducedList = new ArrayList<String>();
		userRightsReducedList.add(Language.noRight);
		userRightsReducedList.add(Language.allRights);
 		List<String> userRightsList = new ArrayList<String>();
		userRightsList.add(Language.noRight);
		userRightsList.add(Language.allRights);
		userRightsList.add(Language.visibleButNotModifiableRight);
		this.credentialTextBox = new TextBoxGeneric(Language.credentialGroup, this.groupCredential == null ? "" : this.groupCredential.getCredential());
		this.credentialTextBox.getTextBox().setWidth(fieldsWidth);
		this.stationListBox = new ListBoxGeneric(Language.credentialStation, userRightsReducedList, this.groupCredential == null ? "" : this.groupCredential.getStation(), false);
		this.stationListBox.getListBox().setWidth(fieldsWidth);
		this.synopticListBox = new ListBoxGeneric(Language.credentialSynoptic, userRightsReducedList, this.groupCredential == null ? "" : this.groupCredential.getSynoptic(), false);
		this.synopticListBox.getListBox().setWidth(fieldsWidth);
		this.alarmsListBox = new ListBoxGeneric(Language.credentialAlarms, userRightsList, this.groupCredential == null ? "" : this.groupCredential.getAlarms(), false);
		this.alarmsListBox.getListBox().setWidth(fieldsWidth);
		this.rdtCtDataListBox = new ListBoxGeneric(Language.credentialRdtCtData, userRightsReducedList, this.groupCredential == null ? "" : this.groupCredential.getRdtCtData(), false);
		this.rdtCtDataListBox.getListBox().setWidth(fieldsWidth);
		this.rdtCtStationsListBox = new ListBoxGeneric(Language.credentialRdtCtStations, userRightsReducedList, this.groupCredential == null ? "" : this.groupCredential.getRdtCtStations(), false);
		this.rdtCtStationsListBox.getListBox().setWidth(fieldsWidth);
		this.rdtTtDataListBox = new ListBoxGeneric(Language.credentialRdtTtData, userRightsReducedList, this.groupCredential == null ? "" : this.groupCredential.getRdtTtData(), false);
		this.rdtTtDataListBox.getListBox().setWidth(fieldsWidth);
		this.rdtTtBoxesListBox = new ListBoxGeneric(Language.credentialRdtTtBoxes, userRightsReducedList, this.groupCredential == null ? "" : this.groupCredential.getRdtTtBoxes(), false);
		this.rdtTtBoxesListBox.getListBox().setWidth(fieldsWidth);
		this.rdtTtItinerariesListBox = new ListBoxGeneric(Language.credentialRdtTtItineraries, userRightsReducedList, this.groupCredential == null ? "" : this.groupCredential.getRdtTtItineraries(), false);
		this.rdtTtItinerariesListBox.getListBox().setWidth(fieldsWidth);
		this.systemListBox = new ListBoxGeneric(Language.credentialSystem, userRightsReducedList, this.groupCredential == null ? "" : this.groupCredential.getSystem(), false);
		this.systemListBox.getListBox().setWidth(fieldsWidth);
		int i = 0;
		FlexTable flexTable = new FlexTable();
		flexTable.setHTML(i, 0, credentialTextBox.getLabel());
		flexTable.setWidget(i, 1, credentialTextBox.getTextBox());
		i++;
		flexTable.setHTML(i, 0, stationListBox.getLabel());
		flexTable.setWidget(i, 1, stationListBox.getListBox());
		i++;
		flexTable.setHTML(i, 0, synopticListBox.getLabel());
		flexTable.setWidget(i, 1, synopticListBox.getListBox());
		i++;
		flexTable.setHTML(i, 0, alarmsListBox.getLabel());
		flexTable.setWidget(i, 1, alarmsListBox.getListBox());
		i++;
		flexTable.setHTML(i, 0, rdtCtDataListBox.getLabel());
		flexTable.setWidget(i, 1, rdtCtDataListBox.getListBox());
		i++;
		flexTable.setHTML(i, 0, rdtCtStationsListBox.getLabel());
		flexTable.setWidget(i, 1, rdtCtStationsListBox.getListBox());
		i++;
		flexTable.setHTML(i, 0, rdtTtDataListBox.getLabel());
		flexTable.setWidget(i, 1, rdtTtDataListBox.getListBox());
		i++;
		flexTable.setHTML(i, 0, rdtTtBoxesListBox.getLabel());
		flexTable.setWidget(i, 1, rdtTtBoxesListBox.getListBox());
		i++;
		flexTable.setHTML(i, 0, rdtTtItinerariesListBox.getLabel());
		flexTable.setWidget(i, 1, rdtTtItinerariesListBox.getListBox());
		i++;
		flexTable.setHTML(i, 0, systemListBox.getLabel());
		flexTable.setWidget(i, 1, systemListBox.getListBox());
		Button saveButton = new Button(Language.saveString);
		saveButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				CredentialEditableParametersPanel.this.saveAction();
			}
		});
		Button cancelButton = new Button(Language.cancelString);
		cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				CredentialEditableParametersPanel.this.cancelAction();
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
	 * Saves all parameters in the database.
	 */
	public void saveAction() {
		if (groupCredential==null) {
			try {
				final String credential = getCredential();
				String station = getStation();
				String synoptic = getSynoptic();
				String alarms = getAlarms();
				String rdtCtData = getRdtCtData();
				String rdtCtStations = getRdtCtStations();
				String rdtTtData = getRdtTtData();
				String rdtTtBoxes = getRdtTtBoxes();
				String rdtTtItineraries = getRdtTtItineraries();
				String system = getSystem();
				UsrCredential newCredential = new UsrCredential(
						credential,
						station,
						synoptic,
						alarms,
						rdtCtData,
						rdtCtStations,
						rdtTtData,
						rdtTtBoxes,
						rdtTtItineraries,
						system
						);
				FrontalWebApp.genericDatabaseService.addNewCredential(newCredential, new AsyncCallback<UsrCredential>() {
					@Override
					public void onFailure(Throwable caught) {
						Log.error("genericDatabaseService.addNewCredential: failure. " + caught.getMessage() + "\n" + Language.credentialNotSaved(credential));
						String message = Language.credentialNotSaved(credential);
						if (caught instanceof ApplicationException) {
							message += "\n" + caught.getMessage();
						}
						new CustomDialogBox(message, Language.okString);
						dialogBox.hide();
					}
					@Override
					public void onSuccess(UsrCredential result) {
						Log.debug("genericDatabaseService.addNewCredential: ok. "+Language.credentialSaved(credential));
						CredentialEditableParametersPanel.this.groupCredential = result;
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
		// if credential is not null, update the credential in the database
		else {	
			try {
				final UsrCredential oldCredential = new UsrCredential(
					groupCredential.getCredential(),
					groupCredential.getStation(),
					groupCredential.getSynoptic(),
					groupCredential.getAlarms(),
					groupCredential.getRdtCtData(),
					groupCredential.getRdtCtStations(),
					groupCredential.getRdtTtData(),
					groupCredential.getRdtTtBoxes(),
					groupCredential.getRdtTtItineraries(),
					groupCredential.getSystem()
				);
				groupCredential.setCredential(getCredential());
				groupCredential.setStation(getStation());
				groupCredential.setSynoptic(getSynoptic());
				groupCredential.setAlarms(getAlarms());
				groupCredential.setRdtCtData(getRdtCtData());
				groupCredential.setRdtCtStations(getRdtCtStations());
				groupCredential.setRdtTtData(getRdtTtData());
				groupCredential.setRdtTtBoxes(getRdtTtBoxes());
				groupCredential.setRdtTtItineraries(getRdtTtItineraries());
				groupCredential.setSystem(getSystem());
				FrontalWebApp.genericDatabaseService.updateCredential(oldCredential, groupCredential, new AsyncCallback<UsrCredential>() {
					@Override
					public void onFailure(Throwable caught) {
						Log.error("genericDatabaseService.updateCredential: failure.\n"+Language.credentialNotSaved(CredentialEditableParametersPanel.this.groupCredential.getCredential()));
						String message = Language.credentialNotSaved(CredentialEditableParametersPanel.this.groupCredential.getCredential());
						if (caught instanceof ApplicationException) {
							message += "\n" + caught.getMessage();
						}
						new CustomDialogBox(message, Language.okString);
						dialogBox.hide();
						table.updateTable();
					}
					@Override
					public void onSuccess(UsrCredential result) {
						Log.debug("genericDatabaseService.updateCredential: ok. "+Language.credentialSaved(CredentialEditableParametersPanel.this.groupCredential.getCredential()));
						CredentialEditableParametersPanel.this.groupCredential = result;
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
	 * Returns the credential parameter entered by the user in the text box.
	 * @return
	 * @throws Exception if the credential is empty
	 */
	private String getCredential() throws Exception{
		String credential = credentialTextBox.getValue().trim();
		if (credential.equals("")) {
			throw new Exception(Language.credentialGroupEmptyError);	
		}
		return credential;
	}
	
	/**
	 * Returns the station parameter chosen by the user in the list box.
	 * @return String 
	 */
	private String getStation(){
		return stationListBox.getSelectedValue();
	}
	
	/**
	 * Returns the synoptic parameter chosen by the user in the list box.
	 * @return String 
	 */
	private String getSynoptic(){
		return synopticListBox.getSelectedValue();
	}
	
	/**
	 * Returns the alarms parameter chosen by the user in the list box.
	 * @return String 
	 */
	private String getAlarms() {
		return alarmsListBox.getSelectedValue();
	}
	
	/**
	 * Returns the rdtCtData parameter chosen by the user in the list box.
	 * @return String 
	 */
	private String getRdtCtData(){
		return rdtCtDataListBox.getSelectedValue();
	}
	
	/**
	 * Returns the rdtCtStations parameter chosen by the user in the list box.
	 * @return String 
	 */
	private String getRdtCtStations(){
		return rdtCtStationsListBox.getSelectedValue();
	}
	
	/**
	 * Returns the rdtTtData parameter chosen by the user in the list box.
	 * @return String 
	 */
	private String getRdtTtData(){
		return rdtTtDataListBox.getSelectedValue();
	}
	
	/**
	 * Returns the rdtTtBoxes parameter chosen by the user in the list box.
	 * @return String 
	 */
	private String getRdtTtBoxes(){
		return rdtTtBoxesListBox.getSelectedValue();
	}
	
	/**
	 * Returns the rdtTtItineraries parameter chosen by the user in the list box.
	 * @return String 
	 */
	private String getRdtTtItineraries(){
		return rdtTtItinerariesListBox.getSelectedValue();
	}
	
	/**
	 * Returns the system parameter chosen by the user in the list box.
	 * @return String 
	 */
	private String getSystem(){
		return systemListBox.getSelectedValue();
	}
}
