	package karrus.client.users.users;

import java.util.List;

import karrus.client.FrontalWebApp;
import karrus.client.generic.CustomDialogBox;
import karrus.client.appearance.Css;
import karrus.shared.hibernate.UsrUser;
import karrus.shared.language.Language;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Creates a panel to see the current user connected (and change its parameters)
 * and if the user has system rights, shows in a table all users in the database, and enables to edit their parameters.
 * 
 * @author estelle.dumas@karrus-its.com
 */
public class UsersConfigurationPanel extends Composite {

	private VerticalPanel usersPanel;
	private List<UsrUser> users;
	private UsersTable usersTable;

	/**
	 * Constructor.
	 */
	public UsersConfigurationPanel(){
		usersPanel = new VerticalPanel();
		updatePanel();
		VerticalPanel panel = new VerticalPanel();
		panel.add(usersPanel);
		this.initWidget(panel);		
	}

	/**
	 * Adds a panel with buttons to manage users.
	 */
	private void addButtonsPanel(){
		Button editButton = new Button(Language.editUserString);
		editButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				editAction();
			}
		});
		Button addNewUserButton = new Button(Language.addNewUserString);
		addNewUserButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				new NewUserParametersPanel(UsersConfigurationPanel.this);
			}
		});
		Button removeUserButton = new Button(Language.removeUserString);
		removeUserButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				removeUserAction();
			}
		});
		HorizontalPanel buttonsPanel = new HorizontalPanel();
		buttonsPanel.add(editButton);
		buttonsPanel.add(addNewUserButton);
		buttonsPanel.add(removeUserButton);
		buttonsPanel.setSpacing(3);
		usersPanel.add(buttonsPanel);
	}

	/**
	 * Opens a pop up panel to edit the user parameters.
	 */
	private void editAction(){
		try {
			usersTable.editUserParameters();
		} catch (Exception e) {
			Log.error(e.getMessage());
			new CustomDialogBox(e.getMessage(), Language.okString);
		}
	}

	/**
	 * Opens a pop up dialog box to select a user to delete.
	 */
	private void removeUserAction(){
		try {
			usersTable.removeUserAction();
		} catch (Exception e) {
			Log.error(e.getMessage());
			new CustomDialogBox(e.getMessage(), Language.okString);
		}
	}

	/**
	 * Updates the current login and group panel, and the user table.
	 */
	public void updatePanel(){
		FrontalWebApp.genericDatabaseService.getUsers(new AsyncCallback<List<UsrUser>>() {
			@Override
			public void onSuccess(List<UsrUser> result) {
				Log.debug("genericDatabaseService.getUsers: ok");
				users = result;
				if (usersTable==null){
					Label usersListTitleLabel = new Label(Language.userListString);
					usersListTitleLabel.setStyleName(Css.titleLabelStyle);
					usersPanel.add(usersListTitleLabel);
					usersTable = new UsersTable(users, UsersConfigurationPanel.this);
					usersPanel.add(usersTable);
					addButtonsPanel();
				}
				else {
					usersPanel.remove(usersTable);
					usersTable = new UsersTable(users, UsersConfigurationPanel.this);
					usersPanel.insert(usersTable,1);
				}	
			}
			@Override
			public void onFailure(Throwable caught) {
				Log.error("genericDatabaseService.getUsers: failure\n"+caught.getMessage());
				new CustomDialogBox("genericDatabaseService.getUsers: failure", Language.okString);
			}
		});
	}
}