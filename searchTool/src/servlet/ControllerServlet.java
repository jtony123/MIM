package servlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
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
import fileprocessor.DocumentProcessor;
import fileprocessor.FilesReader;
import fileprocessor.Inflector;
import fileprocessor.Stemmer;
import fileprocessor.Stopwords;
import retrieval.BooleanExpressionTree;
import retrieval.BooleanIR;
import retrieval.Token;

/**
 * Servlet implementation class ControllerServlet
 */
@WebServlet(name = "/ControllerServlet",
			loadOnStartup = 1,
			urlPatterns = {"/booleansearch",
							"/rankedsearch"})
public class ControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public static String repoUrl = "C:\\a_files_repository";
	public static String stopwordFile = "C:\\a_stopwords_repository\\stopwords.txt";
	List<Document> documents = new ArrayList<Document>();
	//List<Map<Integer,Document>> documentIndex = new ArrayList<Map<Integer,Document>>();
	Map<Integer, Document> documentMap = new HashMap<Integer, Document>();
	List<Integer> documentsIdList = new ArrayList<Integer>();
	AVLTree<String> stopwordTree;
	AVLTree<String> invertedIndex;

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
		stopwordTree = new Stopwords(stopwordFile).getAvlTree();
		// TODO: check if serialised indexer exists, otherwise build it.		
		
        File[] files = new File(repoUrl).listFiles();
		// preprocess each file found
        for (int i = 0; i<files.length; ++i) {
        	DocumentProcessor documentProcessor = new DocumentProcessor();
        	Document document = documentProcessor.processFile(files[i], stopwordTree);
        	document.setDocumentId(i);
        	documentsIdList.add(i);
        	
        	//Map<Integer,Document> mapping = new HashMap<Integer, Document>();
			//mapping.put(document.getDocumentId(), document);
			documentMap.put(document.getDocumentId(), document);
			//documentIndex.add(mapping); 			 
        	documents.add(i, document);
        }
        
        invertedIndex = new AVLTree<String> ();
        for(Document document : documents){
        	Double vectorLength = 0.0;
        	for(String token : document.getDocumentTokens()){
        		invertedIndex.add(token);
        		BinaryNodeInterface<String> node = invertedIndex.getNode(token);
        		Boolean newPosting = true;
        		// check the postings to see if this term is posted already
        		for(Posting posting:node.getPostings()){        			
        			if(posting.getDocumentId().equals(document.getDocumentId())){
        				
        				// get the current tf and subtract its square from the vector length
        				vectorLength = vectorLength - (posting.getTermFrequency() * posting.getTermFrequency()); 
						// found this term previously, just increment the tf
        				posting.incrementTermFrequency();
        				// now add its new square.
        				vectorLength = vectorLength + (posting.getTermFrequency() * posting.getTermFrequency());
        				newPosting = false;
        			}
        		}
        		if(newPosting){
        			Posting post = new Posting();
        			post.setDocuemtId(document.getDocumentId());
        			post.incrementTermFrequency();
        			node.getPostings().add(post);
        			node.incrementDocumentFrequency();
        			// add to the vector length
        			vectorLength += 1;
        		}        		       		
        	}
        	// calculate vector length of document
        	vectorLength = Math.sqrt(vectorLength);
        	document.setVectorLength(vectorLength);
        	
        }        
        //invertedIndex.inorderTraverse();
        invertedIndex.calculateTFidf(documents.size());
        
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String url = "/BooleanSearchPage.jsp";
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
		if (userPath.equals("/booleansearch")) {

            String queryterms = request.getParameter("searchterms");
            System.out.println("got these : "+queryterms);
            
            List<Map<String,String>> matchingDocs = new ArrayList<Map<String,String>>();
            try {
				BooleanExpressionTree booleanExpressionTree = new BooleanExpressionTree(queryterms, invertedIndex, documentsIdList);
				booleanExpressionTree.printExpression();
				
				List<Integer> matchedDocs = booleanExpressionTree.eval();
				
				  for(Integer i : matchedDocs){            	
		            	Document doc = documents.get(i);
		            	Map<String,String> d = new HashMap<String, String>();
		   			 	d.put("name", doc.getDocumentName());
		   			 	d.put("url", doc.getFilePath());
		   			 	matchingDocs.add(d);
		            }
				  
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            url = "/BooleanSearchPage.jsp";
            session.setAttribute("queryterms", queryterms);
            session.setAttribute("matchingDocs", matchingDocs);
         
		} else if(userPath.equals("/rankedsearch")){
			
			String query = request.getParameter("searchterms"); 
			
			// must clear all previous scores :( not very efficient
			for (Document doc : documents){
				doc.setDocumentScore(0.0);
			}
            
            String[] queryterms = query.split("\\s");
            System.out.println("got these : "+query);
        	DocumentProcessor dp = new DocumentProcessor();
        	List<Document> relevantDocs = new ArrayList<Document>();
        	
        	for(String term : queryterms){
        		term = dp.processTerm(term);
        		BinaryNodeInterface<String> node = invertedIndex.getNode(term);
        		
        		for(Posting post : node.getPostings()){
        			Document doc = documentMap.get(post.getDocumentId());
        			doc.addToDocumentScore(((double)post.getTermFrequency())/doc.getVectorLength());
        			if(!relevantDocs.contains(doc)) {
        				relevantDocs.add(doc);
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
		}
        try {
            request.getRequestDispatcher(url).forward(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
		
		//doGet(request, response);
	}

}
