package servlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import datastructures.AVLTree;
import datastructures.BinaryNodeInterface;
import datastructures.Posting;
import fileprocessor.Document;
import fileprocessor.FilesProcessor;
import fileprocessor.Stopwords;
import retrieval.BooleanIR;
import retrieval.PhraseIR;

/**
 * Servlet implementation class ControllerServlet
 */
@WebServlet(name = "/ControllerServlet",
loadOnStartup = 1,
urlPatterns = {"/booleansearch",
				"/rankedsearch",
				"/phrasesearch",
				"/bm25rankedsearch",
				"/bm25",
				"/testcollection"})

public class ControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	//public static String repoUrl = "C:\\a_files_repository";
	//public static String repoUrl = "C:\\aa_files_repository\\CranField";
	public static String repoUrl = "C:\\a_med\\files";
	public static String stopwordFile = "C:\\a_stopwords_repository\\stopwords.txt";
	List<Document> documents;// = new ArrayList<Document>();
	Map<Integer, Document> documentMap;// = new HashMap<Integer, Document>();
	AVLTree<String> stopwordTree;
	AVLTree<String> invertedIndex;
	//public double averageDocumentLength = 0;

	/**
	 * Default constructor. 
	 */
	public ControllerServlet() {
		// TODO Auto-generated constructor stub
	}


	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {

		// build a balanced tree of stopwords.
		FilesProcessor filesProcessor = new FilesProcessor();
		stopwordTree = new Stopwords(stopwordFile).getAvlTree();
		invertedIndex = filesProcessor.getInvertedIndex(stopwordTree, repoUrl);
		documentMap = filesProcessor.getDocumentMap();
		documents = filesProcessor.getDocuments();
		// TODO: check if serialised indexer exists, otherwise build it.		

		
//		
//		File[] files = new File(repoUrl).listFiles();
//		// preprocess each file found
//		for (int i = 0; i<files.length; ++i) {
//			FilesProcessor documentProcessor = new FilesProcessor();
//			Document document = documentProcessor.processFile(files[i], stopwordTree);
//			document.setDocumentId(i);
//			documentMap.put(document.getDocumentId(), document);		 
//			documents.add(i, document);
//			averageDocumentLength += document.getDocumentTokens().size();
//		}
//		averageDocumentLength = averageDocumentLength/(double)documents.size();
//		
//
//		invertedIndex = new AVLTree<String> ();
//		for(Document document : documents){
//
//			//using iterator over the list to get the positions of repeated terms in a document
//			for(int i = 0;i<document.getDocumentTokens().size();++i){
//				String token= document.getDocumentTokens().get(i);
//				invertedIndex.add(token);
//				BinaryNodeInterface<String> node = invertedIndex.getNode(token);
//				Boolean newPosting = true;
//				// check the postings to see if this term in this document is posted already
//				// also need to check if position matches
//				for(Posting posting:node.getPostings()){        			
//					if(posting.getDocument().equals(document)){
//						posting.incrementTermFrequency();
//						posting.addTermPosition(i);
//						document.adjustVectorLength((double)(posting.getTermFrequency()));											
//						node.setIdf(documents.size());
//						node.setIdfBM25(documents.size());
//						newPosting = false;
//					}
//				}
//				if(newPosting){
//					Posting post = new Posting(document);
//					post.setDocumentId(document.getDocumentId());
//					post.incrementTermFrequency();
//					post.addTermPosition(i);
//					node.getPostings().add(post);
//					node.setIdf(documents.size());
//					node.setIdfBM25(documents.size());
//					document.incrementVectorLength((double)(post.getTermFrequency()));
//				}        		       		
//				//}
//			}
//			// calculate vector length of document
//			document.setVectorLength(Math.sqrt(document.getVectorLength()));//vectorLength);//
//
//		}        
//		//invertedIndex.inorderTraverse();
//		invertedIndex.calculateNtf();
//		// calculate bm25
//		invertedIndex.calculateTfBM25(averageDocumentLength);
//		
//		System.out.println("Ready");
		
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String userPath = request.getServletPath();
		String url = null;

		if (userPath.equals("/booleansearch")) {
			url = "/BooleanSearchPage.jsp";
		} else if (userPath.equals("/rankedsearch")){
			url = "/RankedSearchPage.jsp";
		} else if (userPath.equals("/phrasesearch")){
			url = "/PhraseSearchPage.jsp";
		} else if (userPath.equals("/bm25rankedsearch")){
			url = "/BM25RankedSearchPage.jsp";
		} else if (userPath.equals("/testcollection")){
			url = "/PrecisionRecallGraphs.jsp";
		}

		try {
			request.getRequestDispatcher(url).forward(request, response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String userPath = request.getServletPath();
		String url = null;

		HttpSession session = request.getSession();
		if (userPath.equals("/booleansearch")) {// *******************  boolean search   ********************
			
			List<Document> matchingDocs = new ArrayList<Document>();
				
			for(Integer i : new BooleanIR(request.getParameter("searchterms"),invertedIndex, documentMap.size()).eval()){
				matchingDocs.add(documentMap.get(i));
			}
			
			url = "/BooleanSearchPage.jsp";
			session.setAttribute("queryterms", request.getParameter("searchterms"));
			session.setAttribute("matchingDocuments", matchingDocs);

		} else if(userPath.equals("/rankedsearch")){ // *************   simple ranked search   ********************

			
			String query = request.getParameter("searchterms"); 
			// must clear all previous scores :( not very efficient
			for (Document doc : documents){
				doc.setDocumentScore(0.0);
			}
			// TODO: change relevantDocs to a priority queue, pull the top 10 results from it, more efficient than sorting a list
			List<Document> relevantDocs = new ArrayList<Document>();

			for(String term : query.split("\\s")){
				term = new FilesProcessor().processTerm(term);
				BinaryNodeInterface<String> node = invertedIndex.getNode(term);

				if (node != null) {
					for(Posting post : node.getPostings()){
						Document doc = post.getDocument();
						// calculating the score for this document.
						//doc.addToDocumentScore(((double)post.getTermFrequency())/doc.getVectorLength());
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
			session.setAttribute("query", query);
			session.setAttribute("relevantDocs", relevantDocs);

			url = "/RankedSearchPage.jsp";

		} else if(userPath.equals("/phrasesearch")){
			// ************************************************   phrase search    ********************
			String query = request.getParameter("searchterms"); 

			// must clear all previous scores :( not very efficient
			for (Document doc : documents){
				doc.setDocumentScore(0.0);
			}

			PhraseIR phraseIR = new PhraseIR();

			List<Document> docsToReturn = phraseIR.getRelevantDocuments(query, invertedIndex);
			Collections.sort(docsToReturn, new Comparator<Document>() {
				@Override
				public int compare(final Document doc1, final Document doc2) {
					return doc1.getDocumentScore().compareTo(doc2.getDocumentScore());
				}
			});
			Collections.reverse(docsToReturn);
			session.setAttribute("query", query);
			session.setAttribute("docsToReturn", docsToReturn);

			url = "/PhraseSearchPage.jsp";


		} else if(userPath.equals("/bm25rankedsearch")){
			// ************************************************   BM25 search    ********************
			String query = request.getParameter("searchterms"); 
			String NumResults = request.getParameter("numResults");
			//int k = Integer.parseInt(NumResults);
			System.out.println(query);
			// must clear all previous scores :( not very efficient
			for (Document doc : documents){
				doc.setDocumentScore(0.0);
			}
			// TODO: change relevantDocs to a priority queue, pull the top 10 results from it, more efficient than sorting a list
			List<Document> relevantDocs = new ArrayList<Document>();

			for(String term : query.split("\\s")){
				term = new FilesProcessor().processTerm(term);
				BinaryNodeInterface<String> node = invertedIndex.getNode(term);

				if (node != null) {
					for(Posting post : node.getPostings()){
						Document doc = post.getDocument();
						// calculating the score for this document.
						//doc.addToDocumentScore(node.getIdf()*post.getNtf());
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
			
			// limit size of results set to top 20
			
			int L = 20;

			//List<Document> newList = new ArrayList<>(relevantDocs.subList(0,L));
			
			//session.setAttribute("query", query);
			session.setAttribute("relevantDocs", new ArrayList<>(relevantDocs.subList(0,L)));

			url = "/BM25RankedSearchPage.jsp";
			
			
			
		} else if(userPath.equals("/testcollection")){
			// TODO: 
			String path = request.getParameter("path");
			
			if(!path.equals("")){
				System.out.println("passed "+path);
//				FilesProcessor filesProcessor = new FilesProcessor();
//				stopwordTree = new Stopwords(stopwordFile).getAvlTree();
//				invertedIndex = filesProcessor.getInvertedIndex(stopwordTree, path);
//				documentMap = filesProcessor.getDocumentMap();
//				documents = filesProcessor.getDocuments();
				
				
				
				
				
				
			} else {
				System.out.println("failed "+path);
			}
			
			
			
			
			
			
			url = "/PrecisionRecallGraphs.jsp";
		}



		try {
			
			request.getRequestDispatcher(url).forward(request, response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		//doGet(request, response);
	}

}
