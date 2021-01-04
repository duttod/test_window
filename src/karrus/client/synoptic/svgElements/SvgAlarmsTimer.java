package karrus.client.synoptic.svgElements;

import java.util.List;

import karrus.client.FrontalWebApp;
import karrus.client.generic.CustomDialogBox;
import karrus.shared.hibernate.AlAlarmOpened;
import karrus.shared.language.Language;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SvgAlarmsTimer extends Timer {

	List<SvgAlarm> svgAlarms;
//	private DateTimeFormat dateFormatter = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss");
	
	public SvgAlarmsTimer(List<SvgAlarm> svgAlarms) {
		this.svgAlarms = svgAlarms;
	}
	
	@Override
	public void run() {
		
		FrontalWebApp.synopticDatabaseService.getOpenedAlarms(new AsyncCallback<List<AlAlarmOpened>>() {
			@Override
			public void onSuccess(List<AlAlarmOpened> result) {
				Log.info("synopticDatabaseService.getOpenedAlarms succeded");
				for (SvgAlarm svgAlarm : svgAlarms) {
					svgAlarm.getOpenedAlarmsList().clear();
					if (svgAlarm.getType().equals("")) {
						for (AlAlarmOpened openedAlarm : result) {
							if (openedAlarm.getId().getSource().equals(svgAlarm.getSource())) {
								svgAlarm.setColor("red");
								svgAlarm.getOpenedAlarmsList().add(openedAlarm);
							}
						}	
						if (svgAlarm.getOpenedAlarmsList().size() == 0) {
							svgAlarm.setColor("rgb(200, 113, 55)");
						}
					} 
					else {
						for (AlAlarmOpened openedAlarm : result) {
							if ((openedAlarm.getId().getSource().equals(svgAlarm.getSource()) && openedAlarm.getId().getType().equals(svgAlarm.getType())) || (openedAlarm.getId().getSource().equals(svgAlarm.getSource()) && openedAlarm.getId().getType().contains("RESEAU"))) {
								svgAlarm.setColor("red");
								svgAlarm.getOpenedAlarmsList().add(openedAlarm);
							}
						}	
						if (svgAlarm.getOpenedAlarmsList().size() == 0) {
							svgAlarm.setColor("green");
						}
					}
				}
			}
			@Override
			public void onFailure(Throwable caught) {
				Log.error("synopticDatabaseService.getOpenedAlarms failed. Message : " + caught.getMessage());
				new CustomDialogBox("synopticDatabaseService.getOpenedAlarms failed. Message : " + caught.getMessage(), Language.okString);
			}
		});
	}	
}
