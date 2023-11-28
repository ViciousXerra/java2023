package edu.hw8;

import edu.hw8.task1.Server;
import org.junit.jupiter.api.Test;
import java.io.IOException;

public class Task1Test {

    @Test
    void test() throws IOException {
        int port = edu.hw8.task1.Utils.getFreePort();
        Server server = new Server(port, 10000, 1);
    }

}
