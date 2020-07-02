package com.be3short.obj.access;

import java.util.ArrayList;

public class CallerRetriever
{

	public static CallingClass retriever = new CallingClass();

	public static void main(String args[])
	{
		TestCaller caller = new TestCaller();
		TestCaller caller2 = new TestCaller();
		caller.checkSelf();
		caller2.checkSelf();
		//CallingClass caller = new CallingClass();
		//System.out.println(caller.getCallingClasses()[2].getSimpleName());
		//System.out.println(caller.getListOfCallingClasses());

	}

	public static class TestCaller
	{

		public void checkSelf()
		{
			CallingClass caller = new CallingClass();
			for (Class className : caller.getCallingClasses())
			{
				System.out.println(className.getSimpleName());
			}
			//System.out.println(caller.getCallingClasses()[2].getSimpleName());
			//System.out.println(caller.getListOfCallingClasses());
		}
	}

	public static class CallingClass extends SecurityManager
	{

		public static final CallingClass INSTANCE = new CallingClass();

		public Class[] getCallingClasses()
		{
			return getClassContext();
		}

		public Class getCallingClass(ArrayList<String> package_filters)
		{

			Class[] stack = getCallingClasses();
			Integer ind = 0;
			Class caller = stack[0];
			while (caller.getPackage().getName().contains("bs.commons.objects")
			&& !package_filters.contains(caller.getPackage().getName()))
			{
				caller = stack[++ind];
			}
			return caller;
		}

		public String getListOfCallingClasses()
		{
			Class[] classes = getClassContext();
			String callStack = "Call stack\n";
			for (int i = 0; i < classes.length; i++)
			{
				callStack += i + " " + classes[i].getName() + " " + classes[i].toString() + " "
				+ classes[i].getEnclosingClass() + " " + classes[i].getSuperclass() + "\n";
			}
			return callStack;
		}
	}
}
