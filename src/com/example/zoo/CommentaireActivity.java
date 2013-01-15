package com.example.zoo;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView;

public class CommentaireActivity extends ListActivity {

	private CommentaireAdapter ca;
	private BD bd;
	private CommentaireActivity me = this;
	private TextView nom;
	private TextView favori;
	private TextView categorie;
	private TextView description;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.commentaire);
		bd = new BD(this);
		ca = new CommentaireAdapter(this);
		nom = (TextView) findViewById(R.id.nom_animal);
		favori = (TextView) findViewById(R.id.favori);
		categorie = (TextView) findViewById(R.id.cat_animal);
		description = (TextView) findViewById(R.id.introduction);
	}

	
	protected void onStart() {
		super.onStart();

		displayListView();
	}

	private void displayListView() {
		// TODO Auto-generated method stub
		Bundle donnees = getIntent().getExtras();
		int id = donnees.getInt("id");

		System.out.println("here"+id);
		Animal animal = bd.getAnimal(id);

		nom.setText(animal.getNom());
		favori.setText(String.valueOf(animal.getFavori())+" favoris");
		categorie.setText(animal.getCategorie());
		description.setText(animal.getDescription());
		Cursor cursor = ca.getCommentairesToCursor(id);
		String[] columns = new String[] { CommentaireAdapter.KEY_COMMENTAIRE,
				CommentaireAdapter.KEY_UTILISATEUR };
		int[] to = new int[] { R.id.contenu, R.id.auteur };
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.com_list_item, cursor, columns, to, 0);
		ListView listView = getListView();
		listView.setAdapter(adapter);
		adapter.setViewBinder(new ViewBinder() {
			public boolean setViewValue(View view, Object data,
					String textRepresentation) {
				if (view instanceof ImageView && data instanceof Drawable) {
					ImageView iv = (ImageView) view;
					iv.setImageDrawable((Drawable) data);
					return true;
				} else
					return false;
			}

			@Override
			public boolean setViewValue(View arg0, Cursor arg1, int arg2) {
				// TODO Auto-generated method stub
				return false;
			}

		});
	}

	public void toComment(View vue) {
		Intent intention = new Intent(this, Commenter.class);
		intention.putExtra("id",(int)(getIntent().getExtras().getInt("id")));
		startActivity(intention);
		//AppManager.getAppManager().finishActivity(this);
		finish();
	}

	public void toFavori(View vue) {
		new AlertDialog.Builder(me)
				.setTitle("Validation")
				.setMessage("Votre manipulation ont bien enregistré")
				.setPositiveButton("Fermer",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
							}
						}).show();
	}

}
