package com.chalmers.frapp;

import com.google.android.maps.GeoPoint;

public class Entrance {

	private final String streetAddress;
	private final GeoPoint location;

	public Entrance(final String streetAddress, final GeoPoint location) {
		// TODO Auto-generated constructor stub
		this.streetAddress = streetAddress;
		this.location = location;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public GeoPoint getLocation() {
		return location;
	}
}
