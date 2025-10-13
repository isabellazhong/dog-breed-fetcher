package dogapi;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.ws.RealWebSocket.Message;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) {
        String url = "https://dog.ceo/api/breed/" + breed + "/list"; 
        final Request request = new Request.Builder().url(url).get().build();

        try {
            final Response response = client.newCall(request).execute();
            try {
                final JSONObject responseBody = new JSONObject(response.body().string());

                if (responseBody.getString("status").equals("success")) {
                    final JSONArray subBreeds = responseBody.getJSONArray("message");
                    List<String> subBreedsArray = new ArrayList<>();
                    for (int i = 0; i < subBreeds.length(); i++) {
                        subBreedsArray.add(subBreeds.getString(i));
                    }
                    return subBreedsArray;
                }
            } finally {
                response.close();
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }

        return new ArrayList<>();
    }
}