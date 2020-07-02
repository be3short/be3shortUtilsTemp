package com.be3short.obj.manipulation;

import java.lang.reflect.Field;

public class DynamicObjectManipulator extends ObjectManipulator
{

	public Object changeParent;

	public DynamicObjectManipulator(Field field, Object parent, Object change_parent)
	{
		super(field, parent);
		this.changeParent = change_parent;
		// TODO Auto-generated constructor stub
	}

	public Object getChange()
	{
		return getObject(changeParent);
	}

	public Object getChangeParent()
	{
		return changeParent;
	}

}
