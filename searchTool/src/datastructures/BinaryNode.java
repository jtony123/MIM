package datastructures;import java.util.ArrayList;import java.util.List;/** * A class that represents nodes in a binary tree. *  * @author Frank M. Carrano * @version 2.0 */class BinaryNode<T> implements BinaryNodeInterface<T>, java.io.Serializable{  private static final long serialVersionUID = 6828929352995534482L; // needed for serializable objects    private T data;    // new attributes  private int documentFrequency;  private List<Integer> postings = new ArrayList<Integer>();    private BinaryNode<T> left;  private BinaryNode<T> right;    public BinaryNode()  {    this(null); // call next constructor  } // end default constructor    public BinaryNode(T dataPortion)  {	    this(dataPortion, null, null); // call next constructor  } // end constructor  public BinaryNode(T dataPortion, BinaryNode<T> leftChild,                                   BinaryNode<T> rightChild)  {	documentFrequency = 0;    data = dataPortion;    left = leftChild;    right = rightChild;  } // end constructor    public T getData()  {    return data;  } // end getData    public void setData(T newData)  {    data = newData;  } // end setData    /** * @return the documentFrequency */public int getDocumentFrequency() {	//return documentFrequency;	return postings.size();}/** * @param documentFrequency the documentFrequency to set */public void setDocumentFrequency(int documentFrequency) {	this.documentFrequency = documentFrequency;}public void incrementDocumentFrequency(){	++this.documentFrequency;}/** * @return the postings */public List<Integer> getPostings() {	return postings;}/** * @param postings the postings to set */public void setPostings(List<Integer> postings) {	this.postings = postings;}public BinaryNodeInterface<T> getLeftChild()  {    return left;  } // end getLeftChild  	public BinaryNodeInterface<T> getRightChild()	{		return right;	} // end getRightChild    public void setLeftChild(BinaryNodeInterface<T> leftChild)    {      left = (BinaryNode<T>)leftChild;    } // end setLeftChild  	public void setRightChild(BinaryNodeInterface<T> rightChild)	{		right = (BinaryNode<T>)rightChild;	} // end setRightChild		    public boolean hasLeftChild()    {      return left != null;    } // end hasLeftChild  	public boolean hasRightChild()	{		return right != null;	} // end hasRightChild	    public boolean isLeaf()    {      return (left == null) && (right == null);    } // end isLeaf    	// 26.06	public BinaryNodeInterface<T> copy()	{	  BinaryNode<T> newRoot = new BinaryNode<T>(data);	  	  if (left != null)	    newRoot.left = (BinaryNode<T>)left.copy();	    	  if (right != null)	    newRoot.right = (BinaryNode<T>)right.copy();	    	  return newRoot;		} // end copy	// 26.11	public int getHeight()	{	  return getHeight(this); // call private getHeight	} // end getHeight	// 26.11	private int getHeight(BinaryNode<T> node)	{	  int height = 0;	  	  if (node != null)	    height = 1 + Math.max(getHeight(node.left), getHeight(node.right));	                          	  return height;	} // end getHeight	// 26.11	public int getNumberOfNodes()	{	  int leftNumber = 0;	  int rightNumber = 0;	  	  if (left != null)	    leftNumber = left.getNumberOfNodes();	    	  if (right != null)	    rightNumber = right.getNumberOfNodes();	    	  return 1 + leftNumber + rightNumber;	} // end getNumberOfNodes} // end BinaryNode