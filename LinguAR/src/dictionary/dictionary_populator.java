package dictionary;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class dictionary_populator {

	/* class will populate the category dictionary */
	private static HashMap<String, List<String> > category_dictionary;
	
	// initializer 
	{
		category_dictionary = new HashMap<String, List<String> > ();	
	}
	
	public void createCategoryDic(InputStream file) throws IOException
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
		
		try {
			JSONObject reader = new JSONObject(sb.toString() );
			JSONArray a = reader.names();
			
			for(int i=0;i<a.length() ;i++){
				JSONArray a1 = reader.getJSONArray(a.getString(i));
				//System.out.println(a.getString(i));
				
				List<String> words = new ArrayList<String> ();
				for(int j=0;j<a1.length() ;j++){
					//System.out.println(a1.getString(j));
					words.add(a1.getString(j));
				}
				category_dictionary.put(a.getString(i), words); 
				//System.out.println("----");
			}
			
		    System.out.println(category_dictionary);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
