package karrus.client.generic;

import karrus.client.appearance.LayoutInfo;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;

/**
 * Creates a panel to enter password in a text box.
 * 
 * @author Estelle DUMAS: estelle.dumas@karrus-its.com
 */
public class PasswordTextBoxGeneric extends Composite {

	private PasswordTextBox passwordTextBox;
	private Label label;

	/**
	 * Constructor: creates a horizontal panel with a text box
	 * @param lab the label
	 * @param value the default text
	 */
	public PasswordTextBoxGeneric(String lab, String value){
		this.label = new Label(lab + ": ");
		this.passwordTextBox = new PasswordTextBox();
		this.passwordTextBox.setWidth(LayoutInfo.textBoxWidth);
		this.passwordTextBox.setText(value);
		FlexTable flexTable = new FlexTable();
		flexTable.setWidget(0, 0, label);
		flexTable.setWidget(0, 1, passwordTextBox);
		this.initWidget(flexTable);	
	}

	/**
	 * @return the string of the label
	 */
	public String getLabel(){
		return this.label.getText();
	}

	/**
	 * @return PasswordTextBox
	 */
	public PasswordTextBox getTextBox(){
		return this.passwordTextBox;
	}

	/**
	 * Gets the value of the text box
	 * @return String 
	 */
	public String getValue(){
		String s = this.passwordTextBox.getValue();
		return s.trim();
	}

	public void setValue(String value){
		this.passwordTextBox.setText(value);
	}

	public void setEnabled(boolean enabled){
		this.passwordTextBox.setEnabled(enabled);
	}

}

