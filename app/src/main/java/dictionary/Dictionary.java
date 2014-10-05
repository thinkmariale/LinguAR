package dictionary;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Dictionary {
	
	
	private static List<HashMap<String, LookupVal>> dictionary;
	
	// initializer 
	{
		for(int i = 0; i < 26; i++){
			dictionary.add(new HashMap<String,LookupVal>());
		}
		
	}
	
	public Dictionary(){
		
	}
	
	// Format of english/spanish dictionary??
	
	public void LoadDictionary(String filename){
		
		try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
		    for(String line; (line = br.readLine()) != null; ) {
		    	int c = getIndex(line);
		    	
		    	dictionary.get(c).put(line, new LookupVal(line));
		    	
		    }
		    
		} catch (FileNotFoundException e) {
			System.out.println("error finding file");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("error during file IO");
			e.printStackTrace();
		}
		
		
	}
	
	public List<HashMap<String,LookupVal>> getDictionary(){
		return dictionary;
	}
	
	public LookupVal find(String w){
		
		int c = getIndex(w);
		
		return dictionary.get(c).get(w);
		
	}
	
	public LookupVal addWord(String w, LookupVal l){
				
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
}
