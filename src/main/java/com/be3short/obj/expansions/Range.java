package com.be3short.obj.expansions;

public class Range<T>
{

	private Class<T> rangeClass;
	private T lower;
	private T upper;

	public Range()
	{

	}

	public Range(T lower, T upper, Class<T> range_class)
	{
		rangeClass = range_class;
		setLower(lower);
		setUpper(upper);
	}

	public T getLower()
	{
		return lower;
	}

	public void setLower(T lower)
	{
		this.lower = lower;
	}

	public T getUpper()
	{
		return upper;
	}

	public void setValues(T lower, T upper)
	{
		setLower(lower);
		setUpper(upper);
	}

	public void setUpper(T upper)
	{
		this.upper = upper;
	}

	public T getValue()
	{
		if (lower != null && upper != null)
		{
			return rangeClass.cast((((Double) upper) - ((Double) lower)) * Math.random() + ((Double) lower));
		} else
		{
			return lower;
		}
	}

	public Class<T> getItemClass()
	{
		return rangeClass;
	}

	public Range<T> getNewInstance()
	{
		return new Range<T>();
	}

}
