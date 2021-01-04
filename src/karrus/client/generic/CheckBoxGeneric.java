package karrus.client.generic;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;


/**
 * Creates a horizontal panel with a check box.
 * 
 * @author Estelle DUMAS: estelle.dumas@karrus-its.com
 */
public class CheckBoxGeneric extends Composite {

	private Label label;
	private CheckBox checkBox;
	
	/**
	 * Constructor.
	 * @param lab the label of the panel
	 * @param checked boolean
	 */
	public CheckBoxGeneric (String lab, Boolean checked){
		this.label = new Label(lab+": ");
		this.checkBox = new CheckBox();
		this.checkBox.setValue(checked);
		FlexTable flexTable = new FlexTable();
		flexTable.setWidget(0, 0, label);
		flexTable.setWidget(0, 1, checkBox);
		this.initWidget(flexTable);
	}
	
	/**
	 * Gets the text of the label.
	 * @return String
	 */
	public String getLabel(){
		return this.label.getText();
	}
	
	
	/**
	 * @return boolean if the check box is checked or not
	 */
	public boolean getValue(){
		return this.checkBox.getValue();
	}
	
	
	/**
	 * @return CheckBox
	 */
	public CheckBox getCheckBox(){
		return this.checkBox;
	}
	
	public void setEnabled(boolean enabled){
		this.checkBox.setEnabled(enabled);
	}
	
	public void setValue(boolean value){
		this.checkBox.setValue(value);
	}
	
	public void addValueChangeHandler(ValueChangeHandler<Boolean> valueChangeHandler){
		this.checkBox.addValueChangeHandler(valueChangeHandler);
	}
	
	public void addClickHandler(ClickHandler clickHandler){
		this.checkBox.addClickHandler(clickHandler);
	}
}
