/* ===========================================================
 * HSE : The Hybrid Systems Environment
 * ===========================================================
 *
 * MIT License
 * 
 * Copyright (c) 2018 HybridSystemsEnvironment
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * ------------------------------------------------
 * FormatSelector.java
 * ------------------------------------------------
 *
 * Original Author:  Brendan Short
 * Contributor(s):   
 *
 * Changes:
 * --------
 * 01-August-2018 : Version 1 (BS);
 * 
 */

package com.be3short.data.figure;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import org.apache.log4j.Logger;

import com.be3short.data.graphics.GraphicFormat;
import com.be3short.obj.access.Protected;

/**
 * A pop up window that allows formats to be selected
 * 
 * Intended Operator: User
 */
public class FigureFormatSelector<T> {

	/**
	 * Graphic format selector variable
	 */
	private HashMap<String, T> selectables;

	HashMap<JToggleButton, T> buttonMap;

	private Protected<T> selected;

	private JPanel choicePanel;

	private JLabel title;

	private boolean vertical;

	public FigureFormatSelector(HashMap<String, T> selectables) {

		this(selectables, false);
	}

	public FigureFormatSelector(HashMap<String, T> selectables, boolean vertical) {

		this.selectables = selectables;
		selected = new Protected<T>(null, true);
		choicePanel = getChoicePanel(null);
		this.vertical = vertical;
	}

	/**
	 * Opens a window to display graphic formatting options
	 * 
	 * @return selected graphic format
	 */
	public GraphicFormat selectGraphicFormatFromWindow() {

		JDialog dialog = new JDialog();

		dialog.setContentPane(getChoicePanel(dialog));
		dialog.setModal(true);
		dialog.pack();
		dialog.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width) / 2 - dialog.getWidth() / 2,
				(Toolkit.getDefaultToolkit().getScreenSize().height) / 2 - dialog.getHeight() / 2);
		dialog.setVisible(true);
		return null;
	}

	public JPanel getChoicePanel(JDialog dialog) {

		buttonMap = new HashMap<JToggleButton, T>();
		JPanel panel = null;
		if (vertical) {

			panel = new JPanel(new GridLayout(selectables.size() + 1, 1));
		} else {
			panel = new JPanel(new GridLayout(1, selectables.size() + 1));
		}

		for (String form : selectables.keySet()) {
			JToggleButton button = new JToggleButton(form);
			ObjectSelectAction<T> action = new ObjectSelectAction<T>(dialog, buttonMap, button, selected, false);
			button.setAction(action);
			if (selected.get() == null) {
				button.setSelected(true);
				setSelected(selectables.get(form));
			}
			buttonMap.put(button, selectables.get(form));
			panel.add(button);
		}

		return panel;
	}

	/**
	 * @return the selected
	 */
	public Protected<T> getSelected() {

		return selected;
	}

	/**
	 * @param selected
	 *            the selected to set
	 */
	public void setSelected(T selected) {

		this.selected.set(selected);
	}

	/**
	 * @return the selectables
	 */
	public HashMap<String, T> getSelectables() {

		return selectables;
	}

	/**
	 * @return the buttonMap
	 */
	public HashMap<JToggleButton, T> getButtonMap() {

		return buttonMap;
	}

	/**
	 * @return the choicePanel
	 */
	public JPanel getChoicePanel() {

		return choicePanel;
	}

	/**
	 * @return the title
	 */
	public JLabel getTitle() {

		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(JLabel title) {

		this.title = title;
	}

	public static class ObjectSelectAction<T> extends AbstractAction {

		private static Logger log = Logger.getLogger(ObjectSelectAction.class);

		/**
		 * Serial
		 */
		private static final long serialVersionUID = 2710763100435246769L;

		/**
		 * Button to graphic format map
		 */
		private HashMap<JToggleButton, T> buttonMap;

		/**
		 * Selected button
		 */
		private JToggleButton button;

		/**
		 * Dialog container
		 */
		private JDialog dialog;

		private Protected<T> selected;

		private boolean multiSelect;

		/**
		 * Construct format select action
		 * 
		 * @param dialog
		 *            dialog container
		 * @param format
		 *            format selection
		 * @param map
		 *            button to format map
		 * @param button
		 *            specified button
		 */
		public ObjectSelectAction(JDialog dialog, HashMap<JToggleButton, T> map, JToggleButton button,
				Protected<T> selected, boolean multi_select) {

			putValue(NAME, button.getText()); // bounds properties
			putValue(SHORT_DESCRIPTION, button.getText());
			this.buttonMap = map;
			this.button = button;
			this.dialog = dialog;
			this.selected = selected;
			this.multiSelect = multi_select;
		}

		/**
		 * Action to take when button is pressed
		 */
		@Override
		public void actionPerformed(ActionEvent e) {

			selected.set(buttonMap.get(button));
			log.debug("selection : " + selected.get());
			clearNonSelected(button);
			if (dialog != null) {
				dialog.dispose();
			}
		}

		public void clearNonSelected(JToggleButton button) {

			for (JToggleButton but : buttonMap.keySet()) {
				if (!multiSelect) {
					if (!but.equals(button)) {
						but.setSelected(false);
					}
				}
			}
		}
	}
}
