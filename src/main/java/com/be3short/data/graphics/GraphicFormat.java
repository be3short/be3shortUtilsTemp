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
 * GraphicFormat.java
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

package com.be3short.data.graphics;

import java.util.HashMap;

import com.be3short.io.format.FileFormatProperties;
import com.be3short.io.format.FileFormatUtilities;

/**
 * This enumeration defines the graphic formats that are currently supported to
 * export figures.
 * 
 * Intended Operator: User
 */
public enum GraphicFormat implements FileFormatProperties<GraphicFormat> {

	/**
	 * Bit mapped image format.
	 */
	BMP("BMP", ".bmp", true),
	/**
	 * Encapsulated post script format.
	 */
	EPS("EPS", ".eps", false),

	/**
	 * Graphics interchangable format.
	 */
	GIF("gif", ".gif", true),

	/**
	 * Joint photographic experts group format.
	 */
	JPEG("JPEG", ".jpg", true),

	/**
	 * Portable document format.
	 */
	PDF("PDF", ".pdf", false),

	/**
	 * Portable network graphics format.
	 */
	PNG("PNG", ".png", true),

	/**
	 * Standard vector graphics format.
	 */
	SVG("SVG", ".svg", false);

	/**
	 * get the static toolbox for tasks when format isn't important
	 */
	public static FileFormatUtilities<GraphicFormat> staticToolbox = new FileFormatUtilities<GraphicFormat>(
			GraphicFormat.BMP);

	public static HashMap<String, GraphicFormat> getFormatMap() {

		HashMap<String, GraphicFormat> formats = new HashMap<String, GraphicFormat>();
		for (GraphicFormat form : GraphicFormat.values()) {
			formats.put(form.getFormatName(), form);
		}
		return formats;
	}

	/**
	 * the file extension of the graphic format.
	 */
	public final String extension;

	/**
	 * the name of the image format
	 */
	public final String name;

	/**
	 * The flag that indicates if the format represents a rastered graphic.
	 */
	public final boolean rastered;

	/**
	 * the file formatting toolbox
	 */
	private final FileFormatUtilities<GraphicFormat> toolbox;

	/**
	 * Constructor for the image format
	 * 
	 * @param name
	 *            name of format
	 * @param extension
	 *            file extension of the format
	 * @param rastered
	 *            indicating whether the format requires a background when exporting
	 *            the file
	 */
	private GraphicFormat(String name, String extension, boolean rastered) {

		this.name = name;
		this.rastered = rastered;
		this.extension = extension;
		toolbox = new FileFormatUtilities<GraphicFormat>(this);

	}

	/**
	 * Get the file extension
	 * 
	 * @return extension file extension
	 */
	@Override
	public String getFileExtension() {

		return this.extension;
	}

	/**
	 * Get the image format name
	 * 
	 * @return name format name
	 */
	@Override
	public String getFormatName() {

		return this.name();
	}

	/**
	 * Get format file utilities
	 * 
	 * @return toolbox the utilities for that particular file format
	 */
	@Override
	public FileFormatUtilities<GraphicFormat> getUtilities() {

		return toolbox;
	}

}