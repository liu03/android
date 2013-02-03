package com.example.zoo;

import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.ZoomControls;

public class Map extends MapActivity {

	private MapView mapView;
	private MapController mc;
	private ZoomControls zoomControls;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.google_map);
		AppManager.getAppManager().addActivity(this);

		mapView = (MapView) this.findViewById(R.id.mapView);
    	mc = mapView.getController();
    	
    	mapView.setSatellite(true);
    	mc.setZoom(20);
    	mapView.setBuiltInZoomControls(false);
    	
    	List<Overlay> mapOverlays = mapView.getOverlays();
    	Drawable drawable = this.getResources().getDrawable(R.drawable.map_pin_48);
    	
    	MarkerOverlay itemizedoverlay = new MarkerOverlay(drawable, this);
    	double lat = Double.parseDouble("50.638341");
    	double lon = Double.parseDouble("3.045562");
    	GeoPoint point = new GeoPoint((int) (lat * 1E6), (int) (lon * 1E6));
		OverlayItem overlayitem = new OverlayItem(point, "Zoologique","caonima");
    	
		itemizedoverlay.addOverlay(overlayitem);
		mapOverlays.add(itemizedoverlay);
		mc.animateTo(point);
	}

    @Override
    protected void onStart(){
    	super.onStart();
    	
	   	zoomControls = (ZoomControls) findViewById(R.id.zoomcontrols);
    	zoomControls.setOnZoomInClickListener(new View.OnClickListener() {
    		public void onClick(View v) {
    			mapView.getController().zoomIn();
    		}
    	});
    	zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
    		public void onClick(View v) {
    			mapView.getController().zoomOut();
    		}
    	});
    }
    
	public void toShare(View vue){
		Uri shareonfb = Uri.parse("http://www.facebook.com/sharer/sharer.php?u=http://www.mairie-lille.fr/cms/accueil/sport-loisirs/zoo-lille&t=Parc zoologique de Lille");
		Intent intent = new Intent(Intent.ACTION_VIEW, shareonfb);
		startActivity(intent);
	}
	
	public void toBack(View vue){
		Activity previousActivity = AppManager.getAppManager().previousActivity();
		Intent intent = new Intent(this, previousActivity.getClass());
		startActivity(intent);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
    	if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
    		mapView.setSatellite(true);
    		return true;
    	}else if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
    		mapView.setSatellite(false);
    		return true;
    	}
    	return super.onKeyDown(keyCode, event);
    }

}