package Service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

@Service
public class ApplicationService {
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

                JSONObject object = new JSONObject(informationString.toString());
                System.out.println(object.getJSONArray("results").get(0));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return informationString.toString();
    }
}
