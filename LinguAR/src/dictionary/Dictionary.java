package dictionary;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import android.util.Log;

public class Dictionary {
	
	private static Dictionary instance = new Dictionary( );
	//private static List<HashMap<String, LookupVal>> dictionary;
	private static HashMap<String, Word> dictionary;
	
	// initializer 
	{
		//dictionary = new ArrayList<HashMap<String, LookupVal>>();
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
		InputStreamReader is = new InputStreamReader(inputStream);
		BufferedReader br = new BufferedReader(is);
		
		 Log.d("DIC", "in dic!");
		
		 String read          = br.readLine();
		 while(read != null) {
			 String[] wordlist = (read.split(","));
			 dictionary.put(wordlist[0], new Word(wordlist[0],wordlist[1]) );
			 read = br.readLine();
		}
		 	
		 Log.d("DIC","dic length " + String.valueOf( dictionary.size() ));
	}
	

	//public List<HashMap<String,LookupVal>> getDictionary(){
		//return dictionary;
	//}
	public HashMap<String,Word> getDictionary(){
		return dictionary;
	} 
	
	public void setCategory(String word, Category cat)
	{
		dictionary.get(word).categoryList.add(cat);
	}
	
	//public Word find(String w){}

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
