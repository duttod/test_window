package karrus.client.environment.referential;

import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.Uploader;

import java.util.List;

import karrus.client.FrontalWebApp;
import karrus.client.generic.CustomDialogBox;
import karrus.client.appearance.Css;
import karrus.shared.language.Language;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.gen2.table.override.client.FlexTable;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Creates a panel to work on referential
 * 
 * @author estelle.dumas@karrus-its.com
 */
public class ReferentialPanel extends Composite {

	private ReferentialTable referentialTable;
	private Uploader defaultUploader;
	/**
	 * Constructor.
	 */
	public ReferentialPanel(){
		
		Label title = new Label(Language.availableReferentials);
		title.setStyleName(Css.boldStyle);
		referentialTable = new ReferentialTable(this);
		updateTable();
		// upload
		defaultUploader = new Uploader();
		Button button = new Button(Language.uploadString);
		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					String fileName = defaultUploader.getFileName();
					if (fileName.equals(""))
						throw new Exception(Language.referentialFileNameExtensionError);
					//else fileName not empty
					int index = fileName.lastIndexOf(".");
					if (index==-1)
						throw new Exception(Language.referentialFileNameExtensionError);
					//else index!=-1
					String suffix = fileName.substring(index, fileName.length());
					if (!suffix.equals(".ref"))
						throw new Exception(Language.referentialFileNameExtensionError);
					defaultUploader.submit();
				} catch (Exception e){
					new CustomDialogBox(e.getMessage(), Language.okString);
				}
			}
		});
		defaultUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
		HorizontalPanel upPanel = new HorizontalPanel();
		upPanel.add(new Label(Language.uploadReferentialString));
		upPanel.add(defaultUploader);
		upPanel.add(button);
		Label l = new Label(Language.uploadReferentialString);
		l.setStyleName(Css.boldStyle);
		Label saveCurrentReferentialLabel = new Label(Language.saveCurrentReferentialString);
		saveCurrentReferentialLabel.setStyleName(Css.boldStyle);
		Button saveCurrentReferentialButton = new Button(Language.saveCurrentReferentialButtonName);
		saveCurrentReferentialButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				FrontalWebApp.genericDatabaseService.saveCurrentReferential(new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable caught) {
						Log.error("genericDatabaseService.saveCurrentReferential : failed " + caught.getMessage());
						new CustomDialogBox(Language.genericErrorString, Language.okString);
					}

					@Override
					public void onSuccess(Void result) {
						Log.info("genericDatabaseService.saveCurrentReferential : ok");
						new CustomDialogBox(Language.currentReferentialSavedString, Language.okString);
					}
				});
			}
		});
		FlexTable f = new FlexTable();
		f.setWidget(0, 0, l);
		f.setWidget(0, 1, defaultUploader);
		f.setWidget(0, 2, button);
		f.setWidget(1, 0, saveCurrentReferentialLabel);
		f.setWidget(1, 1, saveCurrentReferentialButton);
		VerticalPanel panel = new VerticalPanel();
		panel.setSpacing(5);
		panel.add(title);
		panel.setCellHeight(title, "5%");
		panel.add(referentialTable);
		panel.setCellHeight(referentialTable, "40%");
		panel.add(f);
		this.initWidget(panel);
	}

	private IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler() {
		@Override
		public void onFinish(IUploader uploader) {
			@SuppressWarnings("deprecation")
			String response = uploader.getServerInfo().message;
			if (uploader.getStatus() == Status.SUCCESS) {
				if (response.contains("ERROR")){
					String err = response.split("ERROR : ")[1];
					new CustomDialogBox(Language.uploadReferentialFailureMessage(err), Language.okString);
				}
				else {
					String name = response.split("File saved as : ")[0];
					name = name.substring(name.lastIndexOf('/')+1);
					new CustomDialogBox(Language.uploadReferentialOkMessage(name), Language.okString);
					updateTable();
				}
			}
			defaultUploader.reuse();
		}
	};

	/**
	 * Updates the referential table.
	 */
	public void updateTable(){
		FrontalWebApp.genericDatabaseService.getCurrentReferentialFile(new AsyncCallback<String>() {
			@Override
			public void onSuccess(final String currentRef) {
				Log.debug("hostService.getCurrentReferentialFile: ok");			
				FrontalWebApp.genericDatabaseService.getReferentialFiles(new AsyncCallback<List<String>>() {
					@Override
					public void onSuccess(List<String> result) {
						Log.debug("hostService.getReferentialFiles: ok");			
						referentialTable.setReferentials(result, currentRef);
					}
					@Override
					public void onFailure(Throwable caught) {
						Log.error("hostService.getReferentialFiles: failure\n"+caught.getMessage());
						new CustomDialogBox("hostService.getReferentialFiles: failure", Language.okString);
					}
				});
			}
			@Override
			public void onFailure(Throwable caught) {
				Log.error("hostService.getCurrentReferentialFile: failure\n"+caught.getMessage());
				new CustomDialogBox("hostService.getCurrentReferentialFile: failure", Language.okString);
			}
		});
		
	}
}
