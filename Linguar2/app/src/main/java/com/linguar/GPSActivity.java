package com.linguar;

import android.app.Activity;
import android.content.Context;

import android.os.AsyncTask;
import android.os.Bundle;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.MotionEvent;
import android.widget.TextView;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.linguar.dictionary.Dictionary;
import com.linguar.dictionary.Word;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.json.*;


public class GPSActivity extends Activity implements TextToSpeech.OnInitListener {

    // These URLs are used to make API calls
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
    private static final int PLACES_RADIUS_DEFAULT = 400;

    private static final String FS_REST_API_BASE = "https://api.foursquare.com/v2/venues/search";
    private static final String FS_VERSION = "20130815";

    // Make sure you add the venue ID and then the path /menu at the end of this base
    private static final String FS_MENU_API_BASE = "https://api.foursquare.com/v2/venues";

    private static final String GOOGLE_CLIENT_KEY = "AIzaSyBjIHOsSWfsoPhgTSv7-tCyir3HPx1t-aQ";
    private static final String FS_CLIENT_KEY = "5053XWDMYGDL1ECJGBD4R2FIX4XNWJLCFN0WMVDVWTDZANV0";
    private static final String FS_CLIENT_SEC = "DIALY1OMS1Q3WGBXJ4MWC3X0MTNQE3KOZSYRK23OSMJQ5HDF";

    private static final String TAG = "GEOLOCATION";

    private Map<String, List<String>> menusAndItems;
    private TextView englishFood;
    private TextView spanishFood;

    private TextToSpeech menuTTS;
    private Locale spa;
    private Dictionary dic;

    private GestureDetector mGestureDetector;
    private  String[] finalMenuItems;
    private Integer currentcard;

    private Thread t;

    private double latitude = 40.444802;//currentLocation.getLatitude();
    private double longitude = -79.948518;//currentLocation.getLongitude();

    // Imma borrow dis real quik
    private String[] filterWords = {"a","about","after","all","also","an","am","and","any","as",
            "at","back","be","because","but","by","can","come","could",
            "day","do","even","first","for","from","get","give","go","good",
            "have","he","her","him","his","how","I","if","in","into","it","its",
            "just","know","like","look","make","me","most","my","new","no","not",
            "now","of","on","one","only","or","other","our","out","over","people",
            "say","see","she","so","some","take","than","that","the","their","them",
            "then","there","these","they","think","this","time","to","two","up","us",
            "use","want","way","we","well","what","when","which","who","will","with",
            "work","would","year","you","your"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        mGestureDetector = createGestureDetector(this);

        menuTTS = new TextToSpeech(this, this);
        dic = Dictionary.getInstance();

        try {
                NetworkRequestTask networkRequest = new NetworkRequestTask();
                networkRequest.execute("" + latitude, "" + longitude);

                for (int tries = 0; tries < 15; tries++) {
                    if (menusAndItems == null) {
                        Log.d(TAG, "Still executing network requests");
                        Thread.sleep(3000);
                        buildErrorCard("Loading...");
                    } else {
                        break;
                    }
                }
                if (menusAndItems == null) {
                    buildErrorCard("Sorry, this data is taking too long to fetch. Aborting.");
                    return;
                }

                if (networkRequest.getHasError()) {
                    buildErrorCard(networkRequest.getErrorMessage());
                    return;
                }

                if (menusAndItems.isEmpty()) {
                    buildErrorCard("Sorry, we failed to fetch menu items for you."
                            + " Try again later.");
                    return;
                }

                Set<String> menusNotItems = menusAndItems.keySet();
                Iterator<String> menusIterator = menusNotItems.iterator();
                List<String> flattenedItems = new ArrayList<String>();
                while (menusIterator.hasNext()) {
                    String nextMenu = menusIterator.next();
                    List<String> itemsNotMenus = menusAndItems.get(nextMenu);
                    for (String item : itemsNotMenus) {
                        flattenedItems.add(item);
                    }
                }

                for (String item : flattenedItems) {
                    String[] individualComponents = item.split(" ");
                    String finalSpanishTranslation = "";
                    if (individualComponents.length == 0) {
                        continue;
                    } else if (individualComponents.length == 1) {
                        finalSpanishTranslation = translateThisWord(item);
                    } else {
                        for (String component : individualComponents) {
                            finalSpanishTranslation += translateThisWord(component.trim());
                        }
                    }
                    setContentView(R.layout.activity_gps_result);
                    englishFood = (TextView) findViewById(R.id.textView1);
                    spanishFood = (TextView) findViewById(R.id.textView2);
                    englishFood.setText(item);
                    spanishFood.setText(finalSpanishTranslation);
                    menuTTS.speak(finalSpanishTranslation, TextToSpeech.QUEUE_FLUSH, null);
                    Thread.sleep(500);
                }
        } catch (Exception e) {
            // Display a card that indicates an error
            buildErrorCard("Have you ever wanted to read a cryptic exception message? " + e);
            Log.d(TAG, "Have you ever wanted to read a cryptic exception message?" + e);
        }

    }

    private class NetworkRequestTask extends AsyncTask<String, Void, Map<String, List<String>>> {
        private boolean caughtAnException;
        private String errMsg;
        private Exception caughtException;
        public boolean getHasError() {
            return caughtAnException;
        }
        public Exception getCaughtException() {
            return caughtException;
        }
        public String getErrorMessage() {
            return errMsg;
        }
        public void onPostExecute(Map<String, List<String>> results) {
            menusAndItems = results;
        }
        public Map<String, List<String>> doInBackground(String... params) {
            String latitude = params[0];
            String longitude = params[1];
            Map<String, List<String>> results = new HashMap<String, List<String>>();

            // Places API Call to get the type of establishment (restaurant, ...)
            StringBuffer placesBuffer = new StringBuffer(PLACES_API_BASE);
            placesBuffer.append("?key=" + GOOGLE_CLIENT_KEY);
            placesBuffer.append("&location=" + latitude + "," + longitude);
            placesBuffer.append("&radius=" + PLACES_RADIUS_DEFAULT);
            String placesResult = makeApiCall(placesBuffer.toString());
            Set<String> placesResultTypes = placesJsonExtractor(placesResult);
            if (!placesResultTypes.contains("restaurant")) {
                Log.d(TAG, "Aborting mission");
                return results;
            }

            // else... lat and lng confirmed, so use it for Foursquare API!
            StringBuffer fsBuf = new StringBuffer(FS_REST_API_BASE);
            fsBuf.append("?client_id=" + FS_CLIENT_KEY);
            fsBuf.append("&client_secret=" + FS_CLIENT_SEC);
            fsBuf.append("&v=" + FS_VERSION);
            fsBuf.append("&ll=" + latitude + "," + longitude);
            fsBuf.append("&radius=" + PLACES_RADIUS_DEFAULT);
            fsBuf.append("&intent=browse");
            String venueResult = makeApiCall(fsBuf.toString());
            String venueResultId = venueIdJson(venueResult);

            if (venueResultId.length() == 0) {
                Log.d(TAG, "Houston, we have a problem. Abort!");
                return results;
            }

            // else... venue ID confirmed, so now we do the bulky work and extract the menus
            StringBuffer menuBuf = new StringBuffer(FS_MENU_API_BASE);
            menuBuf.append("/" + venueResultId + "/menu");
            menuBuf.append("?client_id=" + FS_CLIENT_KEY);
            menuBuf.append("&client_secret=" + FS_CLIENT_SEC);
            menuBuf.append("&v=" + FS_VERSION);
            String menuResult = makeApiCall(menuBuf.toString());
            results = menusJson(menuResult);
            return results;
        }
        private String makeApiCall(String url) {
            try {
                URL urlString = new URL(url);
                HttpURLConnection connection = (HttpURLConnection)urlString.openConnection();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuffer response = new StringBuffer();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    reader.close();
                    return response.toString();
                }
            } catch (MalformedURLException e) {
                caughtAnException = true;
                caughtException = e;
                errMsg = "Malformed URL " + url;
            } catch (IOException e) {
                caughtAnException = true;
                caughtException = e;
                errMsg = "Could not open connection to " + url;
            }
            Log.d(TAG, "Did not get HTTP OK response for url " + url);
            caughtAnException = true;
            errMsg = "Did not get HTTP OK response for url " + url;
            return "";
        }

        private Set<String> placesJsonExtractor (String jsonResponse) {
            Set<String> results = new HashSet<String>();
            try {
                JSONObject obj = new JSONObject(jsonResponse);
                JSONArray jsonResults = obj.getJSONArray("results");
                JSONObject restaurantResult = jsonResults.getJSONObject(0);
                for (int i = 0; i < jsonResults.length(); i++) {
                    JSONObject placeResult = jsonResults.getJSONObject(i);
                    JSONArray resultsTypes = placeResult.getJSONArray("types");
                    for (int j = 0; j < resultsTypes.length(); j++) {
                        String type = resultsTypes.getString(j);
                        if ("restaurant".equals(type)) {
                            restaurantResult = placeResult;
                            break;
                        }
                    }
                }

                JSONArray restaurantResultTypes = restaurantResult.getJSONArray("types");
                for (int k = 0; k < restaurantResultTypes.length(); k++) {
                    results.add(restaurantResultTypes.getString(k));
                }
                Log.d(TAG, "Succeeded in getting place types: " + results);
            } catch (JSONException e) {
                caughtAnException = true;
                caughtException = e;
                errMsg = "JSON Parsing Error: " + e;
                Log.d(TAG, errMsg);
            }
            return results;
        }
        private String venueIdJson(String jsonResponse) {
            String venueId = "";
            try {
                JSONObject obj = new JSONObject(jsonResponse);
                JSONObject responseField = obj.getJSONObject("response");
                JSONArray venuesArray = responseField.getJSONArray("venues");
                if (venuesArray.length() == 0) {
                    Log.d(TAG, "Couldn't find any nearby venues");
                    return "";
                }
                JSONObject nearestPlace = venuesArray.getJSONObject(0);
                double nearestDist = (double)PLACES_RADIUS_DEFAULT * 2.0;
                for (int i = 0; i < venuesArray.length(); i++) {
                    // Find the closest venue by comparing Manhattan distance
                    JSONObject nextVenue = venuesArray.getJSONObject(i);
                    JSONObject nextVenueLocation = nextVenue.getJSONObject("location");
                    String vLat = nextVenueLocation.getString("lat");
                    String vLng = nextVenueLocation.getString("lng");
                    Double dLat = new Double(vLat);
                    Double dLng = new Double(vLng);
                    double mDistance = manhattanDistance(latitude, longitude, dLat, dLng);
                    if (mDistance < nearestDist && !nextVenue.getString("id").equals("5115422de4b06bb0f04ef760")) {
                        nearestPlace = nextVenue;
                        nearestDist = mDistance;
                    }
                }
                // Found the closest venue, now extract its id
                venueId = nearestPlace.getString("id");
                Log.d(TAG, "nearest venue is " + nearestPlace.getString("id"));
                return venueId;
            } catch (JSONException e) {
                caughtAnException = true;
                caughtException = e;
                errMsg = "JSON Parsing error in venueIdJson: " + e;
                Log.d(TAG, errMsg);
                return venueId;
            }
        }
        private double manhattanDistance(double lat1, double lng1, double lat2, double lng2) {
            return Math.abs((lat2 - lat1)) + Math.abs((lng2 - lng1));
        }

        private Map<String, List<String>> menusJson(String response) {
            Map<String, List<String>> menusBySection = new HashMap<String, List<String>>();
            try {
                JSONObject obj = new JSONObject(response);
                JSONObject responseField = obj.getJSONObject("response");
                JSONObject outerMenus = responseField.getJSONObject("menu").getJSONObject("menus");

                    JSONArray items = outerMenus.getJSONArray("items");

                    for (int i = 0; i < items.length(); i++) {
                        // Begin processing the 2nd level inner menus (out of 3)
                        JSONObject tier2Menu = items.getJSONObject(i);
                        String menuName = tier2Menu.getString("name");
                        JSONArray subItems = tier2Menu.getJSONObject("entries").getJSONArray("items");
                        List<String> menuItems = new ArrayList<String>();
                        for (int j = 0; j < subItems.length(); j++) {
                            JSONArray flattenedItems = subItems.getJSONObject(j).getJSONObject("entries").getJSONArray("items");
                            for (int k = 0; k < flattenedItems.length(); k++) {
                                String menuItemToAdd =  flattenedItems.getJSONObject(k).getString("name");
                                Log.d(TAG, menuItemToAdd);
                                menuItems.add(menuItemToAdd);
                            }
                        }
                        menusBySection.put(menuName, menuItems);
                    }

                return menusBySection;
            } catch (JSONException e) {
                caughtAnException = true;
                caughtException = e;
                errMsg = "JSON Parsing error in munsJson: " + e;
                Log.d(TAG, errMsg);
                return menusBySection;
            }
        }
    }

    private String translateThisWord(String word) {
        // All the translated words will necessarily be displayed
        // So we set the second param of getWords (boolean isDisplayed)
        // to true.
        Word foundWord = dic.getWord(word, true);
        if (foundWord == null) {
            return "";
        }

        if (isFilterWord(foundWord.englishWord)) {
            return "";
        }

        return foundWord.spanishTranslation;
    }

    private boolean isFilterWord(String word) {
        return Arrays.binarySearch(filterWords, word) >= 0;
    }

    private void buildErrorCard(String errorMessage) {
        TextView messageText;
        setContentView(R.layout.message_layout);
        messageText = (TextView) findViewById(R.id.messageView);
        messageText.setText(errorMessage);
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


    public void onInit(int initStatus) {
        //if successful, set locale
        if (initStatus == TextToSpeech.SUCCESS)
            spa = new Locale("es", "ES");
        menuTTS.setLanguage(spa);
    }

    private GestureDetector createGestureDetector(Context context) {
        //finalize context i guess??
        final Context c = context;

        GestureDetector gestureDetector = new GestureDetector(context);
        //Create a base listener for generic gestures
        gestureDetector.setBaseListener( new GestureDetector.BaseListener() {
            @Override
            public boolean onGesture(Gesture gesture) {
                if (gesture == Gesture.TAP) {
                    // do something on tap
                   // menuTTS.speak((spanishFood.getText()).toString(), TextToSpeech.QUEUE_FLUSH, null);
                    return true;
                } else if (gesture == Gesture.TWO_TAP) {
                    // do something on two finger tap
                    return true;
                } else if (gesture == Gesture.SWIPE_RIGHT) {
                    // do something on right (forward) swipe

                    if(finalMenuItems != null) {
                        currentcard = (currentcard + 1) % finalMenuItems.length;
                        if (finalMenuItems.length > 0) {
                            String menuItem = finalMenuItems[currentcard];
                            String spanishVersion = translateThisWord(menuItem);
                            //one card
                            setContentView(R.layout.activity_gps_result);
                            englishFood = (TextView) findViewById(R.id.textView1);
                            spanishFood = (TextView) findViewById(R.id.textView2);
                            englishFood.setText(menuItem);
                            spanishFood.setText(spanishVersion);
                        }
                    }
                    return true;
                } else if (gesture == Gesture.SWIPE_LEFT) {
                    // do something on left (backwards) swipe
                    if(finalMenuItems != null) {
                        currentcard = (currentcard - 1) % finalMenuItems.length;
                        if (finalMenuItems.length > 0) {
                            String menuItem = finalMenuItems[currentcard];
                            String spanishVersion = translateThisWord(menuItem);
                            //one card
                            setContentView(R.layout.activity_gps_result);
                            englishFood = (TextView) findViewById(R.id.textView1);
                            spanishFood = (TextView) findViewById(R.id.textView2);
                            englishFood.setText(menuItem);
                            spanishFood.setText(spanishVersion);
                        }
                    }
                    return true;
                }
                return false;
            }
        });
        gestureDetector.setFingerListener(new GestureDetector.FingerListener() {
            @Override
            public void onFingerCountChanged(int previousCount, int currentCount) {
                // do something on finger count changes
            }
        });
        gestureDetector.setScrollListener(new GestureDetector.ScrollListener() {
            @Override
            public boolean onScroll(float displacement, float delta, float velocity) {
                // do something on scrolling
                return false;
            }
        });
        return gestureDetector;
    }
    /* Send generic motion events to the gesture detector
    */
    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (mGestureDetector != null) {
            return mGestureDetector.onMotionEvent(event);
        }
        return false;
    }

    @Override
    protected void onDestroy()
    {
        menuTTS.stop();
        menuTTS.shutdown();

        super.onDestroy();

        finish();
    }

}
