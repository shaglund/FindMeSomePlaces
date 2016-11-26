package se.haglund;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {
    private static final String DEFAULT_RADIUS = "500";
    private String apiKey;
    private String radius;

    public Configuration(String configFile) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(configFile)) {
            if (is != null) {
                Properties properties = new Properties();
                properties.load(is);
                apiKey = properties.getProperty("google.api.key");
                radius = properties.getProperty("search.radius", DEFAULT_RADIUS);
            } else {
                throw new FileNotFoundException("Cannot find properties file " + configFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getGoogleApiKey() {
        return apiKey;
    }

    public String getSearchRadius() {
        return radius;
    }
}
