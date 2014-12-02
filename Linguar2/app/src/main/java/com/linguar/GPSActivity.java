package com.linguar;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.location.LocationManager;
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
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.json.*;


public class GPSActivity extends Activity implements TextToSpeech.OnInitListener {

    // These URLs are used to make API calls
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final int PLACES_RADIUS_DEFAULT = 100;

    private static final String FS_REST_API_BASE = "https://api.foursquare.com/v2/venues/search";
    private static final String FS_VERSION = "20130815";

    // Make sure you add the venue ID and then the path /menu at the end of this base
    private static final String FS_MENU_API_BASE = "https://api.foursquare.com/v2/venues";

    private static final String TAG = "GEOLOCATION";

    private TextView englishFood;
    private TextView spanishFood;

    private TextToSpeech menuTTS;
    private Locale spa;
    private Dictionary dic;

    private GestureDetector mGestureDetector;
    private  String[] finalMenuItems;
    private Integer currentcard;

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
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(true);

        Object systemService = this.getSystemService(Context.LOCATION_SERVICE);
        if (!(systemService instanceof LocationManager)) {
            // Display a card that indicates error
            buildErrorCard("Fail fast, fail hard. Couldn't find the Location Service."
                    + " Try again from the main menu.");
            return;
        }
        LocationManager locationManager = (LocationManager)systemService;

        List<String> providers = locationManager.getProviders(criteria, true);
        if (providers.isEmpty()) {
            // Display a card that indicates error
            buildErrorCard("If at first you don't succeed..."
                    + " Ok, we couldn't find loc providers."
                    + " Try again from the main menu.");
            return;
        }

        Location currentLocation = null;
        // We have the location service providers, now let's try them all.
        for (String provider: providers) {
            currentLocation = locationManager.getLastKnownLocation(provider);
        }
        if (currentLocation == null) {
            // Display a card that indicates an error
            buildErrorCard("Where are you? Where am I? Couldn't find location."
                    + " Try again from the main menu.");
            return;
        }

        // Obtain the coordinates of the location
        double latitude = currentLocation.getLatitude();
        double longitude = currentLocation.getLongitude();

        // Render a card to tell the user where they are


        // Look up the location in Google Places
        HttpURLConnection url = null;

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
                    buildErrorCard("Geolocation couldn't find anything special."
                            + " Try again from the main menu.");
                    return;
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
                        buildErrorCard("Glass to Foursquare, do you copy? Nope, guess not. "
                                + "Aborting mission. Try again from the main menu.");
                        return;
                    }
                    String firstIdHopeForTheBest = rest_ids[0];

                    // Make the menu api call
                    String[] menuItems = fsMenuApiCall(fsClientKey, fsClientSec, firstIdHopeForTheBest);

                    if (menuItems.length == 0) {
                        Log.d(TAG, "Could not retrieve menu items");
                        // Display a card that says Geolocation mode is failing
                        buildErrorCard("Foursquare says there are no menu items. Exiting."
                                + " Try again from the main menu.");
                        return;
                    }

                    // ***************************************************************************
                    // ***************************************************************************
                    // ***************************************************************************
                    //                        BEGIN LIST RELATED CODE!
                    //                        The array [finalMenuItems] will hold
                    //                        up to 10 menu items from the first
                    //                        restaurant returned by our Google Places search.
                    //
                    //                        These menu items are Strings, but can contain
                    //                        more than one word, e.g. "chocolate mocha latte"
                    //                        We still need to break down those Strings to
                    //                        each individual word before translating it.
                    //
                    //                        tl;dr [finalMenuItems] is the list!!!!

                    finalMenuItems = menuItems;
                    if (menuItems.length > 10) {
                        Log.d(TAG, "Shortening menu items by selecting random items");
                        finalMenuItems = new String[10];
                        Random generator = new Random();
                        for (int i = 0; i < 10; i++) {
                            int nextRandomIndex = generator.nextInt(menuItems.length);
                            finalMenuItems[i] = menuItems[nextRandomIndex];
                        }
                    }

                    currentcard = 0;
                    if(finalMenuItems.length > 0)
                    {
                        String menuItem = finalMenuItems[currentcard];
                        String spanishVersion = translateThisWord(menuItem);
                        //one card
                        setContentView(R.layout.activity_gps_result);
                        englishFood = (TextView) findViewById(R.id.textView1);
                        spanishFood = (TextView) findViewById(R.id.textView2);
                        englishFood.setText(menuItem);
                        spanishFood.setText(spanishVersion);
                    }
                    /*
                    for (String menuItem : finalMenuItems) {
                        // Display a card, translate the item, etc. etc.
                        // Translate the item
                        String spanishVersion = translateThisWord(menuItem);
                        //one card
                        setContentView(R.layout.activity_gps_result);
                        englishFood = (TextView) findViewById(R.id.textView1);
                        spanishFood = (TextView) findViewById(R.id.textView2);
                        englishFood.setText(menuItem);
                        spanishFood.setText(spanishVersion);
                    }*/
                    // ****************************************************************************
                    // ****************************************************************************
                    // ****************************************************************************
                }

            } else {
                // Show an error card that says we can't connect
                buildErrorCard("Is there really wi-fi around here?"
                        + " You know we need wi-fi to get the show on the road, right?"
                        + " Give up on this mode, do something offline.");
                return;
            }

            buildErrorCard("Geo Mode only works for restaurants for now. Come back later, sorry.");

        } catch (Exception e) {
            // Display a card that indicates an error
            buildErrorCard("Have you ever wanted to read a cryptic exception message? " + e);
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
