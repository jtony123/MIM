package datastructures;

import java.util.ArrayList;
import java.util.List;

import fileprocessor.Document;

public class Posting {

	Document document;
	private Integer documentId;
	private Integer termFrequency;
	private double ntf;
	private List<Integer> termPositions;
	
	public Posting(){
		termPositions = new ArrayList<Integer>();
	}
	
	public Posting(Document document){
		this.document = document;
		termFrequency = 0;
		termPositions = new ArrayList<Integer>();
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

	/**
	 * 
	 */
	public void incrementTermFrequency(){
		++termFrequency;		
	}
	
	/**
	 * @return the ntf
	 */
	public double getNtf() {
		return ntf;
	}

	/**
	 * @param tf
	 * @param docVectorLength
	 */
	public void setNtf(double tf, double docVectorLength) {
		
		this.ntf = tf/docVectorLength;
	}
	
	/**
	 * @return the termPositions
	 */
	public List<Integer> getTermPositions() {
		return termPositions;
	}
	
	/**
	 * @param position
	 */
	public void addTermPosition(Integer position){
		termPositions.add(position);
	}	

    @Override public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof Posting) {
            Posting that = (Posting) other;
            result = (this.getDocumentId() == that.getDocumentId() && 
            		this.getDocument() == that.getDocument());
        }
        return result;
    }

    @Override public int hashCode() {
        return (41 * (41 + getDocumentId()));
    }
}
