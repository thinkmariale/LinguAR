package com.linguar.lessonplan;

/**
 * Created by idealboy on 09/11/2014.
 */
public class DisplayWordModeC {

    void displayWord(String word)
    {
        BonusTestGenerator.BTStates = BonusTestGenerator.BonusTestStates.englishWordDisplayed;
        System.out.println(word);
    }

    void displayTranslation(String translation)
    {
        BonusTestGenerator.BTStates = BonusTestGenerator.BonusTestStates.spanishWordPlayedAndDisplayed;
        System.out.println("The translation is" +" : " + translation);
        playWord();
    }

    void displayTimer(int time) //  Time in milliseconds
    {
    }

    void hideTimer()
    {

    }

    void playWord()
    {
        displaySpeakingSymbol();



        NormalTestGenerator.NTStates = NormalTestGenerator.NormalTestStates.wordSpoken;
    }

    void displaySpeakingSymbol()
    {

    }

    void clearScreen()
    {
        BonusTestGenerator.BTStates = BonusTestGenerator.BonusTestStates.clearScreen;
    }
}
