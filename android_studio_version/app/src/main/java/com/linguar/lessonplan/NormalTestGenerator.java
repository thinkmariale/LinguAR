package com.linguar.lessonplan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import dictionary.Dictionary;
import dictionary.Word;

/**
 * Created by Adarshkumar Pavani on 08/11/2014.
 */
public class NormalTestGenerator {

    private DisplayWordModeB _modeB =  new DisplayWordModeB();
    private CumulativeWordsLearnt _wLearnt = CumulativeWordsLearnt.getInstance();
    private Dictionary _dictionary = new Dictionary();
    public final int WAIT_FOR_TRANSLATED_TEXT = 7000;
    public final int WAIT_FOR_TRANSLATED_SPEECH = 5000;

    void startNormalTest() throws Exception
    {
        List<String> wordsForTest = new ArrayList<String>();
        wordsForTest = _wLearnt.wordsLearnt;
        HashMap<String, Word> wordDictionary = _dictionary.getDictionary();

        if(wordsForTest==null)
            return;

        Collections.shuffle(wordsForTest);

        for(String word : wordsForTest)
        {
            _modeB.displayWord(word);
            _modeB.displayTimer(WAIT_FOR_TRANSLATED_TEXT);
            _modeB.hideTimer();

            _modeB.displayTranslation(wordDictionary.get(word).spanishTranslation);
            _modeB.displayTimer(WAIT_FOR_TRANSLATED_SPEECH);
            _modeB.hideTimer();




        }

    }
}
