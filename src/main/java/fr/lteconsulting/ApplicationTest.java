package fr.lteconsulting;

import java.util.List;

import fr.lteconsulting.dao.DisqueDAO;
import fr.lteconsulting.modele.Bibliotheque;
import fr.lteconsulting.modele.Chanson;
import fr.lteconsulting.modele.Disque;

public class ApplicationTest
{
	public static void main( String[] args )
	{
		DisqueDAO dao = new DisqueDAO();
		Bibliotheque b = genererBib();
		

		chercherEtAfficherDisque( dao, "tyhbc" );
		chercherEtAfficherDisque( dao, "ppttdddd" );
		System.out.println("===============================================================================================");
		chercherEtAfficherTousDisques(dao);
		System.out.println("===============================================================================================");		
		Disque disque = b.getDisques().get(0);
		Disque newDisc = insererDisque(dao, disque);
		System.out.println(newDisc);
		System.out.println("===============================================================================================");	
		String idToModify = disque.getCodeBarre();
		String newName = "Les Enfants";
		Disque modifiedDisc = new Disque(idToModify, newName);
		modifierNom(dao, modifiedDisc);
		System.out.println("===============================================================================================");	
		dao.delete("jdpjp");
		
	}
	
	private static Bibliotheque genererBib(){
		Bibliotheque b = new Bibliotheque();

		Disque d = new Disque( "La Lune" );
		d.addChanson( new Chanson( "Titre", 34 ) );
		d.addChanson( new Chanson( "Titre", 34 ) );
		b.ajouterDisque( d );

		d = new Disque( "Le soleil" );
		d.addChanson( new Chanson( "Titre", 34 ) );
		d.addChanson( new Chanson( "Blah", 23 ) );
		d.addChanson( new Chanson( "Titre", 34 ) );
		d.addChanson( new Chanson( "Titre", 34 ) );
		d.addChanson( new Chanson( "Titre", 34 ) );
		b.ajouterDisque( d );
		
		return b;
	}

	private static void chercherEtAfficherDisque( DisqueDAO dao, String id )
	{
		Disque disque = dao.findById( id );
		if( disque != null )
		{
			System.out.println( "Le disque " + id + " a été trouvé :" );
			disque.afficher();
		}
		else
		{
			System.out.println( "Le disque " + id + "n'existe pas" );
		}
	}
	
	private static void chercherEtAfficherTousDisques(DisqueDAO dao){
		
		List<Disque> allDiscs = dao.findAll();
		
		if(allDiscs.size()==0){
			System.out.println("Il n'y a pas de disques dans la BDD");
		}
		
		int numeroDisque;
		
		for(int i = 0 ; i<allDiscs.size();i++){
			numeroDisque = i+1;
			System.out.print("Le disque "+numeroDisque+" est : ");
			allDiscs.get(i).afficher();
		}
		
	}
	
	private static Disque insererDisque(DisqueDAO dao, Disque disque){
		
		Disque newDisc = dao.add(disque);
		return newDisc;
		
	}
	
	private static void modifierNom(DisqueDAO dao, Disque disque){
		dao.update(disque);
	}
}
