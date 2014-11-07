package com.linguar.lessonplan;
import dictionary.Category;
import dictionary.CategoryDictionary;
import dictionary.Word;
import dictionary.dictionary_populator;
import dictionary.Dictionary;

import java.text.SimpleDateFormat;
import java.util.*;

public class ReviewMode {
	
	private CategoryGetter _cGetter =  new CategoryGetter();
	private WordGetter _wGetter = new WordGetter();
	private DailyLessonQuota dQuota = DailyLessonQuota.getInstance();
	private Dictionary _dictionary = Dictionary.getInstance();
	private DisplayWordModeA _displayModeA = new DisplayWordModeA();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddkkmmss");
    private SimpleDateFormat todaysDate = new SimpleDateFormat("yyyyMMdd");
	private final int WAIT_BETWEEN_2_WORDS = 7000; //in milliseconds
    private List<String> englishWords;
	
	public void startLessonPlan() throws Exception
	{
        //Check for any
        dQuota.deserializeLoad();
        Set<String> tempSet = dQuota.wordsShown.keySet();
        if(tempSet!=null) {
            List<String> retrievedWords = new ArrayList<String>();
            for (String s : tempSet) {
                retrievedWords.add(s);
            }
            englishWords = retrievedWords;
        }

        if(englishWords==null) {
            //Call to saved categories
            List<Category> topFiveCategories = _cGetter.getTopFiveCategories();

            //Call to category dictionary
            englishWords = _wGetter.getWordsFromCategoryList(topFiveCategories);
        }

		//Get Spanish Translation
		HashMap<String, Word> wordDictionary= _dictionary.getDictionary();
		
		for (String englishWord : englishWords) {
			Word word = wordDictionary.get(englishWord);
			if(dQuota.wordsShown.containsKey(englishWord))
				if(dQuota.wordsShown.get(englishWord) <3) {
                    _displayModeA.showWord(word.englishWord, word.spanishTranslation);
                    updateWordStats(word);
                }
			
			//Wait for few seconds before showing the next word
			Thread.sleep(WAIT_BETWEEN_2_WORDS);
		}
		
		//Repeat them 3 times and update their repeat count and timestamp
		
		
		//Start off a timer that would keep track of the words that have started.
		
	}

    private void updateWordStats(Word word)
    {
        dQuota.wordsShown.put(word.englishWord,dQuota.wordsShown.get(word.englishWord)+1);
        word.stats.timesShownSinceBeginnning+=1;
        word.stats.lastShown=sdf.format(Calendar.getInstance().getTime());
    }


}
