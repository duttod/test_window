package karrus.client.menu;

import java.util.List;

import karrus.client.FrontalWebApp;
import karrus.client.appearance.Css;
import karrus.shared.hibernate.AlAlarmOpened;
import karrus.shared.language.Language;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;

public class AlarmsStatusBar extends HTML {
	
	private Timer updateTimer;
	private boolean unAcknowledgedAlarms = false;
	
	public AlarmsStatusBar(FrontalWebApp frontalWebApp) {
		this.setHTML(new SafeHtmlBuilder().appendEscapedLines(Language.unAcknowledgedAlarm).toSafeHtml());
		this.setVisible(false);
		this.setStyleName(Css.redBackgroundStyle);
		updateTimer = new Timer() {
			@Override
			public void run() {
				FrontalWebApp.synopticDatabaseService.getOpenedAlarms(new AsyncCallback<List<AlAlarmOpened>>() {
					@Override
					public void onSuccess(List<AlAlarmOpened> result) {
						unAcknowledgedAlarms = false;
						for (AlAlarmOpened openedAlarm : result) {
							if (!openedAlarm.getStatus().equals(Language.acknowledgedAlarmStatus)) {
								unAcknowledgedAlarms = true;
								break;
							}
						}
						if (unAcknowledgedAlarms && !AlarmsStatusBar.this.isVisible()) {
							AlarmsStatusBar.this.setVisible(true);
						}
						else if (unAcknowledgedAlarms && AlarmsStatusBar.this.isVisible()) {
							AlarmsStatusBar.this.setVisible(false);
						}
						else if (!unAcknowledgedAlarms && AlarmsStatusBar.this.isVisible()) {
							AlarmsStatusBar.this.setVisible(false);
						}
					}
					@Override
					public void onFailure(Throwable caught) {
						Log.debug("synopticDatabaseService.getOpenedAlarms failed : " + caught.getMessage());
					}
				});
			}
		};
		updateTimer.scheduleRepeating(1000);
		updateTimer.run();
	}
}
