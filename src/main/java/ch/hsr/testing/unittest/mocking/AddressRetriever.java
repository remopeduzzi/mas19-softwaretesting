package ch.hsr.testing.unittest.mocking;


import ch.hsr.testing.unittest.testbuilderpattern.Address;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;

public class AddressRetriever {
    private final HttpService httpService = new HttpService();
    private final String apiKey;

    public AddressRetriever(String apiKey) {
        this.apiKey = apiKey;
    }

    public Address retrieve(double latitude, double longitude) throws AddressRetrieverException {
        String params = String.format("lat=%.6f&lon=%.6f", latitude, longitude);
        try {
            String response = httpService.get("http://open.mapquestapi.com/nominatim/v1/reverse.php?key=" + apiKey
                    + "&format=json&" + params);

            JSONObject obj = (JSONObject) new JSONParser().parse(response);
            JSONObject address = (JSONObject) obj.get("address");
            return Address.fromJSON(address);

        } catch (IOException e) {
            throw new AddressRetrieverException("Received Exception from HttpService", e);
        } catch (Exception e) {
            throw new AddressRetrieverException("Could not parse JSON from HttpService", e);
        }

    }
}