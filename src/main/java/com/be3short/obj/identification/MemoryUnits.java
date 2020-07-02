package com.be3short.obj.identification;

import java.util.ArrayList;
import java.util.TreeMap;

public class MemoryUnits
{

	static private ArrayList<MemoryUnits> Units = new ArrayList<MemoryUnits>();
	static private TreeMap<String, MemoryUnits> UnitsByName = new TreeMap<String, MemoryUnits>();
	static private int NextOrdinal = 0;

	public static final MemoryUnits UNKNOWN = addUnits("Unknown");
	public static final MemoryUnits BYTES = addUnits("Bytes");
	public static final MemoryUnits KB = addUnits("KB");
	public static final MemoryUnits MB = addUnits("MB");
	public static final MemoryUnits GB = addUnits("GB");
	public static final MemoryUnits TB = addUnits("TB");

	public static final Double MemoryFactor = 1024.0;
	//	public static final Double BytesPerKB = 1024.0;
	//	public static final Double BytesPerMB = 1048576.0;
	//	public static final Double BytesPerGB = 1073741824.0;
	//	public static final Double BytesPerTB = 1099511627776.0;

	private final int ordinal;
	private final transient String name;

	static private final double[][] FACTORS =
	{
			{ 0. }, // from UNKNOWN_SPEED_UNITS to any
			{ // from bytes to:
					0., // UNKNOWN_SPEED_UNITS
					1., // bytes
					1. / MemoryFactor, // kb
					1. / Math.pow(MemoryFactor, 2), // mb
					1 / Math.pow(MemoryFactor, 3), // gb
					1 / Math.pow(MemoryFactor, 4) }, // tb
			{ // from kb to:
					0., // UNKNOWN_SPEED_UNITS
					1. * MemoryFactor, // bytes
					1., // kb
					1. / MemoryFactor, // mb
					1 / Math.pow(MemoryFactor, 2), // gb
					1 / Math.pow(MemoryFactor, 3) }, // tb
			{ // from mb to:
					0., // UNKNOWN_SPEED_UNITS
					1. * Math.pow(MemoryFactor, 2), // bytes
					1. * MemoryFactor, // kb
					1., // mb
					1 / MemoryFactor, // gb
					1 / Math.pow(MemoryFactor, 2) }, // tb
			{ // from gb to:
					0., // UNKNOWN_SPEED_UNITS
					1. * Math.pow(MemoryFactor, 3), // bytes
					1. * Math.pow(MemoryFactor, 2), // kb
					1. * MemoryFactor, // mb
					1, // gb
					1 / MemoryFactor }, // tb
			{ // from tb to:
					0., // UNKNOWN_SPEED_UNITS
					1. * Math.pow(MemoryFactor, 4), // bytes
					1. * Math.pow(MemoryFactor, 3), // kb
					1. * Math.pow(MemoryFactor, 2), // mb
					1 * MemoryFactor, // gb
					1 }, // tb
	};

	public static double convert(double value, MemoryUnits unitsFrom, MemoryUnits unitsTo)
	{
		// No conversion if units are the same
		if (unitsFrom == unitsTo)
		{
			return value;
		}

		return FACTORS[unitsFrom.ordinal][unitsTo.ordinal] * value;
	}

	private MemoryUnits(String name)
	{
		this.ordinal = NextOrdinal;
		this.name = name;
		NextOrdinal++;
	}

	private static MemoryUnits addUnits(String name)
	{
		MemoryUnits Next = new MemoryUnits(name);
		Units.add(Next);
		UnitsByName.put(name, Next);
		return Next;
	}

	public String getName()
	{
		return name;
	}
}
