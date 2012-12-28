package com.chalmers.frapp.database;

import java.util.List;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

/**
 * This class represents information about a single room.
 */
public class Room extends OverlayItem {

	/**
	 * List of entrance IDs.
	 */
	private final List<String> entranceIDs;
	
	public Room(String name, String description, List<String> entranceIDs, GeoPoint location) {
		super(location, name, description);
		this.entranceIDs = entranceIDs;
	}

	/**
	 * Get the name of this room.
	 * @return a String representing a room name
	 */
	public String getName() {
		return getTitle();
	}

	/**
	 * Get the description for this room.
	 * @return a String representing a description for this room
	 */
	public String getDescription() {
		return getSnippet();
	}

	/**
	 * Get the list of entrance IDs for this room.
	 * @return a List of Strings each representing an ID
	 */
	public List<String> getEntranceIDs() {
		return entranceIDs;
	}

	/**
	 * Get the location for this room.
	 * @return a GeoPoint representing the location of this room
	 */
	public GeoPoint getLocation() {
		return getPoint();
	}
}
