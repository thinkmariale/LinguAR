package com.linguar.lessonplan;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class DailyLessonQuota implements Serializable {

    private static DailyLessonQuota _instance = new DailyLessonQuota();

    public static DailyLessonQuota getInstance() {
        return _instance;
    }

    private SimpleDateFormat todaysDate = new SimpleDateFormat("yyyyMMdd");

    public String lastUsedReviewMode;
    public  final int NO_OF_WORDS_TO_BE_SHOWN_PER_DAY = 7;
    public  final int NO_OF_TIMES_PER_WORD = 3;

    //Serialized 14 Words
    public HashMap<String, Integer> wordsShown;
/*
    public HashMap<String, Integer> getShownWordHashMap(String word)
    {
       if(this.wordsShown.containsKey(word)) {
       return this.wordsShown;
       }
        else
       {
           this.wordsShown.put(word, 0);
           return wordsShown;
       }
    }

    public void putWordInWordsShown(String key, int value) {
        this.wordsShown.put(key,value);
    }

    public List<String> resolveDatesAndReturnWords()
    {
        List<String> englishWords =  new ArrayList<String>();
        if (todaysDate.format(Calendar.getInstance().getTime()).equals(this.lastUsedReviewMode))
        {
            System.out.println("Pulling words from the same day");
            Set<String> tempSet = this.wordsShown.keySet();

            if(tempSet!=null) {
                List<String> retrievedWords = new ArrayList<String>();
                for (String s : tempSet) {
                    retrievedWords.add(s);
                }
                englishWords = retrievedWords;
            }
            else
            {
                System.out.println("List of retrieved words from today's lesson plan is null");
            }
        }
        else
        {
            if(this.lastUsedReviewMode==null)
                System.out.println("Using Review Mode for the first time");
            else
                System.out.println("Pulling words for a different day");

            //Setting new date and resetting the word list to null
            this.wordsShown = new HashMap<String, Integer>();
            this.lastUsedReviewMode = todaysDate.format(Calendar.getInstance().getTime());
        }

        return englishWords;
    }
*/
}
