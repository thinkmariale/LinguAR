package com.linguar.lessonplan;
import dictionary.Category;
import dictionary.Word;
import dictionary.Dictionary;

import java.text.SimpleDateFormat;
import java.util.*;


public class ReviewMode {

	private CategoryGetter _cGetter =  new CategoryGetter();
	private WordGetter _wGetter = new WordGetter();
	private DailyLessonQuota dQuota = DailyLessonQuota.getInstance();
    private CumulativeWordsLearnt wLearnt =  CumulativeWordsLearnt.getInstance();
	private Dictionary _dictionary = Dictionary.getInstance();
    private ScoreKeeper _skeeper =  ScoreKeeper.getInstance();
	private DisplayWordModeA _displayModeA = new DisplayWordModeA();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddkkmmss");
	private final int WAIT_BETWEEN_2_WORDS = 7000; //in milliseconds
    private final int SCORE_FOR_LEARNING = 100;
    private List<String> englishWords;
	private int timesDisplayed = 0;

	public void startLessonPlan() throws Exception {
        //Get words from the daily lesson plan
        englishWords = dQuota.resolveDatesAndReturnWords();

        //If the lesson plan is opened on a new day or, if it is being used for the first time, the system will load 14 words from top 5 categories
        if (englishWords == null) {
            //Call to saved categories
            List<Category> topFiveCategories = _cGetter.getTopFiveCategories();

            //Call to category dictionary
            englishWords = _wGetter.getWordsFromCategoryList(topFiveCategories);
        }

        //Get Spanish Translation
        HashMap<String, Word> wordDictionary = _dictionary.getDictionary();

            for(int i = 0; i<dQuota.NO_OF_TIMES_PER_WORD; i++) {

                for (String englishWord : englishWords) {
                    if (dQuota.wordsShown.get(englishWord) < dQuota.NO_OF_TIMES_PER_WORD) {
                        Word word = wordDictionary.get(englishWord);
                        _displayModeA.showWord(word.englishWord, word.spanishTranslation);
                        updateWordStats(word);

                        //Adding the words to the hashmap of learnt words and updating the score
                        if(!wLearnt.wordsLearnt.contains(word.englishWord)) {
                            wLearnt.wordsLearnt.add(word.englishWord);
                            _skeeper.score += SCORE_FOR_LEARNING;
                        }


                        timesDisplayed++;
                        //Wait for few seconds before showing the next word
                        Thread.sleep(WAIT_BETWEEN_2_WORDS);
                    }
                }
                Collections.shuffle(englishWords); //Shuffle after every one round of words
            }

        if(timesDisplayed==0)
        {
            AlertMessage alert =  new AlertMessage();
            alert.showAlertMessage("You've finished the lesson plan for today. Please revisit tomorrow to learn more words.");
        }

    }

    private void updateWordStats(Word word)
    {
        if(dQuota.wordsShown.containsKey(word.englishWord)) // This is when the list of words are picked from the daily lesson plan
        dQuota.wordsShown.put(word.englishWord, dQuota.wordsShown.get(word.englishWord) + 1);

        else
        dQuota.wordsShown.put(word.englishWord, 1); // This is when a fresh list of words is picked up instead of a saved daily lesson list

        word.stats.timesShownSinceBeginnning+=1;
        word.stats.lastShown=sdf.format(Calendar.getInstance().getTime());
    }

}
