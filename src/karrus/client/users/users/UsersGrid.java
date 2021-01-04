package karrus.client.users.users;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karrus.client.generic.ResizableHeader;
import karrus.shared.hibernate.UsrUser;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class UsersGrid {

	// The list of data to display.
	private List<UserContent> USERS = new ArrayList<UserContent>();
	public DataGrid<UserContent> dataGrid;
	private UserContent lastSelectedUser;
	private SingleSelectionModel<UserContent> selectionModel;
	private boolean test = true;
	private Map<UserContent, Integer> usersToIndex= new HashMap<UserContent, Integer>();
	
	public UsersGrid(List<UsrUser> users) {
		boolean isSelection = true;
		UsrUser user;
		String login;
		String password;
		String email;
		String credential;
		for (int i=0;i<users.size();i++) {
			user = users.get(i);
			login = user.getLogin();
			password = user.getPassword();
			email = user.getEmail();
			credential = user.getCredential();
			UserContent userContent = new UserContent(login, password, email, credential);
			USERS.add(userContent);
			usersToIndex.put(userContent, i);
		}
		// Create a CellTable.
		dataGrid = new DataGrid<UserContent>();
		//######################################################################
		TextColumn<UserContent> loginColumn = new TextColumn<UserContent>() {
			@Override
			public String getValue(UserContent user) {
				return user.getLogin();
			}
		};
		ResizableHeader<UserContent> resizeLoginColumn = new ResizableHeader<UserContent>(
				"Login", dataGrid, loginColumn );
		resizeLoginColumn.setHeaderStyleNames("headerStyle");
		dataGrid.addColumn(loginColumn, resizeLoginColumn);
		dataGrid.setColumnWidth(0, "150px");
		//######################################################################
//		TextColumn<UserContent> passwordColumn = new TextColumn<UserContent>() {
//			@Override
//			public String getValue(UserContent user) {
//				return "";
//			}
//		};
//		ResizableHeader<UserContent> resizePasswordColumn = new ResizableHeader<UserContent>(
//				"Password", dataGrid, passwordColumn );
//		resizePasswordColumn.setHeaderStyleNames("headerStyle");
//		dataGrid.addColumn(passwordColumn, resizePasswordColumn);
//		//######################################################################		
		TextColumn<UserContent> emailColumn = new TextColumn<UserContent>() {
			@Override
			public String getValue(UserContent user) {
				return user.getEmail();
			}
		};
		ResizableHeader<UserContent> resizeEmailColumn = new ResizableHeader<UserContent>(
				"Email", dataGrid, emailColumn );
		resizeEmailColumn.setHeaderStyleNames("headerStyle");
		dataGrid.addColumn(emailColumn, resizeEmailColumn);
		//######################################################################
		TextColumn<UserContent> credentialColumn = new TextColumn<UserContent>() {
			@Override
			public String getValue(UserContent user) {
				return user.getCredential();
			}
		};
		ResizableHeader<UserContent> resizeCredentialColumn = new ResizableHeader<UserContent>(
				"Groupe", dataGrid, credentialColumn );
		resizeCredentialColumn.setHeaderStyleNames("headerStyle");
		dataGrid.addColumn(credentialColumn, resizeCredentialColumn);
		//######################################################################
		dataGrid.setRowCount(USERS.size(), true);
		dataGrid.setRowData(0, USERS);
		dataGrid.addStyleName("bordered");
		if (isSelection) {
			selectionModel = new SingleSelectionModel<UserContent>();
			dataGrid.setSelectionModel(selectionModel);
			selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
				public void onSelectionChange(SelectionChangeEvent event) {
					UserContent selected = selectionModel.getSelectedObject();
			        if (selected != null) {
			        	lastSelectedUser = selected;
			        	test = false;
			        }
			    }
			});
			dataGrid.addHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {
						@Override
						public void execute() {
							UserContent currentSelectedSet = selectionModel
									.getSelectedObject();
							if (currentSelectedSet == lastSelectedUser && test == true) {
								selectionModel.clear();
								lastSelectedUser = null;
							}
							test = true;

						}
					});
				}
			}, ClickEvent.getType());
		}
		dataGrid.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		dataGrid.setHeight("300px");
		dataGrid.setWidth("600px");
//		dataGrid.setPageSize(5);
//		dataGrid.setAlwaysShowScrollBars(true);
	}
	
	public Integer getSelectedStationIndex() {
		UserContent userContent = selectionModel.getSelectedObject();
		if (userContent == null) {
			return -1;
		}
		return usersToIndex.get(userContent);
	}
}