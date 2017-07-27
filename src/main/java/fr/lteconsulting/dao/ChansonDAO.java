package fr.lteconsulting.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.lteconsulting.modele.Chanson;
import fr.lteconsulting.modele.Disque;

public class ChansonDAO {
	private Connection connection;

	public ChansonDAO() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque_audio", "root", "root");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Chargement driver failure", e);
		} catch (SQLException e) {
			throw new RuntimeException("Impossible d'établir une connection avec le SGBD", e);
		}
	}
	
	public int getDuree(Chanson chanson){
		String name = chanson.getNom();
		try {
			String sql = "SELECT * FROM `chansons` WHERE nom = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, name);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				String duree = resultSet.getString("duree");
				int dureeInt = Integer.parseInt(duree);
				return dureeInt;
			} else {
				return 0;
			}
		} catch (SQLException e) {
			throw new RuntimeException("Impossible de réaliser l(es) opération(s)", e);
		}
		
	}
	
	public void setDuree(Chanson chanson, int duree){
		
		try {

			String sql = "INSERT INTO `chansons` (`duree`) VALUES (?) WHERE nom=?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, String.valueOf(duree));
			statement.setString(2, chanson.getNom());
			int nbinsertions = statement.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException("Impossible de réaliser l(es) opération(s)", e);
		}
		
	}
	
	public void addSong(Disque disque, Chanson chanson){
		try {

			String sql = "INSERT INTO `chansons` (`nom`,`duree`,`disque_id`) VALUES (?,?,?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(2, String.valueOf(chanson.getDureeEnSecondes()));
			statement.setString(1, chanson.getNom());
			statement.setString(3, disque.getCodeBarre());
			int nbinsertions = statement.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException("Impossible de réaliser l(es) opération(s)", e);
		}
	}
	
	public List<Chanson> findAll() {

		try {
			String sql = "SELECT * FROM `chansons`";
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();

			List<Chanson> allSongs = new ArrayList<>();

			while (resultSet.next()) {
				String nom = resultSet.getString("nom");
				String duree = resultSet.getString("duree");
				int dureeInt = Integer.parseInt(duree);
				Chanson chanson = new Chanson(nom, dureeInt);
				allSongs.add(chanson);
			}
			return allSongs;

		} catch (SQLException e) {
			throw new RuntimeException("Impossible de réaliser l(es) opération(s)", e);
		}

	}

	
}
