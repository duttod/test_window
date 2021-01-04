package karrus.client.synoptic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import karrus.client.FrontalWebApp;
import karrus.client.appearance.Css;
import karrus.client.event.ServerClockEvent;
import karrus.client.synoptic.svgElements.SvgAlarm;
import karrus.client.synoptic.svgElements.SvgAlarmsTimer;
import karrus.client.synoptic.svgElements.SvgElement;
import karrus.client.synoptic.svgElements.SvgTtTimer;
import karrus.client.synoptic.svgElements.SvgTtColor;
import karrus.client.synoptic.svgElements.SvgRdtColor;
import karrus.client.synoptic.svgElements.SvgRdtTimer;
import karrus.client.synoptic.svgElements.SvgRdtDisplay;
import karrus.client.synoptic.svgElements.SvgTtDisplay;
import karrus.client.synoptic.svgElements.SvgWeatherDisplay;
import karrus.client.synoptic.svgElements.SvgWeatherStation;
import karrus.client.synoptic.svgElements.SvgWeatherTimer;
import karrus.client.utils.DateTime;
import karrus.shared.hibernate.CtLane;
import karrus.shared.hibernate.RsuStation;
import karrus.shared.hibernate.SynAlarm;
import karrus.shared.hibernate.SynRdtColor;
import karrus.shared.hibernate.SynRdtDisplay;
import karrus.shared.hibernate.SynTtColor;
import karrus.shared.hibernate.SynTtDisplay;
import karrus.shared.hibernate.SynWeatherDisplay;
import karrus.shared.hibernate.SynWeatherStation;
import karrus.shared.hibernate.TtItinerary;
import karrus.shared.language.Language;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ObjectElement;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.client.event.RemoteEventService;
import de.novanic.eventservice.client.event.RemoteEventServiceFactory;
import de.novanic.eventservice.client.event.listener.RemoteEventListener;

public abstract class SynopticPanel extends Composite {

	private String svgPath;
	protected String htmlObjectId;
	protected VerticalPanel verticalPanel;
	private AbsolutePanel svgObjectAbsolutePanel;
	private Label timeLabel = new Label();
	private String timestampOfLastServerMessage = "";
	private RemoteEventService theRemoteEventService;
	private ObjectElement objectElement;
	private Widget w;
	private int initialXPosition = 0;
	private int initialYPosition = 0;	
	private List<SvgAlarm> alarms = new ArrayList<SvgAlarm>();
	private List<SvgRdtColor> rdtColors = new ArrayList<SvgRdtColor>();
	private List<SvgRdtDisplay> rdtDisplays = new ArrayList<SvgRdtDisplay>();
	private List<SvgTtColor> ttColors = new ArrayList<SvgTtColor>();
	private List<SvgTtDisplay> ttDisplays = new ArrayList<SvgTtDisplay>();
	private List<SvgWeatherStation> weatherStations = new ArrayList<SvgWeatherStation>();
	private List<SvgWeatherDisplay> weatherDisplays = new ArrayList<SvgWeatherDisplay>();
	private List<SvgElement> svgElementsList = new ArrayList<SvgElement>();
	private SvgTtTimer svgTtTimer;
	private SvgRdtTimer svgRdtTimer;
	private SvgWeatherTimer svgWeatherTimer;
	private SvgAlarmsTimer svgAlarmsTimer;
	private double svgObjectAbsolutePanelWidth = FrontalWebApp.getWidthForMainPanel() - 2;
	private double svgObjectAbsolutePanelHeight =  FrontalWebApp.getHeightForMainPanel() - 40 - 2;
	
	
	public SynopticPanel(String svgPath, String htmlObjectId) {
		this.svgPath = svgPath;
		this.htmlObjectId = htmlObjectId;
		this.verticalPanel = new VerticalPanel();
		this.initWidget(verticalPanel);
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		timeLabel.setStyleName(Css.timeLabelStyle);
		verticalPanel.add(timeLabel);
		// Force the cell containing the time label to be of minimum size (i.e the size of the label) 
		verticalPanel.setCellHeight(timeLabel, "5px");
		verticalPanel.setCellHorizontalAlignment(timeLabel, HasHorizontalAlignment.ALIGN_LEFT);
		// Create an object element to put the svg in
		objectElement = Document.get().createObjectElement();
		objectElement.setType("image/svg+xml");
		objectElement.setData(svgPath);
		objectElement.setId(htmlObjectId);
		// Attach the object element to the document before wrapping it in an HTML panel
		Document.get().getBody().appendChild(objectElement);
		w = HTMLPanel.wrap(objectElement);
		svgObjectAbsolutePanel = new AbsolutePanel();
		svgObjectAbsolutePanel.add(w, 0, 0);
		svgObjectAbsolutePanel.setSize(svgObjectAbsolutePanelWidth + "px", svgObjectAbsolutePanelHeight + "px");
		svgObjectAbsolutePanel.addStyleName(Css.borderStyle3);
//        w.addStyleName(Css.borderStyle2);
		verticalPanel.add(svgObjectAbsolutePanel);
		addLoadHandlers();
		verticalPanel.setSize(FrontalWebApp.getWidthForMainPanel() + "px", FrontalWebApp.getHeightForMainPanel() + "px");
		theRemoteEventService = RemoteEventServiceFactory.getInstance().getRemoteEventService();
		//add a listener to the SERVER_MESSAGE_DOMAIN
		theRemoteEventService.addListener(ServerClockEvent.SERVER_MESSAGE_DOMAIN, new RemoteEventListener() {
		    public void apply(Event anEvent) {
		        if(anEvent instanceof ServerClockEvent) {
		        	ServerClockEvent theServerGeneratedMessageEvent = (ServerClockEvent)anEvent;
		        	timeLabel.setStyleName(Css.timeLabelStyle);
		            timeLabel.setText(theServerGeneratedMessageEvent.getServerGeneratedMessage());
		            timestampOfLastServerMessage = DateTime.timeFormat.format(new Date());
		        }
		    }
		});
		svgElementsList.clear();
		List<SynAlarm> synAlarms = FrontalWebApp.databaseObjects.getSynAlarms();
		alarms.clear();	
		for (SynAlarm synAlarm : synAlarms) {
			if (synAlarm.getId().getSynopticId().equals(htmlObjectId)) {
				SvgAlarm svgAlarm = new SvgAlarm(synAlarm.getSource(), synAlarm.getType(), synAlarm.getId().getItemId(), htmlObjectId, synAlarm.getType().equals("") ? true : false);
				alarms.add(svgAlarm);
			}
		}	
		svgElementsList.addAll(alarms);
		List<SynRdtColor> synRdtColors = FrontalWebApp.databaseObjects.getSynRdtColors();
		rdtColors.clear();
		for (SynRdtColor synRdtColor : synRdtColors) {
			if (synRdtColor.getId().getSynopticId().equals(htmlObjectId)) {
				for (CtLane lane : FrontalWebApp.databaseObjects.getLanes()) {
					if (lane.getId().getStation().equals(synRdtColor.getStation()) && lane.getId().getLane().equals(synRdtColor.getLane())) {
						SvgRdtColor svgRdtColor = new SvgRdtColor(lane, synRdtColor.getId().getItemId(), htmlObjectId);
						rdtColors.add(svgRdtColor);
						break;
					}
				}
			}
		}
		svgElementsList.addAll(rdtColors);
		List<SynTtColor> synTtColors = FrontalWebApp.databaseObjects.getSynTtColors();
		ttColors.clear();	
		for (SynTtColor synTtColor : synTtColors) {
			if (synTtColor.getId().getSynopticId().equals(htmlObjectId)) {
				for (TtItinerary itinerary : FrontalWebApp.databaseObjects.getBtItineraries()) {
					if (itinerary.getId().getOrigin().equals(synTtColor.getOrigin()) && itinerary.getId().getDestination().equals(synTtColor.getDestination())) {
						SvgTtColor svgTtColor = new SvgTtColor(itinerary, synTtColor.getId().getItemId(), htmlObjectId);
						ttColors.add(svgTtColor);
						break;
					}
				}
			}
		}	
		svgElementsList.addAll(ttColors);
		List<SynRdtDisplay> synRdtDisplays = FrontalWebApp.databaseObjects.getSynRdtDisplays();
		rdtDisplays.clear();	
		for (SynRdtDisplay synRdtDisplay : synRdtDisplays) {
			if (synRdtDisplay.getId().getSynopticId().equals(htmlObjectId)) {
				for (RsuStation rsuStation : FrontalWebApp.databaseObjects.getStations()) {
					if (rsuStation.getId().equals(synRdtDisplay.getStation())) {
						SvgRdtDisplay svgRdtDisplay = new SvgRdtDisplay(synRdtDisplay, synRdtDisplay.getId().getItemId(), htmlObjectId);
						rdtDisplays.add(svgRdtDisplay);
						break;
					}
				}
			}
		}	
		svgElementsList.addAll(rdtDisplays);
		List<SynTtDisplay> synTtDisplays = FrontalWebApp.databaseObjects.getSynTtDisplays();
		ttDisplays.clear();	
		for (SynTtDisplay synTtDisplay : synTtDisplays) {
			if (synTtDisplay.getId().getSynopticId().equals(htmlObjectId)) {
				for (TtItinerary itinerary : FrontalWebApp.databaseObjects.getBtItineraries()) {
					if (itinerary.getId().getOrigin().equals(synTtDisplay.getOrigin()) && itinerary.getId().getDestination().equals(synTtDisplay.getDestination())) {
						SvgTtDisplay svgTtDisplay = new SvgTtDisplay(synTtDisplay, synTtDisplay.getId().getItemId(), htmlObjectId);
						ttDisplays.add(svgTtDisplay);
						break;
					}
				}
			}
		}	
		svgElementsList.addAll(ttDisplays);
		List<SynWeatherStation> synWeatherStations = FrontalWebApp.databaseObjects.getSynWeatherStations();
		weatherStations.clear();	
		for (SynWeatherStation synWeatherStation : synWeatherStations) {
			if (synWeatherStation.getId().getSynopticId().equals(htmlObjectId)) {
				for (RsuStation rsuStation : FrontalWebApp.databaseObjects.getStations()) {
					if (rsuStation.getId().equals(synWeatherStation.getStation())) {
						SvgWeatherStation svgWeatherStation = new SvgWeatherStation(rsuStation, synWeatherStation.getId().getItemId(), htmlObjectId);
						weatherStations.add(svgWeatherStation);
						break;
					}
				}
			}
		}	
		svgElementsList.addAll(weatherStations);
		List<SynWeatherDisplay> synWeatherDisplays = FrontalWebApp.databaseObjects.getSynWeatherDisplays();
		weatherDisplays.clear();	
		for (SynWeatherDisplay synWeatherDisplay : synWeatherDisplays) {
			if (synWeatherDisplay.getId().getSynopticId().equals(htmlObjectId)) {
				for (RsuStation rsuStation : FrontalWebApp.databaseObjects.getStations()) {
					if (rsuStation.getId().equals(synWeatherDisplay.getStation())) {
						SvgWeatherDisplay svgWeatherDisplay = new SvgWeatherDisplay(synWeatherDisplay, synWeatherDisplay.getId().getItemId(), htmlObjectId);
						weatherDisplays.add(svgWeatherDisplay);
						break;
					}
				}
			}
		}	
		svgElementsList.addAll(weatherDisplays);
		svgRdtTimer = new SvgRdtTimer(rdtColors, rdtDisplays);
		svgTtTimer = new SvgTtTimer(ttColors, ttDisplays);
		svgWeatherTimer = new SvgWeatherTimer(weatherDisplays);
		svgAlarmsTimer = new SvgAlarmsTimer(alarms);
		verticalPanel.setVisible(true);
	}
	
	
	/**
	 * native method that adds an onload and an unload listener to the object element 
	 */
	private native void addLoadHandlers() /*-{
		var that = this;
		loadSynoptic = function() {
			function unloadSynoptic() {
				@com.allen_sauer.gwt.log.client.Log::debug(Ljava/lang/String;)('Onunload' + that.@karrus.client.synoptic.SynopticPanel::htmlObjectId);
				that.@karrus.client.synoptic.SynopticPanel::stopTimers()();
				that.@karrus.client.synoptic.SynopticPanel::onUnLoadSynoptic()();
			}	
			var svgDoc = $doc.getElementById(that.@karrus.client.synoptic.SynopticPanel::htmlObjectId).contentDocument.getElementsByTagName('svg')[0];
			var svgElement = $doc.getElementById(that.@karrus.client.synoptic.SynopticPanel::htmlObjectId).contentDocument.getElementsByTagName('svg')[0];
			var svgWidth = svgElement.width.baseVal.value;
			var svgHeight = svgElement.height.baseVal.value;
			svgElement.viewBox.baseVal.width = svgWidth;
			svgElement.viewBox.baseVal.height = svgHeight;
			var windowX = 0;
			var windowY = 0;
			var windowWidth = svgWidth;
			var windowHeight = svgHeight;
			if ($doc.getElementById(that.@karrus.client.synoptic.SynopticPanel::htmlObjectId).contentDocument.getElementById('window') != null) {
				var windowX = $doc.getElementById(that.@karrus.client.synoptic.SynopticPanel::htmlObjectId).contentDocument.getElementById('window').x.baseVal.value;
				var windowY = $doc.getElementById(that.@karrus.client.synoptic.SynopticPanel::htmlObjectId).contentDocument.getElementById('window').y.baseVal.value; 
				var windowWidth = $doc.getElementById(that.@karrus.client.synoptic.SynopticPanel::htmlObjectId).contentDocument.getElementById('window').width.baseVal.value;
				var windowHeight = $doc.getElementById(that.@karrus.client.synoptic.SynopticPanel::htmlObjectId).contentDocument.getElementById('window').height.baseVal.value;
			}
			var initialX = 0;
			var initialY = 0;
			var coefficient = 0.;
			if ((that.@karrus.client.synoptic.SynopticPanel::svgObjectAbsolutePanelWidth)/windowWidth < (that.@karrus.client.synoptic.SynopticPanel::svgObjectAbsolutePanelHeight)/svgHeight) {
				coefficient = (that.@karrus.client.synoptic.SynopticPanel::svgObjectAbsolutePanelHeight)/windowHeight;
			}
			else {
				coefficient = (that.@karrus.client.synoptic.SynopticPanel::svgObjectAbsolutePanelWidth)/svgWidth;
			}
			if (that.@karrus.client.synoptic.SynopticPanel::htmlObjectId == 'servers') {
				if ((that.@karrus.client.synoptic.SynopticPanel::svgObjectAbsolutePanelWidth)/windowWidth < (that.@karrus.client.synoptic.SynopticPanel::svgObjectAbsolutePanelHeight)/svgHeight) {
					coefficient = (that.@karrus.client.synoptic.SynopticPanel::svgObjectAbsolutePanelWidth)/svgWidth;
				}
				else {
					coefficient = (that.@karrus.client.synoptic.SynopticPanel::svgObjectAbsolutePanelHeight)/windowHeight;
				}
			}		
			initialX = Math.round((that.@karrus.client.synoptic.SynopticPanel::svgObjectAbsolutePanelWidth - (windowWidth + 2*windowX)*coefficient)/2);
			initialY = Math.round((that.@karrus.client.synoptic.SynopticPanel::svgObjectAbsolutePanelHeight - (windowHeight + 2*windowY)*coefficient)/2);	
			object.setAttribute("width", svgWidth*coefficient);
			object.setAttribute("height", svgHeight*coefficient);
			@com.allen_sauer.gwt.log.client.Log::debug(Ljava/lang/String;)('Onload' + that.@karrus.client.synoptic.SynopticPanel::htmlObjectId);
			that.@karrus.client.synoptic.SynopticPanel::startTimers()();
			that.@karrus.client.synoptic.SynopticPanel::initSvgElements()();
			var svgDoc = $doc.getElementById(that.@karrus.client.synoptic.SynopticPanel::htmlObjectId).contentDocument;
			svgDoc.defaultView.addEventListener("unload", function() {unloadSynoptic()}, false);
			that.@karrus.client.synoptic.SynopticPanel::onLoadSynoptic()();
			$wnd.myGlobalVariable = false;
			$wnd.X = 0;
			$wnd.Y = 0;	
			that.@karrus.client.synoptic.SynopticPanel::resetPosition(II)(initialX,initialY);
			var background = svgDoc.getElementById('background');
			if (background != null) {
			    background.addEventListener("mousedown", function(evt) {$wnd.myGlobalVariable = true;$wnd.X = evt.screenX;$wnd.Y = evt.screenY;that.@karrus.client.synoptic.SynopticPanel::setInitialPositions()();}, false);
				background.addEventListener("mouseup", function() {$wnd.myGlobalVariable = false;}, false);
				background.addEventListener("mouseleave", function() {$wnd.myGlobalVariable = false;}, false);
				background.addEventListener("mousemove", function(evt) {this.style.cursor='move';var deltaX = (evt.screenX - $wnd.X);var deltaY = (evt.screenY - $wnd.Y);if ($wnd.myGlobalVariable == true) {that.@karrus.client.synoptic.SynopticPanel::shiftWidget2(II)(deltaX,deltaY)};if (that.@karrus.client.synoptic.SynopticPanel::getObjectTop()() + evt.clientY < 0 || that.@karrus.client.synoptic.SynopticPanel::getObjectLeft()() + evt.clientX < 0 || that.@karrus.client.synoptic.SynopticPanel::getObjectLeft()() + evt.clientX > that.@karrus.client.synoptic.SynopticPanel::svgObjectAbsolutePanelWidth || that.@karrus.client.synoptic.SynopticPanel::getObjectTop()() + evt.clientY > that.@karrus.client.synoptic.SynopticPanel::svgObjectAbsolutePanelHeight) {background.style.cursor='default';$wnd.myGlobalVariable = false;};}, false);
				background.addEventListener("dblclick", function(evt) {object.setAttribute("width", svgWidth*coefficient);object.setAttribute("height", svgHeight*coefficient);that.@karrus.client.synoptic.SynopticPanel::resetPosition(II)(initialX,initialY);}, false);
				background.addEventListener("wheel", function(evt) {var deltaY = evt.deltaY;var X = evt.clientX;var Y = evt.clientY;that.@karrus.client.synoptic.SynopticPanel::wheelZoom(III)(deltaY, X, Y);}, false);
			}		}
		var object = that.@karrus.client.synoptic.SynopticPanel::objectElement;
		object.onload = loadSynoptic;
	}-*/;
	
	abstract void onLoadSynoptic();
	
	abstract void onUnLoadSynoptic();
	
	public void setDisconnected() {
		timeLabel.setStyleName(Css.disconnectionStyle);
		timeLabel.setText(Language.disconnectedString);
	}
	
	public String getTimestampOfLastServerMessage() {
		return timestampOfLastServerMessage;
	}
	
	private void startTimers() {
		svgRdtTimer.scheduleRepeating(20000);
		svgRdtTimer.run();
		svgTtTimer.scheduleRepeating(20000);
		svgTtTimer.run();
		svgWeatherTimer.scheduleRepeating(20000);
		svgWeatherTimer.run();
		svgAlarmsTimer.scheduleRepeating(20000);
		svgAlarmsTimer.run();
	}
	
	private void stopTimers() {
		svgRdtTimer.cancel();
		svgTtTimer.cancel();
		svgWeatherTimer.cancel();
		svgAlarmsTimer.cancel();
	}
	
	private void initSvgElements() {
		for (SvgElement svgElement : svgElementsList) {
			svgElement.jsInit();
		}
	}
	
	public void shiftWidget(int deltaX, int deltaY) {
		if (/*secondaryAbsolutePanel.getWidgetLeft(w) + deltaX >=-10000 && secondaryAbsolutePanel.getWidgetTop(w) + deltaY >=-10000*/ true) {
			svgObjectAbsolutePanel.setWidgetPosition(w, svgObjectAbsolutePanel.getWidgetLeft(w) + deltaX, svgObjectAbsolutePanel.getWidgetTop(w) + deltaY);
		}
	}
	
	public void shiftWidget2(int deltaX, int deltaY) {
		svgObjectAbsolutePanel.setWidgetPosition(w, initialXPosition + deltaX, initialYPosition + deltaY);
		checkSynopticBoundaries();
	}
	
	public void checkSynopticBoundaries() {
		if (svgObjectAbsolutePanel.getWidgetLeft(w) < (svgObjectAbsolutePanel.getOffsetWidth() - objectElement.getOffsetWidth())) {
			svgObjectAbsolutePanel.setWidgetPosition(w, (svgObjectAbsolutePanel.getOffsetWidth() - objectElement.getOffsetWidth()), svgObjectAbsolutePanel.getWidgetTop(w));
		}
		if (svgObjectAbsolutePanel.getWidgetTop(w) < (svgObjectAbsolutePanel.getOffsetHeight() - objectElement.getOffsetHeight())) {
			svgObjectAbsolutePanel.setWidgetPosition(w, svgObjectAbsolutePanel.getWidgetLeft(w), (svgObjectAbsolutePanel.getOffsetHeight() - objectElement.getOffsetHeight()));
		}
		if (svgObjectAbsolutePanel.getWidgetLeft(w) > 0) {
			svgObjectAbsolutePanel.setWidgetPosition(w, 0, svgObjectAbsolutePanel.getWidgetTop(w) );
		}
		if (svgObjectAbsolutePanel.getWidgetTop(w) > 0) {
			svgObjectAbsolutePanel.setWidgetPosition(w, svgObjectAbsolutePanel.getWidgetLeft(w), 0);
		}
	}
	
	public native void wheelZoom(int wheelVelocity, int X, int Y) /*-{
		var that = this;
		var object = $doc.getElementById(that.@karrus.client.synoptic.SynopticPanel::htmlObjectId);
		if (( object.width*(1 - wheelVelocity/(10*Math.abs(wheelVelocity))) >= that.@karrus.client.synoptic.SynopticPanel::svgObjectAbsolutePanelWidth) & (object.height*(1 - wheelVelocity/(10*Math.abs(wheelVelocity))) >= that.@karrus.client.synoptic.SynopticPanel::svgObjectAbsolutePanelHeight)) {
			object.width = object.width*(1 - wheelVelocity/(10*Math.abs(wheelVelocity)));
			object.height = object.height*(1 - wheelVelocity/(10*Math.abs(wheelVelocity)));
			var shiftX = (1 - (1 - wheelVelocity/(10*Math.abs(wheelVelocity))))*X;
			var shiftY = (1 - (1 - wheelVelocity/(10*Math.abs(wheelVelocity))))*Y;
			that.@karrus.client.synoptic.SynopticPanel::shiftWidget(II)(Math.round(shiftX), Math.round(shiftY));
			that.@karrus.client.synoptic.SynopticPanel::checkSynopticBoundaries()();	
		}
		else {
			if ((that.@karrus.client.synoptic.SynopticPanel::svgObjectAbsolutePanelWidth)/(object.width) < (that.@karrus.client.synoptic.SynopticPanel::svgObjectAbsolutePanelHeight)/(object.height)) {
				coefficient = (that.@karrus.client.synoptic.SynopticPanel::svgObjectAbsolutePanelHeight)/(object.height);
			}
			else {
				coefficient = (that.@karrus.client.synoptic.SynopticPanel::svgObjectAbsolutePanelWidth)/(object.width);
			}
			object.width = object.width*coefficient;
			object.height = object.height*coefficient;
			var shiftX = (1 - coefficient)*X;
			var shiftY = (1 - coefficient)*Y;
			if (coefficient != 1) {
				that.@karrus.client.synoptic.SynopticPanel::shiftWidget(II)(Math.round(shiftX), Math.round(shiftY));
				that.@karrus.client.synoptic.SynopticPanel::checkSynopticBoundaries()();
			}	
		}	
		
	}-*/;
	
	private void resetPosition(int X, int Y) {
		svgObjectAbsolutePanel.setWidgetPosition(w, X, Y);
	}
	
	private void setInitialPositions() {
		this.initialXPosition = svgObjectAbsolutePanel.getWidgetLeft(w);
		this.initialYPosition = svgObjectAbsolutePanel.getWidgetTop(w);
	}
	
	private int getObjectLeft(){
		return svgObjectAbsolutePanel.getWidgetLeft(w);
	}
	
	private int getObjectTop(){
      	return svgObjectAbsolutePanel.getWidgetTop(w);
	}
}
