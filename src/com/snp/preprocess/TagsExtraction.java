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
public class TagsExtraction{
	
	public List<Post> extractHashtagsFromPosts(List<Post> posts){
		
		List<Post> processPost = new ArrayList<Post>();
		for(int i = 0; i < posts.size(); i++){
			Post post = getHashTags(posts.get(i));
			processPost.add(post);
		}
		return processPost;
	}

	public Post getHashTags(Post post){
		
		for(int i = 0; i< post.getSentences().size(); i++){
			Sentence sentence = post.getSentences().get(i);
			if(!sentence.getRawSentence().equals("")){
				List<String> tags = new ArrayList<String>();
				for(String word : sentence.getWords()){
					if(!word.equals("") && (word.charAt(0) == '#' || word.charAt(0) == '$')){
						tags.add(word.substring(1));
					}
				}
				post.getSentences().get(i).setTags(tags);
			}
		}
		return post;
	}

}
