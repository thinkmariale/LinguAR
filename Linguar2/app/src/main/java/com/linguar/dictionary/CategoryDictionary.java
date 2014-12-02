package com.linguar.dictionary;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CategoryDictionary implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static CategoryDictionary instance = new CategoryDictionary();

    private static List<Category> DefaultCategories;
	private static HashMap<Category, List<String> > category_dictionary;
	
	// initializer 
		{
			category_dictionary = new HashMap<Category, List<String> >();
            DefaultCategories = new ArrayList<Category>();
        }

	//get instance
	/* Static 'instance' method */
	public static CategoryDictionary getInstance( ) {
		return instance;
	}
	
	public void setCategoryDic( HashMap<Category, List<String> > c)
	{
		category_dictionary = c;
	}

    public void setDefaultCategories(List<Category> c)
    {
        DefaultCategories = c;
    }

    public List<Category> getDefaultCategories()
    {
        return DefaultCategories;
    }
	public HashMap<Category, List<String> > getCatDictionary(){
		return category_dictionary;
	} 
	
	//Function will increase count to category
	public void increaseCount(Category cat)
	{
		
	}
	//Function returns list of words in THAT category
	public List<String> getWordsFromCategory(Category cat)
	{
		if(category_dictionary.containsKey(cat)){
			return category_dictionary.get(cat);
		}		
		return null;
	}
	
	// Function that returns top 5 categories
	public List<Category> getTopCategories()
	{
		List<Category> top = new ArrayList<Category>()
				{{ add(new Category()); add(new Category()); }};//add(new Category());
				//add(new Category()); add(new Category()); }} ;

        //poppulating from top cat
        //Log.d("CAT", Integer.toString(category_dictionary.size()));
		for (Category key : category_dictionary.keySet()) {
            int c = 0;

            while (c < top.size()) {
                if (key.counter > top.get(c).counter) {
                    //getWordsFromCategory(key);
                    top.remove(c);
                    top.add(c, key);
                    //getWordsFro   mCategory(top.get(c));
                    break;
                } else c++;
            }
        }

        //if some still null, add
        int d = 0;
        int c = 0;
        List<Integer> intList = new ArrayList<Integer>();
       // Log.d("CAT", "top " + Integer.toString(top.size()));
        for(Category cat: top)
        {
            if((cat.category == null || cat.category=="" || cat.category.isEmpty()) )
            {
               intList.add(c);
            }
            c++;
        }
       // Log.d("CAT", "list " + Integer.toString(intList.size()));
        for(Integer i : intList)
        {
            top.remove((int)i);
            top.add(i, DefaultCategories.get(d));
            d++;
         //   Log.d("CAT", "remove " + Integer.toString(i) +  " top " + Integer.toString(top.size()));

        }
       // Log.d("CAT", "index " + Integer.toString(c) + " " + Integer.toString(d));
       // Log.d("CAT", "top " + Integer.toString(top.size()));
		return top;
		
	}
	
}
