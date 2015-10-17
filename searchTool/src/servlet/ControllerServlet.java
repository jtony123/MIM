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
import retrieval.BooleanExpressionTree;
import retrieval.BooleanIR;

/**
 * Servlet implementation class ControllerServlet
 */
@WebServlet(name = "/ControllerServlet",
			loadOnStartup = 1,
			urlPatterns = {"/booleansearch"})
public class ControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public static String repoUrl = "C:\\a_files_repository";
	public static String stopwordFile = "C:\\a_stopwords_repository\\stopwords.txt";
	List<Document> documents = new ArrayList<Document>();
	List<Map<Integer,Document>> documentIndex = new ArrayList<Map<Integer,Document>>();
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
        	
        	Map<Integer,Document> mapping = new HashMap<Integer, Document>();
			mapping.put(document.getDocumentId(), document);
			
			documentIndex.add(mapping); 			 
        	documents.add(i, document);
        }
        
        invertedIndex = new AVLTree<String> ();
        for(Document document : documents){
        	for(String token : document.getDocumentTokens()){
        		invertedIndex.add(token);
        		BinaryNodeInterface<String> node = invertedIndex.getNode(token);
        		List<Integer> postings = node.getPostings();//.add(document.getDocumentId());
        		if(!postings.contains(document.getDocumentId())){
        			node.getPostings().add(document.getDocumentId());
        		}
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

		String userPath = request.getServletPath();
		String url = "/BooleanSearchPage.jsp";// + userPath + ".jsp";
		
        HttpSession session = request.getSession();
		if (userPath.equals("/booleansearch")) {

            String queryterms = request.getParameter("searchterms");
            System.out.println("got these : "+queryterms);
            
            List<Map<String,String>> matchingDocs = new ArrayList<Map<String,String>>();
            try {
				BooleanExpressionTree bet = new BooleanExpressionTree(queryterms, invertedIndex, documentsIdList);
				bet.printExpression();
				
				List<Integer> matchedDocs = bet.eval();
				
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
            session.setAttribute("queryterms", queryterms);
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
