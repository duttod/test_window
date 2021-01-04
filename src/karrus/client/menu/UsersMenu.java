package karrus.client.menu;

import karrus.client.FrontalWebApp;
import karrus.client.appearance.Css;
import karrus.client.generic.CustomDialogBox;
import karrus.client.generic.tab.TabbedPanelGeneric;
import karrus.client.users.UserPanelComposite;
import karrus.client.users.users.UsersConfigurationPanel;
import karrus.client.users.users.UserStatusPanel;
import karrus.shared.language.Language;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;

public class UsersMenu extends Label {
	
	private FrontalWebApp frontalWebApp;

	/**
	 * Constructor.
	 * @param frontalWebApp
	 */
	
	public UsersMenu(FrontalWebApp frontalWebApp) {
		this.frontalWebApp = frontalWebApp;
		this.setText(Language.usersLabelString);
		this.setStyleName(Css.subItemclickableLabelStyle);
		this.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					showUsers();
				} catch (Exception e) {
					Log.error(e.getMessage());
					new CustomDialogBox(e.getMessage(), Language.okString);
				}
			}
		});
	}

	public void showUsers() throws Exception {
		String userManagementPanelId = Language.usersLabelString;
		Composite composite = UsersMenu.this.frontalWebApp.getPanelFromId(userManagementPanelId);
		if (composite != null) {
			UsersMenu.this.frontalWebApp.updateMainPanel(composite);
		}	
		else {
			UserPanelComposite container = new UserPanelComposite();
			UsersConfigurationPanel userManagementPanel = new UsersConfigurationPanel();
			UserStatusPanel userStatusPanel = new UserStatusPanel(userManagementPanel);
//			CredentialsConfigurationPanel credentialsConfigurationPanel = new CredentialsConfigurationPanel(FrontalWebApp.databaseObjects.getCredentials());
//			EmailPanel emailPanel = new EmailPanel(L2WebApp.databaseObjects.getEmails());
			container.addComponent(userStatusPanel);
			container.addComponent(userManagementPanel);
//			container.addComponent(credentialsConfigurationPanel);
//			container.addComponent(emailPanel);
			TabbedPanelGeneric tabbedPanel = new TabbedPanelGeneric(container, userManagementPanelId);
			UsersMenu.this.frontalWebApp.addPanel(userManagementPanelId, tabbedPanel);
			UsersMenu.this.frontalWebApp.updateMainPanel(tabbedPanel);
		}
	}
	
}
