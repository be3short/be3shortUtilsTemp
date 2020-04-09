
package com.be3short.util.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.be3short.util.io.PropertyScanner;

public class FieldFinder {

	public static Logger log = Logger.getLogger(FieldFinder.class);

	public static boolean containsInterface(Object item, Class search_class) {

		try {
			Object sysObj = item;
			Class superClass = sysObj.getClass();
			while (superClass != Object.class) {

				// System.out.println(superClass);
				if (Arrays.asList(superClass.getInterfaces()).contains(search_class)) {

					// System.out.println("yep");
					return true;
				}

				superClass = superClass.getSuperclass();
				Object newSysObj = superClass.cast(sysObj);
				sysObj = newSysObj;
				// System.out.println(superClass.getName());

			}
		} catch (Exception e) {

		}
		// System.out.println("nope");
		return false;
	}

	public static boolean containsSuper(Object item, Class search_class) {

		boolean contains = false;
		contains = contains || containsSuperClass(item, search_class);
		contains = contains || containsInterface(item, search_class);
		return contains;
	}

	public static boolean containsSuperClass(Object item, Class search_class) {

		try {
			Object sysObj = item;
			Class superClass = sysObj.getClass();
			while (superClass != Object.class) {

				// System.out.println(superClass);
				if (superClass.equals(search_class)) {

					// System.out.println("yep");
					return true;
				}

				superClass = superClass.getSuperclass();
				Object newSysObj = superClass.cast(sysObj);
				sysObj = newSysObj;
				// System.out.println(superClass.getName());

			}
		} catch (Exception e) {

		}
		// System.out.println("nope");
		return false;
	}

	/*
	 * Gets the fields of an object
	 * 
	 * @param object - Object to gather fields from
	 * 
	 * @param include_private - Flag to include private fields
	 * 
	 * @param - class_filter - Desired field classes to search for - all other
	 * classes filtered
	 * 
	 * @return Mapping of fields indexed by field name
	 */
	public static List<Field> getClassFields(Class<?> object_class, boolean include_private,
			@SuppressWarnings("rawtypes") Class... class_filter) {

		return getClassFields(object_class, include_private, true, class_filter);
	}

	public static Map<Object, List<Field>> matchingFields(List<Object> objects, String... field_names) {

		LinkedHashMap<Object, List<Field>> fieldMap = new LinkedHashMap<Object, List<Field>>();

		for (Object obj : objects) {

			List<Field> fields = FieldFinder.getObjectFields(obj, field_names);
			// if (fields.size() > 0) {
			fieldMap.put(obj, fields);
			// }
			// XMLParser.print("field s" + fieldMap);
			// XMLParser.print("obj s" + objects);
			for (Field field : FieldFinder.getClassFields(obj.getClass(), true, true)) {
				// System.out.println(field.getName());
				// if (isSubClass(field.getType())) {
				if (!field.getType().isPrimitive()) {
					Object ob;
					try {
						ob = field.get(obj);
						// List<Field> subFields = FieldFinder.getObjectFields(ob, field_names);
						Map<Object, List<Field>> sub = matchingFields(Arrays.asList(new Object[]
							{ ob }), field_names);

						fieldMap.put(ob, sub.get(ob));

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}

		}
		// XMLParser.print(fieldMap);
		return fieldMap;
	}

	public static Map<Class<?>, List<Field>> matchingClassFields(List<Class<?>> objects, String... field_names) {

		HashMap<Class<?>, List<Field>> fieldMap = new HashMap<Class<?>, List<Field>>();

		for (Class<?> obj : objects) {

			List<Field> fields = FieldFinder.getClassFieldsByName(obj, field_names);
			// if (fields.size() > 0) {
			fieldMap.put(obj, fields);
			// }
			// XMLParser.print("field s" + fieldMap);
			// XMLParser.print("obj s" + obj);
			fieldMap.put(obj, fields);

		}
		// XMLParser.print(fieldMap);
		return fieldMap;
	}

	/*
	 * Gets the fields of an object
	 * 
	 * @param object - Object to gather fields from
	 * 
	 * @param include_private - Flag to include private fields
	 * 
	 * @param - class_filter - Desired field classes to search for - all other
	 * classes filtered
	 * 
	 * @return Mapping of fields indexed by field name
	 */
	public static List<Field> getObjectFields(Object object, String... name) {

		ArrayList<Field> fields = getClassFields(object.getClass(), true, true);
		ArrayList<Field> fieldMatch = new ArrayList<Field>();
		for (Field field : fields) // iterate through fields
		{
			if (name.length > 0) {
				for (String nam : name) {
					if (field.getName().equals(nam)) {
						fieldMatch.add(field);
					}
				}
			} else {
				fieldMatch.add(field);

			}
		}
		return fieldMatch;
	}

	/*
	 * Gets the fields of an object
	 * 
	 * @param object - Object to gather fields from
	 * 
	 * @param include_private - Flag to include private fields
	 * 
	 * @param - class_filter - Desired field classes to search for - all other
	 * classes filtered
	 * 
	 * @return Mapping of fields indexed by field name
	 */
	public static List<Field> getClassFieldsByName(Class<?> object, String... name) {

		ArrayList<Field> fields = getClassFields(object, true, true);
		ArrayList<Field> fieldMatch = new ArrayList<Field>();
		for (Field field : fields) // iterate through fields
		{

			for (String nam : name) {
				if (field.getName().equals(nam)) {
					fieldMatch.add(field);
				}
			}
		}
		return fieldMatch;
	}

	/*
	 * Gets the fields of an object
	 * 
	 * @param object - Object to gather fields from
	 * 
	 * @param include_private - Flag to include private fields
	 * 
	 * @param - class_filter - Desired field classes to search for - all other
	 * classes filtered
	 * 
	 * @return Mapping of fields indexed by field name
	 */
	public static ArrayList<Field> getClassFields(Class<?> object_class, boolean include_private, Boolean declared_only,
			@SuppressWarnings("rawtypes") Class... class_filter) {

		ArrayList<Field> fields = new ArrayList<Field>(); // initialize field map
		if (declared_only == null) {
			fields.addAll(Arrays.asList(object_class.getDeclaredFields()));
			fields.addAll(Arrays.asList(object_class.getFields()));
		} else {
			if (declared_only) {

				fields.addAll(Arrays.asList(object_class.getDeclaredFields())); // get all declared fields of
																				// object]
			} else {
				fields.addAll(Arrays.asList(object_class.getFields())); // get all declared fields of object]
			}
		}
		for (Field field : fields) // iterate through fields
		{
			boolean filter = false; // initialize filter flag
			field.setAccessible(true); // set the field to be accessable
			if (class_filter.length > 0) // there are specific field classes
			{
				filter = true; // filter the current field unless class matches filter

				for (@SuppressWarnings("rawtypes")
				Class fieldClass : class_filter) // iterate through filter classes
				{
					if (field.getType().equals(fieldClass)) // the field class matches
					{
						filter = false; // don't filter the field
					}
				}
			}
			if (!filter) // the field is not filtered
			{
				if (Modifier.isPrivate(field.getModifiers())) // the field is private
				{
					if (!include_private) // include private fields
					{
						fields.remove(field); // add the field to the map
					}
				}
			}
		}
		return fields; // return the map of fields
	}

	/*
	 * Gets the fields of an object
	 * 
	 * @param object - Object to gather fields from
	 * 
	 * @param include_private - Flag to include private fields
	 * 
	 * @param - class_filter - Desired field classes to search for - all other
	 * classes filtered
	 * 
	 * @return Mapping of fields indexed by field name
	 */
	public static ArrayList<Object> getObjectFieldValues(Object object, boolean include_private,
			@SuppressWarnings("rawtypes") Class... class_filter) {

		return getObjectFieldValues(object, include_private, null, class_filter);
	}

	/*
	 * Gets the field values of an object
	 * 
	 * @param object - Object to gather fields from
	 * 
	 * @param include_private - Flag to include private fields
	 * 
	 * @param - field_classes - Desired field classes to search for - all other
	 * classes filtered
	 * 
	 * @return Mapping of fields indexed by field name
	 */
	public static ArrayList<Object> getObjectFieldValues(Object object, boolean include_private, Boolean declared_only,
			@SuppressWarnings("rawtypes") Class... field_classes) {

		ArrayList<Field> fields = getClassFields(object.getClass(), include_private, declared_only, field_classes); // get
																													// the
		// fields of
		// the
		// object
		ArrayList<Object> fieldValues = new ArrayList<Object>(); // initialize field value map

		for (Field field : fields) // iterate through the field names
		{
			try {
				Object fieldValue = field.get(object); // get the value of the current field
				if (!fieldValues.contains(fieldValue)) {
					fieldValues.add(fieldValue);
				}

			} catch (Exception noFieldAccess) // couldn't access the value of the field
			{
				System.err.println("Unable to access field " + field.getName() + " of " + object.getClass()); // print
																												// error
																												// notice
				noFieldAccess.printStackTrace(); // print stack trace
			}
		}
		return fieldValues; // return the field value map
	}

	/*
	 * Gets the field values of an object
	 * 
	 * @param object - Object to gather fields from
	 * 
	 * @param include_private - Flag to include private fields
	 * 
	 * @param - field_classes - Desired field classes to search for - all other
	 * classes filtered
	 * 
	 * @return Mapping of fields indexed by field name
	 */
	public static Map<Field, Object> getObjectFieldValueMap(Object object, boolean include_private,
			@SuppressWarnings("rawtypes") Class... field_classes) {

		ArrayList<Field> fields = getClassFields(object.getClass(), include_private, null, field_classes); // get
																											// the
		// fields of
		// the
		// object
		Map<Field, Object> fieldValues = new HashMap<Field, Object>(); // initialize field value map

		for (Field field : fields) // iterate through the field names
		{
			try {
				Object fieldValue = field.get(object); // get the value of the current field
				if (!fieldValues.containsKey(field)) {
					fieldValues.put(field, fieldValue);
				}

			} catch (Exception noFieldAccess) // couldn't access the value of the field
			{
				System.err.println("Unable to access field " + field.getName() + " of " + object.getClass()); // print
																												// error
																												// notice
				noFieldAccess.printStackTrace(); // print stack trace
			}
		}
		return fieldValues; // return the field value map
	}

	/**
	 * Get the value of a field for an object by specifying the field name
	 * 
	 * @param obj
	 *            object to read
	 * @param field_name
	 *            name of field to read
	 * @return value of field or null if object does not have field of specified
	 *         name
	 */
	public static Object getFieldValue(Object obj, String field_name) {

		Object val = null;
		try {
			Field field = obj.getClass().getField(field_name);
			val = field.get(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return val;

	}

	public static Object loadFieldValuesFromMap(Object obj, Map<String, Object> field_values) {

		for (Field field : obj.getClass().getFields()) {
			String fieldName = field.getName();
			Object fieldObj = null;
			try {
				fieldObj = field_values.get(fieldName);
				// System.out.println(field.getName() + " = " + fieldObj);
				if (fieldObj != null) {
					Object ob = PropertyScanner.parseFieldString(fieldObj, field);
					field.set(obj, ob);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return obj;
	}

	public static boolean isSubClass(Class<?> check) {

		boolean toolboxClass = false;
		if (check != null) {

			return FieldMapping.getScanClasses().contains(check);

		}
		return toolboxClass;
	}
}
