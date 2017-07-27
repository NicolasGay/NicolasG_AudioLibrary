package fr.lteconsulting.modele;

import fr.lteconsulting.dao.ChansonDAO;

public class Chanson {
	private String nom;
	private int dureeEnSecondes;
	
	
	ChansonDAO dao = new ChansonDAO();

	public Chanson() {
	}

	public Chanson(String nom, int dureeEnSecondes) {
		this.nom = nom;
		this.dureeEnSecondes = dureeEnSecondes;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public int getDureeEnSecondes() {
		return dureeEnSecondes;

	}

	public void setDureeEnSecondes(int dureeEnSecondes) {
		this.dureeEnSecondes = dureeEnSecondes;
	}

	public void afficher() {
		System.out.println(nom + " (" + dureeEnSecondes + " sec.)");
	}
}
