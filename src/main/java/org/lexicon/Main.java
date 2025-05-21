package org.lexicon;

import org.lexicon.dao.People;
import org.lexicon.dao.TodoItems;
import org.lexicon.dao.impl.PeopleJdbcImpl;
import org.lexicon.dao.impl.TodoItemsJdbcImpl;
import org.lexicon.model.Person;
import org.lexicon.model.TodoItem;

import java.time.LocalDate;
import java.util.Collection;

public class Main {

    public static void main(String[] args) {
        testPersonDAO();
        testTodoItemDAO();
    }

    public static void testPersonDAO() {
        People peopleDao = new PeopleJdbcImpl();

        System.out.println("\n--- TESTING PeopleJdbcImpl ---");

        // Create
        Person newPerson = new Person("Nikola", "Tesla");
        Person savedPerson = peopleDao.create(newPerson);
        System.out.println(savedPerson != null ? "✅ Created: " + savedPerson : "❌ Failed to create.");

        // Find all
        System.out.println("\n📋 All persons:");
        peopleDao.findAll().forEach(System.out::println);

        // Find by ID
        System.out.println("\n🔍 Found by ID:");
        System.out.println(peopleDao.findById(savedPerson.getId()));

        // Update
        savedPerson.setFirstName("Josip");
        savedPerson.setLastName("Jovanovic");
        Person updated = peopleDao.update(savedPerson);
        System.out.println("\n✏️ Updated: " + updated);

        // Find by name
        System.out.println("\n🔎 Found by name 'Josip':");
        peopleDao.findByName("Josip").forEach(System.out::println);

        // Delete
        boolean deleted = peopleDao.deleteById(savedPerson.getId());
        System.out.println(deleted ? "\n✅ Deleted successfully." : "\n❌ Delete failed.");

        // Confirm final state
        System.out.println("\n📋 People after deletion:");
        peopleDao.findAll().forEach(System.out::println);
    }

    public static void testTodoItemDAO() {
        TodoItems todoDao = new TodoItemsJdbcImpl();
        People peopleDao = new PeopleJdbcImpl();

        System.out.println("\n--- TESTING TodoItemsJdbcImpl ---");

        // Create person
        Person person = new Person("Gentrit", "Hoyt");
        Person savedPerson = peopleDao.create(person);
        int personId = savedPerson.getId();

        // Create and insert TodoItems
        TodoItem t1 = new TodoItem("Finish JDBC Project", "Implement all DAO methods and test in Main.", LocalDate.now().plusDays(3), false, personId);
        TodoItem t2 = new TodoItem("Write blog", "Document what you learned.", LocalDate.now().plusDays(5), false, personId);

        todoDao.create(t1);
        todoDao.create(t2);

        // Find all
        System.out.println("\n📋 All TodoItems:");
        todoDao.findAll().forEach(System.out::println);

        // Find by ID
        System.out.println("\n🔍 Find by ID:");
        TodoItem found = todoDao.findById(t1.getId());
        System.out.println(found != null ? found : "❌ No TodoItem found with that ID.");

        // Find by done status
        System.out.println("\n✅ DONE ITEMS:");
        todoDao.findByDoneStatus(true).forEach(System.out::println);
        System.out.println("\n⏳ NOT DONE ITEMS:");
        todoDao.findByDoneStatus(false).forEach(System.out::println);

        // Find by assignee ID
        System.out.println("\n📋 TodoItems assigned to person ID " + personId + ":");
        todoDao.findByAssignee(personId).forEach(System.out::println);

        // Find by assignee Person
        System.out.println("\n👤 TodoItems assigned to: " + savedPerson.getFirstName());
        todoDao.findByAssignee(savedPerson).forEach(System.out::println);

        // Find unassigned (should be empty unless some are missing assignee_id)
        System.out.println("\n🕳 Unassigned TodoItems:");
        todoDao.findByUnassignedTodoItems().forEach(System.out::println);

        // Update item
        t1.setDone(true);
        t1.setTitle("[DONE] Finish JDBC Project");
        TodoItem updated = todoDao.update(t1);
        System.out.println("\n✏️ Updated TodoItem: " + updated);

        // Delete item
        boolean deleted = todoDao.deleteById(t2.getId());
        System.out.println(deleted ? "\n🗑 Deleted t2 successfully." : "\n❌ Failed to delete t2.");

        // Final list
        System.out.println("\n📋 Final TodoItems:");
        todoDao.findAll().forEach(System.out::println);
    }
}
