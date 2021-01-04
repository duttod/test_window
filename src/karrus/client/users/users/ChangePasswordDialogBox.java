package karrus.client.users.users;

import karrus.client.FrontalWebApp;
import karrus.client.generic.CustomDialogBox;
import karrus.client.generic.PasswordTextBoxGeneric;
import karrus.client.appearance.Css;
import karrus.client.appearance.LayoutInfo;
import karrus.client.login.UserManagement;
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
 * Creates a dialog box to change a password.
 * It is made of 3 text boxes : one for the current password, and 2 for the new password.
 * 
 * @author estelle.dumas@karrus-its.com
 */
public class ChangePasswordDialogBox extends DialogBox {

	private PasswordTextBoxGeneric currentPasswordTextBox;
	private PasswordTextBoxGeneric newPasswordTextBox;
	private PasswordTextBoxGeneric newPassword2TextBox;
	private UsersConfigurationPanel userConfigurationPanel;
	private UserEditableParametersPanel parentUserEditableParametersPanel;
	private UsrUser user;
	
	/**
	 * Constructor.
	 * @param user the user
	 * @param userConfigurationPanel UserManagementPanel
	 * @param parentUserEditableParametersPanel : parentPanel UserEditableParametersPanel 
	 */
	public ChangePasswordDialogBox(UsrUser user, UsersConfigurationPanel userConfigurationPanel, UserEditableParametersPanel parentUserEditableParametersPanel) {
		this.user = user;
		this.parentUserEditableParametersPanel = parentUserEditableParametersPanel;
		this.userConfigurationPanel = userConfigurationPanel;
		currentPasswordTextBox = new PasswordTextBoxGeneric(Language.currentPasswordString, Language.emptyString);
		currentPasswordTextBox.setWidth(LayoutInfo.passwordTextBoxWidth);
		newPasswordTextBox = new PasswordTextBoxGeneric(Language.enterNewPasswordString, Language.emptyString);
		newPasswordTextBox.setWidth(LayoutInfo.passwordTextBoxWidth);
		newPassword2TextBox = new PasswordTextBoxGeneric(Language.reEnterNewPasswordString, Language.emptyString);
		newPassword2TextBox.setWidth(LayoutInfo.passwordTextBoxWidth);
		FlexTable flexTable = new FlexTable();
		int rowNb = 0;
		flexTable.setHTML(rowNb, 0, currentPasswordTextBox.getLabel());
		flexTable.setWidget(rowNb, 1, currentPasswordTextBox.getTextBox());
		rowNb++;
		flexTable.setHTML(rowNb, 0, newPasswordTextBox.getLabel());
		flexTable.setWidget(rowNb, 1, newPasswordTextBox.getTextBox());
		rowNb++;
		flexTable.setHTML(rowNb, 0, newPassword2TextBox.getLabel());
		flexTable.setWidget(rowNb, 1, newPassword2TextBox.getTextBox());
		rowNb++;		
		this.setModal(true);
		this.setAutoHideEnabled(false);
		setStyleName(Css.dialogBoxStyle);
		Button okButton = new Button(Language.saveString);
		okButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				try {
					checkUserWithCurrentLogin(ChangePasswordDialogBox.this.user.getLogin(), getCurrentPassword());
				} catch (Exception e) {
					Log.error(e.getMessage());
					new CustomDialogBox(e.getMessage(), Language.okString);
				}
			}
		});
		Button cancelButton = new Button(Language.cancelString);
		cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				ChangePasswordDialogBox.this.hide();
			}
		});
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		buttonPanel.setSpacing(3);
		VerticalPanel panel = new VerticalPanel();
		panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		panel.add(flexTable);
		panel.add(buttonPanel);
		this.setWidget(panel);
		this.center();
		this.show(); 
	}

	/**
	 * Checks in the database if the login and the password correspond.
	 * @param login String
	 * @param currentPassword String
	 */
	private void checkUserWithCurrentLogin(final String login, String currentPassword) {
		FrontalWebApp.genericDatabaseService.checkUserAndPassword(login, currentPassword, new AsyncCallback<UsrUser>() {
			@Override
			public void onFailure(Throwable caught) {
				Log.error("service.checkUserAndPassword: failure\n"+caught.getMessage());
				new CustomDialogBox("service.checkUserAndPassword: failure;", Language.okString);
			}
			@Override
			public void onSuccess(UsrUser result) {
				Log.debug("service.checkUserAndPassword: ok");
				if (result==null){
					try {
						throw new Exception(Language.incorrectPasswordForUserError(login));
					} catch (Exception e) {
						Log.error(e.getMessage());
						new CustomDialogBox(e.getMessage(), Language.okString);
					}
				}
				else {
					try {
						checkNewPasswordsAreEquals(getNewPassword(), getNewPassword2());
						saveNewPassword(user, getNewPassword());
					} catch (Exception e) {
						Log.error(e.getMessage());
						new CustomDialogBox(e.getMessage(), Language.okString);
					}
				}

			}
		});
	}

	/**
	 * Checks that the new passwords are equals.
	 * @param newPwd String
	 * @param newPwd2 String
	 * @throws Exception if not the same
	 */
	private void checkNewPasswordsAreEquals(String newPwd, String newPwd2) throws Exception {
		if (!newPwd.equals(newPwd2)) { // egalite de chaine, pas besoin de crypter
			throw new Exception(Language.incorrectNewPasswordsError);
		}	
	}

	/**
	 * Save the new password for this user
	 * @param user User
	 * @param newPassword String
	 */
	private void saveNewPassword(final UsrUser user, String newPassword) {
		FrontalWebApp.genericDatabaseService.updatePassword(user, newPassword, new AsyncCallback<UsrUser>() {
			@Override
			public void onFailure(Throwable caught) {
				Log.error("service.updatePassword: failure");
				new CustomDialogBox("service.updatePassword: failure. Please, try again.", Language.okString);
			}
			@Override
			public void onSuccess(UsrUser result) {
				Log.info("updatePassword: ok for user="+result.getLogin());
				ChangePasswordDialogBox.this.hide();
				if (user.getLogin().equals(UserManagement.getUserLogin())) {
					UserManagement.setUser(result);
				}	
				if (parentUserEditableParametersPanel!=null) {
					parentUserEditableParametersPanel.setUser(result);
				}	
				FrontalWebApp.updateDatabaseObject();
				userConfigurationPanel.updatePanel();
			}
		});	
	}

	/**
	 * Gets the current password entered in the 'current password' text box.
	 * @return String
	 * @throws Exception if empty
	 */
	private String getCurrentPassword() throws Exception {
		String currentPassword =  currentPasswordTextBox.getValue();
		if (currentPassword.trim().equals("")) {
			throw new Exception(Language.errorFieldEmpty(Language.currentPasswordString));
		}	
		return currentPassword;
	}

	/**
	 * Gets the new password entered in the 'new password' text box.
	 * @return String
	 * @throws Exception if empty
	 */
	private String getNewPassword() throws Exception {
		String pwd =  newPasswordTextBox.getValue();
		if (pwd.trim().equals("")) {
			throw new Exception(Language.errorFieldEmpty(Language.enterNewPasswordString));
		}	
		return pwd;
	}
	
	/**
	 * Gets the new password entered in the 'new password (2)' text box.
	 * @return String
	 * @throws Exception if empty
	 */
	private String getNewPassword2() throws Exception {
		String pwd =  newPassword2TextBox.getValue();
		if (pwd.trim().equals("")) {
			throw new Exception(Language.errorFieldEmpty(Language.reEnterNewPasswordString));
		}	
		return pwd;
	}
}
