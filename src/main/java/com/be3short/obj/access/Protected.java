package com.be3short.obj.access;

public class Protected<T>
{

	private boolean editable;
	private T object;

	public Protected(T object, boolean editable)
	{
		this.object = object;
		this.editable = editable;
	}

	public T get()
	{
		return object;
	}

	public void set(T obj)
	{
		if (editable)
		{
			object = obj;
		}
	}

}
