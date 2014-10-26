package com.example.linguar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import dictionary.Dictionary;
import dictionary.dictionary_populator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends Activity {

	private dictionary_populator creator;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
         try {
    	   creator = new dictionary_populator();
    	   AssetManager assetManager = getResources().getAssets();
    	   InputStream inputStream = null;
    	   inputStream = assetManager.open("categories.txt");
       	   InputStream inputStream1 = null;
       	   inputStream1 = assetManager.open("dictionarySpEn1.txt");
       	   
	       	if ( inputStream1 != null) {
	 		   Log.d("READING_FILE", "loading file Dic  worked!");
	 			Dictionary dic = Dictionary.getInstance();
	 			dic.LoadDictionary(inputStream1);
	 	    }
	       	
	       	if ( inputStream != null) {
	       		Log.d("READING_FILE", "loading file Cat worked!");
	    		creator.createCategoryDic(inputStream);
	    	}
    	   
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
     // Get the message from the intent
      
        String message = "hello";

        // Create the text view
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(message);

        // Set the text view as the activity layout
        setContentView(textView);
       // setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
