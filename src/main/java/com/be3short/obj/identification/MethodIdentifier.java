package com.be3short.obj.identification;

public class MethodIdentifier
{

	public static String getCallerClassName()
	{
		StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
		for (int i = 1; i < stElements.length; i++)
		{
			StackTraceElement ste = stElements[i];
			if (!ste.getClassName().equals(MethodIdentifier.class.getName())
			&& ste.getClassName().indexOf("java.lang.Thread") != 0)
			{
				return ste.getClassName();
			}
		}
		return null;
	}

	public static String getCallerClassName(Integer increment)
	{
		StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
		for (int i = 1; i < stElements.length; i++)
		{
			StackTraceElement ste = stElements[i];
			if (!ste.getClassName().equals(MethodIdentifier.class.getName())
			&& ste.getClassName().indexOf("java.lang.Thread") != 0)
			{
				return stElements[i - increment].getClassName();
			}
		}
		return null;
	}

	public static String[] getThirdCallerClassName()
	{
		StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
		String callerClassName = null;
		String secondCallerClassName = null;
		for (int i = 1; i < stElements.length; i++)
		{
			StackTraceElement ste = stElements[i];
			if (!ste.getClassName().equals(MethodIdentifier.class.getName())
			&& ste.getClassName().indexOf("java.lang.Thread") != 0)
			{
				if (callerClassName == null)
				{
					callerClassName = ste.getClassName();
				} else if (!callerClassName.equals(ste.getClassName()))
				{
					if (secondCallerClassName == null)
					{
						secondCallerClassName = ste.getClassName();
					} else if (!secondCallerClassName.equals(ste.getClassName()))
					{
						return new String[]
						{ ste.getFileName(), ste.getClassName(), ste.getMethodName() };
					}
				}
			}
		}
		return null;
	}

	public static String getCallerCallerClassNameOnly()
	{
		StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
		String callerClassName = null;
		for (int i = 1; i < stElements.length; i++)
		{
			StackTraceElement ste = stElements[i];
			if (!ste.getClassName().equals(MethodIdentifier.class.getName())
			&& ste.getClassName().indexOf("java.lang.Thread") != 0)
			{
				if (callerClassName == null)
				{
					callerClassName = ste.getClassName();
				} else if (!callerClassName.equals(ste.getClassName()))
				{
					return (ste.getClassName());
				}
			}
		}
		return null;
	}

	public static String getCallerCallerClassName()
	{
		StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
		String callerClassName = null;
		for (int i = 1; i < stElements.length; i++)
		{
			StackTraceElement ste = stElements[i];
			if (!ste.getClassName().equals(MethodIdentifier.class.getName())
			&& ste.getClassName().indexOf("java.lang.Thread") != 0)
			{
				if (callerClassName == null)
				{
					callerClassName = ste.getClassName();
				} else if (!callerClassName.equals(ste.getClassName()))
				{
					return (ste.getClassName() + "$" + ste.getMethodName());
				}
			}
		}
		return null;
	}

	public static Class getCallerCallerClass()
	{
		StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
		Class callerClassName = null;
		for (int i = 1; i < stElements.length; i++)
		{
			StackTraceElement ste = stElements[i];
			if (!ste.getClassName().equals(MethodIdentifier.class.getName())
			&& ste.getClassName().indexOf("java.lang.Thread") != 0)
			{

				try
				{
					callerClassName = ClassLoader.getSystemClassLoader().loadClass(ste.getClassName());
				} catch (ClassNotFoundException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		return callerClassName;

	}

	public static String getCallerMethodName(Integer stack_level)
	{
		return Thread.currentThread().getStackTrace()[stack_level].getMethodName();
	}

}