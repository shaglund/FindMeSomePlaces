package se.haglund.googleapi;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import se.haglund.InterestingPlace;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class GoogleSearchParser {

    public String parse(String jsonString, Map<String, Set<InterestingPlace>> places) {
        if (jsonString != null && !jsonString.isEmpty()) {
            try {
                JsonParser jsonParser = new JsonParser();
                JsonElement element = jsonParser.parse(jsonString);
                JsonObject jsonObject = element.getAsJsonObject();
                String status = jsonObject.get("status").getAsString();
                JsonArray results = jsonObject.getAsJsonArray("results");
                if(status.equals("OK") && results != null) {
                    for (JsonElement resultElement : results) {
                        InterestingPlace place = new InterestingPlace();
                        jsonObject = resultElement.getAsJsonObject();
                        if (jsonObject != null) {
                            place.setName(jsonObject.get("name").getAsString());
                            place.setVicinity(jsonObject.get("vicinity").getAsString());
                        }
                        JsonArray types = jsonObject.getAsJsonArray("types");
                        for (JsonElement typeElement : types) {
                            String placeType = typeElement.getAsString();
                            Set<InterestingPlace> placeSet = places.get(placeType);
                            if (placeSet == null) {
                                placeSet = new LinkedHashSet<>();
                                places.put(placeType, placeSet);
                            }
                            placeSet.add(place);
                        }
                    }
                }
                JsonElement npElement = element.getAsJsonObject().get("next_page_token");
                if(npElement != null) {
                    return npElement.getAsString();
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
