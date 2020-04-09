
package com.be3short.util.io;

public class StringCompiler {

	public static String compileArray(Object... objects) {

		String array = "";
		for (Object object : objects) {
			if (object != null) {
				if (array.length() > 0) {
					array = array + ", ";
				}
				array = array + object.toString();
			}
		}
		array = "[" + array + "]";
		return array;
	}
}
