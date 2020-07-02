package com.be3short.obj.execution;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ExternalMethodExecutor
{

	public Method getEventMethod(String identifier) throws Exception
	{
		Method methods[] = this.getClass().getDeclaredMethods(); // methods of the access class

		for (Integer i = 0; i < methods.length; i++) // iterate through the methods
		{
			try
			{
				Method method = methods[i]; // store current method

				MethodId annotation = (MethodId) method.getAnnotation(MethodId.class); // check to see if it is method
																						// to be executed
				if (annotation.id().equals(identifier)) // method is to be executed
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

	public Object executeMethod(Method method, Object... args)
	{
		try
		{
			method.setAccessible(true);
			System.out.println(method.getName() + " " + method.getParameterCount());
			return method.invoke(this, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Object executeMethod(String method_id, Object... args)
	{
		try
		{
			Method method = getEventMethod(method_id);
			method.setAccessible(true);
			System.out.println(method.getName() + " " + method.getParameterCount());
			return method.invoke(this, args);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
