/**
 * 
 */
package com.snp.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.snp.learn.stemmer.TestApp;

/**
 * @author Shirish
 *
 */
public class DecomposeData {
	
	private String datasetPath;
	private List<String> sentences = new ArrayList<String>();	// List to store data sentences
	private int data_size;
	
	public DecomposeData(String datasetPath) {
		// TODO Auto-generated constructor stub
		this.datasetPath = datasetPath;
	}
	
	public String getDatasetPath() {
		return datasetPath;
	}
	public List<String> getSentences() {
		return sentences;
	}
	public int getData_size() {
		return data_size;
	}
	
	public void perform_pre_process() throws Throwable
	{
		File folder = new File(this.datasetPath);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++)
		{
			if (listOfFiles[i].isFile())
			{
				// Read dataset
				/*BufferedReader reader = new BufferedReader(new FileReader("dataset/"+listOfFiles[i].getName()));
				String line,file_sentence="";
				
				// Store dataset in a List for future reference
				while ((line = reader.readLine()) != null)
				{
					file_sentence=file_sentence+" "+line;
				}
				reader.close();
				sentences.add(file_sentence.trim());*/
				try{	
					TestApp test=new TestApp();
					String[] temp=test.sentence(this.datasetPath + "/" + listOfFiles[i].getName(),"english").split(" ");
					String sen="";
					for(int k=0;k<temp.length;k++)
					{
						if(temp[k].length()>2 && !temp[k].equals("and") && !temp[k].equals("the")){
							sen=sen+" "+temp[k];
						}
					}
					sentences.add(sen.trim());
					
					// Writing final output to respective files for later hashing and searching
					BufferedWriter outputWriter = new BufferedWriter(new FileWriter("buff.txt"));
					String[] temp1 = sen.trim().split(" ");
					for(int k=0;k<temp1.length;k++){
						outputWriter.write(temp1[k]+"\n");
					}
					outputWriter.flush();  
					outputWriter.close();
					
					File f1 = new File(""+listOfFiles[i]);
					listOfFiles[i].delete();
					File nf1 =new File("buff.txt");
					nf1.renameTo(f1);
					
					data_size = data_size+temp1.length;
					
				}catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}

}
