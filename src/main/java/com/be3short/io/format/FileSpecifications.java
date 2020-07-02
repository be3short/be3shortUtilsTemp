package com.be3short.io.format;

import java.io.File;

public class FileSpecifications<T extends FileFormatProperties<T>>
{

	private String location;
	private T format;

	public FileSpecifications(String location, T format)
	{
		this.location = location;
		this.format = format;
	}

	public FileSpecifications()
	{
		location = null;
		format = null;
	}

	public boolean isNullFile()
	{
		ImageFormatting f = ImageFormatting.BMP;
		return location == null;
	}

	public File getLocation(boolean adjust)
	{
		if (isNullFile())
		{
			return null;
		}
		if (adjust)
		{
			File adjustedLocation = prepareDirectory(location);
			File adjustedPath = prepareLocation(
			new File(checkSlashes(adjustedLocation.getAbsolutePath(), deriveFileName(location))));
			return adjustedPath;
		}
		return new File(location);
	}

	public static String derivePath(File file)
	{
		return derivePath(file.getAbsolutePath());
	}

	public static String derivePath(String file)
	{
		String path = "";
		String dirs[] = file.split("/");
		for (Integer i = 0; i < (dirs.length - 1); i++)
		{
			path += dirs[i] + "/";
		}
		return path;
	}

	public static String deriveFileName(File file)
	{
		return deriveFileName(file.getAbsolutePath());
	}

	public static String deriveFileName(String file)
	{
		String dirs[] = file.split("/");
		return dirs[dirs.length - 1];
	}

	private File prepareDirectory(String dir)
	{
		File adjustedLocation = new File(derivePath(dir));

		if (adjustedLocation.getAbsolutePath() != null)
		{
			int appendIndex = 1;
			if (!adjustedLocation.exists())
			{
				adjustedLocation.mkdirs();
				return adjustedLocation;
			}
			while (adjustedLocation.exists() && !adjustedLocation.isDirectory())
			{
				adjustedLocation = new File(adjustedLocation.getParent() + appendIndex++ + "_");
			}
			adjustedLocation.mkdirs();
		}
		return adjustedLocation;
	}

	private File prepareLocation(File adj_location)
	{
		File adjustedLocation = adj_location;
		int appendIndex = 1;
		while (new File(adjustedLocation.getAbsolutePath() + format.getFileExtension()).exists())
		{
			adjustedLocation = new File(
			adj_location.getParent() + "/" + appendIndex++ + "_" + adjustedLocation.getName());
		}
		return adjustedLocation;

	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public T getFormat()
	{
		return format;
	}

	public void setFormat(T format)
	{
		this.format = format;
	}

	public void prependDirectory(String prepend)
	{
		// System.out.println("newdir " + prepend + " " + location);
		String newDir = checkSlashes(prepend, location);// prepend);
		location = newDir;
	}

	public static String checkSlashes(String beginning, String end)
	{
		if (beginning.contains("/"))
		{
			String[] split = beginning.split("/");
			// System.out.println(end + " " + beginning + " " + split[split.length - 1] + " " +
			// beginning.contains("/"));
			// if (!(beginning.contains("/") || end.substring(0, 1).contains("/")) || split[split.length - 1].length() <
			// 1)
			if (split[split.length - 1].length() > 0 && !end.substring(0, 1).contains("/"))
			{

				return beginning + "/" + end;
			}
		} else if (!end.substring(0, 1).contains("/"))
		{
			return beginning + "/" + end;
		}
		return beginning + end;
	}

	public FileSpecifications<T> copy()
	{
		return new FileSpecifications<T>(location, format);
	}
}
