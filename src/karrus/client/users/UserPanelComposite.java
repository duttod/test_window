package karrus.client.users;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UserPanelComposite extends Composite {

	private VerticalPanel verticalPanel;
	
	public UserPanelComposite() {
		verticalPanel = new VerticalPanel();
		// Force the height of the vertical panel to be minimum
		verticalPanel.setHeight("1px");
//		verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		this.initWidget(verticalPanel);
	}
	
	public void addComponent(Composite composant) {
		this.verticalPanel.add(composant);
	}
	
}
