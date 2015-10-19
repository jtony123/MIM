package datastructures;

public class Posting {

	private Integer docuemtId;
	private Integer termFrequency;
	private double tfidf;
	
	public Posting(){
		termFrequency = 0;
	}
	/**
	 * @return the docuemtId
	 */
	public Integer getDocumentId() {
		return docuemtId;
	}
	/**
	 * @param docuemtId the docuemtId to set
	 */
	public void setDocuemtId(Integer docuemtId) {
		this.docuemtId = docuemtId;
	}
	/**
	 * @return the termFrequency
	 */
	public Integer getTermFrequency() {
		return termFrequency;
	}
	/**
	 * @param termFrequency the termFrequency to set
	 */
	public void setTermFrequency(Integer termFrequency) {
		this.termFrequency = termFrequency;
	}
	
	public void incrementTermFrequency(){
		++termFrequency;
	}
	/**
	 * @return the tfidf
	 */
	public double getTfidf() {
		return tfidf;
	}
	/**
	 * @param tfidf the tfidf to set
	 */
	public void setTfidf(int documentCount, int documentFrequency) {		
		
		this.tfidf = calculateTfidf(documentCount, documentFrequency);
	}
	
	private double calculateTfidf(int documentCount, int documentFrequency){
		
		double x = ((double)documentCount)/documentFrequency;
		double idf = Math.log10(x);
		return this.termFrequency*idf;
	}
	
}
