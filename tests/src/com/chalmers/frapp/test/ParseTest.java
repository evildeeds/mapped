package com.chalmers.frapp.test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import android.test.AndroidTestCase;

import com.chalmers.frapp.database.Building;
import com.chalmers.frapp.database.LocationDatabase;
import com.chalmers.frapp.database.Parser;
import com.chalmers.frapp.database.Room;

/**
 * Test class for testing the sanity of the parser implementation and
 * verifying that the default shipped database can be parsed without
 * errors.
 */
public class ParseTest extends AndroidTestCase {

	public final static String enc_utf = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
	public final static String xml1 = enc_utf + "<buildings></buildings>";
	public final static String empty_building = "<building name=\"testBuilding\"><entrances></entrances><locations></locations></building>";
	public final static String xml2 = enc_utf + "<buildings>" + empty_building + "</buildings>";
	public final static String point = "<geopoint lat=\"10.10\" long=\"10.10\" />";
	public final static String entrance = "<entrance id=\"first\"><address>Somewhere 1010</address>" + point + "</entrance>";
	public final static String location = "<location name=\"testLocation\"><entrance>first</entrance><category>any</category>" + point + "<description>Test text</description></location>";
	public final static String building = "<building name=\"testBuilding2\"><entrances>" + entrance + "</entrances><locations>" + location + "</locations></building>";
	public final static String xml3 = enc_utf + "<buildings>" + building + "</buildings>";

	/**
	 * The SAX parser requires at least one XML element.
	 */
	public final void testEmpty() {
		try {
			Parser p = new Parser((InputStream) new ByteArrayInputStream("".getBytes()));
			// Always fail since the SAX parser should have thrown an error.
			assertTrue(false);
		} catch(Exception ex) {
			assertTrue(true);
		}
	}

	/**
	 * The SAX parser requires at least one XML element.
	 */
	public final void testAlmostEmpty() {
		try {
			Parser p = new Parser((InputStream) new ByteArrayInputStream(enc_utf.getBytes()));
			assertTrue(false);
		} catch(Exception ex) {
			assertTrue(true);
		}
	}
	
	/**
	 * A document with only an empty building list is a valid
	 * database without any buildings, rooms or categories.
	 */
	public final void testNoBuildings() {
		try {
			Parser p = new Parser((InputStream) new ByteArrayInputStream(xml1.getBytes()));
			LocationDatabase ld = p.getDatabase();
			// Upon "success" the database should be present.
			assertNotNull(ld);
			assertEquals(0, ld.getBuildings().size());
			assertEquals(0, ld.getAllCategories().size());
		} catch(Exception ex) {
			assertTrue(false);
		}
	}

	/**
	 * Verify that empty buildings can be read and found.
	 */
	public final void testEmptyBuilding() {
		try {
			Parser p = new Parser((InputStream) new ByteArrayInputStream(xml2.getBytes()));
			LocationDatabase ld = p.getDatabase();
			assertNotNull(ld);
			assertEquals(1, ld.getBuildings().size());
			assertEquals(0, ld.getAllCategories().size());
			Building b = ld.findBuilding("testBuilding");
			assertNotNull(b);
			assertEquals(0, b.getEntrances().size());
			assertEquals(0, b.getRooms().size());
		} catch(Exception ex) {
			assertTrue(false);
		}
	}

	/**
	 * Verify that a building with a location (room) and entrance can be read.
	 */
	public final void testBasicLocation() {
		try {
			Parser p = new Parser((InputStream) new ByteArrayInputStream(xml3.getBytes()));
			LocationDatabase ld = p.getDatabase();
			assertNotNull(ld);
			assertEquals(1, ld.getBuildings().size());
			assertEquals(1, ld.getAllCategories().size());
			Building b = ld.findBuilding("testBuilding2");
			assertNotNull(b);
			assertEquals(1, b.getEntrances().size());
			assertEquals(1, b.getRooms().size());
			Room r = b.findRoom("testLocation");
			assertNotNull(r);
		} catch(Exception ex) {
			assertTrue(false);
		}
	}
	
	/**
	 * Test that the bundled "chalmers.xml" can be parsed, failure here
	 * is likely due to that the file is incorrect. 
	 */
	public final void testParseProvidedAsset() {
		try {
			Parser p = new Parser(getContext().getAssets().open("chalmers.xml"));
			assertNotNull(p.getDatabase());
			// Note: don't test the parsed content as it may be subjected to change.
		} catch(Exception ex) {
			assertTrue(false);
		}
	}
}
