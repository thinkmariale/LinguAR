package com.linguar;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.location.LocationManager;

import java.util.List;


public class GPSActivity extends Activity {

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

        // Look up the location in Google Maps.

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
