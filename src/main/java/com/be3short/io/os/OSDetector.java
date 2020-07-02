package com.be3short.io.os;

public class OSDetector
{

	private static String OS = System.getProperty("os.name").toLowerCase();

	public static OperatingSystem getOperatingSystem()
	{

		OperatingSystem os = OperatingSystem.OTHER;

		if (isWindows())
		{
			os = OperatingSystem.WINDOWS;
		} else if (isMac())
		{
			os = OperatingSystem.OSX;
		} else if (isUnix())
		{
			os = OperatingSystem.LINUX;
		} else if (isSolaris())
		{
			os = OperatingSystem.SOLARIS;
		}
		return os;
	}

	public static boolean isWindows()
	{

		return (OS.indexOf("win") >= 0);

	}

	public static boolean isMac()
	{

		return (OS.indexOf("mac") >= 0);

	}

	public static boolean isUnix()
	{

		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);

	}

	public static boolean isSolaris()
	{

		return (OS.indexOf("sunos") >= 0);

	}

	public static enum OperatingSystem
	{
		LINUX,
		OSX,
		WINDOWS,
		SOLARIS,
		OTHER;
	}
}