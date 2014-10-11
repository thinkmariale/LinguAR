package dictionary;

import java.util.HashSet;

public class LookupVal {
	public String word;
	public String translation;
	public WordStat stats;
	public HashSet<String> categoryList;
	
	public LookupVal(){
		word = "";
		translation = "";
		stats = new WordStat();
		categoryList = new HashSet<String>(5);
	}
	
	public LookupVal(String w){
		word = w;
		translation = "";
		stats = new WordStat();
		categoryList = new HashSet<String>(5);
	}
	
	public LookupVal(String w, String t, int c, int i, HashSet<String> cats){
		word = w;
		translation = t;
		stats = new WordStat(w,c,i);
		categoryList = cats;		
	}
	
	public LookupVal(String w, String t){
		word = w;
		translation = t;
		stats = new WordStat(w);
		categoryList = new HashSet<String>(5);
	}
	
	public String toString(){
		
		
		return word + " : " + translation;
		
		
	}
	
}
