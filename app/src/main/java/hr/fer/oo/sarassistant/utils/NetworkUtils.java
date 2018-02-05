package hr.fer.oo.sarassistant.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by nameless on 25.1.2018..
 */

public final class NetworkUtils {

    public static final String BASE_URL = "http://d77df3cc.ngrok.io/";


    public static final String RESCUERS_LIST_URL = BASE_URL + "api/Users";
    public static final String ACTIONS_LIST_URL = BASE_URL + "api/Actions";
    public static final String MSG_TEMPLATES_LIST_URL = BASE_URL + "api/MessageTemplates";

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
