/**
 * 
 */
package com.snp.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Shirish
 *
 */
public class ConvertRawToFiles {
	
	/**
	 * Convert a file of data in each line to a list of files with each file representing a data line
	 * @param filePath
	 * @param outputPath
	 */
	public void convertRawToFiles(String filePath, String outputPath){
		File file = new File(System.getProperty("user.dir") + "/" + filePath);
	    try {
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			int i = 1;
		    while ((line = br.readLine()) != null) {
		    	saveLineToFile(line, outputPath + i + ".txt");
		    	i++;
		    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Save the line to a file with each word in a new line
	 * @param line
	 * @param outputPath
	 */
	public void saveLineToFile(String line, String outputPath){
		
		try {
			PrintWriter out = new PrintWriter(System.getProperty("user.dir") + "/" + outputPath);
			List<String> tokens = new ArrayList<String>();
			tokens = breakLineToWords(line);
			for(String word : tokens){
				out.println(word);
			}
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Break line to words
	 * @param line
	 * @return
	 */
	private List<String> breakLineToWords(String line){
		List<String> tokens = new ArrayList<String>();
		
		line = line.replace(",", "").replace("'", "").replace("&", " ");
		
		String[] words = line.split(" ");
		for(int i = 0; i < words.length; i++){
			words[i] = words[i].trim();
		}
		tokens = Arrays.asList(words);

		return tokens;
	}
}