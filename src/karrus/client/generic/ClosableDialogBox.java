package karrus.client.generic;

import karrus.client.appearance.Css;
import karrus.client.synoptic.modal.DataDashboardInterface;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;

/**
 * @author 
 * @version 1.0
 */
@SuppressWarnings("deprecation")
public class ClosableDialogBox extends DialogBox {

	private Image closableImage;
	private HTML title;
	private DataDashboardInterface countDataDashboard = null;

	public ClosableDialogBox(String title, int width, boolean autoHide, boolean modal, DataDashboardInterface countDataDashboard) {
		super(autoHide, modal);
		this.title = new HTML(title);
		this.countDataDashboard = countDataDashboard;
		setStyleName(Css.dialogBoxStyle);
		closableImage = new Image(GWT.getHostPageBaseURL()+"images/croix.svg");
		closableImage.setStyleName(Css.cursorAutoStyle);
		Element td = getCellElement(0, 1);
		DOM.removeChild(td, (Element) td.getFirstChildElement());
		AbsolutePanel captionPanel = new AbsolutePanel();
		captionPanel.addStyleName(Css.captionInDialogBoxStyle);
		captionPanel.setSize(width+"px", "25px");
		DOM.appendChild(td, captionPanel.getElement());
		captionPanel.add(this.title, 0, 0);
		if (countDataDashboard!=null)
			captionPanel.add(closableImage, width-10, 0);
	}

	public ClosableDialogBox(String title, int width, boolean autoHide, boolean modal) {
		this(title, width, autoHide, modal, null);
	}

	@Override
	public String getHTML()	{
		return this.title.getHTML();
	}

	@Override
	public String getText()	{
		return this.title.getText();
	}

	@Override
	public void setHTML(String html){
		this.title.setHTML(html);
	}

	@Override
	public void setText(String text){
		this.title.setText(text);
	}

	@Override
	protected void onPreviewNativeEvent(NativePreviewEvent event){
		NativeEvent nativeEvent = event.getNativeEvent();
		if (countDataDashboard!=null) {
			if (!event.isCanceled()	&& (event.getTypeInt() == Event.ONCLICK) && isCloseEvent(nativeEvent)) {
				countDataDashboard.stopAction();
				this.hide();
			}
		}
		super.onPreviewNativeEvent(event);
	}

	@SuppressWarnings("unlikely-arg-type")
	private boolean isCloseEvent(NativeEvent event){
		return event.getEventTarget().equals(closableImage.getElement());//compares equality of the underlying DOM elements
	}
}