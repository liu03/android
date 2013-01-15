package com.example.zoo;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

public class Commenter extends Activity {

	private Commenter me = this;
	private EditText tnom;
	private EditText tcom;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.commenter);
		tnom = (EditText)findViewById(R.id.nom);
        tcom = (EditText)findViewById(R.id.commentaire);
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
		if(commentaire.equals("")){
			Toast.makeText(this, "Vous devez entrer un commentaire", Toast.LENGTH_SHORT).show();
			return;
		}else if(nom.equals("")){
			tnom.setText("Anonyme");
			Toast.makeText(this, "Commenter avec nom synonyme", Toast.LENGTH_SHORT).show();
		}
		new AlertDialog.Builder(me)
				.setTitle("Validation")
				.setMessage("Votre commentaire ont bien enregistré")
				.setPositiveButton("Fermer",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intention = new Intent(me, CommentaireActivity.class);
								intention.putExtra("id",id);
								startActivity(intention);
//								AppManager.getAppManager().finishActivity(me);
								finish();
								// TODO Auto-generated method stub
							}
						}).show();
	}

}
