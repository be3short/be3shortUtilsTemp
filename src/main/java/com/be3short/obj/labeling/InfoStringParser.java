package com.be3short.obj.labeling;

public class InfoStringParser
{

	public static String getMemoryUsageString()
	{
		long total = Runtime.getRuntime().maxMemory() / 1048576;
		long used = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576;
		return ("[" + used + "/" + total + "]Mb");
	}
}
