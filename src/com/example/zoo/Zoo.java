package com.example.zoo;

import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class Zoo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zoo);
		AppManager.getAppManager().addActivity(this);
		CheckNetworkState();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.zoo, menu);
		return true;
	}
	 
	public void toMap(View vue){
		startActivity(new Intent(this, Map.class));
		//AppManager.getAppManager().finishActivity(this);
		//finish();
	}
	
	public void toList(View vue){
		startActivity(new Intent(this, List.class));
		//AppManager.getAppManager().finishActivity(this);
		//finish();
	}
	
	public void toQuestion(View vue){
		
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

	
	//Check network
    public void CheckNetworkState(){
        ConnectivityManager manager = (ConnectivityManager)getSystemService(
                Context.CONNECTIVITY_SERVICE);
        State mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if(mobile == State.CONNECTED||mobile==State.CONNECTING){
        	Log.v("Connectivity", "2G or 3G is actived !!!");
        	return;
        }
        if(wifi == State.CONNECTED||wifi==State.CONNECTING){
        	Log.v("Connectivity", "Wifi is actived !!!");
        	return;
        }
        showTips();
    }
    
    private void showTips(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle("Status of connectivity");
        builder.setMessage("Would you active the connectivity(Wifi)?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create();
        builder.show();
    }
	
}
