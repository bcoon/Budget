package com.example.budget;

import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Activity that is displayed on start up.  Users can create a new budget for a
 * month.  After at least one plan has been created, a plan can be loaded or the
 * last active plan can be continued.
 * 
 * @author Brian
 */
public class MainActivity extends Activity {

	static final String ALL_FILES = "files";
	
	private static JSONObject plan;
	private static String planName;
	private static String currentBudget = null;
	
	private Button continueButton;
	private Button newButton;
	private Button loadButton;
	
	// JSON Node names
	static final String TAG_FILENAME = "file name";
	static final String TAG_SALARY = "salary";
	static final String TAG_WITHHOLDINGS = "withholdings";
	static final String TAG_CATEGORIES = "categories";
	static final String TAG_CAT_TITLE = "title";
	static final String TAG_TRANSACTIONS = "transactions";
	static final String TAG_TOTAL = "total";
	static final String TAG_SPENT = "spent";
	static final String TAG_REMAINING = "remaining";
	
	// other constants
	static final String TAG_CURRENT = "current";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		continueButton = (Button) findViewById(R.id.continue_button);
		newButton = (Button) findViewById(R.id.new_button);
		loadButton = (Button) findViewById(R.id.load_button);
		
		//disable continue and load if no previous session
		currentBudget = getCurrentBudget();
		
		if (currentBudget == null) {
			continueButton.setEnabled(false);
			loadButton.setEnabled(false);
		} else {
			continueButton.setText("Continue to \"" + currentBudget + "\"");
		}

		//set what to do on button clicks
		//"Continue" button click reaction
		continueButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				System.out.println("Current plan contents: " + MainActivity.plan.toString());
				
				Intent intent = new Intent(MainActivity.this, CategoriesActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});
		
		//"New" button click reaction
		newButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent myIntent = new Intent(MainActivity.this, NewBudgetActivity.class);
				MainActivity.this.startActivity(myIntent);
			}
		});

		//"Load" button click reaction
		loadButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent myIntent = new Intent(MainActivity.this, LoadBudget.class);
				MainActivity.this.startActivity(myIntent);
			}
		});
	}

	@Override
	public void onResume() {  // After device back button is pressed
		super.onResume();
		//re-check if buttons can be made clickable
		if (getCurrentBudget() != null) {
			continueButton.setText("Continue to \"" + currentBudget + "\"");
			continueButton.setEnabled(true);
			loadButton.setEnabled(true);
			
	        //get the contents of the plan
			planName = currentBudget;
			JSONreader reader = new JSONreader(getApplicationContext());
			plan = reader.read(currentBudget);
			
			System.out.println("Plan refreshed to: " + plan.toString());
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/**
	 * Checks to see if there was a budget active in the last user session.  If
	 * so, the name of the budget is returned, otherwise null.
	 * @return The name of the previously active budget, otherwise null.
	 */
	private String getCurrentBudget() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
		//SharedPreferences prefs = getPreferences(MODE_PRIVATE);
				
		String retVal = prefs.getString(TAG_CURRENT, null);
		
		if (retVal == null) {
			System.out.println("no current budget");
		}
		
		return  retVal; //returns null if no current plan found
	}

	public static JSONObject getPlan() {
		return plan;
	}

	public static void setPlan(JSONObject plan) {
		MainActivity.plan = plan;
	}

	public static String getPlanName() {
		return planName;
	}

	public static void setPlanName(String planName) {
		MainActivity.planName = planName;
	}

	public static void setCurrentBudget(String currentBudget) {
		MainActivity.currentBudget = currentBudget;
	}

}
