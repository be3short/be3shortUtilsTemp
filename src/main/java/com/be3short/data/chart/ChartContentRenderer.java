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
 * ------------------------------------------------ HybridContentRenderer.java
 * ------------------------------------------------
 *
 * Original Author: Brendan Short* Contributor(s):
 *
 * Changes: -------- 01-June-2018 : Version 1 (BS);
 *
 */

//////////////////////////////////////////////////////////////////////////////////
// *NOTE: This class was created by modifying the XYLineAndShapeRenderer of the //
// JFreeChart library. The original licensing information is included below. //
//////////////////////////////////////////////////////////////////////////////////

/*
 * =========================================================== JFreeChart : a free chart library for the Java(tm)
 * platform ===========================================================
 *
 * (C) Copyright 2000-2014, by Object Refinery Limited and Contributors.
 *
 * Project Info: http://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 * [Oracle and Java are registered trademarks of Oracle and/or its affiliates. Other names may be trademarks of their
 * respective owners.]
 *
 * --------------------------- XYLineAndShapeRenderer.java --------------------------- (C) Copyright 2004-2014, by
 * Object Refinery Limited.
 *
 * Original Author: David Gilbert (for Object Refinery Limited); Contributor(s): -;
 *
 * Changes: -------- 27-Jan-2004 : Version 1 (DG); 10-Feb-2004 : Minor change to drawItem() method to make cut-and-paste
 * overriding easier (DG); 25-Feb-2004 : Replaced CrosshairInfo with CrosshairState (DG); 25-Aug-2004 : Added support
 * for chart entities (required for tooltips) (DG); 24-Sep-2004 : Added flag to allow whole series to be drawn as a path
 * (necessary when using a dashed stroke with many data items) (DG); 04-Oct-2004 : Renamed BooleanUtils -->
 * BooleanUtilities (DG); 11-Nov-2004 : Now uses ShapeUtilities to translate shapes (DG); 27-Jan-2005 : The
 * getLegendItem() method now omits hidden series (DG); 28-Jan-2005 : Added new constructor (DG); 09-Mar-2005 : Added
 * fillPaint settings (DG); 20-Apr-2005 : Use generators for legend tooltips and URLs (DG); 22-Jul-2005 : Renamed
 * defaultLinesVisible --> baseLinesVisible, defaultShapesVisible --> baseShapesVisible and defaultShapesFilled -->
 * baseShapesFilled (DG); 29-Jul-2005 : Added code to draw item labels (DG); ------------- JFREECHART 1.0.x
 * --------------------------------------------- 20-Jul-2006 : Set dataset and series indices in LegendItem (DG);
 * 06-Feb-2007 : Fixed bug 1086307, crosshairs with multiple axes (DG); 21-Feb-2007 : Fixed bugs in clone() and equals()
 * (DG); 20-Apr-2007 : Updated getLegendItem() for renderer change (DG); 18-May-2007 : Set dataset and seriesKey for
 * LegendItem (DG); 08-Jun-2007 : Fix for bug 1731912 where entities are created even for data items that are not
 * displayed (DG); 26-Oct-2007 : Deprecated override attributes (DG); 02-Jun-2008 : Fixed tooltips at lower edges of
 * data area (DG); 17-Jun-2008 : Apply legend shape, font and paint attributes (DG); 19-Sep-2008 : Fixed bug with
 * drawSeriesLineAsPath - patch by Greg Darke (DG); 18-May-2009 : Clip lines in drawPrimaryLine() (DG); 05-Jul-2012 :
 * Removed JDK 1.3.1 code (DG); 02-Jul-2013 : Use ParamChecks (DG);
 *
 */

package com.be3short.data.chart;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.jfree.chart.LegendItem;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.AbstractXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.chart.util.LineUtilities;
import org.jfree.data.xy.XYDataset;
import org.jfree.io.SerialUtilities;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.BooleanList;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.PublicCloneable;
import org.jfree.util.ShapeUtilities;

import com.be3short.data.store.DataSeries;
import com.be3short.util.reflection.FieldFinder;

/**
 * A renderer that connects data points with lines and/or draws shapes at each
 * data point, and uses a different stroke to connect points where jump has
 * occurred (dotted line by default). This renderer is designed for use with the
 * {@link XYPlot} class.This is a modified version with some added components
 * that support hybrid plotting (dashed line between jumps) and specific stroke
 * and color to be specified.
 *
 * Intended Operator: System
 */
public class ChartContentRenderer extends AbstractXYItemRenderer
		implements XYItemRenderer, Cloneable, PublicCloneable, Serializable {

	public static final String JUMP_OCCURRING_ALT_DOMAIN = "JUMP_OCCURRING_ALT_DOMAIN";

	public static final String JUMP_NOT_OCCURRING_ALT_DOMAIN = "JUMP_NOT_OCCURRING_ALT_DOMAIN";

	public static final String NO_ALT_DOMAIN = "NO_ALT_DOMAIN";

	/** The default value returned by the getLinesVisible() method. */
	private boolean baseLinesVisible;

	/** The default value returned by the getShapeFilled() method. */
	private boolean baseShapesFilled;

	/** The default value returned by the getShapeVisible() method. */
	private boolean baseShapesVisible;

	/** A flag that controls whether outlines are drawn for shapes. */
	private boolean drawOutlines;

	/**
	 * A flag that controls whether or not each series is drawn as a single path.
	 */
	private boolean drawSeriesLineAsPath;

	/** The shape that is used to represent a line in the legend. */
	private transient Shape legendLine;

	/**
	 * A flag that controls whether or not lines are visible for ALL series.
	 *
	 * @deprecated As of 1.0.7.
	 */
	@Deprecated
	private Boolean linesVisible;

	/**
	 * A table of flags that control (per series) whether or not lines are visible.
	 */
	private BooleanList seriesLinesVisible;

	/**
	 * A table of flags that control (per series) whether or not shapes are filled.
	 */
	private BooleanList seriesShapesFilled;

	/**
	 * A table of flags that control (per series) whether or not shapes are visible.
	 */
	private BooleanList seriesShapesVisible;

	/**
	 * A flag that controls whether or not shapes are filled for ALL series.
	 *
	 * @deprecated As of 1.0.7.
	 */
	@Deprecated
	private Boolean shapesFilled;

	/**
	 * A flag that controls whether or not shapes are visible for ALL series.
	 *
	 * @deprecated As of 1.0.7.
	 */
	@Deprecated
	private Boolean shapesVisible;

	/**
	 * A flag that controls whether the fill paint is used for filling shapes.
	 */
	private boolean useFillPaint;

	/**
	 * A flag that controls whether the outline paint is used for drawing shape
	 * outlines.
	 */
	private boolean useOutlinePaint;

	private Chart chartData;

	/**
	 * List of ordered series names
	 */
	private List<String> nameOrder;

	/**
	 * Returns a clone of the renderer.
	 *
	 * @return A clone.
	 *
	 * @throws CloneNotSupportedException
	 *             if the clone cannot be created.
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {

		ChartContentRenderer clone = (ChartContentRenderer) super.clone();
		clone.seriesLinesVisible = (BooleanList) this.seriesLinesVisible.clone();
		if (this.legendLine != null) {
			clone.legendLine = ShapeUtilities.clone(this.legendLine);
		}
		clone.seriesShapesVisible = (BooleanList) this.seriesShapesVisible.clone();
		clone.seriesShapesFilled = (BooleanList) this.seriesShapesFilled.clone();
		return clone;
	}

	/**
	 * Draws the visual representation of a single data item.
	 *
	 * @param g2
	 *            the graphics device.
	 * @param state
	 *            the renderer state.
	 * @param dataArea
	 *            the area within which the data is being drawn.
	 * @param info
	 *            collects information about the drawing.
	 * @param plot
	 *            the plot (can be used to obtain standard color information etc).
	 * @param domainAxis
	 *            the domain axis.
	 * @param rangeAxis
	 *            the range axis.
	 * @param dataset
	 *            the dataset.
	 * @param series
	 *            the series index (zero-based).
	 * @param item
	 *            the item index (zero-based).
	 * @param crosshairState
	 *            crosshair information for the plot (<code>null</code> permitted).
	 * @param pass
	 *            the pass index.
	 */
	@Override
	public void drawItem(Graphics2D g2, XYItemRendererState state, Rectangle2D dataArea, PlotRenderingInfo info,
			XYPlot plot, ValueAxis domainAxis, ValueAxis rangeAxis, XYDataset dataset, int series, int item,
			CrosshairState crosshairState, int pass) {

		// do nothing if item is not visible
		if (!getItemVisible(series, item)) {
			return;
		}

		// first pass draws the background (lines, for instance)
		if (isLinePass(pass)) {
			if (getItemLineVisible(series, item)) {
				if (this.drawSeriesLineAsPath) {
					drawPrimaryLineAsPath(state, g2, plot, dataset, pass, series, item, domainAxis, rangeAxis,
							dataArea);
				} else {
					drawPrimaryLine(state, g2, plot, dataset, pass, series, item, domainAxis, rangeAxis, dataArea);
				}
			}
		}
		// second pass adds shapes where the items are ..
		else if (isItemPass(pass)) {

			// setup for collecting optional entity info...
			EntityCollection entities = null;
			if (info != null && info.getOwner() != null) {
				entities = info.getOwner().getEntityCollection();
			}

			drawSecondaryPass(g2, plot, dataset, pass, series, item, domainAxis, dataArea, rangeAxis, crosshairState,
					entities);
		}
	}

	/**
	 * Tests this renderer for equality with an arbitrary object.
	 *
	 * @param obj
	 *            the object (<code>null</code> permitted).
	 *
	 * @return <code>true</code> or <code>false</code>.
	 */
	@Override
	public boolean equals(Object obj) {

		if (obj == this) {
			return true;
		}
		if (!(obj instanceof ChartContentRenderer)) {
			return false;
		}
		if (!super.equals(obj)) {
			return false;
		}
		ChartContentRenderer that = (ChartContentRenderer) obj;
		if (!ObjectUtilities.equal(this.linesVisible, that.linesVisible)) {
			return false;
		}
		if (!ObjectUtilities.equal(this.seriesLinesVisible, that.seriesLinesVisible)) {
			return false;
		}
		if (this.baseLinesVisible != that.baseLinesVisible) {
			return false;
		}
		if (!ShapeUtilities.equal(this.legendLine, that.legendLine)) {
			return false;
		}
		if (!ObjectUtilities.equal(this.shapesVisible, that.shapesVisible)) {
			return false;
		}
		if (!ObjectUtilities.equal(this.seriesShapesVisible, that.seriesShapesVisible)) {
			return false;
		}
		if (this.baseShapesVisible != that.baseShapesVisible) {
			return false;
		}
		if (!ObjectUtilities.equal(this.shapesFilled, that.shapesFilled)) {
			return false;
		}
		if (!ObjectUtilities.equal(this.seriesShapesFilled, that.seriesShapesFilled)) {
			return false;
		}
		if (this.baseShapesFilled != that.baseShapesFilled) {
			return false;
		}
		if (this.drawOutlines != that.drawOutlines) {
			return false;
		}
		if (this.useOutlinePaint != that.useOutlinePaint) {
			return false;
		}
		if (this.useFillPaint != that.useFillPaint) {
			return false;
		}
		if (this.drawSeriesLineAsPath != that.drawSeriesLineAsPath) {
			return false;
		}
		return true;
	}

	/**
	 * Returns the base 'lines visible' attribute.
	 *
	 * @return The base flag.
	 *
	 * @see #setBaseLinesVisible(boolean)
	 */
	public boolean getBaseLinesVisible() {

		return this.baseLinesVisible;
	}

	/**
	 * Returns the base 'shape filled' attribute.
	 *
	 * @return The base flag.
	 *
	 * @see #setBaseShapesFilled(boolean)
	 */
	public boolean getBaseShapesFilled() {

		return this.baseShapesFilled;
	}

	/**
	 * Returns the base 'shape visible' attribute.
	 *
	 * @return The base flag.
	 *
	 * @see #setBaseShapesVisible(boolean)
	 */
	public boolean getBaseShapesVisible() {

		return this.baseShapesVisible;
	}

	// LINES VISIBLE

	/**
	 * Returns <code>true</code> if outlines should be drawn for shapes, and
	 * <code>false</code> otherwise.
	 *
	 * @return A boolean.
	 *
	 * @see #setDrawOutlines(boolean)
	 */
	public boolean getDrawOutlines() {

		return this.drawOutlines;
	}

	/**
	 * Returns a flag that controls whether or not each series is drawn as a single
	 * path.
	 *
	 * @return A boolean.
	 *
	 * @see #setDrawSeriesLineAsPath(boolean)
	 */
	public boolean getDrawSeriesLineAsPath() {

		return this.drawSeriesLineAsPath;
	}

	/**
	 * Returns the flag used to control whether or not the shape for an item is
	 * visible.
	 *
	 * @param series
	 *            the series index (zero-based).
	 * @param item
	 *            the item index (zero-based).
	 *
	 * @return A boolean.
	 */
	public boolean getItemLineVisible(int series, int item) {

		Boolean flag = this.linesVisible;
		if (flag == null) {
			flag = getSeriesLinesVisible(series);
		}
		if (flag != null) {
			return flag.booleanValue();
		} else {
			return this.baseLinesVisible;
		}
	}

	/**
	 * Returns the flag used to control whether or not the shape for an item is
	 * filled.
	 * <p>
	 * The default implementation passes control to the
	 * <code>getSeriesShapesFilled</code> method. You can override this method if
	 * you require different behaviour.
	 *
	 * @param series
	 *            the series index (zero-based).
	 * @param item
	 *            the item index (zero-based).
	 *
	 * @return A boolean.
	 */
	public boolean getItemShapeFilled(int series, int item) {

		Boolean flag = this.shapesFilled;
		if (flag == null) {
			flag = getSeriesShapesFilled(series);
		}
		if (flag != null) {
			return flag.booleanValue();
		} else {
			return this.baseShapesFilled;
		}
	}

	/**
	 * Returns the flag used to control whether or not the shape for an item is
	 * visible.
	 * <p>
	 * The default implementation passes control to the
	 * <code>getSeriesShapesVisible</code> method. You can override this method if
	 * you require different behaviour.
	 *
	 * @param series
	 *            the series index (zero-based).
	 * @param item
	 *            the item index (zero-based).
	 *
	 * @return A boolean.
	 */
	public boolean getItemShapeVisible(int series, int item) {

		Boolean flag = this.shapesVisible;
		if (flag == null) {
			flag = getSeriesShapesVisible(series);
		}
		if (flag != null) {
			return flag.booleanValue();
		} else {
			return this.baseShapesVisible;
		}
	}

	/**
	 * Returns a legend item for the specified series.
	 *
	 * @param datasetIndex
	 *            the dataset index (zero-based).
	 * @param series
	 *            the series index (zero-based).
	 *
	 * @return A legend item for the series (possibly {@code null}).
	 */
	@Override
	public LegendItem getLegendItem(int datasetIndex, int series) {

		XYPlot plot = getPlot();
		if (plot == null) {
			return null;
		}

		XYDataset dataset = plot.getDataset(datasetIndex);
		if (dataset == null) {
			return null;
		}

		if (!getItemVisible(series, 0)) {
			return null;
		}
		String label = getLegendItemLabelGenerator().generateLabel(dataset, series);
		String description = label;
		String toolTipText = null;
		if (getLegendItemToolTipGenerator() != null) {
			toolTipText = getLegendItemToolTipGenerator().generateLabel(dataset, series);
		}
		String urlText = null;
		if (getLegendItemURLGenerator() != null) {
			urlText = getLegendItemURLGenerator().generateLabel(dataset, series);
		}
		// String st = dataset.getSeriesKey(series).toString();
		// System.out.println(st + "
		// " + elementOrder.indexOf(st));
		// Paint paint = (chart.getSeriesColor(this.nameOrder.indexOf(st)));
		Paint paint = (this.useFillPaint ? lookupSeriesFillPaint(series) : lookupSeriesPaint(series));
		boolean shapeIsVisible = getItemShapeVisible(series, 0);
		Shape shape = lookupLegendShape(series);
		boolean shapeIsFilled = getItemShapeFilled(series, 0);
		Paint fillPaint = paint;// (this.useFillPaint ? lookupSeriesFillPaint(series) :
								// lookupSeriesPaint(series));
		boolean shapeOutlineVisible = this.drawOutlines;
		Paint outlinePaint = (this.useOutlinePaint ? lookupSeriesOutlinePaint(series) : lookupSeriesPaint(series));
		Stroke outlineStroke = lookupSeriesOutlineStroke(series);
		boolean lineVisible = getItemLineVisible(series, 0);
		Stroke lineStroke = lookupSeriesStroke(series);
		Paint linePaint = paint;// lookupSeriesPaint(series);
		LegendItem result = new LegendItem(label, description, toolTipText, urlText, shapeIsVisible, shape,
				shapeIsFilled, fillPaint, shapeOutlineVisible, outlinePaint, outlineStroke, lineVisible,
				this.legendLine, lineStroke, linePaint);
		result.setLabelFont(lookupLegendTextFont(series));
		Paint labelPaint = lookupLegendTextPaint(series);
		if (labelPaint != null) {
			result.setLabelPaint(labelPaint);
		}
		result.setSeriesKey(dataset.getSeriesKey(series));
		result.setSeriesIndex(series);
		result.setDataset(dataset);
		result.setDatasetIndex(datasetIndex);

		return result;
	}

	/**
	 * Returns the shape used to represent a line in the legend.
	 *
	 * @return The legend line (never <code>null</code>).
	 *
	 * @see #setLegendLine(Shape)
	 */
	public Shape getLegendLine() {

		return this.legendLine;
	}

	/**
	 * Returns a flag that controls whether or not lines are drawn for ALL series.
	 * If this flag is <code>null</code>, then the "per series" settings will apply.
	 *
	 * @return A flag (possibly <code>null</code>).
	 *
	 * @see #setLinesVisible(Boolean)
	 *
	 * @deprecated As of 1.0.7, use the per-series and base level settings.
	 */
	@Deprecated
	public Boolean getLinesVisible() {

		return this.linesVisible;
	}

	/**
	 * Returns the number of passes through the data that the renderer requires in
	 * order to draw the chart. Most charts will require a single pass, but some
	 * require two passes.
	 *
	 * @return The pass count.
	 */
	@Override
	public int getPassCount() {

		return 2;
	}

	/**
	 * Returns the flag used to control whether or not the lines for a series are
	 * visible.
	 *
	 * @param series
	 *            the series index (zero-based).
	 *
	 * @return The flag (possibly <code>null</code>).
	 *
	 * @see #setSeriesLinesVisible(int, Boolean)
	 */
	public Boolean getSeriesLinesVisible(int series) {

		return this.seriesLinesVisible.getBoolean(series);
	}

	/**
	 * Returns the flag used to control whether or not the shapes for a series are
	 * filled.
	 *
	 * @param series
	 *            the series index (zero-based).
	 *
	 * @return A boolean.
	 *
	 * @see #setSeriesShapesFilled(int, Boolean)
	 */
	public Boolean getSeriesShapesFilled(int series) {

		return this.seriesShapesFilled.getBoolean(series);
	}

	// SHAPES VISIBLE

	/**
	 * Returns the flag used to control whether or not the shapes for a series are
	 * visible.
	 *
	 * @param series
	 *            the series index (zero-based).
	 *
	 * @return A boolean.
	 *
	 * @see #setSeriesShapesVisible(int, Boolean)
	 */
	public Boolean getSeriesShapesVisible(int series) {

		return this.seriesShapesVisible.getBoolean(series);
	}

	/**
	 * Returns the flag that controls whether the shapes are visible for the items
	 * in ALL series.
	 *
	 * @return The flag (possibly <code>null</code>).
	 *
	 * @see #setShapesVisible(Boolean)
	 *
	 * @deprecated As of 1.0.7, use the per-series and base level settings.
	 */
	@Deprecated
	public Boolean getShapesVisible() {

		return this.shapesVisible;
	}

	/**
	 * Returns <code>true</code> if the renderer should use the fill paint setting
	 * to fill shapes, and <code>false</code> if it should just use the regular
	 * paint.
	 * <p>
	 * Refer to <code>XYLineAndShapeRendererDemo2.java</code> to see the effect of
	 * this flag.
	 *
	 * @return A boolean.
	 *
	 * @see #setUseFillPaint(boolean)
	 * @see #getUseOutlinePaint()
	 */
	public boolean getUseFillPaint() {

		return this.useFillPaint;
	}

	/**
	 * Returns <code>true</code> if the renderer should use the outline paint
	 * setting to draw shape outlines, and <code>false</code> if it should just use
	 * the regular paint.
	 *
	 * @return A boolean.
	 *
	 * @see #setUseOutlinePaint(boolean)
	 * @see #getUseFillPaint()
	 */
	public boolean getUseOutlinePaint() {

		return this.useOutlinePaint;
	}

	/**
	 * Initialises the renderer.
	 * <P>
	 * This method will be called before the first item is rendered, giving the
	 * renderer an opportunity to initialise any state information it wants to
	 * maintain. The renderer can do nothing if it chooses.
	 *
	 * @param g2
	 *            the graphics device.
	 * @param dataArea
	 *            the area inside the axes.
	 * @param plot
	 *            the plot.
	 * @param data
	 *            the data.
	 * @param info
	 *            an optional info collection object to return data back to the
	 *            caller.
	 *
	 * @return The renderer state.
	 */
	@Override
	public XYItemRendererState initialise(Graphics2D g2, Rectangle2D dataArea, XYPlot plot, XYDataset data,
			PlotRenderingInfo info) {

		return new State(info);
	}

	/**
	 * Sets the base 'lines visible' flag and sends a {@link RendererChangeEvent} to
	 * all registered listeners.
	 *
	 * @param flag
	 *            the flag.
	 *
	 * @see #getBaseLinesVisible()
	 */
	public void setBaseLinesVisible(boolean flag) {

		this.baseLinesVisible = flag;
		fireChangeEvent();
	}

	/**
	 * Sets the base 'shapes filled' flag and sends a {@link RendererChangeEvent} to
	 * all registered listeners.
	 *
	 * @param flag
	 *            the flag.
	 *
	 * @see #getBaseShapesFilled()
	 */
	public void setBaseShapesFilled(boolean flag) {

		this.baseShapesFilled = flag;
		fireChangeEvent();
	}

	/**
	 * Sets the base 'shapes visible' flag and sends a {@link RendererChangeEvent}
	 * to all registered listeners.
	 *
	 * @param flag
	 *            the flag.
	 *
	 * @see #getBaseShapesVisible()
	 */
	public void setBaseShapesVisible(boolean flag) {

		this.baseShapesVisible = flag;
		fireChangeEvent();
	}

	/**
	 * Sets the flag that controls whether outlines are drawn for shapes, and sends
	 * a {@link RendererChangeEvent} to all registered listeners.
	 * <P>
	 * In some cases, shapes look better if they do NOT have an outline, but this
	 * flag allows you to set your own preference.
	 *
	 * @param flag
	 *            the flag.
	 *
	 * @see #getDrawOutlines()
	 */
	public void setDrawOutlines(boolean flag) {

		this.drawOutlines = flag;
		fireChangeEvent();
	}

	// SHAPES FILLED

	/**
	 * Sets the flag that controls whether or not each series is drawn as a single
	 * path and sends a {@link RendererChangeEvent} to all registered listeners.
	 *
	 * @param flag
	 *            the flag.
	 *
	 * @see #getDrawSeriesLineAsPath()
	 */
	public void setDrawSeriesLineAsPath(boolean flag) {

		if (this.drawSeriesLineAsPath != flag) {
			this.drawSeriesLineAsPath = flag;
			fireChangeEvent();
		}
	}

	/**
	 * Sets the shape used as a line in each legend item and sends a
	 * {@link RendererChangeEvent} to all registered listeners.
	 *
	 * @param line
	 *            the line (<code>null</code> not permitted).
	 *
	 * @see #getLegendLine()
	 */
	public void setLegendLine(Shape line) {

		// ParamChecks.nullNotPermitted(line, "line");
		this.legendLine = line;
		fireChangeEvent();
	}

	/**
	 * Sets a flag that controls whether or not lines are drawn between the items in
	 * ALL series, and sends a {@link RendererChangeEvent} to all registered
	 * listeners.
	 *
	 * @param visible
	 *            the flag.
	 *
	 * @see #getLinesVisible()
	 *
	 * @deprecated As of 1.0.7, use the per-series and base level settings.
	 */
	@Deprecated
	public void setLinesVisible(boolean visible) {

		setLinesVisible(Boolean.valueOf(visible));
	}

	/**
	 * Sets a flag that controls whether or not lines are drawn between the items in
	 * ALL series, and sends a {@link RendererChangeEvent} to all registered
	 * listeners. You need to set this to <code>null</code> if you want the "per
	 * series" settings to apply.
	 *
	 * @param visible
	 *            the flag (<code>null</code> permitted).
	 *
	 * @see #getLinesVisible()
	 *
	 * @deprecated As of 1.0.7, use the per-series and base level settings.
	 */
	@Deprecated
	public void setLinesVisible(Boolean visible) {

		this.linesVisible = visible;
		fireChangeEvent();
	}

	/**
	 * Sets the 'lines visible' flag for a series and sends a
	 * {@link RendererChangeEvent} to all registered listeners.
	 *
	 * @param series
	 *            the series index (zero-based).
	 * @param visible
	 *            the flag.
	 *
	 * @see #getSeriesLinesVisible(int)
	 */
	public void setSeriesLinesVisible(int series, boolean visible) {

		setSeriesLinesVisible(series, Boolean.valueOf(visible));
	}

	/**
	 * Sets the 'lines visible' flag for a series and sends a
	 * {@link RendererChangeEvent} to all registered listeners.
	 *
	 * @param series
	 *            the series index (zero-based).
	 * @param flag
	 *            the flag (<code>null</code> permitted).
	 *
	 * @see #getSeriesLinesVisible(int)
	 */
	public void setSeriesLinesVisible(int series, Boolean flag) {

		this.seriesLinesVisible.setBoolean(series, flag);
		fireChangeEvent();
	}

	/**
	 * Sets the 'shapes filled' flag for a series and sends a
	 * {@link RendererChangeEvent} to all registered listeners.
	 *
	 * @param series
	 *            the series index (zero-based).
	 * @param flag
	 *            the flag.
	 *
	 * @see #getSeriesShapesFilled(int)
	 */
	public void setSeriesShapesFilled(int series, boolean flag) {

		setSeriesShapesFilled(series, Boolean.valueOf(flag));
	}

	/**
	 * Sets the 'shapes filled' flag for a series and sends a
	 * {@link RendererChangeEvent} to all registered listeners.
	 *
	 * @param series
	 *            the series index (zero-based).
	 * @param flag
	 *            the flag.
	 *
	 * @see #getSeriesShapesFilled(int)
	 */
	public void setSeriesShapesFilled(int series, Boolean flag) {

		this.seriesShapesFilled.setBoolean(series, flag);
		fireChangeEvent();
	}

	/**
	 * Sets the 'shapes visible' flag for a series and sends a
	 * {@link RendererChangeEvent} to all registered listeners.
	 *
	 * @param series
	 *            the series index (zero-based).
	 * @param visible
	 *            the flag.
	 *
	 * @see #getSeriesShapesVisible(int)
	 */
	public void setSeriesShapesVisible(int series, boolean visible) {

		setSeriesShapesVisible(series, Boolean.valueOf(visible));
	}

	/**
	 * Sets the 'shapes visible' flag for a series and sends a
	 * {@link RendererChangeEvent} to all registered listeners.
	 *
	 * @param series
	 *            the series index (zero-based).
	 * @param flag
	 *            the flag.
	 *
	 * @see #getSeriesShapesVisible(int)
	 */
	public void setSeriesShapesVisible(int series, Boolean flag) {

		this.seriesShapesVisible.setBoolean(series, flag);
		fireChangeEvent();
	}

	/**
	 * Sets the 'shapes filled' for ALL series and sends a
	 * {@link RendererChangeEvent} to all registered listeners.
	 *
	 * @param filled
	 *            the flag.
	 *
	 * @deprecated As of 1.0.7, use the per-series and base level settings.
	 */
	@Deprecated
	public void setShapesFilled(boolean filled) {

		setShapesFilled(Boolean.valueOf(filled));
	}

	/**
	 * Sets the 'shapes filled' for ALL series and sends a
	 * {@link RendererChangeEvent} to all registered listeners.
	 *
	 * @param filled
	 *            the flag (<code>null</code> permitted).
	 *
	 * @deprecated As of 1.0.7, use the per-series and base level settings.
	 */
	@Deprecated
	public void setShapesFilled(Boolean filled) {

		this.shapesFilled = filled;
		fireChangeEvent();
	}

	/**
	 * Sets the 'shapes visible' for ALL series and sends a
	 * {@link RendererChangeEvent} to all registered listeners.
	 *
	 * @param visible
	 *            the flag.
	 *
	 * @see #getShapesVisible()
	 *
	 * @deprecated As of 1.0.7, use the per-series and base level settings.
	 */
	@Deprecated
	public void setShapesVisible(boolean visible) {

		setShapesVisible(Boolean.valueOf(visible));
	}

	/**
	 * Sets the 'shapes visible' for ALL series and sends a
	 * {@link RendererChangeEvent} to all registered listeners.
	 *
	 * @param visible
	 *            the flag (<code>null</code> permitted).
	 *
	 * @see #getShapesVisible()
	 *
	 * @deprecated As of 1.0.7, use the per-series and base level settings.
	 */
	@Deprecated
	public void setShapesVisible(Boolean visible) {

		this.shapesVisible = visible;
		fireChangeEvent();
	}

	/**
	 * Sets the flag that controls whether the fill paint is used to fill shapes,
	 * and sends a {@link RendererChangeEvent} to all registered listeners.
	 *
	 * @param flag
	 *            the flag.
	 *
	 * @see #getUseFillPaint()
	 */
	public void setUseFillPaint(boolean flag) {

		this.useFillPaint = flag;
		fireChangeEvent();
	}

	/**
	 * Sets the flag that controls whether the outline paint is used to draw shape
	 * outlines, and sends a {@link RendererChangeEvent} to all registered
	 * listeners.
	 * <p>
	 * Refer to <code>XYLineAndShapeRendererDemo2.java</code> to see the effect of
	 * this flag.
	 *
	 * @param flag
	 *            the flag.
	 *
	 * @see #getUseOutlinePaint()
	 */
	public void setUseOutlinePaint(boolean flag) {

		this.useOutlinePaint = flag;
		fireChangeEvent();
	}

	/**
	 * Provides serialization support.
	 *
	 * @param stream
	 *            the input stream.
	 *
	 * @throws IOException
	 *             if there is an I/O error.
	 * @throws ClassNotFoundException
	 *             if there is a classpath problem.
	 */
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {

		stream.defaultReadObject();
		this.legendLine = SerialUtilities.readShape(stream);
	}

	/**
	 * Load specified stroke and paint configuration
	 * 
	 * @param dataset
	 *            data set to be rendered
	 */
	private void setupPaintAndStrokeConfig() {

		int i = 0;
		ArrayList<String> sortedSeriesNames = new ArrayList<String>(chartData.getData().getSeriesMap().keySet());
		Collections.sort(sortedSeriesNames);
		for (String seriesName : sortedSeriesNames) {
			// System.out.println(seriesName);

			DataSeries series = chartData.getData().getSeries(seriesName);
			Paint paint = series.get(Chart.SERIES_COLOR);
			Stroke stroke = series.get(Chart.SERIES_STROKE);
			if (paint == null) {
				paint = ((ArrayList<Paint>) chartData.get(Chart.DEFAULT_SERIES_COLOR)).get(i);
			}
			if (stroke == null) {
				stroke = chartData.get(Chart.DEFAULT_FLOW_STROKE);
			}
			// System.out.println(paint.toString() + " " + stroke.toString());
			this.setSeriesPaint(i, paint);
			this.setSeriesFillPaint(i, paint);
			this.setSeriesStroke(i, stroke);
			i = i + 1;
		}

	}

	/**
	 * Provides serialization support.
	 *
	 * @param stream
	 *            the output stream.
	 *
	 * @throws IOException
	 *             if there is an I/O error.
	 */
	private void writeObject(ObjectOutputStream stream) throws IOException {

		stream.defaultWriteObject();
		SerialUtilities.writeShape(this.legendLine, stream);
	}

	/**
	 * * Draws the first pass shape.
	 *
	 * @param g2
	 *            the graphics device.
	 * @param pass
	 *            the pass.
	 * @param series
	 *            the series index.
	 * @param item
	 *            the item index.
	 * @param shape
	 *            the shape.
	 * @param dataset
	 *            data in use
	 */
	protected void drawFirstPassShape(Graphics2D g2, int pass, int series, int item, Shape shape, XYDataset dataset) {

		if (this.nameOrder != null) {
			g2.setPaint(getSeriesPaint(series));
		} else {
			g2.setPaint(getItemPaint(series, item));
		}
		g2.draw(shape);
	}

	protected XYSeriesCollectionMultiDomain checkExtraDomain(XYDataset dataset) {

		if (!multiDomain.containsKey(dataset)) {
			XYSeriesCollectionMultiDomain d = new XYSeriesCollectionMultiDomain();
			if (FieldFinder.containsSuper(dataset, XYSeriesCollectionMultiDomain.class)) {
				d = (XYSeriesCollectionMultiDomain) dataset;
			}
			multiDomain.put(dataset, d);
		}
		return multiDomain.get(dataset);
	}

	/**
	 * Draws the item (first pass). This method draws the lines connecting the
	 * items.
	 *
	 * @param g2
	 *            the graphics device.
	 * @param state
	 *            the renderer state.
	 * @param dataArea
	 *            the area within which the data is being drawn.
	 * @param plot
	 *            the plot (can be used to obtain standard color information etc).
	 * @param domainAxis
	 *            the domain axis.
	 * @param rangeAxis
	 *            the range axis.
	 * @param dataset
	 *            the dataset.
	 * @param pass
	 *            the pass.
	 * @param series
	 *            the series index (zero-based).
	 * @param item
	 *            the item index (zero-based).
	 */
	protected void drawPrimaryLine(XYItemRendererState state, Graphics2D g2, XYPlot plot, XYDataset dataset, int pass,
			int series, int item, ValueAxis domainAxis, ValueAxis rangeAxis, Rectangle2D dataArea) {

		if (item == 0) {
			return;
		}

		// get the data point...
		double x1 = dataset.getXValue(series, item);
		double y1 = dataset.getYValue(series, item);
		if (Double.isNaN(y1) || Double.isNaN(x1)) {
			return;
		}

		double x0 = dataset.getXValue(series, item - 1);
		double y0 = dataset.getYValue(series, item - 1);
		if (Double.isNaN(y0) || Double.isNaN(x0)) {
			return;
		}
		try {
			// System.out.println("item= " + item + " x0=" + x0 + " x1=" + x1 + " y0=" + y0
			// + " y1=" + y1);
			String extraDomainJump = checkExtraDomain(dataset).jumpOccurring(series, item, x0, y0, x1, y1);
			// if ((x1 != x0) && (extraDomainJump)) {
			// g2.setStroke(this.getSeriesStroke(series));
			//
			// } else {
			//
			// Stroke stroke1 = chartData.get(Chart.DEFAULT_JUMP_STROKE);
			// g2.setStroke(stroke1);
			// }
			if (((x1 == x0) && (extraDomainJump.equals(NO_ALT_DOMAIN))
					|| extraDomainJump.equals(JUMP_OCCURRING_ALT_DOMAIN))) {
				Stroke stroke1 = chartData.get(Chart.DEFAULT_JUMP_STROKE);
				g2.setStroke(stroke1);
			} else {

				g2.setStroke(this.getSeriesStroke(series));
			}
			RectangleEdge xAxisLocation = plot.getDomainAxisEdge();
			RectangleEdge yAxisLocation = plot.getRangeAxisEdge();

			double transX0 = domainAxis.valueToJava2D(x0, dataArea, xAxisLocation);
			double transY0 = rangeAxis.valueToJava2D(y0, dataArea, yAxisLocation);

			double transX1 = domainAxis.valueToJava2D(x1, dataArea, xAxisLocation);
			double transY1 = rangeAxis.valueToJava2D(y1, dataArea, yAxisLocation);

			// only draw if we have good values
			if (Double.isNaN(transX0) || Double.isNaN(transY0) || Double.isNaN(transX1) || Double.isNaN(transY1)) {
				return;
			}

			PlotOrientation orientation = plot.getOrientation();
			boolean visible;
			if (orientation == PlotOrientation.HORIZONTAL) {
				state.workingLine.setLine(transY0, transX0, transY1, transX1);
			} else if (orientation == PlotOrientation.VERTICAL) {
				state.workingLine.setLine(transX0, transY0, transX1, transY1);
			}
			visible = LineUtilities.clipLine(state.workingLine, dataArea);
			if (visible) {
				drawFirstPassShape(g2, pass, series, item, state.workingLine, dataset);
			}
		} catch (Exception e) {

		}

	}

	/**
	 * Draws the
	 * 
	 * item (first pass). This method draws the lines connecting the items. Instead
	 * of drawing separate lines, a GeneralPath is constructed and drawn at the end
	 * of the series painting.
	 *
	 * @param g2
	 *            the graphics device.
	 * @param state
	 *            the renderer state.
	 * @param plot
	 *
	 * 
	 *            the plot (can be used to obtain standard color information etc).
	 * @param dataset
	 *            the dataset.
	 * @param pass
	 *            the pass.
	 * @param series
	 *            the series index (zero-based).
	 * @param item
	 *            the item index (zero-based).
	 * @param domainAxis
	 *            the domain axis.
	 * @param rangeAxis
	 *            the range axis.
	 * @param dataArea
	 *            the area within which the data is being drawn.
	 */

	protected void drawPrimaryLineAsPath(XYItemRendererState state, Graphics2D g2, XYPlot plot, XYDataset dataset,
			int pass, int series, int item, ValueAxis domainAxis, ValueAxis rangeAxis, Rectangle2D dataArea) {

		RectangleEdge xAxisLocation = plot.getDomainAxisEdge();
		RectangleEdge yAxisLocation = plot.getRangeAxisEdge();

		// get the data point...
		double x1 = dataset.getXValue(series, item);
		double y1 = dataset.getYValue(series, item);
		double transX1 = domainAxis.valueToJava2D(x1, dataArea, xAxisLocation);
		double transY1 = rangeAxis.valueToJava2D(y1, dataArea, yAxisLocation);
		// dataset.getX(series, item + 1);
		try {
			// if (x1 != dataset.getXValue(series, item - 1))
			{
				State s = (State) state;
				// update path to reflect latest point
				if (!Double.isNaN(transX1) && !Double.isNaN(transY1)) {
					float x = (float) transX1;
					float y = (float) transY1;
					PlotOrientation orientation = plot.getOrientation();
					if (orientation == PlotOrientation.HORIZONTAL) {
						x = (float) transY1;
						y = (float) transX1;
					}
					if (s.isLastPointGood()) {
						s.seriesPath.lineTo(x, y);
					} else {
						s.seriesPath.moveTo(x, y);
					}
					s.setLastPointGood(true);
				} else {
					s.setLastPointGood(false);
				}
				// if this is the last item, draw the path ...
				if (item == s.getLastItemIndex()) {
					// draw path
					drawFirstPassShape(g2, pass, series, item, s.seriesPath, dataset);
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	/**
	 * Draws the item shapes and adds chart entities (second pass). This method
	 * draws the shapes which mark the item positions. If <code>entities</code> is
	 * not <code>null</code> it will be populated with entity information for points
	 * that fall within the data area.
	 *
	 * @param g2
	 *            the graphics device.
	 * @param plot
	 *            the plot (can be used to obtain standard color information etc).
	 * @param domainAxis
	 *            the domain axis.
	 * @param dataArea
	 *            the area within which the data is being drawn.
	 * @param rangeAxis
	 *            the range axis.
	 * @param dataset
	 *            the dataset.
	 * @param pass
	 *            the pass.
	 * @param series
	 *            the series index (zero-based).
	 * @param item
	 *            the item index (zero-based).
	 * @param crosshairState
	 *            the crosshair state.
	 * @param entities
	 *            the entity collection.
	 */
	protected void drawSecondaryPass(Graphics2D g2, XYPlot plot, XYDataset dataset, int pass, int series, int item,
			ValueAxis domainAxis, Rectangle2D dataArea, ValueAxis rangeAxis, CrosshairState crosshairState,
			EntityCollection entities) {

		Shape entityArea = null;

		// get the data point...
		double x1 = dataset.getXValue(series, item);
		double y1 = dataset.getYValue(series, item);
		if (Double.isNaN(y1) || Double.isNaN(x1)) {
			return;
		}
		try {
			// if (x1 != dataset.getXValue(series, item - 1))
			{
				PlotOrientation orientation = plot.getOrientation();
				RectangleEdge xAxisLocation = plot.getDomainAxisEdge();
				RectangleEdge yAxisLocation = plot.getRangeAxisEdge();
				double transX1 = domainAxis.valueToJava2D(x1, dataArea, xAxisLocation);
				double transY1 = rangeAxis.valueToJava2D(y1, dataArea, yAxisLocation);

				if (getItemShapeVisible(series, item)) {
					Shape shape = getItemShape(series, item);
					if (orientation == PlotOrientation.HORIZONTAL) {
						shape = ShapeUtilities.createTranslatedShape(shape, transY1, transX1);
					} else if (orientation == PlotOrientation.VERTICAL) {
						shape = ShapeUtilities.createTranslatedShape(shape, transX1, transY1);
					}
					entityArea = shape;
					if (shape.intersects(dataArea)) {
						if (getItemShapeFilled(series, item)) {
							if (this.useFillPaint) {
								g2.setPaint(getItemFillPaint(series, item));
							} else {
								g2.setPaint(getItemPaint(series, item));
							}
							g2.fill(shape);
						}
						if (this.drawOutlines) {
							if (getUseOutlinePaint()) {
								g2.setPaint(getItemOutlinePaint(series, item));
							} else {
								g2.setPaint(getItemPaint(series, item));
							}
							g2.setStroke(getItemOutlineStroke(series, item));
							g2.draw(shape);
						}
					}
				}

				double xx = transX1;
				double yy = transY1;
				if (orientation == PlotOrientation.HORIZONTAL) {
					xx = transY1;
					yy = transX1;
				}

				// draw the item label if there is one...
				if (isItemLabelVisible(series, item)) {
					drawItemLabel(g2, orientation, dataset, series, item, xx, yy, (y1 < 0.0));
				}

				int domainAxisIndex = plot.getDomainAxisIndex(domainAxis);
				int rangeAxisIndex = plot.getRangeAxisIndex(rangeAxis);
				updateCrosshairValues(crosshairState, x1, y1, domainAxisIndex, rangeAxisIndex, transX1, transY1,
						orientation);

				// add an entity for the item, but only if it falls within the data
				// area...
				if (entities != null && isPointInRect(dataArea, xx, yy)) {
					addEntity(entities, entityArea, dataset, series, item, xx, yy);
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	/**
	 * Returns <code>true</code> if the specified pass is the one for drawing items.
	 *
	 * @param pass
	 *            the pass.
	 *
	 * @return A boolean.
	 */
	protected boolean isItemPass(int pass) {

		return pass == 1;
	}

	/**
	 * Returns <code>true</code> if the specified pass is the one for drawing lines.
	 *
	 * @param pass
	 *            the pass.
	 *
	 * @return A boolean.
	 */
	protected boolean isLinePass(int pass) {

		return pass == 0;
	}

	/**
	 * 
	 * 
	 * /** Creates a new renderer.
	 *
	 * @param type
	 *            chart type to render
	 * 
	 * @param data
	 *            trajectory set containing the data information
	 * @param chart
	 *            rendering properties defining how lines and shapes will be
	 *            generated
	 * @param ds
	 *            data set defining exactly what components will be plotted
	 */
	public ChartContentRenderer(Chart chart) {

		chartData = chart;

		this.seriesLinesVisible = new BooleanList();
		updateRenderer();

		this.legendLine = new Line2D.Double(-7.0, 0.0, 7.0, 0.0);

		this.seriesShapesVisible = new BooleanList();

		this.useFillPaint = false; // use item paint for fills by default
		this.seriesShapesFilled = new BooleanList();
		this.baseShapesFilled = true;

		this.drawOutlines = true;
		this.useOutlinePaint = false; // use item paint for outlines by
										// default, not outline paint
		this.drawSeriesLineAsPath = false;
	}

	public void updateRenderer() {

		setupPaintAndStrokeConfig();
		this.baseLinesVisible = ChartRenderData.getRenderType(chartData.get(Chart.TYPE).toString()).getRenderLines();
		this.baseShapesVisible = ChartRenderData.getRenderType(chartData.get(Chart.TYPE).toString()).getRenderShapes();
	}

	public HashMap<XYDataset, XYSeriesCollectionMultiDomain> multiDomain = new HashMap<XYDataset, XYSeriesCollectionMultiDomain>();

	/**
	 * Records the state for the renderer. This is used to preserve state
	 * information between calls to the drawItem() method for a single chart
	 * drawing.
	 */
	public static class State extends XYItemRendererState {

		/** The path for the current series. */
		public GeneralPath seriesPath;

		/**
		 * A flag that indicates if the last (x, y) point was 'good' (non-null).
		 */
		private boolean lastPointGood;

		/**
		 * Returns a flag that indicates if the last point drawn (in the current series)
		 * was 'good' (non-null).
		 *
		 * @return A boolean.
		 */
		public boolean isLastPointGood() {

			return this.lastPointGood;
		}

		/**
		 * Sets a flag that indicates if the last point drawn (in the current series)
		 * was 'good' (non-null).
		 *
		 * @param good
		 *            the flag.
		 */
		public void setLastPointGood(boolean good) {

			this.lastPointGood = good;
		}

		/**
		 * This method is called by the {@link XYPlot} at the start of each series pass.
		 * We reset the state for the current series.
		 *
		 * @param dataset
		 *            the dataset.
		 * @param series
		 *            the series index.
		 * @param firstItem
		 *            the first item index for this pass.
		 * @param lastItem
		 *            the last item index for this pass.
		 * @param pass
		 *            the current pass index.
		 * @param passCount
		 *            the number of passes.
		 */
		@Override
		public void startSeriesPass(XYDataset dataset, int series, int firstItem, int lastItem, int pass,
				int passCount) {

			this.seriesPath.reset();
			this.lastPointGood = false;
			super.startSeriesPass(dataset, series, firstItem, lastItem, pass, passCount);
		}

		/**
		 * Creates a new state instance.
		 *
		 * @param info
		 *            the plot rendering info.
		 */
		public State(PlotRenderingInfo info) {

			super(info);
			this.seriesPath = new GeneralPath();
		}

	}

	/** For serialization. */
	private static final long serialVersionUID = -7435246895986425885L;

}
