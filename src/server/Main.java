package server;

public class Main {

    public static void main(String[] args) {
        Server server = new Server();
        server.setDBCreditantials("localhost", 3306, "root", "", "qbitz");
        server.start();

        server.stop();
    }

}
