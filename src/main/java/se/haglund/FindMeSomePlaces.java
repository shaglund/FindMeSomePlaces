package se.haglund;

import static spark.Spark.get;

import com.google.gson.Gson;
import se.haglund.googleapi.GoogleNearbySearch;

public class FindMeSomePlaces {
    public static void main(String[] args) {
        Configuration config = new Configuration("config.properties");
        NearbySearch searchEngine = new GoogleNearbySearch(config);
        Gson gson = new Gson();
        get("/findmesomeplaces", (req, res) -> {
            res.type("application/json");
            return gson.toJson(searchEngine.searchNearby(req.queryParamsValues("location")));
        });
    }
}
