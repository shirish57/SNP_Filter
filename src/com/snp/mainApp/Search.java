package com.snp.mainApp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.snp.learn.stemmer.TestApp;

public class Search {

	private String cliquePath = System.getProperty("user.dir") + "/" + "res/cliques";
	private String queryPath = System.getProperty("user.dir") + "/" + "res/query.txt";
	private List cliques;
	
	public static void main(String[] args) throws Throwable {
		// TODO Auto-generated method stub
		Search search = new Search();
		//search.cliques = search.loadCliques(search.cliquePath);
		System.out.println(search.search("QUERY"));
	}
	
	public boolean search(String query) throws Throwable{
		
		List<List<String>> cliques = this.loadCliques(this.cliquePath);
		List<String> words = (List) Arrays.asList(query);
		
		PrintWriter pw = new PrintWriter(new File(queryPath));
		for (String word : words){
			pw.println(word);
		}
		pw.close();
		
		TestApp test=new TestApp();
		String[] stemmedQuery=test.sentence(this.queryPath ,"english").split(" ");
		
		words = (List) Arrays.asList(stemmedQuery);
		
		for(List<String> clique : cliques){
			if(words.containsAll(clique) || clique.containsAll(words)){
				return true;
			}
		}
		
		return false;
	}
	
	private List<List<String>> loadCliques(String cliquePath) throws IOException{
		
		List<List<String>> cliques = new ArrayList<List<String>>();
		File folder = new File(cliquePath);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++)
		{
			if (listOfFiles[i].isFile())
			{
				// Read dataset
				BufferedReader reader = new BufferedReader(new FileReader(cliquePath + "/" + listOfFiles[i].getName()));
				String line = "";
				List<String> clique = new ArrayList<String>();
				
				// Store dataset in a List for future reference
				while ((line = reader.readLine()) != null)
				{
					clique.add(line);
				}
				reader.close();
				
				// Only if clique size is greater than 1
				if(clique.size() > 1){
					cliques.add(clique);
				}
			}
		}
		return cliques;
	}

}
