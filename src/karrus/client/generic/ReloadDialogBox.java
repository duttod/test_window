package karrus.client.generic;

import karrus.client.FrontalWebApp;
import karrus.client.appearance.Css;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ReloadDialogBox extends DialogBox {
	
	private Timer timer;
	private Label label;
	
	public ReloadDialogBox(){
		super(false, true);
		Log.debug("new reloadDialogBox opened");
		setStyleName(Css.dialogBoxStyle);
		VerticalPanel panel = new VerticalPanel();
		panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		panel.setSpacing(3);
		label = new Label("Vous allez basculer sur le serveur secondaire dans 10 secondes");
		timer = new Timer() {
			@Override
			public void run() {
				 Window.Location.replace("http://" + FrontalWebApp.secondaryServerIp + ":8080/frt-rdt");
			}
		};
		timer.schedule(10000);
		panel.add(label);
		add(panel);
		setWidth("350px");
		setHeight("200px");
		center();
		show();
	}
	
	public void terminate(){
		timer.cancel();
		hide();
	}
}
