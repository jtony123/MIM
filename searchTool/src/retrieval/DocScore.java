/**
 * 
 */
package retrieval;

/**
 * @author Anthony Jackson
 * @id 11170365
 *
 */
public class DocScore {

	private int docId;
	private String score;
	
	public DocScore(int id, String sc){
		this.docId = id;
		this.score = sc;
	}

	/**
	 * @return the docId
	 */
	public int getDocId() {
		return docId;
	}

	/**
	 * @param docId the docId to set
	 */
	public void setDocId(int docId) {
		this.docId = docId;
	}

	/**
	 * @return the score
	 */
	public String getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(String score) {
		this.score = score;
	}
	
	
	
}
