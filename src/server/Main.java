package server;

public class Main {

    public static void main(String[] args) {
        Server server = new Server();
        //server.setDBCredentials("localhost", 3306, "root", "", "qbitz");
        server.setSocketPort(9999);
        server.start();
        System.out.println("Server Started!");
    }
}
