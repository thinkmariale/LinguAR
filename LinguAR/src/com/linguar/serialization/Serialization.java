package com.linguar.serialization;
import java.io.*;

import dictionary.CategoryDictionary;
import dictionary.Dictionary;

public class Serialization {

	public void saveData()
	{
		Dictionary dir         = Dictionary.getInstance();
		CategoryDictionary cat = CategoryDictionary.getInstance();
		
		 try
	      {
			 // Dictionary
	         FileOutputStream fileOut =
	         new FileOutputStream("/tmp/dictionary.ser");
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(dir);
	         out.close();
	         fileOut.close();
	         System.out.printf("Serialized data is saved in /tmp/dictionary.ser");
	         
	         // Category dictionary
	         FileOutputStream fileOut1 =
	         new FileOutputStream("/tmp/cat_dictionary.ser");
	         ObjectOutputStream out1 = new ObjectOutputStream(fileOut1);
	         out1.writeObject(cat);
	         out1.close();
	         fileOut1.close();
	         System.out.printf("Serialized data is saved in /tmp/cat_dictionary.ser");
	      }catch(IOException i)
	      {
	          i.printStackTrace();
	      }
	}
	
	public void loadData()
	{
		Dictionary dir         = Dictionary.getInstance();
		CategoryDictionary cat = CategoryDictionary.getInstance();
	
		// Load Dictionary/CategoryDictionary from file
		try
	      {
	         FileInputStream fileIn = new FileInputStream("/tmp/dictionary.ser");
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         dir = (Dictionary) in.readObject();
	         in.close();
	         fileIn.close();
	         // now load CategoryDictionary
	         FileInputStream fileIn1 = new FileInputStream("/tmp/cat_dictionary.ser");
	         ObjectInputStream in1 = new ObjectInputStream(fileIn);
	         cat = (CategoryDictionary) in1.readObject();
	         in1.close();
	         fileIn1.close();
	         
	      }catch(IOException i)
	      {
	         i.printStackTrace();
	         return;
	      }catch(ClassNotFoundException c)
	      {
	         System.out.println("dictionary class not found");
	         c.printStackTrace();
	         return;
	      }
		
	}
}
