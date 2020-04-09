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
 * DataFormat.java
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

package com.be3short.util.io;

import com.be3short.io.format.FileFormatProperties;
import com.be3short.io.format.FileFormatUtilities;

/**
 * A set of data file formats that are supported by the HSE to export content in
 * several ways.
 * 
 * Intended Operator: User
 */
public enum DataFormat implements FileFormatProperties<DataFormat> {

	/**
	 * CSV file extension
	 */
	CSV("Comma separated values", ".csv"),

	/**
	 * GZIP file extension
	 */
	GZIP("gzip", ".gzip"),

	/**
	 * XML file extension
	 */
	XML("Extensible markup language", ".xml");

	/**
	 * Global data format toolbox
	 */
	public static FileFormatUtilities<DataFormat> staticToolbox;

	/**
	 * File extension
	 */
	private final String fileExtension;

	/**
	 * Title of the format
	 */
	private final String formatName;

	/**
	 * Data format toolbox
	 */
	private final FileFormatUtilities<DataFormat> toolbox;

	/**
	 * Constructor for the data format enum
	 * 
	 * @param name
	 *            name of the format
	 * @param extension
	 *            file extension
	 */
	private DataFormat(String name, String extension) {

		this.formatName = name;
		this.fileExtension = extension;

		toolbox = new FileFormatUtilities<DataFormat>(this);
	}

	/**
	 * Get the file extension of the format
	 */
	@Override
	public String getFileExtension() {

		return fileExtension;
	}

	/**
	 * Get the name of the format
	 */
	@Override
	public String getFormatName() {

		return formatName;
	}

	/**
	 * Get the file utilities of the format
	 */
	@Override
	public FileFormatUtilities<DataFormat> getUtilities() {

		// TODO Auto-generated method stub
		return toolbox;
	}

	/**
	 * Access the static format toolbox
	 * 
	 * @return toolbox
	 */
	public FileFormatUtilities<DataFormat> getUtils() {

		if (staticToolbox == null) {
			staticToolbox = new FileFormatUtilities<DataFormat>(CSV);
		}
		return staticToolbox;
	}

}