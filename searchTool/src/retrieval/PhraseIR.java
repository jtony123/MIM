
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
 *	4BCT
 */

public class PhraseIR {
	
	public PhraseIR(){
	}
	
	public ArrayList<Document> getRelevantDocuments(String query, AVLTree<String> invertedIndex){
		ArrayList<Document> relevantDocs = new ArrayList<Document>();
		List<ArrayList<Posting>> postingslist = new ArrayList<ArrayList<Posting>>();
		
		List<Integer> intersectionIds = new ArrayList<Integer>();
		ArrayList<Integer> common;
		
		for(String term : query.split("\\s")){
			term = new FilesProcessor().processTerm(term);
			BinaryNodeInterface<String> node = invertedIndex.getNode(term);				
			
			if (node != null) {	
				for(Posting post : node.getPostings()){	
					Document doc = post.getDocument();
					doc.addToDocumentScore(node.getIdf()*post.getNtf());
					//doc.addToDocumentScore(((double)post.getTermFrequency())/doc.getVectorLength());							        			
				}
				// intersect this list with the previous. only keeping intersected list as we go
				
				if (postingslist.size()>0){
					common = new ArrayList<Integer>();
					for(Posting p : node.getPostings()){
						if(intersectionIds.contains(p.getDocumentId())){
							common.add(p.getDocumentId());	
						}					
					}
					intersectionIds = common;
					// create a new copy of the posting, leaving the original alone
					ArrayList<Posting> postingCopyList = new ArrayList<Posting>();
					for(Posting posting : node.getPostings()){
						postingCopyList.add(copyPosting(posting));
					}
					postingslist.add(postingCopyList);
					
				} else {
					// first posting list encountered
					ArrayList<Posting> postingCopyList = new ArrayList<Posting>();
					for(Posting posting : node.getPostings()){
						postingCopyList.add(copyPosting(posting));
					}
					postingslist.add(postingCopyList);
					
					for (Posting post : node.getPostings()){
						intersectionIds.add(post.getDocumentId());
					}
				}
			}
		}
		// cut the lists down to just the intersectedids
		for(ArrayList<Posting> list: postingslist){
			ArrayList<Posting> commonPosts = new ArrayList<Posting>();
			for(Posting post : list){
				if(intersectionIds.contains(post.getDocumentId())){
					commonPosts.add(post);
				}
			}
			list.clear();
			list.addAll(commonPosts);
		}
		
		// get the intersection of the term positions, also checks if they are in the correct order
		List<Integer> commonPositions = new ArrayList<Integer>();
		// outer loop moves downwards thru' the matrix
		for (int x = 0; x<intersectionIds.size(); ++x){
			// inner loop traverses each row of the matrix
			int y = 0;			
			for(; y<postingslist.size() ; ++y){
				if(y>0){
					// all subsequent traversals, intersecting the lists as we go
					List<Integer> shortlist = new ArrayList<Integer>();
					for(Integer z : postingslist.get(y).get(x).getTermPositions()){							
						for(Integer m : commonPositions){
							if(Integer.valueOf(m).equals((Integer.valueOf(z)-y))){
								shortlist.add(m);
							} 								
						}							
					}
					commonPositions = shortlist;					
				} else {
					// this is the initial traversal
					commonPositions = postingslist.get(0).get(x).getTermPositions();// the first list
				}					
			}
			// commonPositions holds the intersection of all the terms
			if(commonPositions.size()>0){
				relevantDocs.add(postingslist.get(y-1).get(x).getDocument());
			}
			commonPositions.clear();
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
	
	
	
	
	private Posting copyPosting(Posting posting){
		Posting copy = new Posting();
		copy.setDocumentId(Integer.valueOf(posting.getDocumentId()));
		copy.setDocument(posting.getDocument());
		for(Integer i:posting.getTermPositions()){
			copy.addTermPosition(new Integer(i));
		}		
		return copy;		
	}
	
	
		


}
