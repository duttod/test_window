package karrus.client.generic;

import karrus.client.appearance.LayoutInfo;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;

/**
 * Creates a horizontal panel with a text area
 * 
 * @author Estelle DUMAS: estelle.dumas@karrus-its.com
 */
public class TextAreaGeneric extends Composite {

	private TextArea textArea;
	private Label label;

	/**
	 * Constructor: creates a horizontal panel with a text box
	 * @param label the label
	 * @param value the default text
	 */
	public TextAreaGeneric (String label, String value){
		this.label = new Label(label+": ");
		this.textArea = new TextArea();
		this.textArea.setVisibleLines(5);
		this.textArea.setWidth(LayoutInfo.textAeraWidth);
		this.textArea.setText(value);
		FlexTable flexTable = new FlexTable();
		flexTable.setWidget(0, 0, this.label);
		flexTable.setWidget(0, 1, textArea);
		this.initWidget(flexTable);
	}

	/**
	 * @return the string of the label
	 */
	public String getLabel(){
		return this.label.getText();
	}

	/**
	 * @return the text area object
	 */
	public TextArea getTextArea(){
		return this.textArea;
	}

	/**
	 * Gets the value of the text area
	 * @return String 
	 */
	public String getValue(){
		String textAreaContent = this.textArea.getText();
		return textAreaContent.trim();
	}

	/**
	 * Sets the value of the text area
	 * @param textAreaContent String
	 */
	public void setValue(String textAreaContent){
		this.textArea.setText(textAreaContent);
	}

	/**
	 * Sets the text area enabled or not.
	 * @param enabled
	 */
	public void setEnabled(boolean enabled){
		this.textArea.setEnabled(enabled);
	}
}
