package karrus.client.appearance;

import java.util.List;
import karrus.client.FrontalWebApp;
import com.google.gwt.user.client.ui.CheckBox;

/**
 * Layout parameters.
 * 
 * @author Estelle DUMAS: estelle.dumas@karrus-its.com
 */
public class LayoutInfo {

	public static String iconDimension = "14px";
	public static String passwordTextBoxWidth = "200px";
	public static String logoWidth = "100px";
	public static String dateBoxWidth = "80px";
	public static String tailTextBoxWidth = "50px";
	public static String aliveTimeHorizonTextBoxWidth = "80px";
	public static String startStopButtonWidth = "100px";
	// sizes to change if the screen resolution change
	public static String closableImageSize = "12px";
	public static String textBoxWidthForSensor = "300px";
	public static String textBoxWidth = "150px";
	public static String textAeraWidth = "300px";
	public static String listBoxWidthForTimes = "50px";
	public static String tableHeaderHeight = "50px";
	public static int yLabelWidthForPlot = 20;
	// others
	public static int indicatorPlotWidth = FrontalWebApp.getWidthForIndicatorPlot();
	public static int maxOpenedTabs = 16;
	public static int plotWidth = FrontalWebApp.getWidthForMainPanel() * 90 / 100;
	public static int onePlotHeight = FrontalWebApp.getHeightForTable() * 93 / 100;
	public static int onePlotHeight2 = FrontalWebApp.getHeightForTable() * 80 / 100;
	public static int plotWidthForPlotInTab = plotWidth * 150 / 100;
	public static int plotHeightForPlotInTab = onePlotHeight * 150 / 100;
	public static int maxNbLinesForBigTable = 35;

	/**
	 * Gets the height for a plot depending on the number of checkboxes checked.
	 * 
	 * @param cbList
	 * @return int
	 */
	public static int getPlotHeight(List<CheckBox> cbList) {
		int cpt = 0;
		for (CheckBox cb : cbList)
			if (cb.getValue())
				cpt++;
		if (cpt == 0)
			return 0;
		else
			return LayoutInfo.onePlotHeight / cpt;
	}

	/**
	 * Gets the height for a plot depending on the number of boolean trues.
	 * 
	 * @param cb1
	 *            boolean
	 * @param cb2
	 *            boolean
	 * @param cb3
	 *            booleans
	 * @return int
	 */
	public static int getPlotHeight(boolean cb1, boolean cb2, boolean cb3) {
		int cpt = 0;
		if (cb1)
			cpt++;
		if (cb2)
			cpt++;
		if (cb3)
			cpt++;
		if (cpt == 0)
			return 0;
		else
			return LayoutInfo.onePlotHeight / cpt;
	}
	
}
