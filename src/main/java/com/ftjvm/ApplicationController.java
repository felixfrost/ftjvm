package com.ftjvm;
import org.apache.tomcat.util.json.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

@Controller
public class ApplicationController {


    @GetMapping("/")
    public String home (Model model){
        return "home";
    }

    @GetMapping("/getQuiz")
    public String apiTest(@RequestParam("amount") int amount, @RequestParam("category") int category, Model model){
        model.addAttribute("response", apiConnect(amount, category));
        return "apiTest";
    }

    // https://opentdb.com/api.php?amount=20&category=13
    // https://opentdb.com/api_category.php (get all categories and their id in an array)

    public String apiConnect(Integer amount, Integer category){
        StringBuilder informationString = new StringBuilder();

        try {
            //Public API:
            //https://www.metaweather.com/api/location/search/?query=<CITY>
            //https://www.metaweather.com/api/location/44418/

            URL url = new URL("https://opentdb.com/api.php?amount=" + amount + "&category=" + category);

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


                //JSON simple library Setup with Maven is used to convert strings to JSON

                GsonJsonParser parse = new GsonJsonParser();
                JSONArray dataObject = new JsonArray(informationString.toString());

                //Get the first JSON object in the JSON array
                System.out.println(dataObject.get(0));



            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return informationString.toString();
    }
}
