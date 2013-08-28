package com.example.budget;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * This Activity is displayed when a user clicks the Load button on 
 * MainActivity.  The user will be able to choose from a selection of previously
 * created plans to load.  Tapping a selection will load the CategoriesActivity.
 * 
 * @author Brian
 */
public class LoadBudget extends ListActivity {
	//temporary list of strings which will serve as list items
	static ArrayList<String> listItems = new ArrayList<String>();

	//defining string adapter which will handle data of listview
	static ArrayAdapter<String> adapter;

	Button addCategory, removeCategory;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.load_budget);

		listItems = getFileNames();
		
		//Set up an adapter to keep track of the list of categories to be displayed
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
		this.setListAdapter(adapter);
		
	}
	
	@Override
	public void onResume() {  // After a pause OR at startup
		super.onResume();
		adapter.notifyDataSetChanged();
	}
	
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        
        //get text from clicked choice
        final String text = (String) ((TextView)v).getText();
        
        //get the contents of the plan
		JSONreader reader = new JSONreader(getApplicationContext());
		MainActivity.setPlan(reader.read(text));
		MainActivity.setPlanName(text);
		
		System.out.println("Current plan contents: " + MainActivity.getPlan().toString());
		
		//remember the current plan for next time
		saveCurrentBudget(text);
        
		Intent intent = new Intent(this, CategoriesActivity.class);
		startActivity(intent);
    }
	
	/**
	 * Reads from the file storing the list of all plan names in internal storage.
	 * The file names are returned in an ArrayList.
	 * @return an ArrayList of file names or null if no file was found.
	 */
	public ArrayList<String> getFileNames() {
		
		ArrayList<String> files = new ArrayList<String>();
		
		/*read from internal storage, one byte at a time.  Each byte will be
		 * turned into it's character representation and added to a string
		 * until a newline is found. This builds a single line of the file
		 */
		
		try {
			System.out.println("opening filestream");
			
			FileInputStream fis = LoadBudget.this.openFileInput(MainActivity.ALL_FILES + ".json");
			int tempInt;
			char tempChar;
			String tempString = "";
			
			System.out.println("about to build string");
			
			do {
				tempInt = fis.read();
				tempChar = (char) tempInt;
				if (tempInt != -1) { //-1 -> EOF
					//add to the string until a newline is found, then add that
					//string to the list of files
					if (tempChar != '\n') {
						tempString += tempChar;
					} else {
						System.out.println("tempString = " + tempString);
						files.add(tempString);
						tempString = "";
					}
				}

			} while (tempInt != -1);
			fis.close();
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			Log.e("bad files file", "Problem reading from files file");
			e.printStackTrace();
		}
						
		return files;
	}
	
	private void saveCurrentBudget(String budgetName) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoadBudget.this);
		//SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		
		SharedPreferences.Editor editor =  prefs.edit();
		
		System.out.println("saving current budget as... " + budgetName);
		
		editor.putString(MainActivity.TAG_CURRENT, budgetName);
		
		editor.commit();
		
		MainActivity.setCurrentBudget(budgetName);
	}
}
