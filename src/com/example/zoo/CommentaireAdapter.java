package com.example.zoo;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class CommentaireAdapter extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "zoo.bd";
	private static final String BASE_URL = "http://10.188.15.39/b.php";

	public static final String KEY_ID = "_id";
	public static final String KEY_UTILISATEUR = "utilisateur";
	public static final String KEY_ANIMAL = "animal";
	public static final String KEY_COMMENTAIRE = "commentaire";

	private static final String SQLITE_TABLE = "commentaires";

	private SQLiteDatabase bd;
	private Context context;

	public CommentaireAdapter(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	public CommentaireAdapter(Context ctx) {
		super(ctx, DATABASE_NAME, null, 1);
		this.context = ctx;
		bd = getWritableDatabase();
		//reinit();
	}

	public String getBDDName() {
		return DATABASE_NAME;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		bd.execSQL("CREATE TABLE " + SQLITE_TABLE + " ( " + KEY_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_UTILISATEUR
				+ " TEXT NOT NULL," + KEY_ANIMAL + " INTEGER NOT NULL," + KEY_COMMENTAIRE
				+ " TEXT NOT NULL);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE " + SQLITE_TABLE + ";");
		onCreate(db);
	}

	public void reinit() {
		if (bd.isOpen())
			fermeture();

		bd = getWritableDatabase();
		bd.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE + ";");
		bd.execSQL("CREATE TABLE " + SQLITE_TABLE + " ( " + KEY_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_UTILISATEUR
				+ " TEXT NOT NULL," + KEY_ANIMAL + " INTEGER NOT NULL," + KEY_COMMENTAIRE
				+ " TEXT NOT NULL);");
	}

	public void fermeture() {
		if (bd.inTransaction())
			bd.endTransaction();
		bd.close();
	}

	public long ajouter(Commentaire commentaire) {
		ContentValues valeurs = new ContentValues();
		valeurs.put(KEY_UTILISATEUR, commentaire.getUtilisateur());
		valeurs.put(KEY_ANIMAL, commentaire.getAnimal());
		valeurs.put(KEY_COMMENTAIRE, commentaire.getCommentaire());
		return bd.insert(SQLITE_TABLE, null, valeurs);
	}

	public int miseAJour(Commentaire commentaire) {
		ContentValues valeurs = new ContentValues();
		valeurs.put(KEY_UTILISATEUR, commentaire.getUtilisateur());
		valeurs.put(KEY_ANIMAL, commentaire.getAnimal());
		valeurs.put(KEY_COMMENTAIRE, commentaire.getCommentaire());
		return bd.update(SQLITE_TABLE, valeurs,
				KEY_ID + " = " + commentaire.getId(), null);
	}

	public int supprimer(int id) {
		return bd.delete(SQLITE_TABLE, KEY_ID + " = " + id, null);
	}

	public Commentaire getCommentaire(int id) {
		Cursor curseur = bd.query(SQLITE_TABLE, null, KEY_ID + " = " + id,
				null, null, null, null);
		if (curseur.getCount() == 0)
			return null;
		else {
			curseur.moveToFirst();
			return curseurToCommentaire(curseur);
		}
	}

	public ArrayList<Commentaire> getCommentaires() {
		ArrayList<Commentaire> liste = new ArrayList<Commentaire>();
		Cursor curseur = bd.query(SQLITE_TABLE, null, null, null, null, null,
				"utilisateur, animal, commentaire");
		if (curseur.getCount() == 0)
			return liste;
		else {
			curseur.moveToFirst();
			do {
				liste.add(curseurToCommentaire(curseur));
			} while (curseur.moveToFirst());
			curseur.close();
			return liste;
		}
	}

	public Cursor getCommentairesToCursor(int id) {

		if (InternetAdapter.isNetWorkAvailable(context)) {
			Thread t = new Thread(listRun);
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Cursor curseur = bd.query(SQLITE_TABLE, null, "animal=" + id, null, null,
				null, "utilisateur, animal, commentaire");
		if (curseur != null)
			curseur.moveToFirst();
		return curseur;
	}

	public Commentaire curseurToCommentaire(Cursor curseur) {
		// TODO Auto-generated method stub
		Commentaire commentaire = new Commentaire();
		commentaire.setId(curseur.getInt(0));
		commentaire.setUtilisateur(curseur.getString(1));
		commentaire.setAnimal(curseur.getInt(2));
		commentaire.setCommentaire(curseur.getString(3));
		return commentaire;
	}

	public void check(String url) {
		try {
			JSONObject jsonObject = JSONUtil.getJSON(url);

			Iterator<String> it = jsonObject.keys();
			while (it.hasNext()) {
				Commentaire com = new Commentaire();
				JSONObject childs = jsonObject.getJSONObject(it.next());
				int _id = childs.getInt("_id");
				String utilisateur = childs.getString("utilisateur");
				int animal = childs.getInt("animal");
				String commentaire = childs.getString("commentaire");

				com.setId(_id);
				com.setUtilisateur(utilisateur);
				com.setAnimal(animal);
				com.setCommentaire(commentaire);
				if (getCommentaire(_id) != null)
					miseAJour(com);
				else
					ajouter(com);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	Runnable listRun = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			check(BASE_URL);
		}
	};

}
