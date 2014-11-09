package com.linguar.lessonplan;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.linguar.dictionary.Dictionary;
import com.linguar.dictionary.Word;


/**
 * Created by Adarshkumar Pavani on 08/11/2014.
 */
public class NormalTestGenerator {

    //The states are used for scoring system in the Response Listener Class
    public enum NormalTestStates{
        clearScreen,
        englishWordDisplayed,
        spanishTranslationDisplayed,
        wordSpoken
    }

    public static NormalTestStates NTStates = NormalTestStates.clearScreen;
    private DisplayWordModeB _modeB =  new DisplayWordModeB();
    private CumulativeWordsLearnt _wLearnt = CumulativeWordsLearnt.getInstance();
    private Dictionary _dictionary = new Dictionary();
    private ResponseListener _rListerner = new ResponseListener();
    public final int WAIT_FOR_TRANSLATED_TEXT = 7000;
    public final int WAIT_FOR_TRANSLATED_SPEECH = 5000;

    void startNormalTest() throws Exception
    {
        List<String> wordsForTest = _wLearnt.wordsLearnt;
        HashMap<String, Word> wordDictionary = _dictionary.getDictionary();

        if(wordsForTest==null)
            return;

        Collections.shuffle(wordsForTest);

        for(String word : wordsForTest)
        {
            _modeB.clearScreen();

            //Show word and wait for few seconds
            _modeB.displayWord(word);
            _modeB.displayTimer(WAIT_FOR_TRANSLATED_TEXT);
            _rListerner.listenAndValidate(wordDictionary.get(word), NTStates);

            _modeB.hideTimer();

            //Show translation and wait for few seconds
            _modeB.displayTranslation(wordDictionary.get(word).spanishTranslation);
            _modeB.displayTimer(WAIT_FOR_TRANSLATED_SPEECH);
            _rListerner.listenAndValidate(wordDictionary.get(word), NTStates);

            _modeB.hideTimer();

            //Speak translation and wait for few seconds
            _modeB.playWord();
            _rListerner.listenAndValidate(wordDictionary.get(word), NTStates);


        }

    }

}
