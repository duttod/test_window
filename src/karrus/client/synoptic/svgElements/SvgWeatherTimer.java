package karrus.client.synoptic.svgElements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import karrus.client.FrontalWebApp;
import karrus.client.generic.CustomDialogBox;
import karrus.shared.hibernate.WthMeteoData;
import karrus.shared.language.Language;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SvgWeatherTimer extends Timer {

	List<SvgWeatherDisplay> svgWeatherDisplays;
	
	public SvgWeatherTimer(List<SvgWeatherDisplay> svgWeatherDisplays) {
		this.svgWeatherDisplays = svgWeatherDisplays;
	}
	
	@Override
	public void run() {
		List<String> stationsNames = new ArrayList<String>();
		for (SvgWeatherDisplay svgWeatherDisplay : svgWeatherDisplays) {
			stationsNames.add(svgWeatherDisplay.getSynWeatherDisplay().getStation());
		}
		FrontalWebApp.synopticDatabaseService.getLastWeatherDataForStations(stationsNames, new AsyncCallback<Map<String, WthMeteoData>>() {
			@Override
			public void onSuccess(Map<String, WthMeteoData> result) {
				for (SvgWeatherDisplay svgWeatherDisplay : SvgWeatherTimer.this.svgWeatherDisplays) {
					String[] displayedData = svgWeatherDisplay.getSynWeatherDisplay().getContent().split("\\|");
					List<String> values = new ArrayList<String>();
					List<String> units = new ArrayList<String>();
					WthMeteoData currentData = result.get(svgWeatherDisplay.getSynWeatherDisplay().getStation());
					String currentValue = "";
					for (int i = 0; i < displayedData.length; i++) {
						if (displayedData[i].equals("T_air")) {
							values.add(currentData == null ? "" : NumberFormat.getFormat("##.#").format(currentData.getTemperatureAir()));
							units.add(currentData == null ? "" : "° C");
						}
						else if (displayedData[i].equals("T_dew")) {
							values.add(currentData == null ? "" : NumberFormat.getFormat("##.#").format(currentData.getTemperatureDew()));
							units.add(currentData == null ? "" : "° C");
						}
						else if (displayedData[i].equals("T_road")) {
							values.add(currentData == null ? "" : NumberFormat.getFormat("##.#").format(currentData.getTemperatureRoad()));
							units.add(currentData == null ? "" : "° C");
						}
						else if (displayedData[i].equals("T_below_surf")) {
							values.add(currentData == null ? "" : NumberFormat.getFormat("##.#").format(currentData.getTemperatureBelowSurf()));
							units.add(currentData == null ? "" : "° C");
						}
						else if (displayedData[i].equals("T_freezing")) {
							values.add(currentData == null ? "" : NumberFormat.getFormat("##.#").format(currentData.getTemperatureFreezing()));
							units.add(currentData == null ? "" : "° C");
						}
						else if (displayedData[i].equals("humidity")) {
							values.add(currentData == null ? "" : NumberFormat.getFormat("##.#").format(currentData.getHumidity()));
							units.add(currentData == null ? "" : "%");
						}
						else if (displayedData[i].equals("surface_state")) {
							if (currentData == null) {
								currentValue = "";
							} 
							else if (currentData.getSurfaceState() == 0) {
								currentValue = Language.display_surfaceState_0;
							}
							else if (currentData.getSurfaceState() == 1) {
								currentValue = Language.display_surfaceState_1;
							}
							else if (currentData.getSurfaceState() == 2) {
								currentValue = Language.display_surfaceState_2;
							}
							else if (currentData.getSurfaceState() == 3) {
								currentValue = Language.display_surfaceState_3;
							}
							else if (currentData.getSurfaceState() == 4) {
								currentValue = Language.display_surfaceState_4;
							}
							else if (currentData.getSurfaceState() == 5) {
								currentValue = Language.display_surfaceState_5;
							}
							else if (currentData.getSurfaceState() == 6) {
								currentValue = Language.display_surfaceState_6;
							}
							else if (currentData.getSurfaceState() == 7) {
								currentValue = Language.display_surfaceState_7;
							}
							else if (currentData.getSurfaceState() == 8) {
								currentValue = Language.display_surfaceState_8;
							}
							else if (currentData.getSurfaceState() == 9) {
								currentValue = Language.display_surfaceState_9;
							}
							values.add(currentValue);
							units.add("");
						}
						else if (displayedData[i].equals("salinity")) {
							values.add(currentData == null ? "" : NumberFormat.getFormat("##.#").format(currentData.getSalinity()));
							units.add(currentData == null ? "" : "%");
						}
						else if (displayedData[i].equals("precipitation_type")) {
							if (currentData == null) {
								currentValue = "";
							} 
							else if (currentData.getPrecipitationType() == 0) {
								currentValue = Language.display_precipitationType_0;
							}
							else if (currentData.getPrecipitationType() == 1) {
								currentValue = Language.display_precipitationType_1;
							}
							else if (currentData.getPrecipitationType() == 2) {
								currentValue = Language.display_precipitationType_2;
							}
							else if (currentData.getPrecipitationType() == 3) {
								currentValue = Language.display_precipitationType_3;
							}
							else if (currentData.getPrecipitationType() == 9) {
								currentValue = Language.display_precipitationType_9;
							}
							values.add(currentValue);
							units.add("");
						}
						else if (displayedData[i].equals("precipitation_intensity")) {
							if (currentData == null) {
								currentValue = "";
							} 
							else if (currentData.getPrecipitationIntensity() == 0) {
								currentValue = Language.display_precipitationIntensity_0;
							}
							else if (currentData.getPrecipitationIntensity() == 1) {
								currentValue = Language.display_precipitationIntensity_1;
							}
							else if (currentData.getPrecipitationIntensity() == 2) {
								currentValue = Language.display_precipitationIntensity_2;
							}
							else if (currentData.getPrecipitationIntensity() == 3) {
								currentValue = Language.display_precipitationIntensity_3;
							}
							else if (currentData.getPrecipitationIntensity() == 4) {
								currentValue = Language.display_precipitationIntensity_4;
							}
							else if (currentData.getPrecipitationIntensity() == 5) {
								currentValue = Language.display_precipitationIntensity_5;
							}
							else if (currentData.getPrecipitationIntensity() == 9) {
								currentValue = Language.display_precipitationIntensity_9;
							}
							values.add(currentValue);
							units.add("");
						}
						else if (displayedData[i].equals("freezing_risk")) {
							if (currentData == null) {
								currentValue = "";
							} 
							else if (currentData.getFreezingRisk() == 0) {
								currentValue = Language.display_freezingRisk_0;
							}
							else if (currentData.getFreezingRisk() == 1) {
								currentValue = Language.display_freezingRisk_1;
							}
							else if (currentData.getFreezingRisk() == 2) {
								currentValue = Language.display_freezingRisk_2;
							}
							else if (currentData.getFreezingRisk() == 3) {
								currentValue = Language.display_freezingRisk_3;
							}
							else if (currentData.getFreezingRisk() == 4) {
								currentValue = Language.display_freezingRisk_4;
							}
							else if (currentData.getFreezingRisk() == 9) {
								currentValue = Language.display_freezingRisk_9;
							}
							values.add(currentValue);
							units.add("");
						}
					}
					svgWeatherDisplay.setTextContent(values, units);		
				}
			}
			@Override
			public void onFailure(Throwable caught) {
				Log.error("synopticDatabaseService.getLastWeatherDataForStations failed. Message : " + caught.getMessage());
				new CustomDialogBox(Language.genericErrorString, Language.okString);
			}
		});
	}
}
