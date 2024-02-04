package edu.hw10.task1;

import edu.hw10.task1.classes.Randomizable;

public interface ObjectGenerator {

    Object nextObject(Class<? extends Randomizable> c);

}
