package dictionary;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Dictionary {
	

	private static List<HashMap<String, Word>> dictionary;
	
	// initializer 
	{

		dictionary = new ArrayList<HashMap<String, Word>>();
		for(int i = 0; i < 26; i++){
			dictionary.add(new HashMap<String,Word>());
		}
		
	}
	
	public Dictionary(){
		
	}
	
	// Format of english/spanish dictionary??
	
	public void LoadDictionary(String filename){
		
		try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
		    for(String line; (line = br.readLine()) != null; ) {
		    	String[] wordlist = (line.split(","));
		    	
		    	int c = getIndex(line);
		    	
		    	dictionary.get(c).put(wordlist[0], new Word(wordlist[0],wordlist[1]));
		    	
		    }
		    
		} catch (FileNotFoundException e) {
			System.out.println("error finding file");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("error during file IO");
			e.printStackTrace();
		}
		
		
	}
	
	public List<HashMap<String,Word>> getDictionary(){
		return dictionary;
	}
	
	public Word find(String w){
		
		int c = getIndex(w);
		
		return dictionary.get(c).get(w);
		
	}
	
	
	public Word addWord(String w, Word l){
				
		int c = getIndex(w);
		
		return dictionary.get(c).put(w, l);
				
	}
	
	public boolean updateWordTranslation(){
		boolean result = false;
		
		return result;
	}
	
	
	public int fullUpdate(Dictionary d){
		int numAdded = 0;
		
		for (int i = 0; i <26; i++){
			if(dictionary.get(i).size() < d.dictionary.get(i).size()){
				
				// find and add all new elements
			}
			
			
		}
		
		
		
		return numAdded;
	}
	
	public int getIndex(String w){
		char c = w.charAt(0);
		int pos = (int)c;
		if(pos < 97){
			c += 32;
		}
		c -= 97;
		return c;
	}
	
	public static void main(String[] args) {
	
			Dictionary d = new Dictionary();
			d.LoadDictionary("dictionarySpEn1.txt");
			
			System.out.println("loaded");
			
			System.out.println(d.find("steak"));
		
	}
	
	
}
