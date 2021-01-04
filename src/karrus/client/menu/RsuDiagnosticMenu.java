package karrus.client.menu;

import karrus.client.FrontalWebApp;
import karrus.client.appearance.Css;
import karrus.client.history.diagnostic.RsuDiagnosticParametersPanel;
import karrus.client.generic.CustomDialogBox;
import karrus.client.generic.tab.TabbedPanelGeneric;
import karrus.shared.language.Language;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;

/**
 * Creates a menu for roadside units diagnostic : 
 * 
 * @author Estelle DUMAS : estelle.dumas@karrus-its.com
 */
public class RsuDiagnosticMenu extends Label {

	private FrontalWebApp frontalWebApp;

	/**
	 * 
	 * @param frontalWebApp
	 */
	public RsuDiagnosticMenu(FrontalWebApp frontalWebApp) {

		this.frontalWebApp = frontalWebApp;
		this.setText(Language.rsuDiagnosticMenu);
		this.setStyleName(Css.subItemclickableLabelStyle);
		this.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					showRsuDiagnosticPanel();
				} catch (Exception e) {
					Log.error(e.getMessage());
					new CustomDialogBox(e.getMessage(), Language.okString);
				}
			}
		});
	}

	public void showRsuDiagnosticPanel() throws Exception {
		
		String rsuDiagnosticPanelId = Language.rsuDiagnosticMenu;
		Composite composite = RsuDiagnosticMenu.this.frontalWebApp.getPanelFromId(rsuDiagnosticPanelId + "_diag");
		if (composite != null) {
			RsuDiagnosticMenu.this.frontalWebApp.updateMainPanel(composite);
			
		}	
		else {
			RsuDiagnosticParametersPanel rsuDiagnosticParametersPanel = new RsuDiagnosticParametersPanel(frontalWebApp);
			TabbedPanelGeneric tabbedPanel = new TabbedPanelGeneric(rsuDiagnosticParametersPanel, rsuDiagnosticPanelId);
			RsuDiagnosticMenu.this.frontalWebApp.addPanel(rsuDiagnosticPanelId + "_diag", tabbedPanel);
			RsuDiagnosticMenu.this.frontalWebApp.updateMainPanel(tabbedPanel);
		}
	}
}
