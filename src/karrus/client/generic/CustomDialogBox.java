package karrus.client.generic;

import karrus.client.appearance.Css;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A dialog box containing either a message and a button, either a message and 2 buttons.
 * The actions on the buttons can also be specified.
 * 
 * @author Estelle DUMAS : estelle.dumas@karrus-its.com
 */
public class CustomDialogBox extends DialogBox {

	private Button button;
	public static boolean isOpened = false;
	
	/**
	 * Constructor.
	 * <br>Generates the dialog box with the message <i>message</i> and the text <i>buttonMessage</i> in the button.
	 * The action on the button is just to hide the dialog box.
	 * @param message : the message of the dialog box
	 * @param buttonMessage : the text on the button
	 */
	
	public CustomDialogBox(String message, String buttonMessage){
		this(message, buttonMessage, "");
	}
	
	public CustomDialogBox(String message, String buttonMessage, String title){
		super(false, true);
		if (!isOpened) {
			isOpened = true;
			setStyleName(Css.dialogBoxStyle);
			if (!title.equals("")) {
				setText(title);
			}	
			VerticalPanel panel = new VerticalPanel();
			HTML label = new HTML(new SafeHtmlBuilder().appendEscapedLines(message).toSafeHtml());	
			button = new Button(buttonMessage);
			button.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					isOpened = false;
					CustomDialogBox.this.hide();
				}
			});
			panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			panel.setSpacing(3);
			panel.add(label);
			panel.add(button);
			add(panel);
			center();
			show();
		}
	}
	
	public CustomDialogBox(Widget widget){
		super(false, true);
		setStyleName(Css.dialogBoxStyle);
		add(widget);
		center();
		show();
	}

	protected void onPreviewNativeEvent(Event.NativePreviewEvent event) {
		if(event.getTypeInt() == Event.ONKEYDOWN) {
			if(event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
				button.click();
			}
		}
		super.onPreviewNativeEvent(event);
	}

	/**
	 * Constructor.
	 * <br>Generates the dialog box with the widget <i>widget</i> and the text <i>buttonMessage</i> in the button.
	 * The action on the button is just to hide the dialog box.
	 * @param widget Widget
	 */
	public CustomDialogBox(Widget widget, String title){
		super(false, true);
		setStyleName(Css.dialogBoxStyle);
		if (!title.equals("")) {
			setText(title);
		}	
		VerticalPanel panel = new VerticalPanel();
		panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		panel.setSpacing(3);
		panel.add(widget);
		add(panel);
		center();
		show();
	}
	
	public CustomDialogBox(Widget widget, String buttonMessage, String title) {
		super(false, true);
		setStyleName(Css.dialogBoxStyle);
		if (!title.equals("")) {
			setText(title);
		}	
		VerticalPanel panel = new VerticalPanel();
		button = new Button(buttonMessage);
		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				CustomDialogBox.this.hide();
			}
		});
		panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		panel.setSpacing(3);
		panel.add(widget);
		panel.add(button);
		add(panel);
		center();
		show();
	}

	/**
	 * Constructor.
	 * <br>Generates the dialog box with the message <i>message</i> and the text <i>buttonMessage</i> in the button.
	 * They are 2 actions when clicking on the button: hide the dialog box and execute <i>action</i>
	 * @param message : the message
	 * @param buttonMessage : the text on the button
	 * @param action : the action to be executed when clicking on the button
	 */
	public CustomDialogBox(String message, String buttonMessage, ClickHandler action){
		this(message, buttonMessage, action, true, "");
	}
	
	public CustomDialogBox(String message, String buttonMessage, ClickHandler action, boolean closeWhenClickOnButton, String title) {
		super(false, true);
		if (!title.equals("")) {
			setText(title);
		}	
		VerticalPanel panel = new VerticalPanel();
		Label label = new Label(message);
		button = new Button(buttonMessage);
		if (closeWhenClickOnButton) {
			button.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					CustomDialogBox.this.hide();
				}
			});
		}	
		button.addClickHandler(action);
		panel.add(label);
		panel.add(button);
		add(panel);
		center();
		show(); 
	}

	/**
	 * Constructor.
	 * <br>Generates the dialog box with the widget <i>widget</i> and the text <i>buttonMessage</i> in the button.
	 * They are 2 actions when clicking on the button: hide the dialog box and execute <i>action</i>
	 * @param widget : the widget
	 * @param buttonMessage : the text on the button
	 * @param action : the action to be executed when clicking on the button
	 */
	public CustomDialogBox(Widget widget, String buttonMessage, ClickHandler action){
		this(widget, buttonMessage, action, true, "");
	}
	
	public CustomDialogBox(Widget widget, String buttonMessage, ClickHandler action, boolean closeWhenClickOnButton){
		this(widget, buttonMessage, action, closeWhenClickOnButton, "");
	}
	
	public CustomDialogBox(Widget widget, String buttonMessage, ClickHandler action, boolean closeWhenClickOnButton, String title){
		if (!title.equals("")) {
			setText(title);
		}	
		this.setModal(true);
		this.setAutoHideEnabled(false);
		setStyleName(Css.dialogBoxStyle);
		VerticalPanel panel = new VerticalPanel();
		button = new Button(buttonMessage);
		if (closeWhenClickOnButton) {
			button.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					CustomDialogBox.this.hide();
				}
			});
		}	
		button.addClickHandler(action);
		panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		panel.setSpacing(3);
		panel.add(widget);
		panel.add(button);
		this.setWidget(panel);
		this.center();
		this.show();
	}

	/**
	 * Constructor.
	 * <br>Generates the dialog box with the message <i>message</i> and 2 buttons.
	 * The actions on the buttons are described by the 2 last parameters.
	 * @param message : the message of the dialog box
	 * @param button1 : the text on the first button
	 * @param button2 : the text on the second button
	 * @param click1 : the action on the first button
	 * @param click2 : the action on the first button
	 */
	public CustomDialogBox(String message, String button1, ClickHandler click1, String button2, ClickHandler click2){
		this(message, button1, click1, true, button2, click2, true, "");
	}
	
	public CustomDialogBox(String message, String button1, ClickHandler click1, boolean closeWhenClickOnButton1, String button2, ClickHandler click2, boolean closeWhenClickOnButton2, String title) {
		if (!title.equals("")) {
			setText(title);
		}	
		this.setModal(true);
		this.setAutoHideEnabled(false);
		setStyleName(Css.dialogBoxStyle);
		Label label = new Label(message);
		Button b1 = new Button(button1);
		if (closeWhenClickOnButton1)
			b1.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					CustomDialogBox.this.hide();
				}
			});
		b1.addClickHandler(click1);
		Button b2 = new Button(button2);
		if (closeWhenClickOnButton2)
			b2.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					CustomDialogBox.this.hide();
				}
			});
		b2.addClickHandler(click2);
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.add(b1);
		hPanel.add(b2);
		hPanel.setSpacing(3);
		VerticalPanel panel = new VerticalPanel();
		panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		panel.add(label);
		panel.add(hPanel);
		this.setWidget(panel);
		this.center();
		this.show(); 
	}

	/**
	 * Constructor.
	 * <br>Generates the dialog box with the widget <i>widget</i> and 2 buttons.
	 * The actions on the buttons are described by the 2 last parameters.
	 * @param widget : the widget
	 * @param button1 : the text on the first button
	 * @param button2 : the text on the second button
	 * @param click1 : the action on the first button
	 * @param click2 : the action on the first button
	 */
	public CustomDialogBox(Widget widget, String button1, ClickHandler click1, String button2, ClickHandler click2){
		this(widget, button1, click1, button2, click2, "");
	}
	
	public CustomDialogBox(Widget widget, String button1, ClickHandler click1, String button2, ClickHandler click2, String title) {
		this(widget, button1, click1, true, button2, click2, true, title);
	}
	
	public CustomDialogBox(Widget widget, String button1, ClickHandler click1, boolean closeWhenClickOnButton1, String button2, ClickHandler click2, boolean closeWhenClickOnButton2) {
		this(widget, button1, click1, closeWhenClickOnButton1, button2, click2, closeWhenClickOnButton2, "");
	}
	
	public CustomDialogBox(Widget widget, String button1, ClickHandler click1, boolean closeWhenClickOnButton1, String button2, ClickHandler click2, boolean closeWhenClickOnButton2, String title) {
		super(false, true);
		if (!title.equals("")) {
			setText(title);
		}	
		this.setModal(true);
		this.setAutoHideEnabled(false);
		setStyleName(Css.dialogBoxStyle);
		Button b1 = new Button(button1);
		if (closeWhenClickOnButton1)
			b1.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					CustomDialogBox.this.hide();
				}
			});
		b1.addClickHandler(click1);
		Button b2 = new Button(button2);
		if (closeWhenClickOnButton2)
			b2.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					CustomDialogBox.this.hide();
				}
			});
		b2.addClickHandler(click2);
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.add(b1);
		hPanel.add(b2);
		hPanel.setSpacing(5);
		VerticalPanel panel = new VerticalPanel();
		panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		panel.add(widget);
		panel.add(hPanel);
		this.setWidget(panel);
		this.center();
		this.show(); 
	}
}
