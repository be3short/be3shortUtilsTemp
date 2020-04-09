package com.be3short.util.io;

import java.util.regex.Pattern;

public enum CompressionFormat
{
	GZIP(
		"gz"),
	ZIP(
		".zip"),
	NONE(
		"txtss"),
	UNKNOWN(
		"???????");

	public final String extension;

	private CompressionFormat(String extension)
	{
		this.extension = extension;
	}

	public static CompressionFormat getFormat(String file_name)
	{
		String[] extensionSplit = file_name.split(Pattern.quote("."));
		for (CompressionFormat formats : CompressionFormat.values())
		{
			if (extensionSplit[extensionSplit.length - 1].contains(formats.extension))
			{
				return formats;
			}
		}
		return UNKNOWN;
	}
}
