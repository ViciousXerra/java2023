package edu.project3;

public class demo {

    public static void main(String[] args) {
        String[] args1 = new String[] {
            "--path",
            "src/test/resources/project3resources/testinputs/*.txt",
            "--format",
            "markdown"
        };
        Driver.execute(args1);
    }

}
