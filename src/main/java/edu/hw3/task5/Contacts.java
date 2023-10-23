package edu.hw3.task5;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Contacts {

    private final static Comparator<Person> COMPARING_BY_LASTNAME = Comparator.comparing(Person::getLastName);

    private final static Comparator<Person> COMPARING_BY_FIRSTNAME = Comparator.comparing(Person::toString);

    private final List<Person> persons;

    {
        persons = new ArrayList<>();
    }

    boolean add(@NotNull String name) {
        if (validate(name)) {
            return persons.add(new Person(name));
        }
        return false;
    }

    void sort(@NotNull String order) {
        if (isAllPersonsHaveLastName() && order.equals("ASC")) {
            persons.sort(COMPARING_BY_LASTNAME);
            return;
        }
        if (isAllPersonsHaveLastName() && order.equals("DESC")) {
            persons.sort(COMPARING_BY_LASTNAME.reversed());
            return;
        }
        if (order.equals("ASC")) {
            persons.sort(COMPARING_BY_FIRSTNAME);
        } else {
            persons.sort(COMPARING_BY_FIRSTNAME.reversed());
        }
    }

    List<String> getNames() {
        return persons.stream().map(Person::toString).collect(Collectors.toList());
    }

    private boolean isAllPersonsHaveLastName() {
        return persons.size() == persons.stream().filter(Person::isLastNameExist).count();
    }

    private boolean validate(@NotNull String name) {
        String[] splitted;
        splitted = name.split(" ");
        return switch (splitted.length) {
            case 1 -> !splitted[0].isEmpty();
            case 2 -> !splitted[0].isEmpty() && !splitted[1].isEmpty();
            default -> false;
        };
    }

}
