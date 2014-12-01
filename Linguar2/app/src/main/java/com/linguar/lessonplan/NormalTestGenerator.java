package com.linguar.lessonplan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.linguar.dictionary.Dictionary;
import com.linguar.dictionary.Word;


/**
 * Created by Adarshkumar Pavani on 08/11/2014.
 */
public class NormalTestGenerator {


    private CumulativeWordsLearnt _wLearnt = CumulativeWordsLearnt.getInstance();
    private Dictionary _dictionary = Dictionary.getInstance();
    private HashMap<String, String> wordTranslations = new HashMap<String,String>();
    //private ResponseListener _rListerner = new ResponseListener();

    //private AlertMessage _alertMessage = new AlertMessage();
    //public final int WAIT_FOR_TRANSLATED_TEXT = 7000;
    //public final int WAIT_FOR_TRANSLATED_SPEECH = 5000;
    //public final int WAIT_FOR_USER_REPEAT = 5000;
    public final int NO_OF_WORDS_PER_TEST = 10;
    public boolean isDone = false;
    private int index = 0;
    private List<String> displayWords = new ArrayList<String>();

    public void startNormalTest() throws Exception
    {
        isDone = false;
        List<String> wordsForTest = _wLearnt.wordsLearnt;
        HashMap<String, Word> wordDictionary = _dictionary.getDictionary();

        if(wordsForTest==null)
            return;

        List<String> subsetWords = new ArrayList<String>();

        if(wordsForTest.size() <= NO_OF_WORDS_PER_TEST) {
            subsetWords = wordsForTest;
        }
        else {
            List<Integer> indicesFound = new ArrayList<Integer>();
            Random rand = new Random();
            while (subsetWords.size() != NO_OF_WORDS_PER_TEST) {
                int random = rand.nextInt(wordsForTest.size());
                if(indicesFound.contains(random))
                    continue;
                else
                {
                    subsetWords.add(wordsForTest.get(random));
                    indicesFound.add(random);
                }
            }
        }

        Collections.shuffle(subsetWords);
        System.out.println("The list of words for the normal test are: " + subsetWords);

        for(String word : subsetWords)
        {
            //_modeB.clearScreen();

            //Show word and wait for few seconds
            wordTranslations.put(word, wordDictionary.get(word).spanishTranslation);
           // _modeB.displayTimer(WAIT_FOR_TRANSLATED_TEXT);
           // _rListerner.listenAndValidate(wordDictionary.get(word), NTStates);

            //_modeB.hideTimer();

            //Show translation and wait for few seconds
           // _modeB.displayTimer(WAIT_FOR_TRANSLATED_SPEECH);
            //_rListerner.listenAndValidate(wordDictionary.get(word), NTStates);

           // _modeB.hideTimer();

            //Speak translation and wait for few seconds
          //  _modeB.playWord();
            //_modeB.displayTimer(WAIT_FOR_USER_REPEAT);
           // _rListerner.listenAndValidate(wordDictionary.get(word), NTStates);
        }

        Set<String> englishWords = wordTranslations.keySet();
        for(String englishWord : englishWords)
        {
            displayWords.add(englishWord);
        }


    //_alertMessage.showAlertMessage("End of Test");
    }

    public String getCurrentWord()
    {

        if(index < displayWords.size()) {
            String displayText = displayWords.get(index);
            index++;
            return displayText;
        }
        else
        {
            isDone = true;
            return "End of test";
        }
    }

    public String getCurrentTranslation(String englishword)
    {
        if(wordTranslations.containsKey(englishword))
            return wordTranslations.get(englishword);
        else
            return null;
    }

}
