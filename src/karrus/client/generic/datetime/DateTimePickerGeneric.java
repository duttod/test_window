package karrus.client.generic.datetime;

import java.util.Date;
import karrus.client.utils.DateTime;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.datepicker.client.DateBox;

/**
 * Panel with date picker and time list boxes.
 * 
 * @author Estelle DUMAS: estelle.dumas@karrus-its.com
 */
public class DateTimePickerGeneric extends Composite {

	private DatePickerGeneric datePicker;
	private FlexTable flexTable;
	
	/**
	 * Constructor.
	 * @param label Label of the composite.
	 * @param date Default date.
	 */
	public DateTimePickerGeneric(String label, Date date) {
		this.datePicker = new DatePickerGeneric(label, date);
		flexTable = new FlexTable();
		flexTable.setWidget(0, 0, datePicker);
		this.initWidget(flexTable);
	}

	/**
	 * Getter.
	 * @return Label of the composite.
	 */
	public Label getLabel(){
		return new Label(datePicker.getLabel());
	}
	
	/**
	 * 
	 * @return the label string
	 */
	public String getStringLabel(){
		return datePicker.getLabel();
	}

	
	/**
	 * Sets the width of the date box.
	 * @param width String
	 */
	public void setDateBoxWidth(String width){
		datePicker.setDateBoxWidth(width);
	}

	public FlexTable getFlexTable(){
		return flexTable;
	}
	
	/**
	 * 
	 * @return the date box object
	 */
	public DateBox getDatePicker(){
		return this.datePicker.getDatePicker();
	}

	
	/**
	 * Gets date and time parameters that the user entered in the fields. 
	 * @return Date
	 * @throws Exception if time or date is not in the right format
	 */
	public Date getDateTime(String Time) throws Exception {
		Date date = getDate();
		if (date == null)
			return null;
		else {
			String s = DateTime.dateFormat.format(date) + " " + Time;
			return DateTime.dateTimeFormat.parse(s);
		}
	}

	/**
	 * Getter.
	 * @return Current date of the date picker.
	 * @throws Exception if the date is not correct.
	 */
	public Date getDate() throws Exception {
		return this.datePicker.getDate();
	}

	/**
	 * Sets date and time.
	 * @param date Date to be set.
	 */
	public void setDate(Date date){
		/// TODO check : Date d = DateTime.dateFormat.parse(DateTime.dateFormat.format(date));
		datePicker.setDate(date);
	}


	/**
	 * Date and time enabler.
	 * @param enabled Set to true to enable date and time selection.
	 */
	public void setEnabled(boolean enabled){
		this.datePicker.setEnabled(enabled);
	}
	
	/**
	 * The date of the date box must be copied in another dateTimePickerGeneric object.
	 * @param copy boolean
	 * @param dateTimePickerGeneric
	 */
	public void copyDateToAnotherDatePickerGeneric(boolean copy, DateTimePickerGeneric dateTimePickerGeneric){
		if (dateTimePickerGeneric!=null)
			this.datePicker.copyDateToAnotherDatePickerGeneric(copy, dateTimePickerGeneric.datePicker);
	}
	
}
