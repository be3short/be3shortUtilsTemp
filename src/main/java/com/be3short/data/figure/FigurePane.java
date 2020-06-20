/*
 * =========================================================== HSE : The Hybrid Systems Environment
 * ===========================================================
 *
 * MIT License
 * 
 * Copyright (c) 2018 HybridSystemsEnvironment
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * 
 * ------------------------------------------------ Figure.java ------------------------------------------------
 *
 * Original Author: Brendan Short Contributor(s):
 *
 * Changes: -------- 01-June-2018 : Version 1 (BS); 01-August-2018 : Added export figure methods with reduced arguments
 * to use file browser when no file specified and use format selection browser when no format specified (BS);
 * 
 */

package com.be3short.data.figure;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.apache.log4j.Logger;

import com.be3short.data.graphics.Graphic;
import com.be3short.data.graphics.GraphicFormat;
import com.be3short.data.store.DynamicFieldObject;
import com.be3short.util.io.FileBrowser;

/**
 * A general figure window to display a collection of java swing and swing based
 * components in any specified arrangement.
 * 
 * Intended Operator: User
 */
public class FigurePane extends DynamicFieldObject implements Figure {

	public static long exportDelay = 2000;

	private static Logger log = Logger.getLogger(FigurePane.class);

	/**
	 * Default height and width of the
	 */
	protected static int defaultHeight = 600;

	/**
	 * Default title font
	 */
	protected static Font defaultTitleFont = new Font("SansSerif", Font.BOLD, 18);

	/**
	 * Default height and width of the figure
	 */
	protected static int defaultWidth = 800;

	/**
	 * Mapping between each component and its assigned constraint
	 */
	protected HashMap<Component, GridBagConstraints> components;

	/**
	 * The panel that contains all of the components
	 */
	protected JPanel contentPanel;

	/**
	 * The frame of the window display
	 */
	protected JFrame displayFrame;

	/**
	 * The root panel that contains the content panel and the title.
	 */
	protected JPanel rootPanel;

	/**
	 * @return the rootPanel
	 */
	public JPanel getRootPanel() {

		return rootPanel;
	}

	/**
	 * The label for the title of the figure
	 */
	protected JLabel title;

	private boolean globalLegend;

	/**
	 * Construct a new figure with default dimensions
	 */
	public FigurePane() {

		this(defaultWidth, defaultHeight);

	}

	/**
	 * Construct a new figure with the specified dimensions
	 * 
	 * @param width
	 *            pixel width of the figure pane
	 * @param height
	 *            height of the figure pane
	 * 
	 */
	public FigurePane(int width, int height) {

		globalLegend = false;
		contentPanel = defaultPanel(null, null);// width, height);
		rootPanel = new JPanel(new BorderLayout());

		rootPanel.setPreferredSize(new Dimension(width, height));
		displayFrame = new JFrame("Figure");
		displayFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		initializeTitle();
		displayFrame.setResizable(true);
		components = new HashMap<Component, GridBagConstraints>();
	}

	/**
	 * Construct a new figure with the specified dimensions
	 * 
	 * @param width
	 *            pixel width of the figure pane
	 * @param height
	 *            height of the figure pane
	 * 
	 */
	public FigurePane(int width, int height, String title) {

		this(width, height);
		this.title.setText(title);
	}

	/**
	 * Add a component to the figure with a pre-defined constraint
	 * 
	 * @param constraints
	 *            custom constraint that will determine the placement of the
	 *            component
	 * @param component
	 *            component to be displayed in the figure
	 */
	public void add(GridBagConstraints constraints, Component component) {

		contentPanel.add(component, constraints);
		components.put(component, constraints);
	}

	/**
	 * Add a component to the figure with a generated constraint from the grid
	 * indices
	 * 
	 * @param x
	 *            column index of the component
	 * @param y
	 *            row index of the component
	 * @param component
	 *            component to be displayed in the figure
	 */
	@Override
	public Figure addComponent(int row, int col, Component component) {

		GridBagConstraints constraints = FigurePane.createGridBagConstraints(col, row);
		contentPanel.add(component, constraints);
		components.put(component, constraints);
		return this;
	}

	/**
	 * Add a sub figure to the figure with a generated constraint from the grid
	 * indices
	 * 
	 * @param x
	 *            column index of the component
	 * @param y
	 *            row index of the component
	 * @param component
	 *            component to be displayed in the figure
	 */
	public void add(int row, int col, FigurePane component) {

		GridBagConstraints constraints = FigurePane.createGridBagConstraints(col, row);
		contentPanel.add(component.rootPanel, constraints);
		components.put(component.rootPanel, constraints);
	}

	/**
	 * Generates the default c panel
	 * 
	 * @param width
	 *            specified panel width
	 * @param height
	 *            specified panel height
	 * @return pane generated figure panel
	 */
	private JPanel defaultPanel(Integer width, Integer height) {

		JPanel pane = new JPanel(new GridBagLayout(), false);
		pane.setOpaque(false);
		if (width != null && height != null) {
			pane.setPreferredSize(new Dimension(width, height));
		}
		return pane;
	}

	// /**
	// * Creates the figure in new window that can be displayed or hidden
	// *
	// * @param x
	// * the width of the image to be generated
	// *
	// * @param y
	// * the height of the image to be generated
	// * @return frame the newly displayed J frames
	// */
	// public JFrame display(int x, int y) {
	//
	// if (title.getText() == null) {
	// rootPanel.remove(title.getParent());
	// }
	// displayFrame.getContentPane().setPreferredSize(new Dimension(x, y));
	//
	// return display();
	// }

	/**
	 * Exports the figure to an output file selected by file browser, in a specified
	 * image format selected by format selector
	 * 
	 * @param format
	 *            image format for file to be exported to
	 * 
	 * @return true if export was a success, false otherwise
	 * 
	 */
	public boolean exportToFile() {

		File file = FileBrowser.save();
		GraphicFormat format = FormatSelector.selectGraphicFormatFromWindow();
		return exportToFile(file, format);
	}

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
	@Override
	public boolean exportToFile(File location, GraphicFormat format) {

		boolean success = false;
		if (location == null) {
			log.error("null file location provided, unable to export figure");
		} else if (format == null) {
			log.error("null file format provided, unable to export figure");

		} else {
			if (!displayFrame.isVisible()) {
				displayFrame.setResizable(false);
				display();

			}

			try {
				Thread.currentThread().sleep(exportDelay);
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
			FigureExporter.exportToFile(location, this, format);

			displayFrame.dispatchEvent(new WindowEvent(displayFrame, WindowEvent.WINDOW_CLOSING));
		}
		return success;
	}

	/**
	 * Exports the figure to an output file in a specified image format
	 * 
	 * @param location
	 *            location where the image file is to be created
	 * @param format
	 *            image format for file to be exported to
	 * 
	 * @param x
	 *            the width of the image to be generated
	 * @param y
	 *            the height of the image to be generated
	 * 
	 * @return true if export was successful, false otherwise
	 */
	public boolean exportToFile(File location, GraphicFormat format, int x, int y) {

		rootPanel.setPreferredSize(new Dimension(x, y));
		contentPanel.setPreferredSize(new Dimension(x, y));
		return exportToFile(location, format);
	}

	/**
	 * Exports the figure to an output file selected by file browser, in a specified
	 * image format
	 * 
	 * @param format
	 *            image format for file to be exported to
	 * 
	 * @return true if export was a success, false otherwise
	 * 
	 */
	public boolean exportToFile(GraphicFormat format) {

		return exportToFile(FileBrowser.save(), format);
	}

	/**
	 * Get the main panel that contains the components
	 * 
	 * @return panel the main panel
	 */
	public JPanel getContentPanel() {

		return contentPanel;
	}

	/**
	 * Get the figure frame
	 * 
	 * @return figure frame
	 */
	@Override
	public JFrame getFrame() {

		return displayFrame;
	}

	/**
	 * Get the title label of the entire figure. This label is displayed at the top
	 * of the figure, or hidden if there is no title. The title is hidden by setting
	 * the text to null
	 * 
	 * @return the title label of the figure
	 */
	@Override
	public JLabel getTitle() {

		return title;
	}

	/**
	 * Initializes an empty title label and panel.
	 */
	private void initializeTitle() {

		this.title = new JLabel();
		getTitle().setText(null);
		getTitle().setFont(defaultTitleFont);

		JPanel titlePane = createSubPanel(new FlowLayout());
		titlePane.add(this.title);
		rootPanel.add(titlePane, BorderLayout.NORTH);
		rootPanel.add(contentPanel, BorderLayout.CENTER);

		this.title.setBackground(null);
		this.title.setVisible(true);
		rootPanel.setOpaque(false);
		rootPanel.setBackground(null);
		contentPanel.setBackground(null);
		contentPanel.setOpaque(false);

		displayFrame.setContentPane(rootPanel);
		globalLegend = false;
	}

	/**
	 * Creates a positioned grid bag constraint with specified x and y locations
	 * 
	 * @param x
	 *            horizontal position index in a grid
	 * @param y
	 *            vertical position index in a grid
	 * 
	 * @return grid bag constraint with specified x and y locations
	 */
	public static GridBagConstraints createGridBagConstraints(int x, int y) {

		GridBagConstraints dc = new GridBagConstraints();
		dc.gridx = x;
		dc.gridy = y;
		dc.fill = GridBagConstraints.BOTH;
		dc.weightx = .5;
		dc.weighty = .5;
		return dc;
	}

	/**
	 * Create a sub panel that can hold chart panels and be added to a figure
	 * 
	 * @param layout
	 *            layout format of the sub panel
	 * @return prepared sub panel
	 */
	public static JPanel createSubPanel(LayoutManager layout) {

		JPanel panel = new JPanel(layout, false);
		panel.setOpaque(false);
		panel.setBackground(null);
		return panel;
	}

	/**
	 * @return the globalLegend
	 */
	public boolean isGlobalLegend() {

		return globalLegend;
	}

	// /**
	// * @param globalLegend
	// * the globalLegend to set
	// */
	// @Override
	// public void setGlobalLegendVisible(boolean globalLegend) {
	//
	// this.globalLegend = globalLegend;
	// }

	@Override
	public Figure setSize(int width, int height) {

		rootPanel.setPreferredSize(new Dimension(width, height));
		return this;
	}

	@Override
	public Figure setTitle(String title) {

		getTitle().setText(title);
		return this;
	}

	@Override
	public Figure addComponent(int row, int col, Graphic component) {

		component.render();

		addComponent(row, col, component.getContent());
		return this;
	}

	@Override
	public Component getContent() {

		return displayFrame;
	}

	@Override
	public void render() {

	}

	@Override
	public boolean export(File destination, GraphicFormat format) {

		// display();

		boolean success = false;
		if (destination == null) {
			log.error("null file location provided, unable to export figure");
		} else if (format == null) {
			log.error("null file format provided, unable to export figure");

		} else {
			if (!displayFrame.isVisible()) {
				display();
			}
			try {
				Thread.currentThread().sleep(exportDelay);
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
			FigureExporter.exportToFile(destination, this, format);

			displayFrame.dispatchEvent(new WindowEvent(displayFrame, WindowEvent.WINDOW_CLOSING));
		}
		return success;
	}

	@Override
	public Figure display() {

		FigureSaveListener saveListener = new FigureSaveListener(this);
		displayFrame.addKeyListener(saveListener);
		displayFrame.addKeyListener(new FigureResizeListener(this));
		if (title.getText() == null) {
			rootPanel.remove(title.getParent());
		}
		displayFrame.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {

				displayFrame.repaint();
			}
		});

		// displayFrame.getContentPane().setPreferredSize(contentPanel.getPreferredSize());

		displayFrame.pack();

		// displayFrame.setResizable(true);

		displayFrame.setVisible(true);
		displayFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		return this;
	}

}