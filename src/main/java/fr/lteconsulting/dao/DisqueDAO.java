package fr.lteconsulting.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.lteconsulting.modele.Disque;

public class DisqueDAO {
	private Connection connection;

	public DisqueDAO() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque_audio", "root", "root");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Chargement driver failure", e);
		} catch (SQLException e) {
			throw new RuntimeException("Impossible d'établir une connection avec le SGBD", e);
		}
	}

	public Disque findById(String id) {
		try {
			String sql = "SELECT * FROM `disques` WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				String nom = resultSet.getString("nom");
				Disque disque = new Disque(id, nom);
				return disque;
			} else {
				return null;
			}
		} catch (SQLException e) {
			throw new RuntimeException("Impossible de réaliser l(es) opération(s)", e);
		}
	}
	
	public Disque findByName(String name) {
		try {
			String sql = "SELECT * FROM `disques` WHERE nom = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, name);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				String id = resultSet.getString("id");
				Disque disque = new Disque(id, name);
				return disque;
			} else {
				return null;
			}
		} catch (SQLException e) {
			throw new RuntimeException("Impossible de réaliser l(es) opération(s)", e);
		}
	}

	public List<Disque> findAll() {

		try {
			String sql = "SELECT * FROM `disques`";
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();

			List<Disque> allDiscs = new ArrayList<>();

			while (resultSet.next()) {
				String nom = resultSet.getString("nom");
				String id = resultSet.getString("id");
				Disque disque = new Disque(id, nom);
				allDiscs.add(disque);
			}
			return allDiscs;

		} catch (SQLException e) {
			throw new RuntimeException("Impossible de réaliser l(es) opération(s)", e);
		}

	}

	public Disque add(Disque disque) {

		String id = disque.getCodeBarre();
		String name = disque.getNom();

		try {

			String sql = "INSERT INTO `disques` (`id`,`nom`) VALUES (?,?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, id);
			statement.setString(2, name);
			int nbinsertions = statement.executeUpdate();
					
			Disque newDisc = findById(id);
			return newDisc;

		} catch (SQLException e) {
			throw new RuntimeException("Impossible de réaliser l(es) opération(s)", e);
		}
	}

	public void update(Disque disque) {
		
		String id = disque.getCodeBarre();
		String name = disque.getNom();

		try {

			String sql = "UPDATE `disques` SET `nom` = ? WHERE `id` = ?";		
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, name);
			statement.setString(2, id);
			int nbupdate = statement.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException("Impossible de réaliser l(es) opération(s)", e);
		}
		
	}

	public void delete(String id) {
		
		try {

			String sql = "DELETE FROM `disques` WHERE `id` = ?";		
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, id);
			int nbupdate = statement.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException("Impossible de réaliser l(es) opération(s)", e);
		}
	}
}
