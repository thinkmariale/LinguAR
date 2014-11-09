package com.linguar.lessonplan;

/**
 * Created by Adarshkumar Pavani on 09/11/2014.
 */
public class DisplayWordModeB {

    void displayWord(String word)
    {
        NormalTestGenerator.NTStates = NormalTestGenerator.NormalTestStates.englishWordDisplayed;
        System.out.println(word);
    }

    void displayTranslation(String translation)
    {
        NormalTestGenerator.NTStates = NormalTestGenerator.NormalTestStates.spanishTranslationDisplayed;
        System.out.println("The translation is" +" : " + translation);
    }

    void displayTimer(int time)
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
        NormalTestGenerator.NTStates = NormalTestGenerator.NormalTestStates.clearScreen;
    }
}
