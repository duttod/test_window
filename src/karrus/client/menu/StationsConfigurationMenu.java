package karrus.client.menu;

import java.util.List;

import karrus.client.FrontalWebApp;
import karrus.client.appearance.Css;
import karrus.client.equipments.station.StationsConfigurationPanel;
import karrus.client.generic.CustomDialogBox;
import karrus.client.generic.tab.TabbedPanelGeneric;
import karrus.shared.hibernate.RsuStation;
import karrus.shared.language.Language;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;

/**
 * Creates a menu for 'historique' : 
 * 
 * @author Estelle DUMAS : estelle.dumas@karrus-its.com
 */
public class StationsConfigurationMenu extends Label {

	private FrontalWebApp frontalWebApp;
	
	/**
	 * Constructor.
	 * @param frontalWebApp
	 */
	public StationsConfigurationMenu(FrontalWebApp frontalWebApp){

		this.frontalWebApp = frontalWebApp;
		this.setText(Language.stationsConfigString);
		this.setStyleName(Css.subItemclickableLabelStyle);
		this.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					showStations();
				} catch (Exception e) {
					Log.error(e.getMessage());
					new CustomDialogBox(e.getMessage(), Language.okString);
				}
			}
		});
	}	
	
	public void showStations() throws Exception {
		FrontalWebApp.configurationsDatabaseService.getStations(new AsyncCallback<List<RsuStation>>() {
			@Override
			public void onSuccess(List<RsuStation> result) {
				Log.debug("service.getStations: ok");
				try {
					String stationsConfigurationPanelId = Language.stationsConfigString;
					Composite composite = StationsConfigurationMenu.this.frontalWebApp.getPanelFromId(stationsConfigurationPanelId);
					if (composite != null) {
						StationsConfigurationMenu.this.frontalWebApp.updateMainPanel(composite); 
					}	
					else {
						StationsConfigurationPanel stationsConfigurationPanel = new StationsConfigurationPanel(result);
						TabbedPanelGeneric tabbedPanel = new TabbedPanelGeneric(stationsConfigurationPanel, stationsConfigurationPanelId);
						StationsConfigurationMenu.this.frontalWebApp.addPanel(stationsConfigurationPanelId, tabbedPanel);
						StationsConfigurationMenu.this.frontalWebApp.updateMainPanel(tabbedPanel);
					}
				} catch (Exception e){
					Log.error(e.getMessage());
					new CustomDialogBox(e.getMessage(), Language.okString);
				}
			}
			@Override
			public void onFailure(Throwable caught) {
				Log.error("service.getStations: failure\n"+caught.getMessage());
				new CustomDialogBox("service.getStations: failure", Language.okString);
			}
		}); 
	}
}
