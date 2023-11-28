package edu.hw8.task3;

import java.nio.file.Path;

public class demo {
    public static void main(String[] args) {
        Path path = Path.of("src/main/resources/hw8resources/hashedpasswords");
        PasswordBruteForce pbf1 = new SingleThreadMdHashPasswordBruteForce(
            path,
            5
        );
        PasswordBruteForce pbf2 = new MultiThreadMdHashPasswordBruteForce(
            path,
            5
        );
        System.out.println(pbf1.extractPasswords());
        System.out.println(pbf2.extractPasswords());
    }
}
