package org.lexicon.dao;

import org.lexicon.model.Person;
import org.lexicon.model.TodoItem;

import java.util.Collection;

public interface TodoItems {
        TodoItem create(TodoItem todo);
        Collection<TodoItem> findAll();
        TodoItem findById(int id);
        Collection<TodoItem> findByDoneStatus(boolean done);
        Collection<TodoItem> findByAssignee(int personId);
        Collection<TodoItem> findByAssignee(Person person);
        Collection<TodoItem> findByUnassignedTodoItems();
        TodoItem update(TodoItem todo);
        boolean deleteById(int id);
    }
