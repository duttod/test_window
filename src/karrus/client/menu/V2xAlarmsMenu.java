package karrus.client.menu;

import karrus.client.FrontalWebApp;
import karrus.client.appearance.Css;
import karrus.client.history.v2x.V2xAlarmsParametersPanel;
import karrus.client.generic.CustomDialogBox;
import karrus.client.generic.tab.TabbedPanelGeneric;
import karrus.shared.language.Language;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;

/**
 * Creates a menu to display v2x alarms : 
 * 
 * @author Estelle DUMAS : estelle.dumas@karrus-its.com
 */
public class V2xAlarmsMenu extends Label {

	private FrontalWebApp frontalWebApp;

	/**
	 * 
	 * @param frontalWebApp
	 */
	public V2xAlarmsMenu(FrontalWebApp frontalWebApp) {
		this.frontalWebApp = frontalWebApp;
		this.setText(Language.v2xAlarmsMenu);
		this.setStyleName(Css.subItemclickableLabelStyle);
		this.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					showV2xAlarms();
				} catch (Exception e) {
					Log.error(e.getMessage());
					new CustomDialogBox(e.getMessage(), Language.okString);
				}
			}
		});
	}


	public void showV2xAlarms() throws Exception {
		String v2xAlarmsPanelId = Language.v2xAlarmsMenu;
		Composite composite = V2xAlarmsMenu.this.frontalWebApp.getPanelFromId(v2xAlarmsPanelId);
		if (composite != null) {
			V2xAlarmsMenu.this.frontalWebApp.updateMainPanel(composite);
			
		}	
		else {
			V2xAlarmsParametersPanel v2xAlarmsParametersPanel = new V2xAlarmsParametersPanel(frontalWebApp);
			TabbedPanelGeneric tabbedPanel = new TabbedPanelGeneric(v2xAlarmsParametersPanel, v2xAlarmsPanelId);
			V2xAlarmsMenu.this.frontalWebApp.addPanel(v2xAlarmsPanelId, tabbedPanel);
			V2xAlarmsMenu.this.frontalWebApp.updateMainPanel(tabbedPanel);
		}
	}
}
