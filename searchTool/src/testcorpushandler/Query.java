package testcorpushandler;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Anthony Jackson
 * @id 11170365
 *	4BCT
 */
public class Query {

	private int queryNum;
	private String queryString = "";
	private List<Integer> relevantDocs = new ArrayList<Integer>();
	private int numMatches;
	private double precision;
	private List<Integer> precisionList = new ArrayList<Integer>();
	private double recall;
	private List<Integer> recallList = new ArrayList<Integer>();
	
	public Query(){
		
		
	}

	/**
	 * @return the queryNum
	 */
	public int getQueryNum() {
		return queryNum;
	}

	
	
	/**
	 * @return the precisionList
	 */
	public List<Integer> getPrecisionList() {
		return precisionList;
	}
	
	public void addToPrecisionList(int d){
		precisionList.add(d);
	}

	/**
	 * @param precisionList the precisionList to set
	 */
	public void setPrecisionList(List<Integer> precisionList) {
		this.precisionList = precisionList;
	}

	/**
	 * @return the recallList
	 */
	public List<Integer> getRecallList() {
		return recallList;
	}
	
	public void addToRecallList(int d){
		recallList.add(d);
	}

	/**
	 * @param recallList the recallList to set
	 */
	public void setRecallList(List<Integer> recallList) {
		this.recallList = recallList;
	}

	/**
	 * @param queryNum the queryNum to set
	 */
	public void setQueryNum(int queryNum) {
		this.queryNum = queryNum;
	}

	/**
	 * @return the queryString
	 */
	public String getQueryString() {
		return queryString;
	}

	/**
	 * @param queryString the queryString to set
	 */
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	/**
	 * @return the relevantDocs
	 */
	public List<Integer> getRelevantDocs() {
		return relevantDocs;
	}

	/**
	 * @param relevantDocs the relevantDocs to set
	 */
	public void setRelevantDocs(List<Integer> relevantDocs) {
		this.relevantDocs = relevantDocs;
	}
	
	public void addToRelevantDocs(Integer i){
		relevantDocs.add(i);
	}

	
	/**
	 * @return the numMatches
	 */
	public int getNumMatches() {
		return numMatches;
	}

	/**
	 * @param numMatches the numMatches to set
	 */
	public void setNumMatches(int numMatches) {
		this.numMatches = numMatches;
	}

	/**
	 * @return the precision
	 */
	public double getPrecision() {
		return precision;
	}

	/**
	 * @param precision the precision to set
	 */
	public void setPrecision(double precision) {
		this.precision = precision;
	}

	/**
	 * @return the recall
	 */
	public double getRecall() {
		return recall;
	}

	/**
	 * @param recall the recall to set
	 */
	public void setRecall(double recall) {
		this.recall = recall;
	}
	
	public String toString(){
		
		String toReturn = queryNum + ": " + queryString + " ->> ";
		for(int i : relevantDocs){
			toReturn += i + ", ";
		}
		return toReturn;		
	}	
}
