
package com.be3short.util.reflection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.be3short.util.io.StringCompiler;
import com.be3short.util.io.XMLParser;
import com.be3short.util.reflection.FieldFinder;

public class FieldMapping {

	private static FieldMapping fieldMapping;

	/**
	 * @return the scanClasses
	 */
	public static ArrayList<Class<?>> getScanClasses() {

		return scanClasses;
	}

	private static ArrayList<Class<?>> scanClasses = new ArrayList<Class<?>>();

	private ArrayList<String> skipFields;

	private Map<Class<?>, Map<String, Field>> elements;

	private static Logger log = Logger.getLogger(FieldMapping.class);

	public static void addScanClasses(Class<?>... classes) {

		fieldMapping = new FieldMapping();
		log.debug("detecting fields with new scan classes: " + StringCompiler.compileArray(classes));
		for (Class<?> cl : classes) {
			if (!scanClasses.contains(cl)) {
				scanClasses.add(cl);

			}
		}
		fieldMapping.mapClasses(getScanClasses());

	}

	public FieldMapping(Object... objects) {

		elements = new HashMap<Class<?>, Map<String, Field>>();
		skipFields = getDefaultSkipFields();
		mapObjects(objects);
		if (fieldMapping == null) {
			fieldMapping = this;
		}
	}

	private static ArrayList<String> getDefaultSkipFields() {

		ArrayList<String> fieldz = new ArrayList<String>();
		fieldz.add("$SWITCH_TABLE$");
		return fieldz;
	}

	public boolean mapObjectList(List<?> objects) {

		boolean success = true;
		for (Object object : objects) {

			success = success && mapObjects(object);
		}
		return success;
	}

	public boolean mapObjects(Object... objects) {

		boolean success = true;
		for (Object object : objects) {
			Class<?> objClass = object.getClass();
			mapClass(objClass);
		}
		return success;
	}

	public boolean mapClasses(List<Class<?>> object_class) {

		log.debug("scanning classes");
		for (Class<?> cl : object_class) {

			mapClass(cl);
		}
		return true;
	}

	public boolean mapClass(Class<?> object_class) {

		boolean success = true;
		if (object_class == null) {
			success = false;
		} else {
			if (!elements.containsKey(object_class)) {
				Map<String, Field> objFields = getClassFieldMap(object_class);
				// if (!elements.containsKey(object_class)) {
				elements.put(object_class, objFields);
				// }
			}
		}
		return success;
	}

	public Map<Class<?>, Map<String, Field>> getMap() {

		return elements;
	}

	/*
	 * Creates a mapping containing all declared fields for every state class
	 * included in the project and dependencies. This mapping allows for values to
	 * be updated without changing pointers.
	 */
	public void makeAllFieldsAccessable() {

		for (Map<String, Field> fields : elements.values()) {
			for (Field field : fields.values()) {
				field.setAccessible(true);
			}
		}
	}

	public static boolean isSkipField(FieldMapping mapping, Field field) {

		ArrayList<String> skip = getDefaultSkipFields();
		if (mapping != null) {
			skip = mapping.skipFields;
		}
		if (skip.contains(field.getName())) {
			return true;
		} else {
			return false;
		}
	}

	public void loadClassFieldMap(Class<?> search_class, Map<String, Field> mapper) {

		log.debug("scanning class " + search_class);
		if (search_class != null) {
			for (Field field : FieldFinder.getClassFields(search_class, true)) {
				if (!isSkipField(this, field)) {
					log.debug("field found: " + search_class.getName() + "." + field.getName());
					if (!mapper.containsKey(field.getName())) {
						mapper.put(field.getName(), field);
					}
					String name = field.getType().getName();
					System.out.println(name);
					if (field.getType().getName().length() < 4) {
						mapClass(field.getType());
					} else {
						if (!field.getType().getName().substring(0, 4).contains("java")
								&& !field.getType().isPrimitive()) {
							// if (scanClasses.contains(field.getType())) {

							mapClass(field.getType());
						}
					}
				}
			}
		}

	}

	public Map<String, Field> getClassFieldMap(Class<?> search_class) {

		Map<String, Field> map = new HashMap<String, Field>();
		if (!elements.containsKey(search_class)) {
			log.debug("creating field map for class " + search_class.getName());
			loadClassFieldMap(search_class, map);
			Class<?> search = search_class.getSuperclass();
			while (search != Object.class) {
				mapClass(search);
				loadClassFieldMap(search, map);
				if (search.getSuperclass() == null) {
					break;
				}
				search = search.getSuperclass();
			}
		} else {
			map = elements.get(search_class);
		}

		return map;
	}

	// public void mapClass

	@Override
	public String toString() {

		String print = "Field Mapping:\n";
		print = print + XMLParser.serializeObject(this);
		return print;
	}

	public boolean validField(Field field, Object object) {

		Map<String, Field> map = getClassFieldMap(object.getClass());
		log.debug("valid field " + field + " for " + object + " = " + map.containsValue(field));

		return map.containsValue(field);
	}

	/**
	 * @return the fieldMapping
	 */
	public static FieldMapping getGlobal() {

		if (fieldMapping == null) {
			fieldMapping = new FieldMapping();
		}
		return fieldMapping;
	}

	/**
	 * @param fieldMapping
	 *            the fieldMapping to set
	 */
	public static void setFieldMapping(FieldMapping fieldMapping) {

		FieldMapping.fieldMapping = fieldMapping;
	}
}
