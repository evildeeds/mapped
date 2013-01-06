package com.chalmers.frapp;

import java.util.Set;
import java.util.regex.Pattern;

import com.chalmers.frapp.database.Building;
import com.chalmers.frapp.database.LocationDatabase;
import com.chalmers.frapp.database.Parser;
import com.chalmers.frapp.database.Room;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;

public class FindLocationActivity extends Activity implements TextWatcher, OnItemSelectedListener {

	private static final Pattern splitPattern = Pattern.compile(",\\s");
	private AutoCompleteTextView textbox;
	private Spinner spinner;
	private LocationDatabase db;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_location);

        try {
        	Parser parser = new Parser(getAssets().open(getDatabaseName()));
        	db = parser.getDatabase();

        	final Set<String> allCategories = db.getAllCategories();
        	String[] categoryArray = new String[allCategories.size() + 1];
        	allCategories.toArray(categoryArray);
        	categoryArray[allCategories.size()] = "";
        	ArrayAdapter<String> adapterCategories = new ArrayAdapter<String>(this,
        			android.R.layout.simple_spinner_dropdown_item, categoryArray);

        	spinner = (Spinner) findViewById(R.id.spinner1);
        	spinner.setAdapter(adapterCategories);
        	spinner.setOnItemSelectedListener(this);

        	textbox = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
            textbox.setThreshold(1);
            textbox.addTextChangedListener(this);
            
            spinner.setSelection(allCategories.size());
        } catch(Exception ex) {
        	throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_find_location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_settings:
        	Intent prefsIntent = new Intent(this, FRAPPPreferencesActivity.class);
        	startActivity(prefsIntent);
            break;
        }
        return true;
    }
    
	@Override
	public void afterTextChanged(Editable s) {
		String[] data = splitPattern.split(textbox.getText());
		if(data.length == 2) {
			Building b = db.findBuilding(data[0]);
			Room r = null;
			if (b != null) {
				r = b.findRoom(data[1]);
			}
			if (r != null) {
				Intent intent = new Intent(this, DisplayLocationActivity.class);
				//intent.putExtra();
				intent.putExtra("building", data[0]);
				intent.putExtra("room", data[1]);
				startActivity(intent);
			}
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// Empty
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// Empty
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		String selected = (String) arg0.getSelectedItem();
        updateList(selected);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		updateList("");
	}

	private void updateList(String category) {
		ArrayAdapter<String> adapter;
		if(category.isEmpty()) {
			adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, db.getStrings(category));
		} else {
			adapter = new ArrayAdapter<String>(this,
	                android.R.layout.simple_dropdown_item_1line, db.getStrings());
		}

        textbox.setAdapter(adapter);
	}
	
	private String getDatabaseName() {
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);
        return p.getString(getString(R.string.pref_key_database), getString(R.string.pref_default_database));
	}
}
