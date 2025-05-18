package org.lexicon;

import org.lexicon.db.DBConnection;
import org.lexicon.model.Person;
import org.w3c.dom.ls.LSOutput;

public class Main {

    public static void main(String[] args) {
        Person  p1 = new Person("d", "df");
        System.out.println(p1);

        DBConnection.getConnection();

    }

}
