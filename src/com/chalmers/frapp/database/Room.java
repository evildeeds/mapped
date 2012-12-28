package com.chalmers.frapp.database;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
	/**
	 * Room categories.
	 */
	private final Set<String> roomCategories;

	/**
	 * Construct a new room.
	 * 
	 * @param name A string representing the name of the new room.
	 * @param description A string representing a description for the new room.
	 * @param entranceIDs A list of strings representing each entrance used to reach the new room.
	 * @param location A GeoPoint representing the exact location of the new room.
	 */
	public Room(String name, String description, List<String> entranceIDs, GeoPoint location) {
		super(location, name, description);
		this.entranceIDs = entranceIDs;
		this.roomCategories = new HashSet<String>();
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

	/**
	 * Get a set of categories which this rooms belongs to.
	 * @return A set of strings representing the names of all categories
	 * which this room belongs to.
	 */
	public Set<String> getCategories() {
		return roomCategories;
	}
}
