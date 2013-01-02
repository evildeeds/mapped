package com.chalmers.frapp.test;

import java.util.LinkedList;
import java.util.List;

import android.test.AndroidTestCase;

import com.chalmers.frapp.database.Building;
import com.chalmers.frapp.database.LocationDatabase;
import com.chalmers.frapp.database.Room;
import com.google.android.maps.GeoPoint;

/**
 * Verify that various aspects of the database classes are
 * working as intended and according to their documentation.
 */
public class DatabaseTest extends AndroidTestCase {

	protected LocationDatabase ld;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		ld = new LocationDatabase();
	}
	
	/**
	 * Verify that a building can be added and is accessible.
	 */
	public final void testDatabase1() {
		String buildingname = "testBuilding";
		Building b = ld.addBuilding(buildingname);
		assertEquals(1, ld.getBuildings().size());
		assertTrue(ld.getBuildings().contains(b));
		assertTrue(b == ld.findBuilding(buildingname));
	}

	/**
	 * Verify that a building and a location (room) can be
	 * added and is accessible.
	 */
	public final void testDatabase2() {
		String buildingname = "testBuilding";
		Building b = ld.addBuilding(buildingname);
		String roomname = "testLocation";
		String roomdescription = "test";
		List<String> emptylist = new LinkedList<String>();
		GeoPoint testpoint = new GeoPoint(0, 0);
		Room r = b.addRoom(roomname, roomdescription, emptylist, testpoint);
		String categoryname = "test category";
		r.getCategories().add(categoryname);
		assertEquals(1, b.getRooms().size());
		assertTrue(b.getRooms().contains(r));
		assertEquals(1, b.getRooms(categoryname).size());
		assertTrue(b.getRooms(categoryname).contains(r));
		assertTrue(r == b.findRoom(roomname));
		assertEquals(roomdescription, r.getDescription());
		assertTrue(testpoint == r.getPoint());
	}
	
}
