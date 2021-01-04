package karrus.client.generic.tab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import karrus.client.FrontalWebApp;
import karrus.client.appearance.Css;
import karrus.client.appearance.LayoutInfo;
import karrus.client.generic.GridResize;
import karrus.shared.language.Language;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Creates a tabbed panel.
 * 
 * @author Estelle DUMAS: estelle.dumas@karrus-its.com
 */
public class TabbedPanelGeneric extends Composite {

	public List<String> usedTitles;
	public TabPanel tabPanel;
	private int tableCounter = 1; // counter for the tables
	private int plotCount = 1; // counter for the plots
	private Widget mainWidget; // the main widget (the content) of the tabbed panel.
	private String mainTitle;
	private Map<Widget, ClosablePanel> widget2closablePanel = new HashMap<Widget, ClosablePanel>();
	private Map<Widget, String> nonClosableWidget2title = new HashMap<Widget, String>();
	public Map<String, Integer> openedTabbedPanel2tabIndex = new HashMap<String, Integer>();

	/**
	 * Constructor.
	 */
	public TabbedPanelGeneric(){
		this.mainWidget = null;
		this.mainTitle = "";
		usedTitles = new ArrayList<String>();
		tabPanel = new TabPanel();
		tabPanel.setStyleName(Css.gwtDecoratedTabBarStyle);
		tabPanel.getDeckPanel().setStyleName(Css.noBorderTabPanelBottomStyle);
		this.initWidget(this.tabPanel);	
	}
	
	/**
	 * Constructor. 
	 * @param composite the content of the tabbed panel.
	 * @param title string
	 */
	public TabbedPanelGeneric(Composite composite, String title){
		ScrollPanel scrollPanel = new ScrollPanel(composite);
		scrollPanel.setSize(FrontalWebApp.getWidthForMainPanel() + "px", FrontalWebApp.getHeightForMainPanel() + "px");
		this.mainWidget = scrollPanel;
		this.mainTitle = title;
		usedTitles = new ArrayList<String>();
		tabPanel = new TabPanel();
		tabPanel.setStyleName(Css.gwtDecoratedTabBarStyle);
		tabPanel.getTabBar().removeStyleName("gwt-TabBarItem");
		tabPanel.getTabBar().removeStyleName(".gwt-DecoratedTabBar .gwt-TabBarItem");
		tabPanel.getDeckPanel().setStyleName(Css.noBorderTabPanelBottomStyle);
		tabPanel.add(scrollPanel, title);
		tabPanel.addSelectionHandler(new SelectionHandler<Integer>() {
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				Widget selectedWidget = tabPanel.getWidget(tabPanel.getTabBar().getSelectedTab());
				if (selectedWidget instanceof ScrollPanel) {
					Widget selectedWidgetChild = ((ScrollPanel)selectedWidget).getWidget();
					if (selectedWidgetChild instanceof GridResize) {
						((GridResize)selectedWidgetChild).gridsResize();
					}
				}
				if (selectedWidget instanceof GridResize) {
					((GridResize)selectedWidget).gridsResize();
				}
			}
		});
		nonClosableWidget2title.put(scrollPanel, title);
		tabPanel.selectTab(0); // to open the right tab
		openedTabbedPanel2tabIndex.put(title, 0);
		this.initWidget(this.tabPanel);	
	}

	public void setSize(int width, int height){
		tabPanel.getDeckPanel().setSize(width+"px", height+"px");
	}

	/**
	 * Gets the main widget (the content) of the tabbed panel.
	 * @return {@link Composite}
	 */
	public Widget getMainWidget(){
		return this.mainWidget;	
	}

	/**
	 * Adds a new closable tab to the tabbed panel.
	 * @param newTabbedPanel the widget to add
	 * @param newTabbedPanelLabel the title on the tab
	 * @throws Exception 
	 */
	public void addClosableTabbedPanel(Widget newTabbedPanel, String newTabbedPanelLabel) throws Exception{
		if (openedTabbedPanel2tabIndex.keySet().contains(newTabbedPanelLabel)) {
			throw new Exception("L'onglet '"+newTabbedPanelLabel+"' est déjà ouvert.");
		}	
		ScrollPanel newTabbedPanelScrollPanel = new ScrollPanel(newTabbedPanel);
		newTabbedPanelScrollPanel.setSize(FrontalWebApp.getWidthForMainPanel() + "px", FrontalWebApp.getHeightForMainPanel() + "px");
		ClosablePanel newTabbedPanelClosableTab = new ClosablePanel(newTabbedPanelLabel, this, newTabbedPanelScrollPanel);
		tabPanel.add(newTabbedPanelScrollPanel, newTabbedPanelClosableTab);
		widget2closablePanel.put(newTabbedPanelScrollPanel, newTabbedPanelClosableTab);
		int index = tabPanel.getWidgetIndex(newTabbedPanelScrollPanel);
		tabPanel.selectTab(index);
		openedTabbedPanel2tabIndex.put(newTabbedPanelLabel, index);
	}

	public Widget getClosableTabPanel(String title) throws Exception{
		for (Widget widget: widget2closablePanel.keySet()){
			ClosablePanel closablePanel = widget2closablePanel.get(widget);
			if (closablePanel.getTitle().equals(title))
				return closablePanel.getWidget();
		}
		throw new Exception(Language.closableTabPanelNotExistString(title));
	}

	public Widget getTabPanel(String title) throws Exception{
		for (Widget widget: nonClosableWidget2title.keySet()){
			if (nonClosableWidget2title.get(widget).equals(title))
				return widget;
		}
		throw new Exception(Language.closableTabPanelNotExistString(title));
	}

	public List<String> getOpenedTabs(){
		List<String> opened = new ArrayList<String>();
		for (String s : openedTabbedPanel2tabIndex.keySet()) {
			opened.add(s);
		}	
		return opened;
	}

	public void removeOpenTabbedPanel(String title){
		int index = openedTabbedPanel2tabIndex.get(title);
		openedTabbedPanel2tabIndex.remove(title);
		for (String opened : openedTabbedPanel2tabIndex.keySet()){
			if (openedTabbedPanel2tabIndex.get(opened)>index) {
				openedTabbedPanel2tabIndex.put(opened, index-1);
			}	
		}
	}

	public void removeClosableTabPanel(String dashboardName){
		int widgetCount = tabPanel.getWidgetCount();
		if (widgetCount!=1)
			for (Widget widget : widget2closablePanel.keySet()){
				if (dashboardName.equals(widget2closablePanel.get(widget).getTitle())){
					widget2closablePanel.get(widget).closeTabPanel();
				}
			}		
	}

	/**
	 * Adds a new closable tab to the tabbed panel.
	 * @param newTabbedPanel the wiget to add
	 * @param title the title on the tab
	 * @throws Exception 
	 */
	public void addTabPanel(Composite newTabbedPanel, String title) throws Exception {
		if (tabPanel.getTabBar().getTabCount()==LayoutInfo.maxOpenedTabs) {
			throw new Exception(Language.maxOpenedTabsError);
		}	
		if (openedTabbedPanel2tabIndex.keySet().contains(title)) {
			throw new Exception("L'onglet '"+title+"' est déjà ouvert.");
		}	
		ScrollPanel newTabbedPanelScrollPanel = new ScrollPanel(newTabbedPanel);
		newTabbedPanelScrollPanel.setSize(FrontalWebApp.getWidthForMainPanel() + "px", FrontalWebApp.getHeightForMainPanel() + "px");
		tabPanel.add(newTabbedPanelScrollPanel, title);
		nonClosableWidget2title.put(newTabbedPanelScrollPanel, title);
		int index = tabPanel.getWidgetIndex(newTabbedPanelScrollPanel);
		openedTabbedPanel2tabIndex.put(title, index);
	}

	/**
	 * Gets the counter for the tables.
	 * @return int
	 */
	public int getTableCounter(){
		int i = tableCounter;
		tableCounter++;
		return i;
	}

	/**
	 * Gets the counter for the plots.
	 * @return int
	 */
	public int getPlotCount(){
		int i = plotCount;
		plotCount++;
		return i;
	}

	/**
	 * Gets the automatic table name according to the table counter.
	 * @return int
	 */
	public String getTableName(){
		int cpt = getTableCounter();
		int nb = 1;
		if (cpt>9 & cpt<100)
			nb = 2;
		else if (cpt>99 & cpt<1000)
			nb = 3;
		String s = Language.tableString+cpt;
		while (usedTitles.contains(s))
			s = s.substring(0, s.length()-nb)+(cpt+1);
		return s;
	}

	/**
	 * Gets the automatic plot name according to the plot counter.
	 * @return int
	 */
	public String getPlotName(){
		int plotCount = getPlotCount();
		int plotCountNumberOfDigits = 1;
		if (plotCount>9 & plotCount<100)
			plotCountNumberOfDigits = 2;
		else if (plotCount>99 & plotCount<1000)
			plotCountNumberOfDigits = 3;
		String plotName = Language.plotString+plotCount;
		while (usedTitles.contains(plotName)) {
			plotName = plotName.substring(0, plotName.length()-plotCountNumberOfDigits)+(plotCount+1);
		}	
		return plotName;
	}


	/**
	 * Validate the name of the future tab.
	 * @param s the name to validate
	 * @return the valid name
	 * @throws Exception if the name is already used.
	 */
	public String validateName(String s) throws Exception{
		if (usedTitles.contains(s))
			throw new Exception(Language.alreadyTitleError);
		usedTitles.add(s);
		return s;
	}

	public TabPanel getTabPanel(){
		return this.tabPanel;
	}


	public int getCpt(){
		return tabPanel.getTabBar().getTabCount();
	}

	public void setUnusedTitle(String s){
		usedTitles.remove(s);
	}

	public void selectTab(int index){ 
		this.tabPanel.selectTab(index);
	}


	public int getWidgetIndex(Widget widget){
		return this.tabPanel.getWidgetIndex(widget);
	}

	public void remove(Widget widget){
		this.tabPanel.remove(widget);
	}

	public int getWidgetCount(){
		return this.tabPanel.getWidgetCount();
	}

	public String getTitle(){
		return this.mainTitle;
	}

	public int getIndexForTabbedPanel(String tabTitle) throws Exception{
		for (String tab : openedTabbedPanel2tabIndex.keySet())
			if (tab.equals(tabTitle))
				return openedTabbedPanel2tabIndex.get(tabTitle);
		throw new Exception(Language.noOpenedTabError(tabTitle));
	}


}
