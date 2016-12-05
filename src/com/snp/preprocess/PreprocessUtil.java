/**
 * 
 */
package com.snp.preprocess;

import java.util.ArrayList;
import java.util.List;

import com.snp.models.Post;
import com.snp.models.Sentence;

/**
 * @author Shirish
 *
 */
public class PreprocessUtil {
	
	private SplitSentences splitSentencesUtil;
	private static TagDecomposer td;
	
	public PreprocessUtil(){
		this.splitSentencesUtil = new SplitSentences();
		List<String> dictionaries = new ArrayList<String>();
		dictionaries = addDictionaries(dictionaries);
		td = new TagDecomposer(dictionaries);
	}
	
	private List<String> addDictionaries(List<String> dictionaries){
		dictionaries.add(System.getProperty("user.dir") + "/res/" + "dictionary2.txt");
		dictionaries.add(System.getProperty("user.dir") + "/res/" + "project_dictionary.txt");
		return dictionaries;
	}
	
	/**
	 * Populate the Post objects with sentences and tokens
	 * @param posts
	 * @return
	 */
	public List<Post> populatePostSentences(List<Post> posts){
		
		List<Post> populatedPosts = new ArrayList<Post>();
		for(Post post : posts){
			populatedPosts.add(splitSentencesUtil.splitPostToSentences(post));
		}
		return populatedPosts;
	}
	
	/**
	 * Remove stop words from the posts
	 * @param posts
	 * @return
	 */
	public List<Post> removeStopWordsFromPosts(List<Post> posts){
		
		List<Post> removedStopWordPosts = new ArrayList<Post>();
		RemoveStopWords removeStopWords = new RemoveStopWords();
		
		for(Post post : posts){
			removedStopWordPosts.add(removeStopWords.removeStopWordsFromPosts(post));
		}
		return removedStopWordPosts;
	}
	
	/**
	 * Extract Tags from Posts
	 * @param posts
	 * @return
	 */
	public List<Post> extractTags(List<Post> posts){
		TagsExtraction te = new TagsExtraction();
		posts = te.extractHashtagsFromPosts(posts);
		return posts;
	}
	
	/**
	 * Decompose Compound words in Tags
	 * @param posts
	 * @return
	 */
	public List<Post> decomposeTags(List<Post> posts){
		posts = td.decomposePostTags(posts);
		return posts;
	}
	
	/**
	 * Generate pre-processed sentences for each post
	 * @param posts
	 * @return
	 */
	public List<Post> generateProcessedCode(List<Post> posts){
		
		for(int i = 0; i< posts.size(); i++){
			Post post = posts.get(i);
			String postRaw = "";
			for(int j = 0; j < post.getSentences().size(); j++){
				Sentence sentence = post.getSentences().get(j);
				postRaw += " " + sentence.getRawSentence();
			}
			post.setRawData(postRaw.trim());
		}
		return posts;
	}
}
