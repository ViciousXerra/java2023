package edu.hw7.task3;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

abstract class AbstractDataBase implements PersonDataBase {

    private final static String NULL_KEY_MESSAGE = "Key must not to be null.";

    protected final Map<String, Set<Person>> nameTable;
    protected final Map<String, Set<Person>> addressTable;
    protected final Map<String, Set<Person>> phoneTable;

    protected AbstractDataBase() {
        nameTable = new HashMap<>();
        addressTable = new HashMap<>();
        phoneTable = new HashMap<>();
    }

    protected final void updateData(Person person) {
        updateNameTable(person, person.name());
        updateAddressTable(person, person.address());
        updatePhoneTable(person, person.phoneNum());
    }

    protected final void removeData(int id) {
        nameTable.values().forEach(set -> set.removeIf(person -> person.id() == id));
        addressTable.values().forEach(set -> set.removeIf(person -> person.id() == id));
        phoneTable.values().forEach(set -> set.removeIf(person -> person.id() == id));
    }

    protected final void updatePhoneTable(Person person, String phone) {
        if (phoneTable.containsKey(phone)) {
            phoneTable.get(phone).add(person);
        } else {
            phoneTable.put(
                phone,
                new HashSet<>() {
                    {
                        add(person);
                    }
                }
            );
        }
    }

    protected final void updateAddressTable(Person person, String address) {
        if (addressTable.containsKey(address)) {
            addressTable.get(address).add(person);
        } else {
            addressTable.put(
                address,
                new HashSet<>() {
                    {
                        add(person);
                    }
                }
            );
        }
    }

    protected final void updateNameTable(Person person, String name) {
        if (nameTable.containsKey(name)) {
            nameTable.get(name).add(person);
        } else {
            nameTable.put(
                name,
                new HashSet<>() {
                    {
                        add(person);
                    }
                }
            );
        }
    }

    protected static void validate(String key) {
        if (key == null) {
            throw new IllegalArgumentException(NULL_KEY_MESSAGE);
        }
    }

    protected static boolean isValid(Person p) {
        if (p == null) {
            return false;
        }
        return p.id() > 0 && p.name() != null && p.address() != null && p.phoneNum() != null;
    }

}
