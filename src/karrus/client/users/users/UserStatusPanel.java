package karrus.client.users.users;

import karrus.client.appearance.Css;
import karrus.client.login.UserManagement;
import karrus.shared.language.Language;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UserStatusPanel extends Composite {

	private VerticalPanel verticalPanel;
	private Label loginLabel;
	private Label groupLabel;
	private UsersConfigurationPanel usersConfigurationPanel;
	
	public UserStatusPanel(UsersConfigurationPanel usersConfigurationPanel) {
		
		this.usersConfigurationPanel = usersConfigurationPanel;
		HorizontalPanel currentLoginPanel = new HorizontalPanel();
		currentLoginPanel.setSpacing(3);
		currentLoginPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		currentLoginPanel.add(new Label(Language.user_currentUser));
		loginLabel = new Label();
		setLoginLabel(UserManagement.getUserLogin());
		currentLoginPanel.add(loginLabel);
		HorizontalPanel currentGroupPanel = new HorizontalPanel();
		currentGroupPanel.setSpacing(3);
		currentGroupPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		currentGroupPanel.add(new Label(Language.user_currentGroupe));
		groupLabel = new Label();
		setGroupLabel(UserManagement.getUserGroup());
		currentGroupPanel.add(groupLabel);
		Button changePasswordButton = new Button(Language.changePasswordString);
		changePasswordButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				new ChangePasswordDialogBox(UserManagement.getUser(), UserStatusPanel.this.usersConfigurationPanel, null);
			}
		});
		verticalPanel = new VerticalPanel();
		verticalPanel.add(currentLoginPanel);
		verticalPanel.add(currentGroupPanel);
		verticalPanel.add(new HTML("&nbsp"));
		verticalPanel.add(changePasswordButton);
		this.initWidget(verticalPanel);
	}
	
	/**
	 * Sets the login.
	 * @param login String
	 */
	public void setLoginLabel(String login){
		loginLabel.setText(login);
		loginLabel.setStyleName(Css.boldStyle);
	}

	/**
	 * Set the group.
	 * @param group String
	 */
	private void setGroupLabel(String group){
		groupLabel.setText(group);
		groupLabel.setStyleName(Css.boldStyle);
	}
}
