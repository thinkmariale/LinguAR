package com.linguar.lessonplan;

import com.linguar.dictionary.*;

/**
 * Created by Adarshkumar Pavani on 09/11/2014.
 */
public class ResponseListener {

    private final int FIRSTPASS_CORRECT_SCORE = 500;
    private final int FIRSTPASS_WRONG_SCORE = 200;

    private final int SECONDPASS_CORRECT_SCORE = 100;
    private final int SECONDPASS_WRONG_SCORE = 200;

    private final int THIRDPASS_SCORE = 200;

    private CongratulationsPopup _congratsPopup = new CongratulationsPopup();
    private ScoreKeeper sKeeper = ScoreKeeper.getInstance();
    public void listenAndValidate(Word word, NormalTestGenerator.NormalTestStates NTState)
    {
        if(NTState == NormalTestGenerator.NormalTestStates.englishWordDisplayed)
        {
            //+500 for right answer, -200 for wrong answer

        }

        else if(NTState == NormalTestGenerator.NormalTestStates.spanishTranslationDisplayed)
        {
            //+300 for right answer, -200 for wrong answer

        }

        else if(NTState == NormalTestGenerator.NormalTestStates.wordSpoken)
        {
            //-100 for right pronunciation, -300 for wrong pronunciation

        }
    }

    public boolean listenAndValidate(Word word, BonusTestGenerator.BonusTestStates BTState) {
        boolean answer = false;
        if(BTState == BonusTestGenerator.BonusTestStates.englishWordDisplayed)
        {
            //+1000 points for the right answer

        }

        return answer;
    }

}
