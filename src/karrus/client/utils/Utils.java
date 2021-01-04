package karrus.client.utils;

import karrus.shared.language.Language;

import com.allen_sauer.gwt.log.client.Log;

public class Utils {

	public static String addLeadingWhitespaces(String stringToFormat, int outputSize) {
		if (stringToFormat.length() > outputSize) {
			Log.error(Language.sizeError);
			return stringToFormat;
		}
		else {
			String output = stringToFormat;
		    for (int i = 0; i < outputSize - stringToFormat.length(); i++) {
		    	output = " " + output;
			}    	
		    return output;
		}
	}
	
	public static String addLeadingZeros(String stringToFormat, int outputSize) {
		if (stringToFormat.length() > outputSize) {
			Log.error(Language.sizeError);
			return stringToFormat;
		}
		else {
			String output = stringToFormat;
		    for (int i = 0; i < outputSize - stringToFormat.length(); i++) {
		    	output = "0" + output;
			}    	
		    return output;
		}
	}
}
