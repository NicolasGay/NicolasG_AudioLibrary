package fr.lteconsulting.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import fr.lteconsulting.modele.Chanson;
import fr.lteconsulting.modele.Disque;

public class DisqueDAO {
	private Connection connection;
	private ChansonDAO chansonDAO;

	public DisqueDAO(MySQLDatabaseConnection connection, ChansonDAO chansonDAO) {
		this.connection = connection.getConnection();
		this.chansonDAO = chansonDAO;
	}

	
	public List<Disque> findAllDiscs() {
		try {
			List<Disque> disques = new ArrayList<>();

			String sql = "SELECT * FROM `disques`";
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Disque disque = discFactory(resultSet);
				disques.add(disque);
			}

			return disques;
		} catch (SQLException e) {
			throw new RuntimeException("Impossible de réaliser l(es) opération(s)", e);
		}
	}
	
	public Disque findDiscById(String id) {
		try {
			String sql = "SELECT * FROM `disques` WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				return discFactory(resultSet);
			else
				return null;
		} catch (SQLException e) {
			throw new RuntimeException("Impossible de réaliser l(es) opération(s)", e);
		}
	}

	public List<Disque> findDiscsByName(String name) {
		name = name.toLowerCase();

		try {
			List<Disque> disques = new ArrayList<>();

			String sql = "SELECT * FROM `disques` WHERE LOWER(`nom`) LIKE ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, "%" + name + "%");
			ResultSet resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				Disque disque = discFactory(resultSet);
				disques.add(disque);
			}

			return disques;
		} catch (SQLException e) {
			throw new RuntimeException("Impossible de réaliser l(es) opération(s)", e);
		}
	}

	public Disque addNewDiscInBDD(Disque disque) {
		try {
			
			String id = disque.getCodeBarre();
			
			if (id == null){
				id = UUID.randomUUID().toString();
				disque.setCodeBarre(id);
			}

			String sql = "INSERT INTO disques (`id`, `nom`) VALUES (?, ?)";

			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, id);
			statement.setString(2, disque.getNom());

			int nbEnregistrementInseres = statement.executeUpdate();
			if (nbEnregistrementInseres == 0)
				throw new RuntimeException("Aucun disque inséré");

			for (Chanson chanson : disque.getChansons()) {
				chanson.setDisqueId(id);
				chansonDAO.addSongInBDD(chanson);
			}

			return disque;
		} catch (SQLException e) {
			throw new RuntimeException("Impossible d'ajouter le disque", e);
		}
	}

	public void updateDiscInBDD(Disque disque) {
		if (disque.getCodeBarre() == null) {
			addNewDiscInBDD(disque);
			System.out.println("Ce disque n'existait pas encore dans la BDD. Il y a été ajouté ");
			return;
		}

		try {
			String sql = "UPDATE disques SET `nom` = ? WHERE id = ?";

			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, disque.getNom());
			statement.setString(2, disque.getCodeBarre());

			statement.executeUpdate();
			
			List<Chanson> allSongsInDisc = disque.getChansons();
			
			for (Chanson chanson : allSongsInDisc) {
				if (chanson.getId() <= 0)
					// l'Id est négatif, c'est donc une nouvelle chanson, il faut l'ajouter à la BDD
					chansonDAO.addSongInBDD(chanson);
				else
					// l'Id est positif, la chanson est donc déjà en BDD, il faut la mettre à jour
					chansonDAO.update(chanson);
			}
			
			List<Chanson> allSongsInBDD = chansonDAO.findSongByDiscId(disque.getCodeBarre());
			
			
			for (Chanson chansonInBDD : allSongsInBDD) {
				if (isInBDDButNotInDisc(allSongsInDisc, chansonInBDD.getId()))
					chansonDAO.deleteSongById(chansonInBDD.getId());
			}
		} catch (SQLException e) {
			throw new RuntimeException("Impossible de mettre à jour le disque", e);
		}
	}

	private boolean isInBDDButNotInDisc(List<Chanson> allSongsInDisc, int idOfChansonInBdd) {
		for (Chanson chansonInDisc : allSongsInDisc) {
			if (chansonInDisc.getId() == idOfChansonInBdd)
				return false;
		}

		return true;
	}

	public void deleteDiscInBDD(String id) {
		try {
			// effacer toutes les chansons du disque puisqu'on efface le disque
			chansonDAO.deleteSongsByDiscId(id);

			String sql = "DELETE FROM disques WHERE id = ?";

			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, id);

			statement.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Impossible de retirer le(s) disque(s)", e);
		}
	}

	private Disque discFactory(ResultSet resultSet) throws SQLException {
		String id = resultSet.getString("id");
		String nom = resultSet.getString("nom");

		Disque disque = new Disque();
		disque.setCodeBarre(id);
		disque.setNom(nom);

		List<Chanson> chansons = chansonDAO.findSongByDiscId(id);
		for (Chanson chanson : chansons)
			disque.addSongInDisc(chanson);

		return disque;
	}
}