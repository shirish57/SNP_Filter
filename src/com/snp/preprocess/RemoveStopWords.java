/**
 * 
 */
package com.snp.preprocess;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.snp.io.ReadInput;
import com.snp.models.Post;
import com.snp.models.Sentence;

/**
 * @author Shirish
 *
 */
public class RemoveStopWords {
	
	private Set<String> stopWords;
	
	/**
	 * Remove stop words from a post
	 * @param post
	 * @return
	 */
	public Post removeStopWordsFromPosts(Post post){
		
		List<Sentence> sentences = new ArrayList<Sentence>();
		for(Sentence sentence : post.getSentences()){
			sentences.add((this.removeStopWordsFromSentence(sentence)));
		}
		post.setSentences(sentences);
		return post;
	}
	
	
	/**
	 * Remove stop words from individual sentences
	 * @param sentence
	 * @return
	 */
	public Sentence removeStopWordsFromSentence(Sentence sentence){
		
		List<String> relevantWords = new ArrayList<String>();
		String[] words = sentence.getRawSentence().split(" ");
		this.populateStopWords();
		for(String word : words){
			if(this.stopWords.contains(word)){
				continue;
			}else{
				relevantWords.add(word);
			}
		}
		
		String rawSentence = "";
		for(String word : relevantWords){
			rawSentence += " " + word;
		}
		rawSentence = rawSentence.trim();
		sentence.setRawSentence(rawSentence);
		return sentence;
	}
	
	/**
	 * Removes stopwords from a list of words
	 * @param words
	 * @return
	 */
	public List<String> removeStopWordsFromList(List<String> words){
		
		List<String> listPostStopWordsRemoval = new ArrayList<String>();
		this.populateStopWords();
		for(String word : words){
			if(this.stopWords.contains(word)){
				continue;
			}else{
				listPostStopWordsRemoval.add(word);
			}
		}
		return listPostStopWordsRemoval;
	}

	/**
	 * Populating the stopword list
	 */
	private void populateStopWords(){
		ReadInput ri = new ReadInput();
		this.stopWords = ri.getStopWords("res/stopWords.txt");
	}
}
