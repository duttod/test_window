package karrus.client.users.users;

import java.util.ArrayList;
import java.util.List;

import karrus.client.FrontalWebApp;
import karrus.client.generic.CustomDialogBox;
import karrus.client.generic.ListBoxGeneric;
import karrus.client.generic.PasswordTextBoxGeneric;
import karrus.client.generic.TextBoxGeneric;
import karrus.client.appearance.Css;
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
 * Creates a pop up panel to create a new user, i.e. to edit all parameters of a user (login, password, email, group).
 * 
 * @author estelle.dumas@karrus-its.com
 */
public class NewUserParametersPanel {

	private TextBoxGeneric loginPanel;
	private PasswordTextBoxGeneric newPasswordPanel;
	private PasswordTextBoxGeneric newPassword2Panel;
	private TextBoxGeneric emailPanel;
	private ListBoxGeneric credentialPanel;
	private DialogBox dialogBox;
	private UsersConfigurationPanel usrConfigurationPanel;

	/**
	 * Constructor.
	 * @param usrConfigurationPanel
	 */
	public NewUserParametersPanel(UsersConfigurationPanel usrConfigurationPanel){
		this.usrConfigurationPanel = usrConfigurationPanel;
		buildPanel();
	}

	/**
	 * Builds the panel : a text box for the login, 2 text boxes for the password, a text box for the email and a list box for the group.
	 */
	private void buildPanel(){
		List<String> userGroups = new ArrayList<String>();
		for (UsrCredential credential : FrontalWebApp.databaseObjects.getCredentials()) {
			userGroups.add(credential.getCredential());
		}
		this.loginPanel = new TextBoxGeneric(Language.userField, Language.emptyString);
		this.newPasswordPanel = new PasswordTextBoxGeneric(Language.newPasswordField, Language.emptyString);
		this.newPassword2Panel = new PasswordTextBoxGeneric(Language.newPassword2Field, Language.emptyString);
		this.emailPanel = new TextBoxGeneric(Language.userEmailField, Language.emptyString);
		this.credentialPanel = new ListBoxGeneric(Language.groupField, userGroups, Language.emptyString, true);
		int i = 0;
		FlexTable flexTable = new FlexTable();
		flexTable.setHTML(i, 0, loginPanel.getLabel());
		flexTable.setWidget(i, 1, loginPanel.getTextBox());
		i++;
		flexTable.setHTML(i, 0, newPasswordPanel.getLabel());
		flexTable.setWidget(i, 1, newPasswordPanel.getTextBox());
		i++;
		flexTable.setHTML(i, 0, newPassword2Panel.getLabel());
		flexTable.setWidget(i, 1, newPassword2Panel.getTextBox());
		i++;
		flexTable.setHTML(i, 0, emailPanel.getLabel());
		flexTable.setWidget(i, 1, emailPanel.getTextBox());
		i++;
		flexTable.setHTML(i, 0, credentialPanel.getLabel());
		flexTable.setWidget(i, 1, credentialPanel.getListBox());
		i++;
		Button saveButton = new Button(Language.saveString);
		saveButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				NewUserParametersPanel.this.saveAction();
			}
		});
		Button cancelButton = new Button(Language.cancelString);
		cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				NewUserParametersPanel.this.cancelAction();
			}
		});
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(5);
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
	 * Action to do when the 'cancelButton' is clicked: no change is saved.
	 */
	public void cancelAction(){
		dialogBox.hide();
	}

	/**
	 * Saves a new user.
	 */
	public void saveAction() {
		// create a new user in the database
		try {
			final String login = getLogin();
			checkNewPasswordsAreEquals(getNewPassword(), getNewPassword2());
			FrontalWebApp.genericDatabaseService.addNewUser(login, getNewPassword(), getEmail(), getCredential(), new AsyncCallback<UsrUser>() {
				@Override
				public void onFailure(Throwable caught) {
					Log.error("service.addNewUser: failure.\n"+Language.userNotSaved(login));
					String message = Language.userNotSaved(login);
					if (caught instanceof ApplicationException) {
						message += "\n" + Language.userConstraintViolationError(getLogin());
					}
					new CustomDialogBox(message, Language.okString);
					dialogBox.hide();
				}
				@Override
				public void onSuccess(UsrUser result) {
					Log.debug("service.addNewUser: ok. "+Language.userSaved(login));
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
	 * Checks that the new passwords are equals.
	 * @param newPwd String
	 * @param newPwd2 String
	 * @throws Exception if not the same
	 */
	private void checkNewPasswordsAreEquals(String newPwd, String newPwd2) throws Exception{
		if (!newPwd.equals(newPwd2)) // egalite de chaine, pas besoin de crypter
			throw new Exception(Language.incorrectNewPasswordsError);
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
	 * Returns the new password parameter entered by the user in the text box.
	 * @return String
	 */
	private String getNewPassword() {
		String pwd = newPasswordPanel.getValue().trim();
		return pwd;
	}
	
	/**
	 * Returns the new password2 parameter entered by the user in the text box.
	 * @return String
	 */
	private String getNewPassword2() {
		String pwd = newPassword2Panel.getValue().trim();
		return pwd;
	}
	
	/**
	 * Returns the email parameter entered by the user in the text box.
	 * @return 
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
	 * Updates the user table in the userManagementPanel
	 */
	public void updateTable(){
		usrConfigurationPanel.updatePanel();
	}
	
	
}
