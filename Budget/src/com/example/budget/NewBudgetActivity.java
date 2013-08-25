package com.example.budget;

import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewBudgetActivity extends Activity {
			
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new);

		Button confirmButton = (Button) findViewById(R.id.confirmSalaryButton);
		final EditText nameInput = (EditText) findViewById(R.id.nameInput);
		final EditText salaryInput = (EditText) findViewById(R.id.salaryInput);
		final EditText withholdingsInput = (EditText) findViewById(R.id.withholdingsInput);
		
		//set cursor to top input field
		nameInput.requestFocus();

		confirmButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				//get data from text fields
				MainActivity.planName = nameInput.getText().toString();
				String salary = salaryInput.getText().toString();
				String withholdings = withholdingsInput.getText().toString();
				
				//create JSONArray that will contain the budget's info
				MainActivity.plan = new JSONObject();
				try {
					MainActivity.plan.put(MainActivity.TAG_FILENAME, MainActivity.planName);
					MainActivity.plan.put(MainActivity.TAG_SALARY, salary);
					MainActivity.plan.put(MainActivity.TAG_WITHHOLDINGS, withholdings);
					MainActivity.plan.put(MainActivity.TAG_CATEGORIES, new JSONArray());
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				
				try {
					//write the plan title to the list of files file
					JSONwriter.append(MainActivity.planName + "\n", MainActivity.ALL_FILES, NewBudgetActivity.this);
					//write the JSON data to the JSON file
					JSONwriter.write(MainActivity.plan.toString(), MainActivity.planName, NewBudgetActivity.this);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				//set current plan to be recognized nextTime
				saveCurrentBudget(MainActivity.planName);

				//move to activity to create categories
				System.out.println("creating new intent");
				
				Intent intent = new Intent(NewBudgetActivity.this, CategoriesActivity.class);
				intent.putExtra("fileName", MainActivity.planName);
				NewBudgetActivity.this.startActivity(intent);
			}
		});
	}
	
	/**
	 * Saves the current budget in user preferences.  When the Budget program is
	 * loaded up again, the user will be able to tap the Continue button and it
	 * will take them to their previous plan.
	 * @param budgetName the budget to save to user preferences.
	 */
	private void saveCurrentBudget(String budgetName) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(NewBudgetActivity.this);
		
		SharedPreferences.Editor editor =  prefs.edit();
		
		System.out.println("saving current budget as... " + budgetName);
		
		editor.putString(MainActivity.TAG_CURRENT, budgetName);
		
		editor.commit();
		
		MainActivity.currentBudget = budgetName;
	}
}
