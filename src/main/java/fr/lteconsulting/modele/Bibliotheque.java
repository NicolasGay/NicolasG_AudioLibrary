package fr.lteconsulting.modele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.lteconsulting.dao.DisqueDAO;

public class Bibliotheque
{
	private DisqueDAO disqueDao;

	public Bibliotheque( DisqueDAO disqueDao )
	{
		this.disqueDao = disqueDao;
	}

	public void ajouterDisque( Disque disque )
	{
		disqueDao.addNewDiscInBDD( disque );
	}

	public List<Disque> getDisques()
	{
		return disqueDao.findAllDiscs();
	}

	public Disque rechercherDisqueParCodeBarre( String codeBarre )
	{
		return disqueDao.findDiscById( codeBarre );
	}

	public List<Disque> rechercherDisqueParNom( String recherche )
	{
		return disqueDao.findDiscsByName( recherche );
	}

	public List<Disque> rechercherDisqueParNom( List<String> termes )
	{
		Map<String, Disque> validDisques = new HashMap<>();

		for( String terme : termes )
		{
			for( Disque disque : disqueDao.findDiscsByName( terme ) )
				validDisques.put( disque.getCodeBarre(), disque );
		}

		return new ArrayList<>( validDisques.values() );
	}

	public void afficher()
	{
		List<Disque> disques = disqueDao.findAllDiscs();

		System.out.println( "BIBLIOTHEQUE avec " + disques.size() + " disques" );
		for( Disque disque : disques )
			disque.afficher();
	}
}