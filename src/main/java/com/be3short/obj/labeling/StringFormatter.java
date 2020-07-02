package com.be3short.obj.labeling;

import java.util.Calendar;
import java.util.Set;

public class StringFormatter
{

	public static Integer headerLength = 88;
	public static Integer instructionLineLength = 84;

	public static String getEvenHeader(String title, boolean following_space)
	{
		return getEvenHeader(title, following_space, true);
	}

	public static String getAppendedName(String name, Set<String> existing_names)
	{
		Integer appender = 1;
		String newName = name;
		while (existing_names.contains(newName))
		{
			newName = name + appender++;
		}
		return newName;
	}

	public static String getEvenHeader(String title, boolean following_space, boolean preceding_space)
	{
		String output = "";
		if (preceding_space)
		{
			output = output + ("\n");
		}
		// printSeparationBar(false);
		// Double barsPerSide = (NAFSetting.Interface.headerLength -
		// (title.length() + 6.0)) / 2;

		output = "[ " + title + " ] ";

		while (output.length() < headerLength)
		{
			output += "-";

		}
		{
			output += "-";
		}
		output += "\n";
		if (following_space)
		{
			output += "\n";
		}
		return output;
	}

	public static void printEvenHeader(String title, boolean following_space)
	{
		printEvenHeader(title, following_space, true);
	}

	public static void printEvenHeader(String title, boolean following_space, boolean preceding_space)
	{
		// SysIO.system(getEvenHeader(title, following_space, preceding_space));
	}

	public static void printSeparationBar(boolean following_space)
	{
		String output = "";
		for (int barCounter = 0; barCounter < headerLength + 7; barCounter++)
		{
			output += "|";
		}
		if (following_space)
		{
			output += "\n";
		}
		System.out.println(output);
	}

	public static boolean checkIfValidName(String test)
	{
		String allowedChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_";
		for (int charIndex = 0; charIndex < test.length(); charIndex++)
		{
			String testChar = test.substring(charIndex, charIndex + 1);
			if (!allowedChars.contains(testChar))
			{
				return false;
			}
		}
		return true;
	}

	public static String getCurrentDateString(long time, String date_separation_character, boolean include_time)
	{
		return StringFormatter.getCurrentDateString(time, date_separation_character, date_separation_character,
		include_time);
	}

	public static String getCurrentDateString(long time, String date_separation_character,
	String time_separation_character, boolean include_time)
	{
		Calendar CurrentCalendar = Calendar.getInstance();
		CurrentCalendar.setTimeInMillis((time) * 1000);
		int Year = CurrentCalendar.get(Calendar.YEAR);
		int Month = CurrentCalendar.get(Calendar.MONTH);
		int Day = CurrentCalendar.get(Calendar.DAY_OF_MONTH);

		String StringYear = Integer.toString(Year);
		String StringMonth;
		String StringDay;
		if (Month < 9)
		{
			StringMonth = ("0" + Integer.toString(Month + 1));
		} else
		{
			StringMonth = Integer.toString(Month + 1);
		}
		if (Day < 10)
		{
			StringDay = ("0" + Integer.toString(Day));
		} else
		{
			StringDay = Integer.toString(Day);
		}

		String returnString = (StringMonth + date_separation_character + StringDay + date_separation_character
		+ StringYear.substring(2, 4));
		if (include_time)
		{
			returnString = StringFormatter.getAbsoluteHHMMSS(time_separation_character) + " (" + returnString + ")";
		}
		return returnString;
	}

	public static String getCurrentDateString(long time, String date_separation_character)
	{

		return getCurrentDateString(time, date_separation_character, true);
	}

	public static String getCurrentDateString(String date_separation_character)
	{
		return getCurrentDateString((System.currentTimeMillis() / 1000), date_separation_character);

	}

	public static String getCurrentCalendarDateString(String date_separation_character)
	{
		return getCurrentDateString((System.currentTimeMillis() / 1000), date_separation_character, false);

	}

	public static String getAbsoluteHHMMSS()
	{
		return StringFormatter.getAbsoluteHHMMSS(":");
	}

	public static String getAbsoluteHHMMSS(String date_separation_character)
	{
		return StringFormatter.getAbsoluteHHMMSS(date_separation_character, false);
	}

	public static String getHHMMSS(long time)
	{
		Calendar currentCalendar = Calendar.getInstance();
		currentCalendar.setTimeInMillis((time) * 1000);
		return getHHMMSS(currentCalendar);

	}

	public static String getHHMMSS(Calendar calendar)
	{
		int Hour = calendar.get(Calendar.HOUR_OF_DAY);
		int Minute = calendar.get(Calendar.MINUTE);
		int Second = calendar.get(Calendar.SECOND);

		String StringHour;
		String StringMinute;
		String StringSecond;
		String StringMiliSecond;

		if (Hour < 10)
		{
			StringHour = ("0" + Integer.toString(Hour));
		} else
		{
			StringHour = Integer.toString(Hour);
		}
		if (Minute < 10)
		{
			StringMinute = ("0" + Integer.toString(Minute));
		} else
		{
			StringMinute = Integer.toString(Minute);
		}

		if (Second < 10)
		{
			StringSecond = ("0" + Integer.toString(Second));
		} else
		{
			StringSecond = Integer.toString(Second);
		}

		String returnString = (StringHour + ":" + StringMinute + ":" + StringSecond);

		return returnString;
	}

	public static String getAbsoluteHHMMSS(String date_separation_character, boolean include_milis)
	{
		Calendar CurrentCalendar = Calendar.getInstance();
		long CurrentTimeMiliseconds = (System.currentTimeMillis());
		CurrentCalendar.setTimeInMillis(CurrentTimeMiliseconds);
		int Hour = CurrentCalendar.get(Calendar.HOUR_OF_DAY);
		int Minute = CurrentCalendar.get(Calendar.MINUTE);
		int Second = CurrentCalendar.get(Calendar.SECOND);
		int MiliSecond = CurrentCalendar.get(Calendar.MILLISECOND);

		String StringHour;
		String StringMinute;
		String StringSecond;
		String StringMiliSecond;

		if (Hour < 10)
		{
			StringHour = ("0" + Integer.toString(Hour));
		} else
		{
			StringHour = Integer.toString(Hour);
		}
		if (Minute < 10)
		{
			StringMinute = ("0" + Integer.toString(Minute));
		} else
		{
			StringMinute = Integer.toString(Minute);
		}

		if (Second < 10)
		{
			StringSecond = ("0" + Integer.toString(Second));
		} else
		{
			StringSecond = Integer.toString(Second);
		}

		if (MiliSecond < 10)
		{
			StringMiliSecond = ("00" + Integer.toString(MiliSecond));
		} else if (MiliSecond < 100)
		{
			StringMiliSecond = ("0" + Integer.toString(MiliSecond));
		} else
		{
			StringMiliSecond = Integer.toString(MiliSecond);
		}

		String returnString = (StringHour + date_separation_character + StringMinute + date_separation_character
		+ StringSecond);
		if (include_milis)
		{
			returnString += date_separation_character + StringMiliSecond;
		}
		return returnString;
	}

	public static String getMemoryUsageString()
	{
		// gc();
		long total = Runtime.getRuntime().maxMemory() / 1048576;
		long used = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576;
		return ("[" + used + "/" + total + "]Mb");
	}

	public static String getMemoryUsageInfoString()
	{
		// Memory memoryUsed = Memory
		// .newBytesValue(Double.valueOf(Runtime.getRuntime().totalMemory() -
		// Runtime.getRuntime().freeMemory()));
		String returnString = "";// "[" + Math.round(memoryUsed.megabytes()) +
									// "/";
		// +
		// Math.round((MemoryVariable.getMaximumAvailableSystemMemory().getValue(MemoryUnits.MB)))
		// + "](Mb)";
		return returnString;
	}
}
