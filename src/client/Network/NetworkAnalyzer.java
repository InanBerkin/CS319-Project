package client.Network;

import java.net.URL;
import java.net.URLConnection;

public class NetworkAnalyzer {

    private String testURL;

    public NetworkAnalyzer(String testURL) {
        this.testURL = testURL;
    }

    public boolean isOnline() {
        try {
            URL url = new URL(testURL);
            URLConnection connection = url.openConnection();
            connection.connect();

            System.out.println("» Online.");
            return true;
        }
        catch (Exception e) {
            System.out.println("» Offline.");
            return false;
        }
    }

}
