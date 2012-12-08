package com.chalmers.frapp;

import com.chalmers.frapp.database.Parser;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.*;

public class FindLocationActivity extends Activity {

    private static final String[] CONTENT = new String[] {
        "HA, HA2", "HA, HA3", "HB, HB2", "HB, HB3", "EDIT, Studion"
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_location);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, CONTENT);

        AutoCompleteTextView w = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        w.setThreshold(1);
        w.setAdapter(adapter);
 
        try {
        	new Parser(getAssets().open("chalmers.xml"));
        } catch(Exception ex) {
        	ex.printStackTrace();
        }

        //finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_find_location, menu);
        return true;
    }
}
