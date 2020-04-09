
package com.be3short.data.figure;

import java.awt.event.ActionEvent;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;

import org.apache.log4j.Logger;

import com.be3short.data.graphics.GraphicFormat;

/**
 * Action for format selection buttons
 * 
 * @author Brendan Short
 *
 */
class FormatSelectAction extends AbstractAction {

	private static Logger log = Logger.getLogger(FormatSelectAction.class);

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 2710763100435246769L;

	/**
	 * Button to graphic format map
	 */
	private HashMap<JButton, GraphicFormat> buttonMap;

	/**
	 * Selected button
	 */
	private JButton button;

	/**
	 * Dialog container
	 */
	private JDialog dialog;

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
	public FormatSelectAction(JDialog dialog, HashMap<JButton, GraphicFormat> map, JButton button) {

		putValue(NAME, button.getText()); // bounds properties
		putValue(SHORT_DESCRIPTION, button.getText());
		this.buttonMap = map;
		this.button = button;
		this.dialog = dialog;
	}

	/**
	 * Action to take when button is pressed
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		FormatSelector.graphicFormat = buttonMap.get(button);
		log.debug("Format selected : " + FormatSelector.graphicFormat.getFormatName());
		if (dialog != null) {
			dialog.dispose();
		}
	}
}