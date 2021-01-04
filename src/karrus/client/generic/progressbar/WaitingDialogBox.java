package karrus.client.generic.progressbar;

import karrus.client.appearance.Css;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class WaitingDialogBox extends DialogBox {
	
	private Timer timer;
	private Label label;
	private String waitingMessage = "Requête en cours";
	
	public WaitingDialogBox(){
		super(false, true);
		setStyleName(Css.dialogBoxStyle);
		VerticalPanel panel = new VerticalPanel();
		panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		panel.setSpacing(3);
		label = new Label(waitingMessage);
		timer = new Timer() {
			private String wait = "..";
			@Override
			public void run() {
				if (wait.length()==3)
					wait = ".";
				else
					wait += ".";
				label.setText(waitingMessage + wait);
			}
		};
		timer.run();
		timer.scheduleRepeating(1000);
		panel.add(label);
		add(panel);
		center();
		setWidth("150px");
		show();
	}
	
	public void terminate(){
		timer.cancel();
		hide();
	}
}
