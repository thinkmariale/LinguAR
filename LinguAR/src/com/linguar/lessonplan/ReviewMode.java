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
	private Dictionary _dictionary = Dictionary.getInstance();
	private DisplayWordModeA _displayModeA = new DisplayWordModeA();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddkkmmss");
	
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
			_displayModeA.showWord(word.englishWord, word.spanishTranslation);
			
			//Update Stats
			word.stats.timesShown+=1;
			word.stats.lastShown=sdf.format(Calendar.getInstance().getTime());
			
			//Wait for 5 seconds before showing the next word
			Thread.sleep(5000);
		}
		
		//Repeat them 3 times and update their repeat count and timestamp
		
		
		//Start off a timer that would keep track of the words that have started.
		
	}


}
