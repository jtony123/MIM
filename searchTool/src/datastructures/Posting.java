package datastructures;

import fileprocessor.Document;

public class Posting {

	Document document;
	private Integer documentId;
	private Integer termFrequency;
	private double ntf;
	
	
	
	public Posting(Document document){
		this.document = document;
		termFrequency = 0;
	}
	
	
	
	/**
	 * @return the nft
	 */
	public double getNtf() {
		return ntf;
	}

	/**
	 * @param double1 
	 * @param nft the nft to set
	 */
	public void setNtf(double tf, double docVectorLength) {
		
		this.ntf = tf/docVectorLength;
	}



	/**
	 * @return the document
	 */
	public Document getDocument() {
		return document;
	}

	/**
	 * @param document the document to set
	 */
	public void setDocument(Document document) {
		this.document = document;
	}




	public void incrementTermFrequency(){
		++termFrequency;		
	}


	/**
	 * @return the docuemtId
	 */
	public Integer getDocumentId() {
		return documentId;
	}
	/**
	 * @param docuemtId the docuemtId to set
	 */
	public void setDocumentId(Integer docuemtId) {
		this.documentId = docuemtId;
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
	
	
}
