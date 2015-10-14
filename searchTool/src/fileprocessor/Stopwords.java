package fileprocessor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import datastructures.AVLTree;

public class Stopwords {

	//private static String stopwordfilepath = "C:\\file_repository\\stopwords.txt";
	private List<String> stopwords;
	private AVLTree<String> avlTree;
	
	public Stopwords(String filepath){	
		
		avlTree = new AVLTree<String>();
		stopwords = getStopwordsFromFile(filepath);
		for (String str : stopwords){
			avlTree.add(str);
		}		
	}	
	
	public AVLTree<String> getAvlTree() {
		return avlTree;
	}



	public void setAvlTree(AVLTree<String> avlTree) {
		this.avlTree = avlTree;
	}



	public boolean isStopword(String word){
		return avlTree.contains(word);
	}
	
	public List<String> getStopwordsFromFile(String path){		
				
		List<String> wordlist = new ArrayList<String>();
        BufferedReader br = null;
                 	
    		try {    			
    			String sCurrentLine;    			
    			br = new BufferedReader(new FileReader(path));
    	
    			while ((sCurrentLine = br.readLine()) != null) {
    			     String[] result = sCurrentLine.split("\\s");
    			     for (int x=0; x<result.length; x++)
    			    	 wordlist.add(result[x]);  				
    			}
    			
    		} catch (IOException e) {
    			e.printStackTrace();
    		} finally {
    			try {
    				if (br != null)br.close();
    			} catch (IOException ex) {
    				ex.printStackTrace();
    			}
    		}
    		
    		return wordlist;
        }	
	
}
