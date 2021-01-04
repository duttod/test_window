package karrus.client.login;

import java.util.ArrayList;
import java.util.List;

import karrus.client.FrontalWebApp;
import karrus.client.appearance.Css;
import karrus.client.appearance.LayoutInfo;
import karrus.client.generic.CustomDialogBox;
import karrus.shared.hibernate.UsrCredential;
import karrus.shared.hibernate.UsrUser;
import karrus.shared.language.Language;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.gen2.table.override.client.FlexTable;
import com.google.gwt.gen2.table.override.client.HTMLTable.CellFormatter;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.reveregroup.gwt.imagepreloader.FitImage;
import com.reveregroup.gwt.imagepreloader.ImageLoadEvent;
import com.reveregroup.gwt.imagepreloader.ImageLoadHandler;
import com.reveregroup.gwt.imagepreloader.ImagePreloader;

/**
 * Creates a login dialog box to enter login and password.
 * 
 * @author Estelle DUMAS : estelle.dumas@karrus-its.com
 */
@SuppressWarnings("deprecation")
public class LoginDialogBox extends DialogBox{

	private List<UsrUser> users ; 
	private Label userLabel;
	private TextBox userTextBox;
	private Label passwordLabel;
	private PasswordTextBox passwordTextBox;
	private FrontalWebApp frontalWebApp;
	private Button okButton; 
	private Label statusBarLabel = new Label();

	/**
	 * Constructor. Pre-load the logo image.
	 * @param frontalWebApp 
	 */
	public LoginDialogBox(FrontalWebApp frontalWebApp) {
		this.frontalWebApp = frontalWebApp;
		users = karrus.client.FrontalWebApp.databaseObjects.getUsers();
		this.setStyleName(Css.dialogBoxStyle);
		this.setAnimationEnabled(false);
		ImagePreloader.load(karrus.client.FrontalWebApp.logoString, new ImageLoadHandler() {
			public void imageLoaded(ImageLoadEvent event) {
				showUserLoginDialogBox();
			}
		});
		userTextBox.setFocus(true);
	}

	/**
	 * Continue to load the login dialog box once the logo image has been pre-loaded
	 */
	private void showUserLoginDialogBox(){
		FitImage logo = new FitImage(karrus.client.FrontalWebApp.logoString);
		logo.setWidth(LayoutInfo.logoWidth);
		List<String> logins = new ArrayList<String>();
		for (UsrUser user : users) {
			logins.add(user.getLogin());
		}	
		userLabel = new Label(Language.userString + " :");
		userTextBox = new TextBox();
		userTextBox.setWidth(LayoutInfo.passwordTextBoxWidth);
		userTextBox.addKeyboardListener(new KeyboardListener() {
			public void onKeyDown(Widget sender, char keyCode, int modifiers) {
			}
			public void onKeyPress(Widget sender, char keyCode, int modifiers) {
				if (keyCode == KeyboardListener.KEY_ENTER) {
					checkUserPasswordAndCredential();
				}	
			}
			public void onKeyUp(Widget sender, char keyCode, int modifiers) {
			}
		});
		passwordLabel = new Label(Language.passwordString + " :");
		passwordTextBox = new PasswordTextBox();
		passwordTextBox.setWidth(LayoutInfo.passwordTextBoxWidth);
		passwordTextBox.addKeyboardListener(new KeyboardListener() {
			public void onKeyDown(Widget sender, char keyCode, int modifiers) {
			}
			public void onKeyPress(Widget sender, char keyCode, int modifiers) {
				if (keyCode == KeyboardListener.KEY_ENTER) {
					checkUserPasswordAndCredential();
				}	
			}
			public void onKeyUp(Widget sender, char keyCode, int modifiers) {
			}
		});
		okButton = new Button(Language.okString);
		okButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				checkUserPasswordAndCredential();
			}
		});
		FlexTable table = new FlexTable();
		CellFormatter formatter = table.getCellFormatter();
		table.setWidget(0, 0, userLabel);
		formatter.setAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT, HasVerticalAlignment.ALIGN_MIDDLE);
		table.setWidget(0, 1, userTextBox);
		formatter.setAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_MIDDLE);
		table.setWidget(1, 0, passwordLabel);
		formatter.setAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT, HasVerticalAlignment.ALIGN_MIDDLE);
		table.setWidget(1, 1, passwordTextBox);
		formatter.setAlignment(1, 1, HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_MIDDLE);
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.setSpacing(3);
		verticalPanel.setSize("350px", "160px");
		statusBarLabel.setSize("350px", "25px");
		statusBarLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		verticalPanel.add(logo);
		verticalPanel.add(table);
		verticalPanel.add(okButton);
		verticalPanel.add(statusBarLabel);
		this.setWidget(verticalPanel);
		this.center();
		this.show();
	}

	/**
	 * Check login, password in the database.
	 */
	private void checkUserPasswordAndCredential() {
		String login = userTextBox.getValue();
		String password = passwordTextBox.getText();
		karrus.client.FrontalWebApp.genericDatabaseService.checkUserAndPassword(login, password, new AsyncCallback<UsrUser>() {
			public void onFailure(Throwable caught) {
				Log.debug("genericDatabaseService.checkUserAndPassword: failure. Message : " + caught.getMessage());
				statusBarLabel.setText(caught.getMessage());
				passwordTextBox.setText("");
			}
			public void onSuccess(UsrUser result) {
				Log.debug("genericDatabaseService.checkUserAndPassword: ok");
				// Check if the user's group is defined in the database
				UsrCredential groupCredential = null;
				boolean isCredentialDefined = false;
				List<UsrCredential> credentials= FrontalWebApp.databaseObjects.getCredentials();
				for (UsrCredential credential : credentials) {
					if (credential.getCredential().equals(result.getCredential())) {
						groupCredential = credential;
						isCredentialDefined = true;
						break;
					}
				}
				// If the group is defined, log in
				if (isCredentialDefined) {
					login(result, groupCredential);
				} 
				// else clear the login dialogbox and wait for new entries
				else {
					statusBarLabel.setText(Language.noCredentialDefinedForThisUserError(result));
					passwordTextBox.setText("");
				}
			}
		});
	}
	
	/**
	 * Set the user and the credential in the UserManagement class and continue the application loading.
	 * @param user
	 */
	public void login(final UsrUser user, UsrCredential credential){
		UserManagement.setUser(user);
		UserManagement.setCredential(credential);
		try {
			frontalWebApp.onModuleLoadContinue();
		} catch (Exception e) {
			Log.error(e.getMessage());
			new CustomDialogBox(e.getMessage(), Language.okString);
		}
		hide();
	}
}
