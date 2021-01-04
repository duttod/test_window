package karrus.client.generic;

import karrus.client.appearance.LayoutInfo;
import karrus.shared.language.Language;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;


/**
 * Creates a horizontal panel with a text box to enter integers.
 * 
 * @author Estelle DUMAS: estelle.dumas@karrus-its.com
 */
public class IntegerBoxGeneric extends Composite {

	private TextBox textBox;
	private Label label;
	private String lab;

	
	/**
	 * Constructor.
	 * @param label the label
	 * @param value the default integer
	 */
	public IntegerBoxGeneric (String label, Integer value){
		this.lab = label;
		this.label = new Label(label+": ");
		this.textBox = new TextBox();
		this.textBox.setWidth(LayoutInfo.textBoxWidth);
		setValue(value);
		HorizontalPanel panel = new HorizontalPanel();
		panel.add(this.label);
		panel.add(this.textBox);
		this.initWidget(panel);
	}

	public String getString(){
		return lab;
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
	 * Gets the integer entered in the text box.
	 * @return Integer
	 * @throws Exception if it is not an integer
	 */
	public Integer getValue() throws Exception{
		String value = this.textBox.getText();
		if (value==null || value.trim().equals("") || value.equals(null)) {
			return null;
		}	
		else {
			try {
				Integer valueAsInteger =  new Integer(value);
				return valueAsInteger;
			} catch (NumberFormatException e){
				throw new Exception (Language.integerError);
			}
		}
	}

	/**
	 * Sets the value of the text box.
	 * @param value
	 */
	public void setValue(Integer value){
		if (value==null) {
			this.textBox.setText("");
		}	
		else {
			this.textBox.setText(value+"");
		}	
	}

	/**
	 * Sets all elements of this panel enabled or not.
	 * @param enabled
	 */
	public void setEnabled(boolean enabled){
		this.textBox.setEnabled(enabled);
	}

}

