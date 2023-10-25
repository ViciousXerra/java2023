package edu.hw3.task5;

import org.jetbrains.annotations.NotNull;

public final class Task5 {

    private Task5() {

    }

    public static String[] sortContacts(String[] names, @NotNull String order) {
        if (names == null) {
            return new String[0];
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
        return contacts.getNames().toArray(String[]::new);
    }

}
