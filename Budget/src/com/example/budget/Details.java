package com.example.budget;

import java.text.NumberFormat;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * This Activity displays information about a specific category.  Information
 * such as total amount of money allocated, amount spent, and amount remaining.
 * **In the future** this activity will allow the user to input the transactions
 * they have made that fall under this category in an ArrayList of Transaction
 * objects.
 * 
 * @author Brian
 */
public class Details extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);
		
		//for formatting to user's currency.
		NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();
		
		//get passed JSONObject
		Intent intent = getIntent();
		JSONObject categoryDetails;
		try {
			categoryDetails = new JSONObject(intent.getStringExtra("category"));
			
			//set up layout
			//title
			TextView title = (TextView) findViewById(R.id.title);
			title.setText(categoryDetails.getString(MainActivity.TAG_CAT_TITLE));
			
			//total
			TextView total = (TextView) findViewById(R.id.total_amount);
			title.setText(defaultFormat.format(categoryDetails.getDouble(MainActivity.TAG_TOTAL)));
			
			//remaining
			TextView remaining = (TextView) findViewById(R.id.amount_left);
			remaining.setText(defaultFormat.format(categoryDetails.getDouble(MainActivity.TAG_REMAINING)));
			
			//spent
			TextView spent = (TextView) findViewById(R.id.amount_spent);
			spent.setText(defaultFormat.format(categoryDetails.getDouble(MainActivity.TAG_SPENT)));
			
		} catch (JSONException e) {
			Log.w("issue", "Couldn't pass in category details.");
			e.printStackTrace();
		}
		//get the list of transactions if it isn't empty, otherwise display "no transactions"
		
		//functionality for buttons
		//update local list and save file
		//refresh dataset
		//on resume
		
		
	}
	
}
