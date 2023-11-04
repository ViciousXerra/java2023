package edu.project2.cellbasedgenerators;

import edu.project2.cellbasedmaze.Maze;

public interface Generator {

    Maze generate(int height, int width);

    Maze generate();

    void reset();

}
