package karrus.client.utils;

import karrus.shared.language.Language;

public class Color {
	
	// colors
	public static String colorBlue = "blue";
	public static String colorGrey = "grey";
	public static String colorRed = "red";
	public static String colorOrange = "orange";
	public static String colorDarkOrange = "#FF8C00";
	public static String colorOrangeRed = "#FF4500";
	public static String colorGreen = "green";
	public static String colorYellow = "yellow";
	private static String colorFuschia = "#FF00FF"; //fuschia
	public static String colorLime = "#00FF00";
	public static String colorTomato = "#FF6347";
	public static String colorSampleSizeValid = "blue";
	public static String colorSampleSizeAll = "#FFEEEE";
	public static String colorGreyLight = "#DCDCDC";
	public static String colorRedLight = "#FF888B";
	public static String colorBlack = "black";
	public static String colorSalmon = "#FA8072";
	public static String colorTurquoise = "#40E0D0";
	public static String  colorMagenta = "#d500ff";

	/**
	 * Gets the url as a string of the color.
	 * @param color String
	 * @return String
	 */
	public static String getColorUrl(String color){
		if (color.equals(colorRed)) {
			return Images.redColor;
		}	
		if (color.equals(colorOrange)) {
			return Images.orangeColor;
		}	
		if (color.equals(colorGreen)) {
 			return Images.greenColor;
		}	
		if (color.equals(colorGreyLight)) {
			return Images.greyLightColor;
		}	
		return null;
	}
	
	public static String getColorFromColumn(String column){
		if (column.equals(Language.speedColumn))
			return colorBlue;
		if (column.equals(Language.countColumn))
			return colorFuschia; 
		if (column.equals(Language.occupancyColumn))
			return colorLime; 
		if (column.equals(Language.lengthColumn))
			return colorYellow;
		if (column.equals(Language.gapColumn))
			return colorRed;
		if (column.equals(Language.travelTimeColumn))
			return colorRed;
		if (column.equals(Language.individualValidTravelTimeColumn))
			return colorGreen;
		if (column.equals(Language.individualNotValidTravelTimeColumn))
			return colorRed;
		if (column.equals(Language.airTemperature)) {
			return colorGreen;
		}
		if (column.equals(Language.dewTemperature)) {
			return colorYellow;
		}
		if (column.equals(Language.roadTemperature)) {
			return colorTurquoise;
		}
		if (column.equals(Language.belowSurfTemperature)) {
			return colorMagenta;
		}
		if (column.equals(Language.freezingTemperature)) {
			return colorGrey;
		}
		return null;
	}
}
