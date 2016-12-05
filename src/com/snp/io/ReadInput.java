/**
 * 
 */
package com.snp.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.snp.models.Post;

/**
 * @author Shirish
 *
 */
public class ReadInput {
	
	private String filePath;
	
	public ReadInput(String path){
		this.filePath = path;
	}
	
	public ReadInput() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Reads the input file to get the post data into a list
	 * @return list of posts
	 */
	@SuppressWarnings("resource")
	public List<Post> getPostContent(){
		
		List<Post> posts = new ArrayList<Post>();
		File file = new File(System.getProperty("user.dir") + "/" + this.filePath);
	    try {
    		BufferedReader br = new BufferedReader(new FileReader(file));
    		String line;
		    while ((line = br.readLine()) != null) {
		    	Post post = new Post();
		    	post.setRawData(line.replace(",", ""));
		    	posts.add(post);
		    }
		    return posts;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<Post>();
	}
	
	public HashMap<String, String> getImageTags(){
		
		//Map<String, String> imageTagMapping = new HashMap<String, String>();
		// Clarifi AI
		
		return null;
	}
	
	@SuppressWarnings("resource")
	public Set<String> getStopWords(String path){
		Set<String> words = new HashSet<String>();
		File file = new File(System.getProperty("user.dir") + "/" + path);
	    try {
    		BufferedReader br = new BufferedReader(new FileReader(file));
    		String line;
		    while ((line = br.readLine()) != null) {
		    	words.add(line);
		    }
		    return words;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new HashSet<String>();
	}
}
