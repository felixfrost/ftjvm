package com.Service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

@Service
public class ApplicationService {
    public JSONArray apiConnect(Integer amount, Integer category, String difficulty){
        StringBuilder informationString = new StringBuilder();
        JSONObject object = new JSONObject();

        try {
            //Public API:
            //https://www.metaweather.com/api/location/search/?query=<CITY>
            //https://www.metaweather.com/api/location/44418/

            URL url = new URL("https://opentdb.com/api.php?amount=" + amount + "&category=" + category + "&difficulty=" + difficulty);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            //Check if connect is made
            int responseCode = conn.getResponseCode();

            // 200 OK
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {

                Scanner scanner = new Scanner(url.openStream());

                while (scanner.hasNext()) {
                    informationString.append(scanner.nextLine());
                }
                //Close the scanner
                scanner.close();

                System.out.println(informationString);

                object = new JSONObject(informationString.toString());
                System.out.println(object.getJSONArray("results").get(0));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return object.getJSONArray("results");
    }
}
