package com.snp.models;

import java.util.ArrayList;
import java.util.List;

public class Post {
	
	private String rawData;
	private List<Sentence> sentences;
	
	public String getRawData() {
		return rawData;
	}
	public void setRawData(String rawData) {
		this.rawData = rawData;
	}
	public List<Sentence> getSentences() {
		return sentences;
	}
	public void setSentences(List<Sentence> sentences) {
		this.sentences = sentences;
	}
	
	public void populateSentences(String[] sentences){
		this.sentences = new ArrayList<Sentence>();
		for(String s : sentences){
			Sentence sentence = new Sentence(s);
			this.sentences.add(sentence);
		}
	}
	
}
