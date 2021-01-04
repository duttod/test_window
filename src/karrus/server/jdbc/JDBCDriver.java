package karrus.server.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Map;

public class JDBCDriver {

	static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	static public void updateStation(String start, String stop, Map<String, String> lanesMap) {
		
		// load JDBC driver
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("ERROR: no PostgreSQL JDBC driver.");
			e.printStackTrace();
			return;
		} 

		// connect
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:postgresql://192.168.0.205:5432/l2-frt-rdt", "karrus", "karruspwd");
		} catch (SQLException e) {
			System.out.println("ERROR: connection to database failed.");
			e.printStackTrace();
			return;
		}
		
		if (connection != null) {
			try {
				Statement statement = connection.createStatement();
				String selectQuery = "" +
				"SELECT date_trunc('hour', \"timestamp\") + interval '1 hour' as \"Horodate\", " + 
				"avg(\"occupancy\") as \"TT\", " +
				"sum(\"count\") as \"QT\", "+ 
				"avg(nullif(\"speed_median\", -1)) as \"VT\" " + 
				"FROM \"ct_ssy_count_lane\" " +
				"WHERE \"timestamp\">'"+start+"' " + "AND \"timestamp\"<'"+stop+"' AND (";
				for (int i =0; i < lanesMap.size(); i++) {
					if (i != lanesMap.size() - 1) {
						selectQuery += "\"lane\" = '" + lanesMap.keySet().toArray()[i] + "' OR ";
					}
					else {
						selectQuery += "\"lane\" = '" + lanesMap.keySet().toArray()[i] + "' ) ";
					}
				}
				selectQuery += "GROUP BY date_trunc('hour', \"timestamp\") " +
				"ORDER BY date_trunc('hour', \"timestamp\");";
				System.out.println(selectQuery);
				ResultSet resultSet = statement.executeQuery(selectQuery);
				resultSet.close();
				statement.close();
			} catch (SQLException e) {
				System.out.println("ERROR: SQL select query failed.");
				e.printStackTrace();
			} 
		} else {
			System.out.println("ERROR: connection to database failed.");
		}

		// disconnect
		try {
			connection.close();
		} catch (SQLException e) {
			System.out.println("ERROR: disconnection from database failed.");
			e.printStackTrace();
		}
	return;
	}

}
