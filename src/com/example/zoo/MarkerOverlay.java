package com.example.zoo;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MarkerOverlay extends ItemizedOverlay<OverlayItem>{
	
	private ArrayList<OverlayItem> mo = new ArrayList<OverlayItem>();
	private Context c;
	
	public MarkerOverlay(Drawable arg0, Context cc) {
		super(boundCenterBottom(arg0));
		this.c = cc;
		// TODO Auto-generated constructor stub
	}

	public MarkerOverlay(Drawable arg0) {
		super(boundCenterBottom(arg0));
		// TODO Auto-generated constructor stub
	}
	
	public void addOverlay(OverlayItem ov){
		mo.add(ov);
		populate();
	}

	@Override
	protected OverlayItem createItem(int arg0) {
		// TODO Auto-generated method stub
		return mo.get(arg0);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return mo.size();
	}
	
	protected boolean onTap(int index){
		OverlayItem it = mo.get(index);
		AlertDialog.Builder dia = new AlertDialog.Builder(c);
		dia.setTitle(it.getTitle());
		dia.setMessage(it.getSnippet());
		dia.show();
		return true;
	}

}
