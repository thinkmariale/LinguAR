package com.example.linguar;


import java.io.File;
import java.io.InputStream;
import java.util.List;

import com.linguar.lessonplan.ReviewMode;
import com.linguar.serialization.Serialization;

import dictionary.Category;
import dictionary.CategoryDictionary;
import dictionary.Dictionary;
import dictionary.Word;
import dictionary.dictionary_populator;
import android.app.Activity;

import android.content.Context;
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
        Dictionary dic = Dictionary.getInstance();
       CategoryDictionary cat = CategoryDictionary.getInstance();
       
        String filePath = "Users/mariale/Documents/dictionary.ser"; //getFilesDir().getPath().toString() + "/dictionary.ser";
		String filePath1 = "Users/mariale/Documents/cat_dictionary.ser";//getFilesDir().getPath().toString() + "/cat_dictionary.ser";
		
        try {
        	Serialization s = new Serialization();
        	s.loadData(filePath, filePath1);
        	
        	if(cat.getCatDictionary().isEmpty())
        		Log.d("READING_FILE", " cat empty!");
        	if(dic.getDictionary().isEmpty()){
        		Log.d("READING_FILE", "empty!");
        		creator = new dictionary_populator();
        		AssetManager assetManager = getResources().getAssets();
        		InputStream inputStream = null;
        		inputStream = assetManager.open("finalCategories.txt");
        		InputStream inputStream1 = null;
        		inputStream1 = assetManager.open("dictionarySpEn1.txt");

        		if ( inputStream1 != null) {
        			Log.d("READING_FILE", "loading file Dic  worked!");
        			dic.LoadDictionary(inputStream1);
        		}

        		if ( inputStream != null) {
        			Log.d("READING_FILE", "loading file Cat worked!");
        			creator.createCategoryDic(inputStream);
        		}
        		
        		
        		s.saveData(filePath, filePath1);
        	}
        	else
        		Log.d("READING_FILE", "not empty!");
        		
    	   
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
     // Get the message from the intent
      
        String []test = {"apple", "banana","peach","car","bike"};
        
        for(String str: test)
        {
        	Word x = dic.getWord(str, true);
        	Log.d("DIC","word " + x.englishWord + " " +  x.spanishTranslation);
        }
        
  
       List<Category> listCat = cat.getTopCategories();
       Log.d("DIC", "Size list " + listCat.size());
       for(Category c: listCat)
       {
    	   if(c != null)
    	   {
    		   Log.d("DIC", "Count " + c.counter  + " " + c.category);
    	   }

       }
       
       //ReviewMode rm = new ReviewMode();
     //  try {
		//rm.startLessonPlan();
	//} catch (Exception e) {
		// TODO Auto-generated catch block
	//	e.printStackTrace();
	//}
       
        String message = "hello";

       // Create the text view
       TextView textView = new TextView(this);
       textView.setTextSize(40);
       textView.setText(message);

       // Set the text view as the activity layout
      // setContentView(textView);
        setContentView(R.layout.activity_main);
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
