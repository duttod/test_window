package karrus.client.history.traveltime;


import java.util.ArrayList;
import java.util.List;

import karrus.client.appearance.Css;
import karrus.client.generic.ListBoxGeneric;
import karrus.shared.hibernate.TtItinerary;
import karrus.shared.language.Language;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 *
 * @author Estelle DUMAS: estelle.dumas@karrus-its.com
 */
public class SelectDataSourcesPanel extends Composite {
	
	private ListBoxGeneric itineraryLB;

	/**
	 * Constructor.
	 */
	public SelectDataSourcesPanel(List<TtItinerary> it) {
		List<String> itineraries = new ArrayList<String>();
		for (TtItinerary i : it) {
			itineraries.add(i.getName());
		}
		itineraryLB = new ListBoxGeneric(null, itineraries, itineraries.size() == 0 ? "" : itineraries.get(0), false);
		Label l = new Label(Language.itineraryString);
		l.setStyleName(Css.boldStyle);
		HorizontalPanel h = new HorizontalPanel();
		h.add(itineraryLB);
		h.setSpacing(5);
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.add(l);
		vPanel.add(h);
		this.initWidget(vPanel);
	}
	
	public String getSelectedItinerary() throws Exception{
		String s = itineraryLB.getSelectedValue();
		if (s.equals(Language.emptyString)) {
			throw new Exception(Language.noElementChosenError(Language.itineraryString));
		}	
		return s;
	}

	
	
	
}
