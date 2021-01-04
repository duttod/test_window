package karrus.client.users.users;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karrus.client.FrontalWebApp;
import karrus.client.generic.CustomDialogBox;
import karrus.client.generic.ScrollTableGeneric;
import karrus.client.appearance.Css;
import karrus.client.login.UserManagement;
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
 * Shows users in a table.
 * 
 * @author estelle.dumas@karrus-its.com
 */
@SuppressWarnings("deprecation")
public class UsersTable extends Composite {

	private List<UsrUser> users;
	private FixedWidthGrid dataTable;
	private Map<Integer, UsrUser> indexToUser;
	private int currentIndexSelected = -1;
	private int columnNumber = 3;
	private UsersConfigurationPanel userConfigurationPanel;
	private UsersGrid usersGrid;

	/**
	 * Constructor.
	 * @param users
	 * @param userConfigurationPanel
	 */
	public UsersTable(List<UsrUser> users, UsersConfigurationPanel userConfigurationPanel){
		this.usersGrid = new UsersGrid(users);
		this.users = users;
		this.userConfigurationPanel = userConfigurationPanel;
		this.indexToUser = new HashMap<Integer, UsrUser>();
		buildTable();
	}

	/**
	 * Builds the table.
	 */
	private void buildTable(){
		FixedWidthFlexTable headerTable = createHeaderTable();
		FixedWidthGrid dataTable = createDataTable();
		ScrollTableGeneric scrollTable = new ScrollTableGeneric(dataTable, headerTable);
		scrollTable.setSize(FrontalWebApp.getWidthForMainPanel()/2 + "px", FrontalWebApp.getHeightForMainPanel()/3+"px");
		this.initWidget(usersGrid.dataGrid);
	}

	/**
	 * Creates the table with data.
	 * @return a table
	 */
	private FixedWidthGrid createDataTable(){
		int rowNumber = this.users.size();
		dataTable = new FixedWidthGrid(rowNumber, columnNumber);
		CellFormatter formatter = dataTable.getCellFormatter();
		int i = 0;
		// we fill the table with the user
		for (UsrUser user : this.users){
			indexToUser.put(i, user);
			String login = user.getLogin();
			String email = user.getEmail();
			String credential = user.getCredential();
			int colNb = 0;
			dataTable.setWidget(i, colNb, new Label(login));
			formatter.setAlignment(i, colNb, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
			colNb++;			
			dataTable.setWidget(i, colNb, new Label(email));
			formatter.setAlignment(i, colNb, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
			colNb++;
			dataTable.setWidget(i, colNb, new Label(credential));
			formatter.setAlignment(i, colNb, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
			colNb++;
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
					if (formerSelected!=-1 && formerSelected != currentIndexSelected) {
						cellFormatter.removeStyleName(formerSelected, j, Css.selectedRowStyle);
					}	
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
	 * Creates a table with headers for each column.
	 * @return a table
	 */
	private FixedWidthFlexTable createHeaderTable() {
		FixedWidthFlexTable headerTable = new FixedWidthFlexTable();
		FlexCellFormatter formatter = headerTable.getFlexCellFormatter();
		headerTable.getColumnFormatter();
		int i = 0;
		headerTable.setWidget(0, i, new Label(Language.loginColumn));
		formatter.setHorizontalAlignment(0, i, HasHorizontalAlignment.ALIGN_CENTER);
		i++;
		headerTable.setWidget(0, i, new Label(Language.emailColumn));
		formatter.setHorizontalAlignment(0, i, HasHorizontalAlignment.ALIGN_CENTER);
		i++;
		headerTable.setWidget(0, i, new Label(Language.groupColumn));
		formatter.setHorizontalAlignment(0, i, HasHorizontalAlignment.ALIGN_CENTER);
		i++;
		return headerTable;
	}

	/**
	 * Opens a pop up panel to edit all parameters of the user.
	 * @throws Exception if no user is selected
	 */
	public void editUserParameters() throws Exception{
		int index = usersGrid.getSelectedStationIndex();
		if (index==-1) {
			throw new Exception(Language.noSelectedUserError);
		}
		if (userConfigurationPanel!=null) {
			new UserEditableParametersPanel(indexToUser.get(index), userConfigurationPanel);
		}	
	}

	/**
	 * Opens a pop up panel to confirm the removing of the user.
	 * @throws Exception if no user is selected
	 */
	public void removeUserAction() throws Exception{
		int index = usersGrid.getSelectedStationIndex();
		if (index==-1) {
			throw new Exception(Language.noSelectedUserError);
		}	
		if (userConfigurationPanel!=null){
			final String selectedUserLogin = indexToUser.get(index).getLogin();
			if (selectedUserLogin.equals(UserManagement.getUserLogin())) {
				throw new Exception(Language.impossibleToRemoveMainUserString);
			}	
			String message = Language.confirmRemovingUser(selectedUserLogin);
			String button1Message = Language.yesString;
			ClickHandler action1 = new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					removeUser(selectedUserLogin);
				}
			};
			String button2Message = Language.noString;
			ClickHandler action2 = new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					new CustomDialogBox(Language.userNotRemoved(selectedUserLogin), Language.okString);
				}
			};
			new CustomDialogBox(message, button1Message, action1, button2Message, action2);
		}
	}


	/**
	 * Removes the user, i.e. deletes it from the database.
	 * @param login
	 */
	private void removeUser(final String login){
		FrontalWebApp.genericDatabaseService.removeUser(login, new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				Log.error("service.removeUser: failure.\n"+Language.userNotRemoved(login));
				new CustomDialogBox(Language.userNotRemoved(login), Language.okString);
			}
			@Override
			public void onSuccess(Void result) {
				Log.debug("service.removeUser: ok. "+Language.userRemoved(login));
				userConfigurationPanel.updatePanel();
				FrontalWebApp.updateDatabaseObject();
			}
		});
	}

	/**
	 * Sets the table with no line underlined.
	 */
	public void setNotUnderlined(){
		CellFormatter cellFormatter = dataTable.getCellFormatter();
		for(int j=0; j<columnNumber; j++) {
			if (currentIndexSelected!=-1) {
				cellFormatter.removeStyleName(currentIndexSelected, j, Css.selectedRowStyle);
			}	
		}
	}
}
