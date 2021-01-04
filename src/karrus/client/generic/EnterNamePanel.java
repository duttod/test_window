package karrus.client.generic;


import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Creates a panel with a text field and a 'ok' button.
 * 
 * @author Estelle DUMAS: estelle.dumas@karrus-its.com
 */
public class EnterNamePanel extends Composite {
	
	private TextBoxGeneric textBox;
	private Button button;
	
	/**
	 * Constructor.
	 * @param label
	 * @param defaultValue in the text field
	 * @param buttonLabel
	 * @param click action when clicking on the button
	 */
	public EnterNamePanel(String label, String defaultValue, String buttonLabel, ClickHandler click){
		textBox = new TextBoxGeneric(label, defaultValue);
		button = new Button(buttonLabel);
		button.addClickHandler(click);
		
		FlexTable flexTable = new FlexTable();
		flexTable.setWidget(0, 0, textBox);
		flexTable.setWidget(0, 1, button);
		
		this.initWidget(flexTable);
		
	}
	
	/**
	 * Gets the text entered in the text box.
	 * @return string
	 * @throws Exception if the text is empty.
	 */
	public String getValue() throws Exception{
		return textBox.getValue();
	}
	
	/**
	 * Sets the text in the text box.
	 * @param s
	 */
	public void setTextInTextBox(String s){
		textBox.setValue(s);
	}
	
	public Label getLabel(){
		return new Label(textBox.getLabel());
	}
	
	public TextBox getTextBox(){
		return textBox.getTextBox();
	}
	
	public Button getButton(){
		return button;
	}
	
}
