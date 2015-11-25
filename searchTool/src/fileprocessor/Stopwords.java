package fileprocessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import datastructures.AVLTree;

/**
 * @author Anthony Jackson
 * @id 11170365
 *	4BCT
 */

public class Stopwords {

	
	private List<String> stopwords;
	private AVLTree<String> avlTree;
	
	
	public Stopwords(){	
		
		avlTree = new AVLTree<String>();
		stopwords = getStopwords();
		for (String str : stopwords){
			avlTree.add(str);
		}		
	}
	
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
	
	private List<String> getStopwords(){
		
		String[] words = {"aaa", "a", "about", "above", "across", "after", "again", "against", "all", "almost", "alone", "along", "already", "also", "although", "always", "among", "an", "and", "another", "any", "anybody", "anyone", "anything", "anywhere", "are", "area", "areas", "around", "as", "ask", "asked", "asking", "asks", "at", "away", "b", "back", "backed", "backing", "backs", "be", "became", "because", "become", "becomes", "been", "before", "began", "behind", "being", "beings", "best", "better", "between", "big", "both", "but", "by", "c", "came", "can", "cannot", "case", "cases", "certain", "certainly", "clear", "clearly", "come", "could", "d", "did", "differ", "different", "differently", "do", "does", "done", "down", "down", "downed", "downing", "downs", "during", "e", "each", "early", "either", "end", "ended", "ending", "ends", "enough", "even", "evenly", "ever", "every", "everybody", "everyone", "everything", "everywhere", "f", "face", "faces", "fact", "facts", "far", "felt", "few", "find", "finds", "first", "for", "four", "from", "full", "fully", "further", "furthered", "furthering", "furthers", "g", "gave", "general", "generally", "get", "gets", "give", "given", "gives", "go", "going", "good", "goods", "got", "great", "greater", "greatest", "group", "grouped", "grouping", "groups", "h", "had", "has", "have", "having", "he", "her", "here", "herself", "high", "high", "high", "higher", "highest", "him", "himself", "his", "how", "however", "i", "if", "important", "in", "interest", "interested", "interesting", "interests", "into", "is", "it", "its", "itself", "j", "just", "k", "keep", "keeps", "kind", "knew", "know", "known", "knows", "l", "large", "largely", "last", "later", "latest", "least", "less", "let", "lets", "like", "likely", "long", "longer", "longest", "m", "made", "make", "making", "man", "many", "may", "me", "member", "members", "men", "might", "more", "most", "mostly", "mr", "mrs", "much", "must", "my", "myself", "n", "necessary", "need", "needed", "needing", "needs", "never", "new", "new", "newer", "newest", "next", "no", "nobody", "non", "noone", "not", "nothing", "now", "nowhere", "number", "numbers", "o", "of", "off", "often", "old", "older", "oldest", "on", "once", "one", "only", "open", "opened", "opening", "opens", "or", "order", "ordered", "ordering", "orders", "other", "others", "our", "out", "over", "p", "part", "parted", "parting", "parts", "per", "perhaps", "place", "places", "point", "pointed", "pointing", "points", "possible", "present", "presented", "presenting", "presents", "problem", "problems", "put", "puts", "q", "quite", "r", "rather", "really", "right", "right", "room", "rooms", "s", "said", "same", "saw", "say", "says", "second", "seconds", "see", "seem", "seemed", "seeming", "seems", "sees", "several", "shall", "she", "should", "show", "showed", "showing", "shows", "side", "sides", "since", "small", "smaller", "smallest", "so", "some", "somebody", "someone", "something", "somewhere", "state", "states", "still", "still", "such", "sure", "t", "take", "taken", "than", "that", "the", "their", "them", "then", "there", "therefore", "these", "they", "thing", "things", "think", "thinks", "this", "those", "though", "thought", "thoughts", "three", "through", "thus", "to", "today", "together", "too", "took", "toward", "turn", "turned", "turning", "turns", "two", "u", "under", "until", "up", "upon", "us", "use", "used", "uses", "v", "very", "w", "want", "wanted", "wanting", "wants", "was", "way", "ways", "we", "well", "wells", "went", "were", "what", "when", "where", "whether", "which", "while", "who", "whole", "whose", "why", "will", "with", "within", "without", "work", "worked", "working", "works", "would", "x", "y", "year", "years", "yet", "you", "young", "younger", "youngest", "your", "yours", "z", 
		};
		List<String> terms = new ArrayList<String>();
		for(String s : words){
			terms.add(s);
		}
		
		return terms;
		
	}
	
}
