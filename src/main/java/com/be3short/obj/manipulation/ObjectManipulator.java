package com.be3short.obj.manipulation;

import java.lang.reflect.Field;

public class ObjectManipulator
{

	private Field field;
	private Object parent;

	public ObjectManipulator(Field field, Object parent)
	{
		this.field = field;
		this.parent = parent;
	}

	public void updateObject(Object update)
	{
		try
		{
			field.set(parent, update);
		} catch (IllegalArgumentException | IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateObject(Object object, Object alternate_parent)
	{
		try
		{
			field.set(alternate_parent, object);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Object getObject()
	{
		try
		{
			return field.get(parent);
		} catch (IllegalArgumentException | IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0.0;
		}

	}

	public Object getObject(Object alternate)
	{
		try
		{
			return field.get(alternate);
		} catch (IllegalArgumentException | IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return null;
	}

	public Object getParent()
	{
		return parent;
	}

	public Field getField()
	{
		return field;
	}

	public static boolean isNumericalValue(ObjectManipulator manipulator, Object update)
	{
		boolean valid = true;
		if (manipulator.field.getType().equals(double.class) || manipulator.field.getType().equals(Double.class))
		{
			try
			{
				Double val = (Double) update;
				valid = valid && (!Double.isNaN(val) && Double.isFinite(val));
				if (!valid)
				{

					throw new Exception("Invalid number entered for field " + manipulator.field.getName() + " of state "
					+ manipulator.parent.getClass().getSimpleName() + " : " + val);
				}
			} catch (Exception e)
			{
				e.printStackTrace();
				System.exit(1);
			}
		}
		return valid;
	}
}
