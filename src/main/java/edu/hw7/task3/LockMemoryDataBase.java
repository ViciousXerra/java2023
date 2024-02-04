package edu.hw7.task3;

import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.jetbrains.annotations.Nullable;

public class LockMemoryDataBase extends AbstractDataBase {

    private static LockMemoryDataBase lockMemoryDataBase;

    private final ReadWriteLock rwLock;
    private final Lock writeLock;
    private final Lock readLock;

    private LockMemoryDataBase() {
        rwLock = new ReentrantReadWriteLock();
        writeLock = rwLock.writeLock();
        readLock = rwLock.readLock();
    }

    public static synchronized LockMemoryDataBase getDataBaseInstance() {
        if (lockMemoryDataBase == null) {
            lockMemoryDataBase = new LockMemoryDataBase();
        }
        return lockMemoryDataBase;
    }

    @Override
    public void add(Person person) {
        if (!isValid(person)) {
            throw new IllegalArgumentException("Unable to add invalid Person arg.");
        }
        writeLock.lock();
        try {
            updateData(person);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void delete(int id) {
        writeLock.lock();
        try {
            removeData(id);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public @Nullable List<Person> findByName(String name) {
        validate(name);
        readLock.lock();
        try {
            Set<Person> persons = nameTable.get(name);
            if (persons == null) {
                return List.of();
            } else {
                return persons.stream().toList();
            }
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public @Nullable List<Person> findByAddress(String address) {
        validate(address);
        readLock.lock();
        try {
            Set<Person> persons = addressTable.get(address);
            if (persons == null) {
                return List.of();
            } else {
                return persons.stream().toList();
            }
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public @Nullable List<Person> findByPhone(String phoneNum) {
        validate(phoneNum);
        readLock.lock();
        try {
            Set<Person> persons = phoneTable.get(phoneNum);
            if (persons == null) {
                return List.of();
            } else {
                return persons.stream().toList();
            }
        } finally {
            readLock.unlock();
        }
    }

}
