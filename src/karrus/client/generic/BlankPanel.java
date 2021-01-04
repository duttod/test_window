package karrus.client.generic;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class BlankPanel extends Composite {

	
	public BlankPanel(int width, int height){	
	
		HorizontalPanel panel = new HorizontalPanel();
		panel.setSize(width+"px", height+"px");
		this.initWidget(panel);
		
	}
	
	
	
}

