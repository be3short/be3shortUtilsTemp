package com.be3short.io.format;

import java.io.File;
import java.util.ArrayList;

import com.be3short.io.general.FileSystemInteractor;

public class FileFormatUtilities<F extends FileFormatProperties<F>>
{

	static ArrayList<FileFormatUtilities<?>> all = new ArrayList<FileFormatUtilities<?>>();
	F fileFormat;
	Class<F> fileClass;

	public FileFormatUtilities(F format)
	{
		this.fileFormat = format;
		fileClass = (Class<F>) format.getClass();
		if (!all.contains(this))
		{
			all.add(this);
		}
	}

	public <F> boolean isFileFormat(File file)
	{
		Integer extensionLength = fileFormat.getFileExtension().length();

		if (file.getName().length() > extensionLength)
		{
			if (file.getName().substring(extensionLength).contains(fileFormat.getFileExtension()))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if a file name is appended with the corresponding extension
	 * 
	 * @param <T>
	 * 
	 * @param file
	 *            location of file to check
	 * @param extension
	 *            extension label of file type
	 * @return true if the file contains the extension, and false otherwise
	 *
	 */
	@SuppressWarnings(
	{ "unchecked", "null" })
	public <T extends FileFormatProperties<T>> T checkFormat(File file)
	{

		for (FileFormatUtilities<?> box : all)
			try
			{
				if (box.hasExtensionAppended(file))
				{
					return (T) box.getFileFormat();
				}
			} catch (Exception badFile)
			{
				System.err.println("Unable to determine what format the file " + file + " was");

			}
		return null;

	}

	/**
	 * Checks if a file name is appended with the corresponding extension
	 * 
	 * @param file
	 *            location of file to check
	 * @return true if the file contains the extension, and false otherwise
	 *
	 */
	public boolean hasExtensionAppended(File file)
	{
		try
		{
			if (file.getName().length() > fileFormat.getFileExtension().length())
			{
				if (file.getName().contains(fileFormat.getFileExtension()))
				{
					return true;
				}
			}
		} catch (Exception badFile)
		{
			System.err.println("Unable to determine if file " + file + " format was " + fileFormat.getFileExtension());
		}
		return false;
	}

	/**
	 * Adds the necessary extension to a file if it has not already been
	 * included.
	 * 
	 * @param file
	 *            location of file to check
	 * @param extension
	 *            extension label of file type
	 * @return the file with the appended extension
	 *
	 */
	public File appendExtension(File file)
	{
		File adjustedFile = file;
		try
		{

			Boolean needsExtension = !hasExtensionAppended(file);
			if (needsExtension)
			{
				adjustedFile = new File(file.getAbsoluteFile() + fileFormat.getFileExtension());
				checkDirectoryExistence(adjustedFile);

			}
		} catch (

		Exception badFile)
		{
			System.out
			.println("Unable to determine if file " + file + " contained extension " + fileFormat.getFileExtension());
		}
		return adjustedFile;

	}

	public void checkDirectoryExistence(File file)
	{
		FileSystemInteractor.checkDirectory(file.getParentFile(), true);
		try
		{
			//file.createNewFile();
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public F getFileFormat()
	{
		return fileFormat;
	}

	public Class<F> getFileClass()
	{
		return fileClass;
	}

}
