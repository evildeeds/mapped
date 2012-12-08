package com.chalmers.frapp.database;

import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.android.maps.GeoPoint;

public class Building {

	private final String name;
	private final Map<String, Entrance> entrances;
	private final Map<String, Room> rooms;

	public Building(String buildingName) {
		name = buildingName;
		entrances = new TreeMap<String, Entrance>();
		rooms = new TreeMap<String, Room>();
	}

	public void addEntrence(String entranceID, String streetAddress, int longitude, int latitude) {
		GeoPoint location = new GeoPoint(longitude, latitude);
		entrances.put(entranceID, new Entrance(streetAddress, location));
	}

	public void addRoom(String name, String description, List<String> entranceIDs, int longitude, int latitude) {
		GeoPoint location = new GeoPoint(longitude, latitude);
		rooms.put(name, new Room(name, description, entranceIDs, location));
	}

	public String getName() {
		return name;
	}

	public Collection<Entrance> getEntrances() {
		return entrances.values();
	}

	public Collection<Room> getRooms() {
		return rooms.values();
	}
	
	public List<Entrance> findEntrances(String roomName) throws InvalidParameterException {
		Room roomObject = rooms.get(roomName);
		List<Entrance> entranceList = null;
		if(roomObject != null) {
			entranceList = new LinkedList<Entrance>();
			for(String entrenceID : roomObject.getEntranceIDs()) {
				entranceList.add(entrances.get(entrenceID));
			}
		} else {
			throw new InvalidParameterException(roomName + " is not part of this building");
		}
		return entranceList;
	}
}
