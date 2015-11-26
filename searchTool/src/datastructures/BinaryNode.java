package datastructures;import java.util.ArrayList;import java.util.List;/** * A class that represents nodes in a binary tree. *  * @author Frank M. Carrano * @version 2.0 */class BinaryNode<T> implements BinaryNodeInterface<T>, java.io.Serializable{  private static final long serialVersionUID = 6828929352995534482L; // needed for serializable objects    private T data;    // new attributes  private int documentFrequency;  private double idf;  private double idfBM25;  private List<Posting> postings = new ArrayList<Posting>();    private BinaryNode<T> left;  private BinaryNode<T> right;    public BinaryNode()  {    this(null); // call next constructor  } // end default constructor    public BinaryNode(T dataPortion)  {	    this(dataPortion, null, null); // call next constructor  } // end constructor  public BinaryNode(T dataPortion, BinaryNode<T> leftChild,                                   BinaryNode<T> rightChild)  {	//documentFrequency = 0;	idf = 0.0;    data = dataPortion;    left = leftChild;    right = rightChild;  } // end constructor    public T getData()  {    return data;  } // end getData    public void setData(T newData)  {    data = newData;  } // end setData    /** * @return the documentFrequency */public int getDocumentFrequency() {	//return documentFrequency;	return postings.size();}/** * @return the idf */public double getIdf() {	return idf;}/** * @param idf the idf to set */public void setIdf(int N) {	this.idf = Math.log10((double)N/(double)postings.size());}/** * @return the idfBM25 */public double getIdfBM25() {	return idfBM25;}/** * @param idfBM25 the idfBM25 to set */public void setIdfBM25(int N) {		double result = Math.log(((double)(N - this.getDocumentFrequency() + 0.5))/			(double)(this.getDocumentFrequency() + 0.5));		this.idfBM25= Math.max(result, 0.1);}/** * @return the postings */public List<Posting> getPostings() {	return postings;}public List<Integer> getPostingsDocumentIds(){	List<Integer> l = new ArrayList<Integer>();	for(Posting p : this.getPostings()){		l.add(p.getDocumentId());	}	return l;}/** * @param postings the postings to set */public void setPostings(List<Posting> postings) {	this.postings = postings;}public BinaryNodeInterface<T> getLeftChild()  {    return left;  } // end getLeftChild  	public BinaryNodeInterface<T> getRightChild()	{		return right;	} // end getRightChild    public void setLeftChild(BinaryNodeInterface<T> leftChild)    {      left = (BinaryNode<T>)leftChild;    } // end setLeftChild  	public void setRightChild(BinaryNodeInterface<T> rightChild)	{		right = (BinaryNode<T>)rightChild;	} // end setRightChild		    public boolean hasLeftChild()    {      return left != null;    } // end hasLeftChild  	public boolean hasRightChild()	{		return right != null;	} // end hasRightChild	    public boolean isLeaf()    {      return (left == null) && (right == null);    } // end isLeaf    	// 26.06	public BinaryNodeInterface<T> copy()	{	  BinaryNode<T> newRoot = new BinaryNode<T>(data);	  	  if (left != null)	    newRoot.left = (BinaryNode<T>)left.copy();	    	  if (right != null)	    newRoot.right = (BinaryNode<T>)right.copy();	    	  return newRoot;		} // end copy	// 26.11	public int getHeight()	{	  return getHeight(this); // call private getHeight	} // end getHeight	// 26.11	private int getHeight(BinaryNode<T> node)	{	  int height = 0;	  	  if (node != null)	    height = 1 + Math.max(getHeight(node.left), getHeight(node.right));	                          	  return height;	} // end getHeight	// 26.11	public int getNumberOfNodes()	{	  int leftNumber = 0;	  int rightNumber = 0;	  	  if (left != null)	    leftNumber = left.getNumberOfNodes();	    	  if (right != null)	    rightNumber = right.getNumberOfNodes();	    	  return 1 + leftNumber + rightNumber;	} // end getNumberOfNodes} // end BinaryNode