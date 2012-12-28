package com.chalmers.frapp.database;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * This class is the general database which stores
 * all buildings, their rooms and entrances.
 */
public class LocationDatabase {

	/**
	 * A map holding all buildings for fast lookup by name.
	 */
	private final Map<String, Building> buildings;

	/**
	 * Construct a new empty database.
	 */
	public LocationDatabase() {
		buildings = new TreeMap<String, Building>();
	}

	/**
	 * Get all buildings in this database.
	 * @return A collection of all buildings in this database
	 */
	public Collection<Building> getBuildings() {
		return buildings.values();
	}

	/**
	 * Get all rooms in this database identified as a string using the
	 * following format.
	 * 
	 * "<Building name>, <Room name>"
	 * 
	 * @return A list of strings identifying each room in this database
	 */
	public List<String> getStrings() {
		List<String> result = new LinkedList<String>();
		for(Building b : getBuildings()) {
			for(Room r : b.getRooms()) {
				result.add(b.getName() + ", " + r.getName());
			}
		}
		return result;
	}

	/**
	 * Get all rooms in this database by category, identified as a string
	 * using the following format.
	 * 
	 * "<Building name>, <Room name>"
	 * 
	 * @return A list of strings identifying each room in this database
	 */
	public List<String> getStrings(String category) {
		List<String> result = new LinkedList<String>();
		for(Building b : getBuildings()) {
			for(Room r : b.getRooms(category)) {
				result.add(b.getName() + ", " + r.getName());
			}
		}
		return result;
	}
	
	/**
	 * Add a new building to this database.
	 * 
	 * @param buildingName A string representing the name of a building
	 * @return The new building added to this database
	 */
	public Building addBuilding(String buildingName) {
		Building result = new Building(buildingName);
		buildings.put(buildingName, result);
		return result;
	}

	/**
	 * Find a specific building by name.
	 * 
	 * @param buildingName A string representing the building name to find
	 * @return A building with the given name or null if no such building exists
	 */
	public Building findBuilding(String buildingName) {
		return buildings.get(buildingName);
	}

	/**
	 * Get all categories across all buildings and rooms.
	 * 
	 * @return A set containing every category available in this database
	 */
	public Set<String> getAllCategories() {
		Set<String> availableCategories = new HashSet<String>();
		for(Building building : getBuildings()) {
			availableCategories.addAll(building.getAvailableCategories());
		}
		return availableCategories;
	}
}
