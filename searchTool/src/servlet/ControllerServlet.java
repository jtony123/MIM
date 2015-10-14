package servlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
        	documentProcessor.process(files[i], stopwordTree);
        	documents.add(documentProcessor.getDocument());
        }			
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

            String terms = request.getParameter("searchterms");
            System.out.println("got these : "+terms);
            String[] termsArray = terms.split("\\s");
            List<Map<String,String>> matchingDocs = new ArrayList<Map<String,String>>();
            
            session.setAttribute("terms", terms);
            
            for (Document doc : documents){
            	for (int i=0; i<termsArray.length; ++i){
            		if(doc.getProcessedText().contains(termsArray[i].toLowerCase())){
            			
            			 Map<String,String> d = new HashMap<String, String>();
            			 d.put("name", doc.getDocumentName());
            			 d.put("url", doc.getFilePath());
            			 matchingDocs.add(d);
            		}
            	}
            	
            }
            
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
