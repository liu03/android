package com.example.zoo;

public class Commentaire {
	private int id;
	private String user;
	private int animal;
	private String commentaire;

	public String getUtilisateur() {
		
		return user;
	}

	public int getAnimal() {		
		return animal;
	}

	public int getId() {
		
		return id;
	}
	
	public String getCommentaire(){
		return commentaire;
	}

	public void setId(int id) {
		
		this.id = id;
	}

	public void setUtilisateur(String user) {
		
		this.user = user.toUpperCase();
	}

	public void setAnimal(int animal) {
		this.animal = animal;
	}
	
	public void setCommentaire(String des) {
		
		this.commentaire = des;
	}

}
