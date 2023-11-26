package edu.hw8.task1;

public class ClientDemo {

    public static void main(String[] args) {
        Client client = new Client(8080, "язык");
        String response = client.getResponse();
        System.out.println(response);
    }

}
