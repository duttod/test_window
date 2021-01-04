package karrus.client.menu;

import java.util.List;

import karrus.client.FrontalWebApp;
import karrus.client.appearance.Css;
import karrus.client.equipments.itinerary.ItinerariesConfigurationPanel;
import karrus.client.generic.CustomDialogBox;
import karrus.client.generic.tab.TabbedPanelGeneric;
import karrus.shared.hibernate.TtItinerary;
import karrus.shared.language.Language;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;

/**
 *  Menu to configure itineraries
 * 
 * @author Estelle DUMAS : estelle.dumas@karrus-its.com
 */
public class ItinerariesConfigurationMenu extends Label {

	private FrontalWebApp frontalWebApp;

	/**
	 * Constructor.
	 * @param frontalWebApp
	 * @throws Exception 
	 */
	public ItinerariesConfigurationMenu(FrontalWebApp frontalWebApp) {
		this.frontalWebApp = frontalWebApp;
		this.setText(Language.sectionsConfigString);
		this.setStyleName(Css.subItemclickableLabelStyle);
		this.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					showItineraries();
				} catch (Exception e) {
					Log.error(e.getMessage());
					new CustomDialogBox(e.getMessage(), Language.okString);
				}
			}
		});
	}

	public void showItineraries() throws Exception {
		FrontalWebApp.configurationsDatabaseService.getAllItineraries(new AsyncCallback<List<TtItinerary>>() {
			@Override
			public void onSuccess(List<TtItinerary> result) {
				Log.debug("service.getItineraries: ok");
				try {
					String label = Language.sectionsConfigString;
					Composite composite = ItinerariesConfigurationMenu.this.frontalWebApp.getPanelFromId(label);
					if (composite != null) {
						ItinerariesConfigurationMenu.this.frontalWebApp.updateMainPanel(composite); 
					}	
					else {
						ItinerariesConfigurationPanel panel = new ItinerariesConfigurationPanel(result);
						TabbedPanelGeneric tabbedPanel = new TabbedPanelGeneric(panel, label);
						ItinerariesConfigurationMenu.this.frontalWebApp.addPanel(label, tabbedPanel);
						ItinerariesConfigurationMenu.this.frontalWebApp.updateMainPanel(tabbedPanel);
					}	
				} catch (Exception e){
					e.printStackTrace();
					Log.error(e.getMessage());
					new CustomDialogBox(e.getMessage(), Language.okString);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				Log.error("service.getItineraries: failure\n"+caught.getMessage());
				new CustomDialogBox("service.getItineraries: failure", Language.okString);
			}
		}); 
	}
}
