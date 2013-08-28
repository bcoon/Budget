package com.example.budget;

import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A custom dialog box that will prompt the user to input the title of a
 * category they want to add or remove.
 * 
 * @author Brian
 */
public class CustomDialog extends Dialog {
	
	private String prompt = "";
	
	public Activity c;
	public View viewFromList;
	public TextView t;
	public Button okay, cancel;

	/**
	 * Constructor for the CustomDialog
	 * @param a the calling activity
	 * @param s the message to be displayed to the user; the prompt
	 * @param view the current view
	 */
	public CustomDialog(Activity a, String s, View view) {
		super(a);
		this.c = a;
		viewFromList = view;
		prompt = s;
	}
	
	@Override
	protected void onCreate(Bundle savedInstaceState) {
		super.onCreate(savedInstaceState);
		//set up dialog
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.custom_dialog);
		setCancelable(true);
		
		//set up buttons
		okay = (Button) findViewById(R.id.okayButton);
		okay.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				//save category title
				EditText text = (EditText) findViewById(R.id.dialogInput);
				String categoryTitle = text.getText().toString();
				try {
					
					//either add or remove a category depending on dialog prompt
					if (prompt.equals("Name of category")) {	//add
							//set up contents of the new JSONObject that contains category info
							JSONObject details = new JSONObject();
							
							//title
							details.put(MainActivity.TAG_CAT_TITLE, categoryTitle);
							
							//transactions ArrayList
							details.put(MainActivity.TAG_TRANSACTIONS, new ArrayList<Transaction>());
							
							//total amount, amount spent, amount left
							details.put(MainActivity.TAG_TOTAL, 0.00);
							details.put(MainActivity.TAG_SPENT, 0.00);
							details.put(MainActivity.TAG_REMAINING, 0.00);
						
							//update plan
							JSONObject tempObj = MainActivity.getPlan();
							tempObj.getJSONArray(MainActivity.TAG_CATEGORIES).put(details);
							MainActivity.setPlan(tempObj);

							JSONwriter.write(MainActivity.getPlan().toString(), MainActivity.getPlanName(), CustomDialog.this.getContext());

							System.out.println("about to read JSONobject");

							JSONreader jsonreader = new JSONreader(CustomDialog.this.getContext());
							System.out.println(jsonreader.read(MainActivity.getPlanName()));

							CategoriesActivity.addItem(viewFromList, categoryTitle);

					} else if (prompt.equals("Name the category to remove")) {		//remove
						MainActivity.setPlan(remove(MainActivity.getPlan(), categoryTitle));

						JSONwriter.write(MainActivity.getPlan().toString(), MainActivity.getPlanName(), CustomDialog.this.getContext());

						System.out.println("about to print JSONobject");

						JSONreader jsonreader = new JSONreader(CustomDialog.this.getContext());
						System.out.println(jsonreader.read(MainActivity.getPlanName()));
						
						CategoriesActivity.removeItem(viewFromList, categoryTitle);

					}
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				dismiss();
			}
		});
		
		cancel = (Button) findViewById(R.id.cancelButton);
		cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				cancel();
			}
		});
		
		//customize the message for either "okay" or "cancel"
		TextView t = (TextView) findViewById(R.id.dialogPrompt);
		t.setText(prompt);
		
	}
	
	/**
	 * Removes an item from a JSONArray and returns the JSONObject after removal
	 * @param jobj the plan in the form of a JSONObject to remove a category from
	 * @param text the category to remove
	 * @return the plan with the category removed
	 * @throws JSONException
	 */
	public static JSONObject remove(JSONObject jobj, String text) throws JSONException {
		System.out.println("Before removal: " + jobj);
		
		JSONArray arr = jobj.getJSONArray(MainActivity.TAG_CATEGORIES);
		ArrayList<JSONObject> list = new ArrayList<JSONObject>();
		
		JSONObject item;
		for (int i = 0; i < arr.length(); i++) {
			item = (JSONObject) arr.get(i);
			if (!item.getString(MainActivity.TAG_CAT_TITLE).equals(text)) {
				list.add(item);
			}
		}
		
		arr = new JSONArray(list);
		
		//delete old JSONArray
		jobj.remove(MainActivity.TAG_CATEGORIES);
		
		jobj.put(MainActivity.TAG_CATEGORIES, arr);
		System.out.println("After removal" + jobj);

		return jobj;
	}
	
}
