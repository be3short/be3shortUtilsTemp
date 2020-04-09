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

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.be3short.data.graphics.GraphicFormat;

/**
 * A pop up window that allows formats to be selected
 * 
 * Intended Operator: User
 */
public class FormatSelector {

	/**
	 * Graphic format selector variable
	 */
	static GraphicFormat graphicFormat;

	/**
	 * Opens a window to display graphic formatting options
	 * 
	 * @return selected graphic format
	 */
	public static GraphicFormat selectGraphicFormatFromWindow() {

		graphicFormat = null;
		JDialog dialog = new JDialog();

		dialog.setContentPane(getChoicePanel(dialog));
		dialog.setModal(true);
		dialog.pack();
		dialog.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width) / 2 - dialog.getWidth() / 2,
				(Toolkit.getDefaultToolkit().getScreenSize().height) / 2 - dialog.getHeight() / 2);
		dialog.setVisible(true);
		return graphicFormat;
	}

	public static JPanel getChoicePanel(JDialog dialog) {

		HashMap<JButton, GraphicFormat> buttonMap = new HashMap<JButton, GraphicFormat>();
		JPanel panel = new JPanel(new GridLayout(GraphicFormat.values().length + 2, 1));
		JLabel label = new JLabel("  Please select output format  ", JLabel.CENTER);
		label.setFont(new Font(label.getFont().getFontName(), Font.PLAIN, 18));
		panel.add(label);
		panel.add(new JLabel("     ", JLabel.CENTER));
		for (GraphicFormat form : GraphicFormat.values()) {
			JButton button = new JButton(form.getFormatName());
			FormatSelectAction action = new FormatSelectAction(dialog, buttonMap, button);
			button.setAction(action);
			buttonMap.put(button, form);
			panel.add(button);
		}
		return panel;
	}
}
