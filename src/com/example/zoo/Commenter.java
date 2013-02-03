package com.example.zoo;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.util.List;
import java.util.ArrayList;

public class Commenter extends Activity {
	static final String URL = "http://10.188.15.222/gestion.php";

	private Commenter me = this;
	private EditText tnom;
	private EditText tcom;
	private Animal animal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.commenter);
		AppManager.getAppManager().addActivity(this);
		
		tnom = (EditText) findViewById(R.id.nom);
		tcom = (EditText) findViewById(R.id.commentaire);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();

		StrictMode.setThreadPolicy(policy);
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
		getMenuInflater().inflate(R.menu.pcommenter, menu);
		return true;
	}

	public void enregistrer(View vue) {
		String commentaire = tcom.getText().toString();
		String nom = tnom.getText().toString();
		Bundle donnees = getIntent().getExtras();
		final int id = donnees.getInt("id");
		if (commentaire.equals("")) {
			Toast.makeText(this, "Vous devez entrer un commentaire",
					Toast.LENGTH_SHORT).show();
			return;
		} else if (nom.equals("")) {
			tnom.setText("Anonyme");
			Toast.makeText(this, "Commenter avec nom synonyme",
					Toast.LENGTH_SHORT).show();
		}
		final HttpPost hp = new HttpPost(URL);
		class Query extends Thread {
			private String result;

			public String getResult() {
				return result;
			}

			@Override
			public void run() {

				List<NameValuePair> params = new ArrayList<NameValuePair>();
				// Add Post Data
				params.add(new BasicNameValuePair("action", "commenter"));
				params.add(new BasicNameValuePair("animal", String.valueOf(id)));
				params.add(new BasicNameValuePair("nom", tnom.getText()
						.toString()));
				params.add(new BasicNameValuePair("contenu", tcom.getText()
						.toString()));
				try {
					UrlEncodedFormEntity urf = new UrlEncodedFormEntity(params,
							HTTP.UTF_8);
					hp.setEntity(urf);
					HttpResponse hr = new DefaultHttpClient().execute(hp);
					if (hr.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(hr.getEntity());
					} else {
						result = "Error State:" + hr.getStatusLine().toString();
					}
					System.out.println(result);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		;
		Query q = new Query();
		q.run();
		try {
			q.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String res = q.getResult();
		new AlertDialog.Builder(me)
				.setTitle("Validation")
				.setMessage("Votre commentaire ont bien enregistré")
				.setPositiveButton("Fermer",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intention = new Intent(me,
										CommentaireActivity.class);
								intention.putExtra("id", id);
								startActivity(intention);
								AppManager.getAppManager().finishActivity(me);
								finish();
								// TODO Auto-generated method stub
							}
						}).show();
	}

}
