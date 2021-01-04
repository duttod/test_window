package karrus.client.service;

import java.util.Date;
import java.util.List;

import karrus.shared.CountDataAndLanesMap;
import karrus.shared.DatabaseObjects;
import karrus.shared.hibernate.CtCountData;
import karrus.shared.hibernate.RsuDiag;
import karrus.shared.hibernate.TtBtItt;
import karrus.shared.hibernate.TtBtStat;
import karrus.shared.hibernate.CtLane;
import karrus.shared.hibernate.RsuStation;
import karrus.shared.hibernate.SysEnv;
import karrus.shared.hibernate.UsrCredential;
import karrus.shared.hibernate.UsrUser;
import karrus.shared.hibernate.V2xAlarm;
import karrus.shared.hibernate.WthMeteoData;
import karrus.shared.plot.TravelTimePlot;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GenericDatabaseServiceAsync {
	
	void setProjectName(String projectName, AsyncCallback<Void> callback);
	void getCountDataForStationAndWay(String stationName, String way, Date startDate, Date finalDate, AsyncCallback<CountDataAndLanesMap> callback);
	void getWaysForStation(String stationName , AsyncCallback<List<String>> callback);
	void getCountDataForStationAndLane(String stationName, String lane, Date startDate, Date finalDate, AsyncCallback<List<CtCountData>> callback);
	void getRsuDiagnosticForStation(String stationName, Date startDate, Date finalDate, AsyncCallback<List<RsuDiag>> callback);
	void getV2xAlarms(String stationName, Date startDate, Date finalDate, AsyncCallback<List<V2xAlarm>> callback);
	void getDatabaseObjects(AsyncCallback<DatabaseObjects> asyncCallback);
	void checkUserAndPassword(String login, String password, AsyncCallback<UsrUser> callback);
	void updateUser(UsrUser formerUser, String newLogin, String newEmail, String newCredential, AsyncCallback<UsrUser> callback);
	void addNewUser(String login, String password, String email, String credential, AsyncCallback<UsrUser> callback);
	void removeUser(String login, AsyncCallback<Void> callback);
	void updatePassword(UsrUser user, String newPwd, AsyncCallback<UsrUser> callback);
	void getUsers(AsyncCallback<List<UsrUser>> callback);
	void getCredentials(AsyncCallback<List<UsrCredential>> callback);
	void removeCredential(UsrCredential credential, AsyncCallback<Void> callback);
	void updateCredential(UsrCredential oldCredential, UsrCredential credential, AsyncCallback<UsrCredential> callback);
	void addNewCredential(UsrCredential credential, AsyncCallback<UsrCredential> callback);
	void getAllStations(AsyncCallback<List<RsuStation>> callback);
	void getStationsIdsForAlarmsFilter(AsyncCallback<List<String>> callback);
	void getAllLanes(AsyncCallback<List<CtLane>> callback);
	void getTtBtItt(String itineraryString, Date initDate, Date finalDate, AsyncCallback<List<TtBtItt>> asyncCallback);
	void getTtBtStat(String itineraryString, Date initDate, Date finalDate, AsyncCallback<List<TtBtStat>> asyncCallback);
	void getTravelTimePlot(String itinerary, Date initDate, Date finalDate, AsyncCallback<TravelTimePlot> callback);
	void getWeatherDataForStation(String stationName,Date startDate, Date finalDate, AsyncCallback<List<WthMeteoData>> callback);
	void getReferentialFiles(AsyncCallback<List<String>> callback);
	void getCurrentReferentialFile(AsyncCallback<String> callback);
	void deleteReferential(String referentialFileName, AsyncCallback<Void> callback);
	void executeReferential(String referentialFileName, AsyncCallback<Void> callback);
	void saveCurrentReferential(AsyncCallback<Void> callback);
	void getEnvironmentVariable(String var, AsyncCallback<SysEnv> callback);
	void setEnvironmentVariableContent(SysEnv sysEnv, String content, AsyncCallback<Void> callback);
}
