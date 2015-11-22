/**
 * 
 */
package retrieval;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import datastructures.AVLTree;
import datastructures.BinaryNodeInterface;
import datastructures.Posting;
import fileprocessor.Document;
import fileprocessor.FilesProcessor;

/**
 * @author Anthony Jackson
 * @id 11170365
 *
 */
public class RankedIR {

	public RankedIR(){
		
	}
	
	
	public ArrayList<Document> getBM25RelevantDocuments(String query, AVLTree<String> invertedIndex){
		
		ArrayList<Document> relevantDocs = new ArrayList<Document>();

		for(String term : query.split("\\s")){
			term = new FilesProcessor().processTerm(term);
			BinaryNodeInterface<String> node = invertedIndex.getNode(term);

			if (node != null) {
				for(Posting post : node.getPostings()){
					Document doc = post.getDocument();
					doc.addToDocumentScore(node.getIdfBM25()*post.getTfBM25());
					// only add the document once to the collection
					if(!relevantDocs.contains(doc)) {
						relevantDocs.add(doc);
					}	        			
				} 
			}
		}        	
		Collections.sort(relevantDocs, new Comparator<Document>() {
			@Override
			public int compare(final Document doc1, final Document doc2) {
				return doc1.getDocumentScore().compareTo(doc2.getDocumentScore());
			}
		});
		Collections.reverse(relevantDocs);
		
		return relevantDocs;
		
	}
	
	
	
	public ArrayList<Document> getVSRelevantDocuments(String query, AVLTree<String> invertedIndex){
		
		
		ArrayList<Document> relevantDocs = new ArrayList<Document>();
		for(String term : query.split("\\s")){
			term = new FilesProcessor().processTerm(term);
			BinaryNodeInterface<String> node = invertedIndex.getNode(term);

			if (node != null) {
				for(Posting post : node.getPostings()){
					Document doc = post.getDocument();
					doc.addToDocumentScore(node.getIdf()*post.getNtf());
					// only add the document once to the collection
					if(!relevantDocs.contains(doc)) {
						relevantDocs.add(doc);
					}	        			
				} 
			}
		}        	
		Collections.sort(relevantDocs, new Comparator<Document>() {
			@Override
			public int compare(final Document doc1, final Document doc2) {
				return doc1.getDocumentScore().compareTo(doc2.getDocumentScore());
			}
		});
		Collections.reverse(relevantDocs);	
		
		
		return relevantDocs;
		
	}
	
	
	
	
	
}
