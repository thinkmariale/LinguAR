package dictionary;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.linguar.lessonplan.ReviewMode;

public class dictionary_populator {

	/* class will populate the category dictionary */
	private static HashMap<Category, List<String> > category_dictionary;
	

	public void createCategoryDic(InputStream file) throws Exception
	{
		System.out.println("creating category directionary");
		InputStreamReader is = new InputStreamReader(file);
		BufferedReader br    = new BufferedReader(is);
		String read          = br.readLine();
		StringBuilder sb     = new StringBuilder();

		while(read != null) {
		   sb.append(read);
		   read = br.readLine();
		}
		
		// getting main dictionary so we can populate the categories per word
		Dictionary dic = Dictionary.getInstance();
		
		System.out.println("dic length: " + dic.getDictionary().size());
		try {
			JSONObject reader = new JSONObject(sb.toString() );
			JSONArray a = reader.names();
			category_dictionary = new HashMap<Category, List<String> > ();	
			
			for(int i = 0; i < a.length(); i++) {
				JSONArray a1 = reader.getJSONArray(a.getString(i));
				//System.out.println(a.getString(i));
				Category cat =  new Category(a.getString(i));
				
				List<String> words = new ArrayList<String> ();
				for(int j = 0; j < a1.length(); j++) {
					//System.out.println(a1.getString(j));
					dic.setCategory(a1.getString(j), cat);
					words.add(a1.getString(j));
				}
				
				category_dictionary.put(cat, words); 
				//System.out.println("----");
			}
			
		   // System.out.println(category_dictionary);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//setting categoryDictionary
		CategoryDictionary cat = CategoryDictionary.getInstance();
		cat.setCategoryDic(category_dictionary);

	}
}
