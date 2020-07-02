package com.be3short.obj.identification;

public class MemoryVariable
{

	Double value;
	protected MemoryUnits Units;

	public MemoryVariable(Double memory_value, MemoryUnits units)
	{
		value = memory_value;
		this.Units = units;
	}

	public static MemoryVariable getMaximumAvailableSystemMemory()
	{
		return (new MemoryVariable((double) Runtime.getRuntime().maxMemory(), MemoryUnits.BYTES));
	}

	public static MemoryVariable getTotalUsedSystemMemory()
	{
		return (new MemoryVariable((double) (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()),
		MemoryUnits.BYTES));
	}

	public static MemoryVariable getTotalAvailableSystemMemory()
	{
		return (new MemoryVariable((double) Runtime.getRuntime().totalMemory(), MemoryUnits.BYTES));
	}

	public Double getValue(MemoryUnits units)
	{
		return MemoryUnits.convert(value, this.Units, units);
	}
}
