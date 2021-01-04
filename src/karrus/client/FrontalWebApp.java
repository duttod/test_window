package karrus.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import karrus.client.appearance.Css;
import karrus.client.generic.CustomDialogBox;
import karrus.client.generic.tab.TabbedPanelGeneric;
import karrus.client.login.LoginDialogBox;
import karrus.client.login.UserManagement;
import karrus.client.menu.AlarmsHistoryMenu;
import karrus.client.menu.AlarmsStatusBar;
import karrus.client.menu.ItinerariesConfigurationMenu;
import karrus.client.menu.LanesConfigurationMenu;
import karrus.client.menu.RsuDiagnosticMenu;
import karrus.client.menu.StationsConfigurationMenu;
import karrus.client.menu.CurrentAlarmsMenu;
import karrus.client.menu.CountDataHistoryMenu;
import karrus.client.menu.SynopticMenu;
import karrus.client.menu.TravelTimesDataHistoryMenu;
import karrus.client.menu.UsersMenu;
import karrus.client.menu.V2xAlarmsMenu;
import karrus.client.menu.WeatherDataHistoryMenu;
import karrus.client.service.AlarmsDatabaseService;
import karrus.client.service.AlarmsDatabaseServiceAsync;
import karrus.client.service.ClockService;
import karrus.client.service.ClockServiceAsync;
import karrus.client.service.ConfigurationsDatabaseService;
import karrus.client.service.ConfigurationsDatabaseServiceAsync;
import karrus.client.service.ExportService;
import karrus.client.service.ExportServiceAsync;
import karrus.client.service.GenericDatabaseService;
import karrus.client.service.GenericDatabaseServiceAsync;
import karrus.client.service.IhmPropertiesService;
import karrus.client.service.IhmPropertiesServiceAsync;
import karrus.client.service.SynopticDatabaseService;
import karrus.client.service.SynopticDatabaseServiceAsync;
import karrus.shared.DatabaseObjects;
import karrus.shared.DatabaseObjectsTools;
import karrus.shared.IhmParameters;
import karrus.shared.hibernate.UsrCredential;
import karrus.shared.hibernate.UsrUser;
import karrus.shared.language.Language;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.reveregroup.gwt.imagepreloader.FitImage;
import com.reveregroup.gwt.imagepreloader.ImageLoadEvent;
import com.reveregroup.gwt.imagepreloader.ImageLoadHandler;
import com.reveregroup.gwt.imagepreloader.ImagePreloader;

/**
 * Entry point class.
 * 
 * @author Estelle DUMAS: estelle.dumas@karrus-its.com
 */

@SuppressWarnings("deprecation")
public class FrontalWebApp implements EntryPoint { 
	
	public static DatabaseObjects databaseObjects;
	public static DatabaseObjectsTools databaseObjectsTools;
	public static GenericDatabaseServiceAsync genericDatabaseService;
	public static ExportServiceAsync exportService;
	public static ConfigurationsDatabaseServiceAsync configurationsDatabaseService;
	public static AlarmsDatabaseServiceAsync alarmsDatabaseService;
	public static IhmPropertiesServiceAsync ihmPropertiesService;
	public static SynopticDatabaseServiceAsync synopticDatabaseService;
	public static ClockServiceAsync clockService;
	public static IhmParameters ihmParameters;
	public static String logoString = GWT.getHostPageBaseURL()+"images/logo.png";
	public static FitImage logo;
	public static int windowHeight = Window.getClientHeight();
	public static int windowWidth = Window.getClientWidth();
	public static int widthForMenu = 0;
	private static Composite mainPanel;
	private static Map<String, Composite> id2Panel = new HashMap<String, Composite>(); // lien entre un id (d'un sensor ou d'une station) et le panel qui lui correspond
	private DockPanel dockPanel = null;
	public static String secondaryServerIp = "";
	private String projectName = "";
	
	/**
	 * Start loading the application
	 */
	public void onModuleLoad() {
		// Handler to catch the uncaught exceptions thrown on the client side
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			public void onUncaughtException(Throwable e) {
				e.printStackTrace();
				Log.error("Unhandled exception : " + e.getMessage());
				for (StackTraceElement element : e.getStackTrace()) {
					Log.error(element.toString());
				}
			}
		});
		DeferredCommand.addCommand(new Command() {
			public void execute() {
				onModuleLoad2();
			}
		});
	}
	
	public void onModuleLoad2() {
//		// Add a window closing handler asking to the user if he wants to quit the application when he tries to close the window 
//		Window.addWindowClosingHandler(new Window.ClosingHandler(){
//			public void onWindowClosing(ClosingEvent event){
//				event.setMessage("Are you sure to close the window?");
//			}
//		});
		// Add a preview handler to create keyboard shortcuts to navigate between panels in a tab panel
		Event.addNativePreviewHandler(new NativePreviewHandler() {
			public void onPreviewNativeEvent(NativePreviewEvent event) {
				if (mainPanel instanceof TabbedPanelGeneric) {
					if (((TabbedPanelGeneric) mainPanel).tabPanel.getWidgetCount() <= 1) {
						return;
					}
				}
				NativeEvent nativeEvent = event.getNativeEvent();
				if (event.getTypeInt() == Event.ONKEYDOWN && (nativeEvent.getCtrlKey()) && (nativeEvent.getKeyCode()=='l' ||nativeEvent.getKeyCode()=='L')) {
					nativeEvent.preventDefault();
					if (mainPanel instanceof TabbedPanelGeneric) {
						TabbedPanelGeneric tabPanel = (TabbedPanelGeneric)mainPanel;
						int currentIndex = tabPanel.tabPanel.getTabBar().getSelectedTab();
						if (currentIndex < tabPanel.tabPanel.getTabBar().getTabCount() - 1){
							tabPanel.selectTab(currentIndex + 1);
						}
						else if (currentIndex == tabPanel.tabPanel.getTabBar().getTabCount() - 1) {
						}
					}
				}
				if (event.getTypeInt() == Event.ONKEYDOWN && (nativeEvent.getCtrlKey()) && (nativeEvent.getKeyCode()=='k' ||nativeEvent.getKeyCode()=='K')) {
					nativeEvent.preventDefault();
					if (mainPanel instanceof TabbedPanelGeneric) {
						TabbedPanelGeneric tabPanel = (TabbedPanelGeneric)mainPanel;
						int currentIndex = tabPanel.tabPanel.getTabBar().getSelectedTab();
						if (currentIndex > 0){
							tabPanel.selectTab(currentIndex - 1);
						}
						else if (currentIndex == 0) {
						}
					}
				}
			}
		});	
		// Create the server side services.
		genericDatabaseService = GWT.create(GenericDatabaseService.class);
		exportService = GWT.create(ExportService.class);
		ihmPropertiesService = GWT.create(IhmPropertiesService.class);
		configurationsDatabaseService = GWT.create(ConfigurationsDatabaseService.class);
		alarmsDatabaseService = GWT.create(AlarmsDatabaseService.class);
		synopticDatabaseService = GWT.create(SynopticDatabaseService.class);
		clockService = GWT.create(ClockService.class);
		// Set the name of the project in the Core class
		String hostPageBaseUrl = GWT.getHostPageBaseURL();
		projectName = "";
		if (hostPageBaseUrl.contains("127.0.0.1")) {
			projectName += "syncro";
		}	
		else {
			hostPageBaseUrl = hostPageBaseUrl.substring(0, hostPageBaseUrl.length()-1);
			projectName += hostPageBaseUrl.substring(hostPageBaseUrl.lastIndexOf("/")+1);
		}	
	    genericDatabaseService.setProjectName(projectName, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				Log.info("genericDatabaseService.setProjectName : ok");
				RootLayoutPanel rootPanel = RootLayoutPanel.get();
				rootPanel.clear();
				RootPanel.get().setStyleName(Css.initialBackgroundStyle);
				Log.setCurrentLogLevel(Log.LOG_LEVEL_DEBUG);
				Window.enableScrolling(true);
				getIhmParameters();
			}
			public void onFailure(Throwable caught) {
				Log.info("genericDatabaseService.setProjectName : failed "+caught.getMessage());
				new CustomDialogBox("genericDatabaseService.setProjectName : failed "+caught.getMessage(), "ok");
			}
		});
	}
	
	/**
	 * Function called once the project name has been set in the Core class. Retrieve gui parameters from the gui.properties file and store them
	 * in a IhmParameters object. Once done, call to the getDatabaseObjects() method.
	 */
	private void getIhmParameters(){
		ihmPropertiesService.getIhmParameters(new AsyncCallback<IhmParameters>() {
			@Override
			public void onSuccess(IhmParameters result) {
				Log.info("ihmPropertiesService.getIhmParameters : ok");
				ihmParameters = result;
				ImagePreloader.load(logoString, new ImageLoadHandler() {

					@Override
					public void imageLoaded(ImageLoadEvent event) {
						logo = new FitImage(logoString);
						RootPanel.get().removeStyleName(Css.initialBackgroundStyle);
						getDatabaseObjects();
					}
				});
			}
			@Override
			public void onFailure(Throwable caught) {
				Log.error("ihmPropertiesService.getIhmParameters : failed "+caught.getMessage());
				new CustomDialogBox("ihmPropertiesService.getIhmParameters : failed : "+caught.getMessage(), "ok");
			}
		});
	}
	
	/**
	 * Construct a DatabaseObject object containing data from the database and open a login dialog box
	 */
	private void getDatabaseObjects(){
		genericDatabaseService.getDatabaseObjects(new AsyncCallback<DatabaseObjects>() {
			@Override
			public void onSuccess(DatabaseObjects result) {
				Log.info("genericDatabaseService.getDatabaseObjects : ok");
				databaseObjects = result;
				databaseObjectsTools = new DatabaseObjectsTools(databaseObjects, ihmParameters);
				runDatabaseObjectTimer();
				String hostPageBaseUrl = GWT.getHostPageBaseURL();
				if (hostPageBaseUrl.contains("127.0.0.1")) {
					UserManagement.setUser(new UsrUser("karrus", "8bccde08878f2bfbb133e82b69a4bc2a", "", "administrateur"));
					UserManagement.setCredential(new UsrCredential("administrateur", "tout", "tout", "tout", "tout", "tout", 
							                                         "tout", "tout", "tout", "tout"));
					try {
						FrontalWebApp.this.onModuleLoadContinue();
					} catch (Exception e) {
						Log.error(e.getMessage());
						new CustomDialogBox("onModuleLoadContinue failed", Language.okString);
					}
				}	
				else {
					new LoginDialogBox(FrontalWebApp.this);
				}	
			}
			@Override
			public void onFailure(Throwable caught) {
				Log.error("genericDatabaseService.getDatabaseObjects : failed "+caught.getMessage());
				new CustomDialogBox("genericDatabaseService.getDatabaseObjects : failed "+caught.getMessage(), "ok");
			}
		});
	}

	/**
	 * Terminate the loading of the application
	 * @throws Exception
	 */
	public void onModuleLoadContinue() throws Exception{
		
		List<Label> menuLabels = new ArrayList<Label>();
		VerticalPanel leftPanel = new VerticalPanel();
		leftPanel.setStyleName("leftPanel");
		leftPanel.setHeight(windowHeight - 4  + "px");
		// window height minus 4 (borders heights of the main dock panel)
		SynopticMenu synopticMenu = new SynopticMenu(this);
		menuLabels.add(synopticMenu);
		Label alarmsLabel = new Label(Language.alarmMenu);
		alarmsLabel.setStyleName(Css.menuItemTitleLabel);
		menuLabels.add(alarmsLabel);
		CurrentAlarmsMenu currentAlarmsMenu = new CurrentAlarmsMenu(this);
		menuLabels.add(currentAlarmsMenu);
		AlarmsHistoryMenu alarmsHistoryMenu = new AlarmsHistoryMenu(this);
		menuLabels.add(alarmsHistoryMenu);
		Label configLabel = new Label(Language.configMenu);
		configLabel.setStyleName(Css.menuItemTitleLabel);
		menuLabels.add(configLabel);
		StationsConfigurationMenu stationsConfigurationMenu = new StationsConfigurationMenu(this);
		menuLabels.add(stationsConfigurationMenu);
		LanesConfigurationMenu lanesConfigurationMenu = new LanesConfigurationMenu(this);
		menuLabels.add(lanesConfigurationMenu);
		ItinerariesConfigurationMenu itinerariesConfigurationMenu = new ItinerariesConfigurationMenu(this);
		menuLabels.add(itinerariesConfigurationMenu);
		Label dataLabel = new Label(Language.dataMenu);
		dataLabel.setStyleName(Css.menuItemTitleLabel);
		menuLabels.add(dataLabel);
		CountDataHistoryMenu countDataHistoryMenu = new CountDataHistoryMenu(this);
		menuLabels.add(countDataHistoryMenu);
		TravelTimesDataHistoryMenu travelTimesDataHistoryMenu = new TravelTimesDataHistoryMenu(this);
		menuLabels.add(travelTimesDataHistoryMenu);
		WeatherDataHistoryMenu weatherDataHistoryMenu = new WeatherDataHistoryMenu(this);
		menuLabels.add(weatherDataHistoryMenu);
		RsuDiagnosticMenu rsuDiagnosticMenu = new RsuDiagnosticMenu(this);
		menuLabels.add(rsuDiagnosticMenu);
		V2xAlarmsMenu v2xAlarmsMenu = new V2xAlarmsMenu(this);
		menuLabels.add(v2xAlarmsMenu);
		Label systemLabel = new Label(Language.systemMenu);
		systemLabel.setStyleName(Css.menuItemTitleLabel);
		menuLabels.add(systemLabel);
		UsersMenu usersMenu = new UsersMenu(this);
		menuLabels.add(usersMenu);
		if (!UserManagement.getCredential().getSynoptic().equals(Language.noRight)) {
			leftPanel.add(synopticMenu);
			leftPanel.setCellHeight(synopticMenu, "1px");
		}
		if (!UserManagement.getCredential().getAlarms().equals(Language.noRight)) {
			leftPanel.add(alarmsLabel);
			leftPanel.setCellHeight(alarmsLabel, "1px");
			leftPanel.add(currentAlarmsMenu);
			leftPanel.setCellHeight(currentAlarmsMenu, "1px");
			leftPanel.add(alarmsHistoryMenu);
			leftPanel.setCellHeight(alarmsHistoryMenu, "1px");
		}
		if (!UserManagement.getCredential().getStation().equals(Language.noRight) || !UserManagement.getCredential().getRdtTtItineraries().equals(Language.noRight)) {
			leftPanel.add(configLabel);
			leftPanel.setCellHeight(configLabel, "1px");
		}
		if (!UserManagement.getCredential().getStation().equals(Language.noRight)) {
			leftPanel.add(stationsConfigurationMenu);
			leftPanel.setCellHeight(stationsConfigurationMenu, "1px");
			leftPanel.add(lanesConfigurationMenu);
			leftPanel.setCellHeight(lanesConfigurationMenu, "1px");
		}
		if (!UserManagement.getCredential().getRdtTtItineraries().equals(Language.noRight)) {
			leftPanel.add(itinerariesConfigurationMenu);
			leftPanel.setCellHeight(itinerariesConfigurationMenu, "1px");
		}
		if (!UserManagement.getCredential().getRdtCtData().equals(Language.noRight) || !UserManagement.getCredential().getRdtTtData().equals(Language.noRight)) {
			leftPanel.add(dataLabel);
			leftPanel.setCellHeight(dataLabel, "1px");
			}
		if (!UserManagement.getCredential().getRdtCtData().equals(Language.noRight)) {
			leftPanel.add(countDataHistoryMenu);
			leftPanel.setCellHeight(countDataHistoryMenu, "1px");
		}
		if (!UserManagement.getCredential().getRdtTtData().equals(Language.noRight)) {
			leftPanel.add(travelTimesDataHistoryMenu);
			leftPanel.setCellHeight(travelTimesDataHistoryMenu, "1px");
		}
		leftPanel.add(weatherDataHistoryMenu);
		leftPanel.setCellHeight(weatherDataHistoryMenu, "1px");
		leftPanel.add(rsuDiagnosticMenu);
		leftPanel.setCellHeight(rsuDiagnosticMenu, "1px");
		leftPanel.add(v2xAlarmsMenu);
		leftPanel.setCellHeight(v2xAlarmsMenu, "1px");
		if (!UserManagement.getCredential().getSystem().equals(Language.noRight)) {
			leftPanel.add(systemLabel);
			leftPanel.setCellHeight(systemLabel, "1px");
			leftPanel.add(usersMenu);
			leftPanel.setCellHeight(usersMenu, "1px");
		}
		// add an empty label to occupy the remaining space in the left panel
		leftPanel.add(new Label());
		AlarmsStatusBar alarmStatusBar = new AlarmsStatusBar(this);
		leftPanel.add(alarmStatusBar);
		leftPanel.setCellHeight(alarmStatusBar, "1px");
		leftPanel.setCellVerticalAlignment(alarmStatusBar, HasVerticalAlignment.ALIGN_BOTTOM);
		leftPanel.setCellHorizontalAlignment(alarmStatusBar, HasHorizontalAlignment.ALIGN_CENTER);
//		ServerInstanceBar serverInstanceBar = new ServerInstanceBar(this);
//		menuLabels.add(serverInstanceBar);
//		leftPanel.add(serverInstanceBar);
//		leftPanel.setCellHeight(serverInstanceBar, "1px");
//		leftPanel.setCellVerticalAlignment(serverInstanceBar, HasVerticalAlignment.ALIGN_BOTTOM);
		dockPanel = new DockPanel();
		dockPanel.setBorderWidth(1);
		dockPanel.setVisible(true);
		dockPanel.setWidth(windowWidth + "px");
		dockPanel.setHeight(windowHeight +"px");
		dockPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		dockPanel.add(leftPanel, DockPanel.WEST);
		ScrollPanel scrollPanel = new ScrollPanel(dockPanel);
		scrollPanel.setWidth("100%");
		scrollPanel.setHeight("100%");
		RootLayoutPanel rootPanel = RootLayoutPanel.get();
		rootPanel.add(scrollPanel);
		List<Integer> menuLabelsWidhts = new ArrayList<Integer>();
		// set the width of the logo to 1px to prevent it  
		logo.setWidth("1px");
		for (Label label : menuLabels) {
			menuLabelsWidhts.add(label.getOffsetWidth());
		}
		Integer menuPanelWidth = Collections.max(menuLabelsWidhts);
		widthForMenu = menuPanelWidth;
		logo.setWidth(widthForMenu + "px");
		leftPanel.insert(logo, 0);
		leftPanel.setCellHeight(logo, "1px");
		dockPanel.setCellWidth(leftPanel, widthForMenu + "px");
		synopticMenu.showSynoptic();
	}
	
	/** 
	 * When adding a panel, update local variables. 
	 * @param id
	 * @param composite
	 * @throws Exception 
	 */
	public void addPanel(String id, Composite composite) throws Exception {
		id2Panel.put(id, composite);
	}
	
	public Map<String, Composite> getId2Panel() {
		return id2Panel;
	}
	
	/**
	 * 
	 * @param newPanel The new panel to be shown 
	 */
	public void updateMainPanel(Composite newPanel) {
		if (mainPanel!=null) {
			this.dockPanel.remove(mainPanel);
		}	
		this.dockPanel.add(newPanel, DockPanel.CENTER);
		this.dockPanel.setStyleName("leftPanel");
		mainPanel = newPanel;
	}

	/**
	 * Updates the closable tabbed panel with a new widget.
	 * @param tabPanelId the tabbed parent panel title
	 * @param newTabbedPanel the new widget to add
	 * @param newTabbedPanelLabel the title for the tab
	 * @param closable boolean if the newPanelToAddInATab has to be closable or not.
	 * @throws Exception 
	 */
	public void addNewTabbedPanel(String tabPanelId, Composite newTabbedPanel, String newTabbedPanelLabel, boolean closable) throws Exception{
		// Get the tab panel for a given id
		Composite tabPanelAsComposite = null;
		for (String id : id2Panel.keySet()){
			if (id.equals(tabPanelId))
				tabPanelAsComposite = id2Panel.get(id);
		}
		// and add a new tab
		if (tabPanelAsComposite instanceof TabbedPanelGeneric){
			TabbedPanelGeneric tabPanel = (TabbedPanelGeneric)tabPanelAsComposite;
			if (closable) {
				tabPanel.addClosableTabbedPanel(newTabbedPanel, newTabbedPanelLabel);
			}	
			else {
				tabPanel.addTabPanel(newTabbedPanel, newTabbedPanelLabel);
			}	
		}
	}

	/**
	 * Create a timer that updates the databaseObjects variable 
	 */
	private void runDatabaseObjectTimer(){
		Timer t = new Timer() {
			@Override
			public void run() {
				updateDatabaseObject();
			}
		};
		t.scheduleRepeating(60*1000);
	}
	
	/**
	 * Update the object databaseObjects
	 */
	public static void updateDatabaseObject(){
		genericDatabaseService.getDatabaseObjects(new AsyncCallback<DatabaseObjects>() {
			@Override
			public void onSuccess(DatabaseObjects result) {
				Log.info("genericDatabaseService.getDatabaseObjects : ok");
				databaseObjects = result;
				databaseObjectsTools = new DatabaseObjectsTools(databaseObjects, ihmParameters);
			}
			@Override
			public void onFailure(Throwable caught) {
				Log.info("genericDatabaseService.getDatabaseObjects : failed "+caught.getMessage());
				new CustomDialogBox("genericDatabaseService.getDatabaseObjects : failed "+caught.getMessage(), "ok");
			}
		});
	}
	
	/**
	 * Gets the height for table.
	 * @return int
	 */
	public static int getHeightForSensysAPTable(){
		return windowHeight*35/100;
	}
	
	/**
	 * Gets the width for main panel.
	 * @return int
	 */
	public static int getWidthForMainPanel(){
        // window width minus 6 (borders widths of the main dock panel) minus 12 (the padding in the tab panel deck panel)
		return windowWidth-widthForMenu - 6 - 12;
	}
	
	/**
	 * Gets the height for main panel.
	 * @return double
	 */
	// window height minus 26 (tab bar height) minus 4 (borders heights of the main dock panel) minus 12 (the padding in the tab panel deck panel)
	public static double getHeightForMainPanel(){
		return windowHeight - 26 - 4 - 12;
	}
	
	public static int getWidthForBorderedParametersWidgets() {
		return getWidthForMainPanel() - 10 ;
	}

	/**
	 * Gets the height for table.
	 * @return int
	 */
	public static int getHeightForTable(){
		return windowHeight*88/100;
	}

	/**
	 * Gets the height for sensor table.
	 * @return int
	 */
	public static int getHeightForSensorsTable(){
		return windowHeight*85/100;
	}

	/**
	 * Gets the height for table.
	 * @return int
	 */
	public static int getHeightForStationsTable(){
		return windowHeight*80/100;
	}

	public static int getHeightForBigTable(){
		return windowHeight*85/100;
	}
	
	/**
	 * Get the height for alarms table in notification menu
	 * @return int
	 */
	public static int getHeightForAlarmsTableInNotificationMenu(){
		return windowHeight*50/100;
	}	
	
	/**
	 * Gets the height for menu.
	 * @return double
	 */
	public double getHeightForMenu(){
		return (windowHeight-300)/4;
	}

	public Composite getPanelFromId(String id){
		return id2Panel.get(id);
	}

	public static String getSelectedId(){
		Set<String> idsList = id2Panel.keySet();
		Iterator<String> iterator = idsList.iterator();
		while (iterator.hasNext()){
			String id = iterator.next();
			if (id2Panel.get(id)==mainPanel)
				return id;
		}
		return null;
	}

	public static double getHeightForDashboardInTabPanel(){
		return windowHeight*30/100;
	}

	/**
	 * Gets the width for indicator plot.
	 * @return int
	 */
	public static int getWidthForIndicatorPlot(){
		return getWidthForMainPanel()/2-100;
	}
	
	public void setDockPanelVisible(boolean visible) {
		this.dockPanel.setVisible(visible);
	}
	
}
