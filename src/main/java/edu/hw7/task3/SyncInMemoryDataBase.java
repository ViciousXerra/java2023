package edu.hw7.task3;

import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.Nullable;

public class SyncInMemoryDataBase extends AbstractDataBase {

    private static SyncInMemoryDataBase syncInMemoryDataBase;

    private SyncInMemoryDataBase() {

    }

    public static synchronized SyncInMemoryDataBase getDataBaseInstance() {
        if (syncInMemoryDataBase == null) {
            syncInMemoryDataBase = new SyncInMemoryDataBase();
        }
        return syncInMemoryDataBase;
    }

    @Override
    public synchronized void add(Person person) {
        if (!isValid(person)) {
            throw new IllegalArgumentException("Unable to add invalid Person arg.");
        }
        updateData(person);
    }

    @Override
    public synchronized void delete(int id) {
        removeData(id);
    }

    @Override
    public synchronized @Nullable List<Person> findByName(String name) {
        validate(name);
        Set<Person> persons = nameTable.get(name);
        if (persons == null) {
            return List.of();
        } else {
            return persons.stream().toList();
        }
    }

    @Override
    public synchronized @Nullable List<Person> findByAddress(String address) {
        validate(address);
        Set<Person> persons = addressTable.get(address);
        if (persons == null) {
            return List.of();
        } else {
            return persons.stream().toList();
        }
    }

    @Override
    public synchronized @Nullable List<Person> findByPhone(String phoneNum) {
        validate(phoneNum);
        Set<Person> persons = phoneTable.get(phoneNum);
        if (persons == null) {
            return List.of();
        } else {
            return persons.stream().toList();
        }
    }

}
