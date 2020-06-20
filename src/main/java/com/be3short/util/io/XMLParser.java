
package com.be3short.util.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;

public class XMLParser {

	public static Logger log = Logger.getLogger(XMLParser.class);

	private static ArrayList<String> ownClassFilter = getOwnPackageFilter();

	public static XStream xstream = new XStream();

	public static String getTraceObj(Object obj) {

		return getLogObj(obj, Level.TRACE_INT);

	}

	public static String getWarnObj(Object obj) {

		return getLogObj(obj, Level.WARN_INT);

	}

	public static String getDebugObj(Object obj) {

		return getLogObj(obj, Level.DEBUG_INT);

	}

	// public static List<Level> getAcceptable(Level level) {
	//
	// Level[] levels = new Level[]
	// { Level.FATAL, Level.ERROR, Level.WARN, Level.INFO, Level.DEBUG };
	// ArrayList<Level> acc = new ArrayList<Level>();
	// for (Level lev : levels) {
	// if (repository.isDisabled(Level.ERROR_INT)) {
	// if (Logger.getRootLogger().getEffectiveLevel()
	// .isGreaterOrEqual(Logger.getRootLogger().getEffectiveLevel())) {
	// acc.add(lev);
	// }
	// }
	// }
	// if (Logger.getRootLogger().isTraceEnabled()) {
	// acc.clear();
	// acc.addAll(Arrays.asList(levels));
	// acc.add(Level.TRACE);
	// }
	// return acc;
	// }

	public static String getErrorObj(Object obj) {

		return getLogObj(obj, Level.ERROR_INT);

	}

	public static String getInfoObj(Object obj) {

		return getLogObj(obj, Level.INFO_INT);

	}

	public static String getLogObj(Object obj, int level) {

		String returnString = null;
		if (Logger.getRootLogger().getEffectiveLevel().toInt() <= level) {

			returnString = serializeObject(obj);

		} else {
			returnString = "";
		}
		return returnString;

	}

	public static String serializeObject(Object obj) {

		String returnString = null;

		try {
			returnString = xstream.toXML(obj);
		} catch (Exception e) {

		}
		return returnString;

	}

	public static Object getObject(String path) {

		try {
			return xstream.fromXML(new File(path));
		} catch (Exception e) {

		}
		return null;
	}

	public static Object getObject(File file) {

		try {
			if (file.exists()) {
				return xstream.fromXML(file);
			} else {
				return null;
			}
		} catch (Exception e) {

		}
		return null;
	}

	public static Object getObjectFromString(String obj_string) {

		return xstream.fromXML(obj_string);
	}

	public static void print(Object object) {

		System.out.println(serializeObject(object));
	}

	private static ArrayList<String> getOwnPackageFilter() {

		ArrayList<String> filterz = new ArrayList<String>();
		filterz.add("bs.commons.objects.manipulation");
		return filterz;
	}

}
