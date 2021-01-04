package karrus.client.users.users;

import java.util.ArrayList;
import java.util.List;

import karrus.client.FrontalWebApp;
import karrus.client.generic.CustomDialogBox;
import karrus.client.generic.ListBoxGeneric;
import karrus.client.generic.TextBoxGeneric;
import karrus.client.appearance.Css;
import karrus.client.login.UserManagement;
import karrus.shared.ApplicationException;
import karrus.shared.hibernate.UsrCredential;
import karrus.shared.hibernate.UsrUser;
import karrus.shared.language.Language;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * Creates a pop up panel to edit parameters of a user (login, password, email, group)
 * 
 * @author estelle.dumas@karrus-its.com
 */
public class UserEditableParametersPanel {

	private TextBoxGeneric loginPanel;
	private TextBoxGeneric emailPanel;
	private ListBoxGeneric credentialPanel;
	private UsrUser user;
	private DialogBox dialogBox;
	private UsersConfigurationPanel usrConfigurationPanel;

	/**
	 * Constructor.
	 * @param user
	 * @param usrConfigurationPanel
	 */
	public UserEditableParametersPanel(UsrUser user, UsersConfigurationPanel usrConfigurationPanel){
		this.user = user;
		this.usrConfigurationPanel = usrConfigurationPanel;
		buildPanel();
	}

	/**
	 * Builds the panel : a text box for the login and a list box for the group.
	 */
	private void buildPanel(){
		List<String> userGroups = new ArrayList<String>();
		for (UsrCredential credential : FrontalWebApp.databaseObjects.getCredentials()) {
			userGroups.add(credential.getCredential());
		}
		this.loginPanel = new TextBoxGeneric(Language.userField, this.user.getLogin());
		this.emailPanel = new TextBoxGeneric(Language.userEmailField, this.user.getEmail());
		this.credentialPanel = new ListBoxGeneric(Language.groupField, userGroups, this.user.getCredential(), false);
		if (user.getLogin().equals(UserManagement.getUserLogin())) {
			this.credentialPanel.setEnabled(false);		
		}	
		int i = 0;
		FlexTable flexTable = new FlexTable();
		flexTable.setHTML(i, 0, loginPanel.getLabel());
		flexTable.setWidget(i, 1, loginPanel.getTextBox());
		i++;
		flexTable.setHTML(i, 0, emailPanel.getLabel());
		flexTable.setWidget(i, 1, emailPanel.getTextBox());
		i++;
		flexTable.setHTML(i, 0, credentialPanel.getLabel());
		flexTable.setWidget(i, 1, credentialPanel.getListBox());
		i++;
		Button editPasswordButton = new Button(Language.changePasswordString);
		editPasswordButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				new ChangePasswordDialogBox(user, usrConfigurationPanel, UserEditableParametersPanel.this);
			}
		});
		Button saveButton = new Button(Language.saveString);
		saveButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				updateUser();
			}
		});
		Button cancelButton = new Button(Language.cancelString);
		cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				UserEditableParametersPanel.this.cancelAction();
			}
		});
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(5);
		buttonPanel.add(editPasswordButton);
		buttonPanel.add(saveButton);
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
	 * Hides the dialog box.
	 */
	public void hide(){
		dialogBox.hide();
	}
	
	/**
	 * Sets the user.
	 * @param user User
	 */
	public void setUser(UsrUser user){
		this.user = user;
	}

	/**
	 * Action to do when the 'cancelButton' is clicked: no change is saved.
	 */
	public void cancelAction(){
		dialogBox.hide();
	}

	/**
	 * Saves all parameters are in the database : update the user.
	 */
	private void updateUser() {
		// update the user in the database
		try {
			String credential = getCredential();
			final String formerLogin = UserEditableParametersPanel.this.user.getLogin();
			FrontalWebApp.genericDatabaseService.updateUser(UserEditableParametersPanel.this.user, getLogin(), getEmail(), credential, new AsyncCallback<UsrUser>() {
				@Override
				public void onFailure(Throwable caught) {
					Log.error("service.updateUser: failure.\n"+Language.userNotSaved(formerLogin));
					String message = Language.userNotSaved(formerLogin);
					if (caught instanceof ApplicationException) {
						message += "\n" + Language.userConstraintViolationError(getLogin());
					}
					new CustomDialogBox(message, Language.okString);
					usrConfigurationPanel.updatePanel();
					dialogBox.hide();
				}
				@Override
				public void onSuccess(UsrUser result) {
					Log.debug("service.updateUser: ok. "+Language.userSaved(result.getLogin()));
					UserEditableParametersPanel.this.user = result;
					if (formerLogin.equals(UserManagement.getUserLogin())) {
						UserManagement.setUser(result);
					}	
					if (usrConfigurationPanel!=null) {
						usrConfigurationPanel.updatePanel();
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

	/**
	 * Returns the login parameter entered by the user in the text box.
	 * @return string
	 */
	private String getLogin() {
		String login = loginPanel.getValue().trim();
		return login;
	}
	
	/**
	 * Returns the email parameter entered by the user in the text box.
	 * @return string
	 */
	private String getEmail() {
		String email = emailPanel.getValue().trim();
		return email;
	}
	
	/**
	 * Returns the credential parameter chosen by the user in the list box.
	 * @return String
	 */
	private String getCredential() {
		String credential = credentialPanel.getSelectedValue().trim();	
		return credential;
	}

	/**
	 * Updates the user table.
	 */
	public void updateTable(){
		usrConfigurationPanel.updatePanel();
	}
}
