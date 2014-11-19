package com.linguar;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.linguar.dictionary.CategoryDictionary;
import com.linguar.dictionary.Dictionary;
import com.linguar.dictionary.Word;
import com.linguar.serialization.Serialization;

public class VoiceRecognitionActivity extends Activity implements
        RecognitionListener {

    private GestureDetector mGestureDetector;

    private TextView returnedText;
    private ToggleButton toggleButton;
    private ProgressBar progressBar;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private String LOG_TAG = "VoiceRecognitionActivity";

    private Dictionary dic = Dictionary.getInstance();

    private Serialization serialization;
    private String filePath;
    private String filePath1;

    private Queue mainText;
    private Queue removeText;
    private Set<String> set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_recognition);

        mGestureDetector = createGestureDetector(this);
        dic = Dictionary.getInstance();
        serialization = new Serialization();
        filePath  = getFilesDir().getPath().toString() + "/dictionary.ser";
        filePath1 = getFilesDir().getPath().toString() + "/cat_dictionary.ser";

        //contains current conversation to display on screen
        mainText   = new LinkedList();
        removeText = new LinkedList();
        set        = new HashSet<String >();

        //---
        Log.d(LOG_TAG, "creating VoiceRecognitionActivity " + dic.getDictionary().size());
        returnedText = (TextView) findViewById(R.id.textView1);
        progressBar  = (ProgressBar) findViewById(R.id.progressBar1);
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton1);

        progressBar.setVisibility(View.INVISIBLE);
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);

        toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setIndeterminate(true);
                    speech.startListening(recognizerIntent);
                } else {
                    progressBar.setIndeterminate(false);
                    progressBar.setVisibility(View.INVISIBLE);
                    speech.stopListening();
                }
            }
        });



        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(800);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // update TextView here!
                                if (!mainText.isEmpty()) {
                                    String str = mainText.element().toString();
                                   // System.out.println("Hello World! " + str);
                                    returnedText.setText(str);
                                    mainText.remove();
                                    removeText.add(str);
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
    }




    // And From your main() method or any other method

    @Override
    public void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "Resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        serialization.<Dictionary>saveData_(dic,filePath);
        serialization.<CategoryDictionary>saveData_(CategoryDictionary.getInstance(), filePath1);

        if (speech != null) {
            speech.destroy();
            toggleButton.setChecked(false);
            Log.i(LOG_TAG, "destroy");
        }

    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech");
        progressBar.setIndeterminate(false);
        progressBar.setMax(10);
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(LOG_TAG, "onBufferReceived: " + buffer);
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech");
        progressBar.setIndeterminate(true);
        toggleButton.setChecked(false);
    }

    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        Log.d(LOG_TAG, "FAILED " + errorMessage);
        if(errorCode != SpeechRecognizer.ERROR_CLIENT) {
            returnedText.setText(errorMessage);
            toggleButton.setChecked(false);
        }
       else toggleButton.setChecked(true);
    }

    @Override
    public void onEvent(int arg0, Bundle arg1) {
        Log.i(LOG_TAG, "onEvent");
    }

    @Override
    public void onPartialResults(Bundle arg0) {

        ArrayList<String> matches = arg0
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        String text = "";
        //showing translation of word
        for (String result : matches)
            text += result + "\n";

        String all[] = text.split(" ");

        if(dic.getDictionary().isEmpty())  Log.d(LOG_TAG,"EMPTY DIC");
        String textFinal = "";

        for(String str : all)
        {
            str = str.toLowerCase().trim();
           // Log.d(LOG_TAG,"word " + str );
            Word tempWord = dic.getWord(str, true);
            if(tempWord != null)
            {
                String t = tempWord.englishWord + " ---- "+ tempWord.spanishTranslation + "\n";
                if (set.add(t)) {
                    mainText.add(t);
                    Log.d(LOG_TAG,"adding " + t);
                }
                textFinal += t;
            }
           // else  Log.d(LOG_TAG,"word null " + str);
        }


        //returnedText.setText(textFinal);
        Log.i(LOG_TAG, "onPartialResults " +  text);
    }

    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.i(LOG_TAG, "onReadyForSpeech");
    }

    @Override
    public void onResults(Bundle results) {

      /*  ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = "";

        //showing translation of word
        for (String result : matches)
            text += result + "\n";


         Log.i(LOG_TAG, "onResults " + textFinal + " "+ text);*/

        while(!removeText.isEmpty())
        {
            set.remove(removeText.element().toString());
            removeText.remove();
        }

        toggleButton.setChecked(true);
    }

    @Override
    public void onRmsChanged(float rmsdB) {
       // Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
        progressBar.setProgress((int) rmsdB);
    }

    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
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
                    toggleButton.toggle();
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
        super.onDestroy();
        speech.destroy();

        serialization.<Dictionary>saveData_(Dictionary.getInstance(),filePath);
        serialization.<CategoryDictionary>saveData_(CategoryDictionary.getInstance(), filePath1);

    }
}
