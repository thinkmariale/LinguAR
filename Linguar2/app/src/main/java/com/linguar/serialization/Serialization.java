package com.linguar.serialization;
import java.io.*;

import com.linguar.dictionary.*;


public class Serialization {


    public <T> void saveData_(T tClass, String filePath)
    {
        FileOutputStream fileOut = null;
        ObjectOutputStream out = null;
        try
        {
            // tClass
            fileOut = new FileOutputStream(filePath);
            out   = new ObjectOutputStream(fileOut);
            out.writeObject(tClass);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in dictionary.ser");
        }catch(Exception i)
        {
            i.printStackTrace();
        }
    }


    public <T> void loadData_(T tClass, String filePath)
    {
        FileOutputStream fileOut = null;
        ObjectOutputStream out = null;
        try
        {
            FileInputStream fileIn = new FileInputStream(filePath);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            tClass = (T) in.readObject();
            in.close();
            fileIn.close();

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
	public void saveData(String filePath, String filePath1)
	{
		Dictionary dir         = Dictionary.getInstance();
		CategoryDictionary cat = CategoryDictionary.getInstance();
		FileOutputStream fileOut = null;
	    ObjectOutputStream out = null;
	    FileOutputStream fileOut1 = null;
	    ObjectOutputStream out1 = null;
	    
		 try
	      {
			 // Dictionary
	         fileOut = new FileOutputStream(filePath);
	         out   = new ObjectOutputStream(fileOut);
	         out.writeObject(dir);
	         out.close();
	         fileOut.close();
	         System.out.printf("Serialized data is saved in dictionary.ser");
	         
	         // Category dictionary
	         fileOut1 = new FileOutputStream(filePath1);
	         out1 = new ObjectOutputStream(fileOut1);
	         out1.writeObject(cat);
	         out1.close();
	         fileOut1.close();
	         System.out.printf("Serialized data is saved in cat_dictionary.ser");
	      }catch(Exception i)
	      {
	          i.printStackTrace();
	      }
	}
	
	public void loadData(String filePath, String filePath1)
	{
		Dictionary dir         = Dictionary.getInstance();
		CategoryDictionary cat = CategoryDictionary.getInstance();
	
		// Load Dictionary/CategoryDictionary from file
		try
	      {
	         FileInputStream fileIn = new FileInputStream(filePath);
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         dir = (Dictionary) in.readObject();
	         in.close();
	         fileIn.close();

	         // now load CategoryDictionary
	         FileInputStream fileIn1 = new FileInputStream(filePath1);
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
