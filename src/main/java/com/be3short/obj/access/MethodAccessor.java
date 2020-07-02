package com.be3short.obj.access;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import com.be3short.obj.execution.MethodId;
import com.be3short.obj.labeling.LabelReader;
import com.be3short.obj.utilities.ClassUtilities;

public class MethodAccessor
{

	public static HashMap<String, Method> getStaticMethods(String[] packages, Class... annotations)
	{
		ArrayList<Class> annotationsList = new ArrayList<Class>(Arrays.asList(annotations));
		HashMap<String, Method> methods = new HashMap<String, Method>();
		try
		{
			Integer i = 0;
			ArrayList<Class> classNames = ClassUtilities.getClassesListFromPackageSection(packages);
			// IO.debug("Classes in class path : " + classNames.size());
			for (Class methodClass : classNames)
			{

				// IO.debug("getting static methods of " + methodClass.getName()
				// + " number of annotations : "
				// + methodClass.getAnnotations().length);
				try
				{
					for (Method method : methodClass.getDeclaredMethods())
					{
						if (annotationsList.size() > 0)
						{
							if (method.getDeclaredAnnotations().length > 0)
							{
								// IO.debug("checking method " +
								// method.getName() + " for input annotations "
								// +
								// method.getDeclaredAnnotations()[0].toString());
							}
							// IO.debug("checking annotation " +
							// annotation.getClass().getName() + " of method "
							// + methodClass.getName() + " for input
							// annotations");
							// if
							// (annotationsList.contains(annotation.annotationType()))
							for (Class annotation : annotations)
							{
								if (FieldFinder.containsSuper(method, annotation))
								{
									// IO.debug(
									// "adding method " + methodClass.getName()
									// + " - has annotation " +
									// annotation.getName());
									methods.put(method.getName(), method);
								}
							}
						} else
						{
							// IO.debug("adding method " + method.getName());
							methods.put(method.getName(), method);
						}
					}
				} catch (Exception noclass)
				{

				}

			}
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return methods;
	}

	/*
	 * Gets the methods of an object that match the annotation
	 * 
	 * @param object - Object to gather methods from
	 * 
	 * @param - annotation_filter - Desired method annotations to search for -
	 * all other methods filtered
	 * 
	 * @return Mapping of methods indexed by name
	 */
	public static HashMap<String, Method> getEventMethod(Object object, Class... method_annotations)
	{
		Method methods[] = object.getClass().getDeclaredMethods(); // methods of
																	// the
																	// access
																	// class
		HashMap<String, Method> returnMethods = new HashMap<String, Method>();
		for (Integer i = 0; i < methods.length; i++) // iterate through the
														// methods
		{

			Method method = methods[i]; // store current method

			if (method_annotations.length > 0)
				for (Class annotation : method_annotations)
				{
					try
					{
						if (method.getAnnotation(annotation).annotationType().equals(annotation))
						{
							String methodLabel = LabelReader.getLabel(method.getAnnotation(annotation));
							// System.out.println(methodLabel);
							returnMethods.put(method.getName(), method);
						}
					} catch (Exception e)
					{
						// throw new Exception();
					}
				}
			else
			{
				returnMethods.put(method.getName(), method);
			}

		}
		return returnMethods;
	}

	public static Method getEventMethod(Object method_class, String identifier) throws Exception
	{
		Method methods[] = method_class.getClass().getDeclaredMethods(); // methods
																			// of
																			// the
																			// access
																			// class

		for (Integer i = 0; i < methods.length; i++) // iterate through the
														// methods
		{
			try
			{
				Method method = methods[i]; // store current method

				MethodId annotation = (MethodId) method.getAnnotation(MethodId.class); // check
																						// to
																						// see
																						// if
																						// it
																						// is
																						// method
																						// to
																						// be
																						// executed
				if (annotation.id().equals(identifier)) // method is to be
														// executed
				{
					return method;
				}
			} catch (Exception e)
			{
				// throw new Exception();
			}
		}
		throw new Exception();
		// return null;
	}

	public static Object executeMethod(Object method_class, Method method, Object... args)
	{
		try
		{
			method.setAccessible(true);
			// System.out.println(method.getName() + " " +
			// method.getParameterCount());
			return method.invoke(method_class, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Object executeMethod(Object method_class, String method_id, Object... args)
	{
		try
		{
			Method method = getEventMethod(method_class, method_id);
			method.setAccessible(true);
			// System.out.println(method.getName() + " " +
			// method.getParameterCount());
			return method.invoke(method_class, args);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
