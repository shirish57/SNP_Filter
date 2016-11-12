/**
 * 
 */
package com.snp.filter1;

import java.awt.List;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * @author Rain
 * Filter out the Twitters that contain terrorism tags
 */
public class FilterSet {
	
	private ArrayList<String> tags; 
	
	public FilterSet() {
		tags = new ArrayList<String>();
		tags.add("Terrorism");
		tags.add("ISIS");
		tags.add("Jihad");
	}
	
	public FilterSet(ArrayList<String> tags) {
		this.tags = tags;
	}
	
	/**
	 * Filter out the twitter that has the terrorism tags. 
	 * The matching method is one key word match
	 * limitations:
	 * 	- name tag can't be matched. 
	 * @param twitters   represented as a string of tokens delimited by white spaces
	 * @return
	 */
	public ArrayList<String> filterSet1(ArrayList<String> twitters){
		ArrayList<String> res = new ArrayList<String>();
		for(String twitter : twitters) {
			ArrayList<String> twitter_tokens = tokenize(twitter);
			for(String token : twitter_tokens) {
				if(tags.contains(token)) {
					res.add(twitter);
					break;
				}
			}
		}
		return res; 
	}
	
	private ArrayList<String> tokenize(String str){
		ArrayList<String> res = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(str);
	     while (st.hasMoreTokens()) {
	         res.add(st.nextToken());
	     }
	     return res;
	}
	
	static public  void main(String [] args) {
		FilterSet fs = new FilterSet();
		ArrayList<String> twitters = new ArrayList<String>();
		twitters.add("Allahu Akbar Jews sent hell brave muslims");
		twitters.add("Allahu Akbhar Muslims kill Jew everything would change");
		twitters.add("month especially bless Jihad brothers successful");
		
		System.out.println(fs.filterSet1(twitters));
		/* Expected results: 
		 *  "month especially bless Jihad brothers successful"
		 * 
		 */
		
	}
	
}
