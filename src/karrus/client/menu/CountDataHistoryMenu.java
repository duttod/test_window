package karrus.client.menu;

import karrus.client.FrontalWebApp;
import karrus.client.appearance.Css;
import karrus.client.history.counting.CountingParametersPanel;
import karrus.client.generic.CustomDialogBox;
import karrus.client.generic.tab.TabbedPanelGeneric;
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
public class CountDataHistoryMenu extends Label {

	private FrontalWebApp frontalWebApp;

	/**
	 * Constructor.
	 * @param frontalWebApp A150WebApp
	 */
	public CountDataHistoryMenu(FrontalWebApp frontalWebApp) {

		this.frontalWebApp = frontalWebApp;
		this.setText(Language.countingMenu);
		this.setStyleName(Css.subItemclickableLabelStyle);
		this.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					showCounting();
				} catch (Exception e) {
					Log.error(e.getMessage());
					new CustomDialogBox(e.getMessage(), Language.okString);
				}
			}
		});
	}

	/**
	 * Action when clicking on the "countingMenuString" item in the "historiqueString" menu.
	 */
	public void showCounting() throws Exception {
		
		String countingParametersPanelId = Language.countingDataHistoryTabLabel;
		Composite composite = CountDataHistoryMenu.this.frontalWebApp.getPanelFromId(countingParametersPanelId);
		if (composite != null) {
			CountDataHistoryMenu.this.frontalWebApp.updateMainPanel(composite);
		}	
		else {
			CountingParametersPanel countingParametersPanel = new CountingParametersPanel(frontalWebApp);
			TabbedPanelGeneric tabbedPanel = new TabbedPanelGeneric(countingParametersPanel, countingParametersPanelId);
			CountDataHistoryMenu.this.frontalWebApp.addPanel(countingParametersPanelId, tabbedPanel);
			CountDataHistoryMenu.this.frontalWebApp.updateMainPanel(tabbedPanel);
		}
	}
}
