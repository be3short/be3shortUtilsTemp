package com.be3short.io.format;

public interface FileFormatProperties<T extends FileFormatProperties<T>>
{

	public FileFormatUtilities<T> getUtilities();

	public String getFileExtension();

	public String getFormatName();
}
