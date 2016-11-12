/**
 * 
 */
package com.snp.mainApp;

import java.util.ArrayList;
import java.util.List;

import com.snp.filter1.FilterSet;
import com.snp.io.ReadInput;
import com.snp.models.Post;
import com.snp.models.Sentence;
import com.snp.preprocess.PreprocessUtil;

/**
 * @author Shirish
 *
 */
public class MainApp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ReadInput reader = new ReadInput("res/rawdata.txt");
		List<Post> posts = reader.getPostContent();

		PreprocessUtil preProcess = new PreprocessUtil();
		posts = preProcess.populatePostSentences(posts);
		//System.out.println(posts.get(0).getSentences().get(0).getRawSentence());
		posts = preProcess.removeStopWordsFromPosts(posts);
		//System.out.println(posts.get(0).getSentences().get(0).getRawSentence());
		
		posts = preProcess.extractTags(posts);
		//System.out.println(posts.get(5).getSentences().get(2).getTags().get(0));
		
		posts = preProcess.decomposeTags(posts);
		//System.out.println(posts.get(3).getSentences().get(0).getDecomposedTags().get(0));
		

		ArrayList<String> preprocessedSentence = new ArrayList<String>();
		for(Post post : posts) {
			//System.out.println(post.getSentences().get(0).getRawSentence());
			for(Sentence sentence : post.getSentences()) {
				String str = sentence.getRawSentence().toLowerCase();
				// Call Function
				preprocessedSentence.add(str);
				
			}
		}
		
		
		ArrayList<String> filterdTwitter; 
		
		//This code is for testing purpose
		//Tags for this testing are extracted from the collected dataset. 
		//set TagsSet1 Terrorism
		ArrayList<String> tags1 = new ArrayList<String>();
		tags1.add("terrorism");
		tags1.add("isis");
		tags1.add("jihad");
		tags1.add("akbar");
		//tags1.add("plotting against");
		tags1.add("bombs");
		tags1.add("lashkar");
		tags1.add("hezbollah");
		
		FilterSet fs1 = new FilterSet(tags1);  
		filterdTwitter = fs1.filterSet1(preprocessedSentence);
		
		for(String twitter :filterdTwitter) {
			System.out.println(twitter);
		}
		
		//set TagsSet2 Motivation
//		ArrayList<String> tags2 = new ArrayList<String>();
//		tags2.add("successful");
//		tags2.add("Lashkar");
//		tags2.add("ISIS");
		
		//set TagsSet3 Name of people from unrelated Context
		//words are extracted from false positive results
//		ArrayList<String> tags3 = new ArrayList<String>();
//		tags3.add("Obama");
//		tags3.add("");
//		tags3.add("");
		
		//System.out.println(preprocessedSentence);
		
	}

}










