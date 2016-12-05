/**
 * 
 */
package com.snp.mainApp;

import java.awt.print.Printable;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.snp.filter1.FilterSet;
import com.snp.io.ConvertRawToFiles;
import com.snp.io.DecomposeData;
import com.snp.io.ReadInput;
import com.snp.learn.Lism;
import com.snp.models.Post;
import com.snp.preprocess.PreprocessUtil;

/**
 * @author Shirish
 *
 */
public class MainApp {

	/**
	 * @param args
	 * @throws Throwable 
	 */
	public static void main(String[] args) throws Throwable {
		// TODO Auto-generated method stub
		ReadInput reader = new ReadInput("res/rawdata.txt");
		List<Post> posts = reader.getPostContent();

		PreprocessUtil preProcess = new PreprocessUtil();
		posts = preProcess.populatePostSentences(posts);
		posts = preProcess.removeStopWordsFromPosts(posts);
		posts = preProcess.extractTags(posts);
		posts = preProcess.decomposeTags(posts);
		posts = preProcess.generateProcessedCode(posts);
		
		ConvertRawToFiles crf = new ConvertRawToFiles();
		int index = 0;
		for(Post post : posts){
			index++;
			crf.saveLineToFile(post.getRawData(), "res/data/Data" + index + ".txt");
		}
		
		// LISM
		//performLism();
		
		// Pre-Process for Rain's Code
		List<String> preprocessedSentence = new ArrayList<String>();
		for(Post post : posts) {
			//System.out.println(post.getSentences().get(0).getRawSentence());
			preprocessedSentence.add(post.getRawData().toLowerCase());
		}
		
		// Rain's Code
		for(String terrorismTwitter: filter1(preprocessedSentence) )
		System.out.println(terrorismTwitter);
		
	}

	/*
	 * 	Filter the twitter based on the tags set
	 */
	private static List<String> filter1(List<String> preprocessedSentence) {
		List<String> filterdTwitters = new ArrayList<String>(); 
		
		//This code is for testing purpose
		//Tags for this testing are extracted from the collected dataset. 
		//set TagsSet1 Terrorism
		List<String> tagsSet1 = new ArrayList<String>();
		// read tags from file and set filterSet 
		try {
			readTagsSet(tagsSet1, System.getProperty("user.dir") + "/" + "res/setTagsTerrorism.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FilterSet fs1 = new FilterSet(tagsSet1);
		
		//set TagsSet2 Motivation
		List<String> tagsSet2 = new ArrayList<String>();
		try {
			readTagsSet(tagsSet2, System.getProperty("user.dir") + "/" + "res/setTagsMotivation.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FilterSet fs2 = new FilterSet(tagsSet2);
		
		
		//set TagsSet3 Name of people from unrelated Context
		//words are extracted from false positive results
		
		List<String> tagsSet3 = new ArrayList<String>(); 
		try {
			readTagsSet(tagsSet3, System.getProperty("user.dir") + "/" + "res/setTagsPrezName.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		FilterSet fs3 = new FilterSet(tagsSet1); 
		
		for(String preprocessedTwitter : preprocessedSentence){
			if(fs1.hasWordsinFilterSet(preprocessedTwitter)){
				if(fs2.hasWordsinFilterSet(preprocessedTwitter)){ 
					if(!fs3.hasWordsinFilterSet(preprocessedTwitter)){
						//do nothing 
					}else {
						filterdTwitters.add(preprocessedTwitter); 
					}
				}
			}
		}
		return filterdTwitters;
		
	}

	private static void readTagsSet(List<String> tagsSet1, String file) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new FileReader(file));
		try {

		    String line = br.readLine();
		  

			    while (line != null) {			        
			        tagsSet1.add(line);
			        line = br.readLine();
			    }
			
		    
		    //System.out.println(tagsSet1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    br.close();
		}
		
	}

	private static void performLism() throws Throwable{
		
		DecomposeData dd = new DecomposeData(System.getProperty("user.dir") + "/" + "res/data");
		dd.perform_pre_process();
		
		Lism ob = new Lism(System.getProperty("user.dir") + "/" + "res/data", System.getProperty("user.dir") + "/" + "res/cliques");
		// --STEMMING
		//ob.perform_pre_process();	// Stemming
		
		// -- NO STEMMING
		ob.simple_lism();
		
		// --CALCULATE VOCABULARY
		ob.calculate_vocab();
		
		// --Stage 1: LISM-Counting
		ob.calculate_co_occurrence();
		ob.calculate_threshold();
		ob.calculate_marginal_counts();
		ob.calculate_total_counts();
		ob.calculate_co_occurrence_probabilities();
		ob.calculate_marginal_probabilities();
		
		// --Stage 2: LISM-Consistency
		ob.calculate_consistency();
		ob.calculate_consistency_threshold();
		
		// --Stage 3: LISM-Denoise
		ob.denoise();		
		
		// --Stage 4: LISM-Discovery
		try {
			ob.calculate_clique_graph();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}