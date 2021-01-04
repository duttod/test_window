package karrus.client.synoptic.modal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




import karrus.client.generic.CustomDialogBox;
import karrus.shared.language.Language;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.gen2.table.override.client.FlexTable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;


/**
 * Creates a horizontal panel with buttons from 5 min to 6h.
 * 
 * @author Estelle DUMAS : estelle.dumas@karrus-its.com
 * 
 */
public class TimeHorizonSelector extends Composite {

	private int timeHorizon;

	Map<Integer, Button> timehorizon2Buttons = new HashMap<Integer, Button>();

	List<TimeHorizonSelectorListener> timeHorizonSelectorListeners = new ArrayList<TimeHorizonSelectorListener>();

	/**
	 * Constructor.
	 * @throws Exception 
	 */
	public TimeHorizonSelector() throws Exception {

		final int time5min = 5*60*1000;
		final Button button5min = new Button(Language.min5);
		button5min.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					setTimeHorizon(time5min);
				} catch (Exception e) {
					Log.error(e.getMessage());
					new CustomDialogBox(e.getMessage(), Language.okString);
				}
			}
		});
		timehorizon2Buttons.put(time5min, button5min);

		final int time10min = 10*60*1000;
		final Button button10min = new Button(Language.min10);
		button10min.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					setTimeHorizon(time10min);
				} catch (Exception e) {
					Log.error(e.getMessage());
					new CustomDialogBox(e.getMessage(), Language.okString);
				}
			}
		});
		timehorizon2Buttons.put(time10min, button10min);

		final int time20min = 20*60*1000;
		final Button button20min = new Button(Language.min20);
		button20min.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					setTimeHorizon(time20min);
				} catch (Exception e) {
					Log.error(e.getMessage());
					new CustomDialogBox(e.getMessage(), Language.okString);
				}
			}
		});
		timehorizon2Buttons.put(time20min, button20min);

		final int time30min = 30*60*1000;
		final Button button30min = new Button(Language.min30);
		button30min.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					setTimeHorizon(time30min);
				} catch (Exception e) {
					Log.error(e.getMessage());
					new CustomDialogBox(e.getMessage(), Language.okString);
				}
			}
		});
		timehorizon2Buttons.put(time30min, button30min);

		final int time45min = 45*60*1000;
		final Button button45min = new Button(Language.min45);
		button45min.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					setTimeHorizon(time45min);
				} catch (Exception e) {
					Log.error(e.getMessage());
					new CustomDialogBox(e.getMessage(), Language.okString);
				}
			}
		});
		timehorizon2Buttons.put(time45min, button45min);

		final int time1h = 60*60*1000;
		final Button button1h = new Button(Language.h1);
		button1h.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					setTimeHorizon(time1h);
				} catch (Exception e) {
					Log.error(e.getMessage());
					new CustomDialogBox(e.getMessage(), Language.okString);
				}
			}
		});
		timehorizon2Buttons.put(time1h, button1h);

		final int time1h30 = 90*60*1000;
		final Button button1h30 = new Button(Language.h1min30);
		button1h30.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					setTimeHorizon(time1h30);
				} catch (Exception e) {
					Log.error(e.getMessage());
					new CustomDialogBox(e.getMessage(), Language.okString);
				}
			}
		});
		timehorizon2Buttons.put(time1h30, button1h30);

		final int time2h = 120*60*1000;
		final Button button2h = new Button(Language.h2);
		button2h.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					setTimeHorizon(time2h);
				} catch (Exception e) {
					Log.error(e.getMessage());
					new CustomDialogBox(e.getMessage(), Language.okString);
				}
			}
		});
		timehorizon2Buttons.put(time2h, button2h);

		final int time3h = 180*60*1000;
		final Button button3h = new Button(Language.h3);
		button3h.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					setTimeHorizon(time3h);
				} catch (Exception e) {
					Log.error(e.getMessage());
					new CustomDialogBox(e.getMessage(), Language.okString);
				}
			}
		});
		timehorizon2Buttons.put(time3h, button3h);

		final int time4h = 240*60*1000;
		final Button button4h = new Button(Language.h4);
		button4h.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					setTimeHorizon(time4h);
				} catch (Exception e) {
					Log.error(e.getMessage());
					new CustomDialogBox(e.getMessage(), Language.okString);
				}
			}
		});
		timehorizon2Buttons.put(time4h, button4h);

		final int time5h = 300*60*1000;
		final Button button5h = new Button(Language.h5);
		button5h.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					setTimeHorizon(time5h);
				} catch (Exception e) {
					Log.error(e.getMessage());
					new CustomDialogBox(e.getMessage(), Language.okString);
				}
			}
		});
		timehorizon2Buttons.put(time5h, button5h);

		final int time6h = 360*60*1000;
		final Button button6h = new Button(Language.h6);
		button6h.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					setTimeHorizon(time6h);
				} catch (Exception e) {
					Log.error(e.getMessage());
					new CustomDialogBox(e.getMessage(), Language.okString);
				}
			}
		});
		timehorizon2Buttons.put(time6h, button6h);
		setTimeHorizon(time1h);

		FlexTable table = new FlexTable();
		int i=0;
//		table.setWidget(0, i, button5min);
//		i++;
		table.setWidget(0, i, button10min);
		i++;
		table.setWidget(0, i, button20min);
		i++;
		table.setWidget(0, i, button30min);
		i++;
		table.setWidget(0, i, button45min);
		i++;
		table.setWidget(0, i, button1h);
		i++;
		table.setWidget(0, i, button1h30);
		i++;
		table.setWidget(0, i, button2h);
		i++;
		table.setWidget(0, i, button3h);
		i++;
		table.setWidget(0, i, button4h);
		i++;
		table.setWidget(0, i, button5h);
		i++;
		table.setWidget(0, i, button6h);
		initWidget(table);
	}


	public void setTimeHorizon(int timeHorizon) throws Exception{
		for (int availableTimeHorizon : timehorizon2Buttons.keySet()){
			if (availableTimeHorizon==timeHorizon){
				this.timeHorizon = timeHorizon;
				select(timeHorizon);
				fireTimeHorizonSelectionEvent();
				return;
			}
		}
		throw new Exception("L'horizon temporel "+timeHorizon+" n'est pas autorisé.");
	}

	public int getTimeHorizon(){
		return this.timeHorizon;
	}


	protected void select(int timeHorizon) {
		for (int availableTimeHorizon : timehorizon2Buttons.keySet()) {
			if (availableTimeHorizon==timeHorizon) 
				timehorizon2Buttons.get(availableTimeHorizon).setEnabled(false);
			else 
				timehorizon2Buttons.get(availableTimeHorizon).setEnabled(true);
		}
	}


	/**
	 * Adds a listener to the buttons panel.
	 * @param timeHorizonSelectorListener TimeHorizonSelectorListener
	 */
	public void addTimeHorizonSelectorListener(TimeHorizonSelectorListener timeHorizonSelectorListener) {
		timeHorizonSelectorListeners.add(timeHorizonSelectorListener);
	}

	/**
	 * Removes a listener to the buttons panel.
	 * @param timeHorizonSelectorListener TimeHorizonSelectorListener
	 */

	public void removeTimeHorizonSelectorListener(TimeHorizonSelectorListener timeHorizonSelectorListener) {
		timeHorizonSelectorListeners.remove(timeHorizonSelectorListener);
	}


	private void fireTimeHorizonSelectionEvent() {
		for (TimeHorizonSelectorListener timeHorizonSelectorListener : timeHorizonSelectorListeners) {
			timeHorizonSelectorListener.onTimeHorizonSelection(this.timeHorizon);
		}
	}

}
