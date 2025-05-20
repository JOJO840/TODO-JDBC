package org.lexicon.dao.impl;

import org.lexicon.dao.TodoItems;
import org.lexicon.db.DBConnection;
import org.lexicon.model.Person;
import org.lexicon.model.TodoItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TodoItemsJdbcImpl implements TodoItems {

    private static final String INSERT_SQL = "INSERT INTO todo_item (title, description, deadline, done, assignee_id) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_SQL = "SELECT todo_id, title, description, deadline, done, assignee_id FROM todo_item";
    private static final String SELECT_BY_ID_SQL = "SELECT todo_id, title, description, deadline, done, assignee_id FROM todo_item WHERE todo_id = ?";

    @Override
    public TodoItem create(TodoItem todo) {
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)){

            ps.setString(1, todo.getTitle());
            ps.setString(2,todo.getDescription());
            ps.setDate(3, Date.valueOf(todo.getDeadline()));
            ps.setBoolean(4, todo.isDone());
            ps.setInt(5, todo.getAssigneeId());

            int affected = ps.executeUpdate();
            if (affected == 0){
                System.err.println("❌ Insert failed!");
                return null;
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()){
                    todo.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error in create(): " + e.getMessage());
            return null;
        }


        return todo;
    }

    @Override
    public Collection<TodoItem> findAll() {
        List<TodoItem> todos = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {

            while (rs.next()) {
                TodoItem todo = new TodoItem(
                        rs.getInt("todo_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getDate("deadline").toLocalDate(), // Convert SQL Date to LocalDate
                        rs.getBoolean("done"),
                        rs.getInt("assignee_id")
                );
                todos.add(todo);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error in findAll(): " + e.getMessage());
        }

        return todos;
    }

    @Override
    public TodoItem findById(int id) {
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_SQL)){

            ps.setInt(1,id);

            try(ResultSet rs = ps.executeQuery()){
                if (rs.next()){
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

        } catch (SQLException e){
            System.err.println("❌ Error in findById(): " + e.getMessage());
        }


        return null;
    }

    @Override
    public Collection<TodoItem> findByDoneStatus(boolean done) {
        return List.of();
    }

    @Override
    public Collection<TodoItem> findByAssignee(int personId) {
        return List.of();
    }

    @Override
    public Collection<TodoItem> findByAssignee(Person person) {
        return List.of();
    }

    @Override
    public Collection<TodoItem> findByUnassignedTodoItems() {
        return List.of();
    }

    @Override
    public TodoItem update(TodoItem todo) {
        return null;
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }
}
