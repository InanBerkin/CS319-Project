package client;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello from client!");
        ClientSocketHandler clientSocketHandler = new ClientSocketHandler("localhost", 9999);
    }

}
