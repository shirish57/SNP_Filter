package com.snp.models;

import java.util.Arrays;
import java.util.List;

public class Sentence {
	
	private String rawSentence;
	private List<String> words;
	private List<String> tags;
	private List<String> decomposedTags;
	private List<String> userMentions;
	
	public Sentence(String sentence){
		this.rawSentence = sentence;
		rawToWordList();
	}

	private void rawToWordList() {
		String[] extractWords = this.rawSentence.split(" ");
		this.words = Arrays.asList(extractWords);
	}
	
	public String getRawSentence() {
		return rawSentence;
	}
	public void setRawSentence(String rawSentence) {
		this.rawSentence = rawSentence;
		rawToWordList();
	}
	public List<String> getWords() {
		return words;
	}
	public void setWords(List<String> words) {
		this.words = words;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> hashTags) {
		this.tags = hashTags;
	}
	public List<String> getDecomposedTags() {
		return decomposedTags;
	}
	public void setDecomposedTags(List<String> decomposedTags) {
		this.decomposedTags = decomposedTags;
	}
	public List<String> getUserMentions() {
		return userMentions;
	}
	public void setUserMentions(List<String> userMentions) {
		this.userMentions = userMentions;
	}
}
