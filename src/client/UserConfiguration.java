package client;

import org.json.JSONObject;

import java.io.*;
import java.util.Scanner;

public class UserConfiguration {

    public static boolean isOnline = false;
    public static boolean isLoggedIn = false;

    public static void set(String configFilePath) throws IOException {
        JSONObject json = new JSONObject();
        json.put("", "");
        json.put("", "");
        json.put("", "");
        json.put("", "");

        BufferedWriter writer = new BufferedWriter(new FileWriter(configFilePath));
        writer.write(json.toString());
        writer.close();
    }

    public static void get(String configFilePath) throws FileNotFoundException {
        Scanner scan = new Scanner(new File(configFilePath));
        String configJSON = scan.next();
        scan.close();

        JSONObject json = new JSONObject(configJSON);

        // Get parsed values to attributes.
    }

}
