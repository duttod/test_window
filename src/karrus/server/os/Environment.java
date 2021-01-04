package karrus.server.os;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import karrus.server.core.Core;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Environment {

	private static Logger logger = Logger.getLogger(Environment.class);
	
	public static String getHomeDirectory() throws Exception {
		String homePath = System.getenv("KARRUS_HOME");
		if (homePath!=null) {		
			homePath = homePath + "/";
			homePath = homePath.replace("\\", "/");
			homePath = homePath.replace("//", "/");
			homePath = homePath.replace("/\\", "/");
			return homePath;
		}
		logger.error("The environnement variable KARRUS_HOME should be defined.");
		throw new Exception("Environnement variable KARRUS_HOME not defined.");
	}

	private static String getLogDirectory() throws Exception {
		return getHomeDirectory() + Core.projectName+"/log/";
	}

	public static String getPropertiesDirectory() throws Exception {
		return getHomeDirectory() + Core.projectName+"/properties/";
	}
	
	public static String getTempDirectory() throws Exception {
		return getHomeDirectory() + Core.projectName + "/temp/";
	}
	
	public static String getScriptsDirectory() throws Exception {
		return getHomeDirectory() + Core.projectName + "/scripts/";
	}
	
	private static String getLog4jTemplatePath() throws Exception {
		return getPropertiesDirectory() + "log4j.template.properties";
	}
	
	private static String getLog4jPath() throws Exception {
		return getPropertiesDirectory() + "log4j.properties";
	}
	
	public static String getReferentialFile() throws Exception {
		return getPropertiesDirectory()+"frt-rdt-l2.ref";
	}
	
	public static String getIhmPropertiesFile() throws Exception {
		return getPropertiesDirectory()+"gui.properties";
	}

	/**
	 * Load and update the log4j.temp.properties file and set log4j accordingly.
	 * @throws Exception
	 */
	public static void log4jInit() throws Exception {

		try {
			// read template file
			BufferedReader reader = new BufferedReader(new FileReader(getLog4jTemplatePath()));
			String line  = null;
			StringBuilder stringBuilder = new StringBuilder();
			String line_separator = System.getProperty("line.separator");
			while((line = reader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append(line_separator);
			}
			reader.close();
			// update template keywords
			String fileContent = stringBuilder.toString().replace("<log>/", getLogDirectory());
			// write the updated property file
			FileWriter fileWriter = new FileWriter(getLog4jPath());
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(fileContent);
			bufferedWriter.close();
		} catch (FileNotFoundException e) {
			logger.error("Template log4j property file not found.");
		} catch (IOException e) {
			logger.error("IOException while updating log4j. Exception message: " + e.getMessage());
		}
		// init log4j with the updated properties file
		PropertyConfigurator.configure(getLog4jPath());
	}
}
