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
		/*
		for(int i = 0; i < 26; i++){
			dictionary.add(new HashMap<String,LookupVal>());
		}*/
		
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
			if(isDisplayed) {
				dictionary.get(w).incrementCategoryCount();
			}
			return dictionary.get(w);
		}
		
		return null;
	}

	/*public LookupVal find(String w){
		
		int c = getIndex(w);
		return dictionary.get(c).get(w);
		
	}
	
	
	public Word addWord(String w, Word l){
				
		int c = getIndex(w);
		
		return dictionary.get(c).put(w, l);
				
	}*/
	
	public boolean updateWordTranslation(){
		boolean result = false;
		
		return result;
	}
	
	
	/*public int fullUpdate(Dictionary d){
		int numAdded = 0;
		
		for (int i = 0; i <26; i++){
			if(dictionary.get(i).size() < d.dictionary.get(i).size()){
				
				// find and add all new elements
			}
			
			
		}
		
		
		
		return numAdded;
	}*/
	
	public int getIndex(String w){
		char c = w.charAt(0);
		int pos = (int)c;
		if(pos < 97){
			c += 32;
		}
		c -= 97;
		return c;
	}
	
	
}
