package karrus.client.environment;

import java.util.ArrayList;
import java.util.List;

import karrus.client.FrontalWebApp;
import karrus.client.appearance.Css;
import karrus.client.environment.referential.ReferentialPanel;
import karrus.client.generic.CustomDialogBox;
import karrus.client.generic.ListBoxGeneric;
import karrus.shared.hibernate.SysEnv;
import karrus.shared.language.Language;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.gen2.table.override.client.FlexTable;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class EnvironmentPanel extends Composite {

	private FlexTable mainContainer;
	private FlexTable flexTable;
	private ListBoxGeneric listBoxGeneric;
	private Button applyButton;
	private Button reloadButton;
	private ReferentialPanel referentialPanel;
	private SysEnv serverInstance;
	
	public EnvironmentPanel(SysEnv serverInstance) {
		this.serverInstance = serverInstance;
		this.mainContainer = new FlexTable();
		// Force the vertical panel to be of minimum size
		mainContainer.setHeight("10px");
		this.flexTable = new FlexTable();
		List<String> listBoxValues = new ArrayList<String>();
		listBoxValues.add(Language.unknownString);
		listBoxValues.add(Language.principalString);
		listBoxValues.add(Language.secondaryString);
		applyButton = new Button(Language.applyString);
		applyButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				saveServerInstance();
			}
		});
		reloadButton = new Button(Language.reloadString);
		reloadButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				reloadListBox();
			}
		});
		Label instanceLabel = new Label(Language.instanceString);
		instanceLabel.setStyleName(Css.boldStyle);
		this.listBoxGeneric = new ListBoxGeneric(Language.serverString, listBoxValues, this.serverInstance.getContent());
		flexTable.setWidget(0, 0, applyButton);
		flexTable.setWidget(0, 1, reloadButton);
		referentialPanel = new ReferentialPanel();
		referentialPanel.setWidth(FrontalWebApp.getWidthForBorderedParametersWidgets()+"px");
		referentialPanel.setStyleName(Css.borderStyle);
		VerticalPanel secondaryVerticalPanel = new VerticalPanel();
		secondaryVerticalPanel.add(instanceLabel);
		secondaryVerticalPanel.add(listBoxGeneric);
		secondaryVerticalPanel.add(flexTable);
		secondaryVerticalPanel.setWidth(FrontalWebApp.getWidthForBorderedParametersWidgets()+"px");
		secondaryVerticalPanel.setStyleName(Css.borderStyle);
		mainContainer.setWidget(0, 0, secondaryVerticalPanel);
		mainContainer.setWidget(1, 0, referentialPanel);
		this.initWidget(mainContainer);
	}
	
	public void reloadListBox() {
		FrontalWebApp.genericDatabaseService.getEnvironmentVariable(Language.instance, new AsyncCallback<SysEnv>() {
			@Override
			public void onSuccess(SysEnv result) {
			    Log.debug("genericDatabaseService.getEnvironmentVariable : ok");
                listBoxGeneric.setSelectedItem(result.getContent());
			}
			@Override
			public void onFailure(Throwable caught) {
				Log.debug("genericDatabaseService.getEnvironmentVariable failed : " + caught.getMessage());
				new CustomDialogBox("genericDatabaseService.getEnvironmentVariable failed : " + caught.getMessage(), Language.okString);
			}
		});
	}
	
	public void saveServerInstance() {
		FrontalWebApp.genericDatabaseService.setEnvironmentVariableContent(serverInstance, listBoxGeneric.getSelectedValue(), new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
			    Log.debug("genericDatabaseService.setEnvironmentVariableContent : ok");
			}
			@Override
			public void onFailure(Throwable caught) {
				Log.debug("genericDatabaseService.setEnvironmentVariableContent failed : " + caught.getMessage());
				new CustomDialogBox("genericDatabaseService.setEnvironmentVariableContent failed : " + caught.getMessage(), Language.okString);
			}
		});
	}
}
