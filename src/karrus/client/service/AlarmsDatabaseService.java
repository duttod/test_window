package karrus.client.service;

import java.util.Date;
import java.util.List;

import karrus.shared.hibernate.AlAlarmClosed;
import karrus.shared.hibernate.AlAlarmOpened;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("AlarmsDatabaseService")
public interface AlarmsDatabaseService extends RemoteService{
	
	List<AlAlarmClosed> getAlarmsHistoryFiltered(Date initDate, Date finalDate, String Source, String Type, String State) throws Exception;
	List<AlAlarmOpened> getCurrentAlarmsFiltered(Date initDate, Date finalDate, String Source, String Type, String State) throws Exception;
	void closeAlarms(List<AlAlarmOpened> selectedAlarms) throws Exception;
	void acknowledgeAlarms(List<AlAlarmOpened> selectedAlarms) throws Exception;
	void openAlarms(List<AlAlarmClosed> selectedAlarms) throws Exception;
	List<AlAlarmClosed> getAlarmSources() throws Exception;
	List<AlAlarmClosed> getClosedAlarms() throws Exception;
}
