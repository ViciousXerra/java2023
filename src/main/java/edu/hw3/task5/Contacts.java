package edu.hw3.task5;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class Contacts {

    private final static Comparator<Person> COMPARING_BY_LASTNAME = Comparator.comparing(Person::getLastName);

    private final static Comparator<Person> COMPARING_BY_FIRSTNAME = Comparator.comparing(Person::toString);

    private final List<Person> persons;

    public Contacts() {
        persons = new ArrayList<>();
    }

    public List<Person> getPersons() {
        return persons;
    }

    boolean add(String name) {
        if (validate(name)) {
            return persons.add(new Person(name));
        }
        return false;
    }

    void sort(@NotNull SortingOrder order) {
        switch (order) {
            case ASC -> {
                if (isAllPersonsHaveLastName()) {
                    persons.sort(COMPARING_BY_LASTNAME);
                } else {
                    persons.sort(COMPARING_BY_FIRSTNAME);
                }
            }
            case DESC -> {
                if (isAllPersonsHaveLastName()) {
                    persons.sort(COMPARING_BY_LASTNAME.reversed());
                } else {
                    persons.sort(COMPARING_BY_FIRSTNAME.reversed());
                }
            }
            default -> {
            }
        }
    }

    private boolean isAllPersonsHaveLastName() {
        return persons.size() == persons.stream().filter(Person::isLastNameExist).count();
    }

    private boolean validate(String name) {
        if (name == null) {
            return false;
        }
        String[] splitted;
        splitted = name.split(" ");
        return switch (splitted.length) {
            case 1 -> !splitted[0].isEmpty();
            case 2 -> !splitted[0].isEmpty() && !splitted[1].isEmpty();
            default -> false;
        };
    }

}
