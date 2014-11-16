package com.linguar;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.linguar.dictionary.*;
import com.linguar.serialization.Serialization;

import java.io.InputStream;

/**
 * An {@link Activity} showing a tuggable "Hello World!" card.
 * <p>
 * The main content view is composed of a one-card {@link CardScrollView} that provides tugging
 * feedback to the user when swipe gestures are detected.
 * If your Glassware intends to intercept swipe gestures, you should set the content view directly
 * and use a {@link com.google.android.glass.touchpad.GestureDetector}.
 * @see <a href="https://developers.google.com/glass/develop/gdk/touch">GDK Developer Guide</a>
 */
public class MainActivity extends Activity {

    private GestureDetector mGestureDetector;

    /** {@link CardScrollView} to use as the main content view. */
    private CardScrollView mCardScroller;

    /** "Hello World!" {@link View} generated by {@link #buildView()}. */
    private View mView;

    private  Serialization serialization;
    private String filePath;
    private String filePath1;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        mGestureDetector = createGestureDetector(this);

        mView = buildView();

        mCardScroller = new CardScrollView(this);
        mCardScroller.setAdapter(new CardScrollAdapter() {
            @Override
            public int getCount() {
                return 1;
            }

            @Override
            public Object getItem(int position) {
                return mView;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return mView;
            }

            @Override
            public int getPosition(Object item) {
                if (mView.equals(item)) {
                    return 0;
                }
                return AdapterView.INVALID_POSITION;
            }
        });
        // Handle the TAP event.
        mCardScroller.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Plays disallowed sound to indicate that TAP actions are not supported.

                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                am.playSoundEffect(Sounds.DISALLOWED);

            }
        });

        // load dictionaries
        Dictionary dic = Dictionary.getInstance();
        CategoryDictionary cat = CategoryDictionary.getInstance();
        dictionary_populator creator;
        serialization = new Serialization();
        filePath  = getFilesDir().getPath().toString() + "/dictionary.ser";
        filePath1 = getFilesDir().getPath().toString() + "/cat_dictionary.ser";


        try {
            serialization.<Dictionary>loadData_(dic,filePath);
            serialization.<CategoryDictionary>loadData_(cat,filePath1);

            //serialization.loadData(filePath, filePath1);

            if(cat.getCatDictionary().isEmpty())
                Log.d("READING_FILE", " cat empty!");
            if(dic.getDictionary().isEmpty()){
                Log.d("READING_FILE", "empty!");
                creator = new dictionary_populator();
                AssetManager assetManager = getResources().getAssets();
                Log.d("READING_FILE", assetManager.toString());

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

                if(!dic.getDictionary().isEmpty() && !cat.getCatDictionary().isEmpty() ){
                    Log.d("READING_FILE", "done!");
                   /* String []test = {"apple", "banana","peach","car","bike"};

                    for(String str: test)
                    {
                        Word x = dic.getWord(str, true);
                        Log.d("DIC","word " + x.englishWord + " " +  x.spanishTranslation);
                    }*/
                }
                //serialization.saveData(filePath, filePath1);
                serialization.<Dictionary>saveData_(dic,filePath);
                serialization.<CategoryDictionary>saveData_(cat,filePath1);

            }
            else
                Log.d("READING_FILE", "not empty!");


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }




        setContentView(mCardScroller);
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
                    return true;
                } else if (gesture == Gesture.TWO_TAP) {
                    // do something on two finger tap
                    // temporary- letting Mode 3 (Location) take over 2 tap
                    Intent locationDetector = new Intent(c, GPSActivity.class);
                    startActivity(locationDetector);
                    return true;
                } else if (gesture == Gesture.SWIPE_RIGHT) {
                    // do something on right (forward) swipe
                    // Intent listening = new Intent(c, ListenerActivity.class);
                    Intent listening = new Intent(c, VoiceRecognitionActivity.class);
                    startActivity(listening);
                    return true;
                } else if (gesture == Gesture.SWIPE_LEFT) {
                    // do something on left (backwards) swipe
                    Intent learning = new Intent(c, LessonActivity.class);
                    startActivity(learning);
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

    /*
     * Send generic motion events to the gesture detector
     */
    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (mGestureDetector != null) {
            return mGestureDetector.onMotionEvent(event);
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCardScroller.activate();
    }

    @Override
    protected void onPause() {
        mCardScroller.deactivate();
        serialization.<Dictionary>saveData_(Dictionary.getInstance(),filePath);
        serialization.<CategoryDictionary>saveData_(CategoryDictionary.getInstance(),filePath1);
        super.onPause();
    }

    /**
     * Builds a Glass styled "Hello World!" view using the {@link CardBuilder} class.
     */
    private View buildView() {
        CardBuilder card = new CardBuilder(this, CardBuilder.Layout.TEXT);

        card.setText("\n Swipe forward for Listening Mode\n Swipe backward for Learning Mode!!");

        return card.getView();
    }

}