package karrus.client.environment.referential;

import java.util.List;

import karrus.client.FrontalWebApp;
import karrus.client.generic.CustomDialogBox;
import karrus.client.generic.ScrollTableGeneric;
import karrus.client.users.admin.AskAdminPasswordDialogBox;
import karrus.client.users.admin.ParentAdminPanel;
import karrus.shared.language.Language;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.gen2.table.client.FixedWidthFlexTable;
import com.google.gwt.gen2.table.client.FixedWidthGrid;
import com.google.gwt.gen2.table.override.client.FlexTable.FlexCellFormatter;
import com.google.gwt.gen2.table.override.client.HTMLTable.CellFormatter;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Shows referentials in a table.
 * 
 * @author estelle.dumas@karrus-its.com
 */
public class ReferentialTable extends Composite implements ParentAdminPanel {

	private List<String> referentials;
	private FixedWidthGrid dataTable;
	private int columnNumber = 4;	
	private VerticalPanel v;
	private ReferentialPanel referentialPanel;
	private String selectedReferential;
	private String currentUsedReferential;

	public ReferentialTable(ReferentialPanel referentialPanel){
		this.referentialPanel = referentialPanel;
		v = new VerticalPanel();
		this.initWidget(v);
	}

	public void setReferentials(List<String> referentials, String currentUsedReferential){
		this.referentials = referentials;
		this.currentUsedReferential = currentUsedReferential;
		FixedWidthFlexTable headerTable = createHeaderTable();
		dataTable = createDataTable();
		ScrollTableGeneric scrollTable = new ScrollTableGeneric(dataTable, headerTable);
		scrollTable.setSize(FrontalWebApp.getWidthForMainPanel()/2+"px", FrontalWebApp.getHeightForMainPanel()/4+"px");
		scrollTable.setMinimumColumnWidth(0, 360);
		scrollTable.setPreferredColumnWidth(0, 370);
		scrollTable.setMaximumColumnWidth(0, 380);
		if (v.getWidgetCount()==0) {
			v.add(scrollTable);
		}	
		else {
			v.remove(0);
			v.add(scrollTable);
		}
	}

	/**
	 * Creates the table with data.
	 * @return a table
	 */
	private FixedWidthGrid createDataTable(){
		int rowNumber = this.referentials.size();
		dataTable = new FixedWidthGrid(rowNumber, columnNumber);
		CellFormatter formatter = dataTable.getCellFormatter();
		int i = 0;
		// we fill the table with the referentials
		for (final String ref : this.referentials){
			int colNb = 0;
			dataTable.setWidget(i, colNb, new Label(ref));
			formatter.setAlignment(i, colNb, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
			colNb++;
			Button downloadButton = new Button(Language.downloadReferentialString);
			downloadButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					downloadAction(ref);
				}
			});
			dataTable.setWidget(i, colNb, downloadButton);
			formatter.setAlignment(i, colNb, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
			colNb++;	
			if (!currentUsedReferential.equals(ref)) {
				Button executeButton = new Button(Language.executeReferentialString);
				executeButton.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						executeAction(ref);
					}
				});
				dataTable.setWidget(i, colNb, executeButton);
				formatter.setAlignment(i, colNb, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
				colNb++;
				Button deleteButton = new Button(Language.deleteReferentialString);
				deleteButton.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						deleteAction(ref);
					}
				});
				dataTable.setWidget(i, colNb, deleteButton);
				formatter.setAlignment(i, colNb, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
				colNb++;
			}
			i++;
		}

		return dataTable;
	}

	private void downloadAction(String referentialFileName){
		String url = GWT.getHostPageBaseURL() + "referential/"+referentialFileName;
		Window.open(url, Language.blankString, "");
	}

	private void executeAction(final String referentialFileName){
		selectedReferential = referentialFileName;
		ClickHandler click1 = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				executeConfirmed();
			}
		};
		ClickHandler click2 = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
			}
		};
		new CustomDialogBox(Language.confirmExecuteReferentialQuestion(referentialFileName), Language.yesString, click1, Language.noString, click2);
	}

	private void executeConfirmed(){
		new AskAdminPasswordDialogBox(this, "execute");
	}

	@Override
	public void actionWhenValidateAdminPassword(String action){
		if (action.equals("execute"))
			FrontalWebApp.genericDatabaseService.executeReferential(selectedReferential, new AsyncCallback<Void>() {
				@Override	
				public void onSuccess(Void result) {
					Log.info("genericDatabaseService.executeReferential: ok");
					new CustomDialogBox(Language.executeReferentialOkMessage(selectedReferential), Language.okString);
				}
				@Override
				public void onFailure(Throwable caught) {
					Log.error("genericDatabaseService.executeReferential: failure" +" "+caught.getMessage());
					new CustomDialogBox(Language.executeReferentialFailureMessage(selectedReferential), Language.okString);
				}
			});
		if (action.equals("delete"))
			FrontalWebApp.genericDatabaseService.deleteReferential(selectedReferential, new AsyncCallback<Void>() {
				@Override	
				public void onSuccess(Void result) {
					Log.info("genericDatabaseService.deleteReferential: ok");
					new CustomDialogBox(Language.deleteReferentialOkMessage(selectedReferential), Language.okString);
					updateTable();
				}
				@Override
				public void onFailure(Throwable caught) {
					Log.error("genericDatabaseService.deleteReferential: failure" +" "+caught.getMessage());
					new CustomDialogBox(Language.deleteReferentialFailureMessage(selectedReferential), Language.okString);
				}
			});
	}

	private void deleteAction(final String referentialFileName){
		selectedReferential = referentialFileName;
		ClickHandler click1 = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				deleteConfirmed();
			}
		};
		ClickHandler click2 = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
			}
		};
		new CustomDialogBox(Language.confirmDeleteReferentialQuestion(referentialFileName), Language.yesString, click1, Language.noString, click2);
	}

	private void deleteConfirmed(){
		new AskAdminPasswordDialogBox(this, "delete");
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
		headerTable.setWidget(0, i, new Label(Language.referentialString));
		formatter.setHorizontalAlignment(0, i, HasHorizontalAlignment.ALIGN_CENTER);
		i++;
		headerTable.setWidget(0, i, new Label());
		formatter.setHorizontalAlignment(0, i, HasHorizontalAlignment.ALIGN_CENTER);
		i++;
		headerTable.setWidget(0, i, new Label());
		formatter.setHorizontalAlignment(0, i, HasHorizontalAlignment.ALIGN_CENTER);
		i++;
		headerTable.setWidget(0, i, new Label());
		formatter.setHorizontalAlignment(0, i, HasHorizontalAlignment.ALIGN_CENTER);
		i++;
		return headerTable;
	}

	/**
	 * Updates the referential table.
	 */
	public void updateTable(){
		referentialPanel.updateTable();
	}
}
