package karrus.client.synoptic.svgElements;

import com.allen_sauer.gwt.log.client.Log;

import karrus.client.generic.ClosableDialogBox;
import karrus.client.synoptic.modal.count.CountDataDashboard;
import karrus.shared.hibernate.CtLane;

public class SvgRdtColor extends SvgElement {

	CtLane lane;
	private int width = 900;
	private int height = 400;
	
	public SvgRdtColor(CtLane lane, String svgId, String htmlObjectId) {
		super(svgId, htmlObjectId, true);
		this.lane = lane;
	}
	
	public void onMouseDown() {
		try {
			CountDataDashboard countDataDashboard = new CountDataDashboard(lane.getId().getStation(), lane.getId().getLane(), width, height);
			ClosableDialogBox closableDialogBox = new ClosableDialogBox(lane.getId().getStation() + "-" + lane.getId().getLane(), width, false, false, countDataDashboard);
			closableDialogBox.add(countDataDashboard);
			closableDialogBox.center();
			closableDialogBox.show();
		} catch (Exception e) {
			Log.error(e.getMessage());
		}
	}
	
	public CtLane getLane() {
		return lane;
	}
}
