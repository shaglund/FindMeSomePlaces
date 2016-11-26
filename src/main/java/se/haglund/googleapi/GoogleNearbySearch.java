package se.haglund.googleapi;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;
import se.haglund.Configuration;
import se.haglund.InterestingPlace;
import se.haglund.NearbySearch;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class GoogleNearbySearch implements NearbySearch {
    private static final String API_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/";

    private final AsyncHttpClient httpClient;
    private final GoogleSearchParser parser;
    private final Configuration config;

    public GoogleNearbySearch(Configuration config) {
        this(new AsyncHttpClient(), new GoogleSearchParser(), config);
    }

    public GoogleNearbySearch(AsyncHttpClient httpClient, GoogleSearchParser parser, Configuration config) {
        if (config.getGoogleApiKey() == null || config.getGoogleApiKey().isEmpty()) {
            // Need to close the httpClient otherwise we hang when shutting down VM
            if(httpClient != null) {
                httpClient.close();
            }
            throw new RuntimeException("Google API key unavailable");
        }
        this.httpClient = httpClient;
        this.parser = parser;
        this.config = config;
    }

    @Override
    public Map<String, Set<InterestingPlace>> searchNearby(String[] locations) {
        Map<String, Set<InterestingPlace>> ret = new HashMap<>();
        for(String location: locations) {
            String nextPageToken = null;
            do {
                StringBuilder urlBuilder = new StringBuilder(API_URL).append("json?");
                if(nextPageToken != null && !nextPageToken.isEmpty()) {
                    urlBuilder
                            .append("pagetoken=").append(nextPageToken);
                } else {
                    urlBuilder
                            .append("location=").append(location)
                            .append("&radius=").append(config.getSearchRadius());
                }

                String url = urlBuilder.append("&key=").append(config.getGoogleApiKey()).toString();

                Request request = new RequestBuilder().setMethod("GET").setUrl(url).build();
                try {
                    nextPageToken = parser.parse(httpClient.executeRequest(request).get().getResponseBody(), ret);
                } catch (IOException | InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            while (nextPageToken != null);
        }
        return ret;
    }

}
