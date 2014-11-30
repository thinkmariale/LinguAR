package com.linguar.lessonplan;

import com.linguar.dictionary.Dictionary;
import com.linguar.dictionary.Word;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;



/**
 * Created by Adarshkumar Pavani on 08/11/2014.
 */
public class BonusTestGenerator {

    /*
    public enum BonusTestStates{
        clearScreen,
        englishWordDisplayed,
        spanishWordPlayedAndDisplayed
    }
    */

    private CumulativeWordsLearnt _wLearnt = CumulativeWordsLearnt.getInstance();
    private Dictionary _dictionary = Dictionary.getInstance();
   //public  static BonusTestStates BTStates = BonusTestStates.clearScreen;
    //private AlertMessage _alertMessage = new AlertMessage();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddkkmmss");
    private BonusTestQuota _btQuota = BonusTestQuota.getInstance();

    private boolean trytomorrow = false;
    private boolean notEnoughWords = false;
    private List<String> displayWords = new ArrayList<String>();
    private int index = 0;

    public boolean isDone = false;
    //private final int TOTAL_CLOCK_TIME = 60000;
    private final long BONUS_TEST_FREQUENCY = 1000000;

    public void startBonusTest()
    {
        isDone = false;
        List<String> wordsForTest = _wLearnt.wordsLearnt;
        if (wordsForTest == null || wordsForTest.size() < 0) {
           // _alertMessage.showAlertMessage("You need to learn more words before you can unlock this mode");
            notEnoughWords = true;
        }

        else {
            trytomorrow = false;
            notEnoughWords = false;
            if (_btQuota.lastTakenBonusTest == null || _btQuota.lastTakenBonusTest.isEmpty() || Long.getLong(sdf.format(Calendar.getInstance().getTime())) - Long.getLong(sdf.format(_btQuota.lastTakenBonusTest)) >= BONUS_TEST_FREQUENCY) {
                HashMap<String, Word> wordDictionary = _dictionary.getDictionary();

                Collections.shuffle(wordsForTest);

                for (String word : wordsForTest) {
                    displayWords.add(word);

                }
            } else {
               // _alertMessage.showAlertMessage("You may take the bonus test only once per day. Please try again tomorrow");
                _btQuota.lastTakenBonusTest =  sdf.format(Calendar.getInstance().getTime());
                trytomorrow = true;
            }
        }
    }

    public boolean TryTomorrow()
    {
        return  trytomorrow;
    }

    public String getCurrentString()
    {
        if(notEnoughWords)
        {
            isDone = true;
            return "You need to learn more words before you can unlock this mode";
        }
        else if(trytomorrow)
        {
            isDone =true;
            return "You may take the bonus test only once per day. Please try again tomorrow";
        }
        else {
            if(index<this.displayWords.size()) {
                String displayText = displayWords.get(index);
                index++;
                return displayText;
            }
            else
            {
                isDone = true;
                return "End of Test";
            }
        }
    }

    public String displayTranslation(String word)
    {
        return ("The Spanish translation of "+word+" is : " + _dictionary.getDictionary().get(word));
    }
}
