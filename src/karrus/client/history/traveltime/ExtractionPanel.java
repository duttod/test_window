package karrus.client.history.traveltime;

import java.util.ArrayList;
import java.util.List;

import karrus.shared.language.Language;
import karrus.client.appearance.Css;
import karrus.client.generic.ListBoxGeneric;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ExtractionPanel extends Composite {
	
	private ListBoxGeneric travelTimePeriodListBox;
	private ListBoxGeneric travelTimeHorizonListBox;
	private RadioButton travelTimeTimestampMiddleOfIntervalRadioButton;
	private RadioButton travelTimeTimestampEndOfIntervalRadioButton;
	
	/**
	 * Constructor.
	 */
	public ExtractionPanel(ClickHandler plotTravelTimesClickHandler, ClickHandler individualTravelTimesExportClickHandler,
			               ClickHandler statisticalTravelTimesExportClickHandler) {
		// Widgets
		Label plotLabel = new Label(Language.plotLabel);
		plotLabel.setStyleName(Css.boldStyle);
		Button plotTravelTimesButton = new Button(Language.displayString);
		plotTravelTimesButton.addClickHandler(plotTravelTimesClickHandler);
		Label travelTimeStatisticalDataLabel = new Label(Language.travelTimeStatisticalDataString);
		travelTimeStatisticalDataLabel.setStyleName(Css.boldStyle);
		List<String> travelTimePeriodValues = new ArrayList<String>();
		travelTimePeriodValues.add("1");
		travelTimePeriodValues.add("2");
		travelTimePeriodValues.add("5");
		travelTimePeriodValues.add("6");
		travelTimePeriodValues.add("10");
		travelTimePeriodValues.add("15");
		travelTimePeriodValues.add("20");
		travelTimePeriodValues.add("30");
		travelTimePeriodValues.add("60");
		String travelTimePeriodSelectedItem = "1";
		travelTimePeriodListBox = new ListBoxGeneric (Language.travelTimePeriodString, travelTimePeriodValues, travelTimePeriodSelectedItem);
		List<String> travelTimeHorizonValues = new ArrayList<String>();
		travelTimeHorizonValues.add("1");
		travelTimeHorizonValues.add("2");
		travelTimeHorizonValues.add("5");
		travelTimeHorizonValues.add("6");
		travelTimeHorizonValues.add("10");
		travelTimeHorizonValues.add("15");
		travelTimeHorizonValues.add("20");
		travelTimeHorizonValues.add("30");
		travelTimeHorizonValues.add("60");
		String travelTimeHorizonSelectedItem = "2";
		travelTimeHorizonListBox = new ListBoxGeneric (Language.travelTimeHorizonString, travelTimeHorizonValues, travelTimeHorizonSelectedItem);
		Label travelTimeTimestampLabel = new Label(Language.timestampString);
		travelTimeTimestampMiddleOfIntervalRadioButton = new RadioButton("timestampInterval", Language.travelTimeTimestampMiddleOfIntervalString);
		travelTimeTimestampEndOfIntervalRadioButton = new RadioButton("timestampInterval", Language.travelTimeTimestampEndOfIntervalString);
		travelTimeTimestampMiddleOfIntervalRadioButton.setValue(true);
		Button statisticalTravelTimesExportButton = new Button(Language.CSVexportString);
		statisticalTravelTimesExportButton.addClickHandler(statisticalTravelTimesExportClickHandler);
		Label travelTimeIndividualDataLabel = new Label(Language.travelTimeIndividualDataString);
		travelTimeIndividualDataLabel.setStyleName(Css.boldStyle);
		Button individualTravelTimesExportButton = new Button(Language.CSVexportString);
		individualTravelTimesExportButton.addClickHandler(individualTravelTimesExportClickHandler);
		// Layout
		FlexTable travelTimePlotFlextable = new FlexTable();
		travelTimePlotFlextable.setWidget(0, 0, plotTravelTimesButton);
		FlexTable travelTimeStatisticalDataFlexTable = new FlexTable();
		travelTimeStatisticalDataFlexTable.setCellSpacing(5);
		travelTimeStatisticalDataFlexTable.setWidget(0, 0, travelTimePeriodListBox);
		travelTimeStatisticalDataFlexTable.setWidget(0, 1, travelTimeHorizonListBox);
		travelTimeStatisticalDataFlexTable.setWidget(0, 2, travelTimeTimestampLabel);
		travelTimeStatisticalDataFlexTable.setWidget(0, 3, travelTimeTimestampMiddleOfIntervalRadioButton);
		travelTimeStatisticalDataFlexTable.setWidget(0, 4, travelTimeTimestampEndOfIntervalRadioButton);
		travelTimeStatisticalDataFlexTable.setWidget(0, 5, statisticalTravelTimesExportButton);
		FlexTable travelTimeIndividualDataFlexTable = new FlexTable();
		travelTimeIndividualDataFlexTable.setWidget(0, 0, individualTravelTimesExportButton);
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setVerticalAlignment(VerticalPanel.ALIGN_TOP);
		verticalPanel.setHorizontalAlignment(VerticalPanel.ALIGN_LEFT);
		verticalPanel.add(plotLabel);
		verticalPanel.add(travelTimePlotFlextable);
		verticalPanel.add(travelTimeStatisticalDataLabel);
		verticalPanel.add(travelTimeStatisticalDataFlexTable);
		verticalPanel.add(travelTimeIndividualDataLabel);
		verticalPanel.add(travelTimeIndividualDataFlexTable);
		this.initWidget(verticalPanel);
		
	}

	/**
	 * Getter.
	 * @return Period in minutes.
	 */
	public int getPeriod() {
		return Integer.parseInt(travelTimePeriodListBox.getSelectedValue());
	}
	
	/**
	 * Getter.
	 * @return Horizon in minutes.
	 */
	public int getHorizon() {
		return Integer.parseInt(travelTimeHorizonListBox.getSelectedValue());
	}
	
	/**
	 * Getter.
	 * @return The way travel times are time-stamped.
	 */
	public String getTypeOfTimestamp() {
		if (travelTimeTimestampMiddleOfIntervalRadioButton.getValue())
			return Language.travelTimeTimestampMiddleOfIntervalString;
		else if (travelTimeTimestampEndOfIntervalRadioButton.getValue())
			return Language.travelTimeTimestampEndOfIntervalString;
		else 
			return null; 
	}
}
