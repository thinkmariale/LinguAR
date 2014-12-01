package com.linguar;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.glass.app.Card;
import com.google.android.glass.media.Sounds;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.linguar.dictionary.CategoryDictionary;
import com.linguar.dictionary.Dictionary;
import com.linguar.lessonplan.BonusTestGenerator;
import com.linguar.lessonplan.LessonPlan;
import com.linguar.lessonplan.NormalTestGenerator;
import com.linguar.lessonplan.ReviewMode;
import com.linguar.lessonplan.ScoreKeeper;
import com.linguar.serialization.Serialization;

import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Locale;

public class LessonActivity extends Activity implements
        RecognitionListener, TextToSpeech.OnInitListener {

    //Text To Speech instance
    private TextToSpeech repeatTTS;

    //Spanish Locale
    private Locale spa;

    private GestureDetector mGestureDetector;
    private LessonPlan lessonPlan;

    private TextView testWordA;
    private TextView testWordB;
    private TextView messageText;
    private TextView returnedText;
    private ToggleButton toggleButton;

    private ReviewMode _reviewMode;
    private NormalTestGenerator _normalTest;
    private BonusTestGenerator _bonusTest;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private String LOG_TAG = "LessonActivity";
    private Serialization serialization;
    private String filePath;
    private String filePath1;
    private String filePath2;
    private int counter = 0;

    private ScoreKeeper _skeeper = ScoreKeeper.getInstance();

    private int curLesson;
    private int numLessons = 3;
    private enum NormalTestStates {showEnglishWord, showTranslation, speakWord};
    private enum BonusTestStates {showEnglishWord, answerWrong};

    private NormalTestStates ts = NormalTestStates.showEnglishWord;
    private BonusTestStates bs = BonusTestStates.showEnglishWord;
    private Thread t = new Thread();
    private boolean waitForTranslate = false;
    private boolean waitForSpeech = false;
    private int tick = 0;
    private boolean didTalk = true;
    private boolean isCorrect = false;
    private boolean testDone  = false;
    private String testMe;

    private boolean testStarted = false;
    private boolean lessonStarted = true;
    private boolean bonusStarted = false;
    private boolean inTest = false;
    private boolean didDing = false;
    private Card testIntroCard;
    private Card lessonIntroCard;
    private Card bonusIntroCard;
    private int currentCardIndex = 0;
    private ArrayList<Card> menuCards = new ArrayList<Card>();
    AudioManager am;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        repeatTTS = new TextToSpeech(this, this);
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        //monster friend intro cards
        lessonIntroCard = new Card(this);
        lessonIntroCard.setImageLayout(Card.ImageLayout.FULL);
        lessonIntroCard.addImage(R.drawable.review_start);
        menuCards.add(lessonIntroCard);

        testIntroCard = new Card(this);
        testIntroCard.setImageLayout(Card.ImageLayout.FULL);
        testIntroCard.addImage(R.drawable.test_start);
        menuCards.add(testIntroCard);

        bonusIntroCard = new Card(this);
        bonusIntroCard.setImageLayout(Card.ImageLayout.FULL);
        bonusIntroCard.addImage(R.drawable.bonus_start);
        menuCards.add(bonusIntroCard);

        testMe    = "";
        curLesson = 0;
        tick = 500;

        Dictionary d = Dictionary.getInstance();
        System.out.println("Lesson Plan Passed Yeah " + d.getDictionary().size());

        mGestureDetector = createGestureDetector(this);
        serialization = new Serialization();
        lessonPlan = LessonPlan.getInstance();

        filePath  = getFilesDir().getPath().toString()  + "/LessonPlan.ser";
        filePath1 = getFilesDir().getPath().toString() + "/dictionary.ser";
        filePath2 = getFilesDir().getPath().toString() + "/cat_dictionary.ser";

        serialization.<LessonPlan>loadData_(lessonPlan,filePath);

        setContentView(R.layout.activity_lesson);

        testWordA = (TextView) findViewById(R.id.textViewA);
        testWordB = (TextView) findViewById(R.id.textViewB);
        messageText = (TextView) findViewById(R.id.messageView);
        returnedText = (TextView) findViewById(R.id.textView3);
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton1);

        //testWordA.setText("Review Mode!");
        //testWordB.setText(" ");

        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "es-ES");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);

        try {

            _reviewMode = new ReviewMode();
            _normalTest = new NormalTestGenerator();
            _bonusTest = new BonusTestGenerator();
            System.out.println("Lesson Plan Passed Yeah " +d.getDictionary().size());
           //startLesson();
           //  curLesson = 1;
           // _normalTest.startNormalTest();
            //_reviewMode.startLessonPlan();
        }
        catch(Exception e)
        {
            System.out.println("Lesson Plan Failed");
            e.printStackTrace();
        }

        setContentView(lessonIntroCard.getView());
    }

    private void attachToggle(int view){
        setContentView(view);
        testWordA = (TextView) findViewById(R.id.textViewA);
        testWordB = (TextView) findViewById(R.id.textViewB);
        returnedText = (TextView) findViewById(R.id.textView3);
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton1);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    // progressBar.setVisibility(View.VISIBLE);
                    //progressBar.setIndeterminate(true);
                    speech.startListening(recognizerIntent);
                    System.out.println("listening");
                } else {
                    //progressBar.setIndeterminate(false);
                    //progressBar.setVisibility(View.INVISIBLE);
                    speech.stopListening();
                    System.out.println("not listening");
                }
            }
        });
    }

    private void startThread(){
        t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // update TextView here!
                                if(curLesson == 0)
                                {
                                    String str = _reviewMode.getCurrentString();
                                    if(!_reviewMode.isDone) {
                                        String[] tmp = str.split(" ");
                                        testWordA.setText(tmp[0]);
                                        testWordB.setText(tmp[2]);
                                        //TODO TTS: Repeat the word during review mode
                                        repeatTTS.speak((testWordB.getText()).toString(), TextToSpeech.QUEUE_FLUSH, null);
                                    }
                                    else{
                                        //set view to new layout card
                                        setContentView(R.layout.message_layout);
                                        messageText = (TextView) findViewById(R.id.messageView);
                                        messageText.setText(str);
                                    }
                                }
                                if(curLesson == 1)
                                {
                                    if(_normalTest.isDone)
                                        testDone = true;
                                    if(ts == NormalTestStates.showEnglishWord) {
                                        if(didTalk && toggleButton.isChecked()) {
                                            if(!isCorrect) {
                                                didTalk = false;
                                                String str = _normalTest.getCurrentWord();
                                                if(!_normalTest.isDone) {
                                                    returnedText.setText("");
                                                    testWordA.setText("Translate: " + str);
                                                    System.out.println("gets here!!!!!!!!!!!!!!!!!!!!!!!!!");
                                                    //Show the count down timer for 7 seconds
                                                    testMe = _normalTest.getCurrentTranslation(str);
                                                }
                                                else{
                                                    //set view to new layout card
                                                    setContentView(R.layout.message_layout);
                                                    messageText = (TextView) findViewById(R.id.messageView);
                                                    messageText.setText(str);

                                                }
                                            }
                                        }
                                        if(isCorrect)
                                        {
                                            //show "CORRECT" for some sec. then start lesson again
                                            //TODO Play a ding sound
                                            System.out.println("Correct2");
                                            if(!didDing) {
                                                am.playSoundEffect(Sounds.SUCCESS);
                                                didDing = true;
                                            }
                                            if(counter >= 4) {
                                                isCorrect = false;
                                                toggleButton.toggle();
                                                counter = 0;
                                                didDing = false;
                                            }
                                        }

                                    }
                                    else if(ts == NormalTestStates.showTranslation)
                                    {
                                            if(!_normalTest.isDone) {
                                                testWordA.setText("Try Again: " + testMe);
                                                returnedText.setText("");
                                                didTalk = false;
                                        }

                                        if(isCorrect)
                                        {
                                            //show "CORRECT" for some sec. then start lesson again
                                            //TODO Play a ding sound
                                            System.out.println("Correct3");
                                            if(!didDing) {
                                                am.playSoundEffect(Sounds.SUCCESS);
                                                didDing = true;
                                            }
                                            if(counter >= 4) {
                                                isCorrect = false;
                                                toggleButton.toggle();
                                                counter = 0;
                                                didDing = false;
                                            }
                                        }
                                        else
                                        {
                                            //show "CORRECT" for some sec. then start lesson again
                                            if(counter >= 4) {
                                                didTalk = true;
                                                counter = 0;
                                                didDing = false;
                                            }
                                        }
                                    }
                                    else if(ts == NormalTestStates.speakWord)
                                    {
                                        if(didTalk && !toggleButton.isChecked()) {
                                            testWordA.setText("Please Repeat: " + testMe);
                                            //TODO TTS: Repeat the word in Spanish
                                            repeatTTS.speak(testMe.toString(), TextToSpeech.QUEUE_FLUSH, null);
                                            //TODO This text should be replaced by a speaker icon, whenever speech is being played
                                            returnedText.setText("");
                                            didTalk = false;
                                        }
                                    }
                                    //increment counter to know in what part of the timer we are in
                                    counter++;
                                }
                                if(curLesson == 2)
                                {
                                    if(_bonusTest.isDone)
                                        testDone = true;

                                    if(didTalk && toggleButton.isChecked()) {
                                        if(!isCorrect && !_bonusTest.TryTomorrow()) {
                                            didTalk = false;
                                            String str = _bonusTest.getCurrentString();
                                            testMe = _bonusTest.displayTranslation(str);
                                            testWordA.setText("Translate: " + str);
                                        }
                                        else if(!isCorrect)//TODO Check with maria if this should be TryTomorrow() or isCorrect
                                        {
                                            didTalk = false;
                                            //testWordA.setText("Try Tomorrow");
                                            //returnedText.setText("");
                                            //set view to new layout card
                                            setContentView(R.layout.message_layout);
                                            messageText = (TextView) findViewById(R.id.messageView);
                                            messageText.setText("Try Again Tomorrow");
                                        }
                                    }
                                    if(isCorrect)
                                    {
                                        //show "CORRECT" for some sec. then start lesson again
                                        //TODO Play Ding sound
                                        System.out.println("Correct1");
                                        if(!didDing) {
                                            am.playSoundEffect(Sounds.SUCCESS);
                                            didDing = true;
                                        }
                                        if(counter >= 4) {
                                            isCorrect = false;
                                            toggleButton.toggle();
                                            counter = 0;
                                            didDing = false;
                                        }
                                    }
                                }//end curLessons
                            }
                        });
                        Thread.sleep(tick);
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();

    }

    private void startLesson()
    {
        try {
            testDone = false;
            if (curLesson == 0)
                tick = 7000;
                _reviewMode.startLessonPlan();

            if (curLesson == 1) {
                tick = 50;
                _normalTest.startNormalTest();
            }
            if (curLesson == 2) {
                tick = 50;
                _bonusTest.startBonusTest();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "Resume");
    }

    @Override
    protected void onPause() {
        super.onPause();

        serialization.<Dictionary>saveData_(Dictionary.getInstance(),filePath1);
        serialization.<CategoryDictionary>saveData_(CategoryDictionary.getInstance(), filePath2);
        serialization.<LessonPlan>saveData_(lessonPlan, filePath);

        if (speech != null) {
            speech.destroy();
            toggleButton.setChecked(false);
            Log.i(LOG_TAG, "destroy");
        }

    }
    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech");
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(LOG_TAG, "onBufferReceived: " + buffer);
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech");
        toggleButton.setChecked(false);
    }

    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        Log.d(LOG_TAG, "FAILED " + errorMessage);
        if(errorCode == SpeechRecognizer.ERROR_SPEECH_TIMEOUT)
        {
            //showing translation text if user didnt talk
            toggleButton.setChecked(false);
            returnedText.setText(testMe);
            didTalk = true;
        }
        else if(errorCode != SpeechRecognizer.ERROR_CLIENT ) {
            returnedText.setText(errorMessage);
            toggleButton.setChecked(false);
        }
       //toggleButton.setChecked(false);
    }

    @Override
    public void onEvent(int arg0, Bundle arg1) {
        Log.i(LOG_TAG, "onEvent");
    }

    @Override
    public void onPartialResults(Bundle arg0) {
        Log.i(LOG_TAG, "onPartialResults ");
    }

    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.i(LOG_TAG, "onReadyForSpeech");
    }

   @Override
    public void onResults(Bundle results) {

       if(testDone){
           //set view to new layout card
           setContentView(R.layout.message_layout);
           messageText = (TextView) findViewById(R.id.messageView);
           messageText.setText("Try Tomorrow");
           //testWordA.setText("Try Tomorrow");
           //returnedText.setText("");
           return;
       }

       String text = "";
      /* ArrayList<String> voiceResults = results.getStringArrayList("results_recognition");
       if (voiceResults == null) {
           Log.e(LOG_TAG, "No voice results");
           ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

           for (String result : matches)
               text += result + "\n";
       } else {
           Log.d(LOG_TAG, "Printing matches: ");
           for (String match : voiceResults) {
               Log.d(LOG_TAG, match);
               text += match;
           }
       }*/

       ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
       boolean pass = false;
       testMe = testMe.trim();
       for (String result : matches) {
           result = result.toLowerCase().trim();
           if(result.contentEquals(testMe) ||  result.contains(testMe) )
               pass = true;
           text += result + "\n";
       }

       counter = 0;
       didTalk = true;
        //texts is what the user said
       if(curLesson == 1 || curLesson == 2) {
           Log.i(LOG_TAG, "onResults " + " " + text);
           String answer = text;
           text = text.toLowerCase().trim();

           if (pass) {
               answer = "CORRECT!!";
               //Adding score for correct answer
               if (curLesson == 1)
               {
                   if (ts == NormalTestStates.showEnglishWord)
                       _skeeper.score += 500;
                   else if (ts == NormalTestStates.showTranslation)
                       _skeeper.score += 300;
                   else if (ts == NormalTestStates.speakWord)
                       _skeeper.score -= 100;

                   ts = NormalTestStates.showEnglishWord;
                   //start with next word
                   isCorrect = true;
               }
               else if(curLesson == 2)
                     _skeeper.score += 1000;
           }
           else //Wrong answer
           {
               //TODO Play wrong sound here
               am.playSoundEffect(Sounds.DISALLOWED);
               if (curLesson == 1)
               {
                   testWordA.setText("Wrong: " + testMe);
                   isCorrect = false;
                   if (ts == NormalTestStates.showEnglishWord) {
                       _skeeper.score -= 200;
                       didTalk = false;
                       ts = NormalTestStates.showTranslation;
                   }
                   else if (ts == NormalTestStates.showTranslation) {
                       _skeeper.score -= 200;
                       ts = NormalTestStates.speakWord;
                   }
                   else if (ts == NormalTestStates.speakWord) {
                       _skeeper.score -= 300;
                       ts = NormalTestStates.showEnglishWord;
                   }
               }
               if(curLesson == 2)
               {
                   testWordA.setText("Wrong: " + testMe);
                   //TODO Speak the word <testMe>
                   repeatTTS.speak(testMe.toString(), TextToSpeech.QUEUE_FLUSH, null);
                   isCorrect = false;
                   testDone  = true;
                   _bonusTest.isDone = true;
               }
           }
           returnedText.setText(answer);
       }
       else
           returnedText.setText(text);
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        // Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
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
                    am.playSoundEffect(Sounds.TAP);
                    if(inTest) {
                        toggleButton.toggle();
                    }
                    else {
                        if (currentCardIndex == 0) {
                            curLesson = 0;
                            //start review
                            attachToggle(R.layout.activity_lesson);
                        } else if (currentCardIndex == 1) {
                            curLesson = 1;
                            //start test
                            attachToggle(R.layout.activity_test);
                            toggleButton.toggle();
                            inTest = true;
                        }
                        /*else if (currentCardIndex == 2) {
                            curLesson = 2;
                            //start bonus
                            attachToggle(R.layout.activity_test);
                            toggleButton.toggle();
                            inTest = true;
                        }*/
                        startLesson();
                        startThread();
                    }

                    return true;
                } else if (gesture == Gesture.TWO_TAP) {
                    // do something on two finger tap
                    return true;
                } else if (gesture == Gesture.SWIPE_RIGHT) {
                    // do something on right (forward) swipe
                    am.playSoundEffect(Sounds.SELECTED);
                    inTest = false;
                    if(currentCardIndex < 1) {
                        currentCardIndex++;
                        setContentView(menuCards.get(currentCardIndex).getView());
                    }
                    return true;
                } else if (gesture == Gesture.SWIPE_LEFT) {
                    am.playSoundEffect(Sounds.SELECTED);
                    // do something on left (backwards) swipe
                    inTest = false;
                    if(currentCardIndex > 0) {
                        currentCardIndex--;
                        setContentView(menuCards.get(currentCardIndex).getView());
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

    /*
     * Send generic motion events to the gesture detector
     */
    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        /*
        //open up the general menu
        if(testStarted) {
            setContentView(testIntroCard.getView());
            testStarted = false;
            return true;
        }
        else if(lessonStarted) {
            setContentView(lessonIntroCard.getView());
            lessonStarted = false;
            return true;
        }
        else if(bonusStarted) {
            setContentView(bonusIntroCard.getView());
            bonusStarted = false;
            return true;
        }
        */
        //else{
            if (mGestureDetector != null) {
                return mGestureDetector.onMotionEvent(event);
            }
            return false;
        //}
    }

    @Override
    protected void onDestroy()
    {
        repeatTTS.stop();
        repeatTTS.shutdown();
        _reviewMode.isDone = true;
        _normalTest.isDone = true;
        _bonusTest.isDone = true;

        super.onDestroy();
        speech.stopListening();
        speech.destroy();

        serialization.<Dictionary>saveData_(Dictionary.getInstance(),filePath1);
        serialization.<CategoryDictionary>saveData_(CategoryDictionary.getInstance(), filePath2);
        serialization.<LessonPlan>saveData_(lessonPlan, filePath);

        inTest = false;

        finish();
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
}
