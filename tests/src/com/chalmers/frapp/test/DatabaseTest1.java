package com.chalmers.frapp.test;

import java.util.LinkedList;
import java.util.List;

import android.test.AndroidTestCase;

import com.chalmers.frapp.database.Building;
import com.chalmers.frapp.database.LocationDatabase;
import com.chalmers.frapp.database.Room;
import com.google.android.maps.GeoPoint;

/**
 * Verify that various basic aspects of the database classes
 * are working as intended and according to their documentation.
 */
public class DatabaseTest1 extends AndroidTestCase {

	private final static String buildingname = "testBuilding";
	private final static String roomname = "testLocation";
	private final static String roomdescription = "test";
	private final static List<String> emptylist = new LinkedList<String>();
	private final static GeoPoint testpoint = new GeoPoint(0, 0);
	private LocationDatabase ld;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		ld = new LocationDatabase();
	}
	
	/**
	 * Verify that a building can be added and is accessible.
	 */
	public final void testDatabase1() {
		Building b = ld.addBuilding(buildingname);
		assertEquals(1, ld.getBuildings().size());
		assertTrue(ld.getBuildings().contains(b));
		assertTrue(b == ld.findBuilding(buildingname));
	}

	/**
	 * Verify that a building and a location (room) can be
	 * added, is accessible and contains correct data.
	 */
	public final void testDatabase2() {
		Building b = ld.addBuilding(buildingname);
		Room r = b.addRoom(roomname, roomdescription, emptylist, testpoint);
		assertEquals(1, b.getRooms().size());
		assertTrue(b.getRooms().contains(r));
		assertTrue(r == b.findRoom(roomname));
		assertEquals(roomdescription, r.getDescription());
		assertTrue(testpoint.equals(r.getPoint()));
	}
	
}
