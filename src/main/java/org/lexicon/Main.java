package org.lexicon;

import org.lexicon.dao.People;
import org.lexicon.dao.impl.PeopleJdbcImpl;
import org.lexicon.model.Person;

import java.util.Collection;

public class Main {

    public static void main(String[] args) {

        testingjdbcImpl();

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
