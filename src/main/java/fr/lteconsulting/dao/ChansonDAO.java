package fr.lteconsulting.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

import fr.lteconsulting.modele.Chanson;

public class ChansonDAO {
	private Connection connection;

	public ChansonDAO(MySQLDatabaseConnection connection) {
		this.connection = connection.getConnection();
	}

	public List<Chanson> findAllSongs() {

		List<Chanson> chansons = new ArrayList<>();
		try {
			String sql = "SELECT * FROM `chansons`";
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();

			Chanson song = new Chanson();

			while (resultSet.next())

				chansons.add(songFactory(resultSet));

			return chansons;
		} catch (SQLException e) {
			throw new RuntimeException("Impossible de réaliser l(es) opération(s)", e);
		}
	}

	public Chanson findSongById(String id) {
		try {
			String sql = "SELECT * FROM `chansons` WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				return songFactory(resultSet);
			else
				return null;
		} catch (SQLException e) {
			throw new RuntimeException("Impossible de réaliser l(es) opération(s)", e);
		}
	}

	public List<Chanson> findSongByDiscId(String disqueId) {
		try {
			List<Chanson> chansons = new ArrayList<>();

			String sql = "SELECT * FROM `chansons` WHERE `disque_id` = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, disqueId);
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next())
				chansons.add(songFactory(resultSet));

			return chansons;
		} catch (SQLException e) {
			throw new RuntimeException("Impossible de réaliser l(es) opération(s)", e);
		}
	}

	private Chanson songFactory(ResultSet resultSet) throws SQLException {

		Chanson chanson = new Chanson();

		int id = resultSet.getInt("id");
		String disqueId = resultSet.getString("disque_id");
		String nom = resultSet.getString("nom");
		int duree = resultSet.getInt("duree");

		chanson.setId(id);
		chanson.setNom(nom);
		chanson.setDureeEnSecondes(duree);
		chanson.setDisqueId(disqueId);

		return chanson;
	}

	public Chanson addSongInBDD(Chanson chanson) {
		if (chanson.getDisqueId() == null)
			throw new RuntimeException("Cette chanson ne possède de pas de disque_id");

		try {
			String sql = "INSERT INTO chansons (`disque_id`, `nom`, `duree`) VALUES (?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, chanson.getDisqueId());
			statement.setString(2, chanson.getNom());
			statement.setInt(3, chanson.getDureeEnSecondes());

			int nbAjouts = statement.executeUpdate();

			ResultSet createdIds = statement.getGeneratedKeys();
			if (createdIds.next()) {
				chanson.setId(createdIds.getInt(1));
				return chanson;
			}

			throw new RuntimeException("Aucune chanson ajoutée");
		} catch (SQLException e) {
			throw new RuntimeException("Impossible d'ajouter la chanson", e);
		}
	}

	public void update(Chanson chanson) {
		if (chanson.getDisqueId() == null)
			throw new RuntimeException("Impossible de modifier une chanson sans connaître son disque !");

		try {
			String sql = "UPDATE chansons SET `disque_id` = ?, `nom` = ?, `duree` = ? WHERE id = ?";

			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, chanson.getDisqueId());
			statement.setString(2, chanson.getNom());
			statement.setInt(3, chanson.getDureeEnSecondes());
			statement.setInt(4, chanson.getId());

			statement.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Impossible de mettre à jour la chanson", e);
		}
	}

	public void deleteSongById(int id) {
		try {
			String sql = "DELETE FROM chansons WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Impossible de retirer la chanson", e);
		}
	}

	public void deleteSongsByDiscId(String disqueId) {
		try {
			String sqlQuery = "DELETE FROM chansons WHERE disque_id = ?";
			PreparedStatement statement = connection.prepareStatement(sqlQuery);
			statement.setString(1, disqueId);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Impossible de retirer la chanson", e);
		}
	}

}