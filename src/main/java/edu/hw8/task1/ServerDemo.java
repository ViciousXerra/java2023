package edu.hw8.task1;

public class ServerDemo {
    public static void main(String[] args) {
        Server server = new Server(8080, 10000);
        server.listen();
    }
}
