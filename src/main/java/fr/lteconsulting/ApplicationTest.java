package fr.lteconsulting;

import fr.lteconsulting.dao.ChansonDAO;
import fr.lteconsulting.dao.DisqueDAO;
import fr.lteconsulting.dao.MySQLDatabaseConnection;
import fr.lteconsulting.modele.Disque;

public class ApplicationTest {
	public static void main(String[] args) {
		MySQLDatabaseConnection databaseConnection = new MySQLDatabaseConnection();

		ChansonDAO chansonDao = new ChansonDAO(databaseConnection);
		DisqueDAO disqueDao = new DisqueDAO(databaseConnection, chansonDao);

		System.out.println("TEST : RECHERCHE DE DISQUE, LE PREMIER EXISTE, LE SECOND NON");
		chercherEtAfficherDisque(disqueDao, "tyhbc");
		chercherEtAfficherDisque(disqueDao, "dontexist");
		System.out.println("============================================================================");

		System.out.println("TEST :AJOUT DE 3 DISQUES");
		GenerateurDisque generateur = new GenerateurDisque();

		for (int i = 0; i < 3; i++) {
			Disque disque = generateur.genererDisque();
			System.out.print("un disque aléatoire a été généré : ");
			disque.afficher(true);
			System.out.println("Le système actualise la BDD avec ce disque...");
			disqueDao.updateDiscInBDD(disque);
			System.out.println("------------------------------------------------------------------------");
		}
		System.out.println("============================================================================");
		System.out.println("La BDD est à jour");
		System.out.println("Elle contient les disques suivants");

		for (Disque disque : disqueDao.findAllDiscs())
			disque.afficher(true);
		System.out.println("============================================================================");

	}

	private static void chercherEtAfficherDisque(DisqueDAO dao, String id) {
		Disque disque = dao.findDiscById(id);
		if (disque != null) {
			System.out.println("Le disque possédant l'" + id + " a été trouvé :");
			disque.afficher();
		} else {
			System.out.println("Le disque possédant l'" + id + " n'existe pas");
		}
	}

}
