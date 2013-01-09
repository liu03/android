package com.example.zoo;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BD extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "zoo.bd";
	private static final String BASE_URL = "http://webtp.fil.univ-lille1.fr/~liu/a.php";

	public static final String KEY_ID = "_id";
	public static final String KEY_NOM = "nom";
	public static final String KEY_CATEGORIE = "categorie";
	public static final String KEY_IMAGE = "image";
	public static final String KEY_DESCRIPTION = "description";

	private static final String SQLITE_TABLE = "animaux";

	private SQLiteDatabase bd;

	public BD(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public BD(Context ctx) {
		super(ctx, DATABASE_NAME, null, 1);
		bd = getWritableDatabase();
	}

	public String getBDDName() {
		return DATABASE_NAME;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE " + SQLITE_TABLE + " ( " + KEY_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NOM
				+ " TEXT NOT NULL," + KEY_CATEGORIE + " TEXT NOT NULL,"
				+ KEY_IMAGE + " TEXT NOT NULL," + KEY_DESCRIPTION
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
		bd.execSQL("DROP TABLE " + SQLITE_TABLE + ";");
		bd.execSQL("CREATE TABLE " + SQLITE_TABLE + " ( " + KEY_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NOM
				+ " TEXT NOT NULL," + KEY_CATEGORIE + " TEXT NOT NULL,"
				+ KEY_IMAGE + " TEXT NOT NULL," + KEY_DESCRIPTION
				+ " TEXT NOT NULL);");
	}

	public void fermeture() {
		if (bd.inTransaction())
			bd.endTransaction();
		bd.close();
	}

	public long ajouter(Animal animal) {
		ContentValues valeurs = new ContentValues();
		valeurs.put(KEY_NOM, animal.getNom());
		valeurs.put(KEY_CATEGORIE, animal.getCategorie());
		valeurs.put(KEY_IMAGE, animal.getImage());
		valeurs.put(KEY_DESCRIPTION, animal.getDescription());
		return bd.insert(SQLITE_TABLE, null, valeurs);
	}

	public int miseAJour(Animal animal) {
		ContentValues valeurs = new ContentValues();
		valeurs.put(KEY_NOM, animal.getNom());
		valeurs.put(KEY_CATEGORIE, animal.getCategorie());
		valeurs.put(KEY_IMAGE, animal.getImage());
		valeurs.put(KEY_DESCRIPTION, animal.getDescription());
		return bd.update(SQLITE_TABLE, valeurs,
				KEY_ID + " = " + animal.getId(), null);
	}

	public int supprimer(int id) {
		return bd.delete(SQLITE_TABLE, KEY_ID + " = " + id, null);
	}

	public Animal getAnimal(int id) {
		Cursor curseur = bd.query(SQLITE_TABLE, null, KEY_ID + " = " + id,
				null, null, null, null);
		if (curseur.getCount() == 0)
			return null;
		else {
			curseur.moveToFirst();
			return curseurToAnimal(curseur);
		}
	}

	public ArrayList<Animal> getAnimals() {
		ArrayList<Animal> liste = new ArrayList<Animal>();
		Cursor curseur = bd.query(SQLITE_TABLE, null, null, null, null, null,
				"nom, categorie, image, description");
		if (curseur.getCount() == 0)
			return liste;
		else {
			curseur.moveToFirst();
			do {
				liste.add(curseurToAnimal(curseur));
			} while (curseur.moveToFirst());
			curseur.close();
			return liste;
		}
	}

	public Cursor getAnimalsToCursor() {
		Thread t = new Thread(listRun);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Cursor curseur = bd.query(SQLITE_TABLE, null, null, null, null, null,
				"nom, categorie, image, description");
		if (curseur != null)
			curseur.moveToFirst();
		return curseur;
	}

	public Animal curseurToAnimal(Cursor curseur) {
		// TODO Auto-generated method stub
		Animal animal = new Animal();
		animal.setId(curseur.getInt(0));
		animal.setNom(curseur.getString(1));
		animal.setCategorie(curseur.getString(2));
		animal.setDescription(curseur.getString(3));
		return animal;
	}

	public void check(String url) {
		try {
			JSONObject jsonObject = JSONUtil.getJSON(url);

			Iterator<String> it = jsonObject.keys();
			while (it.hasNext()) {
				Animal ani = new Animal();
				JSONObject childs = jsonObject.getJSONObject(it.next());
				int _id = childs.getInt("_id");
				String nom = childs.getString("nom");
				String categorie = childs.getString("categorie");
				String image = childs.getString("image");
				String description = childs.getString("description");

				ani.setId(_id);
				ani.setNom(nom);
				ani.setImage(image);
				ani.setCategorie(categorie);
				ani.setDescription(description);
				if (getAnimal(_id) != null)
					miseAJour(ani);
				else
					ajouter(ani);
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
