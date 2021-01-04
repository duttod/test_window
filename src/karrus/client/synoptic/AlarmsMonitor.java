package karrus.client.synoptic;

import karrus.client.FrontalWebApp;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class AlarmsMonitor extends Composite {

	public AlarmsMonitor() {
		IFrameElement element = Document.get().createIFrameElement();
		element.setSrc("http://karrus-its.com:11090/cgi-bin/nagios3/status.cgi?hostgroup=all&style=grid");
		element.setPropertyString("width", FrontalWebApp.getWidthForMainPanel() - 10 + "px");
		element.setPropertyString("height", FrontalWebApp.getHeightForMainPanel() - 10 + "px");
		// Attach the object element to the document before wrapping it in an HTML panel
		Document.get().getBody().appendChild(element);
		Widget w = HTMLPanel.wrap(element);
		this.initWidget(w);
	}
	
}
