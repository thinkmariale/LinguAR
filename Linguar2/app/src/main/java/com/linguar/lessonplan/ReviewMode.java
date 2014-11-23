package com.linguar.lessonplan;

import android.util.Log;

import com.linguar.dictionary.Dictionary;
import com.linguar.dictionary.Word;
import com.linguar.dictionary.Category;


import java.text.SimpleDateFormat;
import java.util.*;


public class ReviewMode {

	private CategoryGetter _cGetter =  new CategoryGetter();
	private WordGetter _wGetter = new WordGetter();
	private DailyLessonQuota dQuota = DailyLessonQuota.getInstance();
    private CumulativeWordsLearnt wLearnt =  CumulativeWordsLearnt.getInstance();
	private Dictionary _dictionary = Dictionary.getInstance();
    private ScoreKeeper _skeeper =  ScoreKeeper.getInstance();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddkkmmss");
	//private final int WAIT_BETWEEN_2_WORDS = 7000; //in milliseconds
    private final int SCORE_FOR_LEARNING = 100;
    private int index = 0;
    private List<String> englishWords;
    private List<String> displayWords;
	//private int timesDisplayed = 0;
    public boolean isDone = false;

	public void startLessonPlan() throws Exception {
        this.displayWords = new ArrayList<String>();
        isDone = false;

        //Get words from the daily lesson plan
        //englishWords = dQuota.resolveDatesAndReturnWords();
        System.out.println(englishWords);

        //If the lesson plan is opened on a new day or, if it is being used for the first time, the system will load 14 words from top 5 categories
        if (englishWords == null || englishWords.size()==0) {
            //Call to saved categories
            System.out.println("English Word list found null. Getting Top Five Categories");
            List<Category> topFiveCategories = _cGetter.getTopFiveCategories();
            System.out.println("Number of categories fetched: "+topFiveCategories.size());

            for(Category cat : topFiveCategories) {
                System.out.println(cat.category);
            }
            //Call to category dictionary
            englishWords = _wGetter.getWordsFromCategoryList(topFiveCategories);
        }

        //Get Spanish Translation
        HashMap<String, Word> wordDictionary = _dictionary.getDictionary();

            for(int i = 0; i<dQuota.NO_OF_TIMES_PER_WORD; i++) {

                for (String englishWord : englishWords) {
                   // if (dQuota.getShownWordHashMap(englishWord).get(englishWord) < dQuota.NO_OF_TIMES_PER_WORD) {
                        Word word = wordDictionary.get(englishWord);
                        displayWords.add(word.englishWord + " : " + word.spanishTranslation);
                        Log.d("RM", word.englishWord + " : " + word.spanishTranslation);
                        updateWordStats(word);

                        //Adding the words to the hashmap of learnt words and updating the score
                        if(!wLearnt.wordsLearnt.contains(word.englishWord)) {
                            wLearnt.wordsLearnt.add(word.englishWord);
                            _skeeper.score += SCORE_FOR_LEARNING;
                     //   }
                    }
                }
                Collections.shuffle(englishWords); //Shuffle after every one round of words
            }
/*
        if(timesDisplayed==0) //If even one word was shown, the timesDisplayed would be greater than 0. Therefore, timesDisplayed is used to check if the lesson plan was executed
        {
            AlertMessage alert =  new AlertMessage();
            alert.showAlertMessage("You've finished the lesson plan for today. Please revisit tomorrow to learn more words.");
        }
        */

    }

    private void updateWordStats(Word word)
    {
        if(dQuota.getShownWordHashMap(word.englishWord).containsKey(word.englishWord)) // This is when the list of words are picked from the daily lesson plan. Incrementing the number of times the word has been shown that day
            dQuota.putWordInWordsShown(word.englishWord, dQuota.getShownWordHashMap(word.englishWord).get(word.englishWord) + 1);

        else
            dQuota.putWordInWordsShown(word.englishWord, 1); // This is when a fresh list of words is picked up instead of a saved daily lesson list. Since it's the first time, putting the count of times shown as 1

            word.stats.timesShownSinceBeginnning+=1;
            word.stats.lastShown=sdf.format(Calendar.getInstance().getTime());
        }

    public String getCurrentString()
    {
        if(this.displayWords.size()>0) {
            if(index<this.displayWords.size()) {
                String displayText = this.displayWords.get(index);
                this.index++;
                return displayText;
            }
            else {
                isDone = true;
                return "You've finished the lesson plan for today. Please revisit tomorrow to learn more words.";
            }
        }

        isDone = true;
        return "You've finished the lesson plan for today. Please revisit tomorrow to learn more words.";

    }

}
