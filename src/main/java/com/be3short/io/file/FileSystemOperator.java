package com.be3short.io.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;

@Deprecated
public class FileSystemOperator
{

	public static ArrayList<File> findAllFilesInDirectory(String directory, String filter)
	{
		ArrayList<File> files = new ArrayList<File>();
		File dir = new File(directory);
		for (File subFile : dir.listFiles())
		{
			if (subFile.isDirectory())
			{
				files.addAll(findAllFilesInDirectory(subFile.getAbsolutePath(), filter));
			} else
			{
				if (subFile.getName().contains(filter))
				{
					files.add(subFile);
				}
			}
		}
		return files;
	}

	public static class Reporting
	{

		public static final boolean reportingEnabled = true;
		public static final boolean reportFilesFound = true;

		public static void filesFound(String directory, String[] prefixes, ArrayList<File> files_found)
		{
			if (reportingEnabled && reportFilesFound)
			{
				// System.out.println("Files found in directory: " + directory);
				String prefixString = "for prefixes: ";
				for (String prefix : prefixes)
				{
					prefixString += prefix + " ";
				}
				// System.out.println(prefixString);
				if (files_found != null)
				{
					for (File foundFile : files_found)
					{
						// System.out.println("> " + foundFile.getName());
					}
				} else
				{
					// System.out.println("> none");
				}
			}
		}
	}

	public String[] getFirstFileHeaders(String directory_name, String file_prefixes[])
	{
		ArrayList<String> headers = new ArrayList<String>();
		try
		{
			for (String filePrefix : file_prefixes)
			{
				String header = getFileHeader(getFilesFromPrefix(directory_name, filePrefix)[0]);
				headers.add(header);
			}
			return headers.toArray(new String[headers.size()]);
		} catch (Exception getHeaderFail)
		{
			getHeaderFail.printStackTrace();
			return null;
		}
	}

	public File[] getFilesFromPrefix(String directory, final String[] prefixes)
	{
		ArrayList<File> allFiles = new ArrayList<File>();
		for (String prefix : prefixes)
		{
			File[] filesFound = getFilesFromPrefix(directory, prefix);
			if (filesFound != null)
			{
				allFiles.addAll(Arrays.asList(filesFound));
			}
		}
		Reporting.filesFound(directory, prefixes, allFiles);
		return allFiles.toArray(new File[allFiles.size()]);
	}

	public File[] getFilesFromPrefix(String directory, final String prefix)
	{

		try
		{
			File filePath = new File(directory);
			File[] fileCandidates = filePath.listFiles(new FileFilter()
			{ // find files that have the intruder path prefix

				public boolean accept(File ownShipPathName)
				{
					return ownShipPathName.getName().startsWith(prefix);
				}
			});
			if (fileCandidates.length > 0)
			{
				return fileCandidates;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public BufferedReader openFileReader(File file) throws Exception
	{
		return new BufferedReader(new FileReader(file));
	}

	public BufferedReader openFileReader(String file_name) throws Exception
	{
		return new BufferedReader(new FileReader(file_name));
	}

	public String getFileHeader(File file) throws Exception
	{
		return openFileReader(file).readLine();
	}

	public String getFileHeader(String file_path) throws Exception
	{

		return getFileHeader(new File(file_path));
	}

	public String[] getFirstLineCSV(File file)
	{

		try
		{
			BufferedReader in = new BufferedReader(new FileReader(file));
			String firstLine = in.readLine();
			String[] firstLineContents = firstLine.split(",");
			return firstLineContents;
		} catch (Exception getFirstLineError)
		{
			System.err.println("Unable to get first line contents for file: " + file.getAbsolutePath());
			return null;
		}
	}

	public static ArrayList<String> getFileContents(File file)
	{
		ArrayList<String> contents = new ArrayList<String>();
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line;
			while ((line = in.readLine()) != null)
			{
				contents.add(line);
			}
		} catch (Exception getFirstLineError)
		{
			getFirstLineError.printStackTrace();
		}
		return contents;
	}

	public static String getFileContentsAsString(File file)
	{
		String contents = "";
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line;
			while ((line = in.readLine()) != null)
			{
				contents += line + "\n";
			}
		} catch (Exception getFirstLineError)
		{
			getFirstLineError.printStackTrace();
		}
		return contents;
	}

	public File[] getFilesFromDirectory(String file_directory)
	{
		File filePath = new File(file_directory);
		return getFilesFromDirectory(filePath);

	}

	public File[] getFilesFromDirectory(File file_directory)
	{
		File[] files = null;
		if (file_directory.isDirectory())
		{
			files = file_directory.listFiles();
		}
		return files;
	}

	public static ArrayList<File> getSpecificFilesFromDirectory(String file_directory, String filter)
	{
		return getSpecificFilesFromDirectory(new File(file_directory), filter);

	}

	public static ArrayList<File> getSpecificFilesFromDirectory(File file_directory, String filter)
	{
		ArrayList<File> files = new ArrayList<File>();
		if (file_directory.isDirectory())
		{
			for (File file : file_directory.listFiles())
			{

				if (file.getName().contains(filter))
				{
					files.add(file);
				}
			}
		}
		return files;
	}

	public static boolean checkDirectory(String directory_path, boolean create_if_missing)
	{
		File theDir = new File(directory_path);
		boolean result = theDir.exists();
		if (!result)
		{
			if (create_if_missing)
			{
				try
				{
					theDir.mkdirs();
					// System.out.println("hi : " + directory_path + " : " + theDir.getAbsolutePath());
				} catch (Exception se)
				{
					se.printStackTrace();
				}
			}

		}

		return result;
	}

	public static void createOutputFile(String path, String file_contents)
	{
		File file = new File(path);
		createOutputFile(file, file_contents);
	}

	public static void createOutputFile(File file, String file_contents)
	{
		checkDirectory(file.getParent(), true);
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"))) // create
		// the
		// output
		// file
		// and
		// initialize
		// file
		// writer
		{

			writer.write(file_contents); // write the line to the file
			writer.close(); // close the file
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void createOutputFile(String directory, String file_name, String file_contents)
	{
		// System.out.println("dir: " + directory + " file: " + file_name);
		checkDirectory(directory, true);
		// File outputFile = new File(directory + "/" + file_name);
		if (directory.length() > 0)
		{
			directory = directory + "/";
		}
		try (Writer writer = new BufferedWriter(
		new OutputStreamWriter(new FileOutputStream(directory + file_name), "utf-8"))) // create
		// the
		// output
		// file
		// and
		// initialize
		// file
		// writer
		{

			writer.write(file_contents); // write the line to the file
			writer.close(); // close the file
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void createOutputFile(File file, ArrayList<String> file_contents)
	{

		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"))) // create
		{// the
				// output
			// file
			// and
			// initialize
			// file
			for (String line : file_contents)
			{

				writer.write(line); // write the line to the file
				// writer.flush();
			}
			writer.close(); // close the file
		} catch (

		Exception e)
		{
			e.printStackTrace();
		}
	}

	public static File getFile(String file_path)
	{
		File newFile = null;
		try
		{
			newFile = new File(file_path);
			if (!newFile.exists())
			{
				newFile = null;
			}
		} catch (Exception noFile)
		{
		}

		return newFile;

	}

	public static File updateFile(File file)
	{
		try
		{
			String verify, putData;
			ArrayList<String> fileContents = getFileContents(file);
			// String fileContents = getFileContentsAsString(file);
			FileWriter pw = new FileWriter(file, false);

			pw.flush();

			for (String line : fileContents)
			{
				pw.write(line);
				// pw.flush();
			}
			// pw.write(fileContents);
			// IO.dev(line);

			pw.close();

			// FileReader fr = new FileReader(file);
			// BufferedReader br = new BufferedReader(fr);

			// while ((verify = br.readLine()) != null)
			// { // ***editted
			// // **deleted**verify = br.readLine();**
			// if (verify != null)
			// { // ***edited
			//
			// bw.write(verify);
			// }
			// }
			// br.close();

		} catch (Exception updateFailed)
		{
			//IO.err(updateFailed);
		}
		return file;
	}
}
