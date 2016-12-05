package com.snp.learn;

import java.util.*;
import java.io.*;

public class BronKerbosch {

	int threshold=0;
	int threshold_cliques=0;
	int counter=0;
	int counter_c=0;
  /**
   * Integer array with a clone method.
   * @author Joerg Kurt Wegner
   */
  public static class Set  {
      public int[] vertex;
      public int size;

      public Set(int _size) {
          size = 0;
          vertex = new int[_size];
      }

      static public Set clone(final Set from, Set to) {
          to.size = from.size;
          for (int i = 0; i < from.size; i++)
              to.vertex[i] = from.vertex[i];
          return to;
      }
  }

  private int breakNumCliques = 0;
  private int numCliques = 0;

  private String cliquePath = "";
  /**
   *  Bron Kerbosch algorithm.
   */
  public BronKerbosch(String cliquePath){
	  this.cliquePath = cliquePath;
  }

  public int findCliquesnum(boolean[][] connected, ArrayList<String> vocab,int threshold) throws IOException
  {
      numCliques = 0;
      this.threshold=threshold;
      clear();

      return bronKerbosch(connected,vocab);
  }

	public int getThresholdCliques() throws IOException
	{
		System.out.println("Max Clique Size:"+counter_c);
		return this.threshold_cliques;
	}
  /**
   *  Description of the Method
   *
   * @param  connected  Description of the Parameter
   * @return            Description of the Return Value
   */
  public TreeSet<int[]> findCliques(boolean[][] connected,ArrayList<String> vocab) throws IOException
  {
      numCliques = 0;
      bronKerbosch(connected,vocab);
      return cliques;
  }

  /**
   *  Bron Kerbosch algorithm. The input graph is excpected in the form of a
   *  symmetrical boolean matrix connected (here as byte matrix with 0,1). The
   *  values of the diagonal elements must be 1.
   *
   * @param  adjMatrix  Graph adjacency matrix, the diagonal elements must be 1.
   * @return            The number of cliques found
   */
  private int bronKerbosch(boolean[][] adjMatrix, ArrayList<String> vocab) throws IOException
  {
      int[] ALL = new int[adjMatrix.length];
      Set actualMD = new Set(adjMatrix.length);
      Set best = new Set(adjMatrix.length);
      int c;

      //    cliques.clear();
      for (c = 0; c < adjMatrix.length; c++)
      {
          ALL[c] = c;
      }

      version2(adjMatrix, ALL, 0, adjMatrix.length, actualMD, best,vocab);

      actualMD = null;
      ALL = null;

      if ((breakNumCliques > 0) && (numCliques >= breakNumCliques))
      {
          return 0;
      }
      else
      {
          return numberOfCliques();
      }
  }

  /**
   *  Bron Kerbosch algorithm. The input graph is excpected in the form of a
   *  symmetrical boolean matrix connected (here as byte matrix with 0,1). The
   *  values of the diagonal elements must be 1.
   *
   * @param  adjMatrix          Graph adjacency matrix, the diagonal elements must be 1.
   * @param  oldMD              Description of the Parameter
   * @param  oldTestedSize      Description of the Parameter
   * @param  oldCandidateSize   Description of the Parameter
   * @param  actualMD           Description of the Parameter
   * @param  best               Description of the Parameter
   */
  private void version2(boolean[][] adjMatrix, int[] oldMD, int oldTestedSize,
      int oldCandidateSize, Set actualMD, Set best,ArrayList<String> vocab) throws IOException
  {
      if ((breakNumCliques > 0) && (numCliques >= breakNumCliques))
      {
          return;
      }

      int[] actualCandidates = new int[oldCandidateSize];
      int nod;
      int fixp = 0;
      int actualCandidateSize;
      int actualTestedSize;
      int i;
      int j;
      int count;
      int pos = 0;
      int p;
      int s = 0;
      int sel;
      int index2Tested;
      boolean fini = false;

      index2Tested = oldCandidateSize;
      nod = 0;

      // Determine each counter value and look for minimum
      // Branch and bound step
      // Is there a node in ND (represented by MD and index2Tested)
      // which is connected to all nodes in the candidate list CD
      // we are finished and backtracking will not be enabled
      for (i = 0; (i < oldCandidateSize) && (index2Tested != 0); i++)
      {
          p = oldMD[i];
          count = 0;

          // Count disconnections
          for (j = oldTestedSize;
                  (j < oldCandidateSize) && (count < index2Tested); j++)
          {
              if (adjMatrix[p][oldMD[j]] == false)
              {
                  count++;

                  // Save position of potential candidate
                  pos = j;
              }
          }

          // Test new minimum
          if (count < index2Tested)
          {
              fixp = p;
              index2Tested = count;

              if (i < oldTestedSize)
              {
                  s = pos;
              }
              else
              {
                  s = i;

                  // preincr
                  nod = 1;
              }
          }
      }

      // If fixed point initially chosen from candidates then
      // number of diconnections will be preincreased by one
      // Backtracking step for all nodes in the candidate list CD
      for (nod = index2Tested + nod; nod >= 1; nod--)
      {
          // Interchange
          p = oldMD[s];
          oldMD[s] = oldMD[oldTestedSize];
          sel = oldMD[oldTestedSize] = p;

          // Fill new set "not"
          actualCandidateSize = 0;

          for (i = 0; i < oldTestedSize; i++)
          {
              if (adjMatrix[sel][oldMD[i]] == true)
              {
                  actualCandidates[actualCandidateSize++] = oldMD[i];
              }
          }

          // Fill new set "candidates"
          actualTestedSize = actualCandidateSize;

          for (i = oldTestedSize + 1; i < oldCandidateSize; i++)
          {
              if (adjMatrix[sel][oldMD[i]] == true)
              {
                  actualCandidates[actualTestedSize++] = oldMD[i];
              }
          }

          // Add to "actual relevant nodes"
          actualMD.vertex[actualMD.size++] = sel;

          // so CD+1 and ND+1 are empty
          if (actualTestedSize == 0)
          {
              if (best.size < actualMD.size)
              {
                  // found a max clique
                  Set.clone(actualMD, best);
              }

              int[] tmpResult = new int[actualMD.size];
              System.arraycopy(actualMD.vertex, 0, tmpResult, 0, actualMD.size);
              addClique(tmpResult,vocab);
              numCliques++;
          }
          else
          {
              if (actualCandidateSize < actualTestedSize)
              {
                  version2(adjMatrix, actualCandidates, actualCandidateSize,
                      actualTestedSize, actualMD, best,vocab);
              }
          }

          if (fini)
          {
              break;
          }

          // move node from MD to ND
          // Remove from compsub
          actualMD.size--;

          // Add to "nod"
          oldTestedSize++;

          if (nod > 1)
          {
              try {
				// Select a candidate disconnected to the fixed point
				  for (s = oldTestedSize; adjMatrix[fixp][oldMD[s]] == true; s++)
				  {
				  }
			} catch (Exception e) {
				e.printStackTrace();
			}
          }
          // end selection
      }

      // Backtrackcycle
      actualCandidates = null;
  }
  
  private static class IntArrayComparator implements Comparator<int[]> {

    public int compare(int[] o1, int[] o2) {
      for (int i=0; i<o1.length; i++) {
        if (i >= o2.length) return -1;
        if (o1[i] < o2[i]) return -1;
        if (o1[i] > o2[i]) return 1;
      }
      if (o2.length > o1.length) return 1;
      else return 0;

    }
    
  }
  
  private TreeSet<int[]> cliques = new TreeSet<int[]>(new IntArrayComparator());
  
  private int numberOfCliques() {
    return cliques.size();
  }
  
  private void clear() {
    cliques.clear();
  }
  
  private void addClique(int[] clique,ArrayList<String> vocab) throws IOException {
    cliques.add(clique);
	
	if(clique.length>=threshold)
	{
		threshold_cliques++;
		System.out.println("\nMaximum Cliques:" + clique.length + "\n");
		for(int i=0;i<clique.length;i++)
		{
			System.out.println(" "+vocab.get(clique[i]));
		}
		System.out.println();
	}
	
	new File(cliquePath).mkdirs();
	
	BufferedWriter outputWriter = new BufferedWriter(new FileWriter(cliquePath + "/image" + counter + ".txt"));
	for(int k=0; k < clique.length; k++)
		outputWriter.write(vocab.get(clique[k])+"\n");
	outputWriter.flush();  
	outputWriter.close();
	counter++;
	
	if(clique.length>counter_c)
		counter_c=clique.length;
	/*System.out.println("\nMaximum Cliques:\n");
	for(int i=0;i<clique.length;i++)
	{
		if(clique.length > 1)
			System.out.println(" "+vocab.get(clique[i]));
	}
	System.out.println();*/
  }
}
