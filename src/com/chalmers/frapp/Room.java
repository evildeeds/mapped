package com.chalmers.frapp;

import java.util.List;
import com.google.android.maps.GeoPoint;

public class Room {

	private final String name;
	private final String description;
	private final List<String> entranceIDs;
	private final GeoPoint location;
	
	public Room(String name, String description, List<String> entranceIDs, GeoPoint location) {
		this.name = name;
		this.description = description;
		this.entranceIDs = entranceIDs;
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public List<String> getEntranceIDs() {
		return entranceIDs;
	}

	public GeoPoint getLocation() {
		return location;
	}
}
