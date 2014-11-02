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
	private DailyLessonQuota dQuota = new DailyLessonQuota();
	private Dictionary _dictionary = Dictionary.getInstance();
	private DisplayWordModeA _displayModeA = new DisplayWordModeA();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddkkmmss");
	private final int WAIT_BETWEEN_2_WORDS = 7000; //in milliseconds
	
	public void startLessonPlan() throws Exception
	{
		//Call to saved categories
		List<Category> topFiveCategories = _cGetter.getTopFiveCategories();
		
		//Call to category dictionary
		List<String> englishWords = _wGetter.getWordsFromCategoryList(topFiveCategories);
		
		//Get Spanish Translation
		HashMap<String, Word> wordDictionary= _dictionary.getDictionary();
		
		for (String englishWord : englishWords) {
			Word word = wordDictionary.get(englishWord);
			if(dQuota.wordsShown.containsKey(word.englishWord))
				if(dQuota.wordsShown.get(englishWord) <3)
			_displayModeA.showWord(word.englishWord, word.spanishTranslation);
			
			//Update Stats
			word.stats.timesShownSinceLastTime+=1;
			word.stats.lastShown=sdf.format(Calendar.getInstance().getTime());
			
			//Wait for few seconds before showing the next word
			Thread.sleep(WAIT_BETWEEN_2_WORDS);
		}
		
		//Repeat them 3 times and update their repeat count and timestamp
		
		
		//Start off a timer that would keep track of the words that have started.
		
	}


}
