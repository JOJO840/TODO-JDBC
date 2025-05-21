package org.lexicon.dao.impl;

import org.lexicon.dao.TodoItems;
import org.lexicon.db.DBConnection;
import org.lexicon.model.Person;
import org.lexicon.model.TodoItem;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TodoItemsJdbcImpl implements TodoItems {

    private static final String INSERT_SQL =
            "INSERT INTO todo_item (title, description, deadline, done, assignee_id) VALUES (?, ?, ?, ?, ?)";

    private static final String SELECT_ALL_SQL =
            "SELECT todo_id, title, description, deadline, done, assignee_id FROM todo_item";

    private static final String SELECT_BY_ID_SQL =
            "SELECT todo_id, title, description, deadline, done, assignee_id FROM todo_item WHERE todo_id = ?";

    private static final String SELECT_BY_DONE_STATUS_SQL =
            "SELECT todo_id, title, description, deadline, done, assignee_id FROM todo_item WHERE done = ?";

    private static final String SELECT_BY_ASSIGNEE_SQL =
            "SELECT todo_id, title, description, deadline, done, assignee_id FROM todo_item WHERE assignee_id = ?";

    private static final String SELECT_UNASSIGNED_SQL =
            "SELECT todo_id, title, description, deadline, done, assignee_id FROM todo_item WHERE assignee_id IS NULL";

    private static final String UPDATE_SQL =
            "UPDATE todo_item SET title = ?, description = ?, deadline = ?, done = ?, assignee_id = ? WHERE todo_id = ?";

    private static final String DELETE_SQL =
            "DELETE FROM todo_item WHERE todo_id = ?";

    @Override
    public TodoItem create(TodoItem todo) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, todo.getTitle());
            ps.setString(2, todo.getDescription());
            ps.setDate(3, Date.valueOf(todo.getDeadline()));
            ps.setBoolean(4, todo.isDone());
            ps.setInt(5, todo.getAssigneeId());

            int affected = ps.executeUpdate();
            if (affected == 0) {
                System.err.println("❌ Insert failed.");
                return null;
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    todo.setId(rs.getInt(1));
                }
            }

            return todo;

        } catch (SQLException e) {
            System.err.println("❌ Error in create(): " + e.getMessage());
            return null;
        }
    }

    @Override
    public Collection<TodoItem> findAll() {
        List<TodoItem> todos = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {

            while (rs.next()) {
                todos.add(new TodoItem(
                        rs.getInt("todo_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getDate("deadline").toLocalDate(),
                        rs.getBoolean("done"),
                        rs.getInt("assignee_id")
                ));
            }

        } catch (SQLException e) {
            System.err.println("❌ Error in findAll(): " + e.getMessage());
        }

        return todos;
    }

    @Override
    public TodoItem findById(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new TodoItem(
                            rs.getInt("todo_id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getDate("deadline").toLocalDate(),
                            rs.getBoolean("done"),
                            rs.getInt("assignee_id")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error in findById(): " + e.getMessage());
        }

        return null;
    }

    @Override
    public Collection<TodoItem> findByDoneStatus(boolean done) {
        List<TodoItem> result = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_DONE_STATUS_SQL)) {

            ps.setBoolean(1, done);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(new TodoItem(
                            rs.getInt("todo_id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getDate("deadline").toLocalDate(),
                            rs.getBoolean("done"),
                            rs.getInt("assignee_id")
                    ));
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error in findByDoneStatus(): " + e.getMessage());
        }

        return result;
    }

    @Override
    public Collection<TodoItem> findByAssignee(int personId) {
        List<TodoItem> result = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ASSIGNEE_SQL)) {

            ps.setInt(1, personId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(new TodoItem(
                            rs.getInt("todo_id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getDate("deadline").toLocalDate(),
                            rs.getBoolean("done"),
                            rs.getInt("assignee_id")
                    ));
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error in findByAssignee(int): " + e.getMessage());
        }

        return result;
    }

    @Override
    public Collection<TodoItem> findByAssignee(Person person) {
        if (person == null) return List.of();
        return findByAssignee(person.getId());
    }

    @Override
    public Collection<TodoItem> findByUnassignedTodoItems() {
        List<TodoItem> result = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_UNASSIGNED_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(new TodoItem(
                        rs.getInt("todo_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getDate("deadline").toLocalDate(),
                        rs.getBoolean("done"),
                        rs.getInt("assignee_id")
                ));
            }

        } catch (SQLException e) {
            System.err.println("❌ Error in findByUnassignedTodoItems(): " + e.getMessage());
        }

        return result;
    }

    @Override
    public TodoItem update(TodoItem todo) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, todo.getTitle());
            ps.setString(2, todo.getDescription());
            ps.setDate(3, Date.valueOf(todo.getDeadline()));
            ps.setBoolean(4, todo.isDone());
            ps.setInt(5, todo.getAssigneeId());
            ps.setInt(6, todo.getId());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                return todo;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error in update(): " + e.getMessage());
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
            System.err.println("❌ Error in deleteById(): " + e.getMessage());
            return false;
        }
    }
}
