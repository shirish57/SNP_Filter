package com.snp.learn;


// Author: Shirish Singh
// The LNMIIT, Jaipur

import java.io.*;
import java.util.*;
import java.lang.Math;

import com.snp.learn.BronKerbosch;
import com.snp.learn.stemmer.TestApp;

public class Lism
{
	private ArrayList<String> vocab = new ArrayList<String>();	// Array List to store vocabulary elements
	private List<String> sentences = new ArrayList<String>();	// List to store data sentences
	private int co_occurrence[];	// Co-occurrence Matrix
	private int marginal_counts[];
	private int total_counts;
	private float marginal_probabilities[];
	private int threshold_co_occurence=2;
	private float threshold_consistency=(float)0.1;
	private int data_size;
	private int vocab_size;
	private String datasetPath = "";
	private String cliquePath = "";
	
	public Lism(String datasetPath, String cliquePath) {
		this.datasetPath = datasetPath;
		this.cliquePath = cliquePath;
	}
	
	public Lism(String datasetPath, String cliquePath, List<String> sentences, int data_size){
		this.datasetPath = datasetPath;
		this.cliquePath = cliquePath;
		this.sentences = sentences;
		this.data_size = data_size;
	}
	
	public void simple_lism() throws Throwable
	{
		//***********************************************************************
		File folder = new File(this.datasetPath);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++)
		{
			if (listOfFiles[i].isFile())
			{
				// Read dataset
				BufferedReader reader = new BufferedReader(new FileReader(this.datasetPath + "/" + listOfFiles[i].getName()));
				String line,file_sentence="";
				
				// Store dataset in a List for future reference
				while ((line = reader.readLine()) != null)
				{
					file_sentence=file_sentence+" "+line;
				}
				reader.close();
				sentences.add(file_sentence.trim());
				
				data_size = data_size + (file_sentence.split(" ")).length;
			}
		}
		//*********************************************************************************
	}
	
	public void calculate_vocab() throws Throwable
	{		
		// Calculate all unique words
		String[] temp;
		for(int i=0;i<sentences.size();i++)
		{
			temp = sentences.get(i).split(" ");
			for(int j=0;j<temp.length;j++)
			{
				if(vocab.indexOf(temp[j])==-1)
					vocab.add(temp[j]);
			}
		}
		vocab_size=vocab.size();
		
		// Write Vocab to file
		BufferedWriter outputWriter = null;
		outputWriter = new BufferedWriter(new FileWriter("vocab.txt"));
		for (int i = 0; i < vocab_size; i++) {
			outputWriter.write(vocab.get(i)+" ");
		}
		outputWriter.flush();
		outputWriter.close();
		
		System.out.println("Data Size: "+data_size);
		System.out.println("Vocab Size: "+vocab_size);
	}
	
	public void calculate_co_occurrence() throws IOException
	{
		co_occurrence=new int[vocab_size];
		BufferedWriter outputWriter = new BufferedWriter(new FileWriter("cooccurrence.txt"));
		for(int i=0;i<vocab_size;i++)
		{
			for(int j=0;j<sentences.size();j++)
			{
				if(sentences.get(j).indexOf(vocab.get(i)) >= 0)
				{
					for(int k=0;k<vocab_size;k++)
					{
						String[] temp = sentences.get(j).split(" ");
						for(String s: temp)
						{
							if(s.equals(vocab.get(k)))
								co_occurrence[k]++;
						}
					}
				}
			}
			for(int j=0;j<vocab_size;j++){
			outputWriter.write(co_occurrence[j]+" ");
			co_occurrence[j]=0;
			}
			outputWriter.write("\n");
		}
		outputWriter.flush();  
		outputWriter.close();
		
		/*// Write Co-occurrence to file
		BufferedWriter outputWriter = null;
		outputWriter = new BufferedWriter(new FileWriter("cooccurrence.txt"));
		for (int i=0;i<vocab_size;i++) {
			for(int j=0;j<vocab_size;j++){
			outputWriter.write(co_occurrence[i][j]+" ");
			}
			outputWriter.write("\n");
		}
		outputWriter.flush();  
		outputWriter.close();*/
		//vocab=null;
		co_occurrence=null;
	}
	
	public void calculate_threshold() throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader("cooccurrence.txt"));
		String line;
		String temp[];
		int l=0, max=0;		
		ArrayList<Integer> arr = new ArrayList<Integer>();
		
		while ((line = reader.readLine()) != null)
		{
			temp = line.split(" ");
			for(int j=0;j<temp.length;j++)
			{
				if(l<j)
				{
					if(Integer.parseInt(temp[j])!=0)
					{
						arr.add(Integer.parseInt(temp[j]));
					}
				}
			}
			l++;
		}
		reader.close();
		
		int diff1[]=new int[arr.size()];
		int diff2[]=new int[arr.size()];
		
		Collections.sort(arr);
	
		for(int i=0;i<arr.size()-1;i++)
			diff1[i]=Math.abs(arr.get(i+1)-arr.get(i));
		
		for(int i=0;i<arr.size()-1;i++)
		{
			diff2[i]=Math.abs(diff1[i+1]-diff1[i]);
			if(max<diff2[i])
				max=diff2[i];
		}
		
		threshold_co_occurence = 2;	//(new Double(((double)data_size/vocab_size))).intValue(); //max; // 1
		System.out.println("Threshold Co-occurrence: "+threshold_co_occurence);

		// Write the co-occurrence after comparing with threshold
		reader = new BufferedReader(new FileReader("cooccurrence.txt"));
		BufferedWriter outputWriter = new BufferedWriter(new FileWriter("cooccurrence_threshold.txt"));
		while ((line = reader.readLine()) != null)
		{
			temp = line.split(" ");
			for(int j=0;j<temp.length;j++)
			{
				int val=Integer.parseInt(temp[j]);
				if(val<threshold_co_occurence)
					val=0;
				outputWriter.write(val+" ");
			}
			outputWriter.write("\n");
		}
		reader.close();
		outputWriter.flush();  
		outputWriter.close();
	}
	
	public void calculate_marginal_counts() throws IOException
	{
		marginal_counts=new int[vocab_size];
		for(int i=0;i<vocab_size;i++)
			marginal_counts[i]=0;
		int l=0;
		String temp[];
		String line;
		
		File f = new File("marginal_counts.txt");
		File ff = new File("marginal_counts1.txt");
		if(f.exists() && !f.isDirectory())
		{
			f.renameTo(ff);
		}
		
		BufferedReader reader = new BufferedReader(new FileReader("cooccurrence_threshold.txt"));
		while ((line = reader.readLine()) != null)
		{
			temp = line.split(" ");
			for(int j=0;j<temp.length;j++)
			{
				if(l!=j)
					marginal_counts[j]=marginal_counts[j]+Integer.parseInt(temp[j]);
			}
			l++;
		}
		reader.close();
		
		BufferedWriter outputWriter = null;
		outputWriter = new BufferedWriter(new FileWriter("marginal_counts.txt"));
		for (int i=0;i<vocab_size;i++) {
			outputWriter.write(marginal_counts[i]+" ");
		}
		outputWriter.flush();  
		outputWriter.close();
		
		marginal_counts=null;
	}
	
	public void calculate_total_counts() throws IOException
	{
		total_counts=0;
		String temp[];
		String line;
		BufferedReader reader = new BufferedReader(new FileReader("marginal_counts.txt"));
		while ((line = reader.readLine()) != null)
		{
			temp = line.split(" ");
			for(int j=0;j<temp.length;j++)
				total_counts+=Integer.parseInt(temp[j]);
		}
		reader.close();
		
		total_counts=total_counts/2;	// Symmetric Matrix: Hence divide by 2
		System.out.println("\n\nTotal Counts: "+total_counts+"\n");
	}
	
	public void calculate_co_occurrence_probabilities() throws IOException
	{
		String temp[];
		String line;
		BufferedReader reader = new BufferedReader(new FileReader("cooccurrence_threshold.txt"));
		BufferedWriter outputWriter = new BufferedWriter(new FileWriter("co_occurrence_probabilities.txt"));
		while ((line = reader.readLine()) != null)
		{
			temp = line.split(" ");
			for(int j=0;j<temp.length;j++)
			{
				outputWriter.write(Float.parseFloat(temp[j])/total_counts+" ");
			}
			outputWriter.write("\n");
		}
		reader.close();
		outputWriter.flush();  
		outputWriter.close();
	}
	
	public void calculate_marginal_probabilities() throws IOException
	{
		String temp[];
		String line;
		BufferedReader reader = new BufferedReader(new FileReader("marginal_counts.txt"));
		BufferedWriter outputWriter = new BufferedWriter(new FileWriter("marginal_probabilities.txt"));
		while ((line = reader.readLine()) != null)
		{
			temp = line.split(" ");
			for(int j=0;j<temp.length;j++)
			{
				outputWriter.write(Float.parseFloat(temp[j])/total_counts+" ");
			}
		}
		reader.close();
		outputWriter.flush();  
		outputWriter.close();
	}
	
	public void calculate_consistency() throws IOException
	{
		String temp[];
		String line;
		BufferedReader reader = new BufferedReader(new FileReader("marginal_probabilities.txt"));
		while ((line = reader.readLine()) != null)
		{
			temp = line.split(" ");
			marginal_probabilities=new float[temp.length];
			for(int j=0;j<temp.length;j++)
			{
				marginal_probabilities[j]=Float.parseFloat(temp[j]);
			}
		}
		reader.close();
		
		int l=0;
		BufferedReader reader1 = new BufferedReader(new FileReader("co_occurrence_probabilities.txt"));
		BufferedWriter outputWriter = new BufferedWriter(new FileWriter("consistency.txt"));
		while ((line = reader1.readLine()) != null)
		{
			temp = line.split(" ");
			for(int j=0;j<temp.length;j++)
			{
				float val=0;
				float consistency=(float)Math.log(Float.parseFloat(temp[j])/(marginal_probabilities[l]*marginal_probabilities[j]));
				if(0>consistency || Float.parseFloat(temp[j])==0 || marginal_probabilities[l]*marginal_probabilities[j]==0)
					val=0;
				else
					val=consistency;
				
				if(Math.log(Float.parseFloat(temp[j]))!=0)
					val=val/(float)(-Math.log(Float.parseFloat(temp[j])));
				else
					val=0;
				outputWriter.write(val+" ");
			}
			l++;
			outputWriter.write("\n");
		}
		reader1.close();
		outputWriter.flush();  
		outputWriter.close();
	}
	
	public void calculate_consistency_threshold() throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader("consistency.txt"));
		String line;
		String temp[];
		int l=0;
		float max=0;
		ArrayList<Float> arr = new ArrayList<Float>();
		
		while ((line = reader.readLine()) != null)
		{
			temp = line.split(" ");
			for(int j=0;j<temp.length;j++)
			{
				if(l<j)
				{
					if(Float.parseFloat(temp[j])!=0)
					{
						arr.add(Float.parseFloat(temp[j]));
					}
				}
			}
			l++;
		}
		reader.close();
		
		float diff1[]=new float[arr.size()];
		float diff2[]=new float[arr.size()];
		
		Collections.sort(arr);
		System.out.println("Size:" + arr.size());
		
		for(int i=0;i<arr.size()-1;i++)
		{
			diff1[i]=Math.abs(arr.get(i+1)-arr.get(i));
		}
		for(int i=0;i<arr.size()-1;i++)
		{
			diff2[i]=Math.abs(diff1[i+1]-diff1[i]);
			if(max<diff2[i])
				max=diff2[i];
		}
		
		threshold_consistency = max;
		System.out.println("\n\nThreshold Consistency: "+threshold_consistency);
		
		// Write the co-occurrence after comparing with threshold
		reader = new BufferedReader(new FileReader("consistency.txt"));
		BufferedWriter outputWriter = new BufferedWriter(new FileWriter("consistency_threshold.txt"));
		while ((line = reader.readLine()) != null)
		{
			temp = line.split(" ");
			for(int j=0;j<temp.length;j++)
			{
				float val=Float.parseFloat(temp[j]);
				if(val<threshold_consistency)
					val=0;
				outputWriter.write(val+" ");
			}
			outputWriter.write("\n");
		}
		reader.close();
		outputWriter.flush();  
		outputWriter.close();
		
	}
	
	public void denoise() throws IOException
	{
		for(int z=0;z<2;z++)
		{
		String[] temp,temp1;
		String line, line1;
		BufferedReader reader = new BufferedReader(new FileReader("cooccurrence_threshold.txt"));
		BufferedReader reader1 = new BufferedReader(new FileReader("consistency_threshold.txt"));
		BufferedWriter outputWriter = new BufferedWriter(new FileWriter("buff1.txt"));
		BufferedWriter outputWriter1 = new BufferedWriter(new FileWriter("buff2.txt"));
		while ((line = reader.readLine()) != null)
		{
			temp = line.split(" ");
			line1 = reader1.readLine();
			temp1 = line1.split(" ");
			for(int j=0;j<temp.length;j++)
			{
				int co_val=Integer.parseInt(temp[j]);
				float val=Float.parseFloat(temp1[j]);
				if(val<threshold_consistency)
				{
					co_val=0;
					val=0;
				}
				outputWriter.write(co_val+" ");
				outputWriter1.write(val+" ");
			}
			outputWriter.write("\n");
			outputWriter1.write("\n");
		}
		reader.close();
		reader1.close();
		outputWriter.flush();  
		outputWriter.close();
		outputWriter1.flush();  
		outputWriter1.close();
		
		File f1 = new File("cooccurrence_threshold.txt");
		File f2 = new File("consistency_threshold.txt");
    	f1.delete();
		f2.delete();
		f1 = new File("cooccurrence_threshold.txt");
		f2 = new File("consistency_threshold.txt");
		File nf1 =new File("buff1.txt");
		File nf2 =new File("buff2.txt");
		nf1.renameTo(f1);
		nf2.renameTo(f2);
			
		this.calculate_marginal_counts();
		this.calculate_total_counts();
		this.calculate_co_occurrence_probabilities();
		this.calculate_marginal_probabilities();
		this.calculate_consistency();
		System.out.println("\n\nTotal Counts: "+z+" Iteration is "+total_counts+"\n");
		}
	}
	
	public void calculate_clique_graph() throws IOException
	{
		boolean graph[][]=new boolean[vocab_size][vocab_size];
		for(int j=0;j<vocab_size;j++)
			for(int k=0;k<vocab_size;k++)
				graph[k][j]=false;
		int k=0;
		String[] temp;
		String line;
		BufferedReader reader = new BufferedReader(new FileReader("consistency_threshold.txt"));
		BufferedWriter outputWriter = new BufferedWriter(new FileWriter("graph.txt"));
		outputWriter.write(vocab_size+"\n");
		while ((line = reader.readLine()) != null)
		{
			temp = line.split(" ");
			//System.out.println(temp.length+" "+k);
			for(int j=0;j<temp.length;j++)
			{
				float val=Float.parseFloat(temp[j]);
				if(val>0.0)
				{
					outputWriter.write(1+" ");
					graph[k][j]=true;
				}
				else
				{
					outputWriter.write(0+" ");
					graph[k][j]=false;
				}
			}
			outputWriter.write("\n");
			k++;
		}
		reader.close();
		outputWriter.flush();  
		outputWriter.close();
		
		/*clique_graph=new boolean[vocab_size][vocab_size];
		
		for(int i=0;i<vocab_size;i++)
		{
			for(int j=0;j<vocab_size;j++)
			{
				if(i==j)
					clique_graph[i][j]=true;
				else if(consistency[i][j]>0.0)
					clique_graph[i][j]=true;
				else
					clique_graph[i][j]=false;
			}
		}*/
		
		BronKerbosch bk=new	BronKerbosch(cliquePath);
		System.out.println("\n\nTotal Cliques:" + bk.findCliquesnum(graph,vocab,2) + "\n");
		System.out.println("\n\nTotal Threshold Cliques:" + bk.getThresholdCliques() + "\n");
	}
	
	/*public static void main(String args[]) throws Throwable
	{
		// --BREAK COMPOUND WORDS
		Decomposer te = new Decomposer(dictionaryPath, datasetPath);
		
		Lism ob=new Lism();
		// --STEMMING
		ob.perform_pre_process();
		//ob.simple_lism();
		
		// --CALCULATE VOCABULARY
		ob.calculate_vocab();
		
		// --Stage 1: LISM-Counting
		ob.calculate_co_occurrence();
		ob.calculate_threshold();
		ob.calculate_marginal_counts();
		ob.calculate_total_counts();
		ob.calculate_co_occurrence_probabilities();
		ob.calculate_marginal_probabilities();
		
		// --Stage 2: LISM-Consistency
		ob.calculate_consistency();
		ob.calculate_consistency_threshold();
		
		// --Stage 3: LISM-Denoise
		ob.denoise();		
		
		// --Stage 4: LISM-Discovery
		ob.calculate_clique_graph();
	}*/
	
}