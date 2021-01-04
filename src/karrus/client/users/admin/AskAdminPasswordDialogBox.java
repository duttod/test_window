package karrus.client.users.admin;

import karrus.client.FrontalWebApp;
import karrus.client.generic.CustomDialogBox;
import karrus.client.appearance.Css;
import karrus.client.appearance.LayoutInfo;
import karrus.client.login.UserManagement;
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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Creates a login dialog box with a list box to choose the login and a text box to enter the password.
 * 
 * @author Estelle DUMAS : estelle.dumas@karrus-its.com
 */
@SuppressWarnings("deprecation")
public class AskAdminPasswordDialogBox extends DialogBox {

	private Label passwordLabel;
	private PasswordTextBox passwordTextBox;

	private int nbErrorInPassword = 0;

	private ParentAdminPanel parentAdminPanel;
	
	private String action;
	/**
	 * Constructor.
	 * @param parentAdminPanel ParentAdminPanel
	 */
	public AskAdminPasswordDialogBox(ParentAdminPanel parentAdminPanel) {
		this(parentAdminPanel, null);
		
	}
	public AskAdminPasswordDialogBox(ParentAdminPanel parentAdminPanel, String action) {
		this.parentAdminPanel = parentAdminPanel;
		this.action = action;
		this.setStyleName(Css.dialogBoxStyle);
		this.setAnimationEnabled(false);
		//
		passwordLabel = new Label(Language.passwordString+" pour l'utilisateur "+UserManagement.getUserLogin()+" :");
		passwordTextBox = new PasswordTextBox();
		passwordTextBox.setWidth(LayoutInfo.passwordTextBoxWidth);
		passwordTextBox.addKeyboardListener(new KeyboardListener() {
			@Override
			public void onKeyDown(Widget sender, char keyCode, int modifiers) {}
			@Override
			public void onKeyPress(Widget sender, char keyCode, int modifiers) {
				if (keyCode==KeyboardListener.KEY_ENTER)
					checkUserAndPassword();
			}
			@Override
			public void onKeyUp(Widget sender, char keyCode, int modifiers) {}
		});

		Button button = new Button(Language.okString);
		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				checkUserAndPassword();
			}
		});
		
		Button cancelButton = new Button(Language.cancelString);
		cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				cancelAction();
			}
		});

		FlexTable table = new FlexTable();
		CellFormatter formatter = table.getCellFormatter();
		table.setWidget(0, 0, passwordLabel);
		formatter.setAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
		table.setWidget(0, 1, passwordTextBox);
		formatter.setAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);


		HorizontalPanel buttonsPanel = new HorizontalPanel();
		buttonsPanel.setSpacing(5);
		buttonsPanel.add(button);
		buttonsPanel.add(cancelButton);
		
		VerticalPanel panel = new VerticalPanel();
		panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		panel.setSpacing(3);
		panel.add(table);
		panel.add(buttonsPanel);

		this.setWidget(panel);
		this.center();
		this.show();

	}

	
	private void cancelAction(){
		hide();
	}

	/**
	 * Checks if the login and the password correspond in the database.
	 */
	private void checkUserAndPassword(){
		String password = passwordTextBox.getText();
		try {
			FrontalWebApp.genericDatabaseService.checkUserAndPassword(UserManagement.getUserLogin(), password, new AsyncCallback<UsrUser>() {

				@Override
				public void onFailure(Throwable caught) {
					Log.error("genericDatabaseService.checkUserAndPassword: failure\n"+caught.getMessage());
					new CustomDialogBox("genericDatabaseService.checkUserAndPassword: failure", Language.okString);
				}

				@Override
				public void onSuccess(UsrUser result) {
					Log.debug("genericDatabaseService.checkUserAndPassword: ok");
					if (result==null){
						passwordTextBox.setText("");
						nbErrorInPassword++;
						passwordLabel.setText(UserManagement.getUserLogin()+" : "+Language.passwordNotCorrectError(nbErrorInPassword));
					}
					else {
						hide();
						parentAdminPanel.actionWhenValidateAdminPassword(action);
					}
				}
			});
		} catch (Exception e) {
			Log.error(e.getMessage());
			new CustomDialogBox(e.getMessage(), Language.okString);	
		}
	}




}
