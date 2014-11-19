package com.linguar.lessonplan;

import com.linguar.dictionary.Dictionary;
import com.linguar.dictionary.Word;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;



/**
 * Created by Adarshkumar Pavani on 08/11/2014.
 */
public class BonusTestGenerator {

    public enum BonusTestStates{
        clearScreen,
        englishWordDisplayed,
        spanishWordPlayedAndDisplayed
    }
    private DisplayWordModeC _modeC = new DisplayWordModeC();
    private CumulativeWordsLearnt _wLearnt = CumulativeWordsLearnt.getInstance();
    private Dictionary _dictionary = Dictionary.getInstance();
    ResponseListener _rListerner = new ResponseListener();
    public  static BonusTestStates BTStates = BonusTestStates.clearScreen;
    private AlertMessage _alertMessage = new AlertMessage();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddkkmmss");
    private BonusTestQuota _btQuota = BonusTestQuota.getInstance();

    private final int TOTAL_CLOCK_TIME = 60000;
    private final long BONUS_TEST_FREQUENCY = 1000000;

    public void startBonusTest()
    {

        List<String> wordsForTest = _wLearnt.wordsLearnt;
        if (wordsForTest.size() < 30) {
            _alertMessage.showAlertMessage("You need to learn more words before you can unlock this mode");
        }

        else {
            if (Long.getLong(sdf.format(Calendar.getInstance().getTime())) - Long.getLong(sdf.format(_btQuota.lastTakenBonusTest)) >= BONUS_TEST_FREQUENCY) {
                HashMap<String, Word> wordDictionary = _dictionary.getDictionary();

                Collections.shuffle(wordsForTest);

                for (String word : wordsForTest) {
                    _modeC.clearScreen();
                    _modeC.displayWord(word);
                    _modeC.displayTimer(TOTAL_CLOCK_TIME);
                    boolean flag = _rListerner.listenAndValidate(wordDictionary.get(word), BTStates);

                    if (!flag) {
                        _modeC.clearScreen();
                        _alertMessage.showAlertMessage("End of test");
                        break;
                    }
                }
            } else {
                _alertMessage.showAlertMessage("You may take the bonus test only once per day. Please try again tomorrow");
            }
        }


    }
}
