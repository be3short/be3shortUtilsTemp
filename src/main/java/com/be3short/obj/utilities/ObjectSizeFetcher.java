
package com.be3short.obj.utilities;

import java.lang.instrument.Instrumentation;

import com.be3short.obj.expansions.Range;
import com.be3short.obj.manipulation.FieldMapper;
import com.be3short.obj.organization.Address;
import com.be3short.util.io.XMLParser;
import com.carrotsearch.sizeof.RamUsageEstimator;

public class ObjectSizeFetcher {

	private static Instrumentation instrumentation;

	public static void premain(String args, Instrumentation inst) {

		instrumentation = inst;
	}

	public static long getObjectSize(Object o) {

		return instrumentation.getObjectSize(o);
	}

	public static void main(String args[]) {

		double d = 214748364.0;
		// Long d = (long) 99999999.99999;
		String string = "this is a string";
		// Class<Double> dc = Double.class;
		Class<FieldMapper> dc = FieldMapper.class;
		Range<Double> range = new Range<Double>(2.0, 3.0, Double.class);
		FieldMapper fm = new FieldMapper(Address.class);
		try {
			System.out.println(ObjectSizeCalculator.sizeOf(d) + " " + ObjectSizeCalculator.sizeOf(range) + " "
					+ RamUsageEstimator.sizeOf(d) + " " + RamUsageEstimator.sizeOf(range));
			System.out.println(ObjectSizeCalculator.sizeOf(fm) + " " + RamUsageEstimator.sizeOf(fm));
			System.out.println(ObjectSizeCalculator.sizeOf(dc) + " " + RamUsageEstimator.sizeOf(dc));
			System.out.println(ObjectSizeCalculator.sizeOf(string) + " " + RamUsageEstimator.sizeOf(string));
			System.out.println(XMLParser.serializeObject(dc));
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}