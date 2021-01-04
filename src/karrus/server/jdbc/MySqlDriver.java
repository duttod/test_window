package karrus.server.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;	
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import karrus.server.database.GenericDatabaseDriver;
import karrus.shared.hibernate.RsuStation;
import karrus.shared.hibernate.SysEnv;
import karrus.shared.language.Language;

import org.apache.log4j.Logger;

public class MySqlDriver {

	private static Logger logger = Logger.getLogger(MySqlDriver.class);
	
	private static String host = "";
	private static String username = "";
	private static String password = "";
	
	static public void checkAlarms() throws Exception {
				
		// load JDBC driver
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			logger.error("ERROR: no MySql JDBC driver.\n" + e.getMessage());
		} 
		
		// connect
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(host, username, password);
		} catch (SQLException e) {
			logger.error("ERROR: connection to database failed.\nMessage : " + e.getMessage());
		}
		
		List<String> stationsIds = new ArrayList<String>();
		for (RsuStation rsuStation : GenericDatabaseDriver.getAllStations()) {
			String stationId = rsuStation.getId().toUpperCase().replace(" ", "-");
			stationId	 = Normalizer.normalize(stationId, Normalizer.Form.NFD);
			stationId = stationId.replaceAll("[^\\p{ASCII}]", "");
			stationsIds.add(stationId);
		}
		SysEnv alarmTypesSysEnv = GenericDatabaseDriver.getEnvironmentVariable(Language.alarmTypes);
		List<String> stationServices = new ArrayList<String>();
		List<String> middlewareServices = new ArrayList<String>();
		String regex = "^MIDDLEWARE.*";
		Pattern pattern = Pattern.compile(regex);
		for (String alarmType : alarmTypesSysEnv.getContent().split(",")) {
			Matcher matcher = pattern.matcher(alarmType);
			if (matcher.matches()) {
				middlewareServices.add(alarmType);
			}
			else {
				stationServices.add(alarmType);
			}
		}
		List<String> hosts = new ArrayList<String>();
		List<String> services = new ArrayList<String>();
		for (String middlewareService : middlewareServices) {
			hosts.add(Language.middleware.toUpperCase());
			services.add(middlewareService.split("/")[1]);
		}
		for (String stationId : stationsIds) {
			for (String stationService : stationServices) {
				hosts.add(stationId);
				services.add(stationService.split("/")[1]);
			}
		}
		
		if (connection != null) {
			try {
				Date timestamp;
				String type;
				String source;
				Statement statement = connection.createStatement();
				for (int i = 0; i < hosts.size(); i++) {
					String query = "SELECT * FROM karrus_servicestatus WHERE host = '" + hosts.get(i) + "' AND service = '" + services.get(i) + "' ORDER BY host, service;";
//					logger.info(query);
					ResultSet resultSet = statement.executeQuery(query);
					while (resultSet.next()) {
						timestamp = new Date();
						source = resultSet.getString("host");
						type = resultSet.getString("service");
						if (source.equals("MIDDLEWARE")) {
							type = "MIDDLEWARE/" + type;
						}
						else {
							type = "UBR/" + type;
						}
						if (resultSet.getString("state").equals("0") || resultSet.getString("state").equals("1")) {
							GenericDatabaseDriver.closeAlarm(timestamp, type, source);
						}
						else {
							GenericDatabaseDriver.openAlarm(timestamp, type, source);
						}
					}
					resultSet.close();
				}
                statement.close(); 
			}
			catch (SQLException e) {
				logger.error("ERROR: SQL select query failed.\nMessage : " + e.getMessage());
			}
		}
	}
	
	public static void setDatabaseCredentials(String newHost, String newUsername, String newPassword) {
		host = newHost;
		username = newUsername;
		password = newPassword;
	}
}
