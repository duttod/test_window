package karrus.client.menu;

import karrus.client.FrontalWebApp;
import karrus.client.appearance.Css;
import karrus.client.generic.CustomDialogBox;
import karrus.client.generic.tab.TabbedPanelGeneric;
import karrus.client.synoptic.SynopticPanelImplementation;
import karrus.client.synoptic.ServersSynopticPanel;
import karrus.client.synoptic.SynopticPanel;
import karrus.shared.language.Language;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;

public class SynopticMenu extends Label {
	
	private FrontalWebApp frontalWebApp;
	private SynopticPanel rdtSynopticPanel;
	private SynopticPanel tpSynopticPanel;
	private SynopticPanel weatherSynopticPanel;
	private SynopticPanel serversSynopticPanel;

	/**
	 * Constructor.
	 * @param frontalWebApp
	 */
	
	public SynopticMenu(FrontalWebApp frontalWebApp){

		this.frontalWebApp = frontalWebApp;
		this.setText(Language.synopticMenu);
		this.setStyleName(Css.subItemclickableLabelStyle);
		this.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					showSynoptic();
				} catch (Exception e) {
					Log.error(e.getMessage());
					new CustomDialogBox(e.getMessage(), Language.okString);
				}
			}
		});
	}

	public void showSynoptic() throws Exception {
		final String synopticId = Language.synopticMenu;
		Composite composite = SynopticMenu.this.frontalWebApp.getPanelFromId(synopticId);
		if (composite != null) {
			SynopticMenu.this.frontalWebApp.updateMainPanel(composite);
		}	
		else {
			String svgPath = GWT.getHostPageBaseURL()+"images/synoptic/syncro-rdt_small.svg";
			String htmlObjectId = "syncroRdt";
			rdtSynopticPanel = new SynopticPanelImplementation(SynopticMenu.this.frontalWebApp, svgPath, htmlObjectId);
			svgPath = GWT.getHostPageBaseURL()+"images/synoptic/syncro-tp_small.svg";
			htmlObjectId = "syncroTp";
			tpSynopticPanel = new SynopticPanelImplementation(SynopticMenu.this.frontalWebApp, svgPath, htmlObjectId);
			svgPath = GWT.getHostPageBaseURL()+"images/synoptic/syncro-weather_small.svg";
			htmlObjectId = "syncroWeather";
			weatherSynopticPanel = new SynopticPanelImplementation(SynopticMenu.this.frontalWebApp, svgPath, htmlObjectId);
			svgPath = GWT.getHostPageBaseURL()+"images/synoptic/servers.svg";
			htmlObjectId = "servers";
			serversSynopticPanel = new ServersSynopticPanel(svgPath, htmlObjectId);
			EmptyComposite countDisabledPanel = new EmptyComposite();
			EmptyComposite travelTimeDisabledPanel = new EmptyComposite();
			EmptyComposite weatherDisabledPanel = new EmptyComposite();
			EmptyComposite serverDisabledPanel = new EmptyComposite();
			TabbedPanelGeneric tabbedPanel = new TabbedPanelGeneric();
			tabbedPanel.addTabPanel(countDisabledPanel, Language.countDisabledSynoptic);
			tabbedPanel.addTabPanel(rdtSynopticPanel, Language.countSynoptic);
			tabbedPanel.addTabPanel(travelTimeDisabledPanel, Language.travelTimeDisabledSynoptic);
			tabbedPanel.addTabPanel(tpSynopticPanel, Language.travelTimeSynoptic);
			tabbedPanel.addTabPanel(weatherDisabledPanel, Language.weatherDisabledSynoptic);
			tabbedPanel.addTabPanel(weatherSynopticPanel, Language.weatherSynoptic);
			tabbedPanel.addTabPanel(serverDisabledPanel, Language.equipmentsDisabledSynoptic);
			tabbedPanel.addTabPanel(serversSynopticPanel, Language.alarmsSynoptic);
			tabbedPanel.getTabPanel().getTabBar().setTabEnabled(0, false);
			tabbedPanel.getTabPanel().getTabBar().setTabEnabled(2, false);
			tabbedPanel.getTabPanel().getTabBar().setTabEnabled(4, false);
			tabbedPanel.getTabPanel().getTabBar().setTabEnabled(6, false);
			tabbedPanel.getTabPanel().selectTab(1);
			SynopticMenu.this.frontalWebApp.addPanel(synopticId, tabbedPanel);
			SynopticMenu.this.frontalWebApp.updateMainPanel(tabbedPanel);
		}	
	}
}	