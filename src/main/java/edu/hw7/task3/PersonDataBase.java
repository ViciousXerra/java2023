package edu.hw7.task3;

import java.util.List;
import org.jetbrains.annotations.Nullable;

public interface PersonDataBase {

    void add(Person person);

    void delete(int id);

    @Nullable List<Person> findByName(String name);

    @Nullable List<Person> findByAddress(String address);

    @Nullable List<Person> findByPhone(String phoneNum);

}
