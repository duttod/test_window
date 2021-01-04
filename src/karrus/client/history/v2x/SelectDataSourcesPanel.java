package karrus.client.history.v2x;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import karrus.client.FrontalWebApp;
import karrus.client.appearance.Css;
import karrus.client.generic.ListBoxGeneric;
import karrus.shared.hibernate.RsuStation;
import karrus.shared.language.Language;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 *
 * @author Estelle DUMAS: estelle.dumas@karrus-its.com
 */
public class SelectDataSourcesPanel extends Composite {
	
	private ListBoxGeneric stationsListBox;
	private List<String> stationsList = new ArrayList<String>();

	/**
	 * Constructor.
	 */
	public SelectDataSourcesPanel() {
		for (RsuStation station : FrontalWebApp.databaseObjects.getStations()) {
			stationsList.add(station.getId());			
		}
		Collections.sort(stationsList);
		stationsListBox = new ListBoxGeneric(Language.station, stationsList, stationsList.size() > 0 ? stationsList.get(0) : "", false);
		final HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSpacing(5);
		horizontalPanel.add(stationsListBox);
		Label label = new Label(Language.stationsLabel);
		label.setStyleName(Css.boldStyle);
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.add(label);
		verticalPanel.add(horizontalPanel);
		DockPanel dockPanel = new DockPanel();
		dockPanel.add(verticalPanel, DockPanel.LINE_START);
		this.initWidget(dockPanel);
	}
	
	/**
	 * 
	 * @return Selected station name
	 * @throws Exception
	 */
	public String getSelectedStation() throws Exception {
		String selectedStation = stationsListBox.getSelectedValue();
		if (selectedStation.equals(Language.emptyString)) {
			throw new Exception(Language.noElementChosenError(Language.station));
		}	
		return selectedStation;
	}
}
