package karrus.client.generic;

import karrus.client.appearance.LayoutInfo;
import karrus.shared.language.Language;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;


/**
 * Creates a horizontal panel with a text box to enter doubles.
 * 
 * @author Estelle DUMAS: estelle.dumas@karrus-its.com
 */
public class DoubleBoxGeneric extends Composite {

	private TextBox textBox;
	private Label label;
	private String lab;

	
	/**
	 * Constructor: creates a horizontal panel with a text box to enter doubles.
	 * @param lab the label
	 * @param value the default double
	 */
	public DoubleBoxGeneric (String lab, Double value){
		this.lab = lab;
		this.label = new Label(lab+": ");
		this.textBox = new TextBox();
		this.textBox.setWidth(LayoutInfo.textBoxWidth);
		setValue(value);
		HorizontalPanel panel = new HorizontalPanel();
		panel.add(this.label);
		panel.add(this.textBox);
		this.initWidget(panel);
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

	public String getString(){
		return lab;
	}
	
	/**
	 * Gets the double entered in box
	 * @return Double
	 * @throws Exception if it is not an double
	 */
	public Double getValue() throws Exception{
		String val = this.textBox.getText();
		if (val==null || val.equals(""))
			return null;
		else {
			try {
				Double d =  new Double(val);
				return d;
			} catch (NumberFormatException e){
				throw new Exception (Language.doubleError);
			}
		}
	}

	/**
	 * Set the value to the double text box.
	 * @param value Double
	 */
	public void setValue(Double value){
		if (value==null) {
			this.textBox.setText("");
		}	
		else {
			this.textBox.setText(value+"");
		}	
	}

	/**
	 * Sets the double text box enabled or not.
	 * @param enabled boolean
	 */
	public void setEnabled(boolean enabled){
		this.textBox.setEnabled(enabled);
	}

}

