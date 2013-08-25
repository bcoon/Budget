package com.example.budget;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

/**
 * class obtained (and modified) from http://www.androidhive.info/2012/01/android-json-parsing-tutorial/
 * This class reads in from a .json file found in internal storage using a 
 * BufferedReader and StringBuilder.
 * 
 * @author Brian
 */
//class obtained (and modified) from http://www.androidhive.info/2012/01/android-json-parsing-tutorial/
public class JSONreader {

  static InputStream is = null;
  static JSONObject jObj = null;
  static String json = "";
  static Context context = null;
  
  /**
   * Basic constructor
   * @param c current context
   */
  public JSONreader(Context c) {
	  context = c;
  }

  /**
   * This method reads in from a .json file using a BufferedReader and 
   * StringBuilder. If the text read can be interpreted as a JSON object, it is
   * returned.  Exceptions will catch any unknown issues while coding.  
   * Exceptions should not occur.
   * @param filename the filename to read from
   * @return the read JSONObject that will be a budgeting plan
   */
  public JSONObject read(String filename) {

     try {
    	 System.out.println("opening input stream...");
    	 
    	 is = context.openFileInput(filename + ".json");
    	 
    	 System.out.println("is =" + is.toString());
    	 
    	 BufferedReader reader = new BufferedReader(new InputStreamReader(
    			 is, "iso-8859-1"), 8);
    	 StringBuilder sb = new StringBuilder();
    	 String line = null;
    	 
    	 System.out.println("about to build string...");
    	 
    	 while ((line = reader.readLine()) != null) {
    		 sb.append(line + "\n");
    	 }
    	 is.close();
    	 json = sb.toString();
     } catch (Exception e) {
    	 Log.e("Buffer Error", "Error converting result " + e.toString());
     }

     // try parse the string to a JSON object
     System.out.println("parsing string to JSON object...");
     try {
    	 jObj = new JSONObject(json);
     } catch (JSONException e) {
    	 Log.e("JSON Parser", "Error parsing data " + e.toString());
     }

     // return JSON String
     return jObj;

  }
}