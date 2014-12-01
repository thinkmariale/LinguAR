<<<<<<< HEAD
package com.linguar;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.location.LocationManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.json.*;


public class GPSActivity extends Activity {

    // These URLs are used to make API calls
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final int PLACES_RADIUS_DEFAULT = 100;

    private static final String FS_REST_API_BASE = "https://api.foursquare.com/v2/venues/search";
    private static final String FS_VERSION = "20130815";

    // Make sure you add the venue ID and then the path /menu at the end of this base
    private static final String FS_MENU_API_BASE = "https://api.foursquare.com/v2/venues";

    private static final String TAG = "GEOLOCATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        // Write code here that will either
        // 1. Send a request to a backend server for location
        //    based data processing
        // 2. Invoke the Google Maps API right here and now
        //    and calculate:
        //      -Whether this is a special type of location
        //      -Obtain special data for this location
        //      -Select a word to display
        //      -Display the word, translation, and any other
        //       relevant info

        // Flow for restaurants:
        // Invoke a Google Search for the restaurant's name
        // Scrape their menu information
        // Enqueue all the items on their menu to somewhere
        // Along with their translations
        // Select a menu item and its translation
        // Display it to the frontend.
        // Perhaps also allow the user to scroll through the
        // vocab words and their translations in case we didn't
        // display the translation they were looking for at first.

        // Additionally, we should save the translated words
        // to the lesson plan module.

        // Current Plan: Do everything locally and refactor
        // to a backend database if it gets too slow.

        // Obtain location services
        Object systemService = this.getSystemService(Context.LOCATION_SERVICE);
        if (!(systemService instanceof LocationManager)) {
            // Display a card that indicates error
            return;
        }
        LocationManager locationManager = (LocationManager)systemService;

        List<String> providers = locationManager.getProviders(true);
        if (providers.isEmpty()) {
            // Display a card that indicates error
            return;
        }

        Location currentLocation = null;
        // We have the location service providers, now let's try them all.
        for (String provider: providers) {
            currentLocation = locationManager.getLastKnownLocation(provider);
        }
        if (currentLocation == null) {
            // Display a card that indicates an error
            return;
        }

        // Obtain the coordinates of the location
        double latitude = currentLocation.getLatitude();
        double longitude = currentLocation.getLongitude();

        // Look up the location in Google Places
        HttpURLConnection url = null;
        StringBuilder jsonResults = new StringBuilder();

        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE);
            // Required params: key, latitude, longitude, radius
            sb.append("?key=AIzaSyBjIHOsSWfsoPhgTSv7-tCyir3HPx1t-aQ");
            sb.append("&location=" + latitude + "," + longitude);
            sb.append("&radius=" + PLACES_RADIUS_DEFAULT);

            URL urlString = new URL(sb.toString());

            // Send the request
            url = (HttpURLConnection) urlString.openConnection();

            // Get the response
            if (url.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Look up the place type
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlString.openStream()));
                StringBuffer res = new StringBuffer();
                String line;

                while ((line = reader.readLine()) != null) {
                    res.append(line);
                }

                reader.close();

                JSONObject obj = new JSONObject(res.toString());
                JSONArray results = obj.getJSONArray("results");
                JSONObject firstResult = results.getJSONObject(0);
                JSONArray types = firstResult.getJSONArray("types");

                if (types == null || types.length() == 0) {
                    // Display a card that says Geolocation has not found anything special

                }

                HashSet<String> typesAsSet = new HashSet<String>();
                for (int i = 0; i < types.length(); i++) {
                    String nextType = types.getString(i);
                    typesAsSet.add(nextType);
                }

                String fsClientKey = "5053XWDMYGDL1ECJGBD4R2FIX4XNWJLCFN0WMVDVWTDZANV0";
                String fsClientSec = "DIALY1OMS1Q3WGBXJ4MWC3X0MTNQE3KOZSYRK23OSMJQ5HDF";

                if (typesAsSet.contains("restaurant")) {
                    // There is a restaurant nearby! Now we will ask Foursquare for its info
                    String[] rest_ids = fsApiCall(fsClientKey, fsClientSec, latitude, longitude);

                    // We have obtained the restaurant Ids. Now we must pick a restaurant Id
                    // and hope it is the correct restaurant, then obtain its menu. We will
                    // use the menus API in Foursquare to obtain the menu via Id.
                    // We will also display the provider, as Foursquare requested.
                    if (rest_ids.length == 0) {
                        // Display a card that says Geolocation's Foursquare API call failed
                    }
                    String firstIdHopeForTheBest = rest_ids[0];

                    // Make the menu api call
                    String[] menuItems = fsMenuApiCall(fsClientKey, fsClientSec, firstIdHopeForTheBest);

                    if (menuItems.length == 0) {
                        Log.d(TAG, "Could not retrieve menu items");
                        // Display a card that says Geolocation mode is phailing

                    }

                    for (String menuItem : menuItems) {
                        // Display a card, translate the item, etc. etc.
                        // ************************************************************************
                        // ************************************************************************
                        // ************************************************************************
                        // TODO TODO TODO
                        // PUT YOUR CODE HERE!!!!!!!!!!!!!!!!!!!!!! You have access to each
                        // menuItem String in [menuItems]
                        // ************************************************************************
                        // ************************************************************************
                        // ************************************************************************
                    }
                }

            } else {
                // Show an error card that says we can't connect
            }


        } catch (Exception e) {
            // Display a card that indicates an error
        }

    }


    private String[] fsApiCall(String cKey, String cSec, double lat, double lng) {
        StringBuffer buffer = new StringBuffer(FS_REST_API_BASE);
        buffer.append("?client_key=" + cKey);
        buffer.append("&client_secret=" + cSec);
        buffer.append("&v=" + FS_VERSION);
        buffer.append("&ll=" + lat + "," + lng);

        try {
            URL urlString = new URL(buffer.toString());

            HttpURLConnection url = (HttpURLConnection)urlString.openConnection();

            if (url.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Go through the response using a JSON parser and find the venues
                // Then for each venue record the id in an ArrayList
                ArrayList<String> venues = new ArrayList<String>();

                // Read in the response
                BufferedReader responseReader = new BufferedReader(new InputStreamReader(url.getInputStream()));
                StringBuffer response = new StringBuffer();
                String line;

                while ((line = responseReader.readLine()) != null) {
                    response.append(line);
                }

                responseReader.close();

                // Finished reading the response. Now we will use a JSON parser to extract
                // the information that we need from the response (mostly IDs)
                JSONObject obj = new JSONObject(response.toString());

                JSONArray theVenues = obj.getJSONObject("response").getJSONArray("venues");
                for (int i = 0; i < theVenues.length(); i++) {
                    // Get the i-th venue and extract its ID
                    JSONObject nextVenue = theVenues.getJSONObject(i);
                    String venueId = nextVenue.getString("id");
                    venues.add(venueId);
                }

                return venues.toArray(new String[venues.size()]);
            } else {
                Log.d(TAG, "Foursquare API Response not 200");
                return new String[0];
            }

        } catch (MalformedURLException e) {
            Log.d(TAG, "Foursquare API Call: " + e.toString());
            return new String[0];
        } catch (IOException e) {
            Log.d(TAG, "Foursquare API Call: " + e.toString());
            return new String[0];
        } catch (JSONException e) {
            Log.d(TAG, "JSON Parsing Foursquare Response: " + e.toString());
            return new String[0];
        }
    }

    private String[] fsMenuApiCall(String cKey, String cSec, String rId) {
        StringBuilder builder = new StringBuilder(FS_MENU_API_BASE);
        builder.append(rId + "/menu");
        builder.append("?v=" + FS_VERSION);
        builder.append("&client_id=" + cKey);
        builder.append("&client_secret=" + cSec);

        try {
            URL urlString = new URL(builder.toString());
            HttpURLConnection connection = (HttpURLConnection)urlString.openConnection();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // This ArrayList will hold our extracted menu items
                ArrayList<String> menuItems = new ArrayList<String>();

                // We have established a connection to the API and are in the process of
                // reading in the response. We shall read in the response and parse the JSON!
                BufferedReader responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer res = new StringBuffer();
                String line;

                while ((line = responseReader.readLine()) != null) {
                    res.append(line);
                }

                responseReader.close();

                // Now we parse the JSON response to extract the menu and put it in [menuItems]
                JSONObject obj = new JSONObject(res.toString());

                JSONObject responseJson = obj.getJSONObject("response");
                JSONObject mJson = responseJson.getJSONObject("menu");
                JSONObject menusJson = mJson.getJSONObject("menus");
                String menusCount = menusJson.getString("count");
                Integer menusCountInt = new Integer(menusCount);
                JSONArray items = menusJson.getJSONArray("items");
                for (int i = 0; i < menusCountInt; i++) {
                    JSONObject menu = items.getJSONObject(i);
                    // The entries is still like, a 4th tier wrapper field
                    // We still need to extract the count from the entries wrapper
                    // Then we loop for count iterations and get the count items
                    // I got this all from
                    // https://developer.foursquare.com/docs/explore#req=venues/
                    // 47a1bddbf964a5207a4d1fe3/menu
                    // Just looked for the pretty menu items
                    JSONObject entries = menu.getJSONObject("entries");
                    String outerEntriesCount = entries.getString("count");
                    Integer outerEntriesCountInt = new Integer(outerEntriesCount);
                    JSONObject innerEntries = entries.getJSONObject("entries");
                    JSONArray innerItems = innerEntries.getJSONArray("items");

                    for (int j = 0; j < outerEntriesCountInt; j++) {
                        JSONObject innerEntry = innerItems.getJSONObject(i);
                        // Ok now we need to get "items" again because it's double nested
                        String entryName = innerEntry.getString("name");
                        menuItems.add(entryName);
                    }
                }

                return menuItems.toArray(new String[menuItems.size()]);
            } else {
                Log.d(TAG, "Bad response from Foursquare Menu API Call");
                return new String[0];
            }

        } catch (MalformedURLException e) {
            Log.d(TAG, "Foursquare Menu API Call: " + e);
            return new String[0];
        } catch (IOException e) {
            Log.d(TAG, "Foursquare Menu API Call: " + e);
            return new String[0];
        } catch (JSONException e) {
            Log.d(TAG, "Foursquare Menu JSON Parsing error: " + e);
            return new String[0];
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_g, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
