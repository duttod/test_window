package karrus.client.generic.datetime;

import java.util.Date;

import karrus.client.appearance.Css;
import karrus.shared.language.Language;
import com.google.gwt.gen2.table.override.client.FlexTable;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;

/**
 * Creates a panel containing 2 DateTimePicker objects: one for the initial date and one for the final date.
 * Check boxes are added to choose period in a day (morning, afternoon...)
 * 
 * @author Estelle DUMAS: estelle.dumas@karrus-its.com
 */
public class SelectDatePanel extends Composite {


	private DatePickerGeneric startDatePickerGeneric;
	private DatePickerGeneric endDatePickerGeneric;
	/**
	 * Constructor.
	 */
	public SelectDatePanel() {
		Date now = new Date();
		startDatePickerGeneric = new DatePickerGeneric(Language.initialDateString, now);
		endDatePickerGeneric = new DatePickerGeneric(Language.finalDateString, now);
		endDatePickerGeneric.getDatePicker().addStyleName(Css.greyFont);
		startDatePickerGeneric.copyDateToAnotherDatePickerGeneric(true, endDatePickerGeneric);
		FlexTable datePickersFlextable = new FlexTable();
		datePickersFlextable.setWidget(0, 0, new Label(startDatePickerGeneric.getLabel()));
		datePickersFlextable.setWidget(0, 1, startDatePickerGeneric.getDatePicker());
		datePickersFlextable.setWidget(0, 2, new Label(endDatePickerGeneric.getLabel()));
		datePickersFlextable.setWidget(0, 3, endDatePickerGeneric.getDatePicker());
		this.initWidget(datePickersFlextable);
	}
	
	/**
	 * @return the initial date parameter enter by the user in the text box.
	 * @throws Exception if the date or time is not correct
	 */
	public Date getInitialDateTime() throws Exception{
		Date initDate =  this.startDatePickerGeneric.getDateTime("00:00:00");
		Date finalDate = this.endDatePickerGeneric.getDateTime("23:59:59");
		if (initDate!=null && finalDate!=null && initDate.after(finalDate))
			throw new Exception(Language.initialAfterFinalDateError);
		return initDate;
	}


	/**
	 * @return the final date parameter enter by the user in the text box.
	 * @throws Exception if the date or time is not correct
	 */
	public Date getFinalDateTime() throws Exception{
		Date finalDate = this.endDatePickerGeneric.getDateTime("23:59:59");
		Date initDate =  this.startDatePickerGeneric.getDateTime("00:00:00");
		if (finalDate!=null && initDate!=null && finalDate.before(initDate))
			throw new Exception(Language.finalBeforeInitialDateError);
		return finalDate;
	}
}
