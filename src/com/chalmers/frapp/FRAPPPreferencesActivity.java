package com.chalmers.frapp;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class FRAPPPreferencesActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
