package karrus.client.menu;

import karrus.client.FrontalWebApp;
import karrus.client.appearance.Css;
import karrus.client.environment.referential.ReferentialPanel;
import karrus.client.generic.CustomDialogBox;
import karrus.client.generic.tab.TabbedPanelGeneric;
import karrus.shared.language.Language;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;

public class ReferentialMenu extends Label {

	private FrontalWebApp frontalWebApp;

	/**
	 * Constructor.
	 * @param frontalWebApp
	 */

	public ReferentialMenu(FrontalWebApp frontalWebApp){

		this.frontalWebApp = frontalWebApp;
		this.setText(Language.referentialMenu);
		this.setStyleName(Css.subItemclickableLabelStyle);
		this.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					showReferential();
				} catch (Exception e) {
					Log.error(e.getMessage());
					new CustomDialogBox(e.getMessage(), Language.okString);
				}
			}
		});
	}

	public void showReferential() throws Exception {
		final String referentialId = Language.referentialMenu;
		Composite composite = ReferentialMenu.this.frontalWebApp.getPanelFromId(referentialId);
		if (composite != null) {
			ReferentialMenu.this.frontalWebApp.updateMainPanel(composite);
		}	
		else {
			try {
				ReferentialPanel referentialPanel = new ReferentialPanel();
				referentialPanel.setWidth(FrontalWebApp.getWidthForMainPanel()*96/100+"px");
				referentialPanel.setHeight("1px");
				referentialPanel.setStyleName(Css.borderStyle);
				TabbedPanelGeneric tabbedPanel = new TabbedPanelGeneric(referentialPanel, referentialId);
				ReferentialMenu.this.frontalWebApp.addPanel(referentialId, tabbedPanel);
				ReferentialMenu.this.frontalWebApp.updateMainPanel(tabbedPanel);
			} catch (Exception exception) {
				exception.printStackTrace();
				Log.error(exception.getMessage());
				new CustomDialogBox(exception.getMessage(), Language.okString);
			}
		}
	}
}
