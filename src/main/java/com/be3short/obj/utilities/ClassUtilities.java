package com.be3short.obj.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

//import bs.commons.io.system.IO;

public class ClassUtilities
{

	public static ArrayList<Class> getClassesListFromPackageSection(String... section)
	{

		try
		{
			ClassPath classpath = ClassPath.from(ClassUtilities.class.getClassLoader());//main_class.getClassLoader().getSystemClassLoader());
			ArrayList<Class> classNames = new ArrayList<Class>();
			for (ClassInfo info : classpath.getTopLevelClasses())
			{
				//System.out.println("Top Level Class Found :" + info.getName());
				boolean containsAllSections = false;
				if (section.length > 0)
				{
					for (String sub_section : section)
					{
						if (info.getPackageName().contains(sub_section))
						{
							containsAllSections = true;
							break;
						} else
						{
							//containsAllSections = containsAllSections || false;
						}
					}
				} else
				{
					containsAllSections = true;
				}
				if (containsAllSections)
				{
					if (!info.getName().contains("org.xerial") && !info.getName().contains("com.sun")
					&& !info.getName().contains("com.apple") && !info.getName().contains("org.jdom"))
					{
						try
						{
							try
							{
								//System.out.println(info..load()..getName());

								//!info.getName().contains("org.osgi.framework.BundleActivator"))
								//{
								ClassLoader loader = ClassUtilities.class.getClassLoader();//new ProtectedClassLoader(ClassLoader.getSystemClassLoader());
								Class classToAdd = loader.loadClass(info.getName());
								classNames.add(classToAdd);//classToAdd);
								//}

							} catch (Exception noClass)
							{
								noClass.printStackTrace();
							}

							// IO.dev("Class added : " + info.getName());
						} catch (Exception noClass)
						{
						}
					}
				}

			}

			return classNames;

		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static HashMap<String, ArrayList<Object>> getAllInterfaceEnumValues(Class interface_class, String... section)
	{
		HashMap<String, ArrayList<Object>> returnMap = new HashMap<String, ArrayList<Object>>();
		for (Class enumClass : getClassesListFromPackageSection(section))
		{

			try
			{
				if (Arrays.asList(enumClass.getInterfaces()).contains(interface_class))
				{
					Object cl = enumClass.getEnumConstants()[0];
					if (!cl.getClass().getSimpleName().equals(interface_class.getSimpleName()))
					{

						ArrayList<Object> enums = new ArrayList<Object>(
						Arrays.asList(cl.getClass().getEnumConstants()));
						returnMap.put(cl.getClass().getSimpleName(), enums);
					}
				}
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return returnMap;
	}

	public static HashMap<String, Object> getAllInterfaceClasses(Class main_class, Object interface_class,
	String... section)
	{
		HashMap<String, Object> instances = new HashMap<String, Object>();

		for (Class interfaceClass : getClassesListFromPackageSection(section))
		{
			try
			{

				if (Arrays.asList(interfaceClass.getInterfaces()).contains(interface_class))
				{
					if (!interfaceClass.getSimpleName()
					.equals(interface_class.getClass().getInterfaces()[0].getSimpleName()))
					{
						instances.put(interfaceClass.getSimpleName(), interfaceClass.newInstance());

					}
				}
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return instances;
	}

	public static class ProtectedClassLoader extends ClassLoader
	{

		public ProtectedClassLoader(ClassLoader loader)
		{
			super(loader);
			// TODO Auto-generated constructor stub
		}

		public Class getClassProtected(String class_name)
		{
			try
			{
				if (findClass(class_name) != null)
				{
					return loadClass(class_name);
				}
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

	}
}
