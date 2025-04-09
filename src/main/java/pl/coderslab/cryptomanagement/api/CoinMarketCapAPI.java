package pl.coderslab.cryptomanagement.api;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class CoinMarketCapAPI {
    private static final String API_KEY = "22bd0777-a2c3-41c0-abe0-fa97683c4db4";
    private static final String API_URL = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";

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

//{
//        "data": [
//        {
//        "symbol": "BTC",
//        "circulating_supply": 19848425,
//        "last_updated": "2025-04-08T12:54:00.000Z",
//        "total_supply": 19848425,
//        "tvl_ratio": null,
//        "cmc_rank": 1,
//        "self_reported_circulating_supply": null,
//        "platform": null,
//        "tags": [
//        "mineable",
//        "pow",
//        "sha-256",
//        "store-of-value",
//        "state-channel",
//        "coinbase-ventures-portfolio",
//        "three-arrows-capital-portfolio",
//        "polychain-capital-portfolio",
//        "binance-labs-portfolio",
//        "blockchain-capital-portfolio",
//        "boostvc-portfolio",
//        "cms-holdings-portfolio",
//        "dcg-portfolio",
//        "dragonfly-capital-portfolio",
//        "electric-capital-portfolio",
//        "fabric-ventures-portfolio",
//        "framework-ventures-portfolio",
//        "galaxy-digital-portfolio",
//        "huobi-capital-portfolio",
//        "alameda-research-portfolio",
//        "a16z-portfolio",
//        "1confirmation-portfolio",
//        "winklevoss-capital-portfolio",
//        "usv-portfolio",
//        "placeholder-ventures-portfolio",
//        "pantera-capital-portfolio",
//        "multicoin-capital-portfolio",
//        "paradigm-portfolio",
//        "bitcoin-ecosystem",
//        "ftx-bankruptcy-estate",
//        "2017-2018-alt-season",
//        "us-strategic-crypto-reserve"
//        ],
//        "date_added": "2010-07-13T00:00:00.000Z",
//        "quote": {"USD": {
//        "fully_diluted_market_cap": 1675073056108.3,
//        "last_updated": "2025-04-08T12:54:00.000Z",
//        "market_cap_dominance": 62.6369,
//        "tvl": null,
//        "percent_change_30d": -5.65886819,
//        "percent_change_1h": -0.10522358,
//        "percent_change_24h": 3.05990315,
//        "market_cap": 1583217234461.2595,
//        "volume_change_24h": -13.5167,
//        "price": 79765.38362420491,
//        "percent_change_60d": -18.54899437,
//        "volume_24h": 65709918966.579704,
//        "percent_change_90d": -15.93029032,
//        "percent_change_7d": -4.75415301
//        }},
//        "num_market_pairs": 12019,
//        "infinite_supply": false,
//        "name": "Bitcoin",
//        "max_supply": 21000000,
//        "id": 1,
//        "self_reported_market_cap": null,
//        "slug": "bitcoin"
//        },
