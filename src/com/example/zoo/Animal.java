package com.example.zoo;

public class Animal {
	private int id;
	private String nom;
	private String categorie;
	private String image;
	private String description;
	private int favori;

	public String getNom() {
		// TODO Auto-generated method stub
		return nom;
	}

	public String getCategorie() {
		// TODO Auto-generated method stub
		return categorie;
	}

	public int getId() {
		// TODO Auto-generated method stub
		return id;
	}
	
	public String getDescription(){
		return description;
	}

	public String getImage(){
		return image;
	}
	
	public int getFavori(){
		return favori;
	}
	
	
	public void setId(int id) {
		// TODO Auto-generated method stub
		this.id = id;
	}

	public void setNom(String nom) {
		// TODO Auto-generated method stub
		this.nom = nom.toUpperCase();
	}

	public void setCategorie(String original) {
		// TODO Auto-generated method stub
		StringBuilder cat = new StringBuilder(original.toLowerCase());
		char premier = Character.toUpperCase(original.charAt(0));
		cat.setCharAt(0, premier);
		this.categorie = cat.toString();
	}
	
	public void setDescription(String des) {
		// TODO Auto-generated method stub
		this.description = des;
	}

	public void setImage(String image) {
		// TODO Auto-generated method stub
		this.image = image;
	}
	
	public void setFavori(int favori) {
		// TODO Auto-generated method stub
		this.favori = favori;
	}

}
