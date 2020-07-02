package com.be3short.obj.execution;

import com.be3short.obj.modification.ObjectCloner;
import java.lang.reflect.Field;

public class ContinuousFieldLoader
{

	public Field field;
	public Object parent;
	public Object variable;
	public Object value;
	public String name;
	//public RandomValue random;

	public ContinuousFieldLoader(Field field, Object parent, String name)
	{
		this.field = field;
		this.parent = parent;
		this.name = name;
		try
		{
			field.setAccessible(true);
			this.value = ObjectCloner.xmlClone(field.get(parent));
			this.variable = field.get(parent);
			System.out.println(value);
		} catch (IllegalArgumentException | IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			this.value = null;
			e.printStackTrace();
		}
	}

	public void reloadVariable(Object parent)
	{
		try
		{
			variable = field.get(parent);
		} catch (IllegalArgumentException | IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@MethodId(id = MethodLabel.update)
	public void updateValue(Object val)
	{
		//		if (val.getClass().equals(RandomValue.class))
		//		{
		//			random = (RandomValue) val;
		//		} else
		//		{
		//			random = null;
		//		}
		try
		{
			value = val;
			field.setAccessible(true);
			field.set(parent, value);
			System.out.println(field.getName() + " " + value);
		} catch (IllegalArgumentException | IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void loadInitialCondition()
	{
		//		if (random != null)
		//		{
		//			value = random.getValue();
		//		}
		//		try
		//		{
		//			field.setAccessible(true);
		//			field.set(parent, value);
		//			System.out.println(field.getName() + " " + value);
		//		} catch (IllegalArgumentException | IllegalAccessException e)
		//		{
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}

	}

	public static class MethodLabel
	{

		public static final String update = "Update";
	}

}