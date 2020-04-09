
package com.be3short.util.io;

import java.io.File;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.be3short.data.store.DataSet;
import com.be3short.io.general.FileSystemInteractor;
import com.be3short.obj.modification.XMLParser;

public class FileParser {

	private static Logger log = Logger.getLogger(FileParser.class);

	private static HashMap<String, Object> resources = new HashMap<String, Object>();

	public static boolean save(Object obj, File output, boolean compress) {

		if (compress) {
			return save(obj, output, DataFormat.GZIP);
		} else {
			return save(obj, output, DataFormat.XML);
		}
	}

	public static boolean save(Object obj, File output, DataFormat format) {

		boolean success = false;
		if (output != null) {
			File checkedOutput = format.getUtilities().appendExtension(output);
			String xmlString = XMLParser.serializeObject(obj);
			System.out.println(checkedOutput.getAbsolutePath());
			switch (format) {
			case XML: {
				success = FileSystemWorker.createOutputFile(checkedOutput, xmlString);
				log.info("Successfully saved global storage file " + checkedOutput);
				break;
			}
			case GZIP: {
				success = GZipper.writeCompressedObjectToFile(checkedOutput, xmlString);
				log.info("Successfully saved global storage file " + checkedOutput);
				break;
			}
			default:
				log.warn("Attempt to save global storage file in " + format.getFormatName() + " format not suported");
				break;
			}
		}
		return success;
	}

	public static <T> T loadFile(Class<T> obj, File file) {

		Object object = loadFile(file);
		return obj.cast(object);
	}

	public static <T> T loadFile(File file) {

		T objClassed = null;

		try {
			String inputString = null;
			if (DataFormat.GZIP.getUtilities().hasExtensionAppended(file)) {
				inputString = GZipper.readCompressedObjectFromFile(file, String.class);
			} else {
				inputString = FileSystemInteractor.getFileContentsAsString(file);
			}
			Object objIn = XMLParser.getObjectFromString(inputString);
			objClassed = (T) objIn;
		} catch (Exception badFile) {
			log.error("unable to load contents of file " + file, badFile);

		}

		return objClassed;
	}

	public static boolean createCSVFile(File output, DataSet data) {

		return createCSVFile(output, data.getDataGrid());
	}

	public static boolean createCSVFile(File output, Object[][] data) {

		String outputString = "";
		Object[][] outputData = data;
		for (Object[] outputDataRow : outputData) {
			String line = "";
			for (int i = 0; i < outputDataRow.length; i++) {
				if (i > 0) {
					line = line + ",";
				}
				System.out.println("i=" + i + " " + outputDataRow[i]);
				line = line + outputDataRow[i].toString();
			}

			outputString = outputString + (line + "\n");
		}
		return FileSystemWorker.createOutputFile(output, outputString);
	}

}
