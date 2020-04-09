
package com.be3short.util.io;

public class ObjectEvaluator {

	public static boolean isNumericalValue(Object object) {

		return isDoubleValue(object) || object.getClass().equals(Integer.class) || object.getClass().equals(int.class);
	}

	public static boolean isDoubleValue(Object object) {

		return object.getClass().equals(Double.class) || object.getClass().equals(Double.class);
	}

	public static Double getDoubleValue(Object object) {

		Double value = null;
		if (isNumericalValue(object)) {
			value = (Double) object;
		}
		return value;
	}
}
