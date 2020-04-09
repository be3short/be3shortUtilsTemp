/* ==============================================================
 * data utils - general data storage & visualization utilities
 * ==============================================================
 *
 * MIT License
 * 
 * Copyright (c) 2020 Brendan Short https://be3short.com/
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
*/

package com.be3short.data.figure;

import java.awt.Component;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.be3short.data.graphics.Graphic;
import com.be3short.data.graphics.GraphicFormat;

public interface Figure extends Graphic {

	public static int defaultWidth = 400;

	public static int defaultHeight = 400;

	public Figure addComponent(int row, int col, Component component);

	public Figure addComponent(int row, int col, Graphic component);

	public Figure setSize(int height, int width);

	public Figure setTitle(String title);

	public JFrame getFrame();

	/**
	 * Get the title label of the entire figure. This label is displayed at the top
	 * of the figure, or hidden if there is no title. The title is hidden by setting
	 * the text to null
	 * 
	 * @return the title label of the figure
	 */
	public JLabel getTitle();

	/**
	 * Exports the figure to an output file in a specified image format
	 * 
	 * @param location
	 *            location where the image file is to be created
	 * @param format
	 *            image format for file to be exported to
	 * 
	 * @return true if export was a success, false otherwise
	 * 
	 */
	public boolean exportToFile(File location, GraphicFormat format);

}
