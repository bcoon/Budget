package com.example.budget;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

/**
 * This class sets up an adapter for and displays the budget categories for the
 * current plan (e.g. food, gass, rent).  The list items can be tapped to start
 * the Details activity that will display more details about the category.
 * 
 * @author Brian
 */
public class CategoriesActivity extends ListActivity {
	
	//temporary list of strings which will serve as list items
	static ArrayList<String> listItems;
	
	//defining string adapter which will handle data of listview
	static ArrayAdapter<String> adapter;
	
	Button addCategory, removeCategory;
	
	protected void onCreate(Bundle savedInstanceState) {
		listItems = new ArrayList<String>();
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.categories_layout);
		
		addCategory = (Button) findViewById(R.id.addCategory);
		removeCategory = (Button) findViewById(R.id.removeCategory);
		
		//Set up an adapter to keep track of the list of categories to be displayed
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        this.setListAdapter(adapter);
        
        System.out.println("adapter set");
		
        System.out.println("loading contents of plan titled: " + MainActivity.getPlanName());
        
		JSONreader reader = new JSONreader(this);
		MainActivity.setPlan(reader.read(MainActivity.getPlanName()));
		
		System.out.println("loaded passed plan");
		
		loadCategories();
		
		//buttons to add/delete category
		//add
		addCategory.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				//name the category to add
				showDialog(v, "Name of category");

			}
		});	
		
		//remove
		removeCategory.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				//name the category to remove
				showDialog(v, "Name the category to remove");

			}
		});
		
				
		//TODO show total salary, amount unallocated for the month
	}
	
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
		//pass the correct category to Details.java to quickly insert the correct info
		try {
			String category = MainActivity.getPlan().getJSONArray(MainActivity.TAG_CATEGORIES).getJSONObject(position).toString();
			Intent intent = new Intent(this, Details.class);
			intent.putExtra("category", category);
			startActivity(intent);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onResume() {  // After device back button is pressed
		super.onResume();
		loadCategories();
	}

	/**
	 * Shows a custom dialog box that will prompt the user to enter a category
	 * title to add/remove.
	 * @param v the current view to pass
	 * @param prompt either the message relating to adding/removing a category
	 */
	public void showDialog(View v, String prompt) {
		CustomDialog cd = new CustomDialog(CategoriesActivity.this, prompt, v);
		//add in prompt
		cd.show();
	}
	
	/**
	 * Handles dynamic insertion into the listItems data set.
	 * @param v the current view to pass
	 * @param title the title of the category to add
	 * @throws JSONException
	 */
	public static void addItem(View v, String title) throws JSONException {
		listItems.add(title);
		adapter.notifyDataSetChanged();
	}
	
	/**
	 * Handles dynamic deletion from the listItems data set.
	 * @param v the current view to pass
	 * @param title the title of the category to remove
	 */
	public static void removeItem(View v, String title) {
		listItems.remove(title);
		adapter.notifyDataSetChanged();
	}
	
	/**
	 * Checks if there are categories to load from a previous session with this
	 * budget.  Those categories are then loaded.
	 */
	private static void loadCategories() {
		try {		
			listItems.removeAll(listItems);

			JSONArray categories = MainActivity.getPlan().getJSONArray(MainActivity.TAG_CATEGORIES);
			for (int i = 0; i < categories.length(); i++) {
				listItems.add(categories.getJSONObject(i).getString(MainActivity.TAG_CAT_TITLE));
			}
			System.out.println("listItems contains: " + listItems.toString());
			adapter.notifyDataSetChanged();
		} catch (JSONException e) {
			System.out.println("Problem obtaining categories array");
			e.printStackTrace();
		}
	}
}