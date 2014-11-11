package com.linguar.dictionary;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.HashMap;

import android.util.Log;

public class Dictionary implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 12L;
	private static Dictionary instance = new Dictionary( );
	//private static List<HashMap<String, LookupVal>> dictionary;
	private static HashMap<String, Word> dictionary;
	
	// initializer 
	{
		dictionary = new HashMap<String, Word>();

		
	}
	
	public Dictionary(){
		
	}
	
	//get instance
	/* Static 'instance' method */
	 public static Dictionary getInstance( ) {
	      return instance;
	 }
	   
	// Format of english/spanish dictionary??
	public void LoadDictionary(InputStream inputStream) throws IOException{
		
		dictionary = new HashMap<String, Word>();
	//	InputStreamReader is = new InputStreamReader(inputStream,"ISO-8859-1");
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream,"ISO-8859-1"));

		 Log.d("DIC", "in dic!");
		
		 String read = "";

        while ((read=br.readLine())!=null){
			 String[] wordList = (read.split(","));
			 dictionary.put(wordList[0], new Word(wordList[0],wordList[1]) );
		}
		 br.close();
		 Log.d("DIC","dic length " + String.valueOf( dictionary.size() ));
	}
	

	public HashMap<String,Word> getDictionary(){
		return dictionary;
	} 
	
	public void setCategory(String word, Category cat)
	{
		dictionary.get(word).categoryList.add(cat);
	}
	
	//Function to get word and if its being shown, increment category count
	public Word getWord(String w, boolean isDisplayed)
	{
		if(dictionary.containsKey(w))
		{
            // if we are thinking on displaying it, it must have at least 2 categories
			if(isDisplayed) {
                if(dictionary.get(w).incrementCategoryCount())
                    return dictionary.get(w);
			}
			else
                return dictionary.get(w);
		}
		
		return null;
	}



	
}
