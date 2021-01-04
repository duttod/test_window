package karrus.client.synoptic.svgElements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import karrus.client.FrontalWebApp;
import karrus.client.generic.CustomDialogBox;
import karrus.client.utils.Utils;
import karrus.shared.hibernate.CtCountData;
import karrus.shared.language.Language;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SvgRdtTimer extends Timer {

	List<SvgRdtColor> svgRdtColors;
	List<SvgRdtDisplay> svgRdtDisplays;
	
	public SvgRdtTimer(List<SvgRdtColor> svgRdtColors, List<SvgRdtDisplay> svgRdtDisplays) {
		this.svgRdtColors = svgRdtColors;
		this.svgRdtDisplays = svgRdtDisplays;
	}
	
	@Override
	public void run() {
		List<String> stationsNames = new ArrayList<String>();
		List<String> lanes = new ArrayList<String>();
		for (SvgRdtColor svgRdtColor : svgRdtColors) {
			stationsNames.add(svgRdtColor.getLane().getId().getStation());
			lanes.add(svgRdtColor.getLane().getId().getLane());
		}
		FrontalWebApp.synopticDatabaseService.getLastDataForLanes(stationsNames, lanes, new AsyncCallback<Map<String, CtCountData>>() {
			@Override
			public void onSuccess(Map<String, CtCountData> result) {
				for (SvgRdtColor svgRdtColor : SvgRdtTimer.this.svgRdtColors) {
					if (result.containsKey(svgRdtColor.getLane().getId().getStation() + "/" + svgRdtColor.getLane().getId().getLane())) {
						if (result.get(svgRdtColor.getLane().getId().getStation() + "/" + svgRdtColor.getLane().getId().getLane()).getSpeed() > 30 ||
						    result.get(svgRdtColor.getLane().getId().getStation() + "/" + svgRdtColor.getLane().getId().getLane()).getSpeed() < 0) {
							svgRdtColor.setColor("green");
						} 
						else {
							svgRdtColor.setColor("red");
						}
					}
					else {
						svgRdtColor.setColor("rgb(102, 204, 255)");
					}
				}
				for (SvgRdtDisplay svgRdtDisplay : SvgRdtTimer.this.svgRdtDisplays) {
					String[] displayedLanes = svgRdtDisplay.getSynRdtDisplays().getContent().split("\\|");
					List<String> values = new ArrayList<String>();
					List<String> units = new ArrayList<String>();
					CtCountData currentData;
					for (int i = 0; i < displayedLanes.length; i++) {
						currentData = result.get(svgRdtDisplay.getSynRdtDisplays().getStation() + "/" + displayedLanes[i].split(",")[1]);
						if (displayedLanes[i].split(",")[0].equals("count")) {
							values.add(currentData == null ? "" : Utils.addLeadingWhitespaces(NumberFormat.getFormat("###").format(currentData.getCount()*60), 4));
							units.add(currentData == null ? "" : "veh/h");
						}
						else if (displayedLanes[i].split(",")[0].equals("speed")) {
							values.add((currentData == null || currentData.getSpeed() == -1) ? "" : Utils.addLeadingWhitespaces(NumberFormat.getFormat("###").format(currentData.getSpeed()), 4));
							units.add((currentData == null || currentData.getSpeed() == -1) ? "" : "km/h");
						}
						else if (displayedLanes[i].split(",")[0].equals("occupancy")) {
							values.add(currentData == null ? "" : Utils.addLeadingWhitespaces(NumberFormat.getFormat("##.#").format(currentData.getOccupancy()), 4));
							units.add(currentData == null ? "" : "%");
						}
						
					}
					svgRdtDisplay.setTextContent(values, units);	
				}
			}
			@Override
			public void onFailure(Throwable caught) {
				Log.error("synopticDatabaseService.getLastDataForLanes failed. Message : " + caught.getMessage());
				new CustomDialogBox(Language.genericErrorString, Language.okString);
			}
		});
	}
}
