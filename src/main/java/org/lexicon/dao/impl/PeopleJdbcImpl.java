package org.lexicon.dao.impl;

import org.lexicon.dao.People;
import org.lexicon.db.DBConnection;
import org.lexicon.model.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class PeopleJdbcImpl implements People {

    private static final String INSERT_SQL =
            "INSERT INTO person (first_name, last_name) VALUES (?, ?)";
    private static final String SELECT_ALL_SQL =
            "SELECT person_id, first_name, last_name FROM person";
    private static final String SELECT_BY_ID_SQL =
            "SELECT person_id, first_name, last_name FROM person WHERE person_id = ?";
    private static final String SELECT_BY_NAME_SQL =
            "SELECT person_id, first_name, last_name FROM person "
                    + "WHERE first_name LIKE ? OR last_name LIKE ?";
    private static final String UPDATE_SQL =
            "UPDATE person SET first_name = ?, last_name = ? WHERE person_id = ?";
    private static final String DELETE_SQL =
            "DELETE FROM person WHERE person_id = ?";

    @Override
    public Person create(Person person) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, person.getFirstName());
            ps.setString(2, person.getLastName());

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Creating person failed, no rows affected.");
            }

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    person.setId(keys.getInt(1));
                } else {
                    throw new SQLException("Creating person failed, no ID obtained.");
                }
            }

            return person;

        } catch (SQLException e) {
            System.err.println("❌ Error creating Person: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Collection<Person> findAll() {
        Collection<Person> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {

            while (rs.next()) {
                list.add(new Person(
                        rs.getInt("person_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name")
                ));
            }

        } catch (SQLException e) {
            System.err.println("❌ Error fetching all Persons: " + e.getMessage());
        }
        return list;
    }

    @Override
    public Person findById(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Person(
                            rs.getInt("person_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error finding Person by ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Collection<Person> findByName(String name) {
        Collection<Person> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_NAME_SQL)) {

            String likeName = "%" + name + "%";
            ps.setString(1, likeName);
            ps.setString(2, likeName);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Person(
                            rs.getInt("person_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name")
                    ));
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error finding Persons by name: " + e.getMessage());
        }
        return list;
    }

    @Override
    public Person update(Person person) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, person.getFirstName());
            ps.setString(2, person.getLastName());
            ps.setInt(3, person.getId());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                return person;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error updating Person: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean deleteById(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error deleting Person: " + e.getMessage());
            return false;
        }
    }
}
