package karrus.client.generic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

import karrus.shared.language.Language;

/**
 * List box panel.
 * 
 * @author Estelle DUMAS: estelle.dumas@karrus-its.com
 */
public class ListBoxGeneric extends Composite {

	private Label label;
	private ListBox listBox;
	private boolean isEmptyLineShown;
	private boolean hasTobeOrdered;

	/** Constructor. 
	 * @param label Label of the list box.
	 * @param values Values for the list box.
	 * @param selectedItem Selected item in the list box. 
	 */
	public ListBoxGeneric(String label, List<String> values, String selectedItem) {
		this(label, values, selectedItem, false, false);
	}
	
	/** Constructor. 
	 * @param label Label of the list box.
	 * @param values Values for the list box.
	 * @param selectedItem Selected item in the list box. 
	 * @param isEmptyLineShown boolean Show empty line if true.
	 */
	public ListBoxGeneric (String label, List<String> values, String selectedItem, boolean isEmptyLineShown){
		this(label, values, selectedItem, isEmptyLineShown, false);
	}
	
	/**
	 * Constructor.
	 * @param label Label of the list box.
	 * @param values Values for the list box.
	 * @param selectedItem Selected item in the list box. 
	 * @param isEmptyLineShown boolean Show empty line if true.
	 * @param isOrdered boolean Order list box entries if true.
	 */
	public ListBoxGeneric (String label, List<String> values, String selectedItem, boolean isEmptyLineShown, boolean isOrdered) {
		// store parameters
		if (label!=null)
			this.label = new Label(label+Language.doubleDot);
		this.listBox = new ListBox();
		if (selectedItem != "") {
			setValues(values, selectedItem);
		}
		else {
			if (values.size() != 0) {
				setValues(values, values.get(0));
			}
		}
		this.isEmptyLineShown = isEmptyLineShown;
		this.hasTobeOrdered = isOrdered;
		// make layout
		FlexTable flexTable = new FlexTable();
		if (this.label!=null){
			flexTable.setWidget(0, 0, this.label);
			flexTable.setWidget(0, 1, listBox);
		}
		else {
			
			flexTable.setWidget(0, 0, listBox);
		}
		this.initWidget(flexTable);
	}

	/**
	 * @return the string of the label
	 */
	public String getLabel(){
		return this.label.getText();
	}

	/**
	 * @return the list box object
	 */
	public ListBox getListBox(){
		return this.listBox;
	}
	
	/**
	 * Number of item shown before showing a drop-down.
	 * @param visibleItems Number of items.
	 */
	public void setVisibleItemCount(int visibleItems){
		this.listBox.setVisibleItemCount(visibleItems);
	}

	/**
	 * Enabler.
	 * @param enabled Enable the list box if true.
	 */
	public void setEnabled(boolean enabled){
		this.listBox.setEnabled(enabled);
	}

	/**
	 * Selects an item in the list box.
	 * @param value
	 */
	public void setSelectedItem(String value) {
		for (int i = 0; i < this.listBox.getItemCount(); i++) {
			if (this.listBox.getItemText(i).equals(value)) {
				this.listBox.setSelectedIndex(i);
			}
		}
	}

	/**
	 * Gets the selected item of the list box.
	 * @return String
	 */
	public String getSelectedValue(){
		if (this.listBox.getSelectedIndex()==-1)
			return "";
		return this.listBox.getItemText(this.listBox.getSelectedIndex());
	}

	/**
	 * Adds a changeHandler object.
	 * @param changeHandler ChangeHandler to add.
	 */
	public void addChangeHandler(ChangeHandler changeHandler){
		this.listBox.addChangeHandler(changeHandler);
	}

	/**
	 * Sets the values in the list box and selects an item
	 * @param values
	 * @param selectedItem
	 */
	public void setValues(List<String> values, String selectedItem){
		this.listBox.clear();
		if (isEmptyLineShown)
			this.listBox.addItem(Language.emptyString);
		if (values!=null && hasTobeOrdered)
			values = sort(values);
		if (values!=null)
			for (String v : values)
				this.listBox.addItem(v);
		if (selectedItem==null) {
			if (this.listBox.getItemCount()!=0)
				this.listBox.setSelectedIndex(0);
			}
		else {
			setSelectedItem(selectedItem);
		}
	}

	private List<String> sort(List<String> list) {
		List<String> hasNoNumber = new ArrayList<String>();
		Map<Integer, String> int2string = new HashMap<Integer, String>();
		for (String s : list) {
			RegExp pattern = RegExp.compile("\\d+");
			MatchResult matcher = pattern.exec(s);
			boolean found = (matcher!=null);
			if (found)
				int2string.put(new Integer(matcher.getGroup(0)), s);
			else
				hasNoNumber.add(s);
		}
		Set<Integer> keys = int2string.keySet();
		List<Integer> orderedKeys = new ArrayList<Integer>();
		for (Integer i : keys)
			orderedKeys.add(i);
		Collections.sort(orderedKeys);
		List<String> orderedSensors = new ArrayList<String>();
		for (int i : orderedKeys)
			orderedSensors.add(int2string.get(i));
		for (String s : hasNoNumber)
			orderedSensors.add(s);
		return orderedSensors;
	}

}
