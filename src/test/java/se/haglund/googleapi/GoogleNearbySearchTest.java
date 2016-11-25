package se.haglund.googleapi;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.ListenableFuture;
import com.ning.http.client.Request;
import com.ning.http.client.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import se.haglund.Configuration;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class GoogleNearbySearchTest {
    @Mock
    AsyncHttpClient httpClient;

    @Mock
    GoogleSearchParser googleSearchParser;

    @Mock
    Configuration configuration;

    @Mock
    ListenableFuture<Response> future;

    @Mock
    Response response;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(configuration.getGoogleApiKey()).thenReturn("apiKey");
        when(configuration.getSearchRadius()).thenReturn("1234");
        when(response.getResponseBody()).thenReturn("body");
        when(future.get()).thenReturn(response);
        when(httpClient.executeRequest(any(Request.class))).thenReturn(future);
    }

    @Test
    public void searchNearbyTest() throws Exception {
        GoogleNearbySearch gns = new GoogleNearbySearch(httpClient, googleSearchParser, configuration);
        String[] locations = {"location1"};
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=location1&radius=1234&key=apiKey";
        ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);
        gns.searchNearby(locations);
        verify(httpClient).executeRequest(captor.capture());
        assertEquals(url, captor.getValue().getUrl());
        assertEquals("GET", captor.getValue().getMethod());
    }

    @Test
    public void multipleLocationsTest() throws Exception {
        GoogleNearbySearch gns = new GoogleNearbySearch(httpClient, googleSearchParser, configuration);
        String[] locations = {"location1", "location2"};
        String url1 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=location1&radius=1234&key=apiKey";
        String url2 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=location2&radius=1234&key=apiKey";
        ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);
        gns.searchNearby(locations);
        verify(httpClient, times(2)).executeRequest(captor.capture());
        List<Request> responses = captor.getAllValues();
        assertEquals(url1, responses.get(0).getUrl());
        assertEquals(url2, responses.get(1).getUrl());
        assertEquals("GET", responses.get(0).getMethod());
    }

    @Test
    public void nextPageTokenTest() throws Exception {
        GoogleNearbySearch gns = new GoogleNearbySearch(httpClient, googleSearchParser, configuration);
        String[] locations = {"location1"};
        String url1 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=location1&radius=1234&key=apiKey";
        String url2 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?pagetoken=token1&key=apiKey";
        when(googleSearchParser.parse(anyString(), anyMap())).thenReturn("token1").thenReturn(null);
        ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);
        gns.searchNearby(locations);
        verify(httpClient, times(2)).executeRequest(captor.capture());
        List<Request> responses = captor.getAllValues();
        assertEquals(url1, responses.get(0).getUrl());
        assertEquals(url2, responses.get(1).getUrl());
        assertEquals("GET", responses.get(0).getMethod());
    }

    @Test
    public void noLocationsTest() throws Exception {

    }

    @Test(expected = RuntimeException.class)
    public void noApiKeyTest() {
        when(configuration.getGoogleApiKey()).thenReturn(null);
        new GoogleNearbySearch(httpClient, googleSearchParser, configuration).searchNearby(null);
        verify(httpClient, never()).executeRequest(any(Request.class));
    }

}