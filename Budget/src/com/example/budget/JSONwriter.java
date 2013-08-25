package com.example.budget;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;

/**
 * Writes data to a .json file in internal storage
 * @author Brian
 *
 */
public class JSONwriter {
	
	/**
	 * Updates a plan's contents
	 * @param data a JSONObject.toString()
	 * @param fileName the filename associated with this plan
	 * @param context current context
	 * @throws IOException
	 */
	public static void write(String data, String fileName, Context context) throws IOException {
		System.out.println("about to write");
		FileOutputStream fos = context.openFileOutput(fileName + ".json", Context.MODE_PRIVATE);
		fos.write(data.getBytes());
		fos.close();
	}
	
	/**
	 * Addes a plan title to the list of all budget plans stored on this device
	 * @param data a plan title
	 * @param fileName the file that stores the titles of all plans
	 * @param context current context
	 * @throws IOException
	 */
	public static void append(String data, String fileName, Context context) throws IOException {
		System.out.println("about to write");
		FileOutputStream fos = context.openFileOutput(fileName + ".json", Context.MODE_APPEND);
		fos.write(data.getBytes());
		fos.close();
	}
	
}
