/**
 * 
 */
package com.snp.mainApp;

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
		performLism();
		
		// Pre-Process for Rain's Code
		List<String> preprocessedSentence = new ArrayList<String>();
		for(Post post : posts) {
			//System.out.println(post.getSentences().get(0).getRawSentence());
			preprocessedSentence.add(post.getRawData().toLowerCase());
		}
		
		// Rain's Code
		//filter1(preprocessedSentence);
		
	}

	private static List<String> filter1(List<String> preprocessedSentence) {
		List<String> filterdTwitters = new ArrayList<String>(); 
		
		//This code is for testing purpose
		//Tags for this testing are extracted from the collected dataset. 
		//set TagsSet1 Terrorism
		List<String> tagsSet1 = new ArrayList<String>();
		
		//TO DO : read tags from file and set filterSet 
		FilterSet fs1 = new FilterSet(tagsSet1);
		
		
		//set TagsSet2 Motivation
		List<String> tagsSet2 = new ArrayList<String>();
		FilterSet fs2 = new FilterSet(tagsSet2);
		
		
		//set TagsSet3 Name of people from unrelated Context
		//words are extracted from false positive results
		
		List<String> tagsSet3 = new ArrayList<String>(); 
		FilterSet fs3 = new FilterSet(tagsSet1); 
		
		for(String preprocessedTwitter : preprocessedSentence){
			if(fs1.hasWordsinFilterSet(preprocessedTwitter)){
				if(fs2.hasWordsinFilterSet(preprocessedTwitter)){
					if(!fs3.hasWordsinFilterSet(preprocessedTwitter)){
						filterdTwitters.add(preprocessedTwitter);
					}
				}
			}
		}
		return filterdTwitters;
		
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