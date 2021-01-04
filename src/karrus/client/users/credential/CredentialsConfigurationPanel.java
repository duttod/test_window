package karrus.client.users.credential;

import java.util.List;

import karrus.client.FrontalWebApp;
import karrus.client.appearance.Css;
import karrus.client.generic.CustomDialogBox;
import karrus.shared.hibernate.UsrCredential;
import karrus.shared.language.Language;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Creates a panel to manage credentials : parameters edition, add credential, remove credential
 * 
 * @author estelle.dumas@karrus-its.com
 */
public class CredentialsConfigurationPanel extends Composite {

	private List<UsrCredential> credentials;
	private CredentialsTable credentialsTable;
	private VerticalPanel panel;
	private HorizontalPanel buttonsPanel;

	/**
	 * Constructor.
	 * @param credentials
	 */
	public CredentialsConfigurationPanel(List<UsrCredential> credentials){
		this.credentials = credentials;
		Button exportTableButton = new Button(Language.CSVexportString);
		exportTableButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				exportTableAction();
			}
		});
		Label titleLabel = new Label(Language.credentialsTitle);
		titleLabel.setStyleName(Css.titleLabelStyle);
		credentialsTable = new CredentialsTable(credentials, this);
		Button editCredentialButton = new Button(Language.editCredentialString);
		editCredentialButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				editCredentialAction();
			}
		});
		Button addNewCredentialButton = new Button(Language.addNewCredentialString);
		addNewCredentialButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				addNewCredentialAction();
			}
		});
		Button removeCredentialButton = new Button(Language.removeCredentialString);
		removeCredentialButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				removeCredentialAction();
			}
		});
		HorizontalPanel editButtonsPanel = new HorizontalPanel();
		editButtonsPanel.setSpacing(3);
		editButtonsPanel.add(editCredentialButton);
		buttonsPanel = new HorizontalPanel();
		buttonsPanel.add(addNewCredentialButton);
		buttonsPanel.add(new HTML("&nbsp"));
		buttonsPanel.add(removeCredentialButton);
		panel = new VerticalPanel();
		panel.setSize("10px", "10px");
		panel.add(titleLabel);
//		VerticalPanel exportButtonContainer = new VerticalPanel();
//		exportButtonContainer.add(exportTableButton);
//		exportButtonContainer.addStyleName(Css.bottomPadding);
//		panel.add(exportButtonContainer);
		panel.add(credentialsTable);
		panel.add(editButtonsPanel);
		editButtonsPanel.add(buttonsPanel);
		this.initWidget(panel);
		
	}

	/**
	 * Export the table containing credentials in a csv file.
	 */
	private void exportTableAction(){
//		L2WebApp.exportService.exportCredentialsTable(credentials, new AsyncCallback<String>() {
//			@Override
//			public void onSuccess(String result) {
//				Log.debug("service.exportCredentialsTable: ok");
//				String baseURL = GWT.getHostPageBaseURL();
//				String url = baseURL + "ExportCountDataTableServlet//" + result;
//				Window.open(url, Language.blankString, "");
//			}
//			@Override
//			public void onFailure(Throwable caught) {
//				Log.error("service.exportCredentialsTable: failure\n"+caught.getMessage());
//				new CustomDialogBox(Language.genericErrorString, Language.okString);
//			}
//		});
	}
	
	/**
	 * Opens a pop up panel to edit all parameters of the credential
	 */
	private void editCredentialAction(){
		try {
			credentialsTable.editCredentialParameters();
		} catch (Exception e) {
			Log.error(e.getMessage());
			new CustomDialogBox(e.getMessage(), Language.okString);
		}
	}
	
	/**
	 * Opens a pop up panel to edit all parameters of the new credential.
	 */
	private void addNewCredentialAction(){
		credentialsTable.setNotUnderlined();
		new CredentialEditableParametersPanel(null, credentialsTable);
	}

	/**
	 * Updates the credentials table.
	 */
	public void updateTable(){
		FrontalWebApp.genericDatabaseService.getCredentials(new AsyncCallback<List<UsrCredential>>() {
			@Override
			public void onSuccess(List<UsrCredential> result) {
				Log.debug("genericDatabaseService.getCredentials: ok");
				credentials = result;
				if (credentialsTable!=null) {
					panel.remove(credentialsTable);
				}	
				credentialsTable = new CredentialsTable(credentials, CredentialsConfigurationPanel.this);
				panel.insert(credentialsTable, 1);
				panel.setCellHeight(credentialsTable, "25%");
			}
			@Override
			public void onFailure(Throwable caught) {
				Log.error("genericDatabaseService.getCredentials: failure\n"+caught.getMessage());
				new CustomDialogBox("genericDatabaseService.getCredentials: failure", Language.okString);
			}
		});
	}

	/**
	 * Removes credential
	 */
	private void removeCredentialAction(){
		try {
			credentialsTable.removeCredential();
		} catch (Exception e) {
			Log.error(e.getMessage());
			new CustomDialogBox(e.getMessage(), Language.okString);
		}
	}
}
