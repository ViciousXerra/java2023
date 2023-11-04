package edu.project2;

import edu.project2.cellbasedgenerators.BacktrackingGenerator;
import edu.project2.cellbasedgenerators.EllersAlgorithmGenerator;
import edu.project2.cellbasedgenerators.Generator;
import edu.project2.cellbasedmaze.Maze;

class DemoMazeProvider {

    static Maze getGeneratedDemoMazeWithoutBlock() {
        Generator generator = new BacktrackingGenerator(false, false);
        return generator.generate(10, 5);
    }

    static Maze getGeneratedDemoMazeWithBlock() {
        Generator generator = new EllersAlgorithmGenerator(false, true);
        return generator.generate(5, 10);
    }

}
