
package com.be3short.util.io;

import java.io.File;
import java.util.ArrayList;

import com.thoughtworks.xstream.XStream;

public class XMLParser {

	private static ArrayList<String> ownClassFilter = getOwnPackageFilter();

	public static XStream xstream = new XStream();

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
