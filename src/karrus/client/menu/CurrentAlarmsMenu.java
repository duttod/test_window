package karrus.client.menu;

import java.util.ArrayList;
import java.util.List;

import karrus.client.FrontalWebApp;
import karrus.client.generic.CustomDialogBox;
import karrus.client.generic.tab.TabbedPanelGeneric;
import karrus.client.alarm.currentAlarms.CurrentAlarmsPanel;
import karrus.client.appearance.Css;
import karrus.shared.hibernate.AlAlarmClosed;
import karrus.shared.language.Language;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;

/**
 * Creates a menu for current alarms
 * 
 * @author Estelle DUMAS : estelle.dumas@karrus-its.com
 */
public class CurrentAlarmsMenu extends Label {

	private FrontalWebApp frontalWebApp;
	public List<CheckBox> checkBoxesList = new ArrayList<CheckBox>();
	public List<AlAlarmClosed> alarms = new ArrayList<AlAlarmClosed>();
	
	/**
	 * 
	 * @param frontalWebApp
	 */
	public CurrentAlarmsMenu(FrontalWebApp frontalWebApp){

		this.frontalWebApp = frontalWebApp;
		// alarmes
		this.setText(Language.currentAlarmsSubMenu);
		this.setStyleName(Css.subItemclickableLabelStyle);
		this.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					showAlarms();
				} catch (Exception e) {
					Log.error(e.getMessage());
					new CustomDialogBox(e.getMessage(), Language.okString);
				}
			}
		});
	}

	private void showAlarms() throws Exception {
		final String label = Language.currentAlarmsString;
		Composite comp = CurrentAlarmsMenu.this.frontalWebApp.getPanelFromId(label);
		if (comp != null) {
			CurrentAlarmsMenu.this.frontalWebApp.updateMainPanel(comp);
		}	
		else {
			try {		
				CurrentAlarmsPanel alarmNotificationPanel = new CurrentAlarmsPanel(frontalWebApp);
				TabbedPanelGeneric tabbedPanel = new TabbedPanelGeneric(alarmNotificationPanel, label);
				CurrentAlarmsMenu.this.frontalWebApp.addPanel(label, tabbedPanel);
				CurrentAlarmsMenu.this.frontalWebApp.updateMainPanel(tabbedPanel);	
			} catch (Exception e) {
				Log.error(e.getMessage());
				new CustomDialogBox(e.getMessage(), Language.okString);
			}
		}
	}
}
