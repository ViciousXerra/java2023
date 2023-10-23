package edu.hw3.task5;

import org.jetbrains.annotations.NotNull;

public final class Task5 {

    private Task5() {

    }

    public static String[] sortContacts(String[] names, @NotNull String order) {
        Contacts contacts = new Contacts();
        for (String name : names) {
            if (!contacts.add(name)) {
                throw new IllegalArgumentException(
                    "Name must match the following pattern.\n${FIRST_NAME} ${LAST_NAME} or\n${FIRST_NAME}");
            }
        }
        switch (order) {
            case "ASC", "DESC" -> contacts.sort(order);
            default -> throw new IllegalArgumentException("Order String value must be \"ASC\" or \"DESC\"");
        }
        return contacts.getNames().toArray(String[]::new);
    }

}
