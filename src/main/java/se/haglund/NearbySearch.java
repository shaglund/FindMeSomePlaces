package se.haglund;

import java.util.Map;
import java.util.Set;

public interface NearbySearch {
    /**
     * Search provider for interesting places.
     * @param locations array of coordinates to search
     * @return Map with type of location as key and a set of locations as value
     */
    Map<String, Set<InterestingPlace>> searchNearby(String[] locations);
}
