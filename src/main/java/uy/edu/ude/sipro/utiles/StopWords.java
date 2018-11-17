package uy.edu.ude.sipro.utiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

public class StopWords {
	
	String[] stopword;
	
	public List<String> CargarStopWordES() {

		File folder = new File("resources\\StopWords\\");
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
		    if (file.isFile()) {
		        //System.out.println(file.getName());
		    }
		}
		
		List<String> list = new ArrayList<String>();
		BufferedReader reader = null;

		for (File archivo : listOfFiles)
		{
			if (archivo.isFile())
			{
				System.out.println(list.size());
				File file = new File("resources\\StopWords\\"+archivo.getName());
				System.out.println(file.getName());
				try {
				    reader = new BufferedReader( new InputStreamReader(new FileInputStream(file), "UTF-8"));
				    String text = null;
		
				    while ((text = reader.readLine()) != null) {
				    	text.replaceAll("\\s+","");
				        list.add(text);
				    }
				} catch (FileNotFoundException e) {
				    e.printStackTrace();
				} catch (IOException e) {
				    e.printStackTrace();
				} finally {
				    try {
				        if (reader != null) {
				            reader.close();
				        }
				    } catch (IOException e) {
				    }
			}
		}

		//print out the list
		//System.out.println(list);
		}
		System.out.println(list.size());
		System.out.println(list);
		list = new ArrayList<String>(new LinkedHashSet<String>(list));
		System.out.println(list.size());
		System.out.println(list);
		return list;
	}	
	
	}
