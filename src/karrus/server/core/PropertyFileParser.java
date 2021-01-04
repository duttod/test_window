package karrus.server.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Parses a file and gets its properties.
 * 
 * @author Estelle DUMAS : estelle.dumas@karrus-its.com
 */
public class PropertyFileParser {
	
	Properties properties;
	private String fileName;
	
	/**
	 * Parses the file.
	 * @param fileName String
	 * @throws IOException
	 */
	public PropertyFileParser(String fileName) throws IOException {
		this.fileName = fileName;
		properties = new Properties();
		FileInputStream input;
		input = new FileInputStream(fileName);
		properties.load(input);
	}
	
	/**
	 * Gets the property from this file.
	 * @param property
	 * @return String
	 * @throws Exception 
	 */
	public String getProperty(String property) throws Exception {
		String propertyAsString = properties.getProperty(property);
		if (propertyAsString==null) {
			throw new Exception("Property \'"+property+"\' is not defined in the file \'"+fileName+"\'.");
		}	
		return propertyAsString;
	}
	
	/**
	 * Search a property value in the ihm.property file, and transforms it in an integer. 
	 * @param property String
	 * @return int
	 * @throws Exception
	 */
	public Integer getPropertyAsInteger(String property) throws Exception{
		String propertyAsString = this.getProperty(property);
		if (propertyAsString==null) {
			throw new Exception("Property \'"+property+"\' is not defined in the file \'"+fileName+"\'.");
		}	
		return Integer.parseInt(propertyAsString);
	}

	/**
	 * Search a property value in the ihm.property file, and transforms it in a double. 
	 * @param property String
	 * @return double
	 * @throws Exception
	 */
	public Double getPropertyAsDouble(String property) throws Exception{
		String propertyAsString = this.getProperty(property);
		if (propertyAsString==null) {
			throw new Exception("Property \'"+property+"\' is not defined in the file \'"+fileName+"\'.");
		}	
		return Double.parseDouble(propertyAsString);
	}

	/**
	 * Search a property value in the ihm.property file, and transforms it in an boolean. 
	 * @param property String
	 * @return boolean
	 * @throws Exception
	 */
	public boolean getPropertyAsBoolean(String property) throws Exception{     
		String propertyAsString = this.getProperty(property);
		if (propertyAsString==null) {
			throw new Exception("Property \'"+property+"\' is not defined in the file \'"+fileName+"\'.");
		}	
		if (propertyAsString.equals("true")) {
			return true;
		}	
		if (propertyAsString.equals("false")) {
			return false;
		}	
		else { 
			throw new Exception("The property '"+property+"' is not a boolean."); 
		}	
	}
}
