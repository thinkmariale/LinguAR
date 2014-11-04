package com.example.linguar;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import java.io.InputStream;
import android.content.res.AssetManager;
import java.util.List;

import com.linguar.lessonplan.ReviewMode;
import com.linguar.serialization.Serialization;

import dictionary.Category;
import dictionary.CategoryDictionary;
import dictionary.Dictionary;
import dictionary.Word;
import dictionary.dictionary_populator;

import com.example.kyna.linguarv1.R;


public class EmulatorTest extends Activity {

    private dictionary_populator creator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // test if loading dic
// test if loading dic
        Dictionary dic = Dictionary.getInstance();
        CategoryDictionary cat = CategoryDictionary.getInstance(); //"Users/mariale/Documents/dictionary.ser";   "Users/mariale/Documents/cat_dictionary.ser";

        String filePath =  getFilesDir().getPath().toString() + "/dictionary.ser";
        String filePath1 =  getFilesDir().getPath().toString() + "/cat_dictionary.ser";

        try {
           // Serialization s = new Serialization();
           // s.loadData(filePath, filePath1);

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


               // s.saveData(filePath, filePath1);
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



        setContentView(R.layout.activity_my);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
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
