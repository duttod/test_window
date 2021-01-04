package karrus.client.generic;

import karrus.client.appearance.LayoutInfo;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Creates a horizontal panel with a text box
 * 
 * @author Estelle DUMAS: estelle.dumas@karrus-its.com
 */
public class TextBoxGeneric extends Composite {

	private TextBox textBox;
	private Label label;

	/**
	 * Constructor: creates a horizontal panel with a text box
	 * @param lab the label
	 * @param value the default text
	 */
	public TextBoxGeneric(String lab, String value){
		this.label = new Label(lab+" : ");
		this.textBox = new TextBox();
		this.textBox.setWidth(LayoutInfo.textBoxWidth);
		this.textBox.setText(value);
		
		FlexTable flexTable = new FlexTable();
		flexTable.setWidget(0, 0, label);
		flexTable.setWidget(0, 1, textBox);
		this.initWidget(flexTable);
		
	}

	/**
	 * @return the string of the label
	 */
	public String getLabel(){
		return this.label.getText();
	}

	/**
	 * @return the text box object
	 */
	public TextBox getTextBox(){
		return this.textBox;
	}

	/**
	 * Gets the value of the text box
	 * @return String
	 */
	public String getValue(){
		String s = this.textBox.getText();
		return s.trim();
	}

	/**
	 * Sets the value of the text box
	 * @param value String
	 */
	public void setValue(String value){
		this.textBox.setText(value);
	}

	/**
	 * Sets the text box enabled or not.
	 * @param enabled boolean
	 */
	public void setEnabled(boolean enabled){
		this.textBox.setEnabled(enabled);
	}

}

