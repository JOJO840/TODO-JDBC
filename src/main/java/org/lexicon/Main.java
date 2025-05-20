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

//        testingjdbcImpl();

        TodoItems todoDao = new TodoItemsJdbcImpl();
        TodoItem t1 = new TodoItem( "Finish JDBC Project",
                "Implement all DAO methods and test in Main.",
                LocalDate.now().plusDays(3),
                false,1);

//        TodoItem savedTodo = todoDao.create(t1);

//        // Step 4: Print saved item
//        System.out.println(savedTodo != null
//                ? "✅ TodoItem created: " + savedTodo
//                : "❌ Failed to create TodoItem");
        
        Collection<TodoItem> todoDaoAll = todoDao.findAll();
        todoDaoAll.forEach(todoItem -> System.out.println("todoItem = " + todoItem));

        TodoItem found =  todoDao.findById(1);
        System.out.println(found != null ? found : "❌ No TodoItem found with that ID.");




    }

    public static void testingjdbcImpl(){
        People peopleDao = new PeopleJdbcImpl();

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
}
