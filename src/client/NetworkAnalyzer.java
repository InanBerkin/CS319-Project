package client;

import java.net.URL;
import java.net.URLConnection;

class NetworkAnalyzer {

    private String testURL;

    NetworkAnalyzer(String testURL) {
        this.testURL = testURL;
    }

    boolean isOnline() {
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
