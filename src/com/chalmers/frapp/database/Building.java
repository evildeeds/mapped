package com.chalmers.frapp.database;

import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.android.maps.GeoPoint;

/**
 * This class represents a single building.
 */
public class Building {

	/**
	 * The name of this building.
	 */
	private final String name;
	/**
	 * A map containing all entrances, for quickly finding an entrance
	 * given an entrance ID.
	 */
	private final Map<String, Entrance> entrances;
	/**
	 * A map containing all rooms in this building, for quickly finding
	 * a room given its name.
	 */
	private final Map<String, Room> rooms;

	public Building(String buildingName) {
		name = buildingName;
		entrances = new TreeMap<String, Entrance>();
		rooms = new TreeMap<String, Room>();
	}

	/**
	 * Add a new entrance to this building.
	 * 
	 * @param entranceID a String representing the entrance ID
	 * @param streetAddress a String representing the street address
	 * @param location an GeoPoint representing the GPS coordinate for the new Entrance
	 */
	public void addEntrance(String entranceID, String streetAddress, GeoPoint location) {
		entrances.put(entranceID, new Entrance(streetAddress, location));
	}

	/**
	 * Add a new room to this building.
	 * 
	 * @param name a String representing the name of the new room
	 * @param description a String representing a description of the new room
	 * @param entranceIDs a List of entranceIDs recommended to reach the new room
	 * @param location an GeoPoint representing the GPS coordinate for the new Room
	 */
	public void addRoom(String name, String description, List<String> entranceIDs, GeoPoint location) {
		rooms.put(name, new Room(name, description, entranceIDs, location));
	}

	/**
	 * Get the name of this building.
	 * 
	 * @return a String representing the name of this building
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get all entrances into this building.
	 * @return a Collection of entrances into this building
	 */
	public Collection<Entrance> getEntrances() {
		return entrances.values();
	}

	/**
	 * Get all rooms in this building.
	 * @return a Collection of rooms in this building.
	 */
	public Collection<Room> getRooms() {
		return rooms.values();
	}

	/**
	 * Find all recommended entrances for a given room.
	 * 
	 * @param roomName a String representing the name of a room in this building.
	 * @return a List of entrances recommended for the given room
	 * @throws InvalidParameterException if the room is not present in this building
	 */
	public List<Entrance> findEntrances(String roomName) throws InvalidParameterException {
		Room roomObject = rooms.get(roomName);
		List<Entrance> entranceList = null;
		// Verify that the given room actually is a part of this building.
		if(roomObject != null) {
			// Generate the result list by fetching all entrance IDs and resolving the entrance.
			entranceList = new LinkedList<Entrance>();
			for(String entranceID : roomObject.getEntranceIDs()) {
				entranceList.add(entrances.get(entranceID));
			}
		} else {
			throw new InvalidParameterException(roomName + " is not part of this building");
		}
		return entranceList;
	}
}
