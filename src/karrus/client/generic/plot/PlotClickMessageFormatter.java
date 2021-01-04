package karrus.client.generic.plot;

import ca.nanometrics.gflot.client.event.PlotItem;
import ca.nanometrics.gflot.client.event.PlotPosition;
import ca.nanometrics.gflot.client.jsni.Plot;

public interface PlotClickMessageFormatter {

	public String getMessage(Plot plot, PlotPosition position, PlotItem item);
}
