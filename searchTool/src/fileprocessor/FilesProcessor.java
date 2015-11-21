package fileprocessor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import datastructures.AVLTree;
import datastructures.BinaryNodeInterface;
import datastructures.Posting;

public class FilesProcessor {
	
	
	Map<Integer, Document> documentMap = new HashMap<Integer, Document>();
	List<Document> documents = new ArrayList<Document>();
	public double averageDocumentLength = 0;
	
	public Map<Integer, Document> getDocumentMap() {
		return documentMap;
	}

	public void setDocumentMap(Map<Integer, Document> documentMap) {
		this.documentMap = documentMap;
	}

	public List<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}


//	private void getInvertedIndex(AVLTree<String> stopwordTree, String repo){
//		
//	}
	
	



	public AVLTree<String> getInvertedIndex(AVLTree<String> stopwordTree, String repo){
						
		File[] files = new File(repo).listFiles();
		// preprocess each file found
		for (int i = 0; i<files.length; ++i) {
			//FilesProcessor documentProcessor = new FilesProcessor();
			Document document = processFile(files[i], stopwordTree);
			document.setDocumentId(i);
			documentMap.put(document.getDocumentId(), document);		 
			documents.add(i, document);
			averageDocumentLength += document.getDocumentTokens().size();
		}
		averageDocumentLength = averageDocumentLength/(double)documents.size();
		
		return getInvertedIndex();
	}
	
	public AVLTree<String> getInvertedIndex(List<Document> docs){
		
		this.documents = docs;
		for(Document doc : docs) {
			documentMap.put(doc.getDocumentId(), doc);
			averageDocumentLength += doc.getDocumentTokens().size();
		}
		averageDocumentLength = averageDocumentLength/(double)documents.size();
		return getInvertedIndex();
		
	}
	
	
	private AVLTree<String> getInvertedIndex(){

		AVLTree<String> invertedIndex = new AVLTree<String> ();
		for(Document document : documents){

			//using iterator over the list to get the positions of repeated terms in a document
			for(int i = 0;i<document.getDocumentTokens().size();++i){
				String token= document.getDocumentTokens().get(i);
				invertedIndex.add(token);
				BinaryNodeInterface<String> node = invertedIndex.getNode(token);
				Boolean newPosting = true;
				// check the postings to see if this term in this document is posted already
				// also need to check if position matches
				for(Posting posting:node.getPostings()){        			
					if(posting.getDocument().equals(document)){
						posting.incrementTermFrequency();
						posting.addTermPosition(i);
						document.adjustVectorLength((double)(posting.getTermFrequency()));											
						node.setIdf(documents.size());
						node.setIdfBM25(documents.size());
						newPosting = false;
					}
				}
				if(newPosting){
					Posting post = new Posting(document);
					post.setDocumentId(document.getDocumentId());
					post.incrementTermFrequency();
					post.addTermPosition(i);
					node.getPostings().add(post);
					node.setIdf(documents.size());
					node.setIdfBM25(documents.size());
					document.incrementVectorLength((double)(post.getTermFrequency()));
				}        		       		
				//}
			}
			// calculate vector length of document
			document.setVectorLength(Math.sqrt(document.getVectorLength()));

		}        

		invertedIndex.calculateNtf();
		// calculate bm25
		invertedIndex.calculateTfBM25(averageDocumentLength);
		
		return invertedIndex;	
	}
	
	
	

	
//	public String[] processQuery(String query){
//		
//		String[] terms = query.split(",|'|\\.|\\s");
//		for(int i=0;i<terms.length;++i){			
//			// Singularize and stem the words
//			Inflector inflector = new Inflector();	
//			Stemmer stemmer = new Stemmer();
//			//put everything in lowercase
//			terms[i] = terms[i].toLowerCase();
//			
//			String singularForm = inflector.singularize(terms[i]);
//			stemmer.add(singularForm);
//			terms[i] = stemmer.stem().toString();
//			stemmer.clear();			
//		}
//		return terms;
//	}
	
	public String processTerm(String term){
		
		Inflector inflector = new Inflector();	
		Stemmer stemmer = new Stemmer();
		//put everything in lowercase
		term = term.toLowerCase();
		
		String singularForm = inflector.singularize(term);
		stemmer.add(singularForm);
		term = stemmer.stem().toString();
		stemmer.clear();
		return term;
	}
	
	public Document processFile(File file, AVLTree<String> tree) {
		Document document = new Document();
		// read in the file
		FileLoader filesReader = new FileLoader();
		filesReader.readFiles(file, document);

		String[] terms = document.getFullText().split(",|'|\\.|\\s");

		for(int i=0;i<terms.length;++i){			
			// Singularize and stem the words
			Inflector inflector = new Inflector();	
			Stemmer stemmer = new Stemmer();
			//put everything in lowercase
			terms[i] = terms[i].toLowerCase();
			
			if(!tree.contains(terms[i])){
				String singularForm = inflector.singularize(terms[i]);
				stemmer.add(singularForm);
				document.addToken(stemmer.stem().toString());
				stemmer.clear();
			}
		}
		return document;
	}
}
