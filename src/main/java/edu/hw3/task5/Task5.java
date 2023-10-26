package edu.hw3.task5;

import java.util.List;
import org.jetbrains.annotations.NotNull;

public final class Task5 {

    private Task5() {

    }

    public static List<Person> sortContacts(String[] names, @NotNull String order) {
        if (names == null) {
            return List.of();
        }
        switch (order) {
            case "ASC", "DESC" -> {
            }
            default -> throw new IllegalArgumentException("Order String value must be \"ASC\" or \"DESC\"");
        }
        Contacts contacts = new Contacts();
        for (String name : names) {
            if (!contacts.add(name)) {
                throw new IllegalArgumentException(
                    "Name cannot be null or starts with whitespace, and must be separated by one whitespace.");
            }
        }
        contacts.sort(order);
        return contacts.getPersons();
    }

}
