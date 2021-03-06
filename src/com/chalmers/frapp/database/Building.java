package com.chalmers.frapp.database;

import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
	 * @return The new room which was added to the this building
	 */
	public Room addRoom(String name, String description, List<String> entranceIDs, GeoPoint location) {
		Room newRoom = new Room(name, description, entranceIDs, location);
		rooms.put(name, newRoom);
		return newRoom;
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
	 * Get all rooms in this building which has the specified category.
	 * @return a Collection of rooms in this building.
	 */
	public Collection<Room> getRooms(String category) {
		List<Room> roomList = new LinkedList<Room>();
		for(Room room : getRooms()) {
			if(room.getCategories().contains(category)) {
				roomList.add(room);
			}
		}
		return roomList;
	}
	
	/**
	 * Find a room given its name.
	 * 
	 * @param roomName a String representing a room name
	 * @return a Room with the given name or null if no room was found
	 */
	public Room findRoom(String roomName) {
		return rooms.get(roomName);
	}
	
	/**
	 * Find all recommended entrances for a given room.
	 * 
	 * @param roomName a String representing the name of a room in this building.
	 * @return a List of entrances recommended for the given room
	 * @throws InvalidParameterException if the room is not present in this building
	 */
	public List<Entrance> findEntrances(String roomName) throws InvalidParameterException {
		Room roomObject = findRoom(roomName);
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

	/**
	 * Get all room categories in this building.
	 * 
	 * @return A set of strings representing the names of all
	 * categories in this building.
	 */
	public Set<String> getAvailableCategories() {
		Set<String> availableRoomCategories = new HashSet<String>();
		for(Room room : getRooms()) {
			availableRoomCategories.addAll(room.getCategories());
		}
		return availableRoomCategories;
	}
}
