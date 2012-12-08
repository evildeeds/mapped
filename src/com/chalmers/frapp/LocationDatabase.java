package com.chalmers.frapp;

import java.util.Collection;
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
		throw new UnsupportedOperationException();
	}
	
	public Building addBuilding(String buildingName) {
		Building result = new Building(buildingName);
		buildings.put(buildingName, result);
		return result;
	}
}
