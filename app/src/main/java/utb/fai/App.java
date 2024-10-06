package utb.fai;

public class App {

    public static void main(String[] args) {
        String serverIp = args[0];
        int port = Integer.parseInt(args[1]);
        TelnetClient telnetClient = new TelnetClient(serverIp, port);
        telnetClient.run();
    }
}
