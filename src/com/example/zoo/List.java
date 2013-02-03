package com.example.zoo;


import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class List extends ListActivity {

	private BD bd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		AppManager.getAppManager().addActivity(this);
		bd = new BD(this);
	}

	protected void onStart() {
		super.onStart();

		displayListView();
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

	private void displayListView() {
		// TODO Auto-generated method stub

		Cursor cursor = bd.getAnimalsToCursor();
		String[] columns = new String[] { BD.KEY_NOM, BD.KEY_CATEGORIE, BD.KEY_IMAGE,
				BD.KEY_DESCRIPTION };
		int[] to = new int[] { R.id.nom, R.id.catagorie, R.id.image, R.id.description};
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.animal_info, cursor, columns, to, 0);
		ListView listView = getListView();
		listView.setAdapter(adapter);
		adapter.setViewBinder(new ViewBinder(){
			             public boolean setViewValue(View view,Object data,String textRepresentation){
				                 if(view instanceof ImageView && data instanceof Drawable){
				                      ImageView iv=(ImageView)view;
				                      iv.setImageDrawable((Drawable)data);
				                     return true;
				                 }
				                 else return false;
				            }

						@Override
						public boolean setViewValue(View arg0, Cursor arg1,
								int arg2) {
							// TODO Auto-generated method stub
							return false;                               
						}

				        });

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub	public void toList(View vue){

				Intent intention = new Intent(List.this, CommentaireActivity.class);
				intention.putExtra("id",(int)(parent.getItemIdAtPosition(position)));
				startActivity(intention);
//				AppManager.getAppManager().finishActivity(me);
//				finish();

				return false;
			}

		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

			}

		});
	}

	protected void onDestroy() {
		bd.fermeture();
		super.onDestroy();
	}

}
