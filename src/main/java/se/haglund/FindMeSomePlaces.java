package se.haglund;

import static spark.Spark.get;
import static spark.Spark.stop;

import com.google.gson.Gson;
import se.haglund.googleapi.GoogleNearbySearch;

public class FindMeSomePlaces {
    public static void main(String[] args) {
        try {
            Configuration config = new Configuration("config.properties");
            NearbySearch searchEngine = new GoogleNearbySearch(config);
            Gson gson = new Gson();
            get("/findmesomeplaces", (req, res) -> {
                res.type("application/json");
                return gson.toJson(searchEngine.searchNearby(req.queryParamsValues("location")));
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Stop Spark
            stop();
        }
    }
}
