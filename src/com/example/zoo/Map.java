package com.example.zoo;

import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.os.Bundle;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.Menu;

public class Map extends MapActivity {

	private MapView mapView;
	private MapController mc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.google_map);

		mapView = (MapView) this.findViewById(R.id.mapView);
		mapView.setBuiltInZoomControls(false);

		mc = mapView.getController();
		mc.setZoom(20);

		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(
				R.drawable.ic_launcher);

		MarkerOverlay itemizedoverlay = new MarkerOverlay(drawable, this);
		double lat = Double.parseDouble("50.638298");
		double lon = Double.parseDouble("3.045576");
		GeoPoint point = new GeoPoint((int) (lat * 1E6), (int) (lon * 1E6));
		OverlayItem overlayitem = new OverlayItem(point, "Zoologique",
				"caonima");

		itemizedoverlay.addOverlay(overlayitem);
		mapOverlays.add(itemizedoverlay);
		mc.animateTo(point);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	protected boolean onKeydown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
			mapView.setSatellite(true);
			return true;
		}else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
			mapView.setSatellite(false);
			return true;
		}
		return super.onKeyDown(keyCode, event); 
	}

}
