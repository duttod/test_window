package karrus.client.menu;

import java.util.ArrayList;
import java.util.List;

import karrus.client.FrontalWebApp;
import karrus.client.generic.CustomDialogBox;
import karrus.client.generic.tab.TabbedPanelGeneric;
import karrus.client.alarm.alarmsHistory.AlarmsHistoryPanel;
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
 * Creates a menu for the alarms history
 * 
 * @author Estelle DUMAS : estelle.dumas@karrus-its.com
 */
public class AlarmsHistoryMenu extends Label {

	private FrontalWebApp frontalWebApp;
	public List<CheckBox> checkBoxesList = new ArrayList<CheckBox>();
	public List<AlAlarmClosed> alarms = new ArrayList<AlAlarmClosed>();
	
	/**
	 * Constructor.
	 * @param karrusDireRdv KarrusDireRdv
	 */
	public AlarmsHistoryMenu(FrontalWebApp karrusDireRdv){

		this.frontalWebApp = karrusDireRdv;
		// alarmes
		this.setText(Language.AlarmsHistorySubMenu);
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
		final String label = Language.alarmsHistoryString;
		Composite comp = AlarmsHistoryMenu.this.frontalWebApp.getPanelFromId(label);
		if (comp != null) {
			AlarmsHistoryMenu.this.frontalWebApp.updateMainPanel(comp);
		}	
		else {
			try {		
				AlarmsHistoryPanel alarmNotificationPanel = new AlarmsHistoryPanel(frontalWebApp);
				TabbedPanelGeneric tabbedPanel = new TabbedPanelGeneric(alarmNotificationPanel, label);
				AlarmsHistoryMenu.this.frontalWebApp.addPanel(label, tabbedPanel);
				AlarmsHistoryMenu.this.frontalWebApp.updateMainPanel(tabbedPanel);	
			} catch (Exception e) {
				Log.error(e.getMessage());
				new CustomDialogBox(e.getMessage(), Language.okString);
			}
		}
	}
}
