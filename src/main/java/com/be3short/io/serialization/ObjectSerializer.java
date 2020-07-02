package com.be3short.io.serialization;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import com.be3short.io.general.FileSystemInteractor;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;

public class ObjectSerializer
{

	private static Kryo kryo = new Kryo();

	public static void store(HashMap<String, Object> multi_component, String directory)
	{
		FileSystemInteractor.checkDirectory(directory, true);
		for (String fileName : multi_component.keySet())
		{
			store(directory + "/" + fileName, multi_component.get(fileName));
		}
	}

	public static void store(String file_path, Object component)
	{
		Output output;
		try
		{

			output = new Output(new FileOutputStream(file_path));

			kryo.writeClassAndObject(output, component);

			output.close();
			// oos.close();

		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// file.data = getDataByteMap(component);

	}
}
