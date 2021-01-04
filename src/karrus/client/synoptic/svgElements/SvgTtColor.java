package karrus.client.synoptic.svgElements;

import com.allen_sauer.gwt.log.client.Log;

import karrus.client.generic.ClosableDialogBox;
import karrus.client.synoptic.modal.travelTime.TravelTimeDashboard;
import karrus.shared.hibernate.TtItinerary;

public class SvgTtColor extends SvgElement {

	private TtItinerary itinerary;
	private int width = 900;
	private int height = 400;
	
	public SvgTtColor(TtItinerary itinerary, String svgId, String htmlObjectId) {
		super(svgId, htmlObjectId, true);
		this.itinerary = itinerary;
	}
	
	@Override
	public void onMouseDown() {
		try {
			TravelTimeDashboard travelTimeDashboard = new TravelTimeDashboard(itinerary.getName(), width, height);
			ClosableDialogBox closableDialogBox = new ClosableDialogBox(itinerary.getName(), width, false, false, travelTimeDashboard);
			closableDialogBox.add(travelTimeDashboard);
			closableDialogBox.center();
			closableDialogBox.show();
		} catch (Exception e) {
			Log.error(e.getMessage());
		}
	};
	
	public TtItinerary getItinerary() {
		return itinerary;
	}
}
