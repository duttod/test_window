package karrus.client.menu;

import java.util.List;

import karrus.client.FrontalWebApp;
import karrus.client.appearance.Css;
import karrus.client.equipments.lane.LanesConfigurationPanel;
import karrus.client.generic.CustomDialogBox;
import karrus.client.generic.tab.TabbedPanelGeneric;
import karrus.shared.hibernate.CtLane;
import karrus.shared.language.Language;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;

/**
 *  Menu to configure lanes
 * 
 * @author Estelle DUMAS : estelle.dumas@karrus-its.com
 */
public class LanesConfigurationMenu extends Label {

	private FrontalWebApp frontalWebApp;

	/**
	 * Constructor.
	 * @param frontalWebApp
	 * @throws Exception 
	 */
	public LanesConfigurationMenu(FrontalWebApp frontalWebApp) {
		this.frontalWebApp = frontalWebApp;
		this.setText(Language.lanesConfigurationMenu);
		this.setStyleName(Css.subItemclickableLabelStyle);
		this.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					showLanes();
				} catch (Exception e) {
					Log.error(e.getMessage());
					new CustomDialogBox(Language.genericErrorString, Language.okString);
				}
			}
		});
	}

	public void showLanes() throws Exception {
		FrontalWebApp.genericDatabaseService.getAllLanes(new AsyncCallback<List<CtLane>>() {
			@Override
			public void onSuccess(List<CtLane> result) {
				Log.debug("genericDatabaseService.getAllLanes : ok");
				try {
					String tabLabel = Language.lanesConfigurationMenuTabLabel;
					String menuId = Language.lanesConfigurationMenuId;
					Composite composite = LanesConfigurationMenu.this.frontalWebApp.getPanelFromId(menuId);
					if (composite != null) {
						LanesConfigurationMenu.this.frontalWebApp.updateMainPanel(composite); 
					}	
					else {
						LanesConfigurationPanel panel = new LanesConfigurationPanel(result);
						TabbedPanelGeneric tabbedPanel = new TabbedPanelGeneric(panel, tabLabel);
						LanesConfigurationMenu.this.frontalWebApp.addPanel(tabLabel, tabbedPanel);
						LanesConfigurationMenu.this.frontalWebApp.updateMainPanel(tabbedPanel);
					}	
				} catch (Exception e){
					Log.error(e.getMessage());
					new CustomDialogBox(e.getMessage(), Language.okString);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				Log.error("genericDatabaseService.getAllLanes: failure\n"+caught.getMessage());
				new CustomDialogBox(Language.genericErrorString, Language.okString);
			}
		}); 
	}
}
