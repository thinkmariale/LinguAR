package dictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CategoryDictionary {
	
	private static CategoryDictionary instance = new CategoryDictionary();
	
	private static HashMap<Category, List<String> > category_dictionary;
	private int topCat = 5;
	
	//get instance
	/* Static 'instance' method */
	public static CategoryDictionary getInstance( ) {
		return instance;
	}
	
	public void setCategoryDic( HashMap<Category, List<String> > c)
	{
		category_dictionary = c;
	}
	
	
	//Function will increase count to category
	public void increaseCount(Category cat)
	{
		
	}
	//Function returns list of words in THAT category
	public List<String> getWordsFromCategory(Category cat)
	{
		return category_dictionary.get(cat);
	}
	
	// Function that returns top 5 categories
	public List<Category> getTopCategories()
	{
		List<Category> top = new ArrayList<Category>(topCat);
		for (Category key : category_dictionary.keySet()) {
			int c = 0;
			while(c < top.size() )
			{
				if(key.counter > top.get(c).counter){
					top.get(c).set( key);
					break;
				}
				else c++;
			}  
		}
		
		return top;
		
	}
	
}
