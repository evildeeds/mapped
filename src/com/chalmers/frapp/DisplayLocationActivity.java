package com.chalmers.frapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;

import com.chalmers.frapp.database.LocationDatabase;
import com.chalmers.frapp.database.Parser;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class DisplayLocationActivity extends MapActivity {

	private MyLocationOverlay myLocation;
	private MapView mv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_location);
		mv = (MapView) findViewById(R.id.mapView1);
		// MapController mapController = mv.getController();
		mv.setBuiltInZoomControls(true);
		mv.displayZoomControls(true);
		myLocation = new MyLocationOverlay(this, mv);

		Parser parser;
        try {
        	parser = new Parser(getAssets().open("chalmers.xml"));
        } catch(Exception ex) {
        	ex.printStackTrace();
        	parser = null;
        }
        LocationDatabase db = parser.getDatabase();
        Drawable drawable = getResources().getDrawable(R.drawable.pin);
		DestinationOverlay dest = new DestinationOverlay(this, mv, drawable, db);

		Intent i = getIntent();
		dest.setDestination(i.getStringExtra("building"), i.getStringExtra("room"));

		mv.getOverlays().add(myLocation);
		mv.getOverlays().add(dest);

		myLocation.runOnFirstFix(new Runnable() {
			public void run() {
				mv.getController().animateTo(myLocation.getMyLocation());
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_display_location, menu);
		return true;
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		myLocation.enableCompass();
		myLocation.enableMyLocation();
	}

	protected void onPause() {
		myLocation.disableCompass();
		myLocation.disableMyLocation();
		super.onPause();
	}
}
