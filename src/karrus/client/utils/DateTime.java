package karrus.client.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import karrus.client.FrontalWebApp;
import karrus.shared.language.Language;
import com.google.gwt.i18n.client.DateTimeFormat;

public class DateTime {

	private static List<String> days;
	public static DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy-MM-dd");
	public static DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss");
	public static DateTimeFormat frenchDateFormat = DateTimeFormat.getFormat("dd-MM-yyyy");
	public static DateTimeFormat frenchBarDateTimeFormat = DateTimeFormat.getFormat("dd/MM/yyyy HH:mm:ss");
	public static DateTimeFormat frenchBarDateFormat = DateTimeFormat.getFormat("dd/MM/yyyy");
	public static DateTimeFormat frenchDateTimeFormat = DateTimeFormat.getFormat("dd-MM-yyyy HH:mm:ss");
	public static DateTimeFormat timeFormat = DateTimeFormat.getFormat("HH:mm:ss");
	public static DateTimeFormat timeFormatHours = DateTimeFormat.getFormat("HH");
	public static DateTimeFormat timeFormatMinutes = DateTimeFormat.getFormat("mm");
	public static DateTimeFormat timeFormatSeconds = DateTimeFormat.getFormat("ss");
	
	public static List<String> getDays(){
		days = new ArrayList<String>();
		days.add("lundi");
		days.add("mardi");
		days.add("mercredi");
		days.add("jeudi");
		days.add("vendredi");
		days.add("samedi");
		days.add("dimanche");
		return days;
	}
   
	public static void checkMaxSizeRequest(Date initialDate, Date finalDate) throws Exception{
		int maxHour = FrontalWebApp.ihmParameters.getPlotMaxTimeWindow();
		if (finalDate.after(new Date(initialDate.getTime()+maxHour*60l*60l*1000l))) {
			throw new Exception(Language.maxRequestSize(maxHour));
		}	
	}

	/**
	 * Gets the hours of the day in a string list.
	 * @return 
	 */
	public static List<String> getHoursOfDay(){
		List<String> hours = new ArrayList<String>();
		for (int i=0; i<24; i++) {
			if (i<10) hours.add("0"+i);
			else  hours.add(""+i);
		}
		return hours;
	}

	/**
	 * Gets the minutes of the day in a string list.
	 * @return 
	 */
	public static List<String> getMinutesOfDay(){
		List<String> minutes = new ArrayList<String>();
		for (int i=0; i<60; i++) {
			if (i<10) minutes.add("0"+i);
			else  minutes.add(""+i);
		}
		return minutes;
	}

	/**
	 * Gets the seconds of the day in a string list.
	 * @return 
	 */
	public static List<String> getSecondsOfDay(){
		List<String> seconds = new ArrayList<String>();
		for (int i=0; i<60; i++) {
			if (i<10) seconds.add("0"+i);
			else  seconds.add(""+i);
		}
		return seconds;
	}

	/**
	 * Checks if the time is correct with the format hh:mm:ss
	 * @param timeAsString
	 * @return
	 * @throws Exception
	 */
	public static boolean isTimeCorrect(String timeAsString) throws Exception{
		try {
			String[] split = timeAsString.split(":");
			if (split.length!=3) {
				throw new Exception(Language.dateTimeFieldMissingError);
			}	
			for (int i=0; i<split.length; i++){
				if (split[i].trim().equals("")) {
					throw new Exception(Language.dateTimeFieldMissingError);
				}	
				Integer nb = new Integer(split[i]).intValue();
				switch (i) {
				case 0:
					if (nb<0 || nb>23) {
						throw new Exception(Language.hourNotValidError);
					}	
					break;
				case 1:
					if (nb<0 || nb>60) {
						throw new Exception(Language.minuteNotValidError);
					}	
					break;
				case 2:
					if (nb<0 || nb>60) {
						throw new Exception(Language.secondNotValidError); 
					}	
					break;
				default:
					break;
				}
			}
			return true;
		} catch (NumberFormatException e){
			throw new Exception (Language.dateTimeFormatIncorrectError);
		}
	}

	public static boolean areDatesAtTheSameDay(Date startDate,Date endDate){
		String startDateYear = DateTimeFormat.getFormat("yyyy").format(startDate);
		String endDateYear = DateTimeFormat.getFormat("yyyy").format(endDate);
		if (!startDateYear.equals(endDateYear)) {
			return false;
		}	
		String startDateMonth = DateTimeFormat.getFormat("MM").format(startDate);
		String endDateMonth = DateTimeFormat.getFormat("MM").format(endDate);
		if (!startDateMonth.equals(endDateMonth)) { 
			return false;
		}	
		String startDateDay = DateTimeFormat.getFormat("dd").format(startDate);
		String endDateDay = DateTimeFormat.getFormat("dd").format(endDate);
		if (startDateDay.equals(endDateDay)) {
			return true;
		}	
		return false;
	}
}
