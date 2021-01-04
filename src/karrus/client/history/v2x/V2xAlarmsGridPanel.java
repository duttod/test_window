package karrus.client.history.v2x;

import java.util.List;

import karrus.client.FrontalWebApp;
import karrus.client.generic.GridResize;
import karrus.shared.hibernate.V2xAlarm;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Panel containing v2x alarms grid
 * 
 * @author estelle.dumas@karrus-its.com
 */
public class V2xAlarmsGridPanel extends Composite implements GridResize {

	private VerticalPanel gridPanel;
	private V2xAlarmsGrid v2xAlarmsGrid;
	
	/**
	 * 
	 */
	public V2xAlarmsGridPanel(List<V2xAlarm> v2xAlarms) {	
		gridPanel = new VerticalPanel();
		gridPanel.setSize(FrontalWebApp.getWidthForMainPanel() + "px", FrontalWebApp.getHeightForMainPanel() + "px");
		gridPanel.getElement().getStyle().setPaddingTop(20, Unit.PX);
		gridPanel.getElement().getStyle().setPaddingLeft(10, Unit.PX);
//		gridPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
//		plotsPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		this.initWidget(gridPanel);
		v2xAlarmsGrid = new V2xAlarmsGrid(v2xAlarms);
		gridPanel.add(v2xAlarmsGrid.getDatagrid());
	}

	@Override
	public void gridsResize() {
		v2xAlarmsGrid.getDatagrid().onResize();
	}
}
