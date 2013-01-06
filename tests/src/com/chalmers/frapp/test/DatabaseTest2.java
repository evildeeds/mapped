package com.chalmers.frapp.test;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import android.test.AndroidTestCase;

import com.chalmers.frapp.database.Building;
import com.chalmers.frapp.database.LocationDatabase;
import com.chalmers.frapp.database.Room;
import com.google.android.maps.GeoPoint;

/**
 * Verify that various advanced aspects of the database classes
 * are working as intended and according to their documentation.
 * Note: ensure that DatabaseTest1 passes first.
 */
public class DatabaseTest2 extends AndroidTestCase {

	private final static String buildingname1 = "testBuilding1";
	private final static String buildingname2 = "testBuilding2";
	private final static String roomname1 = "testLocation1";
	private final static String roomname2 = "testLocation2";
	private final static String roomname3 = "testLocation3";
	private final static String roomdescription = "testDescription";
	private final static String categoryname1 = "test category";
	private final static String categoryname2 = "another category";
	private final static GeoPoint testpoint = new GeoPoint(0, 0);
	private final static List<String> emptylist = new LinkedList<String>();
	private LocationDatabase ld;
	private Building b1;
	private Building b2;
	private Room r1;
	private Room r2;
	private Room r3;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		ld = new LocationDatabase();
		b1 = ld.addBuilding(buildingname1);
		b2 = ld.addBuilding(buildingname2);
		r1 = b1.addRoom(roomname1, roomdescription, emptylist, testpoint);
		r2 = b1.addRoom(roomname2, roomdescription, emptylist, testpoint);
		r3 = b2.addRoom(roomname3, roomdescription, emptylist, testpoint);
		r1.getCategories().add(categoryname1);
		r1.getCategories().add(categoryname2);
		r2.getCategories().add(categoryname1);
		r3.getCategories().add(categoryname2);
	}

	/**
	 * Verify that all building are present.
	 */
	public final void testBuildings() {
		assertEquals(2, ld.getBuildings().size());
		assertTrue(ld.getBuildings().contains(b1));
		assertTrue(ld.getBuildings().contains(b2));
	}

	/**
	 * Verify that all categories are present.
	 */
	public final void testCategories() {
		assertEquals(2, ld.getAllCategories().size());
		assertTrue(ld.getAllCategories().contains(categoryname1));
		assertTrue(ld.getAllCategories().contains(categoryname2));
	}
	
	/**
	 * Verify that the locations were correctly added to the
	 * buildings.
	 */
	public final void testLocations() {
		assertEquals(2, b1.getRooms().size());
		assertEquals(1, b2.getRooms().size());
		assertTrue(b1.getRooms().contains(r1));
		assertTrue(b1.getRooms().contains(r2));
		assertTrue(b2.getRooms().contains(r3));

		assertTrue(r1 == b1.findRoom(roomname1));
		assertTrue(r2 == b1.findRoom(roomname2));
		assertTrue(r3 == b2.findRoom(roomname3));
	}
	
	/**
	 * Verify that categories are working as intended.
	 */
	public final void testBasicCategorySearch() {
		assertEquals(2, b1.getRooms(categoryname1).size());
		assertEquals(1, b1.getRooms(categoryname2).size());
		assertEquals(0, b2.getRooms(categoryname1).size());
		assertEquals(1, b2.getRooms(categoryname2).size());
		
		assertTrue(b1.getRooms(categoryname1).contains(r1));
		assertTrue(b1.getRooms(categoryname1).contains(r2));
		assertTrue(b1.getRooms(categoryname2).contains(r1));
		assertTrue(b2.getRooms(categoryname2).contains(r3));
	}

	/**
	 * Verify that all location identifiers are present, unique and
	 * cannot be modified.
	 */
	public final void testLocationIdentifiers() {
		List<String> idlist = ld.getStrings();
		ListIterator<String> iter = idlist.listIterator();
		while(iter.hasNext()) {
			String id = iter.next();
			iter.remove();
			assertFalse(idlist.contains(id));
		}
		assertEquals(3, ld.getStrings().size());
	}
}
