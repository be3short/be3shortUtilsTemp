package com.be3short.obj.labeling;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class LabelReader
{

	//	public static String getLabel(Annotation annotation)
	//	{
	//		if (annotation.getClass().equals(Settings.class))
	//		{
	//			String label = ((Settings) annotation).label();
	//			return label;
	//		}
	//		return null;
	//	}

	public static String getLabel(Class annotation, Field field)
	{
		if (annotation.equals(Settings.class))
		{
			return getSettingsLabel(field);
		} else if (annotation.equals(Label.class))
		{
			return getGenericLabel(field);
		} else if (annotation.equals(Properties.class))
		{
			return getPropertiesLabel(field);
		} else
		{
			return field.getName();
		}

	}

	public static String getLocation(Annotation annotation)
	{

		return getAnnotationFieldValue(annotation, "location", String.class);
	}

	public static String getId(Annotation annotation)
	{

		return getAnnotationFieldValue(annotation, "id", String.class);
	}

	public static <T> T getAnnotationFieldValue(Annotation annotation, String field_name, Class<T> returnClass)
	{
		System.out.println(annotation.toString());
		T value = null;
		try
		{
			value = (T) (annotation.getClass().getDeclaredField(field_name).get(annotation));

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return value;
	}

	public static String getLabel(Annotation annotation)
	{
		String label = null;
		try
		{
			label = (String) annotation.getClass().getDeclaredField("label").get(annotation);

		} catch (Exception e)
		{

		}
		return label;

	}

	public static String getSettingsLabel(Field field)
	{
		String label = field.getName();
		try
		{
			Settings param = (Settings) field.getDeclaredAnnotation(Settings.class);
			if (!param.label().equals(""))
			{
				System.out.println("label" + param.label());
				label = param.label();
			}
		} catch (Exception e)
		{
		}
		return label;
	}

	//	public static String getLabel(Field field, Class annotation)
	//	{
	//
	//		if (annotation.equals(Label.class))
	//		{
	//			return getGenericLabel(field);
	//		} else
	//		{
	//			return field.getName();
	//		}
	//	}

	public static String getGenericLabel(Field field)
	{
		String label = field.getName();
		System.out.println(label);
		try
		{
			Label param = (Label) field.getDeclaredAnnotation(Label.class);
			if (!param.label().equals(""))
			{
				System.out.println("label" + param.label());
				label = param.label();
			}
		} catch (Exception e)
		{
		}
		return label;
	}

	public static String getPropertiesLabel(Field field)
	{
		String label = field.getName();
		System.out.println(label);
		try
		{
			Properties param = (Properties) field.getDeclaredAnnotation(Properties.class);
			if (!param.label().equals(""))
			{
				System.out.println("label" + param.label());
				label = param.label();
			}
		} catch (Exception e)
		{
		}
		return label;
	}
}
