package fr.lteconsulting.modele;

import java.util.UUID;

import fr.lteconsulting.dao.ChansonDAO;

public class Disque
{
	private String codeBarre;
	private String nom;
	
	ChansonDAO dao=new ChansonDAO();
	//private List<Chanson> chansons = new ArrayList<>();

	public Disque( String nom )
	{
		this( UUID.randomUUID().toString(), nom );
	}

	public Disque( String codeBarre, String nom )
	{
		this.codeBarre = codeBarre;
		this.nom = nom;
	}

	public String getCodeBarre()
	{
		return codeBarre;
	}

	public void setCodeBarre( String codeBarre )
	{
		this.codeBarre = codeBarre;
	}

	public String getNom()
	{
		return nom;
	}

	public void setNom( String nom )
	{
		this.nom = nom;
	}

	public void addChanson( Chanson chanson )
	{
		//chansons.add( chanson );
		dao.addSong(this, chanson);
	}

	public int getDuree()
	{
		int duree = 0;

		for( Chanson chanson : /*chansons*/dao.findAll() )
			duree += chanson.getDureeEnSecondes();

		return duree;
	}

	public void afficher()
	{
		afficher( true );
	}

	public void afficher( boolean avecTitres )
	{
		System.out.println( "DISQUE [" + codeBarre + "] " + nom + ", durée: " + getDuree() );
		if( avecTitres )
		{
			for( Chanson chanson : /*chansons*/dao.findAll() )
				chanson.afficher();
		}
	}
}
