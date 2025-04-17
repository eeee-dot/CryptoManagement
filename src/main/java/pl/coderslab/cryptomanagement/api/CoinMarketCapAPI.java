package pl.coderslab.cryptomanagement.api;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class CoinMarketCapAPI {
    private static final String API_KEY = ApiSettings.API_KEY;
    private static final String API_URL = ApiSettings.API_URL;

    public static JSONObject getAPIResponse() {
        try {
            JSONObject jsonResponse = getJsonObject();

            if (jsonResponse.has("data")) {
                return jsonResponse;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static JSONObject getJsonObject() throws IOException {
        URL url = new URL(API_URL);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-CMC_PRO_API_KEY", API_KEY);
        connection.setRequestProperty("Accept", "application/json");

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            return new JSONObject(content.toString());
        } finally {
            connection.disconnect();
        }
    }
}
