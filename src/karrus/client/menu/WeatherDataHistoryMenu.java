package karrus.client.menu;

import karrus.client.FrontalWebApp;
import karrus.client.appearance.Css;
import karrus.client.history.weather.WeatherParametersPanel;
import karrus.client.generic.CustomDialogBox;
import karrus.client.generic.tab.TabbedPanelGeneric;
import karrus.shared.language.Language;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;

/**
 * Creates a menu for weather data history : 
 * 
 * @author Estelle DUMAS : estelle.dumas@karrus-its.com
 */
public class WeatherDataHistoryMenu extends Label {

	private FrontalWebApp frontalWebApp;

	/**
	 * Constructor.
	 * @param frontalWebApp A150WebApp
	 */
	public WeatherDataHistoryMenu(FrontalWebApp frontalWebApp){

		this.frontalWebApp = frontalWebApp;
		this.setText(Language.weatherDataMenu);
		this.setStyleName(Css.subItemclickableLabelStyle);
		this.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					showWeatherDataTabPanel();
				} catch (Exception e) {
					Log.error(e.getMessage());
					new CustomDialogBox(e.getMessage(), Language.okString);
				}
			}
		});
	}

	/**
	 * Action when clicking on the weather menu 
	 */
	public void showWeatherDataTabPanel() throws Exception {
		
		String weatherDataTabPanelId = Language.weatherDataHistoryTabLabel;
		Composite composite = WeatherDataHistoryMenu.this.frontalWebApp.getPanelFromId(weatherDataTabPanelId);
		if (composite != null) {
			WeatherDataHistoryMenu.this.frontalWebApp.updateMainPanel(composite);
		}	
		else {
			WeatherParametersPanel countingParametersPanel = new WeatherParametersPanel(frontalWebApp);
			TabbedPanelGeneric tabPanel = new TabbedPanelGeneric(countingParametersPanel, weatherDataTabPanelId);
			WeatherDataHistoryMenu.this.frontalWebApp.addPanel(weatherDataTabPanelId, tabPanel);
			WeatherDataHistoryMenu.this.frontalWebApp.updateMainPanel(tabPanel);
		}
	}
}
