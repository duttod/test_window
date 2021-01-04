package karrus.client.history.counting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import karrus.client.FrontalWebApp;
import karrus.client.appearance.Css;
import karrus.client.generic.CustomDialogBox;
import karrus.client.generic.ListBoxGeneric;
import karrus.shared.hibernate.RsuStation;
import karrus.shared.language.Language;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
	
	private ListBoxGeneric stationsLb;
	private ListBoxGeneric waysLB;
	private List<String> stationsList = new ArrayList<String>();

	/**
	 * Constructor.
	 */
	public SelectDataSourcesPanel() {
		
		for (RsuStation station : FrontalWebApp.databaseObjects.getStations()) {
			if (station.getCycleCountingSec() >=  0) {
				stationsList.add(station.getId());
			}
		}
		Collections.sort(stationsList);
		stationsLb = new ListBoxGeneric(Language.station, stationsList, stationsList.size() > 0 ? stationsList.get(0) : "", false);
		waysLB = new ListBoxGeneric(Language.way, new ArrayList<String>(), "", false);
		stationsLb.getListBox().addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				String newValue = stationsLb.getSelectedValue();
				FrontalWebApp.genericDatabaseService.getWaysForStation(newValue, new AsyncCallback<List<String>>() {
					@Override
					public void onSuccess(List<String> result) {
						Log.debug("genericDatabaseService.getWaysForStation: ok");
						waysLB.setValues(result, result.size() > 0 ? result.get(0) : "");
					}
					@Override
					public void onFailure(Throwable caught) {
						Log.error("genericDatabaseService.getWaysForStation: failure\n"+caught.getMessage());
						new CustomDialogBox(Language.genericErrorString, Language.okString);
						waysLB.setValues(new ArrayList<String>(), "");
					}
				});
			}
		});
		final HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSpacing(5);
		horizontalPanel.add(stationsLb);
		horizontalPanel.add(waysLB);
		Label label = new Label(Language.wayOfTrafficString);
		label.setStyleName(Css.boldStyle);
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.add(label);
		verticalPanel.add(horizontalPanel);
		DockPanel dockPanel = new DockPanel();
		dockPanel.add(verticalPanel, DockPanel.LINE_START);
		this.initWidget(dockPanel);
		FrontalWebApp.genericDatabaseService.getWaysForStation(stationsList.size() > 0 ? stationsList.get(0) : "", new AsyncCallback<List<String>>() {
			@Override
			public void onSuccess(List<String> result) {
				Log.debug("genericDatabaseService.getWaysForStation: ok");
				waysLB.setValues(result, result.size() > 0 ? result.get(0) : "");
			}
			@Override
			public void onFailure(Throwable caught) {
				Log.error("genericDatabaseService.getWaysForStation: failure\n"+caught.getMessage());
				new CustomDialogBox(Language.genericErrorString, Language.okString);
				waysLB.setValues(new ArrayList<String>(), "");
			}
		});
	}
	
	/**
	 * 
	 * @return Selected station name
	 * @throws Exception
	 */
	public String getSelectedStation() throws Exception{
		String selectedAccessPointName = stationsLb.getSelectedValue();
		if (selectedAccessPointName.equals(Language.emptyString)) {
			throw new Exception(Language.noElementChosenError(Language.station));
		}	
		return selectedAccessPointName;
	}
	
	/**
	 * 
	 * @return Selected way in the way list box
	 * @throws Exception
	 */
	public String getSelectedWay() throws Exception{
		String selectedWay = waysLB.getSelectedValue();
		if (selectedWay.equals(Language.emptyString)) {
			throw new Exception(Language.noElementChosenError(Language.wayString));
		}	
		return selectedWay;
	}
}
