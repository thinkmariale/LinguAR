package com.linguar.lessonplan;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.linguar.dictionary.*;

/**
 *
 */
public class WordGetter{

	public final int MAX_WORDS_IN_24_HOURS = 7;
	private CategoryDictionary _cDictionary;
    private Dictionary _dictionary;
    private DailyLessonQuota _dQuota;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat newsdgf = new SimpleDateFormat("yyyyMMddkkmmss");
    private long todaysDate;
    /**
	 * Returns the list of 7 words from the list of Categories. The list is shuffled. These words have not been shown in the last 24 hours
	 * @param categoryList List of Categories
	 * @return List of all the words from the supplied list of categories
	 * @throws Exception In case of the method is supplied with less than 5 categories, this exception is called
	 */
	public List<String> getWordsFromCategoryList(List<Category> categoryList) throws Exception
	{
		_cDictionary =  CategoryDictionary.getInstance();
        _dictionary = Dictionary.getInstance();
        _dQuota = DailyLessonQuota.getInstance();
        System.out.println(sdf.format(Calendar.getInstance().getTime()));
        todaysDate = Long.valueOf(sdf.format(Calendar.getInstance().getTime()));
		List<String> allCategoryWords =  new ArrayList<String>();
		
		if(categoryList.size() < 5)
		{
			throw new Exception("Insufficient number of top categories : The number of categories supplied are : "+ categoryList.size());
		}
		
		//Get all the words from the supplied list of categories
		for (Category category : categoryList) {
			System.out.println("Category : "+ category.category);
			 allCategoryWords.addAll(_cDictionary.getWordsFromCategory(category));
		}
		
		//If the total number of words in a category are less than twice the number of words to be shown in 24 hours get more categories
		while(allCategoryWords.size() < MAX_WORDS_IN_24_HOURS*2)
		{
			CategoryGetter _cGetter = new CategoryGetter();
			Category newCategory =_cGetter.getNewCategory(categoryList);
			categoryList.add(newCategory);
			allCategoryWords.addAll(_cDictionary.getWordsFromCategory(newCategory));
		}

        //Get the 7 words that are to be shuffled and sent for the day.
		List<String> finalWordList = new ArrayList<String>();
        HashMap<String, Word> wordDictionary = _dictionary.getDictionary();

        System.out.println("size " + _dictionary.getDictionary().size());
        Collections.shuffle(allCategoryWords);
        for(String word : allCategoryWords) {

                if (_dictionary.getWord(word,false)== null){

                    continue;
                }

               String lastShown = wordDictionary.get(word).stats.lastShown;

            if (!lastShown.equals("") || lastShown.length() != 0) {
                System.out.println("LastShown " + lastShown + " " + lastShown.length());
                if (todaysDate - Long.valueOf(lastShown) >= 1000000) //Differece of 24 hours
                    finalWordList.add(word);
            } else {
                finalWordList.add(word);
            }

            if(finalWordList.size() == _dQuota.NO_OF_WORDS_TO_BE_SHOWN_PER_DAY)
                break;
        }

		//Shuffle the obtained list of words
		Collections.shuffle(finalWordList);

		 System.out.println("Words in these categories are : "+ finalWordList);

		return finalWordList;
	}

}
