package com.be3short.obj.access;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;

public class FieldFinder
{

	public static boolean containsInterface(Object item, Class search_class)
	{
		try
		{
			Object sysObj = item;
			Class superClass = sysObj.getClass();
			while (superClass != Object.class)
			{

				//System.out.println(superClass);
				if (Arrays.asList(superClass.getInterfaces()).contains(search_class))
				{

					//System.out.println("yep");
					return true;
				}

				superClass = superClass.getSuperclass();
				Object newSysObj = superClass.cast(sysObj);
				sysObj = newSysObj;
				//	System.out.println(superClass.getName());

			}
		} catch (Exception e)
		{

		}
		//System.out.println("nope");
		return false;
	}

	public static boolean containsSuper(Object item, Class search_class)
	{
		boolean contains = false;
		contains = contains || containsSuperClass(item, search_class);
		contains = contains || containsInterface(item, search_class);
		return contains;
	}

	public static boolean containsSuperClass(Object item, Class search_class)
	{
		try
		{
			Object sysObj = item;
			Class superClass = sysObj.getClass();
			while (superClass != Object.class)
			{

				//	System.out.println(superClass);
				if (superClass.equals(search_class))
				{

					//	System.out.println("yep");
					return true;
				}

				superClass = superClass.getSuperclass();
				Object newSysObj = superClass.cast(sysObj);
				sysObj = newSysObj;
				//	System.out.println(superClass.getName());

			}
		} catch (Exception e)
		{

		}
		//System.out.println("nope");
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
	public static ArrayList<Field> getObjectFields(Object object, boolean include_private,
	@SuppressWarnings("rawtypes") Class... class_filter)
	{

		return getObjectFields(object, include_private, true, class_filter);
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
	public static ArrayList<Field> getObjectFields(Object object, boolean include_private, Boolean declared_only,
	@SuppressWarnings("rawtypes") Class... class_filter)
	{
		ArrayList<Field> fields = new ArrayList<Field>(); // initialize field map
		if (declared_only == null)
		{
			fields.addAll(Arrays.asList(object.getClass().getDeclaredFields()));
			fields.addAll(Arrays.asList(object.getClass().getFields()));
		} else
		{
			if (declared_only)
			{

				fields.addAll(Arrays.asList(object.getClass().getDeclaredFields())); // get all declared fields of object]
			} else
			{
				fields.addAll(Arrays.asList(object.getClass().getFields())); // get all declared fields of object]
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
	@SuppressWarnings("rawtypes") Class... class_filter)
	{

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
	@SuppressWarnings("rawtypes") Class... field_classes)
	{
		ArrayList<Field> fields = getObjectFields(object, include_private, declared_only, field_classes); // get the fields of the object
		ArrayList<Object> fieldValues = new ArrayList<Object>(); // initialize field value map

		for (Field field : fields) // iterate through the field names
		{
			try
			{
				Object fieldValue = field.get(object); // get the value of the current field
				if (!fieldValues.contains(fieldValue))
				{
					fieldValues.add(fieldValue);
				}

			} catch (Exception noFieldAccess) // couldn't access the value of the field
			{
				System.err.println("Unable to access field " + field.getName() + " of " + object.getClass()); // print error notice
				noFieldAccess.printStackTrace(); // print stack trace
			}
		}
		return fieldValues; // return the field value map
	}

}
