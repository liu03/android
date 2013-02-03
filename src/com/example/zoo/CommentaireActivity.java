package com.example.zoo;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
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

@SuppressLint("NewApi")
public class CommentaireActivity extends ListActivity {

	private CommentaireAdapter ca;
	private BD bd;
	private CommentaireActivity me = this;
	private TextView nom;
	private TextView favori;
	private TextView categorie;
	private TextView description;
	private int id;
	private Animal animal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.commentaire);
		AppManager.getAppManager().addActivity(this);
		
		bd = new BD(this);
		ca = new CommentaireAdapter(this);
		nom = (TextView) findViewById(R.id.nom_animal);
		favori = (TextView) findViewById(R.id.favori);
		categorie = (TextView) findViewById(R.id.cat_animal);
		description = (TextView) findViewById(R.id.introduction);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();

		StrictMode.setThreadPolicy(policy);
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
		Bundle donnees = getIntent().getExtras();
		id = donnees.getInt("id");

		animal = bd.getAnimal(id);

		nom.setText(animal.getNom());
		favori.setText(String.valueOf(animal.getFavori()) + " favoris");
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
		intention.putExtra("id", (int) (getIntent().getExtras().getInt("id")));
		startActivity(intention);
		// AppManager.getAppManager().finishActivity(this);
		//finish();
	}

	public void toFavori(View vue) {
		Thread t = new Thread() {
			HttpPost hp = new HttpPost(Commenter.URL);

			public void run() {
				String res = "";

				List<NameValuePair> params = new ArrayList<NameValuePair>();
				// Add Post Data
				params.add(new BasicNameValuePair("action", "favori"));
				params.add(new BasicNameValuePair("animal", String.valueOf(id)));
				try {
					UrlEncodedFormEntity urf = new UrlEncodedFormEntity(params,
							HTTP.UTF_8);
					hp.setEntity(urf);
					HttpResponse hr = new DefaultHttpClient().execute(hp);

					if (hr.getStatusLine().getStatusCode() == 200) {
						res += EntityUtils.toString(hr.getEntity());
					} else {
						res += "Error State:" + hr.getStatusLine().toString();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		t.run();
		try {
			t.join();
			bd.update();
			favori.setText(String.valueOf(animal.getFavori()) + " favoris");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		new AlertDialog.Builder(me)
				.setTitle("Validation")
				.setMessage("Votre manipulation ont bien enregistrée")
				.setPositiveButton("Fermer",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
							}
						}).show();
	}

	protected void onDestroy() {
		bd.fermeture();
		ca.fermeture();
		super.onDestroy();
	}

}
