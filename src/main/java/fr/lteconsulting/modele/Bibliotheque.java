package fr.lteconsulting.modele;

import java.util.ArrayList;
import java.util.List;

import fr.lteconsulting.dao.DisqueDAO;

public class Bibliotheque
{
	//private Map<String, Disque> disques = new HashMap<>();
	DisqueDAO dao = new DisqueDAO();

	public void ajouterDisque( Disque disque )
	{
		//disques.put( disque.getCodeBarre(), disque );
		dao.add(disque);
	}

	public List<Disque> getDisques()
	{
		//return new ArrayList<>( disques.values() );
		return dao.findAll();
	}

	public Disque rechercherDisqueParCodeBarre( String codeBarre )
	{
		//return disques.get( codeBarre );
		return dao.findById(codeBarre);
	}

	public List<Disque> rechercherDisqueParNom( String recherche )
	{
		recherche = recherche.toLowerCase();
		List<Disque> resultat = new ArrayList<>();

		for( Disque disque : this.getDisques() )
		{
			if( disque.getNom().toLowerCase().contains( recherche ) )
				resultat.add( disque );
		}

		return resultat;
	}

	public List<Disque> rechercherDisqueParNom( List<String> termes )
	{
		List<Disque> resultat = new ArrayList<>();

		for( Disque disque : /*disques.values()*/ this.getDisques() )
		{
			boolean estValide = true;
			for( String terme : termes )
			{
				if( !disque.getNom().toLowerCase().contains( terme.toLowerCase() ) )
				{
					estValide = false;
					break;
				}
			}

			if( estValide )
				resultat.add( disque );
		}

		return resultat;
	}

	public void afficher()
	{
		System.out.println( "BIBLIOTHEQUE avec " + /*disques.size()*/this.getDisques().size() + " disques" );
		for( Disque disque : /*disques.values()*/this.getDisques() )
			disque.afficher();
	}
}
