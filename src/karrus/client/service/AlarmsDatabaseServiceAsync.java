package karrus.client.service;

import java.util.Date;
import java.util.List;

import karrus.shared.hibernate.AlAlarmClosed;
import karrus.shared.hibernate.AlAlarmOpened;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AlarmsDatabaseServiceAsync {
	void getAlarmsHistoryFiltered(Date initDate, Date finalDate, String source, String type, String state, AsyncCallback<List<AlAlarmClosed>> callback);
	void getCurrentAlarmsFiltered(Date initDate, Date finalDate, String source, String type, String state, AsyncCallback<List<AlAlarmOpened>> callback);
	void closeAlarms(List<AlAlarmOpened> selectedAlarms, AsyncCallback<Void> callback);
	void acknowledgeAlarms(List<AlAlarmOpened> selectedAlarms, AsyncCallback<Void> callback);
	void openAlarms(List<AlAlarmClosed> selectedAlarms, AsyncCallback<Void> callback);
	void getAlarmSources(AsyncCallback<List<AlAlarmClosed>> callback);
	void getClosedAlarms(AsyncCallback<List<AlAlarmClosed>> callback);
}
