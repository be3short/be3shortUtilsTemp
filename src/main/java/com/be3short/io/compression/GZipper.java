package com.be3short.io.compression;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPOutputStream;

import com.be3short.io.general.FileSystemInteractor;

public class GZipper
{

	public static <T> T readCompressedObjectFromFile(File file, Class<T> object)
	{
		T contents = null;
		try
		{
			ObjectInputStream in = FileSystemInteractor.getCompressionReader(file, CompressionFormat.GZIP);
			String inStr = in.readObject().toString();
			//System.out.println(inStr);
			contents = (T) (inStr);

		} catch (Exception getFirstLineError)
		{
			getFirstLineError.printStackTrace();
		}
		return contents;
	}

	public static <T> void writeCompressedObjectToFile(File file, T object)
	{
		FileSystemInteractor.checkDirectory(file.getParent(), true);
		try (ObjectOutputStream writer = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(file)))) // create
		{

			writer.writeObject(object);
			writer.flush();
			writer.close();

		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

}
