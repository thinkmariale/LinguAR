package com.linguar.lessonplan;
import dictionary.Category;
import dictionary.Word;
import dictionary.dictionary_populator;
import java.util.*;

public class ReviewMode {
	
	//Call to saved categories
	private CategoryGetter _cGetter =  new CategoryGetter();
	public List<Category> topFiveCategories = _cGetter.getTopFiveCategories();
	
	//Call to category dictionary
	public Queue<Word> englishWords = new LinkedList<Word>();
	
	
	private HashMap<String, List<String>> getCategoryDic()
	{
		HashMap<String, List<String>>  catDictionary = null;
		
		return catDictionary;
	}
	
	//Call to all the words in a category dictionary

	
	//Shuffle the returned set of words
	
	
	//Repeat them 3 times and update their repeat count and timestamp
	
	
	//Start off a timer that would keep track of the words that have started.
	
	

}
