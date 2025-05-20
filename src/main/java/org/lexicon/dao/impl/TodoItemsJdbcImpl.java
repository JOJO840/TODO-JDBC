package org.lexicon.dao.impl;

import org.lexicon.dao.TodoItems;
import org.lexicon.model.Person;
import org.lexicon.model.TodoItem;

import java.util.Collection;
import java.util.List;

public class TodoItemsJdbcImpl implements TodoItems {
    @Override
    public TodoItem create(TodoItem todo) {
        return null;
    }

    @Override
    public Collection<TodoItem> findAll() {
        return List.of();
    }

    @Override
    public TodoItem findById(int id) {
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
