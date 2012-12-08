package com.chalmers.frapp.database;

import java.util.List;
import com.google.android.maps.GeoPoint;

/**
 * This class represents information about a single room.
 */
public class Room {

	/**
	 * Room name.
	 */
	private final String name;
	/**
	 * Room description.
	 */
	private final String description;
	/**
	 * List of entrance IDs.
	 */
	private final List<String> entranceIDs;
	/**
	 * Room GPS location.
	 */
	private final GeoPoint location;
	
	public Room(String name, String description, List<String> entranceIDs, GeoPoint location) {
		this.name = name;
		this.description = description;
		this.entranceIDs = entranceIDs;
		this.location = location;
	}

	/**
	 * Get the name of this room.
	 * @return a String representing a room name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the description for this room.
	 * @return a String representing a description for this room
	 */
	public String getDescription() {
		return description;
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
		return location;
	}
}
