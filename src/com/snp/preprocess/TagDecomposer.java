/**
 * 
 */
package com.snp.preprocess;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.BoxOfC.MDAG.MDAG;
import com.snp.models.Post;
import com.snp.models.Sentence;

/**
 * @author Shirish
 *
 */
public class TagDecomposer {
	
	private static Set<String> dictionary;
	private static MDAG myMDAG= null;
	
	public TagDecomposer(List<String> dictionaries){
		dictionary = new HashSet<String>();
		try {
			populateDictionary(dictionaries);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myMDAG = new MDAG(dictionary);
		myMDAG.simplify();
	}
	
	public List<Post> decomposePostTags(List<Post> posts){
		
		RemoveStopWords rsw = new RemoveStopWords();
		for(Post post : posts){
			for(int i = 0; i< post.getSentences().size(); i++){
				Sentence sentence = post.getSentences().get(i);
				List<String> decomposedTags = new ArrayList<String>();
				if(sentence.getTags() != null){
					for(String tags : sentence.getTags()){
						List<String> decomposedCompoundWord = decompose(tags);
						List<String> removeStopWords = rsw.removeStopWordsFromList(decomposedCompoundWord);
						decomposedTags.addAll(removeStopWords);
					}
				}
				post.getSentences().get(i).setDecomposedTags(decomposedTags);
			}
		}
		return posts;
	}
	
	/**
	 * Decompose the compound word
	 * @param word
	 * @return
	 */
	public List<String> decompose(String word){

		List<String> words = new ArrayList<String>();	// Final word list
		return words = decompose(myMDAG, new StringBuilder(word.toLowerCase()), words);
	}
	
	/**
	 * @param myMDAG
	 * @param str
	 * @param words
	 * @return
	 */
	private static boolean check(StringBuilder str) {
		if(str.length() == 0)
			return true;
		do{
			for(int i = 1; i< str.length() + 1; i++){
				if(myMDAG.contains(str.substring(0,i))){
					if(check(new StringBuilder(str.substring(i))) == false){
						if(myMDAG.contains(str.substring(0))){
							return true;
						}else{
							continue;
						}
					}else{
						return true;
					}
				}
			}
			break;
		}while(str.length() > 0);
		return false;
	}

	/**
	 * @param myMDAG
	 * @param str
	 * @param words
	 * @return
	 */
	private static List<String> decompose(MDAG myMDAG, StringBuilder str,
			List<String> words) {
		int length = str.length();
		do{
			List<String> temp = new ArrayList<String>();	// To store temp computation; last element is the longest element
			int lastIndex = 0;
			for(int i = 1; i< str.length() + 1; i++){
				if(myMDAG.contains(str.substring(0,i))){
					StringBuilder checkString = new StringBuilder(str.substring(i));
					if(check(checkString)){
						temp.add(str.substring(0,i));
						lastIndex = i;
					}
				}
			}
			
			if(temp.size() > 0){
				words.add(temp.get(temp.size()-1));
				str = new StringBuilder(str.substring(lastIndex));
			}else if(length == str.length()){
				return words;
			}
		}while(str.length() > 0);
		return words;
	}
	
	public static void populateDictionary(List<String> dictionaries) throws IOException {
		BufferedReader reader;
		String line="";
		
		for(String dictionaryName : dictionaries){
			reader = new BufferedReader(new FileReader(dictionaryName));
			while ((line = reader.readLine()) != null){
				dictionary.add(line.trim());
			}
			reader.close();
		}
	}

}
