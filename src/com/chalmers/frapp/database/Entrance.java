package com.chalmers.frapp.database;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class Entrance extends OverlayItem {

	public Entrance(final String streetAddress, final GeoPoint location) {
		super(location, "Entrance", streetAddress);
	}

	public String getStreetAddress() {
		return getSnippet();
	}

	public GeoPoint getLocation() {
		return getPoint();
	}
}
