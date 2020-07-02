
package com.be3short.obj.modification;

import com.be3short.util.io.XMLParser;
import com.rits.cloning.Cloner;

public class ObjectCloner {

	public static Cloner cloner = new Cloner();

	public static Object xmlClone(Object object) {

		String xmlString = XMLParser.serializeObject(object);
		Object clonedObject = XMLParser.getObjectFromString(xmlString);
		return clonedObject;
	}
}
