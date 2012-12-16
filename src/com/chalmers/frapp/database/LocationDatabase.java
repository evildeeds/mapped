package com.chalmers.frapp.database;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LocationDatabase {

	private final Map<String, Building> buildings;

	public LocationDatabase() {
		buildings = new TreeMap<String, Building>();
	}

	public Collection<Building> getBuildings() {
		return buildings.values();
	}

	public List<String> getStrings() {
		List<String> result = new LinkedList<String>();
		for(Building b : getBuildings()) {
			for(Room r : b.getRooms()) {
				result.add(b.getName() + ", " + r.getName());
			}
		}
		return result;
	}
	
	public Building addBuilding(String buildingName) {
		Building result = new Building(buildingName);
		buildings.put(buildingName, result);
		return result;
	}

	public Building findBuilding(String buildingName) {
		return buildings.get(buildingName);
	}
}
