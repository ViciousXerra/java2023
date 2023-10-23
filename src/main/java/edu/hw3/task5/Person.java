package edu.hw3.task5;

import org.jetbrains.annotations.NotNull;

class Person {

    private final String firstName;
    private final String lastName;

    Person(@NotNull String fullName) {
        String[] splitted = fullName.split(" ");
        firstName = splitted[0];
        lastName = splitted.length == 2 ? splitted[1] : null;
    }

    @Override
    public String toString() {
        return lastName == null ? firstName : firstName + " " + lastName;
    }

    String getLastName() {
        return lastName;
    }

    boolean isLastNameExist() {
        return lastName != null;
    }

}
