
package com.be3short.io.compression;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.be3short.util.io.XMLParser;

public class DataDecompressor {

	public static String decompressFileContentsToString(String directory, String file_name) {

		File file = new File(directory, file_name);
		CompressionFormat compression = CompressionFormat.getFormat(file_name);
		return decompressFileContentsToString(file, compression);
	}

	public static String decompressFileContentsToString(File file, CompressionFormat compression) {

		String decompressed = null;
		byte[] byteContent = null;
		switch (compression) {
		case GZIP:
			decompressed = decompressDataGZip(file);
			break;
		default:

			break;
		}
		return decompressed;
	}

	public static String decompressDataGZip(File file) {

		String fileContent = "";
		try {
			InputStream input = new BufferedInputStream(new FileInputStream(file));// ByteArrayInputStream(compressed.getBytes()));
			GZIPInputStream gzis = new GZIPInputStream(input);
			InputStreamReader reader = new InputStreamReader(gzis, StandardCharsets.ISO_8859_1);
			BufferedReader in = new BufferedReader(reader);

			String readed;
			while ((readed = in.readLine()) != null) {

				fileContent += readed;
			}
			System.out.println(fileContent);
		} catch (Exception badFile) {
			badFile.printStackTrace();
		}
		return fileContent;
	}

	public static String decompressDataGZipString(byte[] string) {

		String fileContent = "";
		try {
			InputStream input = new ByteArrayInputStream(string);
			GZIPInputStream gzis = new GZIPInputStream(input);
			InputStreamReader reader = new InputStreamReader(gzis, StandardCharsets.ISO_8859_1);
			BufferedReader in = new BufferedReader(reader);

			String readed;
			while ((readed = in.readLine()) != null) {
				// System.out.println(readed);
				fileContent += readed;
			}
			// System.out.println(fileContent);
		} catch (Exception badFile) {
			badFile.printStackTrace();
		}
		return fileContent;
	}

	public static void unzipDirectory(String dir) throws IOException, IllegalArgumentException {
		// Check that the directory is a directory, and get its contents

		// Create a stream to compress data and write it to the zipfile
		ZipInputStream in = new ZipInputStream(new FileInputStream(dir));
		ArrayList<File> filez = new ArrayList<File>();
		ZipEntry entry = in.getNextEntry();
		while (entry != null) {
			byte[] bytes = new byte[(int) entry.getSize()];
			in.read(bytes);
			System.out.println(XMLParser.serializeObject(bytes));
			System.out.println(entry.getName());
			entry = in.getNextEntry();
		}
		// // Loop through all entries in the directory
		// for (int i = 0; i < entries.length; i++)
		// {
		// File f = new File(d, entries[i]);
		// if (f.isDirectory())
		// continue; // Don't zip sub-directories
		// FileInputStream in = new FileInputStream(f); // Stream to read file
		// ZipEntry entry = new ZipEntry(f.getName()); // Make a ZipEntry
		// out.putNextEntry(entry); // Store entry
		// while ((bytes_read = in.read(buffer)) != -1)
		// // Copy bytes
		// out.write(buffer, 0, bytes_read);
		// in.close(); // Close input stream
		// }
		// // When we're done with the whole loop, close the output stream
		// out.close();
	}
}
