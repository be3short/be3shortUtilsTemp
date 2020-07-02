
package com.be3short.data.cloning;

import com.be3short.util.io.XMLParser;
import com.rits.cloning.Cloner;

public class ObjectCloner {

	public static Cloner cloner = new Cloner();

	public static Object xmlClone(Object object) {

		String xmlString = XMLParser.serializeObject(object);
		Object clonedObject = XMLParser.getObjectFromString(xmlString);
		return clonedObject;
	}

	public static Object deepObjectClone(Object object) {

		return cloner.deepClone(object);
	}

	public static <T> T deepInstanceClone(T object) {

		return cloner.deepClone(object);
	}
}
