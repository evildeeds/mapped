package com.chalmers.frapp;

import java.util.regex.Pattern;

import com.chalmers.frapp.database.Building;
import com.chalmers.frapp.database.LocationDatabase;
import com.chalmers.frapp.database.Parser;
import com.chalmers.frapp.database.Room;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.widget.*;

public class FindLocationActivity extends Activity implements TextWatcher {

	private static final Pattern splitPattern = Pattern.compile(",\\s");
	private AutoCompleteTextView w;
	private LocationDatabase db;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_location);

        try {
        	Parser parser = new Parser(getAssets().open("chalmers.xml"));
        	db = parser.getDatabase();

        	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_dropdown_item_1line, db.getStrings());

        	w = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
            w.setThreshold(1);
            w.setAdapter(adapter);
            w.addTextChangedListener(this);
        } catch(Exception ex) {
        	ex.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_find_location, menu);
        return true;
    }

	@Override
	public void afterTextChanged(Editable s) {
		String[] data = splitPattern.split(w.getText());
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
}
