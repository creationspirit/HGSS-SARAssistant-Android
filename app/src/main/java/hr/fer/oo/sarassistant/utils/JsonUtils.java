package hr.fer.oo.sarassistant.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import hr.fer.oo.sarassistant.domain.Rescuer;

/**
 * Created by nameless on 25.1.2018..
 */

public final class JsonUtils {

    public static Rescuer[] getRescuersDataFromJson(String rescuersJsonStr) throws JSONException {

        final String NAME = "name";
        final String IS_AVAILABLE = "isAvailable";
        final String LATITUDE = "lat";
        final String LONGITUDE = "lon";

        JSONArray mJsonArray = new JSONArray(rescuersJsonStr);

        Rescuer[] parsedRescuersData = new Rescuer[mJsonArray.length()];

        for(int i = 0; i < mJsonArray.length(); i++) {
            JSONObject JsonRescuer = mJsonArray.getJSONObject(i);
            String[] fullName = JsonRescuer.getString(NAME).split(" ");
            parsedRescuersData[i] =
                    new Rescuer(fullName[0], fullName[1], JsonRescuer.getBoolean(IS_AVAILABLE),
                            JsonRescuer.getDouble(LATITUDE), JsonRescuer.getDouble(LONGITUDE));
        }
        return parsedRescuersData;
    }

}
