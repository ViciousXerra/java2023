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
    public int hashCode() {
        return Objects.hashCode(firstName);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Person person)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        boolean isSameHash = this.hashCode() == person.hashCode();
        if (this.isLastNameExist() && person.isLastNameExist()) {
            return isSameHash && this.lastName.equals(person.lastName) && this.firstName.equals(person.firstName);
        } else {
            return isSameHash && this.firstName.equals(person.firstName);
        }
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
