/**
 * 
 */
package testcorpushandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Anthony Jackson
 * @id 11170365
 *
 */
public class QueryRelevanceBuilder {

	List<Query> queries = new ArrayList<Query>();
	private String queryFile;// = "C:\\a_med\\MED.QRY";
	private String reldocsFile;// = "C:\\a_med\\MED.REL";
	
	public QueryRelevanceBuilder(String queryFile, String relevantDocsFile){
		this.queryFile = queryFile;
		this.reldocsFile = relevantDocsFile;
		try {
			buildQueryRelevanceList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public List<Query> getQueries() {
		return queries;
	}

	public void setQueries(List<Query> queries) {
		this.queries = queries;
	}



	private void buildQueryRelevanceList() throws Exception{
		
		// this input stream reads the query file
		File file1 = new File(queryFile);
		FileInputStream inputStream1 = new FileInputStream(file1);
		InputStreamReader streamReader1 = new InputStreamReader(inputStream1, "UTF-8");
		BufferedReader reader1 = new BufferedReader(streamReader1);
		
		// this input stream reads the relevant documents list
		File file2 = new File(reldocsFile);
		FileInputStream inputStream2 = new FileInputStream(file2);
		InputStreamReader streamReader2 = new InputStreamReader(inputStream2, "UTF-8");
		BufferedReader reader2 = new BufferedReader(streamReader2);
				
		ArrayList<ArrayList<Integer>> lists = new ArrayList<ArrayList<Integer>>();
		int qNum = 1;
		int ctr = 1;
		String[] lineTokens = null;
		
		// put all the lists of relevant doc ids into a list
		ArrayList<Integer> templist = new ArrayList<Integer>();
		for (String line;(line = reader2.readLine()) != null;) {
			
			lineTokens = line.split("\\s");		        	 
       	 	qNum = Integer.parseInt(lineTokens[0]);
       	 	if (qNum == ctr) {
       	 		// when the number is the same
       	 		templist.add(Integer.parseInt(lineTokens[2]));
       	 	} else {
       	 		// when qnum jumps
       	 		lists.add(templist);
       	 		templist = new ArrayList<Integer>();
       	 		templist.add(Integer.parseInt(lineTokens[2])); 	 
       	 		++ctr;
       	 	}
		}
		lists.add(templist);		
		
		reader1.readLine();

		StringBuilder sb = new StringBuilder();
		Query query = new Query();
		query.setQueryNum(1);
		ctr =0;
		for (String line;(line = reader1.readLine()) != null;) {
			
		   if (line.startsWith(".I")) {
			   
			   query.setRelevantDocs(lists.get(ctr));	
			   query.setQueryString(sb.toString());
			   queries.add(query);
			   
			   sb = new StringBuilder();
				++ctr;			   
			   
			   String queryNum = line.substring(3).trim();
			   int num = Integer.parseInt(queryNum);
		        query = new Query();
		        query.setQueryNum(num);
		        
		    } else if(line.startsWith(".W")){
		    	// skip this line
		    } else {
		    	sb.append(line + " ");
		    }
		}
		query.setRelevantDocs(lists.get(ctr));
		query.setQueryString(sb.toString());
		queries.add(query);
		
		
		reader1.close();
		reader2.close();
		streamReader1.close();
		streamReader2.close();
		inputStream1.close();
		inputStream2.close();
	}
	
}
