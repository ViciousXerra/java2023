package edu.hw3.task5;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public class Person {

    private final String firstName;
    private final String lastName;

    public Person(@NotNull String fullName) {
        String[] splitted = fullName.split(" ");
        firstName = splitted[0];
        lastName = splitted.length == 2 ? splitted[1] : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person person = (Person) o;
        return this.toString().equals(person.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName);
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
