package fileprocessor;

public class Document {

	private String documentName;
	private String filePath;
	private String fullText;
	private String processedText;
	private int wordCount;
	public String getDocumentName() {
		return documentName;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
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
	public int getWordCount() {
		return wordCount;
	}
	public void setWordCount(int wordCount) {
		this.wordCount = wordCount;
	}
	
	
	
	
	
	
}
