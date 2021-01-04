package karrus.client.generic.plot;

import karrus.client.FrontalWebApp;
import karrus.shared.language.Language;

public class PlotMinMax {
	
	private static double individualValidTravelTimeMinValue;
	private static double individualValidTravelTimeMaxValue;
	private static double individualNotValidTravelTimeMinValue;
	private static double individualNotValidTravelTimeMaxValue;
	
	public static double getMinFromColumnName(String columnName) throws Exception{
		if (columnName.equals(Language.speedColumn))
			return FrontalWebApp.ihmParameters.getMinForSpeedPlot();
		if (columnName.equals(Language.countColumn))
			return FrontalWebApp.ihmParameters.getMinForCountPlot();
		if (columnName.equals(Language.occupancyColumn))
			return FrontalWebApp.ihmParameters.getMinForOccupancyPlot();
		if (columnName.equals(Language.travelTimeColumn))
			return FrontalWebApp.ihmParameters.getMinForTravelTimePlot();
		if (columnName.equals(Language.individualValidTravelTimeColumn)) {
			return individualValidTravelTimeMinValue;
		}	
		if (columnName.equals(Language.individualNotValidTravelTimeColumn))
			return individualNotValidTravelTimeMinValue;
		if (columnName.equals(Language.airTemperature)) {
			return -10;
		}
		if (columnName.equals(Language.dewTemperature)) {
			return -10;
		}
		if (columnName.equals(Language.roadTemperature)) {
			return -10;
		}
		if (columnName.equals(Language.belowSurfTemperature)) {
			return -10;
		}
		if (columnName.equals(Language.freezingTemperature)) {
			return -10;
		}
		throw new Exception(Language.columnUnknownError(columnName));
	}
	
	public static double getMaxFromColumnName(String columnName){
		if (columnName.equals(Language.speedColumn))
			return FrontalWebApp.ihmParameters.getMaxForSpeedPlot();
		if (columnName.equals(Language.countColumn))
			return FrontalWebApp.ihmParameters.getMaxForCountPlot();
		if (columnName.equals(Language.occupancyColumn))
			return FrontalWebApp.ihmParameters.getMaxForOccupancyPlot();
		if (columnName.equals(Language.travelTimeColumn))
			return FrontalWebApp.ihmParameters.getMaxForTravelTimePlot();
		if (columnName.equals(Language.individualValidTravelTimeColumn))
			return individualValidTravelTimeMaxValue;
		if (columnName.equals(Language.individualNotValidTravelTimeColumn))
			return individualNotValidTravelTimeMaxValue;
		if (columnName.equals(Language.airTemperature)) {
			return 60;
		}
		if (columnName.equals(Language.dewTemperature)) {
			return 60;
		}
		if (columnName.equals(Language.roadTemperature)) {
			return 60;
		}
		if (columnName.equals(Language.belowSurfTemperature)) {
			return 60;
		}
		if (columnName.equals(Language.freezingTemperature)) {
			return 60;
		}
		return Double.NaN;
	}
	
	public static void setIndividualValidTravelTimeMinValue(double value) {
		PlotMinMax.individualValidTravelTimeMinValue = value;
	}
	
	public static void setIndividualValidTravelTimeMaxValue(double value) {
		PlotMinMax.individualValidTravelTimeMaxValue = value;
	}
	
	public static void setIndividualNotValidTravelTimeMinValue(double value) {
		PlotMinMax.individualNotValidTravelTimeMinValue = value;
	}
	
	public static void setIndividualNotValidTravelTimeMaxValue(double value) {
		PlotMinMax.individualNotValidTravelTimeMaxValue = value;
	}
}
