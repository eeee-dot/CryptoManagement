package pl.coderslab.cryptomanagement.api;

import org.json.JSONObject;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class MainApi {
    private static final String API_KEY = "YOUR_API_KEY";
    private static final String API_URL = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
    public static void main(String[] args) {
        try {
            // Create URL object
            URL url = new URL(API_URL);

            // Open connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-CMC_PRO_API_KEY", API_KEY);
            connection.setRequestProperty("Accept", "application/json");

            // Read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            // Close connections
            in.close();
            connection.disconnect();

            // Parse JSON response
            JSONObject jsonResponse = new JSONObject(content.toString());
            System.out.println(jsonResponse.toString(2)); // Pretty print JSON response

            // Example of reading specific data
            if (jsonResponse.has("data")) {
                System.out.println("First cryptocurrency: " + jsonResponse.getJSONArray("data").getJSONObject(0).getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
