package com.chalmers.frapp;

import java.util.List;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.Toast;

import com.chalmers.frapp.database.Building;
import com.chalmers.frapp.database.Entrance;
import com.chalmers.frapp.database.LocationDatabase;
import com.chalmers.frapp.database.Room;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

public class DestinationOverlay extends ItemizedOverlay<OverlayItem> {

	private final MapView target;
	private final Context context;
	private final LocationDatabase db;
	private String buildingName;
	private String roomName;

	public DestinationOverlay(Context arg0, MapView arg1, Drawable defaultMarker, LocationDatabase arg3) {
		super(boundCenterBottom(defaultMarker));
		context = arg0;
		target = arg1;
		db = arg3;
	}

	public boolean setDestination(String building, String room) {
		buildingName = building;
		roomName = room;
		populate();
		return true;
	}
	
	@Override
	protected OverlayItem createItem(int i) {
		Building b = db.findBuilding(buildingName);
		Room r = b.findRoom(roomName);
		List<Entrance> es = b.findEntrances(roomName);
		
		OverlayItem result;
		if(i == 0) {
			result = (OverlayItem) r;
		} else {
			result = es.get(i - 1);
		}
		return result;
	}

	@Override
	public int size() {
		Building b = db.findBuilding(buildingName);
		//Room r = b.findRoom(roomName); // +1
		List<Entrance> es = b.findEntrances(roomName);

		return es.size() + 1;
	}

	@Override
	public boolean onTap(int index) {
		OverlayItem item = createItem(index);
		Projection p = target.getProjection();
		Point pp = p.toPixels(item.getPoint(), null);
		Toast toast = Toast.makeText(context, item.getTitle() + ":\n" + item.getSnippet(), Toast.LENGTH_LONG);
		toast.setGravity(Gravity.TOP | Gravity.LEFT, pp.x, pp.y);
		toast.show();
		return true;
	}
}

