package karrus.client.generic.datetime;


import java.util.Date;

import karrus.client.appearance.Css;
import karrus.client.appearance.LayoutInfo;
import karrus.client.utils.DateTime;
import karrus.shared.language.Language;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.google.gwt.user.datepicker.client.DateBox;


/**
 * Creates a horizontal panel with a date picker.
 * 
 * @author Estelle DUMAS: estelle.dumas@karrus-its.com
 */
public class DatePickerGeneric extends Composite {

	private DateBox dateBox;
	private Label label;
	private String text;
	private boolean copyDateToAnotherDatePickerGeneric = false;
	private DatePickerGeneric otherDatePickerGeneric;
	private boolean roundToPreviousWeek = false;
	private boolean roundToNextWeek = false;

	/**
	 * Constructor: creates a panel with the label 't' and the default date 'date'.
	 * @param text string label
	 * @param date default date
	 */
	public DatePickerGeneric(String text, Date date) {
		this.text = text;
		String dateAsString = null;
		if (date!=null) {
			dateAsString = DateTime.frenchDateFormat.format(date);
		}
		this.label = new Label(text+" :");
		dateBox = new DateBox();
		dateBox.setWidth(LayoutInfo.textBoxWidth);
		if (dateAsString!=null) {
			dateBox.setFormat(new DateBox.DefaultFormat(DateTime.frenchDateFormat));
			dateBox.setValue(DateTime.frenchDateFormat.parse(dateAsString), true);
		}
		FlexTable flexTable = new FlexTable();
		flexTable.setWidget(0, 0, label);
		flexTable.setWidget(0, 1, dateBox);
		this.initWidget(flexTable);
		dateBox.addValueChangeHandler(new ValueChangeHandler<Date>() {
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				setDate(dateBox.getValue());
				DatePickerGeneric.this.getDatePicker().removeStyleName(Css.greyFont);
				if (copyDateToAnotherDatePickerGeneric) {
					if (otherDatePickerGeneric!=null)
						try {
							otherDatePickerGeneric.setDate(getDate());
							otherDatePickerGeneric.getDatePicker().addStyleName(Css.greyFont);
						} catch (Exception e) {
							Log.error("error in onValueChanged in DatePickerGeneric :"+e.getMessage());
					}
				}
			}
		});
	}


	/**
	 * Sets the width of the date box.
	 * @param width String
	 */
	public void setDateBoxWidth(String width){
		dateBox.setWidth(width);
	}


	public void roundToPreviousWeek(){
		this.roundToPreviousWeek = true;
	}

	public void roundToNextWeek(){
		this.roundToNextWeek = true;
	}


	/**
	 * Sets the date in the text box.
	 * @param date
	 */
	@SuppressWarnings("deprecation")
	public void setDate(Date date){
		String d = null;
		if (date!=null)
			d = DateTime.frenchDateFormat.format(date);

		if (d!=null) {
			if (roundToPreviousWeek) {
				if (date.getDay()==0)
					CalendarUtil.addDaysToDate(date, -6);
				else
					CalendarUtil.addDaysToDate(date, -date.getDay()+1);
			}
			if (roundToNextWeek){
				if (date.getDay()!=0)
					CalendarUtil.addDaysToDate(date, 7-date.getDay());
			}
			dateBox.setValue(date, true);
		}
	}

	/**
	 * @return the label string
	 */
	public String getLabel(){
		return this.label.getText();
	}

	/**
	 * @return the date picker object
	 */
	public DateBox getDatePicker(){
		return this.dateBox;
	}


	/**
	 * Gets the date from the text box.
	 * @return Date
	 * @throws Exception if the date is not correct or empty
	 */
	public Date getDate() throws Exception {
		if (dateBox.getTextBox().getText().trim().equals(Language.emptyString)) {
			throw new Exception(Language.errorFieldEmpty(text));
		}
		Date date = dateBox.getValue();
		if (date == null)
			throw new Exception(Language.paramaterFormatError(this.text));
		else
			return DateTime.frenchDateFormat.parse(DateTime.frenchDateFormat
					.format(date));
	}
	
	/**
	 * Returns date object with date equals to the date in the date box and time equals to the time parameter (HH:mm:ss) 
	 * @param time - time to be added to the date in the date box
	 * @return 
	 * @throws Exception if the date in the date box is not correct or empty
	 */
	public Date getDateTime(String time) throws Exception {
		if (dateBox.getTextBox().getText().trim().equals(Language.emptyString)) {
			throw new Exception(Language.errorFieldEmpty(text));
		}
		Date date = dateBox.getValue();
		if (date == null)
			throw new Exception(Language.paramaterFormatError(this.text));
		else
			return DateTime.dateTimeFormat.parse(DateTime.dateFormat
					.format(date) + " " + time);
	}


	/**
	 * Enables or not the date picker object
	 * @param enabled
	 */
	public void setEnabled(boolean enabled){
		this.dateBox.setEnabled(enabled);
	}



	/**
	 * The date of the date box must be copied in another datePickerGeneric object.
	 * @param b boolean
	 * @param datePickerGeneric
	 */
	public void copyDateToAnotherDatePickerGeneric(boolean b, DatePickerGeneric datePickerGeneric){
		this.copyDateToAnotherDatePickerGeneric = b;
		this.otherDatePickerGeneric = datePickerGeneric;
	}



}
