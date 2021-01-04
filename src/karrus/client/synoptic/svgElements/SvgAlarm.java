package karrus.client.synoptic.svgElements;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.HTML;

import karrus.client.generic.CustomDialogBox;
import karrus.shared.hibernate.AlAlarmOpened;
import karrus.shared.language.Language;

public class SvgAlarm extends SvgElement {

	String source;
	String type;
	private List<AlAlarmOpened> openedAlarmsList = new ArrayList<AlAlarmOpened>();
	private DateTimeFormat dateFormatter = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss");
	boolean isHoverable;
			
	public SvgAlarm(String source, String type, String svgId, String htmlObjectId, boolean isHoverable) {
		super(svgId, htmlObjectId, isHoverable);
		this.source = source;
		this.type = type;
		this.isHoverable = isHoverable;
	}
	
	@Override
	public void onMouseDown() {
		if (openedAlarmsList.size() != 0 && isHoverable) {	
			String message= "";
			message += source + '\n';
			for (AlAlarmOpened openedAlarm : openedAlarmsList) {
				message += "- " + dateFormatter.format(openedAlarm.getId().getOpeningTimestamp()) + " - " + openedAlarm.getId().getType() + "\n";
			}
			HTML label = new HTML(new SafeHtmlBuilder().appendEscapedLines(message).toSafeHtml());
			label.addStyleName("textLeftAlign");
			new CustomDialogBox(label, Language.okString, "");
		} 
	};
	
	public String getSource() {
		return source;
	}
	
	public String getType() {
		return type;
	}
	
	public List<AlAlarmOpened> getOpenedAlarmsList() {
		return openedAlarmsList;
	}
	
}
