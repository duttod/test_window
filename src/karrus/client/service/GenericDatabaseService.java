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

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("GenericDatabaseService")
public interface GenericDatabaseService extends RemoteService{
	
	void setProjectName(String projectName) throws Exception;
	DatabaseObjects getDatabaseObjects() throws Exception;
	CountDataAndLanesMap getCountDataForStationAndWay(String stationName, String way, Date startDate, Date finalDate) throws Exception;
	List<String> getWaysForStation(String stationName) throws Exception;
	List<CtCountData> getCountDataForStationAndLane(String stationName, String lane, Date startDate, Date finalDate) throws Exception;
	List<RsuDiag> getRsuDiagnosticForStation(String stationName, Date startDate, Date finalDate) throws Exception;
	List<V2xAlarm> getV2xAlarms(String stationName, Date startDate, Date finalDate) throws Exception;
    UsrUser checkUserAndPassword(String login, String password) throws Exception;
    UsrUser updateUser(UsrUser formerUser, String newLogin, String newEmail, String newCredential) throws Exception;
    UsrUser addNewUser(String login, String password, String email, String credential) throws Exception;
	void removeUser(String login) throws Exception;
	UsrUser updatePassword(UsrUser user, String newPwd) throws Exception;
	List <UsrUser> getUsers() throws Exception;
	List<UsrCredential> getCredentials() throws Exception;
	void removeCredential(UsrCredential credential) throws Exception;
	UsrCredential updateCredential(UsrCredential oldCredential, UsrCredential credential)  throws Exception;
	UsrCredential addNewCredential(UsrCredential credential)  throws Exception;
	List<RsuStation> getAllStations() throws Exception;
	List<String> getStationsIdsForAlarmsFilter() throws Exception;
	List<CtLane> getAllLanes() throws Exception;
	// Travel Times
	List<TtBtItt> getTtBtItt(String itineraryString, Date initDate, Date finalDate) throws Exception;
	List<TtBtStat> getTtBtStat(String itineraryString, Date initDate, Date finalDate) throws Exception;
	TravelTimePlot getTravelTimePlot(String itinerary, Date initDate, Date finalDate) throws Exception;
	// Weather data
	List<WthMeteoData> getWeatherDataForStation(String stationName,Date startDate, Date finalDate) throws Exception;
	// Referential
	List<String> getReferentialFiles() throws Exception;
	String getCurrentReferentialFile() throws Exception;
	void deleteReferential(String referentialFileName) throws Exception;
	void executeReferential(String referentialFileName) throws Exception;
	void saveCurrentReferential() throws Exception;
	// Environment
	SysEnv getEnvironmentVariable(String var) throws Exception;
	void setEnvironmentVariableContent(SysEnv sysEnv, String content) throws Exception;
}