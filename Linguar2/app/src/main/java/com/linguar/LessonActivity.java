package com.linguar;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;
import com.linguar.lessonplan.LessonPlan;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class LessonActivity extends Activity {

    private GestureDetector mGestureDetector;
    private LessonPlan lessonPlan = LessonPlan.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGestureDetector = createGestureDetector(this);

        String filePath = getFilesDir().getPath() + "/LessonPlan.ser";
        deserializeLoadData(filePath);
        setContentView(R.layout.activity_lesson);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lesson, menu);
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
                    return true;
                } else if (gesture == Gesture.SWIPE_RIGHT) {
                    // do something on right (forward) swipe
                    // Intent listening = new Intent(c, ListenerActivity.class);
                    Intent listening = new Intent(c, MainActivity.class);
                    startActivity(listening);
                    return true;
                } else if (gesture == Gesture.SWIPE_LEFT) {
                    // do something on left (backwards) swipe
                    Intent learning = new Intent(c, VoiceRecognitionActivity.class);
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


    public void serializeSaveData(String filePath)
    {
        FileOutputStream fileOut = null;
        ObjectOutputStream out = null;
        FileOutputStream fileOut1 = null;
        ObjectOutputStream out1 = null;

        try
        {
            // LessonPlanSerialzed
            fileOut = new FileOutputStream(filePath);
            out   = new ObjectOutputStream(fileOut);
            out.writeObject(lessonPlan);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in LessonPlan.ser");
        }
        catch(Exception i)
        {
            i.printStackTrace();
        }
    }

    public void deserializeLoadData(String filePath)
    {
        // Load Dictionary/CategoryDictionary from file
        try
        {
            FileInputStream fileIn = new FileInputStream(filePath);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            lessonPlan = (LessonPlan) in.readObject();
            in.close();
            fileIn.close();
        }
        catch(IOException i)
        {
            i.printStackTrace();
            return;
        }
        catch(ClassNotFoundException c)
        {
            System.out.println("LessonPlan class not found");
            c.printStackTrace();
            return;
        }

    }

    @Override
    protected void onDestroy()
    {
        String filePath = getFilesDir().getPath() + "/LessonPlan.ser";
        serializeSaveData(filePath);
    }
}
