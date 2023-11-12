package edu.project2;

import edu.project2.cellbasedgenerators.BacktrackingGenerator;
import edu.project2.cellbasedgenerators.EllersAlgorithmGenerator;
import edu.project2.cellbasedgenerators.Generator;
import edu.project2.cellbasedmaze.Maze;

class DemoMazeProvider {

    static Maze getGeneratedDemoMazeWithoutBlock() {
        Generator generator = new BacktrackingGenerator(10, 5, false, false);
        return generator.generate();
    }

    static Maze getGeneratedDemoMazeWithBlock() {
        Generator generator = new EllersAlgorithmGenerator(5, 10, false, true);
        return generator.generate();
    }

}
