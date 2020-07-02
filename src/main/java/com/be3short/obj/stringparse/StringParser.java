package com.be3short.obj.stringparse;

public class StringParser
{

	public static Object parseString(String string, Class<?> num_class)
	{
		Object singleVal = getNumByType(string, num_class);
		return singleVal;
	}

	public static Object getNumByType(String item, Class<?> num_class)
	{
		Class<?> adjClass = num_class;
		if (adjClass.equals(Integer.class) || adjClass.equals(int.class))
		{
			return Integer.valueOf(item);
		} else if (adjClass.equals(Double.class) || adjClass.equals(double.class))
		{
			return Double.valueOf(item);
		} else if (adjClass.equals(Long.class) || adjClass.equals(long.class))
		{
			return Long.valueOf(item);
		} else if (adjClass.equals(Short.class) || adjClass.equals(short.class))
		{
			return Short.valueOf(item);
		} else if (adjClass.equals(Float.class) || adjClass.equals(float.class))
		{
			return Float.valueOf(item);
		} else if (adjClass.equals(Number.class))
		{
			return Double.parseDouble(item);
		} else if (adjClass.equals(Boolean.class) || adjClass.equals(boolean.class))
		{
			return Boolean.valueOf(item);
		} else if (adjClass.equals(Byte.class) || adjClass.equals(byte.class))
		{
			return Byte.valueOf(item);
		} else if (adjClass.equals(Character.class) || adjClass.equals(char.class))
		{
			return item.toCharArray()[0];
		} else if (adjClass.equals(String.class))
		{
			return item;
		}
		return null;
	}
}
