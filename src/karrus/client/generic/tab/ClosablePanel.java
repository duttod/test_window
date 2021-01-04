package karrus.client.generic.tab;

import karrus.client.appearance.LayoutInfo;
import karrus.client.utils.Images;
import karrus.shared.language.Language;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * Creates a tabbed panel closable by clicking a cross in the top right hand corner.
 * 
 * @author Estelle DUMAS: estelle.dumas@karrus-its.com
 */
public class ClosablePanel extends Composite{
	
	private String title;
	private TabbedPanelGeneric tabPanel;
	private Widget concernedWidget;

	/**
	 * Constructor.
	 * @param title title of the tab
	 * @param tabPanel the parent tabbed panel related to this object
	 * @param concernedWidget the widget to be added in the panel
	 */
	public ClosablePanel(String title, TabbedPanelGeneric tabPanel, Widget concernedWidget){
		this.concernedWidget = concernedWidget;
		this.tabPanel = tabPanel;
		this.title = title;
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.add(new Label(title));
		Label emptyStringLabel = new Label(Language.emptyString); // necessary to have space between title and cross
		horizontalPanel.add(emptyStringLabel);
		emptyStringLabel.setWidth("5px");
		Image crossImage = new Image(Images.closableImageUrl);
		crossImage.setSize(LayoutInfo.closableImageSize, LayoutInfo.closableImageSize);
		crossImage.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				closeTabPanel();
			}
		});
		horizontalPanel.add(crossImage);	
		horizontalPanel.setCellVerticalAlignment(crossImage, HasVerticalAlignment.ALIGN_MIDDLE);
		this.initWidget(horizontalPanel);
	}
	
	public void closeTabPanel(){
		int index = tabPanel.getWidgetIndex(concernedWidget);
		tabPanel.remove(concernedWidget);
		tabPanel.removeOpenTabbedPanel(ClosablePanel.this.title);
		tabPanel.setUnusedTitle(ClosablePanel.this.title);
		if (index>0 && index==tabPanel.getWidgetCount())
			tabPanel.selectTab(index-1);
		else
			tabPanel.selectTab(index);
	}
	
	public Widget getWidget(){
		return this.concernedWidget;
	}
	
	public String getTitle(){
		return this.title;
	}
}
