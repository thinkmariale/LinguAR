package com.linguar;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;
import com.linguar.dictionary.CategoryDictionary;
import com.linguar.dictionary.Dictionary;
import com.linguar.dictionary.Word;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import java.util.Random;
import java.lang.Integer;

public class QueryActivity extends Activity implements
        RecognitionListener, TextToSpeech.OnInitListener {

    private GestureDetector mGestureDetector;

    /////////////////voice recognition and general variables/////////////////////
    //variable for checking Voice Recognition support on user device
    private static final int VR_REQUEST = 999;

    //ListView for displaying suggested words
    private ListView wordList;
    private TextView returnedTextA;
    private TextView returnedTextB;
    private ProgressBar progressBar;
    private ToggleButton toggleButton;

    //Log tag for output information
    private final String LOG_TAG = "QueryMode";

    ////////////////TTS variables/////////////////////
    //variable for checking TTS engine data on user device
    private int MY_DATA_CHECK_CODE = 0;

    //Text To Speech instance
    private TextToSpeech repeatTTS;
    private Intent recognizerIntent;
    private SpeechRecognizer speech = null;
    //Spanish Locale
    private Locale spa;
    private Locale eng;

    //remember word said
    private String lastWordSaid;

    //get words said
    private ArrayList<String> suggestedWords;

    private Dictionary dic = Dictionary.getInstance();

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_query);
        mGestureDetector = createGestureDetector(this);

        dic = Dictionary.getInstance();
        returnedTextA = (TextView) findViewById(R.id.textView1);
        returnedTextB = (TextView) findViewById(R.id.textView2);
        progressBar  = (ProgressBar) findViewById(R.id.progressBar1);
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton1);
        repeatTTS = new TextToSpeech(this, this);
        lastWordSaid = "";
        suggestedWords = new ArrayList<String>();


        String tmp = "Please speak a word.";
        returnedTextA.setText(tmp);

        progressBar.setVisibility(View.INVISIBLE);
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false);


            // Handle the TAP event.
            toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    if (isChecked) {
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.setIndeterminate(true);
                       // Log.d("l", "in toggle");
                        speech.startListening(recognizerIntent);
                       // Log.d("l", "after speech");
                    } else {
                        progressBar.setIndeterminate(false);
                        progressBar.setVisibility(View.INVISIBLE);
                        speech.stopListening();
                    }
                }
            });
    }

    @Override
    public void onPartialResults(Bundle arg0) {
        Log.i(LOG_TAG, "onPartialResults");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "Resume");
    }

    @Override
    protected void onPause() {
        super.onPause();

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
            //returnedText.setText(errorMessage);
            toggleButton.setChecked(false);
        }
        toggleButton.setChecked(false);
    }

    @Override
    public void onEvent(int arg0, Bundle arg1) {
        Log.i(LOG_TAG, "onEvent");
    }


    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.i(LOG_TAG, "onReadyForSpeech");
    }

    @Override
    public void onResults(Bundle results) {

        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        Log.d(LOG_TAG,"on result" );
        String text = "";
        //showing translation of word
        for (String result : matches)
            text += result + "\n";

        String all[] = text.split(" ");

        if (dic.getDictionary().isEmpty()) Log.d(LOG_TAG, "EMPTY DIC");
        String textFinal = "";

        for (String str : all) {
            str = str.toLowerCase().trim();
            // Log.d(LOG_TAG,"word " + str );
            Word tempWord = dic.getWord(str, false);
            if (tempWord != null) {
                String t = tempWord.englishWord + " ---- " + tempWord.spanishTranslation + "\n";
                Log.d(LOG_TAG,"word " + t );
                returnedTextA.setText(tempWord.englishWord);
                returnedTextB.setText(tempWord.spanishTranslation);
            }
        }
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

    /**
     * onInit fires when TTS initializes
     */
    public void onInit(int initStatus) {
        //if successful, set locale
        if (initStatus == TextToSpeech.SUCCESS)
            spa = new Locale("es", "ES");
        repeatTTS.setLanguage(spa);
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
                    //Log.d("l", "in toggle 1");
                    toggleButton.toggle();
                    return true;
                } else if (gesture == Gesture.TWO_TAP) {
                    repeatTTS.speak((returnedTextB.getText()).toString(), TextToSpeech.QUEUE_FLUSH, null);
                    // do something on two finger tap
                    return true;
                } else if (gesture == Gesture.SWIPE_RIGHT) {
                    // do something on right (forward) swipe
                    return true;
                } else if (gesture == Gesture.SWIPE_LEFT) {
                    // do something on left (backwards) swipe
                    finish();
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
        repeatTTS.stop();
        repeatTTS.shutdown();
        speech.stopListening();
        speech.destroy();


    }
}
