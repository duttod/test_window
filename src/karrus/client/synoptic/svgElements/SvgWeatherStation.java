package karrus.client.synoptic.svgElements;

import karrus.client.generic.ClosableDialogBox;
import karrus.client.synoptic.modal.weather.WeatherDataDashboard;
import karrus.shared.hibernate.RsuStation;

import com.allen_sauer.gwt.log.client.Log;

public class SvgWeatherStation extends SvgElement {

	RsuStation rsuStation;
	private int width = 900;
	private int height = 400;
			
	public SvgWeatherStation(RsuStation rsuStation, String svgId, String htmlObjectId) {
		super(svgId, htmlObjectId, true);
		this.rsuStation = rsuStation;
	}
	
	@Override
	public void onMouseDown() {
		try {
			WeatherDataDashboard weatherDashboard = new WeatherDataDashboard(rsuStation.getId(), width, height);
			Log.debug("weather station clicked");
			ClosableDialogBox closableDialogBox = new ClosableDialogBox(rsuStation.getId(), width, false, false, weatherDashboard);
			closableDialogBox.add(weatherDashboard);
			closableDialogBox.center();
			closableDialogBox.show();
		} catch (Exception e) {
			Log.error(e.getMessage());	
		}
	};
	
	public RsuStation getRsuStation() {
		return rsuStation;
	}
}
