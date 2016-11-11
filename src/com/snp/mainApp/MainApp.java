/**
 * 
 */
package com.snp.mainApp;

import java.util.List;

import com.snp.io.ReadInput;
import com.snp.models.Post;
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
		System.out.println(posts.get(3).getSentences().get(0).getDecomposedTags().get(0));
	}

}










