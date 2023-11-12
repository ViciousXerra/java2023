package edu.hw3.task5;

import java.util.List;

public final class Task5 {

    private final static String EXPLICIT_ENUM_VALUE_MESSAGE = "Sorting order value must be \"ASC\" or \"DESC\"";
    private final static String EXPLICIT_NAME_FORMAT_MESSAGE =
        "Name cannot be null or starts with whitespace, and must be separated by one whitespace.";

    private Task5() {

    }

    public static List<Person> sortContacts(String[] names, SortingOrder order) {
        if (names == null) {
            return List.of();
        }
        if (order == null) {
            throw new IllegalArgumentException(EXPLICIT_ENUM_VALUE_MESSAGE);
        }
        Contacts contacts = new Contacts();
        for (String name : names) {
            if (!contacts.add(name)) {
                throw new IllegalArgumentException(EXPLICIT_NAME_FORMAT_MESSAGE);
            }
        }
        contacts.sort(order);
        return contacts.getPersons();
    }

}
