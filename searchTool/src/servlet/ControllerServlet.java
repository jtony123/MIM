package servlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import fileprocessor.Document;
import fileprocessor.DocumentProcessor;
import fileprocessor.FilesReader;
import fileprocessor.Inflector;
import fileprocessor.Stemmer;
import fileprocessor.Stopwords;

/**
 * Servlet implementation class ControllerServlet
 */
@WebServlet(name = "/ControllerServlet",
			loadOnStartup = 1,
			urlPatterns = {"/search"})
public class ControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public static String repoUrl = "C:\\a_files_repository";
	public static String stopwordFile = "C:\\a_stopwords_repository\\stopwords.txt";
	List<Document> documents = new ArrayList<Document>();
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
        	//Document document = new Document();
        	DocumentProcessor documentProcessor = new DocumentProcessor();
        	//documentProcessor.getDocument().setDocumentId(new Integer(10));
        	Document document = documentProcessor.processFile(files[i], stopwordTree);
        	document.setDocumentId(i+1);
        	
        	System.out.println("doc id = " + document.getDocumentId());
        	documents.add(document);
        }
        
        invertedIndex = new AVLTree<String> ();
        for(Document document : documents){
        	for(String token : document.getDocumentTokens()){
        		invertedIndex.add(token);
        		BinaryNodeInterface<String> node = invertedIndex.getNode(token);
        		node.getPostings().add(document.getDocumentId());
        		node.incrementDocumentFrequency();       		
        	}
        }        
        invertedIndex.inorderTraverse();
        
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String url = "/SearchPage.jsp";
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

		System.out.println("got to doPost method");
		
		String userPath = request.getServletPath();
		String url = "/SearchPage.jsp";// + userPath + ".jsp";
		
        HttpSession session = request.getSession();
		if (userPath.equals("/search")) {

            String queryterms = request.getParameter("searchterms");
            System.out.println("got these : "+queryterms);
            
            //String[] termsArray = queryterms.split("\\s");
            DocumentProcessor documentProcessor = new DocumentProcessor();
            String[] termsArray = documentProcessor.processQuery(queryterms);
            List<Map<String,String>> matchingDocs = new ArrayList<Map<String,String>>();
            
            session.setAttribute("terms", queryterms);
            
            for (Document doc : documents){
            	for (int i=0; i<termsArray.length; ++i){
            		if(doc.getDocumentTokens().contains(termsArray[i].toLowerCase())){
            			
            			 Map<String,String> d = new HashMap<String, String>();
            			 d.put("name", doc.getDocumentName());
            			 d.put("url", doc.getFilePath());
            			 matchingDocs.add(d);
            		}
            	}            	
            }
            
            List<List> matchedPostingLists = new ArrayList<List>();
            for(String queryTerm : termsArray){
            	BinaryNodeInterface<String> node = invertedIndex.getNode(queryTerm);
            	if(node != null){
            		matchedPostingLists.add(node.getPostings());            		
            	}
            }
            // TODO: need to sort the matchedPostingLists ascending in order of size of list.
            
            List<Integer> matchedDocs = new ArrayList<Integer>();
            
            for(int i=0,j=0;i<matchedPostingLists.get(0).size() && j < matchedPostingLists.get(1).size();++i,++j){
            	if(matchedPostingLists.get(0).get(i) == matchedPostingLists.get(1).get(j)){
            		matchedDocs.add((Integer) matchedPostingLists.get(0).get(i));
            		++i;++j;
            	} else {
            		if(matchedPostingLists.get(0).get(i) == matchedPostingLists.get(1).get(j)){
            			
            		}
            	}
            }
            
            // intersect 1 and 2
            
            
            // intersect matched with 3
            
            // intersect matched with 4, etc...
            
            
            

            
            session.setAttribute("matchingDocs", matchingDocs);
            
            
            
            
		}
        try {
            request.getRequestDispatcher(url).forward(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
		
		//doGet(request, response);
	}

}
