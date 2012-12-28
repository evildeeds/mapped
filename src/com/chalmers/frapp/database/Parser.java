package com.chalmers.frapp.database;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import android.util.Pair;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.google.android.maps.GeoPoint;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * A parser class to create a new LocationDatabse from a
 * given XML file.
 * 
 * The general design is to use a stack representing the
 * currently open XML tags or XML path. Each tag includes
 * a parser state containing various data to be processed
 * by this parser class.
 */
public class Parser extends DefaultHandler {

	/**
	 * Enumeration of parsed tags.
	 */
	private enum XMLTags {
		BUILDINGS,
		BUILDING,
		ENTRANCES,
		ENTRANCE,
		ADDRESS,
		GEOPOINT,
		LOCATIONS,
		LOCATION,
		ENTRANCEID,
		CATEGORY,
		DESCRIPTION
	};

	/**
	 * The database which holds the parsed data.
	 */
	private LocationDatabase database;
	/**
	 * A stack to hold the current XML path and additional
	 * parser states.
	 */
	private LinkedList<Pair<XMLTags, Object>> parseStack;

	/**
	 * Parse an InputStream to a LocationDatabase.
	 * 
	 * @param inputStream An InputStream to parse.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public Parser(InputStream inputStream) throws ParserConfigurationException, SAXException, IOException {
		database = new LocationDatabase();
		parseStack = new LinkedList<Pair<XMLTags, Object>>();
		
		// getting SAXParserFactory instance
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
	
		// Getting SAXParser object from AXParserFactory instance
		SAXParser saxParser = saxParserFactory.newSAXParser();
	
		// Parsing XML Document by calling parse method of SAXParser class
		saxParser.parse(inputStream, this);
	}

	/**
	 * Get the parsed database.
	 * 
	 * @return A LocationDatabase filled with parsed information.
	 */
	public LocationDatabase getDatabase() {
		return database;
	}

	/**
	 * Parse a geopoint tag.
	 * 
	 * @param atts Attributes from the SAX parser. 
	 * @return A parsed GeoPoint
	 * @throws SAXException
	 */
	private GeoPoint parseGeopoint(Attributes atts) throws SAXException {
		Integer latitude = null;
		Integer longitude = null;
		int attributeCount = 0;
		for(int i = 0; i < atts.getLength(); i++) {
			// Accept attribute "lat".
			if(latitude == null && atts.getLocalName(i).toLowerCase().equals("lat")) {
				latitude = (int) (Double.parseDouble(atts.getValue(i)) * 1E6);
			
			// Accept attribute "long".
			} else if(longitude == null && atts.getLocalName(i).toLowerCase().equals("long")) {
				longitude = (int) (Double.parseDouble(atts.getValue(i)) * 1E6);

			// Any other attributes is an error.
			} else {
				throw new SAXException("Unexpected attribute: " + atts.getLocalName(i) + ", expected lat and long.");
			}
			attributeCount++;
		}

		// There must be exactly two attributes ("lat" and "long").
		if(attributeCount != 2) {
			throw new SAXException("Unexpected attribute count: " + attributeCount + ", expected two.");
		}

		return new GeoPoint(latitude, longitude);
	}

    public void startElement(String uri, String localName, String qName,
            Attributes atts) throws SAXException {
    	// If the current XML path is empty.
    	if(parseStack.isEmpty()) {
    		// Accept tag "buildings".
    		if(localName.toLowerCase().equals("buildings")) {
    			// Update the XML path
        		parseStack.add(new Pair<XMLTags, Object>(XMLTags.BUILDINGS, null));

        	// Any other tag is an error.
    		} else {
    			throw new SAXException("Unexpected tag: " + localName + ", expected buildings.");
    		}

    	// Currently processing a "buildings" tag.
    	} else if(parseStack.peekLast().first.equals(XMLTags.BUILDINGS)) {
    		// Accept any number of "building" tags.
    		if(localName.toLowerCase().equals("building")) {
        		int attributeCount = 0;
        		for(int i = 0; i < atts.getLength(); i++) {
        			// Accept the "name" XML attribute. 
        			if(atts.getLocalName(i).toLowerCase().equals("name")) {
        				// Update the XML path and include a building state connected to the database.
        				parseStack.add(new Pair<XMLTags, Object>(XMLTags.BUILDING, database.addBuilding(atts.getValue(i))));

        			// Any incorrect attribute is an error.
        			} else {
        				throw new SAXException("Unexpected attribute: " + atts.getLocalName(i) + ", expected name.");
        			}
        			attributeCount++;
        		}
     
        		// There must be exactly one attribute (and that is "name").
        		if(attributeCount != 1) {
        			throw new SAXException("Unexpected attribute count: " + attributeCount + ", expected one.");
        		}

        	// Any other tag is an error.
    		} else {
    			throw new SAXException("Unexpected tag: " + localName + ", expected building.");
    		}

    	// Currently processing a "building" tag.
    	} else if(parseStack.peekLast().first.equals(XMLTags.BUILDING)) {
    		// Accept "entrance" tag.
    		if(localName.toLowerCase().equals("entrances")) {
    			// Update the XML path and include the current building state.
    			parseStack.add(new Pair<XMLTags, Object>(XMLTags.ENTRANCES, parseStack.peekLast().second));

    		// Accept "location" tag.
    		} else if(localName.toLowerCase().equals("locations")) {
    			// Update the XML path and include the current building state.
    			parseStack.add(new Pair<XMLTags, Object>(XMLTags.LOCATIONS, parseStack.peekLast().second));

    		// Any other tag is an error.
    		} else {
    			throw new SAXException("Unexpected tag: " + localName + ", expected entrances or locations.");
    		}
    	
    	// Currently processing a "entrances"  tag.
    	} else if(parseStack.peekLast().first.equals(XMLTags.ENTRANCES)) {
    		// Accept "entrance" tag.
    		if(localName.toLowerCase().equals("entrance")) {
        		int attributeCount = 0;
        		for(int i = 0; i < atts.getLength(); i++) {
        			if(atts.getLocalName(i).toLowerCase().equals("id")) {
        				// Create a new state for this entrance.
        				ArrayList<Object> entranceData = new ArrayList<Object>(3);
        				entranceData.add(atts.getValue(i)); // The entrance ID (String)
        				entranceData.add(null);             // Placeholder for address (String)
        				entranceData.add(null);             // Placeholder for location (GeoPoint)
        				
        				// Update the XML path and use the new entrance state.
        				// Note: see processing of the "entrance" tag and the
        				// method characters for filling all placeholders.
        				parseStack.add(new Pair<XMLTags, Object>(XMLTags.ENTRANCE, entranceData));			
        			} else {
        				throw new SAXException("Unexpected attribute: " + atts.getLocalName(i) + ", expected id.");
        			}
        			attributeCount++;
        		}

        		if(attributeCount != 1) {
        			throw new SAXException("Unexpected attribute count: " + attributeCount + ", expected one.");
        		}
    		} else {
    			throw new SAXException("Unexpected tag: " + localName + ", expected entrance.");
    		}

    	// Process "entrance" tag.
    	} else if(parseStack.peekLast().first.equals(XMLTags.ENTRANCE)) {
    		ArrayList<Object> entranceData = (ArrayList<Object>) parseStack.peekLast().second;
    		if(entranceData.get(1) == null && localName.toLowerCase().equals("address")) {
    			parseStack.add(new Pair<XMLTags, Object>(XMLTags.ADDRESS, entranceData));
    		} else if(entranceData.get(2) == null && localName.toLowerCase().equals("geopoint")) {
        		entranceData.set(2, parseGeopoint(atts));
        		parseStack.add(new Pair<XMLTags, Object>(XMLTags.GEOPOINT, null));
    		} else {
    			throw new SAXException("Unexpected tag: " + localName + ", expected address or geopoint.");
    		}

    	// Process "locations" tag.
    	} else if(parseStack.peekLast().first.equals(XMLTags.LOCATIONS)) {
    		if(localName.toLowerCase().equals("location")) {
        		int attributeCount = 0;
        		for(int i = 0; i < atts.getLength(); i++) {
        			if(atts.getLocalName(i).toLowerCase().equals("name")) {
        				// Create a new state for this location/room.
        				ArrayList<Object> locationData = new ArrayList<Object>(5);
        				locationData.add(atts.getValue(i));         // Room name.
        				locationData.add(new LinkedList<String>()); // List of entranceIDs.
        				locationData.add(new LinkedList<String>()); // List of categories.
        				locationData.add(null);                     // GeoPoint placeholder.
        				locationData.add(null);                     // Description placeholder.
        				parseStack.add(new Pair<XMLTags, Object>(XMLTags.LOCATION, locationData));
        			} else {
        				throw new SAXException("Unexpected attribute: " + atts.getLocalName(i) + ", expected name.");
        			}
        			attributeCount++;
        		}

        		if(attributeCount != 1) {
        			throw new SAXException("Unexpected attribute count: " + attributeCount + ", expected one.");
        		}
    		} else {
    			throw new SAXException("Unexpected tag: " + localName + ", expected location.");
    		}

    	// Process "location" tag.
    	} else if(parseStack.peekLast().first.equals(XMLTags.LOCATION)) {
    		ArrayList<Object> locationData = (ArrayList<Object>) parseStack.peekLast().second;
    		if(localName.toLowerCase().equals("entrance")) {
    			parseStack.add(new Pair<XMLTags, Object>(XMLTags.ENTRANCEID, locationData.get(1)));
    		} else if(localName.toLowerCase().equals("category")) {
    			parseStack.add(new Pair<XMLTags, Object>(XMLTags.CATEGORY, locationData.get(2)));
    		} else if(localName.toLowerCase().equals("geopoint")) {
    			locationData.set(3, parseGeopoint(atts));
        		parseStack.add(new Pair<XMLTags, Object>(XMLTags.GEOPOINT, null));
    		} else if(localName.toLowerCase().equals("description")) {
    			parseStack.add(new Pair<XMLTags, Object>(XMLTags.DESCRIPTION, locationData));
    		} else {
    			throw new SAXException("Unexpected tag: " + localName + ", expected entrance, category, geopoint or description.");
    		}
    	}

    	// No other interesting start states should be present.
    }

    public void characters(char[] ch, int start, int length)
    		throws SAXException {
    	// Process "address" tag under "entrance".
    	if(parseStack.peekLast().first.equals(XMLTags.ADDRESS)) {
    		ArrayList<Object> entranceData = (ArrayList<Object>) parseStack.peekLast().second;
    		String address = new String(ch, start, length);
    		entranceData.set(1, address);
  
    	// Process "entrance" tag under "location" (room).
    	} else if(parseStack.peekLast().first.equals(XMLTags.ENTRANCEID)) {
    		LinkedList<String> entrances = (LinkedList<String>) parseStack.peekLast().second;
    		String entranceID = new String(ch, start, length);
    		entrances.add(entranceID);

    	// Process "category" tag under "location" (room).
    	} else if(parseStack.peekLast().first.equals(XMLTags.CATEGORY)) {
			LinkedList<String> categoryList = (LinkedList<String>) parseStack.peekLast().second;
			String category = new String(ch, start, length);
			categoryList.add(category);

		// Process "description" tag under "location" (room).
    	} else if(parseStack.peekLast().first.equals(XMLTags.DESCRIPTION)) {
    		ArrayList<Object> locationData = (ArrayList<Object>) parseStack.peekLast().second;
    		String description = new String(ch, start, length);
    		locationData.set(4, description);
    	}
    }
    
    public void endElement(String uri, String localName, String qName) 
            throws SAXException {
    	// Pop the current parse state from the parse stack
    	Pair<XMLTags, Object> current = parseStack.removeLast();

    	// When closing an "entrance" tag, add it to the building.
    	if(current.first.equals(XMLTags.ENTRANCE)) {
    		ArrayList<Object> entranceData = (ArrayList<Object>) current.second;
    		Building b = (Building) parseStack.peekLast().second;
    		b.addEntrance((String) entranceData.get(0), (String) entranceData.get(1), (GeoPoint) entranceData.get(2));
 
    	// When closing an "location" tag (room), add it to the building.
    	} else if(current.first.equals(XMLTags.LOCATION)) {
    		ArrayList<Object> locationData = (ArrayList<Object>) current.second;
    		Building b = (Building) parseStack.peekLast().second;
    		Room newRoom = b.addRoom((String) locationData.get(0), (String) locationData.get(4), (LinkedList<String>) locationData.get(1), (GeoPoint) locationData.get(3));
    		newRoom.getCategories().addAll((LinkedList<String>) locationData.get(2));
    	}
    }
 
    public void endDocument() throws SAXException {
    	// If the parse stack is not empty at the end of the XML document
    	// then the XML document is incorrect.
    	if(!parseStack.isEmpty()) {
    		throw new SAXException("Unexpected end of document.");
    	}
    }
}