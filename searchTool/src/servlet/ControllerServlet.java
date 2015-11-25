package servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
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

import datastructures.AVLTree;
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
 * @author Anthony Jackson
 * @id 11170365
 *	4BCT
 */

/**
 * Servlet implementation class ControllerServlet
 * 
 */
@WebServlet(name = "/ControllerServlet",
loadOnStartup = 1,
urlPatterns = {"/upload",
				"/booleansearch",
				"/rankedsearch",
				"/phrasesearch",
				"/bm25rankedsearch",
				"/bm25",
				"/startnewtest",
				"/nextqueryprecisionrecall",
				"/prevqueryprecisionrecall"
				})
@MultipartConfig
public class ControllerServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    private final static Logger LOGGER = 
            Logger.getLogger(ControllerServlet.class.getCanonicalName());

	List<Document> documents;
	Map<Integer, Document> documentMap;
	AVLTree<String> stopwordTree;
	AVLTree<String> invertedIndex;
	List<Query> queries;
	String uploadPath = null;

	List<List<DocScore>> docQryScores = new ArrayList<List<DocScore>>();

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
		
		uploadPath = config.getServletContext().getRealPath("") + "upload";
		// creates the directory if it does not exist
					File uploadDir = new File(uploadPath);
					if (!uploadDir.exists()) {
					    uploadDir.mkdir();
					}
		// build a balanced tree of stopwords.
		stopwordTree = new Stopwords().getAvlTree();
		//stopwordTree = new Stopwords(stopwordFile).getAvlTree();		

	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String userPath = request.getServletPath();
		String url = null;
		HttpSession session = request.getSession();
		session.setAttribute("relevantDocs", null);

		if (userPath.equals("/upload")) {
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
		
		
		if (userPath.equals("/booleansearch")) {
			
			String searchTerms = request.getParameter("searchterms");
			List<Document> matchingDocs = new ArrayList<Document>();
			BooleanIR booleanIR 
			= new BooleanIR(searchTerms, invertedIndex, documentMap.size());
				
			for(Integer i : booleanIR.eval()){
				matchingDocs.add(documentMap.get(i));
			}
			
			url = "/BooleanSearchPage.jsp";
			session.setAttribute("queryterms", searchTerms);
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
			List<Document> relevantDocs = 
					vsIR.getBM25RelevantDocuments(query, invertedIndex);

			session.setAttribute("relevantDocs", relevantDocs);
			url = "/BM25RankedSearchPage.jsp";
			

			
		} else if(userPath.equals("/nextqueryprecisionrecall")){
			
			String queryNum = request.getParameter("qrynumber");
			System.out.println("queryNum = "+queryNum);
			int qNum = 1 + Integer.parseInt(queryNum);
			if(qNum > queries.size()){
				qNum = qNum-1;
			}
			
			List<Map<String, Integer>> precisionList 
				= getPrecisionRecallResults(qNum);
			List<Map<String, Integer>> interPrecisionList
			= getInterpolatedPrecision(qNum);

			session.setAttribute("queryNum", qNum);
			session.setAttribute("precisionList", precisionList);
			session.setAttribute("interPrecisionList", interPrecisionList);
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
			List<Map<String, Integer>> interPrecisionList = getInterpolatedPrecision(qNum);
			
			session.setAttribute("queryNum", qNum);
			session.setAttribute("precisionList", precisionList);
			session.setAttribute("interPrecisionList", interPrecisionList);
			session.setAttribute("docQryScores", docQryScores.get(qNum));
						
			url = "/PrecisionRecallGraphs.jsp";
			
			
			
			
		} else if(userPath.equals("/upload")){			
			     
			starttime = System.currentTimeMillis();
			
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
							
			List<Map<String, Integer>> interPrecisionList = getInterpolatedPrecision(0);

			qryTestTime = System.currentTimeMillis();

			long buildIndexTime = invIndexTime - starttime;
			long testQueryTime = qryTestTime - invIndexTime;

			session.setAttribute("IndexBuildTime", buildIndexTime);
			session.setAttribute("TestQueryTime", testQueryTime);
			session.setAttribute("queryNum", 1);
			session.setAttribute("precisionList", precisionList);
			session.setAttribute("interPrecisionList", interPrecisionList);
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
	
	
	private ArrayList<Map<String, Integer>> getInterpolatedPrecision(int qNum){
		
		ArrayList<Map<String, Integer>> interPrecisionList = new ArrayList<>();
		int max =0;
		int index = 0;
		int size = queries.get(qNum).getPrecisionList().size();
		List<Integer> remaining = new ArrayList<Integer>();
		
		for(Integer i : queries.get(qNum).getPrecisionList()){
			remaining.add(i);
			if(i>max) {
				max=i;
				index = remaining.lastIndexOf(i);
			}
		}
		
		Map<String, Integer> precValue1 = new HashMap<String, Integer>();
		precValue1.put("rec", 0);
		precValue1.put("prec", max);
		interPrecisionList.add(precValue1);
		
		int prevRecall = 0;
		
		while (!remaining.isEmpty()){
			max =0;
			index = 0;
			// get max value precision value, and note its index and its recall			
			
			for(int r : remaining){
				if(r>max) {
					max=r;
					index = remaining.lastIndexOf(r);
				}				
			}
			int diff = remaining.size() - index;
			System.out.println("diff = " + diff + ", size = " + size + " subed = "+ (size-diff));
			
			Map<String, Integer> precValue = new HashMap<String, Integer>();
			precValue.put("rec", prevRecall);
			precValue.put("prec", queries.get(qNum).getPrecisionList().get(size-diff));
			interPrecisionList.add(precValue);				
			
			precValue = new HashMap<String, Integer>();
			prevRecall = queries.get(qNum).getRecallList().get(size-diff);
			precValue.put("rec", queries.get(qNum).getRecallList().get(size-diff));
			precValue.put("prec", queries.get(qNum).getPrecisionList().get(size-diff));
			interPrecisionList.add(precValue);
			
			for(int i = 0; i<index;++i){
				remaining.remove(0);
			}
			if(!remaining.isEmpty())remaining.remove(0);
		}		
		return interPrecisionList;	
		
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
	         int read = 0;
	         final byte[] bytes = new byte[1024];

	         while ((read = filecontent.read(bytes)) != -1) {
	             out.write(bytes, 0, read);
	         }

	     } catch (Exception e) {

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
	
}
