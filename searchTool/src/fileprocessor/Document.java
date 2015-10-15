package fileprocessor;

import java.util.ArrayList;
import java.util.List;

public class Document {

	private String documentName;
	private Integer documentId;
	private String filePath;
	private String fullText;
	private String processedText;
	private List<String> documentTokens;
	private int wordCount;
	
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
	
	
	
	
	
	
}
