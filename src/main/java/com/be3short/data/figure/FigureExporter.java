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
 * FigureExporter.java
 * ------------------------------------------------
 *
 * Original Author:  Brendan Short
 * Contributor(s):   
 *
 * Changes:
 * --------
 * 01-June-2018 : Version 1 (BS);
 *
 */

package com.be3short.data.figure;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.apache.fop.svg.PDFDocumentGraphics2D;
import org.apache.log4j.Logger;
import org.apache.xmlgraphics.java2d.ps.EPSDocumentGraphics2D;
import org.jfree.graphics2d.svg.SVGGraphics2D;

import com.be3short.data.graphics.GraphicFormat;
import com.be3short.util.io.XMLParser;

/**
 * A set of utilities for performing the steps necessary to export a figure in
 * any of the supported formats.
 * 
 * Intended Operator: System
 */
public class FigureExporter {

	private static Logger log = Logger.getLogger(FigureExporter.class);

	/**
	 * Close a figure window after the graphics have been printed to file
	 * 
	 * @param out
	 *            output stream to write graphics to file
	 * @param g2d
	 *            graphics component
	 * @param format
	 *            image format
	 * @return graphics component
	 * @throws Exception
	 *             unable to close graphics and figure
	 */
	private static Graphics2D closeWindows(OutputStream out, Graphics2D g2d, GraphicFormat format) throws Exception {

		switch (format) {
		case EPS:
			((EPSDocumentGraphics2D) g2d).finish();
			break;
		case PDF:
			((PDFDocumentGraphics2D) g2d).finish();
			break;
		case SVG:
			String svg = ((SVGGraphics2D) g2d).getSVGDocument();
			((BufferedOutputStream) out).write(svg.getBytes());
			((SVGGraphics2D) g2d).finalize();
			break;
		default:
			break;
		}
		return g2d;
	}

	/**
	 * Exports the figure to an output file in a specified image form
	 * 
	 * @param frame
	 *            location where the image file is to be created
	 * 
	 */
	public static void ensureVisibility(FigurePane frame) {

		if (!frame.getFrame().isVisible()) {
			frame.display();
		}

	}

	/**
	 * * Exports the figure to an output file in a specified image form
	 * 
	 * @param location
	 *            location where the image file is to be created
	 * @param frame
	 *            figure to be exported
	 * @param format
	 *            output file format
	 * @return true if export successful
	 */
	public static boolean exportToFile(File location, FigurePane frame, GraphicFormat format) {

		ensureVisibility(frame);
		File checkedLocation = format.getUtilities().appendExtension(location);
		boolean success = false;
		if (format.rastered) {
			success = renderRasteredImageFile(checkedLocation, frame, format);
		} else {
			success = renderVectoredImageFile(checkedLocation, frame, format);

		}
		return success;
	}

	/**
	 * Prepare image graphics
	 * 
	 * @param g2d
	 *            graphics component containing image
	 * @param out
	 *            output stream to print image to
	 * @param w
	 *            width of image
	 * @param h
	 *            height of image
	 * @param format
	 *            image format
	 * @return graphics component with image
	 * @throws Exception
	 *             unable to setup the graphics component
	 */
	private static Graphics2D prepareImage(Graphics2D g2d, OutputStream out, int w, int h, GraphicFormat format)
			throws Exception {

		switch (format) {
		case EPS:
			EPSDocumentGraphics2D g3d = new EPSDocumentGraphics2D(true);
			g3d.setGraphicContext(new org.apache.xmlgraphics.java2d.GraphicContext());
			g3d.setupDocument(out, w, h);
			g2d = g3d;
			break;
		case PDF:
			PDFDocumentGraphics2D g4d = new PDFDocumentGraphics2D(true);
			g4d.setGraphicContext(new org.apache.xmlgraphics.java2d.GraphicContext());
			g4d.setupDocument(out, w, h);
			g2d = g4d;
			break;
		case SVG:
			SVGGraphics2D g6d = new SVGGraphics2D(w, h);
			g2d = g6d;
			break;
		default:
			break;
		}
		return g2d;
	}

	/**
	 * Render figure to rastered image file
	 * 
	 * @param f
	 *            file location
	 * @param app
	 *            figure to be rendered
	 * @param format
	 *            output file format
	 * @return true if raster successful
	 */
	private static boolean renderRasteredImageFile(File f, FigurePane app, GraphicFormat format) {

		int w = app.rootPanel.getWidth();
		int h = app.rootPanel.getHeight();
		log.debug(XMLParser.serializeObject(app.rootPanel.getSize()));

		BufferedImage bimg = new BufferedImage(w, h, BufferedImage.SCALE_SMOOTH);
		Graphics2D graphics = (Graphics2D) bimg.getGraphics();
		app.rootPanel.setOpaque(true);
		app.rootPanel.setBackground(Color.white);
		app.rootPanel.paint(graphics);

		try {
			ImageIO.write(bimg, format.getFileExtension().substring(1), f);
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	/**
	 * Render figure to vectored image file
	 * 
	 * @param f
	 *            file location
	 * @param app
	 *            figure to be rendered
	 * @param format
	 *            output file format
	 * @return true if render successful
	 */
	private static boolean renderVectoredImageFile(File f, FigurePane app, GraphicFormat format) {

		int w = app.rootPanel.getWidth();
		int h = app.rootPanel.getHeight();
		log.debug(XMLParser.serializeObject(app.rootPanel.getSize()));
		// Initialize the general transfer buffer
		Graphics2D g2d = null;
		try {
			final OutputStream out = new BufferedOutputStream(new FileOutputStream(f));

			g2d = prepareImage(g2d, out, w, h, format);
			app.rootPanel.paint(g2d);
			closeWindows(out, g2d, format);

			out.flush();
			out.close();

		} catch (

		Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}
}
