package com.example.zoo;

import java.net.URL;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;

public class ZooAdapter extends SimpleCursorAdapter {

	
	public ZooAdapter(Context context, int layout, Cursor c, String[] from,
			int[] to) {
		super(context, layout, c, from, to);
		// TODO Auto-generated constructor stub
	}

	public void setViewImage (ImageView v, String value){
		//ImageLoader imgLoader = new ImageLoader(super.context);
	}

}
