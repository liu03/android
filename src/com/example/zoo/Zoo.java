package com.example.zoo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class Zoo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zoo);
		AppManager.getAppManager().addActivity(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.zoo, menu);
		return true;
	}
	
	public void toMap(View vue){
		startActivity(new Intent(this, List.class));
		AppManager.getAppManager().finishActivity(this);
		finish();
	}
	
	public void toList(View vue){
		startActivity(new Intent(this, List.class));
		AppManager.getAppManager().finishActivity(this);
		finish();
	}

}
