package karrus.client.synoptic.svgElements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import karrus.client.FrontalWebApp;
import karrus.client.generic.CustomDialogBox;
import karrus.client.utils.Utils;
import karrus.shared.language.Language;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SvgTtTimer extends Timer {

	List<SvgTtColor> svgTtColors;
	List<SvgTtDisplay> svgTtDisplays;
	
	public SvgTtTimer(List<SvgTtColor> svgTtColors, List<SvgTtDisplay> svgTtDisplaies) {
		this.svgTtColors = svgTtColors;
		this.svgTtDisplays = svgTtDisplaies;
	}
	
	@Override
	public void run() {
		List<String> itinerariesNames = new ArrayList<String>();
		for (SvgTtColor svgTtColor : svgTtColors) {
			itinerariesNames.add(svgTtColor.getItinerary().getId().getOrigin() + "-" + svgTtColor.getItinerary().getId().getDestination());
		}
		FrontalWebApp.synopticDatabaseService.getLastDataForItineraries(itinerariesNames, new AsyncCallback<Map<String, Double>>() {
			@Override
			public void onSuccess(Map<String, Double> result) {
				for (SvgTtColor svgTtColor : SvgTtTimer.this.svgTtColors) {
					if (result.containsKey(svgTtColor.getItinerary().getId().getOrigin() + "-" + svgTtColor.getItinerary().getId().getDestination())) {
						double meanTravelTime = result.get(svgTtColor.getItinerary().getId().getOrigin() + "-" + svgTtColor.getItinerary().getId().getDestination());
						if (meanTravelTime < 0) {
							svgTtColor.setColor("rgb(102, 204, 255)");
						}
						else if (0 <= meanTravelTime && meanTravelTime < svgTtColor.getItinerary().getLevelOfServiceThreshold1()) {
							svgTtColor.setColor("green");
						} 
						else if (svgTtColor.getItinerary().getLevelOfServiceThreshold1() <= meanTravelTime && meanTravelTime < svgTtColor.getItinerary().getLevelOfServiceThreshold2()) {
							svgTtColor.setColor("orange");
						}
						else {
							svgTtColor.setColor("red");
						}
					}
					else {
						svgTtColor.setColor("rgb(102, 204, 255)");
					}
				}
				int minutes = 0;
				int seconds = 0;
				String travelTime = "";
				for (SvgTtDisplay svgTtDisplay : SvgTtTimer.this.svgTtDisplays) {
					String[] displayedItineraries = svgTtDisplay.getSynTtDisplay().getContent().split("\\|");
					String value;
					List<String> values = new ArrayList<String>();
					List<String> units = new ArrayList<String>();
					Double currentData;
					for (int i = 0; i < displayedItineraries.length; i++) {
						if (displayedItineraries[i].equals("up")) {
							value = "";
							currentData = result.get(svgTtDisplay.getSynTtDisplay().getOrigin() + "-" + svgTtDisplay.getSynTtDisplay().getDestination());
							if (currentData != null) {
								if (currentData >= 0) {
									minutes = (int) Math.floor(currentData/60);
									seconds = (int) Math.round(currentData % 60);
									travelTime = Utils.addLeadingWhitespaces(NumberFormat.getFormat("###").format(minutes), 3) + " min " + Utils.addLeadingZeros(NumberFormat.getFormat("##").format(seconds), 2) + " s";
									value = travelTime;
								}
							}
							values.add(value);
							units.add("");
						} 
						else if (displayedItineraries[i].equals("down")) {
							value = "";
							currentData = result.get(svgTtDisplay.getSynTtDisplay().getDestination() + "-" + svgTtDisplay.getSynTtDisplay().getOrigin());
							if (currentData != null) {
								if (currentData >= 0) {
									minutes = (int) Math.floor(currentData/60);
									seconds = (int) Math.round(currentData % 60);
									travelTime = Utils.addLeadingWhitespaces(NumberFormat.getFormat("###").format(minutes), 3) + " min " + Utils.addLeadingZeros(NumberFormat.getFormat("##").format(seconds), 2) + " s";
									value = travelTime;
								}
							}
							values.add(value);
							units.add("");
						} 						
					}
					svgTtDisplay.setTextContent(values, units);		
				}
			}
			@Override
			public void onFailure(Throwable caught) {
				Log.error("synopticDatabaseService.getLastDataForItineraries failed. Message : " + caught.getMessage());
				new CustomDialogBox(Language.genericErrorString, Language.okString);
			}
		});
	}
}
