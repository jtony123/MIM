package fileprocessor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anthony Jackson
 * @id 11170365
 *	4BCT
 */

public class Document {

	private String documentName;
	private Integer documentId;
	private String filePath;
	private String fullText;
	private String processedText;
	private List<String> documentTokens;
	private int wordCount;
	private double normtf;
	private double tfidf;
	private Double vectorLength = 0.0;
	private Double documentScore = 0.0;	
	
	/**
	 * @return the vectorLength
	 */
	public Double getVectorLength() {
		return vectorLength;
	}
	
	public void incrementVectorLength(double increment){
		vectorLength += Math.pow(increment, 2);
	}
	
	public void adjustVectorLength(double adjustment){
		// take off previous squared component
		vectorLength -= Math.pow(adjustment-1.0, 2);
		// replace it with current tf
		incrementVectorLength(adjustment);
	}

	/**
	 * @param vectorLength the vectorLength to set
	 */
	public void setVectorLength(Double vectorLength) {
		this.vectorLength = vectorLength;
	}

	/**
	 * @return the documentScore
	 */
	public Double getDocumentScore() {
		return documentScore;
	}
	
	public void addToDocumentScore(Double tfidf){
		documentScore += tfidf;
	}

	/**
	 * @param documentScore the documentScore to set
	 */
	public void setDocumentScore(Double documentScore) {
		this.documentScore = documentScore;
	}

	public Document(){
		documentTokens = new ArrayList<String>();
		documentId = new Integer(0);
	}
	
	public void addToken(String token){
		documentTokens.add(token);
	}
	
	public String getDocumentName() {
		return documentName;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	
	/**
	 * @return the documentId
	 */
	public Integer getDocumentId() {
		return documentId;
	}

	/**
	 * @param documentId the documentId to set
	 */
	public void setDocumentId(Integer documentId) {
		this.documentId = documentId;
	}

	/**
	 * @param documentTokens the documentTokens to set
	 */
	public void setDocumentTokens(List<String> documentTokens) {
		this.documentTokens = documentTokens;
	}

	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFullText() {
		return fullText;
	}
	public void setFullText(String fullText) {
		this.fullText = fullText;
	}
	public String getProcessedText() {
		return processedText;
	}
	public void setProcessedText(String processedText) {
		this.processedText = processedText;
	}
	
	/**
	 * @return the documentTokens
	 */
	public List<String> getDocumentTokens() {
		return documentTokens;
	}
	/**
	 * @param documentTokens the documentTokens to set
	 */
	public void setDocumentTokens(ArrayList<String> documentTokens) {
		this.documentTokens = documentTokens;
	}
	public int getWordCount() {
		return wordCount;
	}
	public void setWordCount(int wordCount) {
		this.wordCount = wordCount;
	}

	/**
	 * @param termFrequency
	 */
	public void setNormalisedTf(Integer termFrequency) {

		this.normtf = termFrequency/this.vectorLength;
		
	}
	
	
	
	
	
	
}
