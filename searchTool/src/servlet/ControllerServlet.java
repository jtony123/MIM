package servlet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;

import datastructures.AVLTree;
import datastructures.BinaryNodeInterface;
import datastructures.Posting;
import fileprocessor.Document;
import fileprocessor.FilesProcessor;
import fileprocessor.Stopwords;
import retrieval.BooleanIR;
import retrieval.DocScore;
import retrieval.PhraseIR;
import retrieval.RankedIR;
import testcorpushandler.DocumentBuilder;
import testcorpushandler.Query;
import testcorpushandler.QueryRelevanceBuilder;

/**
 * Servlet implementation class ControllerServlet
 * 
 * 
public class FileUploadServlet extends HttpServlet {

    private final static Logger LOGGER = 
            Logger.getLogger(FileUploadServlet.class.getCanonicalName());
 * 
 * 
 */
@WebServlet(name = "/ControllerServlet",
loadOnStartup = 1,
urlPatterns = {"/testcollection",
				"/booleansearch",
				"/rankedsearch",
				"/phrasesearch",
				"/bm25rankedsearch",
				"/bm25",
				"/startnewtest",
				"/nextqueryprecisionrecall",
				"/prevqueryprecisionrecall",
				"/upload"})
@MultipartConfig
public class ControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private final static Logger LOGGER = 
            Logger.getLogger(ControllerServlet.class.getCanonicalName());

	public static String repoUrl = "C:\\a_files_repository";
	//public static String repoUrl = "C:\\aa_files_repository\\CranField";
	//public static String repoUrl = "C:\\a_med\\files";
	public static String stopwordFile = "C:\\a_stopwords_repository\\stopwords.txt";
	List<Document> documents;// = new ArrayList<Document>();
	Map<Integer, Document> documentMap;// = new HashMap<Integer, Document>();
	AVLTree<String> stopwordTree;
	AVLTree<String> invertedIndex;
	List<Query> queries;
	String uploadPath = null;

	List<List<DocScore>> docQryScores = new ArrayList<List<DocScore>>();
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
		
//		uploadPath = config.getServletContext().getRealPath("")
//			    + File.separator + "upload";
		uploadPath = config.getServletContext().getRealPath("") + "upload";
		// creates the directory if it does not exist
					File uploadDir = new File(uploadPath);
					if (!uploadDir.exists()) {
					    uploadDir.mkdir();
					}
		// build a balanced tree of stopwords.
		FilesProcessor filesProcessor = new FilesProcessor();
		stopwordTree = new Stopwords(stopwordFile).getAvlTree();
//		invertedIndex = filesProcessor.getInvertedIndex(stopwordTree, repoUrl);
//		documentMap = filesProcessor.getDocumentMap();
//		documents = filesProcessor.getDocuments();
//		invertedIndex.inorderTraverse();
		// TODO: check if serialised indexer exists, otherwise build it.		

	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String userPath = request.getServletPath();
		String url = null;
		HttpSession session = request.getSession();
		session.setAttribute("relevantDocs", null);

		if (userPath.equals("/testcollection")) {
			url = "/PrecisionRecallTest.jsp";
		} else if (userPath.equals("/rankedsearch")){
			url = "/RankedSearchPage.jsp";
		} else if (userPath.equals("/phrasesearch")){
			url = "/PhraseSearchPage.jsp";
		} else if (userPath.equals("/bm25rankedsearch")){
			url = "/BM25RankedSearchPage.jsp";
		} else if (userPath.equals("/booleansearch")){
			url = "/BooleanSearchPage.jsp";
		} else if (userPath.equals("/queryprecisionrecall")){
			url = "/PrecisionRecallGraphs.jsp";
		} else if (userPath.equals("/startnewtest")){
			url = "/PrecisionRecallTest.jsp";
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
		long starttime = 0;
		long invIndexTime = 0;
		long qryTestTime = 0;

		HttpSession session = request.getSession();
		
		
		if (userPath.equals("/booleansearch")) {// *******************  boolean search   ********************
			
			List<Document> matchingDocs = new ArrayList<Document>();
			BooleanIR booleanIR = new BooleanIR(request.getParameter("searchterms"),invertedIndex, documentMap.size());
				
			for(Integer i : booleanIR.eval()){
				matchingDocs.add(documentMap.get(i));
			}
			
			url = "/BooleanSearchPage.jsp";
			session.setAttribute("queryterms", request.getParameter("searchterms"));
			session.setAttribute("matchingDocuments", matchingDocs);

			
			
		} else if(userPath.equals("/rankedsearch")){
			
			String query = request.getParameter("searchterms"); 		
			resetScores();
			RankedIR vsIR = new RankedIR();
			List<Document> relevantDocs 
				= vsIR.getVSRelevantDocuments(query, invertedIndex);

			session.setAttribute("query", query);
			session.setAttribute("relevantDocs", relevantDocs);

			url = "/RankedSearchPage.jsp";

			
			
		} else if(userPath.equals("/phrasesearch")){
			
			String query = request.getParameter("searchterms"); 
			resetScores();
			PhraseIR phraseIR = new PhraseIR();
			List<Document> docsToReturn 
				= phraseIR.getRelevantDocuments(query, invertedIndex);

			session.setAttribute("query", query);
			session.setAttribute("docsToReturn", docsToReturn);

			url = "/PhraseSearchPage.jsp";


			
		} else if(userPath.equals("/bm25rankedsearch")){

			String query = request.getParameter("searchterms"); 
			resetScores();
			RankedIR vsIR = new RankedIR();
			List<Document> relevantDocs = vsIR.getBM25RelevantDocuments(query, invertedIndex);

			session.setAttribute("relevantDocs", relevantDocs);
			url = "/BM25RankedSearchPage.jsp";
			
			
			
//		} else if(userPath.equals("/testcollection")){
//			// TODO: 
//			
//			starttime = System.currentTimeMillis();
//			String path = request.getParameter("path");		
//			//String absoluteDiskPath;
//			if(path.equals("")){
//				path = getServletContext().getRealPath("\\WEB-INF\\defaultCollection");
//				//path = request.getParameter("defpath");
//				//path = "\\defaultCollection";
//			}
//			System.out.println("path = "+path);
//			
//			if(!path.equals("")){
//				
//				File[] files = new File(path).listFiles();
//				
//				String all = "";
//				String qry = "";
//				String rel = "";
//				for(int i = 0; i<files.length;++i){
//					if(files[i].getName().contains("ALL")){
//						all = "\\"+files[i].getName();
//					} else if (files[i].getName().contains("QRY")){
//						qry = "\\"+files[i].getName();
//					} else if (files[i].getName().contains("REL")){
//						rel = "\\"+files[i].getName();
//					}
//				}
//				
//				// build a new inverted Index for this collection				
//				DocumentBuilder db = new DocumentBuilder(path + all, stopwordTree);
//				FilesProcessor filesProcessor = new FilesProcessor();
//				invertedIndex = filesProcessor.getInvertedIndex(db.getDocuments());
//				documentMap = filesProcessor.getDocumentMap();
//				documents = filesProcessor.getDocuments();
//				
//				invIndexTime = System.currentTimeMillis();
//				
//				QueryRelevanceBuilder qrb = new QueryRelevanceBuilder(path + qry, path + rel);
//				queries = qrb.getQueries();
//				
//				// submit each query and note the results
//				for (Query q: queries){
//					
//					resetScores();
//					
//					RankedIR vsIR = new RankedIR();
//					List<Document> relevantDocs = vsIR.getBM25RelevantDocuments(q.getQueryString(), invertedIndex);
//					
//					List<DocScore> docQryScore = new ArrayList<DocScore>();
//					for(Document d : relevantDocs){
//						String str = String.format("%.2f", d.getDocumentScore());						
//						DocScore ds = new DocScore(d.getDocumentId(), str);
//						docQryScore.add(ds);						
//					}		
//					docQryScores.add(docQryScore);
//					
//					// now check the returned docs against the relevant docs
//			         int numMatching = 0;
//			         List<Integer> deemedRelevant = new ArrayList<Integer>();
//			         deemedRelevant.addAll(q.getRelevantDocs());
//			         int counter = 1;
//			         for(Document d : relevantDocs){
//			        	 if(!deemedRelevant.isEmpty()){
//			        		 if(deemedRelevant.contains(d.getDocumentId())){
//			        			 numMatching++;
//				        		 // here we populate the precision and recall figures for this query
//				        		 double prec = ((double)numMatching)/counter;
//				        		 q.addToPrecisionList((int)(prec*100));
//				        		 double rec = ((double)numMatching)/q.getRelevantDocs().size();
//				        		 q.addToRecallList((int)(rec*100));
//				        		 deemedRelevant.remove(d.getDocumentId());
//				        		 
//				        	 } else {
//				        		 double prec = ((double)numMatching)/counter;
//				        		 q.addToPrecisionList((int)(prec*100));
//				        		 double rec = ((double)numMatching)/q.getRelevantDocs().size();
//				        		 q.addToRecallList((int)(rec*100));
//			        		 }
//			        	 }
//			        	++counter;
//			         } 
//				}
//				
//			} else {
//				System.out.println("failed "+path);
//			}
//			
//			List<Map<String, Integer>> precisionList 
//				= getPrecisionRecallResults(0);
//			
//			qryTestTime = System.currentTimeMillis();
//
//			long buildIndexTime = invIndexTime - starttime;
//			long testQueryTime = qryTestTime - invIndexTime;
//			
//			session.setAttribute("IndexBuildTime", buildIndexTime);
//			session.setAttribute("TestQueryTime", testQueryTime);
//			session.setAttribute("queryNum", 1);
//			session.setAttribute("precisionList", precisionList);
//			session.setAttribute("docQryScores", docQryScores.get(0));
//			
//			url = "/PrecisionRecallGraphs.jsp";
			
		} else if(userPath.equals("/nextqueryprecisionrecall")){
			
			String queryNum = request.getParameter("qrynumber");
			System.out.println("queryNum = "+queryNum);
			int qNum = 1 + Integer.parseInt(queryNum);
			if(qNum > queries.size()){
				qNum = qNum-1;
			}
			
			List<Map<String, Integer>> precisionList 
				= getPrecisionRecallResults(qNum);

			session.setAttribute("queryNum", qNum);
			session.setAttribute("precisionList", precisionList);
			session.setAttribute("docQryScores", docQryScores.get(qNum));
			
			url = "/PrecisionRecallGraphs.jsp";
			
		} else if(userPath.equals("/prevqueryprecisionrecall")){
			
			String queryNum = request.getParameter("qrynumber");
			System.out.println("queryNum = "+queryNum);
			int qNum = Integer.parseInt(queryNum) - 1;
			if(qNum < 0){
				qNum = 0;
			}			
			List<Map<String, Integer>> precisionList = getPrecisionRecallResults(qNum);
			
			session.setAttribute("queryNum", qNum);
			session.setAttribute("precisionList", precisionList);
			session.setAttribute("docQryScores", docQryScores.get(qNum));
						
			url = "/PrecisionRecallGraphs.jsp";
			
			
			
			
		} else if(userPath.equals("/upload")){			
			     
			starttime = System.currentTimeMillis();
//			long qryTestTime = 0;
			
			final Part allPart = request.getPart("allfile");			     
			File all = saveFile(allPart);

			final Part qryPart = request.getPart("qryfile");			     
			File qry = saveFile(qryPart);

			final Part relPart = request.getPart("relfile");			     
			File rel = saveFile(relPart);

			// build a new inverted Index for this collection				
			DocumentBuilder db = new DocumentBuilder(all.getPath(), stopwordTree);
			FilesProcessor filesProcessor = new FilesProcessor();
			invertedIndex = filesProcessor.getInvertedIndex(db.getDocuments());
			documentMap = filesProcessor.getDocumentMap();
			documents = filesProcessor.getDocuments();

			invIndexTime = System.currentTimeMillis();

			QueryRelevanceBuilder qrb = new QueryRelevanceBuilder(qry.getPath(), rel.getPath());
			queries = qrb.getQueries();

			// submit each query and note the results
			for (Query q: queries){

				resetScores();

				RankedIR vsIR = new RankedIR();
				List<Document> relevantDocs = vsIR.getBM25RelevantDocuments(q.getQueryString(), invertedIndex);

				List<DocScore> docQryScore = new ArrayList<DocScore>();
				for(Document d : relevantDocs){
					String str = String.format("%.2f", d.getDocumentScore());						
					DocScore ds = new DocScore(d.getDocumentId(), str);
					docQryScore.add(ds);						
				}		
				docQryScores.add(docQryScore);

				// now check the returned docs against the relevant docs
				int numMatching = 0;
				List<Integer> deemedRelevant = new ArrayList<Integer>();
				deemedRelevant.addAll(q.getRelevantDocs());
				int counter = 1;
				for(Document d : relevantDocs){
					if(!deemedRelevant.isEmpty()){
						if(deemedRelevant.contains(d.getDocumentId())){
							numMatching++;
							// here we populate the precision and recall figures for this query
							double prec = ((double)numMatching)/counter;
							q.addToPrecisionList((int)(prec*100));
							double rec = ((double)numMatching)/q.getRelevantDocs().size();
							q.addToRecallList((int)(rec*100));
							deemedRelevant.remove(d.getDocumentId());

						} else {
							double prec = ((double)numMatching)/counter;
							q.addToPrecisionList((int)(prec*100));
							double rec = ((double)numMatching)/q.getRelevantDocs().size();
							q.addToRecallList((int)(rec*100));
						}
					}
					++counter;
				} 
			}


			List<Map<String, Integer>> precisionList 
			= getPrecisionRecallResults(0);

			qryTestTime = System.currentTimeMillis();

			long buildIndexTime = invIndexTime - starttime;
			long testQueryTime = qryTestTime - invIndexTime;

			session.setAttribute("IndexBuildTime", buildIndexTime);
			session.setAttribute("TestQueryTime", testQueryTime);
			session.setAttribute("queryNum", 1);
			session.setAttribute("precisionList", precisionList);
			session.setAttribute("docQryScores", docQryScores.get(0));
				
				url = "/PrecisionRecallGraphs.jsp";
		}

		try {
			
			request.getRequestDispatcher(url).forward(request, response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	
	private ArrayList<Map<String, Integer>> getPrecisionRecallResults(int qNum){
		
		ArrayList<Map<String, Integer>> precisionList = new ArrayList<>();
		
		for (int i = 0; i< queries.get(qNum).getRecallList().size(); ++i){
			Map<String, Integer> precValue = new HashMap<String, Integer>();
			precValue.put("rec", queries.get(qNum).getRecallList().get(i));
			precValue.put("prec", queries.get(qNum).getPrecisionList().get(i));
			precisionList.add(precValue);
		}		
		return precisionList;		
	}
	
	private void resetScores(){
		for (Document doc : documents){
			doc.setDocumentScore(0.0);
		}
	}
	

	private String getFileName(final Part part) {
	    final String partHeader = part.getHeader("content-disposition");
	    LOGGER.log(Level.INFO, "Part Header = {0}", partHeader);
	    for (String content : part.getHeader("content-disposition").split(";")) {
	        if (content.trim().startsWith("filename")) {
	            return content.substring(
	                    content.indexOf('=') + 1).trim().replace("\"", "");
	        }
	    }
	    return null;
	}
	
	private File saveFile(Part filePart){
		final String fileName = getFileName(filePart);
	     InputStream filecontent = null;
	     FileOutputStream out = null;
	     File qry = null;
	    
	     try {
	         out = new FileOutputStream(qry = new File(uploadPath + File.separator
	                 + fileName));
	         filecontent = filePart.getInputStream();
	         //qry = stream2file(filecontent);
	         int read = 0;
	         final byte[] bytes = new byte[1024];

	         while ((read = filecontent.read(bytes)) != -1) {
	             out.write(bytes, 0, read);
	         }
//	         writer.println("New file " + fileName + " created at " + path);
//	         LOGGER.log(Level.INFO, "File{0}being uploaded to {1}", 
//	                 new Object[]{fileName, path});
	     } catch (Exception fne) {

	         LOGGER.log(Level.SEVERE, "Problems during file upload. Error: {0}", 
	                 new Object[]{fne.getMessage()});
	     } finally {

	         if (filecontent != null) {
	             try {
					filecontent.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	         }
	         if (out != null) {
	             try {
					out.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	         }
	     }
	     return qry;
	}
	
//    public File stream2file (InputStream in) throws IOException {
//        final File tempFile = File.createTempFile("temp", ".tmp");
//        //tempFile.deleteOnExit();
//        try (FileOutputStream out = new FileOutputStream(tempFile)) {
//            IOUtils.copy(in, out);
//        }
//        return tempFile;
//    }
}
