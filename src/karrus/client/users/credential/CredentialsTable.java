package karrus.client.users.credential;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karrus.client.FrontalWebApp;
import karrus.client.appearance.Css;
import karrus.client.generic.CustomDialogBox;
import karrus.client.generic.ScrollTableGeneric;
import karrus.client.login.UserManagement;
import karrus.shared.hibernate.UsrCredential;
import karrus.shared.hibernate.UsrUser;
import karrus.shared.language.Language;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.gen2.table.client.FixedWidthFlexTable;
import com.google.gwt.gen2.table.client.FixedWidthGrid;
import com.google.gwt.gen2.table.override.client.FlexTable.FlexCellFormatter;
import com.google.gwt.gen2.table.override.client.HTMLTable.CellFormatter;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;

/**
 * Shows credentials in a table.
 * 
 * @author estelle.dumas@karrus-its.com
 */
@SuppressWarnings("deprecation")
public class CredentialsTable extends Composite{

	private List<UsrCredential> credentials;
	private Map<Integer, UsrCredential> indexToCredential;
	private FixedWidthGrid dataTable;
	private int currentIndexSelected = -1;
	private int columnNumber = 10;
	private CredentialsConfigurationPanel credentialsConfigurationPanel;

	/**
	 * Constructor.
	 * @param credentials
	 * @param credentialsConfigurationPanel
	 */
	public CredentialsTable(List<UsrCredential> credentials, CredentialsConfigurationPanel credentialsConfigurationPanel) {
		this.credentials = credentials;
		this.credentialsConfigurationPanel = credentialsConfigurationPanel;
		this.indexToCredential = new HashMap<Integer, UsrCredential>();
		FixedWidthFlexTable headerTable = createHeaderTable();
		dataTable = createDataTable();
		ScrollTableGeneric scrollTable = new ScrollTableGeneric(dataTable, headerTable);
		scrollTable.setSize(FrontalWebApp.getWidthForMainPanel()*80/100+"px", FrontalWebApp.getHeightForTable()*30/100+"px");
		this.initWidget(scrollTable);
	}

	/**
	 * Creates the table with data.
	 * @return a table
	 */
	private FixedWidthGrid createDataTable(){
		int rowNumber = this.credentials.size();
		dataTable = new FixedWidthGrid(rowNumber, columnNumber);
		int i = 0;
		// we fill the table with the credentials
		for (UsrCredential credential : this.credentials){
			if (credential!=null)
				setElementInTable(i, credential);
			i++;
		}
		dataTable.addTableListener(new TableListener() {
			@Override
			public void onCellClicked(SourcesTableEvents sender, int row, int cell) {
				int formerSelected = currentIndexSelected;
				currentIndexSelected = row;
				CellFormatter cellFormatter = dataTable.getCellFormatter();
				for(int j=0; j<columnNumber; j++) {
					// underline the selected row
					cellFormatter.addStyleName(currentIndexSelected, j, Css.selectedRowStyle);
					// remove underline for the other rows
					if (formerSelected!=-1 && formerSelected != currentIndexSelected)
						cellFormatter.removeStyleName(formerSelected, j, Css.selectedRowStyle);
					else if (formerSelected == currentIndexSelected) {
						cellFormatter.removeStyleName(formerSelected, j, Css.selectedRowStyle);
						if (j == columnNumber - 1) {
							currentIndexSelected = -1;
						}
					}
				}
			}
		});
		return dataTable;
	}

	/**
	 * Sets the credential in the table
	 * @param i position of the credential in the table
	 * @param groupCredential
	 */
	public void setElementInTable(int i, UsrCredential groupCredential){
		indexToCredential.put(i, groupCredential);
		CellFormatter formatter = dataTable.getCellFormatter();
		String credential = groupCredential.getCredential();
		String station = groupCredential.getStation();
		String synoptic = groupCredential.getSynoptic();
		String alarms = groupCredential.getAlarms();
		String rdtCtData = groupCredential.getRdtCtData();
		String rdtCtStations = groupCredential.getRdtCtStations();
		String rdtTtData = groupCredential.getRdtTtData();
		String rdtTtBoxes = groupCredential.getRdtTtBoxes();
		String rdtTtItineraries = groupCredential.getRdtTtItineraries();
		String system = groupCredential.getSystem();
		int colNb = 0;
		dataTable.setWidget(i, colNb, new Label(credential));
		formatter.setAlignment(i, colNb, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
		colNb++;
		dataTable.setWidget(i, colNb, new Label(station));
		formatter.setAlignment(i, colNb, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
		colNb++;
		dataTable.setWidget(i, colNb, new Label(synoptic));
		formatter.setAlignment(i, colNb, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
		colNb++;
		dataTable.setWidget(i, colNb, new Label(alarms));
		formatter.setAlignment(i, colNb, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
		colNb++;
		dataTable.setWidget(i, colNb, new Label(rdtCtData));
		formatter.setAlignment(i, colNb, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
		colNb++;
		dataTable.setWidget(i, colNb, new Label(rdtCtStations));
		formatter.setAlignment(i, colNb, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
		colNb++;
		dataTable.setWidget(i, colNb, new Label(rdtTtData));
		formatter.setAlignment(i, colNb, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
		colNb++;
		dataTable.setWidget(i, colNb, new Label(rdtTtBoxes));
		formatter.setAlignment(i, colNb, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
		colNb++;
		dataTable.setWidget(i, colNb, new Label(rdtTtItineraries));
		formatter.setAlignment(i, colNb, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
		colNb++;
		dataTable.setWidget(i, colNb, new Label(system));
		formatter.setAlignment(i, colNb, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
	}

	/**
	 * Creates a table with headers for each column.
	 * @return a table
	 */
	private FixedWidthFlexTable createHeaderTable() {
		FixedWidthFlexTable headerTable = new FixedWidthFlexTable();
		FlexCellFormatter formatter = headerTable.getFlexCellFormatter();
		int i = 0;
		headerTable.setWidget(0, i, new Label(Language.credentialGroupHeader));
		formatter.setHorizontalAlignment(0, i, HasHorizontalAlignment.ALIGN_CENTER);
		i++;
		headerTable.setWidget(0, i, new Label(Language.credentialStationHeader));
		formatter.setHorizontalAlignment(0, i, HasHorizontalAlignment.ALIGN_CENTER);
		i++;
		headerTable.setWidget(0, i, new Label(Language.credentialSynopticHeader));
		formatter.setHorizontalAlignment(0, i, HasHorizontalAlignment.ALIGN_CENTER);
		i++;
		headerTable.setWidget(0, i, new Label(Language.credentialAlarmsHeader));
		formatter.setHorizontalAlignment(0, i, HasHorizontalAlignment.ALIGN_CENTER);
		i++;
		headerTable.setWidget(0, i, new Label(Language.credentialRdtCtDataHeader));
		formatter.setHorizontalAlignment(0, i, HasHorizontalAlignment.ALIGN_CENTER);
		i++;
		headerTable.setWidget(0, i, new Label(Language.credentialRdtCtStationsHeader));
		formatter.setHorizontalAlignment(0, i, HasHorizontalAlignment.ALIGN_CENTER);
		i++;
		headerTable.setWidget(0, i, new Label(Language.credentialRdtTtDataHeader));
		formatter.setHorizontalAlignment(0, i, HasHorizontalAlignment.ALIGN_CENTER);
		i++;
		headerTable.setWidget(0, i, new Label(Language.credentialRdtTtBoxesHeader));
		formatter.setHorizontalAlignment(0, i, HasHorizontalAlignment.ALIGN_CENTER);
		i++;
		headerTable.setWidget(0, i, new Label(Language.credentialRdtTtItinerariesHeader));
		formatter.setHorizontalAlignment(0, i, HasHorizontalAlignment.ALIGN_CENTER);
		i++;
		headerTable.setWidget(0, i, new Label(Language.credentialSystemHeader));
		formatter.setHorizontalAlignment(0, i, HasHorizontalAlignment.ALIGN_CENTER);
		return headerTable;
	}

	/**
	 * Opens a pop up panel to edit all parameters of the credential.
	 * @throws Exception if no credential is selected
	 */
	public void editCredentialParameters() throws Exception {
		if (currentIndexSelected==-1) {
			throw new Exception(Language.noSelectedCredentialError);
		}
		// prevent the modification of the current user group
		if (indexToCredential.get(currentIndexSelected).getCredential().equals(UserManagement.getUser().getCredential())) {
			throw new Exception(Language.currentUserGroupModificationError);
		}
		new CredentialEditableParametersPanel(indexToCredential.get(currentIndexSelected), this);
	}

	/**
	 * Opens a pop up panel to confirm the removing of the credential.
	 * @throws Exception if no credential is selected
	 */
	public void removeCredential() throws Exception{
		if (currentIndexSelected==-1) {
			throw new Exception(Language.noSelectedCredentialError);
		}
		// prevent the removal of the current user group
		if (indexToCredential.get(currentIndexSelected).getCredential().equals(UserManagement.getUser().getCredential())) {
			throw new Exception(Language.currentUserGroupRemovalError);
		}
		// prevent the removal of a group which is defined for at least one user
		for (UsrUser user : FrontalWebApp.databaseObjects.getUsers()) {
			if (indexToCredential.get(currentIndexSelected).getCredential().equals(user.getCredential())) {
				throw new Exception(Language.usedGroupRemovalError(indexToCredential.get(currentIndexSelected).getCredential()));
			}
		}
		final UsrCredential seletedCredential = indexToCredential.get(currentIndexSelected);
		final String selectedCredentialCredential = seletedCredential.getCredential();
		String message = Language.confirmRemovingCredential(selectedCredentialCredential);
		String button1Message = Language.yesString;
		ClickHandler action1 = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				removeCredential(seletedCredential);
			}
		};
		String button2Message = Language.noString;
		ClickHandler action2 = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				new CustomDialogBox(Language.credentialNotRemoved(selectedCredentialCredential), Language.okString);
			}
		};
		new CustomDialogBox(message, button1Message, action1, button2Message, action2);
	}

	/**
	 * Removes the credential, i.e. deletes it from the database
	 * @param credential
	 */
	private void removeCredential(final UsrCredential credential){
		FrontalWebApp.genericDatabaseService.removeCredential(credential, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				Log.debug("genericDatabaseService.removeCredential: ok. "+Language.credentialRemoved(credential.getCredential()));
				updateTable();
				FrontalWebApp.updateDatabaseObject();
			}
			@Override
			public void onFailure(Throwable caught) {
				Log.error("genericDatabaseService.removeCredential: failure.\n"+Language.credentialNotRemoved(credential.getCredential()));
				new CustomDialogBox(Language.credentialNotRemoved(credential.getCredential()), Language.okString);
			}
		}); 
	}

	/**
	 * Updates the credentials table.
	 */
	public void updateTable(){
		this.credentialsConfigurationPanel.updateTable();
	}

	/**
	 * Sets the table with no line underlined.
	 */
	public void setNotUnderlined(){
		CellFormatter cellFormatter = dataTable.getCellFormatter();
		for(int j=0; j<columnNumber; j++) {
			if (currentIndexSelected!=-1)
				cellFormatter.removeStyleName(currentIndexSelected, j, Css.selectedRowStyle);
		}
	}
}
