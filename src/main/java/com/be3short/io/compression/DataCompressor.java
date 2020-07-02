package com.be3short.io.compression;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class DataCompressor
{

	public static byte[] stringToByteCompressor(String compressable, CompressionFormat compression)
	{
		byte[] byteOut = null;
		switch (compression)
		{
		case GZIP:
			byteOut = compressDataGZip(compressable);
			break;
		default:
			byteOut = compressable.getBytes();
			break;
		}
		return byteOut;
	}

	public static byte[] compressDataGZip(String string)
	{
		byte[] dataToCompress = string.getBytes(StandardCharsets.ISO_8859_1);
		byte[] compressedData = null;
		try
		{
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream(dataToCompress.length);
			try
			{
				GZIPOutputStream zipStream = new GZIPOutputStream(byteStream);
				try
				{
					zipStream.write(dataToCompress);
				} finally
				{
					zipStream.close();
				}
			} finally
			{
				byteStream.close();
			}

			compressedData = byteStream.toByteArray();
		} catch (Exception e)
		{
			e.printStackTrace();
			compressedData = string.getBytes();
		}
		return compressedData;
	}

	public static void zipDirectory(String dir, String zipfile) throws IOException, IllegalArgumentException
	{
		// Check that the directory is a directory, and get its contents
		File d = new File(dir);
		if (!d.isDirectory())
			throw new IllegalArgumentException("Compress: not a directory:  " + dir);
		String[] entries = d.list();
		byte[] buffer = new byte[4096]; // Create a buffer for copying
		int bytes_read;

		// Create a stream to compress data and write it to the zipfile
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));

		// Loop through all entries in the directory
		for (int i = 0; i < entries.length; i++)
		{
			File f = new File(d, entries[i]);
			if (f.isDirectory())
				continue; // Don't zip sub-directories
			FileInputStream in = new FileInputStream(f); // Stream to read file
			ZipEntry entry = new ZipEntry(f.getName()); // Make a ZipEntry
			out.putNextEntry(entry); // Store entry
			while ((bytes_read = in.read(buffer)) != -1)
				// Copy bytes
				out.write(buffer, 0, bytes_read);
			in.close(); // Close input stream
		}
		// When we're done with the whole loop, close the output stream
		out.close();
	}

	private static final int BUFFER_SIZE = 4096;

	/**
	 * Extracts a zip file specified by the zipFilePath to a directory specified
	 * by destDirectory (will be created if does not exists)
	 * 
	 * @param zipFilePath
	 * @param destDirectory
	 * @throws IOException
	 */
	public void unzip(String zipFilePath, String destDirectory) throws IOException
	{
		File destDir = new File(destDirectory);
		if (!destDir.exists())
		{
			destDir.mkdir();
		}
		ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
		ZipEntry entry = zipIn.getNextEntry();
		// iterates over entries in the zip file
		while (entry != null)
		{
			String filePath = destDirectory + File.separator + entry.getName();
			if (!entry.isDirectory())
			{
				// if the entry is a file, extracts it
				extractFile(zipIn, filePath);
			} else
			{
				// if the entry is a directory, make the directory
				File dir = new File(filePath);
				dir.mkdir();
			}
			zipIn.closeEntry();
			entry = zipIn.getNextEntry();
		}
		zipIn.close();
	}

	/**
	 * Extracts a zip entry (file entry)
	 * 
	 * @param zipIn
	 * @param filePath
	 * @throws IOException
	 */
	private void extractFile(ZipInputStream zipIn, String filePath) throws IOException
	{
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
		byte[] bytesIn = new byte[BUFFER_SIZE];
		int read = 0;
		while ((read = zipIn.read(bytesIn)) != -1)
		{
			bos.write(bytesIn, 0, read);
		}
		bos.close();
	}
}
