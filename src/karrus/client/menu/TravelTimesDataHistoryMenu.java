package karrus.client.menu;

import karrus.client.FrontalWebApp;
import karrus.client.appearance.Css;
import karrus.client.generic.CustomDialogBox;
import karrus.client.generic.tab.TabbedPanelGeneric;
import karrus.client.history.traveltime.TravelTimeParametersPanel;
import karrus.shared.language.Language;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;

/**
 * Creates a menu for 'historique' : 
 * 
 * @author Estelle DUMAS : estelle.dumas@karrus-its.com
 */
public class TravelTimesDataHistoryMenu extends Label {

	private FrontalWebApp frontalWebApp;

	/**
	 * Constructor.
	 * @param frontalWebApp
	 * @throws Exception 
	 */
	public TravelTimesDataHistoryMenu(FrontalWebApp frontalWebApp) {
		this.frontalWebApp = frontalWebApp;
		this.setText(Language.travelTimeMenu);
		this.setStyleName(Css.subItemclickableLabelStyle);
		this.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					showTP();
				} catch (Exception e) {
					e.printStackTrace();
					Log.error(e.getMessage());
					new CustomDialogBox(e.getMessage(), Language.okString);
				}
			}
		});
	}

	public void showTP() throws Exception {
		String label = Language.travelTimeBluetoothMenu;
		Composite composite = TravelTimesDataHistoryMenu.this.frontalWebApp.getPanelFromId(label);
		if (composite != null)
			TravelTimesDataHistoryMenu.this.frontalWebApp.updateMainPanel(composite);
		else {
			TravelTimeParametersPanel panel = new TravelTimeParametersPanel(frontalWebApp);
			TabbedPanelGeneric tabbedPanel = new TabbedPanelGeneric(panel, label);
			TravelTimesDataHistoryMenu.this.frontalWebApp.addPanel(label, tabbedPanel);
			TravelTimesDataHistoryMenu.this.frontalWebApp.updateMainPanel(tabbedPanel);
		}
	}
}
